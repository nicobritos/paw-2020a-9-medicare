<nav class="navbar navbar-expand header">
  <!-- TODO -->
  <div class="container w-100 justify-content-start">
    <img
      class="navbar-brand"
      src='<c:url value="/css/whiteLogo.svg"/>'
      alt="logo"
      id="navbar-logo"
    />
    <a class="navbar-brand header-brand header-a-element" href='<c:url value="/"/>'
      >MediCare</a
    >
  </div>
  <div class="container w-100 justify-content-end">
      <%--TODO:connect to correct link--%>
    <c:choose>
      <c:when test="${user.get().staffs.isEmpty()}">
      <a href="<c:url value="/patient/home"/> " class="header-a-element nav-link mx-3"
        >Mis turnos</a
      >
      </c:when>
      <c:otherwise>
        <a href="<c:url value="/staff/home"/> " class="header-a-element nav-link mx-3"
        >Mis turnos</a
        >
      </c:otherwise>
    </c:choose>
    <a href="<c:url value="/mediclist/1"/>" class="header-a-element nav-link mx-3"
      >Buscar médicos</a
    >
    <div class="d-inline-flex flex-column align-items-end">
      <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">${user.get().firstName} ${user.get().surname}</p>
      <a href="<c:url value="/logout"/>" class="m-0 p-0 header-a-element"><small class="m-0 p-0">Cerrar sesión</small></a>
    </div>
    <%--TODO:connect correct image--%>
    <img id="navbarUserImage" class="ml-2" src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="">
  </div>
</nav>