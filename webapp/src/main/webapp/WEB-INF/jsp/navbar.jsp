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
	<select id="selLocality" class="form-control d-inline">
		<option value="-1" disabled selected>Localidad</option>
		<option value="-1">Cualquiera</option>
		<c:forEach var="locality" items="${localities}">
			<option value="<c:out value="${locality.id}"/>"><c:out value="${locality.name}"/></option>
		</c:forEach>
	</select>
	<button type="button" class="btn btn-info" id="filtrarBtn">Filtrar</button>
</div>
<script>
	let filtrarBtn = document.getElementById("filtrarBtn");
	let selEspecialidad = document.getElementById("selEspecialidad");
	let txtSearchName = document.getElementById("txtSearchName");
	let txtSearchSurname = document.getElementById("txtSearchSurname");
	let selLocality = document.getElementById("selLocality");
	filtrarBtn.onclick = ()=>{
		let specialtyId = selEspecialidad.options[selEspecialidad.selectedIndex].value;
		let localityId = selLocality.options[selLocality.selectedIndex].value;
		let name = txtSearchName.value ? "name=" + txtSearchName.value : "";
		let surname = txtSearchSurname.value ? "surname=" + txtSearchSurname.value : "";
		let specialties = specialtyId >= 0 ? "specialties=" + specialtyId : "";
		let localities = localityId >= 0 ? "localities=" + localityId : "";
		let nameSurnameConnector = name && surname ? "&" : "";
		let specialtiesConnector = specialtyId>=0 && (name || surname) ? "&" : "";
		let localitiesConnector = localityId>=0 && (name || surname || specialtyId>=0) ? "&" : "";
		let querySelector = (name || surname || specialtyId>=0 || localityId>=0) ? "?" : "";
		location.href = "<c:url value="/mediclist"/>" + querySelector + name + nameSurnameConnector + surname + specialtiesConnector + specialties + localitiesConnector + localities;
	};
</script>
