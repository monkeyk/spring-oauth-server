package com.monkeyk.sos.service.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 浏览器登录时使用的凭据描述对象。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
public class CredentialDescriptorDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4987064789438732933L;

    /**
     * 固定值：public-key。
     */
    private String type = "public-key";

    /**
     * 凭据 ID，Base64URL 编码。
     */
    private String id;

    /**
     * 凭据传输方式。
     */
    private String transports;

    public CredentialDescriptorDto() {
    }

    public CredentialDescriptorDto(String id, String transports) {
        this.id = id;
        this.transports = transports;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransports() {
        return transports;
    }

    public void setTransports(String transports) {
        this.transports = transports;
    }
}
