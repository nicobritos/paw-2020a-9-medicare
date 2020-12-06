<html>
<head>
    <%@ include file="../../partials/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="<c:url value="
    /css/addTurno.css"/>">
    <link rel="stylesheet" type="text/css"
          href="https://cdn.jsdelivr.net/npm/pretty-checkbox@3.0/dist/pretty-checkbox.min.css">
</head>
<body>
<div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
    <c:url var="addTurnUrl" value="/doctor/profile/workday"/>
    <form:form modelAttribute="workdayForm" class="addturn-form border p-5 rounded" action="${addTurnUrl}"
               method="post">


        <div class="row">
            <h6>Medicare <img src='<c:url value="/img/logo.svg"/> ' id="logo" alt="logo"/></h6>
        </div>
        <div class="row justify-content-start">
            <h1 class="addturn-form-title">
                <spring:message code="AddTurno"/>
            </h1>
        </div>

        <div class="form-group row">
            <div class="col">
                <label>
                    <spring:message code="DayOfWeek"/>
                </label>
            </div>
            <div class="col-8">
                <div class="pretty p-icon p-round p-smooth">
                    <form:checkbox path="dow[0]"/>
                    <div class="state p-success">
                        <i class="icon mdi mdi-check"></i>
                        <label>
                            <spring:message code="Monday"/>
                        </label>
                    </div>
                </div>
                <div class="pretty p-icon p-round p-smooth">
                    <form:checkbox path="dow[1]"/>
                    <div class="state p-success">
                        <i class="icon mdi mdi-check"></i>
                        <label>
                            <spring:message code="Tuesday"/>
                        </label>
                    </div>
                </div>
                <div class="pretty p-icon p-round p-smooth">
                    <form:checkbox path="dow[2]"/>
                    <div class="state p-success">
                        <i class="icon mdi mdi-check"></i>
                        <label>
                            <spring:message code="Wednesday"/>
                        </label>
                    </div>
                </div>
                <div class="pretty p-icon p-round p-smooth">
                    <form:checkbox path="dow[3]"/>
                    <div class="state p-success">
                        <i class="icon mdi mdi-check"></i>
                        <label>
                            <spring:message code="Thursday"/>
                        </label>
                    </div>
                </div>
                <div class="pretty p-icon p-round p-smooth">
                    <form:checkbox path="dow[4]"/>
                    <div class="state p-success">
                        <i class="icon mdi mdi-check"></i>
                        <label>
                            <spring:message code="Friday"/>
                        </label>
                    </div>
                </div>
                <div class="pretty p-icon p-round p-smooth">
                    <form:checkbox path="dow[5]"/>
                    <div class="state p-success">
                        <i class="icon mdi mdi-check"></i>
                        <label>
                            <spring:message code="Saturday"/>
                        </label>
                    </div>
                </div>
                <div class="pretty p-icon p-round p-smooth">
                    <form:checkbox path="dow[6]"/>
                    <div class="state p-success">
                        <i class="icon mdi mdi-check"></i>
                        <label>
                            <spring:message code="Sunday"/>
                        </label>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-group row">
            <div class="col">
                <label for="startHour">
                    <spring:message code="StartingHour"/>
                </label>
            </div>
            <div class="col-8">
                <form:input class="form-control" type="time" name="startHour" id="startHour" path="startHour"/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col">
                <label for="endHour">
                    <spring:message code="FinishingHour"/>
                </label>
            </div>
            <div class="col-8">
                <form:input class="form-control" type="time" name="endHour" id="endHour" path="endHour"/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col">
                <label for="officeId">
                    <spring:message code="Office"/>
                </label>
            </div>
            <div class="col-8">
                <form:select class="form-control" name="officeId" id="officeId" path="officeId">
                    <c:forEach items="${doctors}" var="doctor">
                        <form:option value="${doctor.office.id}">
                            <c:out value="${doctor.office.name}"/>
                        </form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <div class="form-row justify-content-between">
            <a href="<c:url value="/doctor/profile"/>">
            <form:button class="form-atras-btn btn" type="button">
                <spring:message
                    code="Back"/>
            </form:button>
            </a>
            <form:button type="submit"
                         class="btn btn-primary">
                <spring:message code="Add"/>
            </form:button>
        </div>
        <form:errors path="*" cssClass="mt-4 mb-0 text-danger" element="p"/>
    </form:form>
</div>
</body>
</html>
