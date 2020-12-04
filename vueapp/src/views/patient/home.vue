<template>
    <div class="container h-75 w-100 mt-5">
        <div class="row">
            <h4>{{ $t('MyAppointments') }}</h4>
        </div>
        <div class="row h-100">
            <div class="col h-100 pl-0 mr-5 w-100">
                <ul class="list-group turno-list mr-2 w-100 h-100 overflow-auto">
                    <div v-if="!appointments.length" class="container-fluid justify-content-center">
                        <p class="text-left mt-4" style="color:grey;">
                            <spring:message code="NoAppointments"/>
                        </p>
                    </div>
                    <li v-for="appointment in appointments" :key="appointment.id"
                        class="list-group-item turno-item mb-3">
                        <div class="container">
                            <div class="row">
                                <div class="col-4 d-flex flex-column justify-content-center">
                                    <div class="profile-picture-container">
                                        <div style="margin-top: 100%;"></div>
                                        <img
                                            class="profile-picture rounded-circle"
                                            :src="getUrl('profilePics/'+appointment.staff.user.profilePictureId)"
                                            alt="profile pic"
                                        />
                                    </div>
                                </div>
                                <div class="col-7">
                                    <div class="row justify-content-start">
                                        <h5>{{ appointment.staff.user.firstName }}
                                            {{ appointment.staff.user.surname }}</h5>
                                    </div>
                                    <div class="row">
                                        <p class="m-0">
                                            <!-- TODO: check -->
                                            {{ appointment.staff.staffSpecialties }}
                                        </p>
                                    </div>
                                    <div class="row">
                                        <p class="m-0">{{ appointment.staff.office.street }}</p>
                                    </div>
                                    <div class="row">
                                        <p class="m-0">
                                            <!-- TODO: Check whay would be to -->
                                            {{
                                                $t(
                                                    'dow_dom_moy_sh_sm_eh_em',
                                                    [
                                                        getDoW(appointment.from.getDay()),
                                                        appointment.from.getDate(),
                                                        getMoY(appointment.from.getMonth()),
                                                        timeWithZero(appointment.from.getHour()),
                                                        timeWithZero(appointment.from.getMinute()),
                                                        timeWithZero(appointment.to.getHour()),
                                                        timeWithZero(appointment.to.getMinute())
                                                    ]
                                                )
                                            }}
                                        </p>
                                    </div>
                                </div>
                                <div class="col-1 justify-content-start">
                                    <div class="dropdown">
                                        <img :src='moreOptions' class="moreOptionsButton"
                                             alt="nore options" data-toggle="dropdown">
                                        <div class="dropdown-menu">
                                            <!-- TODO: connect button -->
                                            <button type="button" class="dropdown-item cancel-appt-btn">
                                                {{ $t('Cancel') }}
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="col">
                <form class="container p-5 filter-form">
                    <div class="row justify-content-start">
                        <h3 class="form-title">{{ $t('SearchMedics') }}</h3>
                    </div>
                    <div class="row justify-content-start my-3">
                        <input class="w-100 form-control" type="text" name="name" id="name"
                               :placeholder='$t("NameAndOrSurname")'>
                    </div>
                    <div class="row justify-content-start my-3">
                        <select name="specialties" class="form-control">
                            <option value="-1" disabled selected>{{ $t('Specialty') }}</option>
                            <option value="-1">{{ $t('Any') }}</option>
                            <option v-for="specialty in specialties" :key="specialty.id" :value="specialty.id">
                                {{ specialty.name }}
                            </option>
                        </select>
                    </div>
                    <div class="row justify-content-start my-3">
                        <select name="localities" class="form-control">
                            <option value="-1" disabled selected>{{ $t('Locality') }}</option>
                            <option value="-1">{{ $t('Any') }}</option>
                            <option v-for="locality in localities" :key="locality.id" :value="locality.id">
                                {{ locality.name }}
                            </option>
                        </select>
                    </div>
                    <div class="row justify-content-start my-3">
                        <button class="w-100 btn rounded-pill btn-light header-btn-element">{{ $t('SearchMedics') }}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script lang="ts">

import {Component, Vue} from 'vue-property-decorator';

@Component
export default class PatientHome extends Vue {
    private appointments = [];
    private specialties = [];
    private localities = [];

    getDoW(t: number): string {
        switch (t) {
            case 1:
                return this.$t('Monday');
            case 2:
                return this.$t('Tuesday');
            case 3:
                return this.$t('Wednesday');
            case 4:
                return this.$t('Thursday');
            case 5:
                return this.$t('Friday');
            case 6:
                return this.$t('Saturday');
            case 7:
                return this.$t('Sunday');
            default:
                return t;
        }
    }

    getMoY(t: number): string {
        switch (t) {
            case 1:
                return this.$t('January');
            case 2:
                return this.$t('February');
            case 3:
                return this.$t('March');
            case 4:
                return this.$t('April');
            case 5:
                return this.$t('May');
            case 6:
                return this.$t('June');
            case 7:
                return this.$t('July');
            case 8:
                return this.$t('August');
            case 9:
                return this.$t('September');
            case 10:
                return this.$t('October');
            case 11:
                return this.$t('November');
            case 12:
                return this.$t('December');
            default:
                return t;
        }
    }

    timeWithZero(t: number): string {
        if (t < 10) {
            return '0' + t;
        } else {
            return t.toString();
        }
    }
}
</script>

<style scoped>

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