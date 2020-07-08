<!DOCTYPE html>
<html lang="en">
<%@ include file="../partials/head.jsp" %>
<link rel="stylesheet" href='<c:url value="/css/unverified.css"/>'/>
</head>
<body class="d-flex flex-column">
<%@ include file="navbar/navbar.jsp" %>
<div class="container flex-grow-1 d-flex flex-column w-100 h-100 justify-content-center align-items-center">
    <div class="px-5 py-3 d-flex flex-column align-items-center justify-content-center styled-container">
        <h1 class="mb-4"><spring:message code="MissingEmailVerification"/></h1>
        <h4><spring:message code="VerificationEmailSent"/>:</h4>
        <h4>"<c:out value="${user.get().email}"/>"</h4>
        <h4><spring:message code="UntilVerifiedCantContinue"/></h4>
    </div>
</div>
<c:if test="${tokenError}">
    <div class="alert alert-danger medicare-alert-error">
        <spring:message code="TokenError.loginForm"/>
    </div>
</c:if>
</body>
</html>