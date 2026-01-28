<%--
  Created by IntelliJ IDEA.
  User: alirezavahedi
  Date: 22.09.23
  Time: 01:05
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
<jsp:include page="WEB-INF/include/header.jsp"/>

<div class="container">
    <h2 class="form-signin-heading">Login with OAuth 2.0</h2>
    <table class="table table-striped">
        <c:forEach items="${oidcLoginLinks}" var="oidcLoginLink">
            <tr>
                <td>
                    <a href="<c:out value='${oidcLoginLink.key}'/>"><c:out value='${oidcLoginLink.value}'/></a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
