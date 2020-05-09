<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-expand header">
  <!-- TODO -->
  <div class="container w-100 justify-content-start">
    <img
      class="navbar-brand"
      src='<c:url value="/img/whiteLogo.svg"/>'
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
      <c:when test="${staffs == null}">
        <a href="<c:url value="/patient/home"/> " class="header-a-element nav-link mx-3"
        ><spring:message code="MyAppointments"/></a
        >
        <a href="<c:url value="/mediclist/1"/>" class="header-a-element nav-link mx-3"
        ><spring:message code="SearchMedics"/></a
        >
      </c:when>
      <c:otherwise>
        <a href="<c:url value="/staff/home"/> " class="header-a-element nav-link mx-3"
        ><spring:message code="MyAppointments"/></a
        >
      </c:otherwise>
    </c:choose>
    <div class="d-inline-flex flex-column align-items-end">
      <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">${user.get().firstName} ${user.get().surname}</p>
      <a href="<c:url value="/logout"/>" class="m-0 p-0 header-a-element"><small class="m-0 p-0"><spring:message code="Logout"/></small></a>
    </div>
    <%--TODO:connect correct image--%>
        <c:choose>
          <c:when test="${staffs == null}">
            <a href="<c:url value="/patient/profile"/>">
              <%-- TODO:move style to css --%>
              <%-- TODO: connect image--%>
              <img id="navbarPatientUserImage" class="ml-2 rounded-circle" src="<c:url value="/profilePics/${user.get().id}"/>" alt="" style="height: 2em;">
            </a>
          </c:when>
          <c:otherwise>
            <a href="<c:url value="/staff/profile"/>">
                <%-- TODO:move style to css --%>
                <%-- TODO: connect image--%>
              <img id="navbarStaffUserImage" class="ml-2 rounded-circle" src="<c:url value="/profilePics/${user.get().id}"/>" alt="" style="height: 2em;">
            </a>
          </c:otherwise>
        </c:choose>
  </div>
</nav>