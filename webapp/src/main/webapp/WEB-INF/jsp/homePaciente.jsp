<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/homePaciente.css"/>' />
  </head>
  <body>
    <%@ include file="navbarNotLogged.jsp"%>

    <div class="container h-75 w-100 mt-5">
      <div class="row">
        <h4>Mis turnos</h4>
      </div>
      <div class="row h-100">
        <div class="col h-100 pl-0 mr-5 w-100">
          <!-- TODO connect to turnos -->
          <ul class="list-group turno-list mr-2 w-100 h-100 overflow-auto">
              <!-- TODO remove id. was just for testing -->
            <li class="list-group-item turno-item mb-3" id="lit">
              <!-- TODO this is one element -->
              <div class="container">
                <div class="row">
                  <div class="col-4">
                    <img
                      class="w-100"
                      src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg"
                      alt=""
                    />
                  </div>
                  <div class="col-7">
                    <div class="row justify-content-start">
                        <!-- TODO name of doctor -->
                      <h5>Dr Nombre Apellido</h5>
                    </div>
                    <div class="row">
                        <!-- TODO doctor specialty -->
                      <p class="m-0">Especialidad</p>
                    </div>
                    <div class="row">
                        <!-- TODO doctor direction -->
                      <p class="m-0">Direccion</p>
                    </div>
                    <div class="row">
                        <!-- TODO appointment time -->
                      <p class="m-0">Horario</p>
                    </div>
                  </div>
                  <div class="col-1 justify-content-start">
                      <div class="dropdown">
                          <!-- TODO connect icon -->
                          <img src='<c:url value="/css/moreOptions.svg"/>' class="moreOptionsButton" alt="nore options" data-toggle="dropdown">
                          <div class="dropdown-menu">
                            <!-- TODO conect to the options -->
                          <a class="dropdown-item" href="#">Action</a>
                          <a class="dropdown-item" href="#">Another action</a>
                          <a class="dropdown-item" href="#">Something else here</a>
                        </div>
                      </div>
                  </div>
                </div>
              </div>
            </li>
            <!-- TODO WTF is this. this shouldve been removed. GOD -->
            <script>
              let li = document.getElementById("lit");
              li.parentNode.appendChild(li.cloneNode(true));
              li.parentNode.appendChild(li.cloneNode(true));
              li.parentNode.appendChild(li.cloneNode(true));
              li.parentNode.appendChild(li.cloneNode(true));
              li.parentNode.appendChild(li.cloneNode(true));
              li.parentNode.appendChild(li.cloneNode(true));
              li.parentNode.appendChild(li.cloneNode(true));
            </script>
          </ul>
        </div>
        <div class="col">
          <!-- TODO connect form -->
          <form class="container p-5 filter-form">
            <div class="row justify-content-start">
              <h3 class="form-title">Buscar medico</h3>
            </div>
            <div class="row justify-content-start my-3">
              <!-- TODO first input nombre -->
              <input class="form-control w-100" type="text" name="nombre" id="nombre" placeholder="nombre"/>
            </div>
            <div class="row justify-content-start my-3">
              <!-- TODO second input especialidad -->
              <select class="form-control" name="especialidad" id="especialidad">
                <option value="-1" disabled selected>Especialidad</option>
                <!-- TODO add especialty options -->
              </select>
            </div>
            <div class="row justify-content-start my-3">
              <!-- TODO third input localidad -->
              <select class="form-control" name="localidad" id="localidad">
                <option value="-1" disabled selected>Localidad</option>
                <!-- TODO add locality options -->
              </select>
            </div>
            <div class="row justify-content-start my-3">
                <!-- TODO connect button -->
              <button class="w-100 btn rounded-pill btn-light header-btn-element">
                Buscar medicos
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>
