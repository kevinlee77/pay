package phoneseller;

import org.springframework.beans.factory.annotation.Autowired;
import phoneseller.config.kafka.KafkaProcessor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{

    @Autowired
    PaymentRepository paymentRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCancelled_PayCancel(@Payload OrderCancelled orderCancelled){

        if(orderCancelled.isMe()) {
            for (Payment payment : paymentRepository.findAll()) {
                if (payment.getOrderId() == orderCancelled.getId()) {
                    System.out.println("이 주문 건이 결제 목록에서 결제 취소 되어야해ㅏㅁ~~~!");
                    System.out.println(payment.toString());
                    System.out.println(orderCancelled.toJson());
                    payment.setProcess("OrderCancelled");
                    paymentRepository.save(payment);

//                    payment.setProcess("OrderCancelled");
//                    payment.setPrice(orderCancelled.getPrice() * (-1));
//                    paymentRepository.save(payment);
                }
            }
        }
    }

//    @StreamListener(KafkaProcessor.INPUT)
//    public void wheneverPayCompleted_OrderStatus(@Payload PayCompleted payCompleted){
//        System.out.println("app_policy_paycompleted_status");
//        System.out.println(payCompleted.toJson());
//        if(payCompleted.isMe()){
//            if(orderRepository.findById(payCompleted.getOrderId()) != null){
//                System.out.println("====================================결제완료====================================");
//                Order order = orderRepository.findById(payCompleted.getOrderId()).get();
//                System.out.println(payCompleted.getProcess());
//                order.setStatus("Payed");
//                System.out.println(payCompleted.toJson());
//                orderRepository.save(order);
//            }
//
//        }
//
//    }

}
