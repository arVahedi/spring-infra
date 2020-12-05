<%--
  Created by IntelliJ IDEA.
  User: Gl4di4tor
  Date: 11/24/2020 AD
  Time: 02:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="personal.project.springinfra.assets.AuthorityType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<security:authorize access="isAuthenticated()">
    <%--<security:authentication property="principal.authorities" var="authorities"/>--%>
    authenticated as <security:authentication property="principal.username"/>
    <c:set var="isAuthenticated" value="true"/>
</security:authorize>

<div>
    <a href="/">Home</a>
    <c:choose>
        <c:when test="${isAuthenticated}">
            <a href="/logout">Logout</a>
            <security:authorize access="hasRole('${AuthorityType.USER_MANAGEMENT_AUTHORITY.getValue()}')">
            <a href="/admin/user-management">User Management</a>
            </security:authorize>
            <security:authorize access="hasRole('${AuthorityType.ACCOUNT_INFO_AUTHORITY.getValue()}')">
            <a href="/user/account-info">My Account</a>
            </security:authorize>
        </c:when>
        <c:otherwise>
            <a href="/login">Login</a>
        </c:otherwise>
    </c:choose>
</div>
<hr>
<br>
<br>
