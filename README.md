## spring-oauth-server

<br/>
java config版本

<strong>Spring与OAuth2的整合示例. OIDC1.0 + OAuth2.1</strong>


项目用Maven管理
<br/>
Base on SpringBoot


使用的主要技术与版本号
<ol>
 <li>JDK (openjdk 17)</li>
 <li>Spring Boot(3.1.5)</li>
 <li>Spring Core(6.0.11)</li>
 <li>spring-security-oauth2-authorization-server (1.1.1)</li>
 <li>thymeleaf (3.1.1.RELEASE)</li>
</ol>

<hr/>
<h3>授权协议</h3>
<em><a href="https://gitee.com/shengzhao/spring-oauth-server/tree/master/LICENSE">GPL-2.0</a></em>

<hr/>
<h3>技术视频</h3>
<ol>
    <li> <a href="https://v.youku.com/v_show/id_XMzgyNjQwMjA4OA==.html?f=51900110&o=1">spring-oauth-server开源项目背景</a> 2018-09-16</li>
    <li> <a href="https://v.youku.com/v_show/id_XMzgyNzYzNjAzMg==.html?f=51900110&o=1">如何在本地运行spring-oauth-server(传统版本) </a> 2018-09-17</li>
    <li> <a href="https://v.youku.com/v_show/id_XMzg1NDM2NjU0MA==.html?f=51900110&o=1">如何在本地运行spring-oauth-server(config版本)</a> 2018-10-08</li>
    <li> <a href="https://v.youku.com/v_show/id_XMzg2Mjk0Mjk0MA==.html?f=51900110&o=1">在线测试环境的使用</a> 2018-10-14</li>
    <li> <a href="https://v.youku.com/v_show/id_XNDMwMTg4MTQ5Mg==.html?f=51900110&o=1">spring-oauth-server v2.0.1更新内容简介</a> 2019-08-05</li>
</ol>

> 注意：以上视频主要适用于v2.x版本

<a href="http://list.youku.com/albumlist/show/id_51900110.html" target="_blank">http://list.youku.com/albumlist/show/id_51900110.html</a>
(持续更新...)
<br/>
<p>
    1000 star Gitee奖杯: <a href="http://andaily.com/blog/wp-content/uploads/2019/09/sos-1000-stars.jpg" target="_blank">sos-1000-stars.jpg</a> [2019年]
</p>
<hr/>

<h3>版本分支介绍</h3>

- MySQL版本请访问Branch: <a href="https://gitee.com/shengzhao/spring-oauth-server/">master</a>

- MongoDB版本请访问Branch: <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/mongodb/">mongodb</a>

- Redis版本请访问Branch: <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/config-redis/">config-redis</a>

<hr/>

<h3>相关项目</h3>

1. OAuth2客户端项目请访问 <a href="https://gitee.com/mkk/spring-oauth-client">spring-oauth-client</a>

2. 在线测试访问地址 <a href="https://andaily.com/spring-oauth-server">https://andaily.com/spring-oauth-server</a> (v1.0)

3. Shiro与OLTU整合的OAuth2项目 <a href="https://gitee.com/mkk/oauth2-shiro">https://gitee.com/mkk/oauth2-shiro</a>
   (相比spring-oauth-server, 该项目入门门槛相对较低, 代码更加透明, 理解更容易,可扩展性更强, 且模块化开发)

<hr/>

<h3>如何使用?</h3>

1. 项目是Maven管理的, 需要本地安装Maven(开发用的maven版本号为3.6.0), 还有MySql(开发用的mysql版本号为5.7.22)

2. <a href="https://gitee.com/shengzhao/spring-oauth-server">下载</a>(或clone)项目源码到本地

3. 创建MySQL数据库(默认数据库名oauth2_boot), 并运行相应的SQL脚本(脚本文件位于others/database目录),
   运行脚本的顺序: <code>initial_db.ddl -> oauth.ddl -> initial_data.ddl</code>

4. 修改application.properties(位于src/main/resources目录)中的数据库连接信息(包括username, password等)

5. 将本地项目导入到IDE(如Intellij IDEA)中, 可直接运行<code>SpringOauthServerApplication.java</code>进行访问；或配置Tomcat(或类似的servlet运行服务器), 并启动Tomcat(默认端口为8080);
   也可通过maven package命令将项目编译为jar文件(spring-oauth-server.jar),
         使用命令<code>java -jar</code>启动访问.
        若使用<code>java -jar spring-oauth-server.jar</code>启动, 建议使用参数<em>spring.config.location</em>指定配置文件, 
        如：<code>java -jar spring-oauth-server.jar --spring.config.location=xxx.properties</code>

6. 参考<a href="https://gitee.com/shengzhao/spring-oauth-server/blob/config/others/oauth2.1-flow.md">oauth2.1-flow.md</a>(位于others目录)的内容并测试之(也可在浏览器中访问相应的地址,如: http://localhost:8080/ 在界面上操作).


<div>
    <h4>配置参数说明</h4>
    说明配置文件<em>application.properties</em>中的主要变量。
    <table>
        <thead>
            <tr><th>参数名</th><th>必须?</th><th>默认值</th><th>说明</th></tr>
        </thead>
        <tbody>
            <tr><td>spring.datasource.*</td><td>是</td><td>-</td><td>数据库连接相关配置</td></tr>
            <tr><td>spring.thymeleaf.*</td><td>是</td><td>-</td><td>Spring MVC thymeleaf相关配置</td></tr>
            <tr><td>server.port</td><td>是</td><td>8080</td><td>服务运行端口号</td></tr>
            <tr><td>spring.security.oauth2.authorizationserver.issuer</td><td>是</td><td></td><td>OAuth2 issuer, 生产环境配置对外访问完整地址</td></tr>
            <tr><td>spring.application.name</td><td>是</td><td></td><td>应用组件名称</td></tr>
        </tbody>
    </table>
</div>

<hr/>
<h3>grant_type 介绍</h3>

说明OAuth2.1支持的grant_type(授权方式)与功能
<ol>
    <li><code>authorization_code</code> -- 授权码模式(即先登录获取code,再获取token)</li>
    <li><code>authorization_code + PKCE</code> -- 授权码模式+PKCE (即先登录获取code, 请求时增加参数<em>code_challenge</em>与<em>code_challenge_method</em>; 再获取token,增加参数<em>code_verifier</em>)</li>
    <li><code>password</code> -- 密码模式(将用户名,密码传过去,直接获取token) <mark>OAuth2.1不推荐使用</mark></li>
    <li><code>refresh_token</code> -- 刷新access_token</li>
    <li><code>device_code</code> -- 适用于各类无输入键盘的物联网智能设备进行认证授权, 通过类似'扫码登录'形式完成整个流程 <strong>OAuth2.1新增</strong></li>
    <li><code>client_credentials</code> -- 客户端模式(无用户,用户向客户端注册,然后客户端以自己的名义向'服务端'获取资源)</li>
    <li><code>jwt-bearer</code> -- 增强client端请求安全性的断言(assertion)实现; 通过类似'双向SSL'的机制来让server端验证client端的签名实现强安全性.</li>
</ol>

> 注意：相比OAuth2.0，去掉了 **implicit** 模式

<hr/>
<h3>帮助与改进</h3>
<ol>
<li>
<p>
 与该项目相关的博客请访问 <a target="_blank" href="https://blog.csdn.net/monkeyking1987/article/details/16828059">https://blog.csdn.net/monkeyking1987/article/details/16828059</a>
</p>
</li>
<li>
<p>
 如果在使用过程中遇到特殊的问题(如:如何将oauth_code存入数据库),请访问项目的 <a href="https://gitee.com/shengzhao/spring-oauth-server/wikis/pages">Wiki</a> 
 与 <a href="https://gitee.com/shengzhao/spring-oauth-server/attach_files">附件</a>. 
 <br/>
 我会把大家反馈的问题解决办法添加在这里.
 <br/>
 若在这两个地方没有找到解决办法的,
 欢迎发邮件到<a href="mailto:sz@monkeyk.com">sz@monkeyk.com</a>一起讨论.
</p>
</li>

<li>
<p>
 如果在使用项目的过程中发现任何的BUG或者更好的提议, 建议将其提交到项目的 <a href="https://gitee.com/shengzhao/spring-oauth-server/issues">Issues</a> 中, 
 我会一直关注并不断改进项目. 
</p>
</li>
</ol>

<hr/>
<h3>功能扩展</h3>
<ol>
    <li>
        <code>oauth_code存入数据库的配置</code>,  请下载文件 <a href="https://git.oschina.net/shengzhao/spring-oauth-server/attach_files/download?i=4858&u=http%3A%2F%2Ffiles.git.oschina.net%2Fgroup1%2FM00%2F00%2F31%2FcHwGbFQXzC-AeseiAAfnNw23X70580.jpg%3Ftoken%3De81934223d99a0fddc02639017b568a6%26ts%3D1421151523%26filename%3Doauth_code%E5%AD%98%E5%85%A5%E6%95%B0%E6%8D%AE%E5%BA%93%E7%9A%84%E9%85%8D%E7%BD%AE.jpg">oauth_code存入数据库的配置.jpg</a>
    </li>
    <li>
        <code>改变token过期的时间的配置</code>, 请下载文件<a href="https://git.oschina.net/shengzhao/spring-oauth-server/attach_files/download?i=6589&u=http%3A%2F%2Ffiles.git.oschina.net%2Fgroup1%2FM00%2F00%2F43%2FcHwGbFRpuk6ANN2CAANJ-Rkiz_c649.jpg%3Ftoken%3D686e6d5b1e9ab04446dbfeb977c3b7a1%26ts%3D1421151523%26filename%3D%E6%94%B9%E5%8F%98token%E8%BF%87%E6%9C%9F%E7%9A%84%E6%97%B6%E9%97%B4%E7%9A%84%E9%85%8D%E7%BD%AE.jpg">改变token过期的时间的配置.jpg</a>
    </li>
    <li>
        <code>自定义 grant_type</code>, 默认情况支持的grant_type包括 [password,authorization_code,refresh_token,device_code], 若不需要其中的某些grant_type,
        则可以修改 oauth_client_details 表中的 authorized_grant_types 字段的值;
    </li>
    <li>
        <div>
            <code>如何刷新access_token(refresh_token)</code>, 在通过客户端(如移动设备)登录成功后返回的数据如下
            <br/>
            <pre>{"access_token":"eyJraWQiOiJteW9pZGMta2V5aWQiLCJhbGciOiJSUzI1...","token_type":"bearer","refresh_token":"UCFNxUj4ytr241KzwJJgnMno1RfmoLs0GKVxNWPjW5VZ7d4U4YsDM7...","expires_in":43199,"scope":"openid"}</pre>
            <br/>
            若需要刷新获取新的token(一般在 expires_in 有效期时间快到时), 请求的URL类似如下
            <br/>
            <pre>http://localhost:8080/oauth2/token?client_id=mobile-client&client_secret=mobile&grant_type=refresh_token&refresh_token=UCFNxUj4ytr241KzwJJgnMno1RfmoLs0GKVxNWPjW5VZ7</pre>
            <br/>
            注意: refresh_token 参数值必须与登录成功后获取的 refresh_token 一致, 且grant_type = refresh_token
            <br/>
            另: 刷新token 需要 ClientDetails 支持 refresh_token 类型的 grant_type (默认是支持的)
        </div>
    </li>
</ol>


<hr/>
<h3>开发计划</h3>
<p>
从 0.3版本开始将项目的所有计划的开发内容列出来, 方便大家跟进, 也欢迎你加入.
<br/>
项目的开发管理使用开源项目 <a href="https://gitee.com/mkk/andaily-developer">andaily-developer</a>.
</p>
<ul>    
	   <li>
            <p>
                Version: <strong>3.0.0</strong> [finished]
                <br/>
                Date: 2023-10-10 / 2023-10-31
            </p>
            <ol>
                <li><p>底层安全架构升级：jdk升级17, spring6.x, springboot3.x, thymeleaf替换servlet/jsp</p></li>     
                <li><p>全面升级支持 OAuth2.1协议与 OIDC1.0协议</p></li>     
                <li><p>构建包由war换成jar, SQL相应调整</p></li>     
                <li><p>用spring-security-oauth2-authorization-server升级替换spring-security-oauth2, 详见<a href="https://andaily.com/blog/?p=20077">背景说明</a></p></li>     
                <li><p>界面使用说明按OAuth2.1进行友好设计并更新各提示语句</p></li>     
                <li><p>增加spring-restdocs文档支持, 自动生成API相关文档</p></li>     
            </ol>
       </li>   	
	   <li>
            <p>
                Version: <strong>2.1.1</strong> [canceled]
                <br/>
                Date: 2022-05-05 / ---
            </p>
            <ol>
                <li><p>尝试升级替换spring-security-oauth2, 详见<a href="https://andaily.com/blog/?p=20077">背景说明</a></p></li>     
            </ol>
       </li>   	   
        <li>
            <p>
                Version: <strong>2.1.0</strong> [finished]
                <br/>
                Date: 2020-06-05 / 2022-05-01
            </p>
            <ol>
                <li><p><del>升级access_token, refresh_token为JWT(Json Web Token), 提高性能</del></p></li>    
                <li><p><del>增加灵活性配置参数<code>sos.reuse.refresh-token</code>可实时延长token时效(类似session机制)</del></p></li>                     
                <li><p><del>升级spring-security oauth2版本为2.3.8.RELEASE, 增加spring-security-jwt版本1.1.1.RELEASE</del></p></li>                        
            </ol>
            <br/>
       </li>   
	   <li>
            <p>
                Version: <strong>2.0.2</strong> [finished]
                <br/>
                Date: 2019-08-05 / 2020-06-04
            </p>
            <ol>
                <li><p><del>Fix CVE-2019-3778, use spring-security-oauth 2.3.5.RELEASE</del></p></li>             
                <li><p><del>解决数据库多条 access_token问题，增加唯一约束</del></p></li>             
                <li><p><del>升级Spring-Boot版本为2.1.4.RELEASE</del></p></li>             
            </ol>
            <br/>
       </li>  
       <li>
            <p>
                Version: <strong>2.0.1</strong> [finished]
                <br/>
                Date: 2018-05-01 / 2019-08-04
            </p>
            <ol>
                <li><p><del>增加使用代码生成AccessToken功能</del></p></li>
                <li><p><del>增加将AccessToken存入Redis的配置参考</del></p></li>
                <li><p><del>升级Spring Security OAuth版本为2.3.4.RELEASE</del></p></li>
                <li><p><del>修改ROLE的错误配置</del></p></li>
                <li><p><del>Use spring-boot 2.0.2.RELEASE</del></p></li>
                <li><p><del>Fix issue   #IJO9R  /oauth/rest_token 接口 client_secret字段没有校验</del></p></li>
                <li><p><del>将项目用视频方式展现出来，更直观</del></p></li>                
            </ol>
            <br/>
       </li> 
       <li>
            <p>
                Version: <strong>2.0.0</strong> [finished]
                <br/>
                Date: 2018-04-09 / 2018-04-21
            </p>
            <ol>
                <li><p><del>更新UI,为了更易理解与使用,场景化</del></p></li>
                <li><strong><del>使用 spring-boot 重构</del></strong></li>
            </ol>
       </li>
      <li>
           <p>
               Version: <strong>1.1</strong> [cancel]
               <br/>
               Date: 2018-10-14 / ---
           </p>
           <ol>
               <li><p>---</p></li>
           </ol>
      </li>
       <li>
            <p>
                Version: <strong>1.0</strong> [finished]
                <br/>
                Date: 2017-03-30 / 2018-04-04
            </p>
            <ol>
                <li><p><del>implicit测试时 取消掉 client secret</del></p></li>
                <li><p>更新UI,为了更易理解与使用,场景化</p></li>
                <li><p>增加删除access_token API</p></li>
                <li><p>增加删除 refresh_token API</p></li>
                <li><p><del>增加校验 access_token API: /oauth/check_token</del></p></li>
                <li><p><del>Fix ISSUE #IGNQ9  CustomJdbcTokenStore中的CacheEvict不起作用</del></p></li>
                <li><p>---</p></li>
            </ol>
       </li>
       <li>
            <p>
                Version: <strong>0.6</strong> [finished]
                <br/>
                Date: 2016-07-07 / 2016-10-13
            </p>
            <ol>
                <li><p><del>(150) - 修改OAUTH错误时返回JSON数据</del></p></li>
                <li><p><del>(151) - 数据添加Ehcache缓存支持</del></p></li>
                <li><p><del>(158) - 对配置,代码必要的地方添加注释,方便理解</del></p></li>
                <li><p><del>添加OIDC协议文档</del></p></li>
            </ol>
       </li>   
       <li>
            <p>
                Version: <strong>0.5</strong> [finished]
                <br/>
                Date: 2016-02-19 / 2016-05-24
            </p>
            <ol>
                <li><p><del>(118) - Add java-config(零配置) 的支持, 以及启用 新的注解, branch: config</del></p></li>
                <li><p><del>(138) - OAuth 'token' Restful API</del></p></li>
                <li><p><del>(139) - User Overview/ user add/archive</del></p></li>
                <li><p><del>(143) - Add project API document</del></p></li>
                <li><p><del>(144) - Add MongoDB branch</del></p></li>
            </ol>
            <br/>
       </li>
       <li>
            <p>
                Version: <strong>0.4</strong> [finished]
                <br/>
                Date: 2015-11-09 / 2015-11-30
            </p>
            <ol>
                <li><p><del>(97) - Fix custom access_token_validity,refresh_token_validity issue(#5)</del></p></li>
                <li><p><del>(109) - 升级 spring-security-oauth2 的版本到 2.0.6以上, 目前是1.0.5 </del></p></li>
                <li><p><del>(113) - Upgrade spring, spring security version to > 4.0</del></p></li>
                <li><p><del>将项目添加到在线测试服务器</del></p></li>
                <li><p><del>(115) - Sync update spring-oauth-client version with spring-oauth-server</del></p></li>
                <li><p><del>(116) - Remove mybatis dependency</del></p></li>
                <li><p><del>Upgrade JAVA to 1.8; Servlet 3.0</del></p></li>
                <li><p><del>Oauth table add index </del></p></li>
            </ol>
       </li>
       <li>
            <p>
                Version: <strong>0.3</strong> [finished]
                <br/>
                Date: 2015-05-14 / 2015-06-07
            </p>
            <ol>
                <li><p>#73 - Upgrade 'spring-security-oauth2' version to '2.0.6.RELEASE' (current: 1.0.5.RELEASE)      [CANCELED]</p></li>
                <li><p><del>#74 - oauth mysql ddl add create_time,  default is now()  </del></p></li>
                <li><div><del>#75 - Add user information API, for <a href="https://gitee.com/mkk/spring-oauth-client"><code>spring-oauth-client</code></a> project use
                    <pre>
                    URL: /unity/user_info
                    Login: Yes (ROLE_UNITY)
                    Data Format: JSON
                    URL: /m/user_info
                    Login: Yes (ROLE_MOBILE)
                    Data Format: JSON
                    </pre>
                    </del></div>
                </li>
                <li><p><del>#77 - User add Privilege domain.
                                  Addition initial two user: unityuser(ROLE_UNITY),mobileuser("ROLE_MOBILE).
                                  If default user, return all privilegs, otherwise return specify privilege(s) </del></p></li>
                <li><p><del>#78 - Initial 'sprint-oauth-client' project(maven), add sub-modules</del></p></li>
                <li><p><del>#91 - User log4j replace logback dependency </del></p></li>
                <li><p><del>#92 - Add database table column description. (添加数据库表的字段说明) </del></p></li>
                <li><p><del>#93 - 将默认的 oauth_code存入数据库(当前是存入内存) </del></p></li>
                <li><p><del> spring-oauth-server project add Bootstrap CSS  </del></p></li>
                <li><p><del>#95 - Add 'client-details' management; create/delete, show testing links</del></p></li>
            </ol>
       </li>
</ul>
<br/>

<hr/>
<h3>数据库表字段说明</h3>
<p>
    在0.3版本中添加了<code>db_table_description.html</code>文件(位于/others目录, 3.0.0版本db_table_description_3.0.0.html), 
用来说明数据库脚本文件<code>oauth.ddl</code>中各表,各字段的用途及使用场合.
    <br/>
    也可在线访问<a href="https://andaily.com/spring-oauth-server/db_table_description_3.0.0.html">db_table_description_3.0.0.html</a>.
</p>


<hr/>
<h3>Project Log</h3>
<div>
    <ol>
        <li><p><em>2013-11-19</em>     Initial project, start push code</p></li>
        <li><p><em>2013-11-20</em>     发布<a href="https://gitee.com/shengzhao/spring-oauth-server/tree/0.1/">0.1</a>版本</p></li>
        <li><p><em>2015-05-06</em>        发布<a href="http://www.oschina.net/news/62176/spring-oauth-server-0-2">0.2</a>版本</p></li>
        <li><p><em>2015-05-27</em>      创建项目博客,访问地址 <a href="http://andaily.com/blog/?cat=19">http://andaily.com/blog/?cat=19</a></p></li>
        <li><p><em>2015-06-07</em>        发布<a href="https://gitee.com/shengzhao/spring-oauth-server/tree/0.3/">0.3</a>版本</p></li>
        <li><p>
            <em>2015-06-16</em>       添加github访问: <a href="https://github.com/monkeyk/spring-oauth-server">https://github.com/monkeyk/spring-oauth-server</a>,
            以后的更新将同步github与gitosc.
        </p></li>
        <li><p><em>2015-11-09</em>        开始开发 0.4-beta 版本</p></li>
        <li><p><em>2015-11-18</em>        发布 <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/0.4-beta/">0.4-beta</a> 版本</p></li>
        <li><p><em>2016-01-02</em>        发布 <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/0.4/">0.4</a> 版本</p></li>
        <li><p><em>2016-02-19</em>        Add 0.5 version development planning</p></li>
        <li><p><em>2016-04-03</em>        Add <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/config/">config</a> branch</p></li>
        <li><p><em>2016-04-14</em>        Add <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/mongodb/">mongodb</a> branch</p></li>
        <li><p><em>2018-04-21</em>        使用spring-boot重构,增加2.0.0实现 <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/2.0.0/">2.0.0</a> </p></li>
        <li><p><em>2018-05-01</em>        开始 <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/config/">2.0.1</a> 分支开发</p></li>
        <li><p><em>2019-07-04</em>        Add Redis <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/config-redis/">config</a> branch</p></li>
        <li><p><em>2019-08-04</em>        发布 <a href="https://gitee.com/shengzhao/spring-oauth-server/tree/2.0.1/">2.0.1</a> 版本</p></li>
        <li><p><em>2020-06-04</em>        发布 2.0.2 版本</p></li>
        <li><p><em>2022-05-01</em>        发布 2.1.0 版本</p></li>
        <li><p><em>2023-10-10</em>        开始全新大版本 3.0.0 开发</p></li>
        <li><p><em>2023-10-31</em>        发布 3.0.0 全新版本</p></li>
    </ol>
</div>


<hr/>
<h3>更多资源</h3>
<p>以下是在学习工作中收集的更多关于OAuth2的资源,对深入理解与运用OAuth2有帮助</p>
<ul>
       <li>
            <p>
                <a href="https://tools.ietf.org/html/rfc6749">RFC 6749 - The OAuth 2.0 Authorization Framework</a>, OAuth2.0协议(英文)
            </p>
       </li>
       <li>
            <p>
                <a href="https://oauth.net/2/">OAuth 2.0 &mdash; OAuth</a>, OAuth2.0官方网站
            </p>
       </li>
       <li>
            <p>
                <a href="https://netment.iteye.com/blog/945402">OAuth2核心参数说明</a>, 重点介绍了grant_type 与 response_type 以及示例
            </p>
       </li>
       <li>
            <p>
                <a href="https://apiwiki.poken.com/authentication/oauth2">OAuth2 flows</a>, 详细介绍OAuth2的流程,各类错误发生时的响应
            </p>
       </li>
       <li>
            <p>
                <a href="https://www.oschina.net/translate/oauth-2-developers-guide">OAuth 2 开发人员指南（Spring security oauth2）</a>, 翻译OAuth 2 Developers Guide(spring security oauth2)
            </p>
       </li>
       <li>
            <p>
                <a href="https://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html">理解OAuth 2.0</a>, 介绍OAuth2各类grant_type的使用
            </p>
       </li>
       <li>
            <p>
                <a href="https://www.dannysite.com/blog/178/">OAuth2：隐式授权（Implicit Grant）类型的开放授权</a>, 介绍grant_type='implicit'模式
            </p>
       </li>
       <li>
            <p>
                <a href="https://oltu.apache.org/">Apache Oltu</a>, Java版的 OAuth2参考实现, 建议去了解了解
            </p>
       </li>
       <li>
            <p>
                <a href="https://andaily.com/blog/?p=440">OIDC–基于OAuth2的下一代身份认证授权协议</a>
            </p>
       </li>       
       <li>
            <p>
                <a href="https://andaily.com/blog/?p=19776">在spring-oauth-server中将AccessToken存入Redis的配置</a>
            </p>
       </li>       
       <li>
            <p>
                <a href="https://andaily.com/blog/?p=19793">如何通过代码生成AccessToken</a>
            </p>
       </li>
       <li>
            <p>
                <a href="https://andaily.com/blog/?p=19884">OAuth2中 access_token，refresh_token的各类配置与使用场景FAQ</a>
            </p>       
       </li>       
        <li>
            <p><a href="https://oauth.net/2.1/">OAuth2.1 介绍</a></p>       
       </li>       
        <li>
            <p><a href="https://andaily.com/blog/?p=20004">OAuth 2.1的状态与主要特征</a>, 个人总结</p>       
       </li>
        <li>
            <p><a href="https://openid.net/developers/how-connect-works/">OIDC官方网站</a></p>       
       </li>
</ul>

<br/>
<strong>
    与项目相关的技术文章请访问 <a href="https://andaily.com/blog/?cat=19">http://andaily.com/blog/?cat=19</a> (不断更新与OAuth2相关的文章)
</strong>
<div>
    <strong>问答与讨论</strong>
    <br/>
    与项目相关的，与OAuth2相关的问题与回答，以及各类讨论请访问<br/>
    <a href="https://andaily.com/blog/?dwqa-question_category=oauth">http://andaily.com/blog/?dwqa-question_category=oauth</a>
</div>


<hr/>
<h3>使用案例</h3>
<p>以下是已知的使用(或基于) spring-oauth-server 开源项目的各类商业项目(排名不分先后), 若你有案例希望添加, 请联系作者.</p>
<ul>
    <li><p>Hongkong Parkway Online (在线医疗服务系统)</p></li>
    <li><p>海尔日日平台 (B2B电商平台)</p></li>
    <li><p>wdcy-game (手机游戏服务端)</p></li>
    <li><p>Honyee Management System (企业管理系统)</p></li>
    <li><p>AoLin Open Platform (国际物流开发平台)</p></li>
    <li><p>IDS (移动安全产品)</p></li>
    <li><p>......</p></li>
</ul>


<hr/>
<div>
    <h3>捐助</h3>

    支付宝: monkeyking1987@126.com (**钊)

- 快意江湖 -- 100元
- yufan -- 100元
</div>

<hr/>
<h3>其他...</h3>
<p>
 关注更多开源项目请访问 <a href="https://andaily.com/my_projects.html">https://andaily.com/my_projects.html</a>
</p>

<p>
 <em>欢迎联系作者 <a href="mailto:sz@monkeyk.com">sz@monkeyk.com</a> 进行探讨</em>
</p>



