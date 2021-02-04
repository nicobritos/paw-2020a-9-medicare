<template>
    <div class="container ml-0 mr-0 pr-0 fill-height">
        <!-- TODO: maybe there should be a delay -->
        <div v-if="doctor&&user&&locality&&monday" class="row h-100">
            <div class="col-4 h-100 grey-background">
                <div class="row mt-4">
                    <div class="col-3 d-flex flex-column justify-content-center">
                        <div class="profile-picture-container">
                            <div style="margin-top: 100%;"></div>
                            <img
                                class="profile-picture rounded-circle"
                                :src="getApiUrl('/users/' + doctor.user.id + '/picture')"
                                alt="profile pic"
                            />
                        </div>
                    </div>
                    <div class="col mr-3">
                        <div class="row mt-2">
                            <h5>{{ doctor.user.firstName + ' ' + doctor.user.surname }}</h5>
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
                    <!-- TODO: yo guido apuesto que esto se rompe en la primera de cambio -->
                    <p class="m-0"><b>{{ $t('Address') }}:</b> {{ doctor.office.street }} - {{locality.name}}</p>
                    <a
                        class="link"
                        :href="'https://www.google.com/maps/search/?api=1&query='+ locality.name +','+doctor.office.street"
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
                        <button @click="changeWeek(-1)" class="btn" id="day-left">{{ prev }}</button>
                    </div>
                    <div class="d-flex flex-horizontal" id="week-container">
                        <div class="d-flex flex-vertical" id="day-container">

                        </div>
                    </div>
                    <div v-for="(_,i) in 7" :key="i" class="col-1 mr-4 p-0">
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
                        <button @click="changeWeek(1)" id="day-right" class="btn">{{ next }}</button>
                    </div>
                </div>
                <div v-if="noSlotsAvailable" class="row justify-content-center">
                    <p class="text-center mt-2" style="color:grey;">{{ $t('NoAvailableAppointmentsThisWeek') }}</p>
                </div>
            </div>
        </div>
        <div v-else class="row justify-content-center">
            <b-spinner class="loading-spinner"></b-spinner>
        </div>
    </div>
</template>

<script lang="ts">
import {Component, Vue, Watch} from 'vue-property-decorator';
import {User} from '~/logic/models/User';
import {Doctor} from '~/logic/models/Doctor';

import {createApiPath, createPath, Nullable} from '~/logic/Utils';
import { State } from 'vuex-class';
import { DoctorService } from '~/logic/interfaces/services/DoctorService';
import TYPES from '~/logic/types';
import { Locality } from '~/logic/models/Locality';
import { LocalityService } from '~/logic/interfaces/services/LocalityService';
import { AppointmentTimeSlotService } from '~/logic/interfaces/services/AppointmentTimeSlotService';
import { DateRange, FullDate } from '~/logic/models/utils/DateRange';
import { AppointmentTimeslotDate } from '~/logic/models/AppointmentTimeslotDate';
import { APIError } from '~/logic/models/APIError';

// @ts-ignore
Date.prototype.plusDays = function (i) {
    let date = new Date(this.valueOf());
    date.setDate(date.getDate() + i);
    return date;
};

@Component
export default class SelectAppointment extends Vue {
    @State(state => state.auth.user)
    private readonly user:User;
    private readonly prev = '<';
    private readonly next = '>';
    private monday:Date = this.getMonday(new Date());
    // TODO:check implications of type change
    private weekSlots:AppointmentTimeslotDate[] = [];
    private doctor:Nullable<Doctor> = null;
    private locality:Nullable<Locality> = null;

    async created() {
        if(!this.monday){
            let today = new Date();
            //@ts-ignore
            today.plusDays(7*parseInt(this.$route.params.weekNo))
            this.monday = this.getMonday(today)
        }
        //TODO:check nico
        this.doctor = await this.$container
                        .get<DoctorService>(TYPES.Services.DoctorService)
                        .get(parseInt(this.$route.params.memberId));
        //TODO:nico haceme un get por id de localidad y cambia esto
        // this.locality = await this.$container.get<LocalityService>(TYPES.Services.LocalityService).get("AR",1,this.doctor!.office.localityId!);
        this.locality = new Locality();
        this.locality.name = "Tigre";

        this.updateSlots()
    }

    updateWeek(){
        let today = new Date();
        //@ts-ignore
        today = today.plusDays( 7 * parseInt(this.$route.params.weekNo) );
        this.monday = this.getMonday(today);
        //TODO:refresh timeslots
        this.updateSlots();
    }

    async updateSlots(){
        //@ts-ignore
        let saturday = this.monday.plusDays(6);
        
        //TODO: esto me tirando un error raro
        let slots = await this.$container
                    .get<AppointmentTimeSlotService>(TYPES.Services.AppointmentTimeSlotService)
                    .list(this.doctor!.id,{
                            from: {
                                    year: this.monday.getFullYear(),
                                    month: this.monday.getMonth(),
                                    day: this.monday.getDate()
                            } as FullDate,
                            to: {
                                    year: saturday.getFullYear(),
                                    month: saturday.getMonth(),
                                    day: saturday.getDate()
                            } as FullDate
                    } as DateRange); 
        if(!(slots instanceof APIError)){
            this.weekSlots = slots;
        }       
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

    getApiUrl(url:string):string{
        return createApiPath(url);
    }

    getUrl(url:string):string{
        return createPath(url);
    }
    getMonday(day:Date):Date {
        // get day of week
        let toAdd = day.getDay();

        //remove the amount of days necessary to get to monday
        //(monday is 1, sunday is 0)
        toAdd = toAdd == 0 ? -6 : -1 * ( toAdd - 1 );
        
        //@ts-ignore
        return day.plusDays( toAdd );
    }
    changeWeek(num:number){
        let prevnum = parseInt(this.$route.params.weekNo);
        this.$router.push({name:"SelectAppointment",params:{
            ...this.$route.params,
            weekNo:""+(prevnum+num)
        }})
        this.updateWeek()
    }

    //checks if theres at least 1 timeslot this week
    //TODO:check that its working
    get noSlotsAvailable(){
        if(this.weekSlots.length > 0){
            for (const daySlot of this.weekSlots) {
                if(daySlot.timeslots.length > 0){
                    return false;
                }
            }
        }
        return true;
    }
}
</script>

<style scoped>
.grey-background {
    background-color: rgba(214, 214, 214);
}

.fill-height {
    flex: 1 1 auto;
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
.loading-spinner{
    margin-top: 5rem;
    width:7rem;
    height:7rem;
    color: rgb(0, 160, 152)
}
</style>