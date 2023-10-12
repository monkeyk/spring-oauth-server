package com.monkeyk.sos;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


/**
 * @since 2.0.0
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class SpringOauthServerApplicationTests {

    @Test
    public void contextLoads() {
    }

}
