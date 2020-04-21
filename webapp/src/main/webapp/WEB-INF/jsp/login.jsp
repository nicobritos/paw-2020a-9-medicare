<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
  <head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/login.css"/> '>
  </head>
  <body>
  <div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
    <%--TODO: connect--%>
      <form class="register-form border p-5 rounded">
        <div class="row">
          <h6>Medicare <img src='<c:url value="/css/logo.svg"/>' id="logo"/></h6>
        </div>
        <div class="row justify-content-start">
          <h1 class="register-form-title">Iniciar sesion</h1>
        </div>
        <div class="form-group row">
          <div class="col">
            <label for="email">email</label>
          </div>
          <div class="col-8">
            <%--TODO: connect--%>
            <input class="form-control" type="email" name="email" id="email">
          </div>
        </div>
        <div class="form-group row">
          <div class="col">
            <label for="password">contraseña</label>
          </div>
          <div class="col-8">
            <%--TODO: connect--%>
              <input class="form-control" type="password" name="contraseña" id="password">
              <label for="password" id="toggle-visibility"><img src='<c:url value="/css/eye.svg"/>'><img src='<c:url value="/css/noeye.svg"/>' style="display: none;"></label>
          </div>
        </div>
        <div class="form-row justify-content-between align-items-center">
          <%--TODO: connect--%>
          <a class="form-link" href="">Crear cuenta</a><button class="form-comfirm-button btn">Confirmar</button>
        </div>
      </form>
    </div>
  <script src='<c:url value="/js/login.js"/> '></script>
  </body>
</html>
