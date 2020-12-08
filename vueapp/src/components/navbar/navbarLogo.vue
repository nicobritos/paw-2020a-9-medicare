<template>
    <div class="container w-100 justify-content-start">
        <img class="navbar-brand" :src='logo' alt="logo" id="navbar-logo">
        <!-- TODO: se elimino getUrl -->
        <RouterLink :to='getUrl()' class="navbar-brand header-brand header-a-element">MediCare</RouterLink>
    </div>
</template>

<script lang="ts">
import logo from '@/assets/whiteLogo.svg';
import {Component, Vue} from 'vue-property-decorator';

import {createPath} from "~/logic/Utils";

@Component
export default class NavbarLogo extends Vue {
    private readonly logo = logo;

    getUrl(url:string):string{
        return createPath(url);
    }
}
</script>