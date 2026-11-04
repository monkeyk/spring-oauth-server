package com.monkeyk.sos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWKS 外部化配置属性。
 * <p>
 * 支持三种加载优先级（由高到低）：
 * <ol>
 *   <li>环境变量 / 系统属性 {@code sos.jwks.value} —— 适合 Vault / K8s Secret 注入</li>
 *   <li>外部文件路径 {@code sos.jwks.file-path} —— 适合挂载卷或运维管理</li>
 *   <li>类路径文件 {@code sos.jwks.classpath} —— 默认回退到 classpath:jwks.json</li>
 * </ol>
 * <p>
 * 生产环境强烈建议使用优先级 1 或 2，并将 {@code jwks.json} 从源码中移除。
 *
 * @author Shengzhao Li
 * @since 3.1.0
 */
@ConfigurationProperties(prefix = "sos.jwks")
public class JwkProperties {


    /**
     * JWKS JSON 内容，直接注入。
     * <p>
     * 优先级最高，适合通过 Vault、K8s Secret、环境变量注入。
     * 示例：{@code export SOS_JWKS_VALUE='{"keys":[...]}' }
     */
    private String value;

    /**
     * 外部 JWKS 文件的绝对路径。
     * <p>
     * 优先级次之，适合 Docker/K8s 挂载卷。
     * 示例：{@code /etc/sos/jwks.json}
     */
    private String filePath;

    /**
     * 类路径下的 JWKS 文件名。
     * <p>
     * 优先级最低，作为开发/测试环境的默认回退。
     * 默认值：{@code jwks.json}
     */
    private String classpath = "jwks.json";


    public JwkProperties() {
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }


    /**
     * 判断是否配置了直接注入的 JWKS 值。
     */
    public boolean hasValue() {
        return value != null && !value.isBlank();
    }

    /**
     * 判断是否配置了外部文件路径。
     */
    public boolean hasFilePath() {
        return filePath != null && !filePath.isBlank();
    }
}
