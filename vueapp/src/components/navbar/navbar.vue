<template>
  <!--TODO:check -->
  <nav class="navbar navbar-expand header">       
    <NavbarLogo/>
    <!-- <NavbarLogged v-if="user.isPreset()"></NavbarLogged> -->
    <NavbarNotLogged/>
  </nav>
</template>

<script>
import NavbarNotLogged from "./navbarNotLogged";
// import NavbarLogged from "./navbarLogged";
import NavbarLogo from "./navbarLogo";

export default {
  name: "Navbar",
  components: {
    // NavbarLogged, 
    NavbarLogo:NavbarLogo,
    NavbarNotLogged:NavbarNotLogged
  },
};
</script>
