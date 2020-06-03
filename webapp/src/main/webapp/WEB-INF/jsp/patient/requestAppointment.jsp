<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../../partials/head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/reservarTurno.css"/>'/>
</head>
<body class="container-fluid p-0 m-0 d-flex flex-column">
<%@ include file="../navbar/navbarLogged.jsp" %>
<div class="container fill-height">
    <div class="row mt-4">
        <c:url value="/patient/appointment/${staffId}/${year}/${month}/${day}/${hour}/${minute}"
               var="createAppointmentUrl"/>
        <form:form modelAttribute="appointmentForm" action="${createAppointmentUrl}" method="post"
                   class="col d-flex flex-column" id="appointment-request-form">
            <h4 class="text-muted"><spring:message code="ScheduleAppointment"/></h4>
            <p class="mt-3 text-muted"><spring:message code="Motive"/></p>
            <spring:message var="motivePlaceholder" code="Motive"/>
            <label for="motive"></label><form:input path="motive" placeholder="${motivePlaceholder}" type="text"
                                                    name="motive" id="motive" class="form-control w-50"/>
            <p class="mt-3 text-muted mb-1"><spring:message code="PersonalData"/></p>
            <div class="container-fluid p-0 mb-1 d-flex flex-row">
                <div class="col px-0">
                    <spring:message var="firstNamePlaceholder" code="Name"/>
                    <form:input path="firstName" placeholder="${firstNamePlaceholder}" type="text" name="firstName"
                                id="firstName" class="form-control-plaintext w-100" readonly="true"/>
                </div>
                <div class="col p-0 ml-2">
                    <spring:message var="surnamePlaceholder" code="Surname"/>
                    <form:input path="surname" placeholder="${surnamePlaceholder}" type="text" name="surname"
                                id="surname" class="form-control-plaintext w-100" readonly="true"/>
                </div>
            </div>
            <spring:message var="phonePlaceholder" code="Phone"/>
            <form:input path="phone" placeholder="${phonePlaceholder}" type="text" name="phone" id="phone"
                        class="form-control w-50 mb-1"/>
            <spring:message var="emailPlaceholder" code="Email"/>
            <form:input path="email" placeholder="${emailPlaceholder}" type="text" name="email" id="email"
                        class="form-control-plaintext w-50 mb-1" readonly="true"/>
            <spring:message var="commentPlaceholder" code="OptionalComment"/>
            <form:textarea path="comment" placeholder="${commentPlaceholder}" class="form-control mt-3" name="comment"
                           id="comment" cols="30" rows="5"/>
            <button type="button" id="appointment-request-button" class="btn btn-info mt-3 w-100"><spring:message code="ScheduleAppointment"/></button>

            <form:errors path="*" cssClass="mt-4 mb-0 text-danger" element="p"/>
        </form:form>
        <div class="col">
            <div class="container details-container mt-5 p-3 w-75">
                <div class="row justify-content-center">
                    <h4 class="white-text"><spring:message code="AppointmentDetails"/></h4>
                </div>
                <div class="row justify-content-center border-top border-light py-2">
                    <div class="col-3 d-flex flex-column justify-content-center">
                        <div class="profile-picture-container">
                            <div style="margin-top: 100%;"></div>
                            <img
                                    class="profile-picture rounded-circle"
                                    src="<c:url value="/profilePics/${staff.user.profilePicture.id}"/>"
                                    alt=""
                            />
                        </div>
                    </div>
                    <div class="col p-0">
                        <p class="m-0 white-text"><c:out value="${staff.firstName} ${staff.surname}"/></p>
                        <small class="white-text">
                            <c:forEach var="specialty" items="${staff.staffSpecialties}">
                                <c:out value="${specialty.name} "/>
                            </c:forEach>
                        </small>
                    </div>
                </div>
                <div class="row justify-content-center border-top border-light py-2">
                    <div class="col-3 d-flex align-items-center justify-content-center">
                        <img src='<c:url value="/img/calendarIcon.svg"/>' class="w-75" alt="calendar icon">
                    </div>
                    <div class="col p-0">
                        <p class="m-0 white-text"><c:choose>
                            <c:when test="${date.dayOfWeek == 1}"><spring:message code="Monday"/></c:when>
                            <c:when test="${date.dayOfWeek == 2}"><spring:message code="Tuesday"/></c:when>
                            <c:when test="${date.dayOfWeek == 3}"><spring:message code="Wednesday"/></c:when>
                            <c:when test="${date.dayOfWeek == 4}"><spring:message code="Thursday"/></c:when>
                            <c:when test="${date.dayOfWeek == 5}"><spring:message code="Friday"/></c:when>
                            <c:when test="${date.dayOfWeek == 6}"><spring:message code="Saturday"/></c:when>
                            <c:when test="${date.dayOfWeek == 7}"><spring:message code="Sunday"/></c:when>
                            <c:otherwise><c:out value="${date.dayOfWeek}"/></c:otherwise>
                        </c:choose><c:out value="${date.dayOfMonth} "/><spring:message code="of"/> <c:choose>
                            <c:when test="${date.monthOfYear == 1}"><spring:message code="January"/></c:when>
                            <c:when test="${date.monthOfYear == 2}"><spring:message code="February"/></c:when>
                            <c:when test="${date.monthOfYear == 3}"><spring:message code="March"/></c:when>
                            <c:when test="${date.monthOfYear == 4}"><spring:message code="April"/></c:when>
                            <c:when test="${date.monthOfYear == 5}"><spring:message code="May"/></c:when>
                            <c:when test="${date.monthOfYear == 6}"><spring:message code="June"/></c:when>
                            <c:when test="${date.monthOfYear == 7}"><spring:message code="July"/></c:when>
                            <c:when test="${date.monthOfYear == 8}"><spring:message code="August"/></c:when>
                            <c:when test="${date.monthOfYear == 9}"><spring:message code="September"/></c:when>
                            <c:when test="${date.monthOfYear == 10}"><spring:message code="October"/></c:when>
                            <c:when test="${date.monthOfYear == 11}"><spring:message code="November"/></c:when>
                            <c:when test="${date.monthOfYear == 12}"><spring:message code="December"/></c:when>
                            <c:otherwise><c:out value="${date.monthOfYear}"/></c:otherwise>
                        </c:choose>, <c:if test="${date.hourOfDay < 10}">0</c:if><c:out
                                value="${date.hourOfDay}"/>:<c:if test="${date.minuteOfHour < 10}">0</c:if><c:out
                                value="${date.minuteOfHour}hs"/></p>
                        <a href="<c:url value="/appointment/${staffId}/0"/> "><small class="white-text"><spring:message
                                code="ChangeDate"/></small></a>
                    </div>
                </div>
                <div class="row justify-content-center border-top border-light py-2">
                    <div class="col-3 d-flex align-items-center justify-content-center">
                        <img src='<c:url value="/img/mapIcon.svg"/> ' class="w-75" alt="map icon">
                    </div>
                    <div class="col p-0">
                        <p class="m-0 white-text"><c:out value="${staff.office.street}"/></p>
                        <small class="white-text"><c:out value="${staff.office.locality.name}"/></small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="<c:url value="/js/scripts/AppointmentRequest.js"/>"></script>
<script>
    AppointmentRequest.init();
</script>
</html>
