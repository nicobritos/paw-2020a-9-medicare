<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
    <head>
        <%@ include file = "head.jsp" %>
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>">
    </head>
    <body>
        <%@ include file = "header.jsp" %>
        <%@ include file = "navbar.jsp" %>
        <br>
        <div style="overflow-x:auto;">
            <table>
                <tr>
                    <th>Apellido, Nombre</th>
                    <th>Especialidad</th>
                    <th>Correo electrónico</th>
                    <th>Teléfono</th>
                    <th>Dirección</th>
                    <th>Localidad</th>
                </tr>
                <c:forEach var="member" items="${staff}">
                    <tr>
                        <td><c:out value="${member.surname}"/>, <c:out value="${member.firstName}"/></td>
                        <td>
                            <c:forEach var="specialty" items="${member.staffSpecialties}">
                                <c:out value="${specialty.name}"/>
                            </c:forEach>
                        </td>
                        <td><c:out value="${member.email}"/></td>
                        <td><c:out value="${member.phone}"/></td>
                        <td><c:out value="${member.office.street}"/></td>
                        <td><c:out value="${member.office.locality.name}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div id="paging" class="p-3 container w-100 justify-content-center">
            <c:if test="${page > 1}">
                <button type="button" class="btn btn-info btn-sm" id="prevButton"><</button>
            </c:if>

            <p class="d-inline mx-2">Página <c:out value="${page}"/></p>
            <c:if test="${staff.size() == 10}">
                <button type="button" class="btn btn-info btn-sm" id="nextButton">></button>
            </c:if>
        </div>
        <script>
            let prevButton = document.getElementById("prevButton");
            let nextButton = document.getElementById("nextButton");
            if(prevButton!=null){
                prevButton.onclick= ()=>{
                    location.href="<c:url value="/mediclist/${page-1}"/>" + location.search;
                }
            }
            if(nextButton!=null){
                nextButton.onclick= ()=>{
                    location.href="<c:url value="/mediclist/${page+1}"/>" + location.search;
                }
            }
        </script>
    </body>
</html>