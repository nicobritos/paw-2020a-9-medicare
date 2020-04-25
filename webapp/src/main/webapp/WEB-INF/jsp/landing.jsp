<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <!-- TODO: check ortografia -->
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" type="text/css" href='<c:url value="/css/landing.css"/>'>
</head>
<body>
<%@ include file = "navbarNotLogged.jsp" %>
<div class="container w-100 ml-4 mb-5">
    <h2 class="display-5 mt-5 green-text">Encontra médicos de forma rápida y fácil</h2>
</div>
<div class="container h-50 justify-content-center">
    <form class="filter-form p-3" action="/mediclist/1">
        <div class="form-row">
            <div class="col">
                <h2 class="ml-5 mt-3 form-title">Buscar médico</h2>
            </div>
        </div>
        <div class="form-row justify-content-center justify-content-around mt-3">
            <div class="col-5 pl-5">
                <!-- TODO: connect -->
                <input class="w-100 form-control" type="text" name="name" id="name" placeholder="Nombre y/o apellido">
            </div>
            <div class="col">
                <!-- TODO: connect -->
                <select name="specialties" class="form-control">
                    <option value="-1" disabled selected>Especialidad</option>
                    <option value="-1">Cualquiera</option>
                    <c:forEach var="specialty" items="${specialties}">
                        <option value="<c:out value="${specialty.id}"/>"><c:out value="${specialty.name}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="col pr-5">
                <!-- TODO: connect -->
                <select name="localities" class="form-control">
                    <option value="-1" disabled selected>Localidad</option>
                    <option value="-1">Cualquiera</option>
                    <c:forEach var="locality" items="${localities}">
                        <option value="<c:out value="${locality.id}"/>"><c:out value="${locality.name}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-row px-5 mt-4 mb-3">
            <!-- TODO: connect -->
            <button class="w-100 btn rounded-pill btn-light header-btn-element">Buscar médico</button>
        </div>
    </form>
</div>
</body>
</html>
