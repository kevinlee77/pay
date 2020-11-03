package phoneseller;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Entity
@Table(name="Payment_table")
public class Payment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private Double price;
    private String process;

    @PostPersist
    public void onPrePersist(){
        System.out.println("***** 결재 처리 중 *****");
        System.out.println(getId());
        System.out.println(getOrderId());
        System.out.println(getProcess());

        if("Ordered".equals(process)) {
            System.out.println("***** 결재 진행 중 *****");

            PayCompleted payCompleted = new PayCompleted();
            payCompleted.setId(getId());
            payCompleted.setOrderId(getOrderId());
            payCompleted.setProcess("Payed");
            BeanUtils.copyProperties(this, payCompleted);
            payCompleted.publishAfterCommit();


            //바로 이벤트를 보내버리면 주문정보가 커밋되기도 전에 배송발송됨 이벤트가 발송되어 주문테이블의 상태가 바뀌지 않을 수 있다.
            // TX 리스너는 커밋이 완료된 후에 이벤트를 발생하도록 만들어준다.
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void beforeCommit(boolean readOnly) {
                    System.out.println("published");
                    payCompleted.publish();
                }
            });

            System.out.println("***** 결재 완료 *****");

//            try {
//                Thread.currentThread().sleep((long) (400 + Math.random() * 220));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


        } else if("Order Cancel".equals(process)) {
            System.out.println("***** 결재 취소 중 *****");

            PayCancelled payCancelled = new PayCancelled();
            payCancelled.setId(getId());
            payCancelled.setOrderId(getOrderId());
            payCancelled.setProcess("Pay Cancelled");
            BeanUtils.copyProperties(this, payCancelled);
            payCancelled.publishAfterCommit();
        }

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }




}
