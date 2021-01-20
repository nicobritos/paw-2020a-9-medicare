<template>
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
import {APIErrorCallback, APIErrorEventName} from '~/logic/interfaces/APIErrorEvent';
import EventBus from '~/logic/EventBus';
import {APIError} from '~/logic/models/APIError';

@Component({
    components: {
        Navbar
    }
})
export default class App extends Vue {
    get hideNav(): boolean {
        return this.$route.meta.hideNav;
    }

    mounted() {
        EventBus.$on(APIErrorEventName, this.apiErrorCallback as APIErrorCallback);
    }

    beforeDestroy() {
        // Clean up listener
        EventBus.$off(APIErrorEventName, this.apiErrorCallback as APIErrorCallback);
    }

    showErrorToast(code:number){
        this.$bvToast.toast(getErrorMessage(code),{
            title:this.$t("ThereWasAnError").toString(),
            variant:"danger"
        })
    }

    private apiErrorCallback(error: APIError): void {
        this.showErrorToast(error.code);
        for (let e of error.errors) {
            this.showErrorToast(e.code);
        }
    }
}
</script>

<style>
</style>
