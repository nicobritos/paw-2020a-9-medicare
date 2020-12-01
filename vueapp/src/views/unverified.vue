<template>    
    <div class="container flex-grow-1 d-flex flex-column w-100 h-100 justify-content-center align-items-center">
        <div class="px-5 py-3 d-flex flex-column align-items-center justify-content-center styled-container">
            <h1 class="mb-4">{{$t("MissingEmailVerification")}}</h1>
            <h4>{{$t("VerificationEmailSent")}}:</h4>
            <h4>"{{user.email}}"</h4>
            <h4>{{$t("UntilVerifiedCantContinue")}}</h4>
        </div>
        <div v-if="tokenError" class="alert alert-danger medicare-alert-error">
            {{$t("TokenError.loginForm")}}
        </div>
    </div>
</template>

<script>
import apiTypes from "@/logic/apiTypes";

export default {
    name:"Unverified",
    data:function() {
        return {
            user:new apiTypes.User(1,"email","firstName","surname",false,"00.000",1),
            tokenError:false
        }
    }
}
</script>

<style>

html, body {
    height: 100%;
}

.header {
    background-color: #00C4BA;
}

.header-brand {
    font-weight: bold;
}

.header-brand:hover {
    font-weight: bold;
    color: white !important;
}

.header-a-element {
    color: white;
}

.header-a-element:hover {
    color: #e0e0e0;
}

.header-btn-element {
    color: #00C4BA;
    font-weight: bold;
}

.header-btn-element:hover {
    color: rgb(0, 160, 152);
    font-weight: bold;
}

.green-text {
    color: #00C4BA;
}

#navbar-logo {
    width: 2em;
}

.filter-form {
    background-color: #00C4BA;
    border-radius: 1em;
}

.form-title {
    color: white;
}

.form-control {
    background-color: rgba(214, 214, 214);
}

.turno-item {
    border-radius: 2em !important;
    background-color: rgba(214, 214, 214);
}

.turno-list {
    -ms-overflow-style: none;
    scrollbar-width: none;
}

.turno-list::-webkit-scrollbar {
    display: none;
}

.moreOptionsButton {
    height: 1.5em;
    cursor: pointer;
}

.white-text {
    color: white !important;
}

.styled-container {
    background-color: rgba(150, 149, 149, 0.404);
    border-radius: 1em;
}

.medicare-alert-error {
    position: fixed;
    top: 5em;
    right: .5em;
}
</style>