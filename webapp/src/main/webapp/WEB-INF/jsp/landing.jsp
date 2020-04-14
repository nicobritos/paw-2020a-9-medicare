<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/landing.css"/>">
</head>
<body>
    <div class="container h-100 d-flex flex-column justify-content-center align-items-center">
        <div class="container d-flex flex-column justify-content-center align-items-center with-logo">
            <h1 class="display-1">MediCare</h1>
            <h4>Encuentra médicos de forma rápida y fácil</h4>
            <a href="<c:url value="/mediclist"/>" class="btn btn-secondary" style="margin:50px;">Buscar médicos</a>
        </div>
    </div>
</body>
</html>
