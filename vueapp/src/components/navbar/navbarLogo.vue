<template>
    <div class="container w-100 justify-content-start">
        <img class="navbar-brand" :src='logo' alt="logo" id="navbar-logo">
        <router-link :to='getUrl()' class="navbar-brand header-brand header-a-element">MediCare</router-link>
    </div>
</template>

<script lang="ts">
import logo from '@/assets/whiteLogo.svg';
import {Component, Vue} from 'vue-property-decorator';

@Component
export default class NavbarLogo extends Vue {
    private readonly logo = logo;
}
</script>