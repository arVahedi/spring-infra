<%@ page import="examples.domain.User" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Gl4di4tor
  Date: 6/6/2020 AD
  Time: 01:24
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>User Management</title>
</head>
<body>
<c:forEach items="${userList}" var="user">
    <c:out value='${user.firstName}' /> <c:out value='${user.lastName}' /><br>
</c:forEach>
</body>
</html>
