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
    <c:if test="${user.get().verified}">
      <a href="<c:url value="/home"/> " class="header-a-element nav-link mx-3"
      ><spring:message code="MyAppointments"/></a
      >
    </c:if>
    <c:if test="${user.get().verified && staffs == null}">
      <a href="<c:url value="/mediclist/1"/>" class="header-a-element nav-link mx-3"
      ><spring:message code="SearchMedics"/></a
      >
    </c:if>

    <div class="d-inline-flex flex-column align-items-end">
      <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">${user.get().firstName} ${user.get().surname}</p>
      <a href="<c:url value="/logout"/>" class="m-0 p-0 header-a-element"><small class="m-0 p-0"><spring:message code="Logout"/></small></a>
    </div>
    <%--TODO:connect correct image--%>
        <c:choose>
          <c:when test="${!user.get().verified}">
            <img id="navbarUnverifiedUserImage" class="ml-2" src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="">
          </c:when>
          <c:when test="${staffs == null}">
            <a href="<c:url value="/patient/profile"/>">
              <%-- TODO:move style to css --%>
              <%-- TODO: check image--%>
              <img id="navbarPatientUserImage" class="ml-2 rounded-circle" src="<c:url value="/profilePics/${user.get().profileId}"/>" alt="" style="height: 2em;">
            </a>
          </c:when>
          <c:otherwise>
            <a href="<c:url value="/staff/profile"/>">
                <%-- TODO:move style to css --%>
                <%-- TODO: check image--%>
              <img id="navbarStaffUserImage" class="ml-2 rounded-circle" src="<c:url value="/profilePics/${user.get().profileId}"/>" alt="" style="height: 2em;">
            </a>
          </c:otherwise>
        </c:choose>
  </div>
</nav>