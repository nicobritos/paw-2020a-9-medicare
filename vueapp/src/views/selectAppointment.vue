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
                                    :src='getUrl("profilePics/"+staff.user.profilePictureId)'
                                    alt="profile pic"
                            />
                        </div>
                    </div>
                    <div class="col mr-3">
                        <div class="row mt-2">
                            <h5>{{staff.user.firstName + " " +staff.user.surname}}}</h5>
                        </div>
                        <div class="row mt-3 d-flex justify-content-start">
                            <p>
                                <!-- TODO:check -->
                                <span v-for="(specialty,index) in staff.staffSpecialtyIds" :key="specialty.id">
                                    {{index != staff.staffSpecialtyIds.length-1? getSpecialty(specialty) + "," : getSpecialty(specialty)}}
                                </span>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="row mt-3 pl-4">
                    <p class="m-0"><b>{{$t("Address")}}:</b> {{staff.office.street}} - {{getLocality(staff.office.localityId)}}</p>
                    <a
                        class="link"
                        :href="'https://www.google.com/maps/search/?api=1&query='+getLocality(staff.office.localityId)+','+staff.office.street"
                        target="_blank"
                    >
                        <small class="m-1">{{$t("SeeInGoogleMaps")}}</small>
                    </a>
                </div>
                <span v-if="(!!staff.user.phone && staff.user.phone.length) || (!!staff.office.phone && staff.office.phone.length)">
                    <div class="row mt-3 pl-4">
                        <p><b>{{$t("Phones")}}:</b></p>
                    </div>
                    <ul>
                        <li v-if="!!staff.user.phone && staff.user.phone.length">{{staff.user.phone}} ({{$t("Personal")}})</li>
                        <li v-if="!!staff.office.phone && staff.office.phone.length">{{staff.office.phone}} ({{staff.office.name}})</li>
                    </ul>
                </span>
                <div v-else class="row mt-3 pl-4">
                    <p><b>{{$t("Phones")}}:</b> {{$t("UserWithNoPhones")}}</p>
                </div>
                <div class="row mt-3 pl-4">
                    <p><b>{{$t("Email")}}:</b> {{staff.email}}</p>
                </div>
            </div>
            <div class="col ml-5 mt-3 p-0">
                <div class="row">
                    <h4>{{$t("SelectAppointment")}}</h4>
                </div>
                <div class="row">
                    <div class="col-1 p-0">
                        <button class="btn" id="day-left">{{prev}}</button>
                    </div>
                    <div class="d-flex flex-horizontal" id="week-container">
                        <div class="d-flex flex-vertical" id="day-container">

                        </div>
                    </div>
                    <div v-for="i in 7" :key="i" class="col-1 mr-4 p-0">
                        <span class="d-flex flex-column align-items-center text-center">
                                <p class="mb-0">
                                    {{getDoW(monday.plusDays(i).getDay())}}
                                </p>
                                <!-- day/month -->
                                <p class="my-0">
                                    {{$t(
                                        "dom_moy",
                                        [
                                            monday.plusDays(i).getDate(),
                                            getMoY(monday.plusDays(i).getMonth())
                                        ]
                                    )}}
                                </p>
                            </span>
                        <div class="d-flex flex-column align-content-center">
                            <router-link 
                                v-for="timeslot in weekSlots[monday.plusDays(i).getDay()]" :key="timeslot.id"
                                :to="'patient/appointment/'+staff.id+'/'+timeslot.date.year+'/'+timeslot.date.monthOfYear+'/'+timeslot.date.dayOfMonth+'/'+timeslot.date.hourOfDay+'/'+timeslot.date.minuteOfHour"
                                class="btn btn-sm btn-secondary mb-2">
                                <p class="m-0">
                                    {{$t(
                                        "hod_moh",
                                        [timeWithZero(timeslot.date.hourOfDay),timeWithZero(timeslot.date.minuteOfHour)]
                                    )}}
                                </p>
                            </router-link>
                        </div>
                    </div>
                    <div class="col-1 p-0 flex-shrink-1">
                        <button id="day-right" class="btn">{{next}}</button>
                    </div>
                </div>
                <div v-if="!timeslots || !timeslots.length" class="row justify-content-center">
                    <p class="text-center mt-2" style="color:grey;">{{$t("NoAvailableAppointmentsThisWeek")}}</p>
                </div>
            </div>
        </div>
    </div>    
</template>

<script>
import utils from "@/logic/utils";
import apiTypes from "@/logic/apiTypes";

// TODO:check
Date.prototype.plusDays = function(i) {
    let date = new Date(this.valueOf());
    date.setDate(date.getDate() + i);
    return date;
}


export default {
    name:"SelectAppointment",
    data(){
        let user = new apiTypes.User(1,"example@email.com","firstName","surname",true,"0000-0000",1);
        return {
            prev:"<",
            next:">",

            monday:new Date(2020,7,13),
            weekSlots:[[],[],[],[],[],[],[]],
            timeslots:[],
            user:user,
            staff:new apiTypes.Staff(
                1,
                "0000-0000",
                "example@email.com",
                1,
                user,
                new apiTypes.Office(1,"0000-0000","example@email.com","street","url",1),
                [1])
        }
    },
    methods:{
        getLocality(id){
            return "locality" + id;
        },
        getSpecialty(id){
            return "Specialty" + id;
        },
        getMoY(t){
            switch(t){
                case 0:
                    return this.$t("January");
                case 1:
                    return this.$t("February");
                case 2:
                    return this.$t("March");
                case 3:
                    return this.$t("April");
                case 4:
                    return this.$t("May");
                case 5:
                    return this.$t("June");
                case 6:
                    return this.$t("July");
                case 7:
                    return this.$t("August");
                case 8:
                    return this.$t("September");
                case 9:
                    return this.$t("October");
                case 10:
                    return this.$t("November");
                case 11:
                    return this.$t("December");
                default:
                    return t;
            }
        },
        getDoW(t){
            switch (t) {   
                case 1:
                    return this.$t("Monday");
                case 2:
                    return this.$t("Tuesday");
                case 3:
                    return this.$t("Wednesday");
                case 4:
                    return this.$t("Thursday");
                case 5:
                    return this.$t("Friday");
                case 6:
                    return this.$t("Saturday");
                case 0:
                    return this.$t("Sunday");
                default:
                    return t;
            }
        },
        getUrl:utils.getUrl
    }
}
</script>