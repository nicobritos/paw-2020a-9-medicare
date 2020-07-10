<template>
    <div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
        <form class="register-form border p-5 rounded">
            <div class="row">
                <h6>Medicare <img :src='logo' id="logo"/></h6>
            </div>
            <div class="row justify-content-start">
                <h1 class="register-form-title">{{$t("Login")}}</h1>
            </div>
            <div class="form-group row">
                <div class="col">
                    <label for="medicare_email">{{$t("Email")}}</label>
                </div>
                <div class="col-8">
                    <input class="form-control" type="email" name="medicare_email" id="medicare_email"/>
                </div>
            </div>
            <div class="form-group row">
                <div class="col">
                    <label for="medicare_password">{{$t("Password")}}</label>
                </div>
                <div class="col-8">
                    <input class="form-control pr-5" :type='showPassword?"text":"password"' name="medicare_password" id="medicare_password"/>
                    <!-- For this to work for must be the id of the password input -->
                    <label for="medicare_password" class="toggle-visibility" @click="toggleShowPassword()">
                        <img v-if="!showPassword" :src='eye'>
                        <img v-else :src='noeye'>
                    </label>
                </div>
            </div>
            <div class="form-group row align-items-center">
                <div class="col">
                    <label for="medicare_remember_me" class="mb-0">{{$t("RememberMe")}}</label>
                </div>
                <div class="col-8">
                    <checkbox id="medicare_remember_me" name="medicare_remember_me"/>
                </div>
            </div>
            <div class="form-row justify-content-between align-items-end mt-2">
                <a class="form-link" :href="signupUrl">{{$t("CreateAccount")}}</a>
                <button type="submit" class="btn btn-primary">{{$t("Confirm")}}</button>
            </div>
            <p v-if="invalidCredentials" class="mt-4 mb-0 text-danger">
                {{$t("InvalidCredentials.loginForm")}}
            </p>
        </form>
    </div>
</template>

<script>
import logo from "@/assets/logo.svg";
//TODO:change to bootstrap icons
import eye from "@/assets/eye.svg";
import noeye from "@/assets/noeye.svg";

export default {
    name:"Login",
    data:function() {
        return{
            logo:logo,
            eye:eye,
            noeye:noeye,
            showPassword:false
        }
    },
    methods:{
        toggleShowPassword(){
            this.showPassword = !this.showPassword;
        }
    }
}
</script>

<style>
body, html {
    height: 100%;
    background-color: rgba(0, 196, 186, 0.205);
}

.register-form {
    background-color: #fff;
    border-radius: 1em !important;
    box-shadow: 10px 9px 12px 0px rgba(0, 196, 186, 0.205);
    max-width: 430px;
    width: 430px;
}


.form-link:hover {
    text-decoration: none;
}

.register-form input {
    background-color: #f0f0f0;
}

.register-form input:focus {
    background-color: #e0e0e0;
}

.register-form-title {
    margin-bottom: 1em;
}

.register-form #logo {
    width: 1em;
}

.register-form button {
    background-color: #00C4BA;
    color: white;
}

.register-form button:hover {
    background-color: rgb(1, 150, 142);
    color: #fafafa;
}

.form-password {
    position: relative;
}

.toggle-visibility {
    position: absolute;
    right: 2em;
    top: 0.6em;
    bottom: auto;
    left: auto;
    z-index: 1;

    cursor: pointer;
}

.profile-picture-container {
    display: inline-block;
    position: relative;
    width: 100%;
}

.profile-picture {
    object-fit: cover;
    height: 100%;
    width: 100%;
    top: 0;
    bottom: 0;
    right: 0;
    left: 0;
    position: absolute;
}

</style>