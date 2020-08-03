<template>
  <!--TODO:check -->
  <nav class="navbar navbar-expand header">       
    <NavbarLogo/>
    <NavbarLogged v-if="value" :user="value" @logout="logout"></NavbarLogged>
    <NavbarNotLogged v-else/>
  </nav>
</template>

<script>
import NavbarNotLogged from "./navbarNotLogged";
import NavbarLogged from "./navbarLogged";
import NavbarLogo from "./navbarLogo";

export default {
  name: "Navbar",
  props:["value"],
  components: {
    NavbarLogged:NavbarLogged, 
    NavbarLogo:NavbarLogo,
    NavbarNotLogged:NavbarNotLogged
  },
  methods:{
    logout(){
      this.$emit("input",null);
    }
  }
};
</script>

<style>
/* TODO:Check probably should use scoped with deep selector */
html,
body {
  height: 100%;
}

.header {
  background-color: #00c4ba;
}

.header-brand {
  font-weight: bold;
}

.header-brand:hover {
  font-weight: bold;
  color: white !important;
}

.header-a-element {
  color: white;
}

.header-a-element:hover {
  color: #e0e0e0;
}

.header-btn-element {
  color: #00c4ba;
  font-weight: bold;
}

.header-btn-element:hover {
  color: rgb(0, 160, 152);
  font-weight: bold;
}

.green-text {
  color: #00c4ba !important;
}

#navbar-logo {
  width: 2em;
}

.white-text {
  color: white !important;
}

#navbarUserImage {
  width: 3em;
}

.grey-background {
  background-color: rgba(214, 214, 214);
}

.fill-height {
  flex: 1 1 auto;
}

.profile-picture-container {
  display: inline-block;
  position: relative;
  width: 100%;
}

.profile-picture {
  object-fit: cover;
  height: 100%;
  width: 100%;
  top: 0;
  bottom: 0;
  right: 0;
  left: 0;
  position: absolute;
}
</style>
