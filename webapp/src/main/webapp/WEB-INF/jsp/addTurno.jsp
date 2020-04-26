<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
  <head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/addTurno.css"/>">
  </head>
  <body>
    <div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
      <%--TODO:connect form--%>
      <form class="addturn-form border p-5 rounded">
        
        
        <div class="row">
          <h6>Medicare <img src='<c:url value="/css/logo.svg"/> ' id="logo"/></h6>
        </div>
        <div class="row justify-content-start">
          <h1 class="addturn-form-title">Agregar turno</h1>
        </div>



        <div class="form-group row">
          <div class="col">
            <label for="diaSemana">Dia de la semana</label>
          </div>
          <div class="col-8">
            <%--TODO:connect form--%>
            <select class="form-control" name="diaSemana" id="diaSemana">
                <option selected>Lunes</option>
                <option>Martes</option>
                <option>Miercoles</option>
                <option>Jueves</option>
                <option>Viernes</option>
                <option>Sabado</option>
                <option>Domingo</option>
            </select>
          </div>
        </div>
        
        
        
        <div class="form-group row">
          <div class="col">
            <label for="horaInicio">Hora de inicio</label>
          </div>
          <div class="col-8">
            <%--TODO:connect form--%>
            <input class="form-control" type="time" name="horaInicio" id="horaInicio">
          </div>
        </div>
        
        
        
        <div class="form-group row">
          <div class="col">
            <label for="horaFin">Hora de fin</label>
          </div>
          <div class="col-8">
            <%--TODO:connect form--%>
            <input class="form-control" type="time" name="horaFin" id="horaFin">
          </div>
        </div>
        
        <div class="form-row justify-content-between">
          <%--TODO:connect buttons--%>
          <button class="form-atras-btn btn" type="reset">Atras</button><button class="btn btn-primary">Agregar</button>
        </div>
      </form>
    </div>
  </body>
</html>
