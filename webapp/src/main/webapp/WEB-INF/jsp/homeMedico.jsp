<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/homeMedico.css"/> ' />
  </head>
  <body>
    <%@ include file="navbarLogged.jsp"%>
    <div class="container h-75 w-100 mt-5">
      <div class="row">
        <div class="col-5">
          <h4>Agenda de hoy</h4>
        </div>
        <div class="col">
          <h4>Agenda semanal</h4>
        </div>
      </div>
      <div class="row h-100">
        <div class="col-4 h-100 pl-0 mr-3 w-100">
          <!-- TODO connect to turnos -->
          <ul class="list-group turno-list mr-2 w-100 h-100 overflow-auto">
            <!-- TODO remove id. was just for testing -->
            <c:forEach var="appointment" items="${todayAppointments}">
              <li class="list-group-item turno-item mb-3" id="lit">
                <div class="container">
                  <div class="row">
                    <div class="col-4">
                      <img
                        class="w-100"
                        src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg"
                        alt=""
                      />
                    </div>
                    <div class="col-6">
                      <div class="row justify-content-start">
                        <h5>${appointment.patient.user.firstName} ${appointment.patient.user.surname}</h5>
                      </div>
                      <div class="row">
                        <p class="m-0">${appointment.fromDate.hours}:${appointment.fromDate.minutes} - ${appointment.toDate.hours}:${appointment.toDate.minutes}</p>
                      </div>
                    </div>
                    <div class="col-2 justify-content-start">
                      <div class="dropdown">
                        <img
                          src='<c:url value="/css/moreOptions.svg" />'
                          class="moreOptionsButton"
                          alt=""
                          data-toggle="dropdown"
                        />
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
          <div class="row d-flex justify-content-around">
            <!-- TODO connect buttons and days -->
            <!-- TODO the selected day should be font-weight-bold -->
            <button type="button" class="btn"><</button>
            <!-- this is one -->
            <c:forEach var="i" begin="0" end="5">
              <span class="d-flex flex-column align-items-center">
                <!-- day of the week -->
                <p class="mb-0">${monday.plusDays(i).dayOfWeek.name()}</p>
                <!-- day/month -->
                <p class="my-0">${monday.plusDays(i).dayOfMonth} de ${monday.plusDays(i).month.name()}</p>
                <!-- TODO:  Amount of turnos this day -->
                <p>${todayAppointments.size()} turnos</p>
              </span>
            </c:forEach>
            <button type="button" class="btn">></button>
          </div>
          <div class="row justify-content-center">
            <!-- TODO: change items -->
            <ul class="list-group turno-list mr-2 w-50 overflow-auto">
              <c:forEach var="appointment" items="${todayAppointments}">
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
                      <div class="col-6">
                        <div class="row justify-content-start">
                          <h5>${appointment.patient.user.firstName} ${appointment.patient.user.surname}</h5>
                        </div>
                        <div class="row">
                          <p class="m-0">${appointment.fromDate.hours}:${appointment.fromDate.minutes} - ${appointment.toDate.hours}:${appointment.toDate.minutes}</p>
                        </div>
                      </div>
                      <div class="col-2 justify-content-start">
                        <div class="dropdown">
                          <img
                            src='<c:url value="/css/moreOptions.svg"/> '
                            class="moreOptionsButton"
                            alt=""
                            data-toggle="dropdown"
                          />
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
        </div>
      </div>
    </div>
  </body>
</html>
