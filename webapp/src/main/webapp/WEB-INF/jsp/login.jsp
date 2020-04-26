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
    <form:form modelAttribute="loginForm" class="register-form border p-5 rounded" action="${loginUrl}" method="POST"
               enctype="application/x-www-form-urlencoded">
        <div class="row">
            <h6>Medicare <img src='<c:url value="/css/logo.svg"/>' id="logo"/></h6>
        </div>
        <div class="row justify-content-start">
            <h1 class="register-form-title">Crear cuenta</h1>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="email">Email</label>
            </div>
            <div class="col-8">
                <form:input path="email" class="form-control" type="email" name="email" id="email"/>
            </div>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="password">Contraseña</label>
            </div>
            <div class="col-8">
                <form:input path="password" class="form-control" type="password" name="password" id="password"/>
                <!-- For this to work for must be the id of the password input -->
                <label for="password" class="toggle-visibility"><img src='<c:url value="/css/eye.svg"/> '><img
                        src='<c:url value="/css/noeye.svg"/>' style="display: none;"></label>
            </div>
        </div>
        <div class="form-row justify-content-between">
            <button type="submit" class="btn btn-primary">Confirmar</button>
        </div>
    </form:form>
</div>
<script src='<c:url value="/js/scripts/login.js"/> '></script>
<script>
    Login.init();
</script>
</body>
</html>
