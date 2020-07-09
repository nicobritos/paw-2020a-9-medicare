<template>
    <div class="container w-100 justify-content-end">
        <a v-if="user.verified" :href='getUrl("home")' class="header-a-element nav-link mx-3">{{$t("MyAppointments")}}</a>
        <a v-if="user.verified && !staffs" :href='getUrl("mediclist/1")' class="header-a-element nav-link mx-3">{{$t("SearchMedics")}}</a>
        
        <div class="d-inline-flex flex-column align-items-end">
            <a v-if="!staffs" :href='getUrl("patient/profile")'>
                <!-- TODO:remove style -->
                <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">
                    {{user.firstName+" "+user.surname}}
                </p>
            </a>
            <a v-else :href='getUrl("staff/profile")'>
                <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text">
                    {{user.firstName+" "+ user.surname}}
                </p>
            </a>
            <a :href='getUrl("logout")' class="m-0 p-0 header-a-element"><small class="m-0 p-0">{{$t("Logout")}}</small></a>
        </div>
        <img v-if="!user.verified" id="navbarUnverifiedUserImage" class="ml-2" src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="unverified user image">
        <a v-else-if="!staffs" :href='getUrl("patient/profile")'>
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
        </a>
        <a v-else :href='getUrl("staff/profile")'>
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
        </a>
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