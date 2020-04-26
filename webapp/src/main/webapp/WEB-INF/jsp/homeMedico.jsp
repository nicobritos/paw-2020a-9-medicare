<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
    <%@ include file = "head.jsp" %>
    <link rel="stylesheet" href='<c:url value="/css/homeMedico.css"/> ' />
  </head>
  <body>
    <%@ include file="navbarNotLogged.jsp"%>

    <div class="container h-75 w-100 mt-5">
      <div class="row">
        <div class="col-5">
          <h4>Agenda de hoy</h4>
        </div>
        <div class="col">
          <h4>Agenda semanal</h4>
        </div>
      </div>
      <div class="row h-100">
        <div class="col-4 h-100 pl-0 mr-3 w-100">
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
                  <div class="col-6">
                    <div class="row justify-content-start">
                      <!-- TODO name of pacient -->
                      <h5>Nombre Apellido</h5>
                    </div>
                    <div class="row">
                      <!-- TODO appointment time -->
                      <p class="m-0">Horario</p>
                    </div>
                  </div>
                  <div class="col-2 justify-content-start">
                    <div class="dropdown">
                      <!-- TODO connect icon -->
                      <img
                        src='<c:url value="/css/moreOptions.svg" />'
                        class="moreOptionsButton"
                        alt=""
                        data-toggle="dropdown"
                      />
                      <div class="dropdown-menu">
                        <!-- TODO conect to the options -->
                        <a class="dropdown-item" href="#">Action</a>
                        <a class="dropdown-item" href="#">Another action</a>
                        <a class="dropdown-item" href="#"
                          >Something else here</a
                        >
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
          <div class="row d-flex justify-content-around">
            <!-- TODO connect buttons and days -->
            <!-- TODO the selected day should be font-weight-bold -->
            <button type="button" class="btn"><</button>
            <!-- this is one -->
            <span class="d-flex flex-column align-items-center">
              <!-- day of the week -->
              <p class="mb-0">lunes</p>
              <!-- day/month -->
              <p class="my-0">23 abr</p>
              <!-- Amount of turnos this day -->
              <p>3 turno</p>
            </span>
            <span class="d-flex flex-column">
              <!-- day of the week -->
              <p class="mb-0 font-weight-bold ">martes</p>
              <!-- day/month -->
              <p class="my-0">23 abr</p>
              <!-- Amount of turnos this day -->
              <p>3 turno</p>
            </span>
            <span class="d-flex flex-column">
               <!-- day of the week -->
              <p class="mb-0">miercoles</p>
              <!-- day/month -->
              <p class="my-0">23 abr</p>
              <!-- Amount of turnos this day -->
              <p>3 turno</p>
            </span>
            <span class="d-flex flex-column">
               <!-- day of the week -->
              <p class="mb-0">jueves</p>
              <!-- day/month -->
              <p class="my-0">23 abr</p>
              <!-- Amount of turnos this day -->
              <p>3 turno</p>
            </span>
            <span class="d-flex flex-column">
               <!-- day of the week -->
              <p class="mb-0">viernes</p>
              <!-- day/month -->
              <p class="my-0">23 abr</p>
              <!-- Amount of turnos this day -->
              <p>3 turno</p>
            </span>
            <span class="d-flex flex-column">
               <!-- day of the week -->
              <p class="mb-0">sabado</p>
              <!-- day/month -->
              <p class="my-0">23 abr</p>
              <!-- Amount of turnos this day -->
              <p>3 turno</p>
            </span>
            <span class="d-flex flex-column">
               <!-- day of the week -->
              <p class="mb-0">domingo</p>
              <!-- day/month -->
              <p class="my-0">23 abr</p>
              <!-- Amount of turnos this day -->
              <p>3 turno</p>
            </span>
            <button type="button" class="btn">></button>
          </div>
          <div class="row justify-content-center">
            <!-- TODO connect to turnos -->
            <ul class="list-group turno-list mr-2 w-50 overflow-auto">
              <!-- TODO remove id. was just for testing -->
              <li class="list-group-item turno-item mb-3" id="lit2">
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
                    <div class="col-6">
                      <div class="row justify-content-start">
                        <!-- TODO name of patient -->
                        <h5>Nombre Apellido</h5>
                      </div>
                      <div class="row">
                        <!-- TODO appointment time -->
                        <p class="m-0">Horario</p>
                      </div>
                    </div>
                    <div class="col-2 justify-content-start">
                      <div class="dropdown">
                        <!-- TODO connect icon -->
                        <img
                          src='<c:url value="/css/moreOptions.svg"/> '
                          class="moreOptionsButton"
                          alt=""
                          data-toggle="dropdown"
                        />
                        <div class="dropdown-menu">
                          <!-- TODO conect to the options -->
                          <a class="dropdown-item" href="#">Action</a>
                          <a class="dropdown-item" href="#">Another action</a>
                          <a class="dropdown-item" href="#"
                            >Something else here</a
                          >
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </li>
              <!-- TODO WTF is this. this shouldve been removed. GOD -->
              <script>
                let li2 = document.getElementById("lit2");
                li2.parentNode.appendChild(li2.cloneNode(true));
                li2.parentNode.appendChild(li2.cloneNode(true));
                li2.parentNode.appendChild(li2.cloneNode(true));
                li2.parentNode.appendChild(li2.cloneNode(true));
                li2.parentNode.appendChild(li2.cloneNode(true));
                li2.parentNode.appendChild(li2.cloneNode(true));
                li2.parentNode.appendChild(li2.cloneNode(true));
              </script>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
