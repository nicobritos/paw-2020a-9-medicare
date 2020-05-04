<script type="text/template" id="appointment-select-modal">
    <div class="d-flex flex-row justify-content-between align-content-center">
<%--        <button class="btn" id="day-left"><</button>--%>
        <div class="d-flex flex-row w-100 justify-content-center">
            <div class="d-flex flex-row w-75 justify-content-center" id="week-container">
            </div>
        </div>
<%--        <button id="day-right" class="btn">></button>--%>
<%--        <c:forEach var="i" begin="0" end="4">--%>
<%--            <div class="col-1 mr-4 p-0">--%>
<%--                <a href="<c:url value="/patient/appointment/${staff.id}/${monday.plusDays(i).year}/${monday.plusDays(i).dayOfYear}"/>">--%>
<%--                <span class="d-flex flex-column align-items-center">--%>
<%--                  <!-- day of the week -->--%>
<%--                  <p class="mb-0">${monday.plusDays(i).dayOfWeek.name()}</p>--%>
<%--                    <!-- day/month -->--%>
<%--                  <p class="my-0">${monday.plusDays(i).dayOfMonth} de ${monday.plusDays(i).month.name()}</p>--%>
<%--                    <!-- TODO:connect boton para seleccionar dia-->--%>
<%--                </span>--%>
<%--                </a>--%>
<%--                <div class="d-flex flex-vertical align-content-center">--%>
<%--                        &lt;%&ndash; TODO:connect workdays variables por dia &ndash;%&gt;--%>
<%--                    <c:forEach var="workday" items="${staff.workdays}">--%>
<%--                        <button class="btn btn-sm btn-secondary mb-2">${workday.startHour}:${workday.startMinute}hs</button>--%>
<%--                    </c:forEach>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </c:forEach>--%>
        <%-- TODO:connect boton adelante --%>
    </div>
</script>

<script type="text/template" id="appointment-select-modal-day">
    <div class="d-flex flex-column w-100 flex-shrink-1" id="day-container">
        <h3 class="text-center mb-2">{0}</h3>
        <div class="d-flex flex-column" id="button-container" style="overflow-y: auto; max-height: 40vh;">
        </div>
    </div>
</script>

