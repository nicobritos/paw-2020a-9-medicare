<template>
    <div class="container flex-fill mx-5 pl-5 mt-3 w-100">
        <div class="row">
            <!-- TODO:make component for reusability -->
            <div class="col-4 align-items-start d-flex flex-column">
                <div class="picture-container no-select">
                    <div class="w-100 d-flex flex-column justify-content-center">
                        <div class="profile-picture-container">
                            <div style="margin-top: 100%;"></div>
                            <img
                                id="profilePic"
                                class="profile-picture rounded-circle"
                                :src="getUrl('profilePics/'+user.profilePictureId)"
                                :srcset="defaultProfilePic"
                                alt="profile pics"
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
                    <div class="row mb-3">
                        <h3>{{ $t('Office') }}</h3>
                        <div v-for="doctor in doctors" :key="doctor.id" class="container p-0 m-0 pl-3">
                            <div class="row d-flex align-items-center justify-content-between">
                                <p class="m-0">{{ '- ' + doctor.office.name + ' - TEL: ' + doctor.office.phone }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <h3>{{ $t('Schedule') }}</h3>
                        <div class="container p-0 m-0 pl-3">
                            <div v-if="workdays.length == 0" class="container-fluid justify-content-center">
                                <p class="text-center mt-2" style="color:grey;">{{ $t('NoSchedule') }}</p>
                            </div>
                            <div v-for="workday in workdays" :key="workday.id"
                                 class="row d-flex align-items-center justify-content-between">
                                <p class="m-0">-
                                    {{
                                        $t('wkd_from_wksh_wksm_to_wkeh_wkem_cons',
                                            [
                                                getDow(workday.getDay()),
                                                timeWithZero(workday.startHour),
                                                timeWithZero(workday.startMinute),
                                                timeWithZero(workday.endHour),
                                                timeWithZero(workday.endMinute),
                                                workday.doctor.office.name
                                            ])
                                    }}
                                </p>
                                <!-- TODO: connect button -->
                                <button class="btn cancel-workday-btn" type="button">X</button>
                            </div>
                            <div class="row d-flex align-items-center justify-content-center my-3">
                                <button @click="openAddWorkday" class="btn btn-info">{{ $t('AddSchedule') }}</button>
                            </div>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <h3>{{ $t('Specialties') }}</h3>
                        <div v-if="specialties.length == 0" class="container-fluid justify-content-center">
                            <p class="text-center mt-2" style="color:grey;">{{ $t('NoSpecialties') }}</p>
                        </div>
                        <div v-for="specialty in specialties" :key="specialty.id" class="container p-0 m-0 pl-3">
                            <div class="row d-flex align-items-center justify-content-between">
                                <p class="m-0">{{ specialty.name }}</p>
                                <!-- TODO:connect button -->
                                <button class="btn cancel-specialty-btn" type="button">X</button>
                            </div>
                        </div>
                    </div>
                    <div class="row d-flex align-items-center justify-content-center my-3">
                        <button @click="openAddSpecialties" class="btn btn-info">{{ $t('AddSpecialty') }}</button>
                    </div>
                </div>
            </div>
            <div class="col-2">
            </div>
        </div>
        <AddWorkday v-model="showAddWorkday"/>
        <AddSpecialty v-model="showAddSpecialties"/>
    </div>

</template>

<script lang="ts">
import moreOptions from '@/assets/moreOptions.svg';
import editPencil from '@/assets/editPencil.svg';
import eye from '@/assets/eye.svg';
import noeye from '@/assets/noeye.svg';
import {Component, Vue} from 'vue-property-decorator';
import {User} from '~/logic/models/User';
import AddSpecialty from "./addSpecialty.vue";
import AddWorkday from "./addWorkday.vue";

import {createPath} from "~/logic/Utils";
import defaultProfilePic from "@/assets/defaultProfilePic.svg";

let user = new User();
user.email = user.firstName = user.phone = user.surname = 'asd';
user.id = user.profilePictureId = 1;

@Component({
    components:{
        AddSpecialty,
        AddWorkday
    }
})
export default class MedicProfile extends Vue {
    private moreOptions = moreOptions;
    private editPencil = editPencil;
    private eye = eye;
    private noeye = noeye;
    private readonly defaultProfilePic = defaultProfilePic;
    private user: User = user;
    private doctors = [];
    private workdays = [];
    private specialties = [];

    private passwordVis = false;
    private repeatPasswordVis = false;

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

    private showAddSpecialties = false;
    private showAddWorkday = false;

    getDow(day: Date): string {
        switch (day.getDay()) {
            case 1:
                return this.$t('Monday').toString();
            case 2:
                return this.$t('Tuesday').toString();
            case 3:
                return this.$t('Wednesday').toString();
            case 4:
                return this.$t('Thursday').toString();
            case 5:
                return this.$t('Friday').toString();
            case 6:
                return this.$t('Saturday').toString();
            case 0:
                return this.$t('Sunday').toString();
            default:
                return day.getDay().toString();
        }
    }

    timeWithZero(t: number): string {
        if (t < 10) {
            return '0' + t;
        } else {
            return t.toString();
        }
    }
    
    //TODO: check typescript
    getUrl(url:string):string{
        return createPath(url);
    }

    openAddSpecialties(){
        this.showAddSpecialties = true;
        return;
    }

    openAddWorkday(){
        this.showAddWorkday = true;
        return;
    }
}
</script>

<style scoped>
.toggle-visibility{
    position: absolute;
    bottom: 1em;
    right: 5em;
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
    background-color: rgb(214, 214, 214);
}

.white-text {
    color: white !important;
}

#navbarUserImage {
    width: 3em;
}

.turno-item {
    border-radius: 2em !important;
    background-color: rgb(214, 214, 214);
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

.picture-container {
    position: relative;
    height: 10em;
    width: 10em;
    border-radius: 10em;
}

.picture-container > * {
    position: absolute;
    top: 0;
    left: 0;
}

.picture-container .picture-overlay {
    z-index: 1;
    width: 100%;
    height: 100%;
    border-radius: 10em;
    opacity: 0;
    transition: opacity 0.1s ease-in-out 0s;
}

.picture-container:hover .picture-overlay,
.picture-container:active .picture-overlay {
    opacity: 100%;
}

.picture-container .picture-overlay > i {
    padding: 10px;
    background: #333333bb;
    border-radius: 50px;
    font-size: 20px;
    color: #ffffff;
    cursor: pointer;
}

#profilePic {
    height: 100%;
    border-radius: 10em;
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