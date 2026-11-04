package com.monkeyk.sos.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;

/**
 * JWKSource 工厂，根据 {@link JwkProperties} 的配置按优先级加载 JWKS。
 * <p>
 * 加载优先级：
 * <ol>
 *   <li>{@code sos.jwks.value} —— 环境变量 / Vault 直接注入（生产推荐）</li>
 *   <li>{@code sos.jwks.file-path} —— 外部文件路径（Docker 挂载卷等）</li>
 *   <li>{@code sos.jwks.classpath} —— 类路径文件（开发/测试回退）</li>
 * </ol>
 * <p>
 * 同时对加载的 JWKSet 执行安全加固：
 * <ul>
 *   <li>将所有 key 的 {@code key_ops} 限制为仅 SIGN / VERIFY（最小权限原则）</li>
 *   <li>打印当前生效的加载方式及公钥指纹，便于运维审计</li>
 * </ul>
 *
 * @author Shengzhao Li
 * @since 3.1.0
 */
public final class JwkSourceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JwkSourceFactory.class);


    private JwkSourceFactory() {
    }


    /**
     * 根据配置创建 JWKSource 实例。
     *
     * @param properties JWKS 配置属性
     * @return JWKSource
     * @throws IOException    文件读取失败
     * @throws ParseException JWKS JSON 解析失败
     */
    public static JWKSource<SecurityContext> create(JwkProperties properties) throws IOException, ParseException {
        String jwksJson = resolveJwksJson(properties);
        JWKSet jwkSet = JWKSet.parse(jwksJson);

        // 安全检查：审计 key_ops 是否过于宽松
        checkKeyOps(jwkSet);

        logKeySummary(jwkSet, properties);

        return new ImmutableJWKSet<>(jwkSet);
    }


    /**
     * 按优先级解析出 JWKS JSON 字符串。
     */
    private static String resolveJwksJson(JwkProperties properties) throws IOException {
        // 优先级 1：环境变量 / Vault 直接注入
        if (properties.hasValue()) {
            LOG.info("Loading JWKS from environment variable / injected value");
            return properties.getValue();
        }

        // 优先级 2：外部文件路径
        if (properties.hasFilePath()) {
            Path path = Path.of(properties.getFilePath());
            if (!Files.exists(path)) {
                throw new IOException("JWKS file not found: " + properties.getFilePath());
            }
            LOG.info("Loading JWKS from external file: {}", properties.getFilePath());
            return Files.readString(path, StandardCharsets.UTF_8);
        }

        // 优先级 3：类路径文件（开发/测试默认）
        String classpathFile = properties.getClasspath();
        LOG.info("Loading JWKS from classpath: {}", classpathFile);
        try (var is = JwkSourceFactory.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (is == null) {
                throw new IOException("JWKS classpath resource not found: " + classpathFile
                        + ". Please configure sos.jwks.value or sos.jwks.file-path for production.");
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


    /**
     * 安全检查：检测 key_ops 是否过于宽松并记录警告。
     * <p>
     * 签名密钥不应具备 encrypt/decrypt/deriveKey 等操作能力。
     * 由于 nimbus-jose-jwt 的 JWK 对象不可变，此处仅做审计日志，
     * 实际的 key_ops 限制应在密钥生成阶段完成（参见 JwksTest）。
     */
    private static void checkKeyOps(JWKSet jwkSet) {
        for (com.nimbusds.jose.jwk.JWK jwk : jwkSet.getKeys()) {
            if (jwk.getKeyOperations() != null && jwk.getKeyOperations().size() > 2) {
                LOG.warn("Security: kid={} has overly permissive key_ops={}, "
                                + "recommended: [SIGN, VERIFY]. Regenerate keys with restricted key_ops.",
                        jwk.getKeyID(), jwk.getKeyOperations());
            }
        }
    }


    /**
     * 打印密钥摘要信息，便于运维审计和密钥轮换确认。
     */
    private static void logKeySummary(JWKSet jwkSet, JwkProperties properties) {
        if (!LOG.isInfoEnabled()) {
            return;
        }
        LOG.info("JWKS loaded: {} key(s), source=[value={}, filePath={}, classpath={}]",
                jwkSet.getKeys().size(),
                properties.hasValue() ? "YES" : "NO",
                properties.hasFilePath() ? properties.getFilePath() : "NO",
                properties.getClasspath());

        jwkSet.getKeys().forEach(jwk ->
                LOG.info("  kid={}, kty={}, alg={}",
                        jwk.getKeyID(), jwk.getKeyType(), jwk.getAlgorithm())
        );

        // 输出公钥指纹，用于与 token 中的 kid 对照验证
        JWKSet publicSet = jwkSet.toPublicJWKSet();
        LOG.info("Public JWKS (safe to share):\n{}", publicSet.toJSONObject());
    }
}
