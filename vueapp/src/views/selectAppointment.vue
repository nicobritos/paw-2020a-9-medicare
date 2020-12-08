<template>
    <div class="container ml-0 mr-0 pr-0 fill-height">
        <div class="row h-100">
            <div class="col-4 h-100 grey-background">
                <div class="row mt-4">
                    <div class="col-3 d-flex flex-column justify-content-center">
                        <div class="profile-picture-container">
                            <div style="margin-top: 100%;"></div>
                            <img
                                class="profile-picture rounded-circle"
                                :src='getUrl("profilePics/"+doctor.user.profilePictureId)'
                                alt="profile pic"
                            />
                        </div>
                    </div>
                    <div class="col mr-3">
                        <div class="row mt-2">
                            <h5>{{ doctor.user.firstName + ' ' + doctor.user.surname }}}</h5>
                        </div>
                        <div class="row mt-3 d-flex justify-content-start">
                            <p>
                                <!-- TODO:check -->
                                <span v-for="(specialty,index) in doctor.doctorSpecialtyIds" :key="specialty.id">
                                    {{ index != doctor.doctorSpecialtyIds.length - 1 ? 'SPECIALTY' + ',' : 'SPECIALTY' }}
                                </span>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="row mt-3 pl-4">
                    <p class="m-0"><b>{{ $t('Address') }}:</b> {{ doctor.office.street }} - TODO Locality</p>
                    <a
                        class="link"
                        :href="'https://www.google.com/maps/search/?api=1&query=TODOLocality'+','+doctor.office.street"
                        target="_blank"
                    >
                        <small class="m-1">{{ $t('SeeInGoogleMaps') }}</small>
                    </a>
                </div>
                <span
                    v-if="(!!doctor.user.phone && doctor.user.phone.length) || (!!doctor.office.phone && doctor.office.phone.length)">
                    <div class="row mt-3 pl-4">
                        <p><b>{{ $t('Phones') }}:</b></p>
                    </div>
                    <ul>
                        <li v-if="!!doctor.user.phone && doctor.user.phone.length">{{ doctor.user.phone }} ({{ $t('Personal') }})</li>
                        <li v-if="!!doctor.office.phone && doctor.office.phone.length">{{ doctor.office.phone }} ({{ doctor.office.name }})</li>
                    </ul>
                </span>
                <div v-else class="row mt-3 pl-4">
                    <p><b>{{ $t('Phones') }}:</b> {{ $t('UserWithNoPhones') }}</p>
                </div>
                <div class="row mt-3 pl-4">
                    <p><b>{{ $t('Email') }}:</b> {{ doctor.email }}</p>
                </div>
            </div>
            <div class="col ml-5 mt-3 p-0">
                <div class="row">
                    <h4>{{ $t('SelectAppointment') }}</h4>
                </div>
                <div class="row">
                    <div class="col-1 p-0">
                        <button class="btn" id="day-left">{{ prev }}</button>
                    </div>
                    <div class="d-flex flex-horizontal" id="week-container">
                        <div class="d-flex flex-vertical" id="day-container">

                        </div>
                    </div>
                    <div v-for="i in 7" :key="i" class="col-1 mr-4 p-0">
                        <span class="d-flex flex-column align-items-center text-center">
                                <p class="mb-0">
                                    {{ getDoW(monday.plusDays(i).getDay()) }}
                                </p>
                            <!-- day/month -->
                                <p class="my-0">
                                    {{
                                        $t(
                                            'dom_moy',
                                            [
                                                monday.plusDays(i).getDate(),
                                                getMoY(monday.plusDays(i).getMonth())
                                            ]
                                        )
                                    }}
                                </p>
                            </span>
                        <div class="d-flex flex-column align-content-center">
                            <RouterLink
                                v-for="timeslot in weekSlots[monday.plusDays(i).getDay()]" :key="timeslot.id"
                                :to="'patient/appointment/'+doctor.id+'/'+timeslot.date.year+'/'+timeslot.date.monthOfYear+'/'+timeslot.date.dayOfMonth+'/'+timeslot.date.hourOfDay+'/'+timeslot.date.minuteOfHour"
                                class="btn btn-sm btn-secondary mb-2">
                                <p class="m-0">
                                    {{
                                        $t(
                                            'hod_moh',
                                            [timeWithZero(timeslot.date.hourOfDay), timeWithZero(timeslot.date.minuteOfHour)]
                                        )
                                    }}
                                </p>
                            </RouterLink>
                        </div>
                    </div>
                    <div class="col-1 p-0 flex-shrink-1">
                        <button id="day-right" class="btn">{{ next }}</button>
                    </div>
                </div>
                <div v-if="!timeslots || !timeslots.length" class="row justify-content-center">
                    <p class="text-center mt-2" style="color:grey;">{{ $t('NoAvailableAppointmentsThisWeek') }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import {Component, Vue} from 'vue-property-decorator';
import {User} from '~/logic/models/User';
import {Doctor} from '~/logic/models/Doctor';
import {Office} from '~/logic/models/Office';

import {createPath} from "~/logic/Utils";

// TODO:check
// @ts-ignore
Date.prototype.plusDays = function (i) {
    let date = new Date(this.valueOf());
    date.setDate(date.getDate() + i);
    return date;
};

@Component
export default class SelectAppointment extends Vue {
    private readonly user = new User();
    private readonly prev = '<';
    private readonly next = '>';
    private readonly monday = new Date(2020, 7, 13);
    private readonly weekSlots = [[], [], [], [], [], [], []];
    private readonly timeslots = [];
    private readonly doctor = new Doctor();

    created(): void {
        this.user.id = 1;
        this.user.email = 'example@email.com';
        this.user.firstName = 'firstName';
        this.user.surname = 'surname';
        this.user.verified = true;
        this.user.phone = '00000000';
        this.user.profilePictureId = 1;

        let office = new Office();
        office.id = 1;
        office.name = 'Consultorio de ' + this.user.surname;
        office.phone = this.user.phone;
        office.email = this.user.email;
        office.street = 'Street 1234';
        office.url = 'example.com';

        this.doctor.id = 1;
        this.doctor.user = this.user;
        this.doctor.phone = this.user.phone;
        this.doctor.email = this.user.email;
        this.doctor.registrationNumber = 12345;
        this.doctor.specialtyIds = [1];
    }

    public getMoY(t: number): string {
        switch (t) {
            case 0:
                return this.$t('January').toString();
            case 1:
                return this.$t('February').toString();
            case 2:
                return this.$t('March').toString();
            case 3:
                return this.$t('April').toString();
            case 4:
                return this.$t('May').toString();
            case 5:
                return this.$t('June').toString();
            case 6:
                return this.$t('July').toString();
            case 7:
                return this.$t('August').toString();
            case 8:
                return this.$t('September').toString();
            case 9:
                return this.$t('October').toString();
            case 10:
                return this.$t('November').toString();
            case 11:
                return this.$t('December').toString();
            default:
                return t.toString();
        }
    }

    public getDoW(t: number): string {
        switch (t) {
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
                return t.toString();
        }
    }

    //TODO:check typescript
    getUrl(url:string):string{
        return createPath(url);
    }
}
</script>