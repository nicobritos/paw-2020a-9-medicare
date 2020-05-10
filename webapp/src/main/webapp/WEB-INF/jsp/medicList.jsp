<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="head.jsp" %>

    <link rel="stylesheet" href='<c:url value="/css/loggedMedicList.css"/> '/>
</head>
<body>
<%@ include file="navbar/navbar.jsp" %>

<form action="<c:url value="/mediclist/1"/>">
    <div class="container h-75">
        <div class="row mt-4 justify-content-center">
            <input class="form-control w-100" type="text" name="name" value="${name}" placeholder="<spring:message code="Name"/>"/>
        </div>
        <div class="row mt-4">
            <div class="col-4 px-3">
                <%--          <div class="row">--%>
                <%--            &lt;%&ndash;TODO connect sort search&ndash;%&gt;--%>
                <%--            <select class="form-control w-100" type="text" name="sort" id="sort">--%>
                <%--              <option value="-1" disabled selected>Ordenar por</option>--%>
                <%--            </select>--%>
                <%--          </div>--%>
                <div class="row mt-4">
                    <select class="form-control w-100" type="text" name="specialties" id="selEspecialidad">
                        <option value="-1" disabled <c:if test="${searchedSpecialties.isEmpty()}">selected</c:if>><spring:message code="Specialty"/></option>
                        <option value="-1"><spring:message code="Any"/></option>
                        <c:forEach var="specialty" items="${specialties}">
                            <option value="<c:out value="${specialty.id}" />" <c:if test="${searchedSpecialties.contains(specialty)}">selected</c:if>><c:out value="${specialty.name}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="row mt-4">
                    <select class="form-control w-100" type="text" name="localities" id="localidad">
                        <option value="-1" disabled <c:if test="${searchedLocalities.isEmpty()}">selected</c:if>><spring:message code="Locality"/></option>
                        <option value="-1"><spring:message code="Any"/></option>
                        <c:forEach var="locality" items="${localities}">
                            <option value="<c:out value="${locality.id}"/>" <c:if test="${searchedLocalities.contains(locality)}">selected</c:if>><c:out value="${locality.name}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="row mt-4">
                    <button type="submit" class="btn btn-info w-100 rounded-pill"><spring:message code="Filter"/></button>
                </div>
            </div>
            <div class="col-1"></div>
            <div class="col">
                <ul class="list-group turno-list mr-2 w-100">
                    <c:forEach var="member" items="${staff}">
                        <li class="list-group-item turno-item mb-3">
                            <div class="container">
                                <div class="row">
                                    <div class="col-3 d-flex align-items-center">
                                            <%--TODO check image--%>
                                        <img
                                                class="w-75 rounded-circle"
                                                src="<c:url value="/profilePics/${member.user.id}"/>"
                                                alt=""
                                        />
                                    </div>
                                    <div class="col-6">
                                        <div class="row justify-content-start">
                                            <h5>${member.firstName} ${member.surname}</h5>
                                        </div>
                                        <div class="row">
                                            <p class="m-0">
                                                <c:forEach var="specialty" items="${member.staffSpecialties}">
                                                    <c:out value="${specialty.name}"/>
                                                </c:forEach>
                                            </p>
                                        </div>
                                        <div class="row">
                                            <p class="m-0">${member.office.street}</p>
                                        </div>
                                    </div>
                                    <div class="col d-flex justify-content-center align-items-center">
                                        <a href="<c:url value="/appointment/${member.id}/0"/>">
                                        <button type="button" class="btn btn-info available-appointments-button"
                                                data-id="${member.id}">
                                            <spring:message code="AvailableAppointments"/>
                                        </button>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
                <div id="paging" class="p-3 d-flex container w-100 justify-content-center">
                    <c:if test="${page > 1}">
                        <div>
                            <button type="button" class="btn btn-info btn-sm" id="prevButton"><</button>
                        </div>
                    </c:if>

                    <p class="d-inline mx-2">Página <c:out value="${page}"/></p>
                    <c:if test="${staff.size() == 10}">
                        <div>
                            <button type="button" class="btn btn-info btn-sm" id="nextButton">></button>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</form>

</body>
<script src='<c:url value="/js/scripts/AppointmentRequest.js"/> '></script>
<script src='<c:url value="/js/scripts/AppointmentList.js"/> '></script>
<script>
    let prevButton = document.getElementById("prevButton");
    let nextButton = document.getElementById("nextButton");
    if (prevButton != null) {
        prevButton.onclick = () => {
            location.href = "<c:url value="/mediclist/${page-1}"/>" + location.search;
        }
    }
    if (nextButton != null) {
        nextButton.onclick = () => {
            location.href = "<c:url value="/mediclist/${page+1}"/>" + location.search;
        }
    }

    AppointmentList.init();
</script>
</html>
