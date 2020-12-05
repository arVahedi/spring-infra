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
    <title>Login</title>
</head>
<body>
<form method="post" action="./authenticate">
    <input name="username" id="username" placeholder="Username">
    <br>
    <input name="password" id="password" type="password" placeholder="Password">
    <br>
    <button type="submit">Login</button>
</form>
</body>
</html>
