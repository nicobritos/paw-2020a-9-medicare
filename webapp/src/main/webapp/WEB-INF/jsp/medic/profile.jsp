<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html lang="en">
<head>
    <%@ include file="../../partials/head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/staff/medicProfile.css"/> '/>
</head>
<body class="d-flex flex-column">
<%@ include file="../navbar/navbarLogged.jsp" %>
<div class="container flex-fill mx-5 pl-5 mt-3 w-100">
    <div class="row">
        <div class="col-4 align-items-start d-flex flex-column">
            <div class="picture-container no-select">
                <div class="w-100 d-flex flex-column justify-content-center">
                    <div class="profile-picture-container">
                        <div style="margin-top: 100%;"></div>
                        <img
                                id="profilePic"
                                class="profile-picture rounded-circle"
                                src="<c:url value="/profilePics/${user.get().profilePicture.id}"/>"
                                alt=""
                        />
                    </div>
                </div>
                <div class="picture-overlay d-flex flex-column align-items-center justify-content-end pb-3">
                    <input id="profile-picture-input" style="display: none;" type="file" accept="image/*">
                    <i class="fas fa-pencil-alt"></i>
                </div>
            </div>
            <!-- TODO Connect-->
            <%--<a class="mt-3" href=""><spring:message code="ChangePassword"/></a>--%>
        </div>
        <div class="col-6">
            <div class="container p-0 pt-4 m-0">
                <c:url value="/staff/profile" var="staffProfileUrl"/>
                <form:form modelAttribute="medicProfileForm" action="${staffProfileUrl}" method="post">
                    <div class="row">
                        <div class="col p-0 m-0">
                            <h3><spring:message code="Name"/> <label for="firstName" class="toggle-readonly"><img
                                    type="button" src='<c:url value="/img/editPencil.svg"/>' alt="editar"/></label></h3>
                            <form:input class="form-control mb-3 w-75" id="firstName" name="firstName"
                                        value="${user.get().firstName}" path="firstName" readonly="true"/>
                        </div>
                        <div class="col p-0 m-0">
                            <h3><spring:message code="Surname"/> <label for="surname" class="toggle-readonly"><img
                                    type="button" src='<c:url value="/img/editPencil.svg"/>' alt="editar"/></label></h3>
                            <form:input class="form-control mb-3 w-75" name="surname" id="surname"
                                        value="${user.get().surname}" path="surname" readonly="true"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col p-0 m-0">
                            <h3><spring:message code="Phone"/><label for="phone" class="toggle-readonly"><img
                                    type="button" src='<c:url value="/img/editPencil.svg"/>' alt="editar"/></label></h3>
                            <label for="phone"></label><form:input class="form-control mb-3 w-75" id="phone"
                                                                   name="phone"
                                                                   value="${user.get().phone}" path="phone"
                                                                   readonly="true"/>
                        </div>
                        <div class="col p-0 m-0">
                            <h3><spring:message code="Email"/> <label for="email" class="toggle-readonly"><img
                                    type="button" src='<c:url value="/img/editPencil.svg"/>' alt="editar"/></label></h3>
                            <form:input class="form-control mb-3 w-75" id="email" name="email"
                                        value="${user.get().email}" path="email" readonly="true"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col p-0 m-0">
                            <h3><spring:message code="Password"/> <label for="password" class="toggle-readonly"><img
                                    type="button" src='<c:url value="/img/editPencil.svg"/>' alt="editar"/></label></h3>
                            <form:input type="password" class="form-control mb-3 w-75" id="password" name="password"
                                        path="password" readonly="true"/>
                            <label for="password" class="toggle-visibility"><img src='<c:url value="/img/eye.svg"/> '
                                                                                 style="display: none;"><img
                                    src='<c:url value="/img/noeye.svg"/>' style="display: none;"></label>
                        </div>
                        <div class="col p-0 m-0" id="repeat-password-container" style="display: none">
                            <h3><spring:message code="RepeatPassword"/></h3>
                            <label for="repeatPassword" class="toggle-readonly">
                                    <form:input visible="false" type="password" class="form-control mb-3 w-75"
                                                id="repeatPassword" name="repeatPassword" path="repeatPassword"
                                                readonly="true"/>
                                <label for="repeatPassword" class="toggle-visibility"><img
                                        src='<c:url value="/img/eye.svg"/> ' style="display: none;"><img
                                        src='<c:url value="/img/noeye.svg"/>' style="display: none;"></label>
                        </div>
                    </div>
                    <form:errors path="*" cssClass="mt-4 mb-0 text-danger" element="p"/>
                    <div class="form-row justify-content-between align-items-end mt-2">
                        <button type="submit" class="btn btn-info"><spring:message code="ConfirmChanges"/></button>
                    </div>
                </form:form>
                <div class="row mb-3">
                    <h3><spring:message code="Office"/></h3>
                    <c:forEach var="staff" items="${staffs}">
                        <div class="container p-0 m-0 pl-3">
                            <div class="row d-flex align-items-center justify-content-between">
                                <p class="m-0"><c:out value="- ${staff.office.name} - TEL: ${staff.office.phone}"/></p>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="row">
                    <h3><spring:message code="Schedule"/></h3>
                    <div class="container p-0 m-0 pl-3">
                        <div class="row d-flex align-items-center justify-content-between">
                            <c:if test="${workdays.isEmpty()}">
                                <div class="container-fluid justify-content-center">
                                    <p class="text-center mt-2" style="color:grey;"><spring:message code="NoSchedule"/></p>
                                </div>
                            </c:if>
                            <c:forEach var="workday" items="${workdays}">
                                <p class="m-0">-
                                    <c:choose>
                                        <%-- TODO: maybe use spring:message directly instead of with choose --%>
                                        <c:when test="${workday.day == 'MONDAY'}"><spring:message code="Monday" var="wkDay"/></c:when>
                                        <c:when test="${workday.day == 'TUESDAY'}"><spring:message code="Tuesday" var="wkDay"/></c:when>
                                        <c:when test="${workday.day == 'WEDNESDAY'}"><spring:message code="Wednesday" var="wkDay"/></c:when>
                                        <c:when test="${workday.day == 'THURSDAY'}"><spring:message code="Thursday" var="wkDay"/></c:when>
                                        <c:when test="${workday.day == 'FRIDAY'}"><spring:message code="Friday" var="wkDay"/></c:when>
                                        <c:when test="${workday.day == 'SATURDAY'}"><spring:message code="Saturday" var="wkDay"/></c:when>
                                        <c:when test="${workday.day == 'SUNDAY'}"><spring:message code="Sunday" var="wkDay"/></c:when>
                                        <c:otherwise><c:set value="${workday.day}" var="wkDay"/></c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${workday.startHour < 10}"><c:set value="0${workday.startHour}" var="wkStartHour"/></c:when>
                                        <c:otherwise><c:set value="${workday.startHour}" var="wkStartHour"/></c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${workday.startMinute < 10}"><c:set value="0${workday.startMinute}" var="wkStartMinute"/></c:when>
                                        <c:otherwise><c:set value="${workday.startMinute}" var="wkStartMinute"/></c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${workday.endHour < 10}"><c:set value="0${workday.endHour}" var="wkEndHour"/></c:when>
                                        <c:otherwise><c:set value="${workday.endHour}" var="wkEndHour"/></c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${workday.endMinute < 10}"><c:set value="0${workday.endMinute}" var="wkEndMinute"/></c:when>
                                        <c:otherwise><c:set value="${workday.endMinute}" var="wkEndMinute"/></c:otherwise>
                                    </c:choose>
                                    <spring:message code="wkd_from_wksh_wksm_to_wkeh_wkem_cons" argumentSeparator=";" arguments="${wkDay};${wkStartHour};${wkStartMinute};${wkEndHour};${wkEndMinute};${workday.staff.office.name}"/>
                                </p>
                                <c:choose>
                                    <c:when test="${appointmentMap.get(workday) == 1}">
                                        <spring:message code='YouWillDeleteAppt' javaScriptEscape='true' var="appts" arguments="${appointmentMap.get(workday)}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code='YouWillDeleteAppts' javaScriptEscape='true' var="appts" arguments="${appointmentMap.get(workday)}"/>
                                    </c:otherwise>
                                </c:choose>
                                <form action="<c:url value="/staff/profile/workday/delete/${workday.id}"/>"
                                      method="post" class="cancel-workday-form"
                                      data-appointment_url="<c:url value="/staff/appointment/workday/${workday.id}"/>"

                                      data-appts="${appts}"
                                >
                                    <button class="btn cancel-workday-btn" type="button">X</button>
                                </form>
                            </c:forEach>
                        </div>
                        <div class="row d-flex align-items-center justify-content-center my-3">
                            <a href="<c:url value="/staff/profile/workday"/>" type="button"
                               class="btn btn-info"><spring:message code="AddSchedule"/></a>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <h3><spring:message code="Specialties"/></h3>
                    <c:if test="${specialties.isEmpty()}">
                        <div class="container-fluid justify-content-center">
                            <p class="text-center mt-2" style="color:grey;"><spring:message code="NoSpecialties"/></p>
                        </div>
                    </c:if>
                    <c:forEach var="specialty" items="${specialties}">
                        <div class="container p-0 m-0 pl-3">
                            <div class="row d-flex align-items-center justify-content-between">
                                <p class="m-0"><c:out value="${specialty.name}"/></p>
                                <form action="<c:url value="/staff/profile/specialty/delete/${specialty.id}"/>"
                                      method="post" class="cancel-specialty-form">
                                    <button class="btn cancel-specialty-btn" type="button">X</button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="row d-flex align-items-center justify-content-center my-3">
                    <c:url value="/staff/profile/specialty" var="addSpecialtyUrl"/>
                    <a href="${addSpecialtyUrl}" type="button" class="btn btn-info"><spring:message code="AddSpecialty"/></a>
                </div>
            </div>
        </div>
        <div class="col-2">
        </div>
    </div>
</div>
<script type="text/javascript">
    let workday_strings = new Array();
    workday_strings['title'] = "<spring:message code='YouAreAboutToCancelAWorkday' javaScriptEscape='true' />";
    workday_strings['body'] = "<spring:message code='DoYouWantToContinue' javaScriptEscape='true' />";
    workday_strings['accept'] = "<spring:message code='Accept' javaScriptEscape='true' />";
    workday_strings['cancel'] = "<spring:message code='Cancel' javaScriptEscape='true' />";
    workday_strings['deleted'] = "<spring:message code='Deleted' javaScriptEscape='true' />";
    workday_strings['deleted_body'] = "<spring:message code='OperationCompletedSuccessfully' javaScriptEscape='true' />";
    workday_strings['title2'] = "";
    workday_strings['body2'] = "<spring:message code='DoYouWantToCancelFutureAppointmentsThatMatchInThisWorkday' javaScriptEscape='true' />";
    workday_strings['accept2'] = "<spring:message code='Yes' javaScriptEscape='true' />";
    workday_strings['cancel2'] = "<spring:message code='No' javaScriptEscape='true' />";
    let specialty_strings = new Array();
    specialty_strings['title'] = "<spring:message code='YouAreAboutToDeleteASpecialty' javaScriptEscape='true' />";
    specialty_strings['body'] = "<spring:message code='DoYouWantToContinue' javaScriptEscape='true' />";
    specialty_strings['accept'] = "<spring:message code='Accept' javaScriptEscape='true' />";
    specialty_strings['cancel'] = "<spring:message code='Cancel' javaScriptEscape='true' />";
    specialty_strings['deleted'] = "<spring:message code='Deleted' javaScriptEscape='true' />";
    specialty_strings['deleted_body'] = "<spring:message code='OperationCompletedSuccessfully' javaScriptEscape='true' />";
    let error_message = new Array();
    error_message['error'] = "<spring:message code='ThereWasAnError' javaScriptEscape='true' />";
</script>
<script src='<c:url value="/js/scripts/Profile.js"/> '></script>
<script>
    $(document).ready(() => {
        Profile.init()
    });
</script>
</body>
</html>
