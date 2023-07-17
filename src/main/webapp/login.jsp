<%--
  Created by IntelliJ IDEA.
  User: Gl4di4tor
  Date: 6/8/2020 AD
  Time: 01:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <script src="js/jquery-3.7.0.min.js"></script>
    <title>Login</title>
</head>
<body>
<jsp:include page="WEB-INF/include/header.jsp"/>

<form id="LoginForm" onsubmit="return false">
    <h1>Login Form</h1>
    <div class="FormRow">
        <label for="Username">Username:</label>
        <input type="text" size="15" id="Username" name="Username">
    </div>
    <div class="FormRow">
        <label for="Password">Password: </label>
        <input type="password" size="15" id="Password" name="Password">
    </div>
    <div class="FormRow" id="LoginButtonDiv">
        <input type="button" value="Login" id="LoginBtn">
    </div>
    <div id="BadLogin">
        <p>The login information you entered does not match
            an account in our records. Please try again.</p>
    </div>
</form>

<script src="js/login.js"></script>
</body>
</html>
