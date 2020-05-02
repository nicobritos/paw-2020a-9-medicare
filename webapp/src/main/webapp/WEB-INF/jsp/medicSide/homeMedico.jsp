<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
    <%@ include file = "../head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/staff/homeMedico.css"/> ' />
  </head>
  <body>
    <%@ include file="../navbar/navbarLogged.jsp"%>
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
          <ul class="list-group turno-list mr-2 w-100 h-100 overflow-auto">
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
                          src='<c:url value="/img/moreOptions.svg" />'
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
            <button type="button" class="btn" id="prevWeekBtn"><</button>
            <!-- this is one -->
            <c:forEach var="i" begin="0" end="6">
              <span class="ml-1 mr-1 d-flex flex-column align-items-center">
                <!-- day of the week -->

                <p class="mb-0">
                  <c:choose>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'MONDAY'}">Lunes</c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'TUESDAY'}">Martes</c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'WEDNESDAY'}">Miércoles</c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'THURSDAY'}">Jueves</c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'FRIDAY'}">Viernes</c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'SATURDAY'}">Sábado</c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'SUNDAY'}">Domingo</c:when>
                    <c:otherwise>${monday.plusDays(i).dayOfWeek.name()}</c:otherwise>
                  </c:choose>
                </p>
                <!-- day/month -->
                <p class="my-0">${monday.plusDays(i).dayOfMonth} de <c:choose>
                  <c:when test="${monday.plusDays(i).month.name() == 'JANUARY'}">enero</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'FEBRUARY'}">febrero</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'MARCH'}">marzo</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'APRIL'}">abril</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'MAY'}">mayo</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'JUNE'}">junio</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'JULY'}">julio</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'AUGUST'}">agosto</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'SEPTEMBER'}">septiembre</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'OCTOBER'}">octubre</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'NOVEMBER'}">noviembre</c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'DECEMBER'}">diciembre</c:when>
                  <c:otherwise>${monday.plusDays(i).month.name()}</c:otherwise></c:choose></p>
                <p>${todayAppointments.size()} <c:choose>
                  <c:when test="${todayAppointments.size() == 1}">turno</c:when>
                  <c:otherwise>turnos</c:otherwise>
                </c:choose></p>
              </span>
            </c:forEach>
            <button type="button" class="btn" id="nextWeekBtn">></button>
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
                            src='<c:url value="/img/moreOptions.svg"/> '
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
  <script src='<c:url value="/js/scripts/staff/homeMedico.js"/> '></script>
  </body>
</html>
