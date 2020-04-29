<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en">
  <head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/patientProfile.css"/> ' />
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
                    <c:url value="/patient/profile" var="patientProfileUrl"/>
                    <form:form modelAttribute="patientProfileForm" action="${patientProfileUrl}" method="post">
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
                                <h3>Telefono<img type="button" src='<c:url value="/css/editPencil.svg"/>' alt="editar"></h3>
                                <!-- TODO Connect telefono-->
                                <c:forEach var="patient" items="${user.get().patients}">
                                    <label for="phone"></label><input class="form-control mb-3 w-75" id="phone" name="phone" value="${patient.phone}" readonly/>
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
          <div class="col-2">
          </div>
        </div>
      </div>
      </div>
    </div>
    <script src='<c:url value="/js/scripts/patientProfile.js"/> '></script>
  <script>Profile.init()</script>
  </body>
</html>
