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
    <h2 class="display-5 mt-5 green-text">Encontra medicos de forma rapida y facil</h2>
</div>
<div class="container h-50 justify-content-center">
    <form class="filter-form p-3">
        <div class="form-row">
            <div class="col">
                <h2 class="ml-5 mt-3 form-title">Buscar medico</h2>
            </div>
        </div>
        <div class="form-row justify-content-center justify-content-around mt-3">
            <div class="col-5 pl-5">
                <!-- TODO: connect -->
                <input class="w-100 form-control" type="text" name="nombre" id="nombre" placeholder="nombre">
            </div>
            <div class="col">
                <!-- TODO: connect -->
                <select class="w-100 form-control" name="especialidad" id="especialidad">
                    <option value="-1" disabled selected>Especialidad</option>
                </select>
            </div>
            <div class="col pr-5">
                <!-- TODO: connect -->
                <select class="w-100 form-control" name="localidad" id="localidad">
                    <option value="-1" disabled selected>Localidad</option>
                </select>
            </div>
        </div>
        <div class="form-row px-5 mt-4 mb-3">
            <!-- TODO: connect -->
            <button class="w-100 btn rounded-pill btn-light header-btn-element">Buscar medico</button>
        </div>
    </form>
</div>
</body>
</html>
