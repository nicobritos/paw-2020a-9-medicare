<template>
  <!-- 
    TODO:CHECK
    lo dejo asi checkear si el resto funciona bien con esto
  -->
  <span id="app">
    <Navbar :user="user" :logged="logged" />
    <router-view />
  </span>
</template>

<script>
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import apiTypes from "@/scripts/apiTypes";
import Navbar from "./components/navbar/navbar.vue";

export default {
  name: "App",
  components: {
    Navbar: Navbar,
  },
  data: function() {
    return {
      user: new apiTypes.User(0,"default@useremail.com","firstName","surname",false,"0000-0000",0),
      logged: false,
    };
  },
};
</script>

<style>
</style>
