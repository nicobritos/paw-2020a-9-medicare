<template>
  <!-- 
    TODO:CHECK
    lo dejo asi checkear si el resto funciona bien con esto
  -->
  <span id="app">
    <Navbar v-if="!hideNav"/>
    <RouterView/>
  </span>
</template>

<script lang="ts">
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import Navbar from "./components/navbar/navbar.vue";
import {Component, Vue} from 'vue-property-decorator';
import {userActionTypes} from '~/store/types/user.types';

import { getErrorMessage } from "@/logic/Utils";

@Component({
    components: {
        Navbar
    }
})
export default class App extends Vue {
    get hideNav(): boolean {
        return this.$route.meta.hideNav;
    }

    created(): void {
        this.$store.dispatch('users/me', userActionTypes.me());
        /**
         * TODO: NICO
         * N  N I   CCCC  OOOOOO
         * NN N I CC      O    O
         * N NN I CC      O    O
         * N  N I   CCCC  OOOOOO
         */
    }

    showErrorToast(code:number){
      this.$bvToast.toast(getErrorMessage(code),{
        title:this.$t("ThereWasAnError").toString(),
        variant:"danger"
      })
    }
}
</script>

<style>
</style>
