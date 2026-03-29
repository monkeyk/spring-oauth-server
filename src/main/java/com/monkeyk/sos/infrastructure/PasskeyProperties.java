package com.monkeyk.sos.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Passkey/WebAuthn 配置项。
 * <p>
 * 用于统一解析 rpId、rpName 与 origin。
 *
 * @author Shengzhao Li
 * @since 3.0.1
 */
@Component
public class PasskeyProperties {

    /**
     * WebAuthn 的 RP ID。
     */
    @Value("${sos.webauthn.rp-id:}")
    private String rpId;

    /**
     * WebAuthn 的 RP 展示名称。
     */
    @Value("${sos.webauthn.rp-name:Spring OAuth Server}")
    private String rpName;

    /**
     * 浏览器请求来源地址。
     */
    @Value("${sos.webauthn.origin:}")
    private String origin;

    /**
     * 优先读取配置中的 rpId；未配置时回退为当前请求域名。
     */
    public String resolveRpId(HttpServletRequest request) {
        return StringUtils.isNotBlank(rpId) ? rpId : request.getServerName();
    }

    /**
     * 获取 RP 名称。
     */
    public String resolveRpName() {
        return rpName;
    }

    /**
     * 优先读取显式配置的 origin；未配置时根据当前请求自动拼装。
     */
    public String resolveOrigin(HttpServletRequest request) {
        if (StringUtils.isNotBlank(origin)) {
            return origin;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme()).append("://").append(request.getServerName());
        int port = request.getServerPort();
        boolean appendPort = ("http".equalsIgnoreCase(request.getScheme()) && port != 80)
                || ("https".equalsIgnoreCase(request.getScheme()) && port != 443);
        if (appendPort) {
            builder.append(':').append(port);
        }
        return builder.toString();
    }
}
