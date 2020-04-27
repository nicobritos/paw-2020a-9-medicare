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
    <div class="register-form border p-5 rounded">
        <div class="row">
            <h6>Medicare <img src='<c:url value="/css/logo.svg"/>' id="logo"/></h6>
        </div>
        <div class="row justify-content-start">
            <h1 class="register-form-title">Crear cuenta</h1>
        </div>
        <div class="form-row justify-content-between align-items-end">
            <div class="d-flex flex-column mr-5 align-items-center card card-shadow pointer p-2" id="signup-patient">
                <i class="fas fa-hospital-user" style="font-size: 80px;"></i>
                <h4 class="mt-2">Busco médicos</h4>
            </div>
            <div class="d-flex flex-column align-items-center card card-shadow pointer p-2" id="signup-staff">
                <i class="fa fa-user-md" style="font-size: 80px;"></i>
                <h4 class="mt-2">Soy un médico</h4>
            </div>
        </div>
    </div>
</div>
<script src='<c:url value="/js/scripts/register.js"/> '></script>
<script>
    $(document).ready(() => {
        Register.init();
    })
</script>
</body>
</html>
