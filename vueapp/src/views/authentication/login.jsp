<html>
<head>
    <%@ include file="../../partials/head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/authentication/login.css"/> '>
</head>
<body>
<div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
    <c:choose>
        <c:when test="${token != null}">
            <c:url value="/login?token=${token}" var="loginUrl"/>
        </c:when>
        <c:otherwise>
            <c:url value="/login" var="loginUrl"/>
        </c:otherwise>
    </c:choose>
    <c:url value="/signup" var="signupUrl"/>
    <form:form modelAttribute="loginForm" class="register-form border p-5 rounded" action="${loginUrl}" method="POST"
               enctype="application/x-www-form-urlencoded">
        <div class="row">
            <h6>Medicare <img src='<c:url value="/img/logo.svg"/>' id="logo"/></h6>
        </div>
        <div class="row justify-content-start">
            <h1 class="register-form-title"><spring:message code="Login"/></h1>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="medicare_email"><spring:message code="Email"/></label>
            </div>
            <div class="col-8">
                <form:input path="email" class="form-control" type="email" name="medicare_email" id="medicare_email"/>
            </div>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="medicare_password"><spring:message code="Password"/></label>
            </div>
            <div class="col-8">
                <form:input path="password" class="form-control pr-5" type="password" name="medicare_password"
                            id="medicare_password"/>
                <!-- For this to work for must be the id of the password input -->
                <label for="medicare_password" class="toggle-visibility"><img src='<c:url value="/img/eye.svg"/> '><img
                        src='<c:url value="/img/noeye.svg"/>' style="display: none;"></label>
            </div>
        </div>
        <div class="form-group row align-items-center">
            <div class="col">
                <label for="medicare_remember_me" class="mb-0"><spring:message code="RememberMe"/></label>
            </div>
            <div class="col-8">
                <form:checkbox path="rememberMe" id="medicare_remember_me" name="medicare_remember_me"/>
            </div>
        </div>
        <div class="form-row justify-content-between align-items-end mt-2">
            <a class="form-link" href="${signupUrl}"><spring:message code="CreateAccount"/></a>
            <button type="submit" class="btn btn-primary"><spring:message code="Confirm"/></button>
        </div>

        <form:errors path="*" cssClass="mt-4 mb-0 text-danger" element="p"/>
        <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
            <p class="mt-4 mb-0 text-danger">
                <spring:message code="InvalidCredentials.loginForm"/>
            </p>
        </c:if>
    </form:form>
</div>
<script src='<c:url value="/js/scripts/authentication/Login.js"/> '></script>
<script>
    $(document).ready(() => {
        Login.init();
    })
</script>
</body>
</html>
