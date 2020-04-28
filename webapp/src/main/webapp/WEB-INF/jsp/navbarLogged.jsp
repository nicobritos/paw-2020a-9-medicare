<nav class="navbar navbar-expand header">
  <!-- TODO -->
  <div class="container w-100 justify-content-start">
    <img
      class="navbar-brand"
      src='<c:url value="/css/logo.svg"/>'
      alt="logo"
      id="navbar-logo"
    />
    <a class="navbar-brand header-brand header-a-element" href='<c:url value="/"/>'
      >MediCare</a
    >
  </div>
  <div class="container w-100 justify-content-end">
      <%--TODO:connect to correct link--%>
    <a href="/mediclist" class="header-a-element nav-link mx-3"
      >Mis turnos</a
    >
    <%--TODO:connect to correct link--%>
    <a href="/mediclist" class="header-a-element nav-link mx-3"
      >Buscar médicos</a
    >
    <div class="d-inline-flex flex-column align-items-end">
      <%--TODO:connect user name and surname--%>
      <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">Nombre Apellido</p>
      <%--TODO:connect to correct link--%>
      <a href="" class="m-0 p-0 header-a-element"><small class="m-0 p-0">cerrar sesion</small></a>
    </div>
    <%--TODO:connect correct image--%>
    <img id="navbarUserImage" class="ml-2" src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="">
  </div>
</nav>
