<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file = "../head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/reservarTurno.css"/>' />
  </head>
  <body class="container-fluid p-0 m-0 d-flex flex-column">
  <%@ include file="../navbar/navbarLogged.jsp" %>
  <div class="container fill-height">
      <div class="row mt-4">
        <%-- TODO:connect form--%>
        <form class="col d-flex flex-column">
          <h4 class="text-muted">Reserva de turnos</h4>
          <p class="mt-3 text-muted">Motivos</p>
          <%-- TODO:connect input--%>
          <label for="motivo"></label><input placeholder="Motivo de consulta" value="Consulta <c:forEach var="specialty" items="${staff.staffSpecialties}">${specialty.name}</c:forEach>" type="text" name="motivo" id="motivo" class="form-control w-50"/>
          <p class="mt-3 text-muted mb-1">Datos personales</p>
          <div class="container-fluid p-0 d-flex flex-row">
            <div class="col px-0">
              <%-- TODO:connect nombre--%>
              <input placeholder="Nombre" value="${user.get().firstName}" type="text" name="motivo" id="motivo" class="form-control">
            </div>
            <div class="col p-0 ml-2">
              <%-- TODO:connect apellido--%>
              <input placeholder="Apellido" value="${user.get().surname}" type="text" name="motivo" id="motivo" class="form-control">
            </div>
          </div>
          <%-- TODO:connect telefono--%>
          <input placeholder="Teléfono" type="text" name="telefono" id="telefono" class="form-control mt-3 w-50">
          <%-- TODO:connect email--%>
          <input placeholder="Email" value="${user.get().email}" type="text" name="email" id="email" class="form-control mt-3 w-50">
          <%-- TODO:connect comentario--%>
          <textarea placeholder="Comentario (opcional)" class="form-control mt-3" name="comentarios" id="comentarios" cols="30" rows="5"></textarea>
          <button type="submit" class="btn btn-info mt-3 w-100">Reservar turno</button>
        </form>
        <div class="col">
          <div class="container details-container mt-5 p-3 w-75">
            <div class="row justify-content-center">
              <h4 class="white-text">Detalles de consulta</h4>
            </div>
            <div class="row justify-content-center border-top border-light py-2">
              <div class="col-3">
                <%-- TODO: connect profile pic --%>
                <img src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" class="w-100" alt="profile pic">
              </div>
              <div class="col p-0">
                <p class="m-0 white-text">${staff.firstName} ${staff.surname}</p>
                <c:forEach var="specialty" items="${staff.staffSpecialties}">
                  <small class="white-text">${specialty.name}</small>
                </c:forEach>
              </div>
            </div>
            <div class="row justify-content-center border-top border-light py-2">
              <div class="col-3 d-flex align-items-center justify-content-center">
                <img src='<c:url value="/css/calendarIcon.svg"/>' class="w-75" alt="calendar icon">
              </div>
              <div class="col p-0">
                <%-- TODO: connect hora--%>
                <p class="m-0 white-text">${date.dayOfWeek.name()} ${date.dayOfMonth} de ${date.month.name()}, 20:00hs</p>
                  <a href="<c:url value="/appointment/${staffId}"/> "><small class="white-text">Cambiar fecha</small></a>
              </div>
            </div>
            <div class="row justify-content-center border-top border-light py-2">
              <div class="col-3 d-flex align-items-center justify-content-center">
                <img src='<c:url value="/css/mapIcon.svg"/> ' class="w-75" alt="map icon">
              </div>
              <div class="col p-0">
                <p class="m-0 white-text">${staff.office.street}</p>
                  <%-- TODO: connect localidad --%>
                  <small class="white-text">${staff.office.locality.name}</small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
