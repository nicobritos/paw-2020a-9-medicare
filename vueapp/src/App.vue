<template>
  <!-- 
    TODO:CHECK
    lo dejo asi checkear si el resto funciona bien con esto
  -->
  <span id="app">
    <Navbar v-if="!isAuth"/>
    <router-view/>
  </span>
</template>

<script lang="ts">
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import Navbar from "./components/navbar/navbar.vue";
import {Component, Vue} from 'vue-property-decorator';

@Component({
    components: {
        Navbar
    }
})
export default class App extends Vue {
    get isAuth(): boolean {
        return ["Login","Signup","SignupDoctor","SignupPatient"].includes(this.$route.name!);
    }
}
</script>

<style>
</style>
