package phoneseller;

import org.springframework.beans.factory.annotation.Autowired;
import phoneseller.config.kafka.KafkaProcessor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
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
            System.out.println("##### listener PayCancelled : " + orderCancelled.toJson());
            System.out.println("pay_policy_wheneverOrderCancelled_PayCancel");
            System.out.println((paymentRepository.findByOrderId(orderCancelled.getId()).toString()));

            Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderCancelled.getId());
            Payment payment = paymentOptional.get();
            payment.setProcess("OrderCancelled");
            paymentRepository.save(payment);

//            for (Payment payment : paymentRepository.findAll()) {
//                if (payment.getOrderId() == orderCancelled.getId()) {
//                    System.out.println("이 주문 건이 결제 목록에서 결제 취소 되어야해ㅏㅁ~~~!");
//                    System.out.println(orderCancelled.toJson());
//
//                    payment.setProcess("OrderCancelled");
////                    payment.setPrice(orderCancelled.getPrice() * (-1));
//                    System.out.println(payment.toString());
//                    paymentRepository.save(payment);
////                    paymentRepository.deleteById(payment.getId());
//                    System.out.println("paymentrepo delete");
//                }
//            }
        }
    }

}
