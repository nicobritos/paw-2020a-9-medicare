<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en">
  <head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/medicProfile.css"/> ' />
  </head>
  <body class="d-flex flex-column">
    <%@ include file="navbarLogged.jsp" %>
      <div class="container flex-fill mx-5 pl-5 mt-3 w-100">
        <div class="row">
          <div class="col-4 align-items-start d-flex flex-column">
            <!-- TODO connect imagen -->
            <img id="profilePic" src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="">
            <!-- TODO connect-->
<%--            <a href="">Cambiar foto de perfil</a>--%>
            <!-- TODO Connect-->
            <a class="mt-3" href="">Cambiar contraseña</a>
          </div>
          <div class="col-6">
            <div class="container p-0 pt-4 m-0">
                <c:url value="/staff/profile" var="staffProfileUrl"/>
                <form:form modelAttribute="medicProfileForm" action="${staffProfileUrl}" method="post">
                <div class="row">
                <div class="col p-0 m-0">
            <!-- TODO Connect image function-->
                    <h3>Nombre/s <label for="firstName" class="toggle-readonly"><img type="button" src='<c:url value="/css/editPencil.svg"/>' alt="editar"/></label></h3>
                  <form:input class="form-control mb-3 w-75" id="firstName" name="firstName" value="${user.get().firstName}" path="firstName" readonly="true"/>
                </div>
                <div class="col p-0 m-0">
            <!-- TODO Connect image function-->
                    <h3>Apellido <label for="surname" class="toggle-readonly"><img type="button" src='<c:url value="/css/editPencil.svg"/>' alt="editar"/></label></h3>
                  <form:input class="form-control mb-3 w-75" name="surname" id="surname" value="${user.get().surname}" path="surname" readonly="true"/>
                </div>
              </div>
                <div class="row">
                <div class="col p-0 m-0">
            <!-- TODO Connect image function-->
                  <h3>Telefono<img type="button" href="#" src='<c:url value="/css/editPencil.svg"/>' alt="editar"></h3>
            <!-- TODO Connect telefono-->
                  <c:forEach var="staff" items="${user.get().staffs}">
                      <label for="phone"></label><input class="form-control mb-3 w-75" id="phone" name="phone" value="${staff.office.phone}" readonly/>
                  </c:forEach>
                </div>
                <div class="col p-0 m-0">
            <!-- TODO Connect image function-->
                    <h3>Email <label for="email" class="toggle-readonly"><img type="button" src='<c:url value="/css/editPencil.svg"/>' alt="editar"/></label></h3>
                  <form:input class="form-control mb-3 w-75" id="email" name="email" value="${user.get().email}" path="email" readonly="true"/>
                </div>
              </div>
                <form:errors path="*" cssClass="mt-4 mb-0 text-danger" element="p"/>
                <div class="form-row justify-content-between align-items-end mt-2">
                    <button type="submit" class="btn btn-info">Confirmar cambios</button>
                </div>
              </form:form>
              <div class="row mb-3">
                <h3>Consultorio</h3>
                  <c:forEach var="staff" items="${user.get().staffs}">
                    <div class="container p-0 m-0 pl-3">
                      <div class="row d-flex align-items-center justify-content-between">
                        <p class="m-0">- ${staff.office.name}</p>
                      </div>
                    </div>
                  </c:forEach>
              </div>
              <div class="row">
                <h3>Horarios</h3>
                <div class="container p-0 m-0 pl-3">
                  <!-- TODO Connect example this structure must be repeated-->
                  <div class="row d-flex align-items-center justify-content-between">
                    <c:forEach var="staff" items="${user.get().staffs}">
                      <c:forEach var="workday" items="${staff.workdays}">
                        <p class="m-0">- ${workday.day} de ${workday.startHour}:00hs a ${workday.endHour}:00hs - ${workday.staff.office.name}</p>
                        <!-- TODO Connect remove button-->
                        <a href="" class="btn">X</a>
                      </c:forEach>
                    </c:forEach>
                  </div>
                  <!-- TODO Connect agregar-->
                  <div class="row d-flex align-items-center justify-content-center my-3">
                    <a href="" type="button" class="btn btn-info">Agregar horario</a>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="col-2">
          </div>
        </div>
      </div>
    <script src='<c:url value="/js/scripts/medicProfile.js"/> '></script>
    <script>Profile.init()</script>
  </body>
</html>
