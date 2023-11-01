package com.monkeyk.sos;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * @author Shengzhao Li
 */

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class ContextTest extends AbstractTransactionalJUnit4SpringContextTests {


    @BeforeTransaction
    public void before() throws Exception {

    }


}