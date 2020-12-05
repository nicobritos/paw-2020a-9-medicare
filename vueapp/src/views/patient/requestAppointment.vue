<template>
    <div class="container fill-height">
        <div class="row mt-4">
            <!-- TODO:connect form -->
            <form class="col d-flex flex-column" id="appointment-request-form">
                <h4 class="text-muted">{{ $t('ScheduleAppointment') }}</h4>
                <p class="mt-3 text-muted">{{ $t('Motive') }}</p>
                <label for="motive"></label>
                <input :placeholder="$t('Motive')" type="text"
                       name="motive" id="motive" class="form-control w-50"/>
                <p class="mt-3 text-muted mb-1">{{ $t('PersonalData') }}</p>
                <div class="container-fluid p-0 mb-1 d-flex flex-row">
                    <div class="col px-0">
                        <p>{{ user.firstName }}</p>
                    </div>
                    <div class="col p-0 ml-2">
                        <p>{{ user.surname }}</p>
                    </div>
                </div>
                <input :placeholder='$t("Phone")' type="text" name="phone" id="phone"
                       class="form-control w-50 mb-1"/>
                <p>{{ user.email }}</p>
                <textarea :placeholder="$t('OptionalComment')" class="form-control mt-3" name="comment"
                          id="comment" cols="30" rows="5"/>
                <button type="submit" id="appointment-request-button" class="btn btn-info mt-3 w-100">
                    {{ $t('ScheduleAppointment') }}
                </button>
            </form>
            <div class="col">
                <div class="container details-container mt-5 p-3 w-75">
                    <div class="row justify-content-center">
                        <h4 class="white-text">{{ $t('AppointmentDetails') }}/></h4>
                    </div>
                    <div class="row justify-content-center border-top border-light py-2">
                        <div class="col-3 d-flex flex-column justify-content-center">
                            <div class="profile-picture-container">
                                <div style="margin-top: 100%;"></div>
                                <img
                                    class="profile-picture rounded-circle"
                                    :src="getUrl('profilePics/'+ staff.user.profilePicturId)"
                                    alt="profile pic"
                                />
                            </div>
                        </div>
                        <div class="col p-0">
                            <p class="m-0 white-text">{{ staff.user.firstName }} {{ staff.user.surname }}</p>
                            <small class="white-text">
                                <!-- TODO: check -->
                                {{ staff.staffSpecialties }}
                            </small>
                        </div>
                    </div>
                    <div class="row justify-content-center border-top border-light py-2">
                        <div class="col-3 d-flex align-items-center justify-content-center">
                            <img :src='calendarIcon' class="w-75" alt="calendar icon">
                        </div>
                        <div class="col p-0">
                            <p class="m-0 white-text">
                                {{
                                    $t(
                                        'dow_dom_moy_hod_moh',
                                        [
                                            getDoW(date.getDay()),
                                            date.getDate(),
                                            getMoY(date.getMonth()),
                                            timeWithZero(date.getHours()),
                                            timeWithZero(date.getMinutes())
                                        ]
                                    )
                                }}
                            </p>
                            <a :href="getUrl('appointment/'+staff.id+'/0')"><small
                                class="white-text">{{ $t('ChangeDate') }}</small></a>
                        </div>
                    </div>
                    <div class="row justify-content-center border-top border-light py-2">
                        <div class="col-3 d-flex align-items-center justify-content-center">
                            <img :src='mapIcon ' class="w-75" alt="map icon">
                        </div>
                        <div class="col p-0">
                            <!-- TODO: check -->
                            <p class="m-0 white-text">{{ staff.office.street }} -
                                {{ getLocality(staff.office.localityId) }}</p>
                            <a
                                class="link"
                                :href="'https://www.google.com/maps/search/?api=1&query='+getLocality(staff.office.localityId)+','+staff.office.street"
                                target="_blank"
                            >
                                <small class="white-text m-0">{{ $t('SeeInGoogleMaps') }}</small>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import mapIcon from '@/assets/mapIcon.svg';
import calendarIcon from '@/assets/calendarIcon.svg';
import {Component, Vue} from 'vue-property-decorator';
import {User} from '~/logic/models/User';
import {Staff} from '~/logic/models/Staff';

let user = new User();
let staff = new Staff();

staff.email = staff.phone = user.email = user.firstName = user.phone = user.surname = 'asd';
user.id = user.profilePictureId = staff.id = 1;

@Component
export default class RequestAppointment extends Vue {
    private mapIcon = mapIcon;
    private calendarIcon = calendarIcon;
    private date = new Date(2020, 1, 10);
    private staff = staff;
    private user = user;

    getDoW(t: number): string {
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
            case 7:
                return this.$t('Sunday').toString();
            default:
                return t.toString();
        }
    }

    getMoY(t: number): string {
        switch (t) {
            case 1:
                return this.$t('January').toString();
            case 2:
                return this.$t('February').toString();
            case 3:
                return this.$t('March').toString();
            case 4:
                return this.$t('April').toString();
            case 5:
                return this.$t('May').toString();
            case 6:
                return this.$t('June').toString();
            case 7:
                return this.$t('July').toString();
            case 8:
                return this.$t('August').toString();
            case 9:
                return this.$t('September').toString();
            case 10:
                return this.$t('October').toString();
            case 11:
                return this.$t('November').toString();
            case 12:
                return this.$t('December').toString();
            default:
                return t.toString();
        }
    }

    timeWithZero(t: number): string {
        if (t < 10) {
            return '0' + t;
        } else {
            return t.toString();
        }
    }

    getLocality(id: number): string {
        return 'Locality' + id;
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

.fill-height {
    flex: 1 1 auto;
}

.white-text {
    color: white !important;
}

#navbarUserImage {
    width: 3em;
}

.details-container {
    background-color: #00C4BA;
    border-radius: 3em;
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