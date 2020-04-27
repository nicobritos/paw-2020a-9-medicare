<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/selectTurno.css"/>' />
  </head>
  <body class="container-fluid d-flex flex-column p-0">
  <%--TODO use navbarLogged--%>
  <%@ include file="navbarNotLogged.jsp" %>
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
                <%-- TODO:connect name--%>
                <h5>Dr. Nombre Apellido </h5>
              </div>
              <div class="row mt-3 d-flex justify-content-start">
                <%-- TODO:connect specialty--%>
                <p>Especialidad</p>
                <%-- TODO:connect age--%>
                <p class="ml-3">Edad:##</p>
              </div>
            </div>
          </div>
            <%-- TODO:connect direccion--%>
          <div class="row mt-3 pl-4">
            <p>Direccíon: calle siempre viva</p>
          </div>
            <%-- TODO:connect telefono--%>
          <div class="row pl-4">
            <p>Teléfono: ######</p>
          </div>
            <%-- TODO:connect email--%>
          <div class="row pl-4">
            <p>Email: algo@itba.edu.ar</p>
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
            <%-- TODO:connect day--%>
            <div class="col-1 mr-4 p-0">
              <%-- TODO:connect dia de la semana--%>
              <p class="mb-0">Lunes</p>
              <%-- TODO:connect dia del mes--%>
              <p>23 abr</p>
              <%-- TODO:connect boton para seleccionar dia--%>
              <%-- TODO:connect agregar mas botones abajo --%>
              <button class="btn btn-sm btn-secondary mb-2">10:00hs</button>
              <button class="btn btn-sm btn-secondary mb-2">10:00hs</button>
            </div>
            <%-- TODO:connect day--%>
            <div class="col-1 mr-4 p-0">
              <p class="mb-0">Martes</p>
              <p>23 abr</p>
              <button class="btn btn-sm btn-secondary mb-2">10:00hs</button>
            </div>
            <%-- TODO:connect day--%>
            <div class="col-1 mr-4 p-0">
              <p class="mb-0">Miercoles</p>
              <p>23 abr</p>
              <button class="btn btn-sm btn-secondary mb-2">10:00hs</button>
            </div>
            <%-- TODO:connect day--%>
            <div class="col-1 mr-4 p-0">
              <p class="mb-0">Jueves</p>
              <p>23 abr</p>
              <button class="btn btn-sm btn-secondary mb-2">10:00hs</button>
            </div>
            <%-- TODO:connect day--%>
            <div class="col-1 mr-4 p-0">
              <p class="mb-0">Viernes</p>
              <p>23 abr</p>
              <button class="btn btn-sm btn-secondary mb-2">10:00hs</button>
            </div>
            <%-- TODO:connect day--%>
            <div class="col-1 mr-4 p-0">
              <p class="mb-0">Sabado</p>
              <p>23 abr</p>
              <button class="btn btn-sm btn-secondary mb-2">10:00hs</button>
            </div>
            <%-- TODO:connect day--%>
            <div class="col-1 p-0">
              <p class="mb-0">Domingo</p>
              <p>23 abr</p>
              <button class="btn btn-sm btn-secondary mb-2">10:00hs</button>
            </div>
            <%-- TODO:connect boton adelante --%>
            <div class="col-1 p-0 flex-shrink-1"><button class="btn">></button></div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
