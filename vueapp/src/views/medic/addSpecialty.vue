<template>
    <div class="container w-100 h-100 d-flex flex-column justify-content-center align-items-center">
        <form class="addturn-form border p-5 rounded">
            <div class="row">
                <h6>Medicare <img :src='logo' id="logo" alt="logo"/></h6>
            </div>
            <div class="row justify-content-start">
                <h1 class="addturn-form-title">
                    {{$t("AddSpecialty")}}
                </h1>
            </div>

            <div class="form-group row">
                <div class="col">
                    <label for="specialtyId">
                        {{$t("Specialty")}}
                    </label>
                </div>
                <div class="col-8">
                    <select class="form-control" name="specialtyId" id="specialtyId" path="specialtyId">
                        <option :value="null" disabled selected>{{ $t('Specialty') }}</option>
                        <option :value="null">{{ $t('Any') }}</option>
                        <option v-for="specialty in specialties" :key="specialty.id"
                                :value="specialty.id">{{ specialty.name }}</option>
                    </select>
                </div>
            </div>

            <div class="form-row justify-content-between">
                <a :href="getUrl('/doctor/profile')">
                    <button class="form-atras-btn btn" type="button">
                        {{$t("Back")}}
                    </button>
                </a>
                <button type="submit" class="btn btn-primary">
                    {{$t("Add")}}
                </button>
            </div>
        </form>
    </div>
</template>

<script lang="ts">
import {Component, Vue} from 'vue-property-decorator';
import logo from "@/assets/logo.svg";
import { createPath } from "@/logic/Utils";
import {doctorSpecialtyActionTypes} from '~/store/types/doctorSpecialties.types';
import { State } from 'vuex-class';

@Component
export default class AddSpecialty extends Vue {
    private readonly logo = logo;

    @State(state => state.doctorSpecialties.doctorSpecialties)
    private readonly specialties: [];

    mounted(): void {
        this.$store.dispatch('doctorSpecialties/loadDoctorSpecialties', doctorSpecialtyActionTypes.loadDoctorSpecialties());
    }

    public getUrl(url:string):string{
        return createPath(url);
    }

}
</script>

<style scoped>
body, html {
    height: 100%;
    background-color: rgba(0, 196, 186, 0.205);
}

.addturn-form {
    background-color: #fff;
    border-radius: 1em !important;
    box-shadow: 10px 9px 12px 0px rgba(0, 196, 186, 0.205);
}


.form-link:hover {
    text-decoration: none;
}

.addturn-form input, .addturn-form select {
    background-color: #f0f0f0;
}

.addturn-form input:focus, .addturn-form select:focus {
    background-color: #e0e0e0;
}

.addturn-form-title {
    margin-bottom: 1em;
}

.addturn-form #logo {
    width: 1em;
}

.addturn-form button {
    background-color: #00C4BA;
    color: white;
}

.addturn-form button:hover {
    background-color: rgb(1, 150, 142);
    color: #fafafa;
}

.form-atras-btn {
    background-color: grey !important;
    color: white;
}

.form-atras-btn:hover {
    background-color: rgb(94, 94, 94) !important;
    color: white;
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