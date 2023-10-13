package com.monkeyk.sos.domain.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 2023/10/13 10:28
 *
 * @author Shengzhao Li
 * @since 3.0.0
 */
class GuidGeneratorTest {

    @Test
    void generate() {

        String generate = GuidGenerator.generate();
        assertNotNull(generate);
//        System.out.println(generate);
    }
}