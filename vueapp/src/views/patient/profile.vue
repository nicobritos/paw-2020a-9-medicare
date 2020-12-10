<template>
    <div class="container flex-fill mx-5 pl-5 mt-3 w-100">
        <div class="row">
            <!-- TODO:make component for reusability -->
            <div class="col-4 align-items-start d-flex flex-column">
                <!-- TODO check imagen -->
                <div class="picture-container no-select">
                    <div class="w-100 d-flex flex-column justify-content-center">
                        <div class="profile-picture-container">
                            <div style="margin-top: 100%;"></div>
                            <img
                                id="profilePic"
                                class="profile-picture rounded-circle"
                                :src="getUrl('profilePics/'+user.profilePictureId)"
                                alt="profile pic"
                            />
                        </div>
                    </div>
                    <div class="picture-overlay d-flex flex-column align-items-center justify-content-end pb-3">
                        <input id="profile-picture-input" style="display: none;" type="file" accept="image/*">
                        <i class="fas fa-pencil-alt"></i>
                    </div>
                </div>
            </div>
            <div class="col-6">
                <div class="container p-0 pt-4 m-0">
                    <!-- TODO:connect form -->
                    <!-- TODO:make component for reusability -->
                    <form>
                        <div class="row">
                            <div class="col p-0 m-0">
                                <!-- TODO Connect image function-->
                                <h3>{{ $t('Name') }}
                                    <label for="firstName" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar" @click="enableFirstnameMod"/>
                                    </label>
                                </h3>
                                <input class="form-control mb-3 w-75" id="firstName" name="firstName"
                                       :value="user.firstName" :readonly="!firstnameModEnabled"/>
                            </div>
                            <div class="col p-0 m-0">
                                <!-- TODO Connect image function-->
                                <h3>{{ $t('Surname') }}
                                    <label for="surname" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar" @click="enableSurnameMod"/>
                                    </label>
                                </h3>
                                <input class="form-control mb-3 w-75" name="surname" id="surname" :value="user.surname"
                                       :readonly="!surnameModEnabled"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col p-0 m-0">
                                <h3>{{ $t('Phone') }}
                                    <label for="phone" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar" @click="enablePhoneMod">
                                    </label>
                                </h3>
                                <input class="form-control mb-3 w-75" id="phone" name="phone" :value="user.phone"
                                       :readonly="!phoneModEnabled"/>
                            </div>
                            <div class="col p-0 m-0">
                                <h3>{{ $t('Email') }}
                                    <label for="email" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar" @click="enableEmailMod"/>
                                    </label>
                                </h3>
                                <input class="form-control mb-3 w-75" id="email" name="email" :value="user.email"
                                       :readonly="!emailModEnabled"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col p-0 m-0">
                                <h3>{{ $t('Password') }}
                                    <label for="password" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar" @click="enablePasswordMod"/>
                                    </label>
                                </h3>
                                <input :type='passwordVis? "text": "password"' class="form-control mb-3 w-75" id="password" name="password"
                                       :readonly="!passwordModEnabled"/>
                                <label for="password" class="toggle-visibility">
                                    <img type="button" :src='eye' v-if="!passwordVis && passwordModEnabled" alt="not visible password" @click="passwordVis=true">
                                    <img type="button" :src='noeye' v-else-if="passwordModEnabled" alt="visible password" @click="passwordVis=false">
                                </label>
                            </div>
                            <div v-if="passwordModEnabled" class="col p-0 m-0" id="repeat-password-container">
                                <h3>
                                    {{ $t('RepeatPassword') }}
                                    <label for="repeatPassword" class="toggle-readonly">
                                        <img :src='editPencil' alt="editar"/>
                                    </label>    
                                </h3>
                                <input :type='repeatPasswordVis? "text":"password"' class="form-control mb-3 w-75"
                                       id="repeatPassword" name="repeatPassword"/>
                                <label for="repeatPassword" class="toggle-visibility">
                                    <img type="button" :src='eye' v-if="!repeatPasswordVis" alt="not visible password" @click="repeatPasswordVis=true">
                                    <img type="button" :src='noeye' v-else alt="visible password" @click="repeatPasswordVis=false">
                                </label>
                            </div>
                        </div>
                        <div class="row justify-content-center align-items-end mt-2">
                            <button type="submit" class="btn btn-info">{{ $t('ConfirmChanges') }}</button>
                        </div>
                    </form>
                    <div class="col-2">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import noeye from '@/assets/noeye.svg';
import eye from '@/assets/eye.svg';
import editPencil from '@/assets/editPencil.svg';
import {Component, Vue} from 'vue-property-decorator';
import {User} from '~/logic/models/User';

import {createPath} from "~/logic/Utils";

let user = new User();
user.email = user.firstName = user.phone = user.surname = 'asd';
user.id = user.profilePictureId = 1;

@Component
export default class PatientProfile extends Vue {
    private noeye = noeye;
    private eye = eye;
    private editPencil = editPencil;

    private passwordVis = false;
    private repeatPasswordVis = false;


    private user = user;

    //enable/disable readonly property of input
    private firstnameModEnabled = false;
    enableFirstnameMod():void{this.firstnameModEnabled=true};
    private surnameModEnabled = false;
    enableSurnameMod():void{this.surnameModEnabled=true};
    private phoneModEnabled = false;
    enablePhoneMod():void{this.phoneModEnabled=true};
    private emailModEnabled = false;
    enableEmailMod():void{this.emailModEnabled=true};
    private passwordModEnabled = false;
    enablePasswordMod():void{this.passwordModEnabled=true};


    //TODO: check typescript
    getUrl(url:string):string{
        return createPath(url);
    }

}
</script>

<style scoped>
.toggle-visibility{
    position: absolute;
    bottom: 1em;
    right: 5em;
}
</style>