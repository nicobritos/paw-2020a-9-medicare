<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html lang="en">
  <head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/patientProfile.css"/> ' />
  </head>
  <body class="d-flex flex-column">
    <%--TODO use navbarLogged--%>
    <%@ include file="navbarNotLogged.jsp" %>
      <div class="container flex-fill mx-5 pl-5 mt-3 w-100">
        <div class="row">
          <div class="col-4 align-items-start d-flex flex-column">
            <!-- TODO connect imagen -->
            <img id="profilePic" src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="">
            <!-- TODO connect-->
            <a href="">Cambiar foto de perfil</a>
            <!-- TODO Connect-->
            <a class="mt-3" href="">Cambiar contraseña</a>
          </div>
          <div class="col-6">
            <div class="container p-0 pt-4 m-0">
              <div class="row">
                <div class="col p-0 m-0">
            <!-- TODO Connect image function-->
                  <h3>Nombre/s <img type="button" href="#" src='<c:url value="/css/editPencil.svg"/>' alt="editar"></h3>
            <!-- TODO Connect nombre-->
                  <p>Guido</p>
                </div>
                <div class="col p-0 m-0">
            <!-- TODO Connect image function-->
                  <h3>Apellido <img type="button" href="#" src='<c:url value="/css/editPencil.svg"/>' alt="editar"></h3>
            <!-- TODO Connect apellido-->
                  <p>Barbieri</p>
                </div>
              </div>
              <div class="row">
                <div class="col p-0 m-0">
            <!-- TODO Connect image function-->
                  <h3>Telefono<img type="button" href="#" src='<c:url value="/css/editPencil.svg"/>' alt="editar"></h3>
            <!-- TODO Connect telefono-->
                  <p>#########</p>
                </div>
                <div class="col p-0 m-0">
            <!-- TODO Connect image function-->
                  <h3>Email <img type="button" href="#" src='<c:url value="/css/editPencil.svg"/>' alt="editar"></h3>
            <!-- TODO Connect email-->
                  <p>guido_gucci@hotmail.com</p>
                </div>
              </div>
            </div>
          </div>
          <div class="col-2">
          </div>
        </div>
      </div>
  </body>
</html>
