<template>    
    <div class="container w-100 justify-content-end">
        <a :href='getUrl("signup/staff")' class="header-a-element nav-link mx-3">{{$t('AreYouMedic')}}</a>
        <a :href='getUrl("signup")' class="header-a-element nav-link mx-3">{{$t("SignUp")}}</a>
        <a :href='getUrl("login")' class="header-btn-element btn btn-light rounded mx-3">{{$t("Login")}}</a>
    </div>
</template>
<script>
import utils from "@/scripts/utils";


export default {
    name:"NavbarNotLogged",
    methods:{
        getUrl:utils.getUrl
    }
}
</script>