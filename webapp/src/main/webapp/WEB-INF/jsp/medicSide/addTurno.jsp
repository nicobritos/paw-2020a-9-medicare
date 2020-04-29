<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
  <head>
    <%@ include file = "../head.jsp" %>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/addTurno.css"/>">
  </head>
  <body>
    <div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
      <!-- TODO: Agregar url -->
      <c:url var="addTurnUrl" value="/staff/profile/workday"/>
      <form:form modelAttribute="workdayForm" class="addturn-form border p-5 rounded" action="${addTurnUrl}" method="post">
        
        
        <div class="row">
          <h6>Medicare <img src='<c:url value="/img/logo.svg"/> ' id="logo"/></h6>
        </div>
        <div class="row justify-content-start">
          <h1 class="addturn-form-title">Agregar turno</h1>
        </div>



        <div class="form-group row">
          <div class="col">
            <label for="dow">Dia de la semana</label>
          </div>
          <div class="col-8">
            <form:select class="form-control" name="dow" id="dow" path="dow">
              <form:option selected="true" value="1">Lunes</form:option>
              <form:option value="2">Martes</form:option>
              <form:option value="3">Miércoles</form:option>
              <form:option value="4">Jueves</form:option>
              <form:option value="5">Viernes</form:option>
              <form:option value="6">Sábado</form:option>
              <form:option value="0">Domingo</form:option>
            </form:select>
          </div>
        </div>
        
        
        
        <div class="form-group row">
          <div class="col">
            <label for="startHour">Hora de inicio</label>
          </div>
          <div class="col-8">
            <form:input class="form-control" type="time" name="startHour" id="startHour" path="startHour"/>
          </div>
        </div>
        
        
        
        <div class="form-group row">
          <div class="col">
            <label for="endHour">Hora de fin</label>
          </div>
          <div class="col-8">
            <form:input class="form-control" type="time" name="endHour" id="endHour" path="endHour"/>
          </div>
        </div>


        <div class="form-group row">
          <div class="col">
            <label for="officeId">Consultorio</label>
          </div>
          <div class="col-8">
            <form:select class="form-control" name="officeId" id="officeId" path="officeId">
              <c:forEach items="${user.get().staffs}" var="staff">
                <form:option value="${staff.office.id}">${staff.office.name}</form:option>
              </c:forEach>
            </form:select>
          </div>
        </div>

        <div class="form-row justify-content-between">
          <a href="<c:url value="/staff/profile"/>">
            <form:button class="form-atras-btn btn" type="button">Atrás</form:button></a><form:button type="submit" class="btn btn-primary">Agregar</form:button>
        </div>
      </form:form>
    </div>
  </body>
</html>
