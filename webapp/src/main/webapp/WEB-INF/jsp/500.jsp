<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href="<c:url value="/css/errorPage.css"/>" />
</head>
<body>
<div class="container h-100 d-flex flex-column justify-content-center align-items-center">
    <div>
        <h1>MediCare<img src="<c:url value="/css/logo.svg"/>" alt="logo" class="logo-for-error"></h1>
        <h4><h4 class="d-inline">500</h4>. Hubo un error.</h4>
        <h6>Falló algo en nuestro servidor.</h6>
        <h6>Prueba refrescar o volver al Home</h6>
        <a href="<c:url value="/"/>" class="btn btn-secondary" id="homeBtn">Volver al Home</a>
    </div>
</div>
</body>
</html>