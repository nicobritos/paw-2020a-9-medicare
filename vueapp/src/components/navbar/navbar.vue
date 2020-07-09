<template>
  <!--TODO:check -->
  <nav class="navbar navbar-expand header">       
    <NavbarLogo/>
    <NavbarLogged v-if="logged" :user="user"></NavbarLogged>
    <NavbarNotLogged v-else/>
  </nav>
</template>

<script>
import NavbarNotLogged from "./navbarNotLogged";
import NavbarLogged from "./navbarLogged";
import NavbarLogo from "./navbarLogo";
import apiTypes from "@/scripts/apiTypes";

export default {
  name: "Navbar",
  props:{
    user:apiTypes.User,
    logged:Boolean
  },
  components: {
    NavbarLogged:NavbarLogged, 
    NavbarLogo:NavbarLogo,
    NavbarNotLogged:NavbarNotLogged
  }
};
</script>
