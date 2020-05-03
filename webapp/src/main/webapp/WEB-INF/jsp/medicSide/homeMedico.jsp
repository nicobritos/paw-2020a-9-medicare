<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
          <h4>
            <spring:message code="AgendaFor"/>
          <c:choose>
            <c:when test="${isToday}">
              <spring:message code="today"/>
            </c:when>
            <c:otherwise>
              <%--TODO: revise--%>
              <fmt:parseDate value="${today}" pattern="yyyy-MM-dd" var="parsedDate" type="date"/>
              <fmt:formatDate value="${parsedDate}" type="date"/>
            </c:otherwise>
          </c:choose>
          </h4>
        </div>
        <div class="col">
          <h4><spring:message code="WeeklyAgenda"/></h4>
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
                          <a class="dropdown-item" href="#"><spring:message code="Cancel"/></a>
                          <a class="dropdown-item" href="#"><spring:message code="Reprogram"/></a>
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
                <span class="medicare-day-span" data-day="${monday.plusDays(i)}">


                <p class="mb-0">
                  <c:choose>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'MONDAY'}"><spring:message code="Monday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'TUESDAY'}"><spring:message code="Tuesday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'WEDNESDAY'}"><spring:message code="Wednesday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'THURSDAY'}"><spring:message code="Thursday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'FRIDAY'}"><spring:message code="Friday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'SATURDAY'}"><spring:message code="Saturday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek.name() == 'SUNDAY'}"><spring:message code="Sunday"/></c:when>
                    <c:otherwise>${monday.plusDays(i).dayOfWeek.name()}</c:otherwise>
                  </c:choose>
                </p>
                <!-- day/month -->
                <p class="my-0">${monday.plusDays(i).dayOfMonth} <spring:message code="of"/> <c:choose>
                  <c:when test="${monday.plusDays(i).month.name() == 'JANUARY'}"><spring:message code="January"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'FEBRUARY'}"><spring:message code="February"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'MARCH'}"><spring:message code="March"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'APRIL'}"><spring:message code="April"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'MAY'}"><spring:message code="May"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'JUNE'}"><spring:message code="June"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'JULY'}"><spring:message code="July"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'AUGUST'}"><spring:message code="August"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'SEPTEMBER'}"><spring:message code="September"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'OCTOBER'}"><spring:message code="October"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'NOVEMBER'}"><spring:message code="November"/></c:when>
                  <c:when test="${monday.plusDays(i).month.name() == 'DECEMBER'}"><spring:message code="December"/></c:when>
                  <c:otherwise>${monday.plusDays(i).month.name()}</c:otherwise></c:choose></p>
                <p>${todayAppointments.size()} <c:choose>
                  <c:when test="${todayAppointments.size() == 1}"><spring:message code="appointment"/></c:when>
                  <c:otherwise><spring:message code="appointments"/></c:otherwise>
                </c:choose></p>
              </span>
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
                            <a class="dropdown-item" href="#"><spring:message code="Cancel"/></a>
                            <a class="dropdown-item" href="#"><spring:message code="Reprogram"/></a>
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
