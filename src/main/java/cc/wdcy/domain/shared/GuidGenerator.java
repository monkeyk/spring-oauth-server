package cc.wdcy.domain.shared;

import java.util.UUID;

/**
 * @author Shengzhao Li
 */
public abstract class GuidGenerator {


    /**
     * private constructor
     */
    private GuidGenerator() {
    }

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}