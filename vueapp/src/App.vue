<template>
  <!-- 
    TODO:CHECK
    lo dejo asi checkear si el resto funciona bien con esto
  -->
  <span id="app">
    <Navbar v-if="!isAuth" v-model="user" />
    <router-view v-model="user" />
  </span>
</template>

<script lang="ts">
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import Navbar from "./components/navbar/navbar.vue";
import {Component, Vue} from 'vue-property-decorator';
import {User} from '~/logic/models/User';

@Component({
    components: {
        Navbar
    }
})
export default class App extends Vue {
    private readonly user: User = new User();

    get isAuth(): boolean {
        return ["Login","Signup","SignupStaff","SignupPatient"].includes(this.$route.name!);
    }
}
</script>

<style>
</style>
