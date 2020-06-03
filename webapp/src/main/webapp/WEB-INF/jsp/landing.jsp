<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <%@ include file="../partials/head.jsp" %>
    <link rel="stylesheet" type="text/css" href='<c:url value="/css/landing.css"/>'>
</head>
<body>
<%@ include file="navbar/navbar.jsp" %>
<div class="container w-100 ml-4 mb-5">
    <h2 class="display-5 mt-5 green-text"><spring:message code="FindingMedicQuickAndEasy"/></h2>
</div>
<div class="container h-50 justify-content-center">
    <form class="filter-form p-3" action="<c:url value="/mediclist/1"/>">
        <div class="form-row">
            <div class="col">
                <h2 class="ml-5 mt-3 form-title"><spring:message code="SearchMedics"/></h2>
            </div>
        </div>
        <div class="form-row justify-content-center justify-content-around mt-3">
            <div class="col-5 pl-5">
                <label for="name"></label><input class="w-100 form-control" type="text" name="name" id="name"
                                                 placeholder="<spring:message code="NameAndOrSurname"/>">
            </div>
            <div class="col">
                <label for="specialties">
                </label>
                <select name="specialties" class="select-css form-control" id="specialties">
                    <option value="-1" disabled selected><spring:message code="Specialty"/></option>
                    <option value="-1"><spring:message code="Any"/></option>
                    <c:forEach var="specialty" items="${specialties}">
                        <option value="<c:out value="${specialty.id}"/>"><c:out value="${specialty.name}"/></option>
                    </c:forEach>
                </select>

            </div>
            <div class="col pr-5">
                <label for="localities">
                </label>
                <select name="localities" class="select-css form-control" id="localities">
                    <option value="-1" disabled selected><spring:message code="Locality"/></option>
                    <option value="-1"><spring:message code="Any"/></option>
                    <c:forEach var="locality" items="${localities}">
                        <option value="<c:out value="${locality.id}"/>"><c:out value="${locality.name}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-row px-5 mt-4 mb-3">
            <button class="w-100 btn rounded-pill btn-light header-btn-element"><spring:message
                    code="SearchMedics"/></button>
        </div>
    </form>
</div>
</body>
</html>
