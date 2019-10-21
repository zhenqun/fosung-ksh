package meeting;


import com.fosung.ksh.aiface.AiFaceApplication;
import com.fosung.ksh.aiface.service.impl.AiFaceServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.fosung"})
@SpringBootTest(classes = AiFaceApplication.class)
public class AiFaceApplicationTest {


    @Autowired
    AiFaceServiceImpl aiFaceService;


    @Test
    public void create() throws TencentCloudSDKException {

        aiFaceService.create("测试1",
                "3341300171270011117",
                "http://visual.fosung.com:91/app/attachment/download/3346481692780005499");
        aiFaceService.create("测试2",
                "3341300192744844843",
                "http://visual.fosung.com:91/app/attachment/download/3346482128719189405");
        aiFaceService.create("测试3",
                "3341300265759287842",
                "http://visual.fosung.com:91/app/attachment/download/3346482403597100493");
    }

}
