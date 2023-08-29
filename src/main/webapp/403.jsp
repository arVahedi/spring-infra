<%--
  Created by IntelliJ IDEA.
  User: alirezavahedi
  Date: 01.08.23
  Time: 11:26
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
    <title>Account Info</title>
</head>
<body>
<jsp:include page="WEB-INF/include/header.jsp"/>

Access Denied !!!
<br>
Your IP is : <c:out value="${request_ip}"/>
</body>
</html>
