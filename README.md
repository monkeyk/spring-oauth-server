# spring-oauth-server
Deep Integrate Spring Security &amp; OAuth2

<p>
    Latest <a href="http://git.oschina.net/shengzhao/spring-oauth-server">http://git.oschina.net/shengzhao/spring-oauth-server</a>
</p>

<p>
    spring-oauth-server is based on
    <a href="https://github.com/spring-projects/spring-security-oauth/tree/master/spring-security-oauth2">spring-security-oauth2</a>,
    but we do more useful extension as follow
</p>
<ol>
    <li><p>Split Spring MVC configuration(wdcy-servlet.xml) and Oauth(security.xml) configuration</p></li>
    <li><p>Save Spring Security User data to database</p></li>
    <li><p>Save ClientDetails to database, and manage it</p></li>
    <li><p>Extend ClientDetails fields, add <code>trusted</code> field for check the client is trust or not</p></li>
    <li><p>Cancel unnecessary configuration</p></li>
    <li><p>Different resources config different roles</p></li>
    <li><p>access_token, code save to database(replace save to memory)</p></li>
    <li><p>Add Restful OAuth API, see <code>OAuthRestController</code></p></li>
</ol>

<hr/>

<div>
    <h3>Dependency Framework, version</h3>
    <ul>
        <li>JDK (1.8.0_40)</li>
        <li>Servlet API (3.1.0)</li>
        <li>Spring (4.1.6.RELEASE)</li>
        <li>Spring Security (4.0.1.RELEASE)</li>
        <li>spring-security-oauth2 (2.0.7.RELEASE)</li>
        <li>Log4j (1.2.14)</li>
        <li>MySQL (5.6.23)</li>
    </ul>
</div>

<hr/>

<div>
    <h3>How to use</h3>
    <ol>
        <li><p>
            Download or clone the project source code to local computer,
            make sure the computer install JAVA development environment(JDK,Maven,Tomcat,IDE...)
        </p></li>
        <li><p>
            Create MySql(5.0+) Database: <em>oauth2</em>, run SQL script files(position: /others/database),
            run script order: initial_db.ddl -> oauth.ddl -> initial_data.ddl.
        </p></li>
        <li><p>
            Config <code>spring-oauth-server.properties</code> (position: src/main/resources), update database
            connection information(username, password).
        </p></li>
        <li><p>
            Use Maven import local project to IDE(for example: Intellij IDEA), Add Servlet-Container-Server(Tomcat),
            startup the server(make sure set project contextPath = 'spring-oauth-server').
            <br/>
            Besides, use maven command <code>mvn package</code> compile the project(generate 'spring-oauth-server.war'),
            copy the war to Tomcat(make sure add 'spring-oauth-server.properties' to classpath) and startup.
        </p></li>
        <li><p>
            Visit <a href="https://github.com/monkeyk/spring-oauth-server/blob/master/others/oauth_test.txt">oauth_test.txt</a> (position: others)
            and testing step by step(Browser default URL: http://localhost:8080/spring-oauth-server).
        </p></li>
    </ol>
</div>

<hr/>

<div>
    <h3>Database table,column explain</h3>
    <p>
        <em>db_table_description.html</em>(position: /others) is explain the SQL file <code>oauth.ddl</code> all tables, columns,
        tell how to use and where use.
        <br/>
        Online visit address
        <a href="http://andaily.com/spring-oauth-server/db_table_description.html">http://andaily.com/spring-oauth-server/db_table_description.html</a>(Chinese).
    </p>
</div>


<hr/>
<div>
    <h3>More...</h3>
    <p>Project Blog address: <a href="http://andaily.com/blog/?cat=19">http://andaily.com/blog/?cat=19</a>(Chinese)</p>
    <p>More mysql open-source projects: <a href="http://andaily.com/my_projects.html">http://andaily.com/my_projects.html</a></p>
    <p>More help please contact: <a href="mailto:shengzhao@shengzhaoli.com">shengzhao@shengzhaoli.com</a></p>
    <p>项目中文GIT库地址: <a href="http://git.oschina.net/shengzhao/spring-oauth-server">http://git.oschina.net/shengzhao/spring-oauth-server</a></p>
    <p>Expect your joining...</p>
</div>
