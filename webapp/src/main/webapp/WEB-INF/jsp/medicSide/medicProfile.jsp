<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html lang="en">
<head>
    <%@ include file="../head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/staff/medicProfile.css"/> '/>
</head>
<body class="d-flex flex-column">
<%@ include file="../navbar/navbarLogged.jsp" %>
<div class="container flex-fill mx-5 pl-5 mt-3 w-100">
    <div class="row">
        <div class="col-4 align-items-start d-flex flex-column">
            <!-- TODO check imagen -->
            <div class="picture-container no-select">
                <div class="w-100 d-flex flex-column justify-content-center">
                    <div class="profile-picture-container">
                        <div style="margin-top: 100%;"></div>
                        <img
                                id="profilePic"
                                class="profile-picture rounded-circle"
                                src="<c:url value="/profilePics/${user.get().profileId}"/>"
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
                            <label for="phone"></label><form:input class="form-control mb-3 w-75" id="phone" name="phone"
                                                                  value="${user.get().phone}" path="phone" readonly="true"/>
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
                            <c:forEach var="staff" items="${staffs}">
                                <c:forEach var="workday" items="${workdays}">
                                    <p class="m-0">- <c:choose>
                                        <%-- TODO: maybe use spring:message directly instead of with choose --%>
                                        <c:when test="${workday.day == 'MONDAY'}"><spring:message
                                                code="Monday"/></c:when>
                                        <c:when test="${workday.day == 'TUESDAY'}"><spring:message
                                                code="Tuesday"/></c:when>
                                        <c:when test="${workday.day == 'WEDNESDAY'}"><spring:message
                                                code="Wednesday"/></c:when>
                                        <c:when test="${workday.day == 'THURSDAY'}"><spring:message
                                                code="Thursday"/></c:when>
                                        <c:when test="${workday.day == 'FRIDAY'}"><spring:message
                                                code="Friday"/></c:when>
                                        <c:when test="${workday.day == 'SATURDAY'}"><spring:message
                                                code="Saturday"/></c:when>
                                        <c:when test="${workday.day == 'SUNDAY'}"><spring:message
                                                code="Sunday"/></c:when>
                                        <c:otherwise>${workday.day}</c:otherwise>
                                    </c:choose> <spring:message code="of"/> <c:if
                                            test="${workday.startHour < 10}">0</c:if><c:out value="${workday.startHour}"/>:<c:if
                                            test="${workday.startMinute < 10}">0</c:if><c:out value="${workday.startMinute}hs a "/><c:if
                                            test="${workday.endHour < 10}">0</c:if><c:out value="${workday.endHour}:"/><c:if
                                            test="${workday.endMinute < 10}">0</c:if><c:out value="${workday.endMinute}hs - ${workday.staff.office.name}"/></p>
                                    <a href='<c:url value="/staff/profile/workday/delete/${workday.id}"/> '
                                       class="btn deleteWorkday">X</a>
                                </c:forEach>
                            </c:forEach>
                        </div>
                        <div class="row d-flex align-items-center justify-content-center my-3">
                            <a href="<c:url value="/staff/profile/workday"/>" type="button"
                               class="btn btn-info"><spring:message code="AddSchedule"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-2">
        </div>
    </div>
</div>
<script src='<c:url value="/js/scripts/staff/medicProfile.js"/> '></script>
<script>Profile.init()</script>
</body>
</html>
