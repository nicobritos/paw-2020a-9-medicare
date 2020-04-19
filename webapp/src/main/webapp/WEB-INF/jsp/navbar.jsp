<div id="navbar">
	<input id="txtSearchName" class="form-control d-inline" type="search" placeholder="Buscá por nombre...">
	<input id="txtSearchSurname" class="form-control d-inline" type="search" placeholder="Buscá por apellido...">
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
	let txtSearchName = document.getElementById("txtSearchName");
	let txtSearchSurname = document.getElementById("txtSearchSurname");
	filtrarBtn.onclick = ()=>{
		let id = selEspecialidad.options[selEspecialidad.selectedIndex].value;
		let name = txtSearchName.value ? "name=" + txtSearchName.value : "";
		let surname = txtSearchSurname.value ? "surname=" + txtSearchSurname.value : "";
		let specialties = id >= 0 ? "specialties=" + id : "";
		let nameSurnameConnector = name && surname ? "&" : "";
		let specialtiesConnector = id>=0 && (name || surname) ? "&" : "";
		let querySelector = (id>=0 || name || surname) ? "?" : "";
		location.href = "<c:url value="/mediclist"/>" + querySelector + name + nameSurnameConnector + surname + specialtiesConnector + specialties;
	};
</script>
