<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <%@ include file="../head.jsp" %>
    <link rel="stylesheet" href="<c:url value="/css/errorPage.css"/>"/>
</head>
<body>
<div class="container h-100 d-flex flex-column justify-content-center align-items-center">
    <div>
        <h1>MediCare<img src="<c:url value="/img/logo.svg"/>" alt="logo" class="logo-for-error"></h1>
        <h4><h4 class="d-inline">404</h4>. <spring:message code="ThereWasAnError"/>.</h4>
        <h6><spring:message code="NoPageFound"/></h6>
        <a href="<c:url value="/"/>" class="btn btn-secondary" id="homeBtn"><spring:message code="BackToHome"/></a>
    </div>
</div>
</body>
</html>