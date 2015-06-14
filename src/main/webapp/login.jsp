<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Oauth login</title>
</head>
<body>
<h2>Oauth login</h2>

<form action="${contextPath}/login.do" method="post">

    <label for="username">Username:</label>
    <input type="text" id="username" name="j_username" value="" required="required"/>
    <br/>
    <br/>
    <label for="password">Password:</label>
    <input type="password" name="j_password" id="password" value="" required="required"/>
    <br/>
    <input type="submit" value="Login" class="btn btn-primary"/>
    <span class="text-danger">
        <c:if test="${param.authorization_error eq 2}">Access denied !!!</c:if>
        <c:if test="${param.authentication_error eq 1}">Authentication Failure</c:if>
    </span>
</form>
<div>
    <p>You can use the users to login as follow:</p>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Username</th>
            <th>Password</th>
            <th>Privileges</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>admin</td>
            <td>admin</td>
            <td>All privileges, allow visit [Mobile] and [Unity] resources</td>
        </tr>
        <tr>
            <td>unity</td>
            <td>unity</td>
            <td>Only allow visit [Unity] resource, support grant_type:
                <em>authorization_code,refresh_token,implicit</em></td>
        </tr>
        <tr>
            <td>mobile</td>
            <td>mobile</td>
            <td>Only allow visit [Mobile] resource, support grant_type: <em>password,refresh_token</em></td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>