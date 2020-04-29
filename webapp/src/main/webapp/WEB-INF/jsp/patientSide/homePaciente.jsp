<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file = "../head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/patient/homePaciente.css"/>' />
  </head>
  <body>
  <!-- TODO: change navbar -->
    <%@ include file="../navbar/navbarLogged.jsp"%>

    <div class="container h-75 w-100 mt-5">
      <div class="row">
        <h4>Mis turnos</h4>
      </div>
      <div class="row h-100">
        <div class="col h-100 pl-0 mr-5 w-100">
          <ul class="list-group turno-list mr-2 w-100 h-100 overflow-auto">
            <c:forEach var="appointment" items="${appointments}">
              <li class="list-group-item turno-item mb-3">
                <div class="container">
                  <div class="row">
                    <div class="col-4">
                      <img
                        class="w-100"
                        src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg"
                        alt=""
                      />
                    </div>
                    <div class="col-7">
                      <div class="row justify-content-start">
                        <h5>Dr ${appointment.staff.firstName} ${appointment.staff.surname}</h5>
                      </div>
                      <div class="row">
                          <!-- TODO doctor specialty -->
                        <c:forEach var="specialty" items="${appointment.staff.staffSpecialties}">
                          <p class="m-0">${specialty}</p>
                        </c:forEach>
                      </div>
                      <div class="row">
                        <p class="m-0">${appointment.staff.office.street}</p>
                      </div>
                      <div class="row">
                        <p class="m-0">${appointment.fromDate.day} de ${appointment.fromDate.month}
                            ${appointment.fromDate.hours}:${appointment.fromDate.minutes} hasta ${appointment.toDate.hours}:${appointment.toDate.minutes}</p>
                      </div>
                    </div>
                    <div class="col-1 justify-content-start">
                        <div class="dropdown">
                            <!-- TODO connect icon -->
                            <img src='<c:url value="/img/moreOptions.svg"/>' class="moreOptionsButton" alt="nore options" data-toggle="dropdown">
                            <div class="dropdown-menu">
                              <!-- TODO conect to the options -->
                            <a class="dropdown-item" href="#">Cancelar</a>
                            <a class="dropdown-item" href="#">Reprogramar</a>
                          </div>
                        </div>
                    </div>
                  </div>
                </div>
              </li>
            </c:forEach>
          </ul>
        </div>
        <div class="col">
          <form action="<c:url value="/mediclist/1"/>" class="container p-5 filter-form">
            <div class="row justify-content-start">
              <h3 class="form-title">Buscar medico</h3>
            </div>
            <div class="row justify-content-start my-3">
              <input class="w-100 form-control" type="text" name="name" id="name" placeholder="Nombre y/o apellido">
            </div>
            <div class="row justify-content-start my-3">
              <select name="specialties" class="form-control">
                <option value="-1" disabled selected>Especialidad</option>
                <option value="-1">Cualquiera</option>
                <c:forEach var="specialty" items="${specialties}">
                  <option value="<c:out value="${specialty.id}"/>"><c:out value="${specialty.name}"/></option>
                </c:forEach>
              </select>
            </div>
            <div class="row justify-content-start my-3">
              <select name="localities" class="form-control">
                <option value="-1" disabled selected>Localidad</option>
                <option value="-1">Cualquiera</option>
                <c:forEach var="locality" items="${localities}">
                  <option value="<c:out value="${locality.id}"/>"><c:out value="${locality.name}"/></option>
                </c:forEach>
              </select>
            </div>
            <div class="row justify-content-start my-3">
              <button class="w-100 btn rounded-pill btn-light header-btn-element">Buscar médicos</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>
