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
            <spring:message code="AgendaFor"/> <spring:message code="today"/>
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
                      <%-- TODO: check image--%>
                      <img
                        class="w-100 rounded-circle"
                        src="<c:url value="/profilePics/${appointment.patient.userId}"/>"
                        alt=""
                      />
                    </div>
                    <div class="col-6">
                      <div class="row justify-content-start">
                        <h5>${appointment.patient.user.firstName} ${appointment.patient.user.surname}</h5>
                      </div>
                      <div class="row">
                        <p class="m-0"><c:if test="${appointment.fromDate.hourOfDay < 10}">0</c:if>${appointment.fromDate.hourOfDay}:<c:if test="${appointment.fromDate.minuteOfHour < 10}">0</c:if>${appointment.fromDate.minuteOfHour} - <c:if test="${appointment.toDate.hourOfDay < 10}">0</c:if>${appointment.toDate.hourOfDay}:<c:if test="${appointment.toDate.minuteOfHour < 10}">0</c:if>${appointment.toDate.minuteOfHour}</p>
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
                          <!-- TODO add reprogramar -->
                          <a class="dropdown-item cancelAppointmentBtn" href="<c:url value="/staff/appointment/${appointment.id}"/>"><spring:message code="Cancel"/></a>
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
            <button type="button" class="btn" id="prevWeekBtn"><</button>
            <!-- this is one -->
            <c:forEach var="i" begin="0" end="6">
              <span class="ml-1 mr-1 d-flex flex-column align-items-center">
                <!-- day of the week -->
                <span class="medicare-day-span" data-day="${monday.plusDays(i)}" <c:if test="${monday.plusDays(i).dayOfYear == today.dayOfYear && monday.plusDays(i).year == today.year}">style = "font-weight:bold"</c:if>>
                <p class="mb-0">
                  <c:choose>
                    <c:when test="${monday.plusDays(i).dayOfWeek == 1}"><spring:message code="Monday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek == 2}"><spring:message code="Tuesday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek == 3}"><spring:message code="Wednesday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek == 4}"><spring:message code="Thursday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek == 5}"><spring:message code="Friday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek == 6}"><spring:message code="Saturday"/></c:when>
                    <c:when test="${monday.plusDays(i).dayOfWeek == 7}"><spring:message code="Sunday"/></c:when>
                    <c:otherwise>${monday.plusDays(i).dayOfWeek}</c:otherwise>
                  </c:choose>
                </p>
                <!-- day/month -->
                <p class="my-0">${monday.plusDays(i).dayOfMonth} <spring:message code="of"/> <c:choose>
                  <c:when test="${monday.plusDays(i).monthOfYear == 1}"><spring:message code="January"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 2}"><spring:message code="February"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 3}"><spring:message code="March"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 4}"><spring:message code="April"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 5}"><spring:message code="May"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 6}"><spring:message code="June"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 7}"><spring:message code="July"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 8}"><spring:message code="August"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 9}"><spring:message code="September"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 10}"><spring:message code="October"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 11}"><spring:message code="November"/></c:when>
                  <c:when test="${monday.plusDays(i).monthOfYear == 12}"><spring:message code="December"/></c:when>
                  <c:otherwise>${monday.plusDays(i).monthOfYear}</c:otherwise></c:choose></p>
                <p>${weekAppointments.get(monday.plusDays(i).dayOfWeek).size()} <c:choose>
                  <c:when test="${weekAppointments.get(monday.plusDays(i).dayOfWeek).size() == 1}"><spring:message code="appointment"/></c:when>
                  <c:otherwise><spring:message code="appointments"/></c:otherwise>
                </c:choose></p>
              </span>
                </span>
            </c:forEach>
            <button type="button" class="btn" id="nextWeekBtn">></button>
          </div>
          <div class="row justify-content-center">
            <ul class="list-group turno-list mr-2 w-50 overflow-auto">
              <c:forEach var="appointment" items="${weekAppointments.get(today.dayOfWeek)}">
                <li class="list-group-item turno-item mb-3">
                  <div class="container">
                    <div class="row">
                      <div class="col-4">
                          <%-- TODO: check image--%>
                        <img
                          class="w-100 rounded-circle"
                          src="<c:url value="/profilePics/${appointment.patient.userId}"/>"
                          alt=""
                        />
                      </div>
                      <div class="col-6">
                        <div class="row justify-content-start">
                          <h5>${appointment.patient.user.firstName} ${appointment.patient.user.surname}</h5>
                        </div>
                        <div class="row">
                          <p class="m-0"><c:if test="${appointment.fromDate.hourOfDay < 10}">0</c:if>${appointment.fromDate.hourOfDay}:<c:if test="${appointment.fromDate.minuteOfHour < 10}">0</c:if>${appointment.fromDate.minuteOfHour} - <c:if test="${appointment.toDate.hourOfDay < 10}">0</c:if>${appointment.toDate.hourOfDay}:<c:if test="${appointment.toDate.minuteOfHour < 10}">0</c:if>${appointment.toDate.minuteOfHour}</p>
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
                            <!-- TODO add reprogramar -->
                            <a class="dropdown-item cancelAppointmentBtn" href="<c:url value="/staff/appointment/${appointment.id}"/>"><spring:message code="Cancel"/></a>
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
