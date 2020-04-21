<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
  <head>
    <%@ include file = "head.jsp" %>
    <!-- TODO: connect -->
    <link rel="stylesheet" href='<c:url value="/css/register.css"/> '>
  </head>
  <body>
    <div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
      <!-- TODO:connect -->
      <form class="register-form border p-5 rounded">
        
        
        <div class="row">
          <h6>Medicare <img src='<c:url value="/css/logo.svg"/>' id="logo"/></h6>
        </div>
        <div class="row justify-content-start">
          <h1 class="register-form-title">Crear cuenta</h1>
        </div>



        <!-- TODO:connect -->
        <div class="form-group row">
          <div class="col">
            <label for="name">Nombre</label>
          </div>
          <div class="col-8">
            <input class="form-control" type="text" name="name" id="name">
          </div>
        </div>
        
        
        
        <!-- TODO:connect -->
        <div class="form-group row">
          <div class="col">
            <label for="surname">Apellido</label>
          </div>
          <div class="col-8">
            <input class="form-control" type="text" name="surname" id="surname">
          </div>
        </div>
        
        
        
        <!-- TODO:connect -->
        <div class="form-group row">
          <div class="col">
            <label for="email">Email</label>
          </div>
          <div class="col-8">
            <input class="form-control" type="email" name="email" id="email">
          </div>
        </div>
        
        
        
        <!-- TODO:connect -->
        <div class="form-group row">
          <div class="col">
            <label for="password">Contraseña</label>
          </div>
          <div class="col-8">
            <input class="form-control" type="password" name="password" id="password">
            <!-- For this to work for must be the id of the password input -->
            <label for="password" class="toggle-visibility"><img src='<c:url value="/css/eye.svg"/> '><img src='<c:url value="/css/noeye.svg"/> ' style="display: none;"></label>
          </div>
        </div>
        
        
        
        
        <!-- TODO:connect -->
        <div class="form-group row">
          <div class="col">
            <label for="repeatPassword">Repetir contraseña</label>
          </div>
          <div class="col-8">
            <input class="form-control" type="password" name="repeatPassword" id="repeatPassword">
            <!-- For this to work for must be the id of the password input -->
            <label for="repeatPassword" class="toggle-visibility"><img src='<c:url value="/css/eye.svg"/> '><img src='<c:url value="/css/noeye.svg"/> ' style="display: none;"></label>
          </div>
        </div>
        
        
        
        <!-- TODO:connect -->
        <div class="form-row justify-content-between">
          <button class="form-atras-btn btn" type="reset">Atras</button><button class="btn btn-primary">Confirmar</button>
        </div>
      </form>
    </div>
    <script src='<c:url value="/js/register.js"/> '></script>
  </body>
</html>
