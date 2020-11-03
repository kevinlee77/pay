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
            this.setProcess("Payed");
            PayCompleted payCompleted = new PayCompleted();
            BeanUtils.copyProperties(this, payCompleted);
            System.out.println(payCompleted.toJson());
            payCompleted.publishAfterCommit();


            try {
                Thread.currentThread().sleep((long) (400 + Math.random() * 220));
                System.out.println("***** 결재 완료 *****");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } else if("Order Cancel".equals(process)) {
            System.out.println("***** 결재 취소 중 *****");
            this.setProcess("Pay Cancelled");
            PayCancelled payCancelled = new PayCancelled();
            BeanUtils.copyProperties(this, payCancelled);
            payCancelled.publishAfterCommit();
            System.out.println("***** 결재 취소 완료 *****");
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
