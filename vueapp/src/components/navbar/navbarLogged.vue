<template>
    <div class="container w-100 justify-content-end">
        <RouterLink v-if="user.verified" to="Landing" class="header-a-element nav-link mx-3">
            {{ $t('MyAppointments') }}
        </RouterLink>
        <RouterLink v-if="user.verified && !isDoctor" :to='getUrl("mediclist/1")'
                     class="header-a-element nav-link mx-3">{{ $t('SearchMedics') }}
        </RouterLink>

        <div class="d-inline-flex flex-column align-items-end">
            <RouterLink v-if="!isDoctor" to="PatientProfile">
                <!-- TODO:remove style -->
                <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">
                    {{ user.firstName + ' ' + user.surname }}
                </p>
            </RouterLink>
            <RouterLink v-else to="MedicProfile">
                <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">
                    {{ user.firstName + ' ' + user.surname }}
                </p>
            </RouterLink>
            <a @click="logout" href="" class="m-0 p-0 header-a-element"><small
                class="m-0 p-0">{{ $t('Logout') }}</small></a>
        </div>
        <img v-if="!user.verified" id="navbarUnverifiedUserImage" class="ml-2"
             src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="unverified user image">
        <RouterLink v-else-if="!doctors" :to='getUrl("patient/profile")'>
            <!-- TODO:move style to css -->
            <div style="width: 2em;" class="d-flex flex-column justify-content-center">
                <div class="profile-picture-container">
                    <div style="margin-top: 100%;"></div>
                    <img
                        id="navbarPatientUserImage"
                        class="profile-picture rounded-circle"
                        :src='getUrl("profilePics/"+user.profilePictureId)'
                        alt="profile pic"
                    />
                </div>
            </div>
        </RouterLink>
        <RouterLink v-else :to='getUrl("doctor/profile")'>
            <div style="width: 2em;" class="d-flex flex-column justify-content-center">
                <div class="profile-picture-container">
                    <div style="margin-top: 100%;"></div>
                    <img
                        id="navbarDoctorUserImage"
                        class="profile-picture rounded-circle"
                        :src='getUrl("profilePics/"+user.profilePictureId)'
                        alt="profile pic"
                    />
                </div>
            </div>
        </RouterLink>
    </div>
</template>

<script lang="ts">
import {Component, Vue} from 'vue-property-decorator';
import {State} from 'vuex-class';
import {User} from '~/logic/models/User';
import {Doctor} from '~/logic/models/Doctor';
import {Patient} from '~/logic/models/Patient';

import {createPath} from "~/logic/Utils";

@Component
export default class NavbarLogged extends Vue {
    @State(state => state.auth.user)
    private readonly user: User;
    @State(state => state.auth.doctors)
    private readonly doctors: Doctor[];
    @State(state => state.auth.patients)
    private readonly patients: Patient[];
    @State(state => state.auth.isDoctor)
    private readonly isDoctor: boolean;

    logout(e: Event): void {
        e.preventDefault();

        // TODO:check because it throws an error if redundant navigation to same url
        this.$router.push('Landing');
        this.$emit('logout');
    };
    //TODO:check typescript
    getUrl(url:string):string{
        return createPath(url);
    }
}
</script>