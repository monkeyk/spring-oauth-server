<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Home</title>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" scope="application"/>

</head>
<body>
<h2>Spring Security&Oauth2 is work!</h2>

<p>
    <a href="${contextPath}/login.jsp">Login</a>
    &nbsp;|&nbsp;
    <a href="${contextPath}/logout.do">Logout</a>
</p>

<div>
    操作说明:
    <ol>
        <li>
            <p>
                菜单 User 是不需要Oauth 验证即可访问的(即公开的resource); 用于管理用户信息(添加,删除等).
            </p>
        </li>
        <li>
            <p>
                菜单 Unity 与 Mobile 需要Oauth 验证后才能访问(即受保护的resource); <br/>
                Unity 需要 [ROLE_UNITY] 权限, Mobile 需要 [ROLE_MOBILE] 权限.
            </p>
        </li>
        <li>
            <p>
                在项目的 others目录里有 <a
                    href="http://git.oschina.net/shengzhao/spring-oauth-server/blob/master/others/oauth_test.txt"
                    target="_blank">oauth_test.txt</a>文件, 里面有测试的URL地址(包括浏览器与客户端的),<br/>
                若想访问 Unity 与 Mobile, 则先用基于浏览器的测试URL 访问,等验证通过后即可访问(注意不同的账号对应的权限).
            </p>
        </li>
        <li>
            <p>
                若需要自定义<code>client_details</code>数据并进行测试, 可进入<a href="client_details">client_details</a>去手动添加<code>client_details</code>或删除已创建的<code>client_details</code>.
            </p>
        </li>
    </ol>
</div>
<br/>
菜单
<ul>
    <li>
        <a href="${contextPath}/user/overview">User</a>
    </li>
    <li>
        <a href="${contextPath}/unity/dashboard">Unity</a>
    </li>
    <li>
        <a href="${contextPath}/m/dashboard">Mobile</a>
    </li>
</ul>
</body>
</html>