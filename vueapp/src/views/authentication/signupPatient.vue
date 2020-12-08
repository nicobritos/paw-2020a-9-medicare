<template>
    <div
        class="container-fluid w-100 h-100 d-flex flex-column justify-content-center align-items-center signup-container">
        <form class="register-form border p-5 rounded">
            <div class="row">
                <h6>Medicare <img :src='logo' id="logo" alt="logo"/></h6>
            </div>
            <div class="row justify-content-start">
                <h1 class="register-form-title">{{ $t('CreateAccount') }}</h1>
            </div>
            <div class="form-group row">
                <div class="col">
                    <label for="first_name">{{ $t('Name') }}</label>
                </div>
                <div class="col-8">
                    <input class="form-control" type="text" name="firstName" id="first_name"/>
                </div>
            </div>
            <div class="form-group row">
                <div class="col">
                    <label for="surname">{{ $t('Surname') }}</label>
                </div>
                <div class="col-8">
                    <input class="form-control" type="text" name="surname" id="surname"/>
                </div>
            </div>
            <div class="form-group row">
                <div class="col">
                    <label for="medicare_email">{{ $t('Email') }}</label>
                </div>
                <div class="col-8">
                    <input class="form-control" type="email" name="medicare_email" id="medicare_email"/>
                </div>
            </div>
            <div class="form-group row">
                <div class="col">
                    <label for="medicare_password">{{ $t('Password') }}</label>
                </div>
                <div class="col-8">
                    <input class="form-control pr-5"
                           :type='showPassword?"text":"password"' name="medicare_password"
                           id="medicare_password"/>
                    <label for="medicare_password" class="toggle-visibility" @click="toggleShowPassword()">
                        <!-- TODO: check icons -->
                        <img v-if="!showPassword" :src='eye'>
                        <img v-else :src='noeye'>
                    </label>
                </div>
            </div>
            <div class="form-group row">
                <div class="col">
                    <label for="medicare_repeatPassword">{{ $t('RepeatPassword') }}</label>
                </div>
                <div class="col-8">
                    <input class="form-control pr-5"
                           :type='showRepeatPassword?"text":"password"' name="medicare_repeatPassword"
                           id="medicare_repeatPassword"/>
                    <label for="medicare_repeatPassword" class="toggle-visibility" @click="toggleShowRepeatPassword()">
                        <!-- TODO: check icons -->
                        <img v-if="!showRepeatPassword" :src='eye'>
                        <img v-else :src='noeye'></label>
                </div>
            </div>
            <div class="form-row justify-content-between align-items-end mt-2">
                <RouterLink class="form-link" :to='getUrl("login")'>{{ $t('Login') }}</RouterLink>
                <button type="submit" class="btn btn-primary">{{ $t('Confirm') }}</button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import logo from '@/assets/logo.svg';
import eye from '@/assets/eye.svg';
import noeye from '@/assets/noeye.svg';
import {Component, Vue} from 'vue-property-decorator';

import {createPath} from "~/logic/Utils";

@Component
export default class SignupPatient extends Vue {
    private logo = logo;
    private showPassword = false;
    private showRepeatPassword = false;
    private eye = eye;
    private noeye = noeye;

    toggleShowPassword(): void {
        this.showPassword = !this.showPassword;
    }

    toggleShowRepeatPassword(): void {
        this.showRepeatPassword = !this.showRepeatPassword;
    }

    getUrl(url:string):string{
        return createPath(url);
    }
}
</script>

<style scoped>
.signup-container {
    background-color: rgba(0, 196, 186, 0.205);
}

.register-form {
    background-color: #fff;
    border-radius: 1em !important;
    box-shadow: 10px 9px 12px 0px rgba(0, 196, 186, 0.205);
    box-sizing: border-box;
}


.form-link:hover {
    text-decoration: none;
}

.register-form input, .register-form select {
    background-color: #f0f0f0;
}

.register-form input:focus, .register-form select:focus {
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

.form-back-btn {
    background-color: grey !important;
    color: white;
}

.form-back-btn:hover {
    background-color: rgb(94, 94, 94) !important;
    color: white;
}

.card.card-shadow {
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.15);
    transition: box-shadow 0.3s ease-in-out;
}

.card.card-shadow:hover {
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.pointer {
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