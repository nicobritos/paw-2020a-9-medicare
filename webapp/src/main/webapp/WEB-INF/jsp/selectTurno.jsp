<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/selectTurno.css"/>' />
  </head>
  <body class="container-fluid d-flex flex-column p-0">
  <%@ include file="navbar/navbar.jsp" %>
    <div class="container ml-0 mr-0 pr-0 fill-height">
      <div class="row h-100">
        <div class="col-4 h-100 grey-background">
          <%-- TODO:connect--%>
          <div class="row mt-4">
            <div class="col-4">
              <%-- TODO:conect image--%>
              <img class="w-100" src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="">
            </div>
            <div class="col mr-3">
              <div class="row mt-2">
                <h5>${staff.firstName} ${staff.surname} </h5>
              </div>
              <div class="row mt-3 d-flex justify-content-start">
                <c:forEach items="${staff.staffSpecialties}" var="specialty">
                  <p>${specialty}</p>
                </c:forEach>
              </div>
            </div>
          </div>
          <div class="row mt-3 pl-4">
            <p>Direccíon: ${staff.office.street} - ${staff.office.locality}</p>
          </div>
          <div class="row pl-4">
            <p>Teléfono: ${staff.office.phone}</p>
          </div>
          <div class="row pl-4">
            <p>Email: ${staff.email}</p>
          </div>
        </div>
        <div class="col ml-5 mt-3 p-0">
          <div class="row">
            <h4>Seleccionar turno</h4>
          </div>
          <div class="row">
            <div class="col-1 p-0">
              <%-- TODO:connect button left--%>
              <button type="button" class="btn"><</button>
            </div>
            <c:forEach var="i" begin="0" end="5">
            <div class="col-1 mr-4 p-0">
              <a href="<c:url value="/patient/appointment/${staff.id}/${monday.plusDays(i).year}/${monday.plusDays(i).dayOfYear}"/>">
                <span class="d-flex flex-column align-items-center">
                  <!-- day of the week -->
                  <p class="mb-0">${monday.plusDays(i).dayOfWeek.name()}</p>
                  <!-- day/month -->
                  <p class="my-0">${monday.plusDays(i).dayOfMonth} de ${monday.plusDays(i).month.name()}</p>
                  <!-- TODO:connect boton para seleccionar dia-->
                </span>

              <%-- TODO:connect workdays variables por dia --%>
              <c:forEach var="workday" items="${staff.workdays}">
                <button class="btn btn-sm btn-secondary mb-2">${workday.startHour}:${workday.startMinute}hs</button>
              </c:forEach>
              </a>
            </div>
            </c:forEach>
            <%-- TODO:connect boton adelante --%>
            <div class="col-1 p-0 flex-shrink-1"><button class="btn">></button></div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
