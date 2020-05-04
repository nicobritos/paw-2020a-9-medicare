<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:choose>
    <c:when test="${user.isPresent()}">
        <%@ include file = "navbarLogged.jsp" %>
    </c:when>
    <c:otherwise>
        <%@ include file = "navbarNotLogged.jsp" %>
    </c:otherwise>
</c:choose>


