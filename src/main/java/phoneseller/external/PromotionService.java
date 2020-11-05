
package phoneseller.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="promotion", url="${api.url.promotion}")
public interface PromotionService {

    @RequestMapping(method= RequestMethod.POST, path="/promotions")
    public void payCancel(@RequestBody Promotion promotion);

}