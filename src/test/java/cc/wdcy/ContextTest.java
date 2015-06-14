package cc.wdcy;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * @author Shengzhao Li
 */
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
public abstract class ContextTest extends AbstractTransactionalTestNGSpringContextTests {

    @BeforeTransaction
    public void beforeTest() {

    }
}