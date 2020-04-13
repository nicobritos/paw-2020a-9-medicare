<input id="txtSearch" class="form-control" type="search" placeholder="Buscá por nombre...">
<select id="selEspecialidad" class="form-control">
    <option value="" disabled selected>Especialidad</option>
    <c:forEach var="specialty" items="${specialties}">
        <option><c:out value="${specialty.name}"/></option>
    </c:forEach>
</select>