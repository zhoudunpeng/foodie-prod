import com.zdp.Application;
import com.zdp.service.StuService;
import com.zdp.service.TestTransService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author sesshomaru
 * @date 2021/4/22 21:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransTest {

    @Autowired
    private StuService stuService;

    @Autowired
    private TestTransService testTransService;

    @Test
    public void myTest() {
        testTransService.testPropagationTrans();
    }
}
