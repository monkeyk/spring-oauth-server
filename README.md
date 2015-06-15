# spring-oauth-server
Deep Integrate Spring Security &amp; Oauth2

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
</ol>


