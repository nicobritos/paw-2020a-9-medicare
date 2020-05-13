<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="../head.jsp" %>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/addTurno.css"/>">
</head>
<body>
<div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
    <c:url var="addTurnUrl" value="/staff/profile/workday"/>
    <form:form modelAttribute="workdayForm" class="addturn-form border p-5 rounded" action="${addTurnUrl}"
               method="post">


        <div class="row">
            <h6>Medicare <img src='<c:url value="/img/logo.svg"/> ' id="logo" alt="logo"/></h6>
        </div>
        <div class="row justify-content-start">
            <h1 class="addturn-form-title"><spring:message code="AddTurno"/></h1>
        </div>

        <div class="form-group row">
            <div class="col">
                <label for="dow"><spring:message code="DayOfWeek"/></label>
            </div>
            <div class="col-8">
                <form:select class="form-control" name="dow" id="dow" path="dow">
                    <form:option selected="true" value="1"><spring:message code="Monday"/></form:option>
                    <form:option value="2"><spring:message code="Tuesday"/></form:option>
                    <form:option value="3"><spring:message code="Wednesday"/></form:option>
                    <form:option value="4"><spring:message code="Thursday"/></form:option>
                    <form:option value="5"><spring:message code="Friday"/></form:option>
                    <form:option value="6"><spring:message code="Saturday"/></form:option>
                    <form:option value="7"><spring:message code="Sunday"/></form:option>
                </form:select>
            </div>
        </div>

        <div class="form-group row">
            <div class="col">
                <label for="startHour"><spring:message code="StartingHour"/></label>
            </div>
            <div class="col-8">
                <form:input class="form-control" type="time" name="startHour" id="startHour" path="startHour"/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col">
                <label for="endHour"><spring:message code="FinishingHour"/></label>
            </div>
            <div class="col-8">
                <form:input class="form-control" type="time" name="endHour" id="endHour" path="endHour"/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col">
                <label for="officeId"><spring:message code="Office"/></label>
            </div>
            <div class="col-8">
                <form:select class="form-control" name="officeId" id="officeId" path="officeId">
                    <c:forEach items="${staffs}" var="staff">
                        <form:option value="${staff.office.id}"><c:out value="${staff.office.name}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <div class="form-row justify-content-between">
            <a href="<c:url value="/staff/profile"/>">
                <form:button class="form-atras-btn btn" type="button"><spring:message
                        code="Back"/></form:button></a><form:button type="submit"
                                                                    class="btn btn-primary"><spring:message code="Add"/></form:button>
        </div>
        <form:errors path="*" cssClass="mt-4 mb-0 text-danger" element="p"/>
    </form:form>
</div>
</body>
</html>
