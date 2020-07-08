<!DOCTYPE html>
<html lang="en">
<%@ include file="../../partials/head.jsp" %>
<link rel="stylesheet" href='<c:url value="/css/staff/homeMedico.css"/> '/>
</head>
<body>
<%@ include file="../navbar/navbarLogged.jsp" %>
<div class="container h-75 w-100 mt-5">
    <div class="row h-100">
        <div class="col-4 h-100 pl-0 mr-3 w-100">
            <h4><spring:message code="AgendaFor"/> <spring:message code="today"/></h4>
            <ul class="list-group turno-list mr-2 w-100 h-100 overflow-auto">
                <c:if test="${todayAppointments.isEmpty()}">
                    <div class="container-fluid justify-content-center">
                        <p class="text-left mt-4" style="color:grey;"><spring:message code="NoAppointmentsToday"/></p>
                    </div>
                </c:if>
                <c:forEach var="appointment" items="${todayAppointments}">
                    <li class="list-group-item turno-item mb-3" id="lit">
                        <div class="container">
                            <div class="row">
                                <div class="col-4 d-flex flex-column justify-content-center">
                                    <div class="profile-picture-container">
                                        <div style="margin-top: 100%;"></div>
                                        <img
                                                class="profile-picture rounded-circle"
                                                src="<c:url value="/profilePics/${appointment.patient.user.profilePicture.id}"/>"
                                                alt=""
                                        />
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="row justify-content-start">
                                        <h5><spring:message code="name_surname" arguments="${appointment.patient.user.firstName};${appointment.patient.user.surname}" argumentSeparator=";"/></h5>
                                    </div>
                                    <div class="row">
                                        <p class="m-0">
                                            <c:choose>
                                                <c:when test="${appointment.fromDate.hourOfDay < 10}"><c:set value="0${appointment.fromDate.hourOfDay}" var="vafromHourOfDay"/></c:when>
                                                <c:otherwise><c:set value="${appointment.fromDate.hourOfDay}" var="vafromHourOfDay"/></c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${appointment.fromDate.minuteOfHour < 10}"><c:set value="0${appointment.fromDate.minuteOfHour}" var="vafromMinuteOfHour"/></c:when>
                                                <c:otherwise><c:set value="${appointment.fromDate.minuteOfHour}" var="vafromMinuteOfHour"/></c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${appointment.toDate.hourOfDay < 10}"><c:set value="0${appointment.toDate.hourOfDay}" var="vatoHourOfDay"/></c:when>
                                                <c:otherwise><c:set value="${appointment.toDate.hourOfDay}" var="vatoHourOfDay"/></c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${appointment.toDate.minuteOfHour < 10}"><c:set value="0${appointment.toDate.minuteOfHour}" var="vatoMinuteOfHour"/></c:when>
                                                <c:otherwise><c:set value="${appointment.toDate.minuteOfHour}" var="vatoMinuteOfHour"/></c:otherwise>
                                            </c:choose>
                                            <spring:message argumentSeparator=";" arguments="${vafromHourOfDay};${vafromMinuteOfHour};${vatoHourOfDay};${vatoMinuteOfHour}" code="fhom_fmoh_thod_tmoh"/>
                                        </p>
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

                                            <form action="<c:url value="/staff/appointment/${appointment.id}${query}"/>" method="post">
                                                <button type="submit" class="dropdown-item"><spring:message code="Cancel"/></button>
                                            </form>
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
            <h4><spring:message code="WeeklyAgenda"/></h4>
            <table class="table table-borderless">
                <tr>
                    <td class="px-0">
                        <button type="button" class="btn" id="prevWeekBtn"><</button>
                    </td>
                    <c:forEach var="i" begin="0" end="6">
                        <td class="px-0">
                            <!-- day of the week -->
                            <span class="medicare-day-span container px-0 mx-2 d-flex flex-column align-items-center text-center"
                                  data-day="<c:out value="${monday.plusDays(i)}"/>"
                                  <c:if test="${monday.plusDays(i).dayOfYear == today.dayOfYear && monday.plusDays(i).year == today.year}">style="font-weight:bold"</c:if>>
                                <p class="mb-0">
                                  <c:choose>
                                      <c:when test="${monday.plusDays(i).dayOfWeek == 1}"><spring:message code="MondayAbbreviated"/></c:when>
                                      <c:when test="${monday.plusDays(i).dayOfWeek == 2}"><spring:message code="TuesdayAbbreviated"/></c:when>
                                      <c:when test="${monday.plusDays(i).dayOfWeek == 3}"><spring:message code="WednesdayAbbreviated"/></c:when>
                                      <c:when test="${monday.plusDays(i).dayOfWeek == 4}"><spring:message code="ThursdayAbbreviated"/></c:when>
                                      <c:when test="${monday.plusDays(i).dayOfWeek == 5}"><spring:message code="FridayAbbreviated"/></c:when>
                                      <c:when test="${monday.plusDays(i).dayOfWeek == 6}"><spring:message code="SaturdayAbbreviated"/></c:when>
                                      <c:when test="${monday.plusDays(i).dayOfWeek == 7}"><spring:message code="SundayAbbreviated"/></c:when>
                                      <c:otherwise><c:out value="${monday.plusDays(i).dayOfWeek}"/></c:otherwise>
                                  </c:choose>
                                </p>
                                <!-- day/month -->
                                <p class="my-0">
                                    <c:choose>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 1}"><spring:message code="JanuaryAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 2}"><spring:message code="FebruaryAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 3}"><spring:message code="MarchAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 4}"><spring:message code="AprilAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 5}"><spring:message code="MayAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 6}"><spring:message code="JuneAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 7}"><spring:message code="JulyAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 8}"><spring:message code="AugustAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 9}"><spring:message code="SeptemberAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 10}"><spring:message code="OctoberAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 11}"><spring:message code="NovemberAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:when test="${monday.plusDays(i).monthOfYear == 12}"><spring:message code="DecemberAbbreviated" var="mpdMonthOfYear"/></c:when>
                                        <c:otherwise><c:set value="${monday.plusDays(i).monthOfYear}" var="mpdMonthOfYear"/> </c:otherwise>
                                    </c:choose>
                                    <spring:message code="dom_moy" argumentSeparator=";" arguments="${monday.plusDays(i).dayOfMonth};${mpdMonthOfYear}"/>
                                </p>
                                <p>
                                    <c:choose>
                                        <c:when test="${weekAppointments.get(monday.plusDays(i).dayOfWeek).size() == 1}"><spring:message code="appointmentAbbreviated" var="vwapp"/></c:when>
                                        <c:otherwise><spring:message code="appointmentsAbbreviated" var="vwapp"/></c:otherwise>
                                    </c:choose>
                                    <spring:message arguments="${weekAppointments.get(monday.plusDays(i).dayOfWeek).size()};${vwapp}" argumentSeparator=";" code="NumberedAppointments"/>
                                </p>
                            </span>
                        </td>
                    </c:forEach>
                    <td class="px-0">
                        <button type="button" class="btn" id="nextWeekBtn">></button>
                    </td>
                </tr>
                <tr>
                    <td colspan="9">
                        <div class="container-fluid d-flex justify-content-center">
                            <ul class="list-group turno-list mr-2 w-50 overflow-auto">
                                <c:if test="${weekAppointments.get(today.dayOfWeek).isEmpty()}">
                                    <div class="container-fluid justify-content-center">
                                        <p class="text-center mt-4" style="color:grey;"><spring:message code="NoAppointmentsThisDay"/></p>
                                    </div>
                                </c:if>
                                <c:forEach var="appointment" items="${weekAppointments.get(today.dayOfWeek)}">
                                    <li class="list-group-item turno-item mb-3">
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-4 d-flex flex-column justify-content-center">
                                                    <div class="profile-picture-container">
                                                        <div style="margin-top: 100%;"></div>
                                                        <%--TODO: check this--%>
                                                        <img
                                                                class="profile-picture rounded-circle"
                                                                src="<c:url value="/profilePics/${appointment.patient.user.profilePicture.id}"/>"
                                                                alt=""
                                                        />
                                                    </div>
                                                </div>
                                                <div class="col-6">
                                                    <div class="row justify-content-start">
                                                        <h5><c:out value="${appointment.patient.user.firstName} ${appointment.patient.user.surname}"/></h5>
                                                    </div>
                                                    <div class="row">
                                                        <p class="m-0">
                                                            <c:choose>
                                                                <c:when test="${appointment.fromDate.hourOfDay < 10}"><c:set value="0${appointment.fromDate.hourOfDay}" var="vafromHourOfDay"/></c:when>
                                                                <c:otherwise><c:set value="${appointment.fromDate.hourOfDay}" var="vafromHourOfDay"/></c:otherwise>
                                                            </c:choose>
                                                            <c:choose>
                                                                <c:when test="${appointment.fromDate.minuteOfHour < 10}"><c:set value="0${appointment.fromDate.minuteOfHour}" var="vafromMinuteOfHour"/></c:when>
                                                                <c:otherwise><c:set value="${appointment.fromDate.minuteOfHour}" var="vafromMinuteOfHour"/></c:otherwise>
                                                            </c:choose>
                                                            <c:choose>
                                                                <c:when test="${appointment.toDate.hourOfDay < 10}"><c:set value="0${appointment.toDate.hourOfDay}" var="vatoHourOfDay"/></c:when>
                                                                <c:otherwise><c:set value="${appointment.toDate.hourOfDay}" var="vatoHourOfDay"/></c:otherwise>
                                                            </c:choose>
                                                            <c:choose>
                                                                <c:when test="${appointment.toDate.minuteOfHour < 10}"><c:set value="0${appointment.toDate.minuteOfHour}" var="vatoMinuteOfHour"/></c:when>
                                                                <c:otherwise><c:set value="${appointment.toDate.minuteOfHour}" var="vatoMinuteOfHour"/></c:otherwise>
                                                            </c:choose>
                                                            <spring:message argumentSeparator=";" arguments="${vafromHourOfDay};${vafromMinuteOfHour};${vatoHourOfDay};${vatoMinuteOfHour}" code="fhom_fmoh_thod_tmoh"/>
                                                        </p>
                                                    </div>
                                                </div>
                                                <div class="col-2 justify-content-start">
                                                    <div class="dropdown">
                                                        <img
                                                                src='<c:url value="/img/moreOptions.svg"/> '
                                                                class="moreOptionsButton"
                                                                alt="more options"
                                                                data-toggle="dropdown"
                                                        />
                                                        <div class="dropdown-menu">
                                                            <!-- TODO add reprogramar -->
                                                            <form action="<c:url value="/staff/appointment/${appointment.id}${query}"/>"
                                                                  method="post" class="cancel-appt-form">
                                                                <button type="button" class="dropdown-item cancel-appt-btn">
                                                                    <spring:message code="Cancel"/></button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    let strings = new Array();
    strings['title'] = "<spring:message code='YouAreAboutToCancelAnAppointment' javaScriptEscape='true' />";
    strings['body'] = "<spring:message code='DoYouWantToContinue' javaScriptEscape='true' />";
    strings['accept'] = "<spring:message code='Accept' javaScriptEscape='true' />";
    strings['cancel'] = "<spring:message code='Cancel' javaScriptEscape='true' />";
</script>
<script src='<c:url value="/js/scripts/staff/MedicHome.js"/> '></script>
<script>
    $(document).ready(() => {
        MedicHome.init()
    });
</script>
</body>
</html>