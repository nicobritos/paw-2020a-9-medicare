<div id="navbar">
	<input id="txtSearch" class="form-control d-inline" type="search" placeholder="Buscá por nombre...">
	<select id="selEspecialidad" class="form-control d-inline">
	    <option value="" disabled selected>Especialidad</option>
	    <c:forEach var="specialty" items="${specialties}">
	        <option><c:out value="${specialty.name}"/></option>
	    </c:forEach>
	</select>
	<button type="button" class="btn btn-secondary">Filtrar</button>
</div>
