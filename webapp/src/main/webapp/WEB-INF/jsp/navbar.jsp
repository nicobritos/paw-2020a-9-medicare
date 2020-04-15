<div id="navbar">
	<input id="txtSearch" class="form-control d-inline" type="search" placeholder="Buscá por nombre...">
	<select id="selEspecialidad" class="form-control d-inline">
	    <option value="-1" disabled selected>Especialidad</option>
		<option value="-1">Cualquiera</option>
		<c:forEach var="specialty" items="${specialties}">
			<option value="<c:out value="${specialty.id}"/>"><c:out value="${specialty.name}"/></option>
		</c:forEach>
	</select>
	<button type="button" class="btn btn-info" id="filtrarBtn">Filtrar</button>
</div>
<script>
	let filtrarBtn = document.getElementById("filtrarBtn");
	let selEspecialidad = document.getElementById("selEspecialidad");
	let txtSearch = document.getElementById("txtSearch");
	filtrarBtn.onclick = ()=>{
		let id = selEspecialidad.options[selEspecialidad.selectedIndex].value;
		let name = txtSearch.value ? "name=" + txtSearch.value : "";
		let specialties = id >= 0 ? "specialties=" + id : "";
		let connector = id>=0 && name ? "&&" : "";
		let querySelector = id>=0 || name ? "?" : "";
		location.href = "<c:url value="/mediclist"/>" + querySelector + name + connector + specialties;
	};
</script>
