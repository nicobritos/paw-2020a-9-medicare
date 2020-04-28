<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file = "head.jsp" %>

    <link rel="stylesheet" href='<c:url value="/css/loggedMedicList.css"/> ' />
  </head>
  <body>
    <%--TODO use navbarLogged--%>
    <%@ include file="navbarNotLogged.jsp" %>

    <div class="container h-75">
      <div class="row mt-4 justify-content-center">
        <%--TODO connect name search--%>
        <input class="form-control w-100" type="text" name="nombre" placeholder="Nombre"/>
      </div>
      <div class="row mt-4">
        <div class="col-4 px-3">
          <div class="row">
            <%--TODO connect sort search--%>
            <select class="form-control w-100" type="text" name="sort" id="sort">
              <option value="-1" disabled selected>Ordenar por</option>
            </select>
          </div>
          <div class="row mt-4">
            <%--TODO connect especialidad search--%>
            <select class="form-control w-100" type="text" name="Especialidad" id="Especialidad">
              <option value="-1" disabled selected>Especialidad</option>
            </select>
          </div>
          <div class="row mt-4">
            <%--TODO connect localidad search--%>
            <select class="form-control w-100" type="text" name="localidad" id="localidad">
              <option value="-1" disabled selected>Localidad</option>
            </select>
          </div>
          <div class="row mt-4">
            <%--TODO connect button search--%>
            <button class="btn btn-info w-100 rounded-pill">Filtrar</button>
          </div>
        </div>
        <div class="col-1"></div>
        <div class="col">
          <!-- TODO connect to turnos -->
          <ul class="list-group turno-list mr-2 w-100">
            <!-- TODO remove id. was just for testing -->
            <li class="list-group-item turno-item mb-3" id="lit">
              <!-- TODO this is one element -->
              <div class="container">
                <div class="row">
                  <div class="col-3 d-flex align-items-center">
                    <%--TODO connect image--%>
                    <img
                      class="w-75"
                      src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg"
                      alt=""
                    />
                  </div>
                  <div class="col-6">
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
                  <div class="col d-flex justify-content-center align-items-center">
                    <%--TODO connect button--%>
                    <button class="btn btn-info">ver turno</button>
                  </div>
                </div>
              </div>
            </li>
            <!-- TODO WTF is this. this shouldve been removed. GOD -->
            <script defer>
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
      </div>
    </div>>
  </body>
</html>
