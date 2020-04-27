<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <%@ include file="../head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/register.css"/> '>
</head>
<body>
<div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
    <c:url value="/login" var="loginUrl"/>
    <c:url value="/signup/patient" var="signupUrl"/>
    <form:form modelAttribute="signupForm" class="register-form border p-5 rounded" action="${signupUrl}" method="POST"
               enctype="application/x-www-form-urlencoded">
        <div class="row">
            <h6>Medicare <img src='<c:url value="/css/logo.svg"/>' id="logo"/></h6>
        </div>
        <div class="row justify-content-start">
            <h1 class="register-form-title">Crear cuenta</h1>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="first_name">Nombre</label>
            </div>
            <div class="col-8">
                <form:input path="firstName" class="form-control" type="text" name="firstName" id="first_name"/>
            </div>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="surname">Apellido</label>
            </div>
            <div class="col-8">
                <form:input path="surname" class="form-control" type="text" name="surname" id="surname"/>
            </div>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="medicare_email">Email</label>
            </div>
            <div class="col-8">
                <form:input path="email" class="form-control" type="email" name="medicare_email" id="medicare_email"/>
            </div>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="medicare_password">Contraseña</label>
            </div>
            <div class="col-8">
                <form:input path="password" class="form-control pr-5" type="password" name="medicare_password" id="medicare_password"/>
                <!-- For this to work for must be the id of the password input -->
                <label for="medicare_password" class="toggle-visibility"><img src='<c:url value="/css/eye.svg"/> '><img
                        src='<c:url value="/css/noeye.svg"/>' style="display: none;"></label>
            </div>
        </div>
        <div class="form-group row">
            <div class="col">
                <label for="medicare_repeatPassword">Repetir contraseña</label>
            </div>
            <div class="col-8">
                <form:input path="repeatPassword" class="form-control pr-5" type="password" name="medicare_repeatPassword"
                            id="medicare_repeatPassword"/>
                <!-- For this to work for must be the id of the password input -->
                <label for="medicare_repeatPassword" class="toggle-visibility"><img src='<c:url value="/css/eye.svg"/> '><img
                        src='<c:url value="/css/noeye.svg"/> ' style="display: none;"></label>
            </div>
        </div>
        <div class="form-row justify-content-between align-items-end mt-2">
            <a class="form-link" href="${loginUrl}">Iniciar sesión</a>
            <button type="submit" class="btn btn-primary">Confirmar</button>
        </div>

        <form:errors path="*" cssClass="mt-4 mb-0 text-danger" element="p"/>
    </form:form>
</div>
<script src='<c:url value="/js/scripts/register.js"/> '></script>
<script>
    $(document).ready(() => {
        Register.initPatient();
    })
</script>
</body>
</html>