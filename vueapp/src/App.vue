<template>
  <div id="app">
    <navbar></navbar>
    <router-view/>
  </div>
</template>

<script>
import { navbar } from "./components/navbar/navbar.vue";


export default {
  name:"App",
  components:[
    navbar
  ]
}
</script>

<style>
</style>
