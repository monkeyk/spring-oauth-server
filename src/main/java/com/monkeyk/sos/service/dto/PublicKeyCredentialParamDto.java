package com.monkeyk.sos.service.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 注册 Passkey 时可接受的公钥算法参数。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class PublicKeyCredentialParamDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2447419548726954138L;

    /**
     * 固定值：public-key。
     */
    private String type = "public-key";

    /**
     * 公钥算法编号。
     */
    private int alg;

    public PublicKeyCredentialParamDto() {
    }

    public PublicKeyCredentialParamDto(int alg) {
        this.alg = alg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAlg() {
        return alg;
    }

    public void setAlg(int alg) {
        this.alg = alg;
    }
}
