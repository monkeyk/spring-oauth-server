package com.monkeyk.sos.domain.shared;


import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

/**
 * @author Shengzhao Li
 */
public abstract class GuidGenerator {


//    private static RandomValueStringGenerator defaultClientSecretGenerator = new RandomValueStringGenerator(32);


    /**
     * private constructor
     */
    private GuidGenerator() {
    }

    /**
     * generate random number, length 32
     *
     * @return number
     * @since 3.0.0
     */
    public static String generateNumber() {
        return RandomStringUtils.random(32, false, true);
    }


    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateClientSecret() {
        return RandomStringUtils.random(32, true, true);
    }

}