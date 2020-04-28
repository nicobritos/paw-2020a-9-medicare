<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%@ include file = "head.jsp" %>

    <link rel="stylesheet" href='<c:url value="/css/loggedMedicList.css"/> ' />
  </head>
  <body>
    <%@ include file="navbar.jsp" %>

    <form action="<c:url value="/mediclist/1"/>">
      <div class="container h-75">
        <div class="row mt-4 justify-content-center">
          <%--TODO connect name search--%>
          <input class="form-control w-100" type="text" name="name" placeholder="Nombre"/>
        </div>
        <div class="row mt-4">
          <div class="col-4 px-3">
  <%--          <div class="row">--%>
  <%--            &lt;%&ndash;TODO connect sort search&ndash;%&gt;--%>
  <%--            <select class="form-control w-100" type="text" name="sort" id="sort">--%>
  <%--              <option value="-1" disabled selected>Ordenar por</option>--%>
  <%--            </select>--%>
  <%--          </div>--%>
            <div class="row mt-4">
              <select class="form-control w-100" type="text" name="specialties" id="selEspecialidad">
                <option value="-1" disabled selected>Especialidad</option>
                <option value="-1">Cualquiera</option>
                <c:forEach var="specialty" items="${specialties}">
                  <option value="<c:out value="${specialty.id}"/>"><c:out value="${specialty.name}"/></option>
                </c:forEach>
              </select>
            </div>
            <div class="row mt-4">
              <%--TODO connect localidad search--%>
              <select class="form-control w-100" type="text" name="localities" id="localidad">
                <option value="-1" disabled selected>Localidad</option>
                <option value="-1">Cualquiera</option>
                <c:forEach var="locality" items="${localities}">
                  <option value="<c:out value="${locality.id}"/>"><c:out value="${locality.name}"/></option>
                </c:forEach>
              </select>
            </div>
            <div class="row mt-4">
              <button type="submit" class="btn btn-info w-100 rounded-pill">Filtrar</button>
            </div>
          </div>
          <div class="col-1"></div>
          <div class="col">
            <ul class="list-group turno-list mr-2 w-100">
              <c:forEach var="member" items="${staff}">
                <li class="list-group-item turno-item mb-3">
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
                          <h5>${member.firstName} ${member.surname}</h5>
                        </div>
                        <div class="row">
                          <p class="m-0">
                            <c:forEach var="specialty" items="${member.staffSpecialties}">
                              <c:out value="${specialty.name}"/>
                            </c:forEach>
                          </p>
                        </div>
                        <div class="row">
                          <p class="m-0">${member.office.street}</p>
                        </div>
                      </div>
                      <div class="col d-flex justify-content-center align-items-center">
                        <!-- TODO link to request appointment url -->
                        <button onclick="<c:url value="/"/>" class="btn btn-info">Ver turno</button>
                      </div>
                    </div>
                  </div>
                </li>
              </c:forEach>
            </ul>
            <div id="paging" class="p-3 container w-100 justify-content-center">
              <c:if test="${page > 1}">
                <button type="button" class="btn btn-info btn-sm" id="prevButton"><</button>
              </c:if>

              <p class="d-inline mx-2">Página <c:out value="${page}"/></p>
              <c:if test="${staff.size() == 10}">
                <button type="button" class="btn btn-info btn-sm" id="nextButton">></button>
              </c:if>
            </div>
          </div>
        </div>
      </div>
    </form>
    <script>
      let prevButton = document.getElementById("prevButton");
      let nextButton = document.getElementById("nextButton");
      if(prevButton!=null){
        prevButton.onclick= ()=>{
          location.href="<c:url value="/mediclist/${page-1}"/>" + location.search;
        }
      }
      if(nextButton!=null){
        nextButton.onclick= ()=>{
          location.href="<c:url value="/mediclist/${page+1}"/>" + location.search;
        }
      }
    </script>
  </body>
</html>
