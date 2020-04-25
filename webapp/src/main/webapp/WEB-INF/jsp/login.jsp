<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <%@ include file="head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/login.css"/> '>
</head>
<body>
<%--TODO: Remember me--%>
<div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
    <c:url value="/login" var="loginUrl"/>
    <form:form modelAttribute="loginForm" class="register-form border p-5 rounded" action="${loginUrl}" method="POST" enctype="application/x-www-form-urlencoded">
        <div class="row">
            <h6>Medicare <img src='<c:url value="/css/logo.svg"/>' id="logo"></h6>
        </div>
        <div class="row justify-content-start">
            <h1 class="register-form-title">Iniciar sesion</h1>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="email">Email</label>
            </div>
            <div class="col-8">
                <form:input path="email" class="form-control" type="email" name="Email" id="email"/>
            </div>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="password">Contraseña</label>
            </div>
            <div class="col-8">
                <form:input path="password" class="form-control" type="password" name="Contraseña" id="password"/>
                <label for="password" id="toggle-visibility"><img src='<c:url value="/css/eye.svg"/>'><img
                        src='<c:url value="/css/noeye.svg"/>' style="display: none;"></label>
            </div>
        </div>
        <c:if test="${credentialsError}">
            <div class="row">
                <div class="col error">
                    Credenciales invalidas
                </div>
            </div>
        </c:if>
        <div class="form-row justify-content-between align-items-center">
            <a class="form-link" href="<c:url value="/signup"/>">Crear cuenta</a>
            <button type="submit" class="form-comfirm-button btn" id="login-confirm">Confirmar</button>
        </div>
    </form:form>
</div>
<script src='<c:url value="/js/scripts/login.js"/> '></script>
<script>
    Login.init();
</script>
</body>
</html>
