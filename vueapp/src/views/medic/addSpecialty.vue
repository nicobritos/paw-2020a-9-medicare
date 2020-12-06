<html>
<head>
    <%@ include file="../../partials/head.jsp" %>
    <link rel="stylesheet" type="text/css" href="<c:url value="
    /css/addTurno.css"/>">
</head>
<body>
<div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
    <c:url var="addSpecialtyUrl" value="/doctor/profile/specialty"/>
    <form:form modelAttribute="specialtyForm" class="addturn-form border p-5 rounded" action="${addSpecialtyUrl}"
               method="post">
        <div class="row">
            <h6>Medicare <img src='<c:url value="/img/logo.svg"/> ' id="logo" alt="logo"/></h6>
        </div>
        <div class="row justify-content-start">
            <h1 class="addturn-form-title">
                <spring:message code="AddSpecialty"/>
            </h1>
        </div>

        <div class="form-group row">
            <div class="col">
                <label for="specialtyId">
                    <spring:message code="Specialty"/>
                </label>
            </div>
            <div class="col-8">
                <form:select class="form-control" name="specialtyId" id="specialtyId" path="specialtyId">
                    <c:forEach var="specialty" items="${specialties}">
                        <form:option value="${specialty.id}">
                            <c:out value="${specialty.name}"/>
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
