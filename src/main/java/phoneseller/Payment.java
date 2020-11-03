package phoneseller;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
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
    public void onPostPersist(){
        System.out.println("***** 결재 처리 중 *****");
        System.out.println(getId());
        System.out.println(getProcess());

        if("Ordered".equals(process)) {
            System.out.println("***** 결재 진행 중 *****");

            PayCompleted payCompleted = new PayCompleted();
            payCompleted.setId(getId());
            payCompleted.setOrderId(getOrderId());
            payCompleted.setProcess("Payed");
            BeanUtils.copyProperties(this, payCompleted);
            payCompleted.publishAfterCommit();

            System.out.println("***** 결재 완료 *****");

        } else {
            System.out.println("***** 결재 취소 중 *****");

            PayCancelled payCancelled = new PayCancelled();
            payCancelled.setId(getId());
            payCancelled.setOrderId(getOrderId());
            payCancelled.setProcess("Payment Cancelled");
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
