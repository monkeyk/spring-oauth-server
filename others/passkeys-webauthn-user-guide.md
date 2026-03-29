# Passkeys（WebAuthn）使用手册

本文档用于说明本项目中 `Passkeys / WebAuthn` 功能的使用方式、部署要求、注意事项以及常见问题。

适用版本：`spring-oauth-server 3.0.1+`

---

## 1. 功能说明

本项目已支持浏览器侧的 `Passkeys（通行密钥）/ WebAuthn` 能力，可用于实现：

- 用户在浏览器中为当前账号绑定一个或多个 Passkey
- 后续登录时，用户无需输入密码，仅输入用户名并使用设备认证即可登录
- 用户可在系统中查看、删除自己已绑定的 Passkey

当前实现特点：

- 登录方式为：`用户名 + Passkey`
- 注册 Passkey 前，用户需要先通过原有账号密码完成一次登录
- 登录成功后，系统仍沿用现有 Spring Security 会话机制

---

## 2. Passkey 是什么

`Passkey` 是基于 `WebAuthn` 标准的一种无密码认证方式。

用户在登录时，不再手工输入密码，而是调用设备自身的安全能力完成认证，例如：

- 指纹
- Face ID / 面容识别
- Windows Hello
- 系统 PIN
- 安全密钥（如外置 FIDO 设备）

对用户来说，Passkey 的体验通常是：

1. 浏览器弹出系统认证窗口
2. 用户完成指纹、人脸、PIN 或安全密钥验证
3. 浏览器将认证结果返回给服务端
4. 服务端校验通过后建立登录会话

---

## 3. 前置条件

### 3.1 服务端要求

- JDK：`17+`
- Maven：建议使用支持 Java 17 的版本
- 数据库：执行新增的 `user_passkey` 表结构

项目默认运行环境请参考：

- `pom.xml`
- `src/main/resources/application.properties`
- `others/database/initial_db.ddl`

### 3.2 浏览器要求

建议使用以下较新的浏览器版本：

- Chrome / Edge 最新稳定版
- Safari 最新稳定版
- Firefox 较新版本

同时，浏览器所在设备需要支持 WebAuthn/Passkey 的认证能力。

### 3.3 运行域名要求

WebAuthn 必须运行在“受信任上下文”中，通常要求：

- 生产环境使用 `HTTPS`
- 本地开发环境可使用 `localhost` 或可信的回环地址

建议：

- 开发环境：优先使用 `http://localhost:8080`
- 生产环境：必须使用 `https://your-domain`

---

## 4. 数据库准备

新增的 Passkey 数据表定义位于：

- `others/database/initial_db.ddl`

新增表：

- `user_passkey`

该表用于保存：

- 绑定用户 `user_guid`
- Passkey 对应的 `credential_id`
- 公钥 `public_key`
- 公钥算法 `public_key_algorithm`
- 签名计数器 `sign_count`
- 设备标签 `label`
- 传输方式 `transports`
- 最后使用时间 `last_used_time`

如果是已有数据库升级，请至少执行新增表语句，而不是整库重建。

---

## 5. 配置说明

Passkey 相关配置位于：

- `src/main/resources/application.properties`

可选配置项如下：

```properties
# Passkeys(WebAuthn)
#sos.webauthn.rp-id=127.0.0.1
#sos.webauthn.origin=http://127.0.0.1:${server.port}
#sos.webauthn.rp-name=Spring OAuth Server
```

说明如下：

### 5.1 `sos.webauthn.rp-id`

Relying Party ID，通常应与访问域名一致或为其上级有效域。

示例：

- 本地：`localhost`
- 生产：`auth.example.com` 或 `example.com`

### 5.2 `sos.webauthn.origin`

浏览器发起 WebAuthn 的来源地址，必须与前端实际访问地址完全一致。

示例：

- `http://localhost:8080`
- `https://auth.example.com`

### 5.3 `sos.webauthn.rp-name`

Passkey 注册时展示给用户看的站点名称。

---

## 6. 页面入口

当前实现新增了以下入口：

### 6.1 登录页 Passkey 登录入口

登录页：

- `/login`

页面中新增：

- `Passkey Login`

用于：

- 输入用户名
- 调起浏览器 Passkey 认证
- 无密码登录

### 6.2 Passkey 管理页

首页登录后新增菜单：

- `Passkeys`

页面地址：

- `/passkeys`

用于：

- 注册当前设备为 Passkey
- 查看当前账号已注册的 Passkey
- 删除指定 Passkey

---

## 7. 用户如何注册 Passkey

### 7.1 注册流程

首次使用时，请按如下步骤操作：

1. 使用原有用户名密码登录系统
2. 登录成功后进入首页
3. 点击菜单 `Passkeys`
4. 打开 Passkey 管理页 `/passkeys`
5. 在 `Register Passkey` 区域输入一个设备标签（可选）
6. 点击 `Register Current Device`
7. 浏览器会调用系统认证能力
8. 用户完成指纹、人脸、PIN 或安全密钥确认
9. 注册成功后，页面会提示成功并自动刷新

### 7.2 设备标签建议

建议给不同设备设置易识别的标签，例如：

- `MacBook Chrome`
- `iPhone Safari`
- `Office Windows Hello`
- `USB Security Key`

便于后续在管理页中区分和删除。

---

## 8. 用户如何使用 Passkey 登录

### 8.1 登录流程

当用户已经绑定过 Passkey 后，可按如下方式无密码登录：

1. 打开登录页 `/login`
2. 在 `Passkey Login` 区域输入用户名
3. 点击 `Login with Passkey`
4. 浏览器会拉取当前用户可用的 Passkey 列表
5. 浏览器弹出系统认证窗口
6. 用户完成指纹、人脸、PIN 或安全密钥确认
7. 认证通过后，系统自动建立登录会话并跳转到原目标页面或首页

### 8.2 当前登录方式的限制

当前实现不是“纯 Passkey discoverable login”，因此：

- 仍需要先输入用户名
- 系统会根据用户名加载该账号已绑定的 Passkey 列表

这意味着当前功能更准确地说是：

- `无密码登录`
- 但不是 `免输用户名登录`

---

## 9. 如何删除 Passkey

用户可以在 `/passkeys` 页面删除已注册的 Passkey。

操作步骤：

1. 登录系统
2. 进入 `Passkeys` 页面
3. 在 `Registered Passkeys` 列表中找到目标记录
4. 点击 `Delete`

删除后：

- 该设备/凭据将不再可用于 Passkey 登录
- 若用户删除了所有 Passkey，则只能继续使用账号密码登录

建议：

- 至少保留一种可用登录方式
- 不要在未验证新设备可用前删除唯一可用的 Passkey

---

## 10. 服务端校验内容

当前实现中，服务端会对以下内容进行校验：

- `challenge` 是否匹配
- `origin` 是否匹配
- `rpId` 是否匹配
- `authenticatorData` 中用户在场标记是否有效
- 签名是否与保存的公钥匹配
- `signCount` 是否单调增加

其目的是尽量避免：

- 重放攻击
- 域名不一致导致的伪造请求
- 凭据被替换或伪造

---

## 11. 生产环境注意事项

### 11.1 必须使用 HTTPS

生产环境中，必须使用 `HTTPS`。

否则常见问题包括：

- 浏览器直接拒绝调用 WebAuthn
- Passkey 注册失败
- Passkey 登录失败

### 11.2 `origin` 与 `rp-id` 必须匹配

如果浏览器访问的是：

- `https://auth.example.com`

则服务端配置的：

- `origin` 必须与实际访问地址一致
- `rp-id` 必须是有效匹配值

如果配置错误，常见表现为：

- 注册时报 `origin mismatch`
- 登录时报 `rpId mismatch`

### 11.3 反向代理场景

如果应用部署在 Nginx、Ingress、SLB 等反向代理之后，请确保：

- 浏览器访问地址与后端感知的外部地址一致
- 如果项目依赖请求头推导 origin，请正确转发协议与主机信息

最稳妥的方式是直接显式配置：

- `sos.webauthn.origin`
- `sos.webauthn.rp-id`

### 11.4 账号恢复机制

Passkey 只是登录方式之一，建议系统运营时保留恢复机制，例如：

- 管理员重置登录方式
- 保留用户名密码登录
- 邮件/短信等辅助恢复能力

否则用户更换设备后，可能无法自行恢复访问。

---

## 12. 用户使用建议

建议用户：

- 至少绑定两个可用设备或两种登录方式
- 为不同设备设置清晰标签
- 设备更换前先确认新设备已可正常登录
- 不要在公用电脑上随意保存或绑定长期凭据

---

## 13. 常见问题

### 13.1 为什么我点击注册没有反应？

请优先检查：

- 浏览器版本是否过旧
- 当前页面是否在可信上下文中运行
- 当前设备是否支持 WebAuthn/Passkey

### 13.2 为什么本地开发建议用 `localhost`？

因为 WebAuthn 对运行上下文有较高要求。

虽然某些回环地址也可能可用，但为了减少浏览器差异，开发时更推荐：

- `http://localhost:8080`

### 13.3 为什么登录时还要输入用户名？

因为当前实现采用的是：

- `用户名 + Passkey`

服务端需要先知道用户是谁，再返回该用户允许使用的凭据列表。

### 13.4 为什么删除 Passkey 后无法无密码登录？

因为 Passkey 凭据已从服务端移除。

如果账号没有其他 Passkey，后续只能改用原有密码登录，或重新绑定新的 Passkey。

### 13.5 更换域名后为什么原来的 Passkey 不能用了？

因为 WebAuthn 与 `RP ID / Origin` 强绑定。

常见情况：

- 原来在 `localhost` 上注册
- 现在切换为 `127.0.0.1` 或新的正式域名

这通常需要用户在新域名下重新注册 Passkey。

---

## 14. 建议的测试方式

### 14.1 本地开发测试

建议：

1. 使用 JDK17 启动项目
2. 初始化数据库并执行 `user_passkey` 表结构
3. 使用浏览器访问 `http://localhost:8080/login`
4. 用已有账号密码登录
5. 进入 `/passkeys` 绑定当前设备
6. 退出登录
7. 返回 `/login` 使用 Passkey 登录

### 14.2 回归检查点

建议测试以下内容：

- 绑定 Passkey 成功
- 同一账号可绑定多个 Passkey
- 登录成功后跳转正确
- 删除后该 Passkey 不再可用
- 错误域名配置时有明确报错

---

## 15. 安全与限制说明

当前实现定位是：

- 为现有系统增加浏览器侧 Passkey 能力
- 支持常见的无密码登录场景

当前版本的边界包括：

- 主要支持浏览器环境
- 当前登录流程仍要求输入用户名
- 更偏向“用户手工绑定 + 用户自行管理”的场景

如果后续需要，还可以继续扩展为：

- 免输用户名的 discoverable credentials 登录
- 更完整的 attestation 策略校验
- 后台管理端查看用户 Passkey
- 风险控制、设备信任评级、审计日志

---

## 16. 相关代码位置

如需进一步了解实现，可查看以下文件：

- `src/main/java/com/monkeyk/sos/web/controller/PasskeyController.java`
- `src/main/java/com/monkeyk/sos/service/impl/PasskeyServiceImpl.java`
- `src/main/java/com/monkeyk/sos/infrastructure/PasskeyUtils.java`
- `src/main/resources/templates/login.html`
- `src/main/resources/templates/passkeys/manage.html`
- `others/database/initial_db.ddl`

---

## 17. 总结

对于普通用户：

- 先用密码登录一次
- 到 `Passkeys` 页面绑定设备
- 后续即可在登录页使用 `Login with Passkey` 实现无密码登录

对于部署人员：

- 确保数据库已增加 `user_passkey` 表
- 确保运行在 `JDK17+`
- 确保 `origin / rp-id / https` 配置正确

如果你的部署环境是正式生产环境，建议优先完成：

- HTTPS 配置
- 域名与 RP 参数固定
- 账号恢复方案设计

