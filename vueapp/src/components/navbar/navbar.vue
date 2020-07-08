//
<c:choose>
//     <c:when test="${user.isPresent()}">
//         <%@ include file="navbarLogged.jsp" %>
//     </c:when>
//     <c:otherwise>
//         <%@ include file="navbarNotLogged.jsp" %>
//     </c:otherwise>
// </c:choose>
<template>
  <!--TODO:check -->
  <div>
    <NavbarLogged v-if="user.isPreset()"></NavbarLogged>
    <NavbarNotLogged v-else></NavbarNotLogged>
  </div>
</template>

<script>
import { NavbarLogged } from "./navbarLogged";
import { NavbarNotLogged } from "./navbarNotLogged";

export default {
  name: "Navbar",
  components: [NavbarLogged, NavbarNotLogged],
};
</script>
