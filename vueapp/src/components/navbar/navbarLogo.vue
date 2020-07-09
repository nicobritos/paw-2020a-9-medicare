<template>
    <div class="container w-100 justify-content-start">
        <img class="navbar-brand" :src='logo' alt="logo" id="navbar-logo">
        <a class="navbar-brand header-brand header-a-element" :href='getUrl()'>MediCare</a>
    </div>
</template>

<script>
import logo from "@/assets/whiteLogo.svg";
import utils from "@/js/medicareUtils";

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