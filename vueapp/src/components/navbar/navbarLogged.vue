<template>
    <div class="container w-100 justify-content-end">
        <router-link v-if="user.verified" :to='getUrl("home")' class="header-a-element nav-link mx-3">{{$t("MyAppointments")}}</router-link>
        <router-link v-if="user.verified && !staffs" :to='getUrl("mediclist/1")' class="header-a-element nav-link mx-3">{{$t("SearchMedics")}}</router-link>
        
        <div class="d-inline-flex flex-column align-items-end">
            <router-link v-if="!staffs" :to='getUrl("patient/profile")'>
                <!-- TODO:remove style -->
                <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">
                    {{user.firstName+" "+user.surname}}
                </p>
            </router-link>
            <router-link v-else :to='getUrl("staff/profile")'>
                <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">
                    {{user.firstName+" "+ user.surname}}
                </p>
            </router-link>
            <router-link :to='getUrl("logout")' class="m-0 p-0 header-a-element"><small class="m-0 p-0">{{$t("Logout")}}</small></router-link>
        </div>
        <img v-if="!user.verified" id="navbarUnverifiedUserImage" class="ml-2" src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="unverified user image">
        <router-link v-else-if="!staffs" :to='getUrl("patient/profile")'>
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
        </router-link>
        <router-link v-else :to='getUrl("staff/profile")'>
            <div style="width: 2em;" class="d-flex flex-column justify-content-center">
                <div class="profile-picture-container">
                    <div style="margin-top: 100%;"></div>
                    <img
                            id="navbarStaffUserImage"
                            class="profile-picture rounded-circle"
                            :src='getUrl("profilePics/"+user.profilePictureId)'
                            alt="profile pic"
                    />
                </div>
            </div>
        </router-link>
    </div>
</template>
<script>
import utils from "@/scripts/utils";
import apiTypes from "@/scripts/apiTypes";

export default {
    name:"NavbarLogged",
    props:{
        user:{
            type:apiTypes.User,
            required:true
        },
        staffs:[apiTypes.Staff]
    },
    methods:{
        getUrl:utils.getUrl
    }
}
</script>