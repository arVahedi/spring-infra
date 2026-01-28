<%--
  Created by IntelliJ IDEA.
  User: Gl4di4tor
  Date: 6/6/2020 AD
  Time: 01:24
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
    <title>User Management</title>
</head>
<body>
<jsp:include page="WEB-INF/include/header.jsp"/>

<c:forEach items="${userList}" var="user">
    <c:out value='${user.firstName}'/> <c:out value='${user.lastName}'/><br>
</c:forEach>
</body>
</html>
