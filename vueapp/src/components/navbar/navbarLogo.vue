<template>
    <div class="container w-100 justify-content-start">
        <img class="navbar-brand" :src='logo' alt="logo" id="navbar-logo">
        <router-link :to='getUrl()' class="navbar-brand header-brand header-a-element" >MediCare</router-link>
    </div>
</template>

<script>
import logo from "@/assets/whiteLogo.svg";
import utils from "@/logic/utils";

export default {
    name:"NavbarLogo",
    data:function() {
        return {
            logo:logo
        }
    },
    methods:{
        getUrl:utils.getUrl
    }
}
</script>