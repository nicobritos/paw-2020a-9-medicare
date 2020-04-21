<div id="navbar">
    <div class="container d-flex flex-wrap justify-content-center w-100">
		<input id="txtSearchName" class="form-control" type="search" placeholder="Buscá por nombre...">
		<select id="selEspecialidad" class="form-control">
			<option value="-1" disabled selected>Especialidad</option>
			<option value="-1">Cualquiera</option>
			<c:forEach var="specialty" items="${specialties}">
				<option value="<c:out value="${specialty.id}"/>"><c:out value="${specialty.name}"/></option>
			</c:forEach>
		</select>
		<select id="selLocality" class="form-control">
			<option value="-1" disabled selected>Localidad</option>
			<option value="-1">Cualquiera</option>
			<c:forEach var="locality" items="${localities}">
				<option value="<c:out value="${locality.id}"/>"><c:out value="${locality.name}"/></option>
			</c:forEach>
		</select>
		<button type="button" class="btn btn-info" id="filtrarBtn">Filtrar</button>
    </div>
</div>
<script>
	let filtrarBtn = document.getElementById("filtrarBtn");
	let selEspecialidad = document.getElementById("selEspecialidad");
	let txtSearchName = document.getElementById("txtSearchName");
	let selLocality = document.getElementById("selLocality");
	filtrarBtn.onclick = ()=>{
		let specialtyId = selEspecialidad.options[selEspecialidad.selectedIndex].value;
		let localityId = selLocality.options[selLocality.selectedIndex].value;
		let name = txtSearchName.value ? "name=" + txtSearchName.value : "";
		let specialties = specialtyId >= 0 ? "specialties=" + specialtyId : "";
		let localities = localityId >= 0 ? "localities=" + localityId : "";
		let specialtiesConnector = specialtyId>=0 && name ? "&" : "";
		let localitiesConnector = localityId>=0 && (name || specialtyId>=0) ? "&" : "";
		let querySelector = (name  || specialtyId>=0 || localityId>=0) ? "?" : "";
		location.href = "<c:url value="/mediclist"/>" + querySelector + name + specialtiesConnector + specialties + localitiesConnector + localities;
	};
</script>
