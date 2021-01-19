<template>
    <div class="container h-75 w-100 mt-5">
        <div class="row h-100">
            <div class="col-4 h-100 pl-0 mr-3 w-100">
                <h4>{{ $t('AgendaFor') }} {{ $t('today') }}</h4>
                <ul class="list-group turno-list mr-2 w-100 h-100 overflow-auto">
                    <div v-if="!todayAppointments" class="container-fluid justify-content-center">
                        <p class="text-left mt-4" style="color:grey;">{{ $t('NoAppointmentsToday') }}</p>
                    </div>
                    <li v-for="appointment in todayAppointments" :key="appointment.id"
                        class="list-group-item turno-item mb-3" id="lit">
                        <div class="container">
                            <div class="row">
                                <div class="col-4 d-flex flex-column justify-content-center">
                                    <div class="profile-picture-container">
                                        <div style="margin-top: 100%;"></div>
                                        <!-- TODO: profile pic -->
                                        <img
                                            class="profile-picture rounded-circle"
                                            :src='getApiUrl("/users/" + appointment.patient.user.id)'
                                            alt="profile pic"
                                        />
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="row justify-content-start">
                                        <h5>
                                            {{
                                                $t('name_surname', [appointment.patient.user.firstName, appointment.patient.user.surname])
                                            }}</h5>
                                    </div>
                                    <div class="row">
                                        <p class="m-0">
                                            {{
                                                $t(
                                                    'fhom_fmoh_thod_tmoh',
                                                    [
                                                        timeWithZero(appointment.fromDate.hourOfDay),
                                                        timeWithZero(appointment.fromDate.minuteOfHour),
                                                        timeWithZero(appointment.toDate.hourOfDay),
                                                        timeWithZero(appointment.toDate.minuteOfHour)
                                                    ]
                                                )
                                            }}
                                        </p>
                                    </div>
                                </div>
                                <div class="col-2 justify-content-start">
                                    <div class="dropdown">
                                        <img
                                            :src='moreOptions'
                                            class="moreOptionsButton"
                                            alt=""
                                            data-toggle="dropdown"
                                        />
                                        <div class="dropdown-menu">
                                            <!-- TODO add reprogramar -->
                                            <!-- TODO cancel -->
                                            <button type="submit" class="dropdown-item">{{ $t('Cancel') }}</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="col">
                <h4>{{ $t('WeeklyAgenda') }}</h4>
                <table class="table table-borderless">
                    <tr>
                        <td class="px-0">
                            <!-- TODO:connect button -->
                            <button type="button" class="btn" id="prevWeekBtn">{{ prev }}</button>
                        </td>
                        <!-- TODO:check, should be 0 to 6 -->
                        <td v-for="i in 7" :key="i" class="px-0">
                            <!-- day of the week -->
                            <!-- TODO:CHECK -->
                            <span
                                class="medicare-day-span container px-0 mx-2 d-flex flex-column align-items-center text-center"
                                :data-day="monday.plusDays(i)"
                                :style='(monday.plusDays(i-1) == today)?"font-weight:bold":""'>
                                <p class="mb-0"
                                   v-if="monday.plusDays(i-1).getDay() == 1">{{ $t('MondayAbbreviated') }}</p>
                                <p class="mb-0"
                                   v-else-if="monday.plusDays(i-1).getDay() == 2">{{ $t('TuesdayAbbreviated') }}</p>
                                <p class="mb-0"
                                   v-else-if="monday.plusDays(i-1).getDay() == 3">{{ $t('WednesdayAbbreviated') }}</p>
                                <p class="mb-0"
                                   v-else-if="monday.plusDays(i-1).getDay() == 4">{{ $t('ThursdayAbbreviated') }}</p>
                                <p class="mb-0"
                                   v-else-if="monday.plusDays(i-1).getDay() == 5">{{ $t('FridayAbbreviated') }}</p>
                                <p class="mb-0"
                                   v-else-if="monday.plusDays(i-1).getDay() == 6">{{ $t('SaturdayAbbreviated') }}</p>
                                <p class="mb-0"
                                   v-else-if="monday.plusDays(i-1).getDay() == 0">{{ $t('SundayAbbreviated') }}</p>
                                <p class="mb-0" v-else>{{ monday.plusDays(i - 1).getDay() }}</p>
                                <!-- day/month -->
                                <p class="my-0">
                                    {{
                                        $t('dom_moy', [monday.plusDays(i - 1).getDate(), getMpdMonthOfYear(monday.plusDays(i - 1).getMonth())])
                                    }}
                                </p>
                                <p>
                                    {{
                                        $t(
                                            'NumberedAppointments',
                                            [
                                                weekAppointments[monday.plusDays(i - 1).getDay()].length,
                                                (weekAppointments[monday.plusDays(i - 1).getDay()].length == 1) ?
                                                    $t('appointmentAbbreviated') :
                                                    $t('appointmentsAbbreviated')
                                            ]
                                        )
                                    }}
                                </p>
                            </span>
                        </td>
                        <td class="px-0">
                            <!-- TODO: connect button -->
                            <button type="button" class="btn" id="nextWeekBtn">{{ next }}</button>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="9">
                            <div class="container-fluid d-flex justify-content-center">
                                <ul class="list-group turno-list mr-2 w-50 overflow-auto">
                                    <div v-if="!weekAppointments[today.getDay()]"
                                         class="container-fluid justify-content-center">
                                        <p class="text-center mt-4" style="color:grey;">
                                            {{ $t('NoAppointmentsThisDay') }}</p>
                                    </div>
                                    <li v-for="appointment in weekAppointments[today.getDay()]" :key="appointment.id"
                                        class="list-group-item turno-item mb-3">
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-4 d-flex flex-column justify-content-center">
                                                    <div class="profile-picture-container">
                                                        <div style="margin-top: 100%;"></div>
                                                        <imgmonth
                                                            class="profile-picture rounded-circle"
                                                            :src='"/profilePics/"+appointment.patient.user.profilePicture.id'
                                                            alt="profile pic"
                                                        />
                                                    </div>
                                                </div>
                                                <div class="col-6">
                                                    <div class="row justify-content-start">
                                                        <h5>
                                                            {{
                                                                appointment.patient.user.firstName + ' ' + appointment.patient.user.surname
                                                            }}</h5>
                                                    </div>
                                                    <div class="row">
                                                        <p class="m-0">
                                                            {{
                                                                $t(
                                                                    'fhom_fmoh_thod_tmoh',
                                                                    [
                                                                        timeWithZero(appointment.fromDate.hourOfDay),
                                                                        timeWithZero(appointment.fromDate.minuteOfHour),
                                                                        timeWithZero(appointment.toDate.hourOfDay),
                                                                        timeWithZero(appointment.toDate.minuteOfHour)
                                                                    ]
                                                                )
                                                            }}
                                                        </p>
                                                    </div>
                                                </div>
                                                <div class="col-2 justify-content-start">
                                                    <div class="dropdown">
                                                        <img
                                                            :src='moreOptions'
                                                            class="moreOptionsButton"
                                                            alt="more options"
                                                            data-toggle="dropdown"
                                                        />
                                                        <div class="dropdown-menu">
                                                            <!-- TODO add reprogramar -->
                                                            <!-- TODO connect button -->
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
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import moreOptions from '@/assets/moreOptions.svg';
import {Component, Vue} from 'vue-property-decorator';
import {Appointment} from '~/logic/models/Appointment';

// @ts-ignore
Date.prototype.plusDays = function (i) {
    let date = new Date(this.valueOf());
    date.setDate(date.getDate() + i);
    return date;
};

@Component
export default class MedicHome extends Vue {
    private next = '>';
    private prev = '<';
    private moreOptions = moreOptions;
    private monday = new Date(2020, 7, 6);
    private today = new Date(2020, 7, 9);
    private todayAppointments: Appointment[] = [];
    private weekAppointments: Appointment[] = [[], [], [], [], [], [], []];

    async mounted(): Promise<void> {

    }

    timeWithZero(time: number): string {
        if (time < 10) {
            return '0' + time;
        } else {
            return time.toString();
        }
    }

    getMpdMonthOfYear(i: number): string {
        // @ts-ignore
        switch (this.monday.plusDays(i).monthOfYear) {
            case 0:
                return this.$t('JanuaryAbbreviated').toString();
            case 1:
                return this.$t('FebruaryAbbreviated').toString();
            case 2:
                return this.$t('MarchAbbreviated').toString();
            case 3:
                return this.$t('AprilAbbreviated').toString();
            case 4:
                return this.$t('MayAbbreviated').toString();
            case 5:
                return this.$t('JuneAbbreviated').toString();
            case 6:
                return this.$t('JulyAbbreviated').toString();
            case 7:
                return this.$t('AugustAbbreviated').toString();
            case 8:
                return this.$t('SeptemberAbbreviated').toString();
            case 9:
                return this.$t('OctoberAbbreviated').toString();
            case 10:
                return this.$t('NovemberAbbreviated').toString();
            case 11:
                return this.$t('DecemberAbbreviated').toString();
            default:
                // @ts-ignore
                return this.monday.plusDays(i).monthOfYear;
        }
    }
}
</script>

<style scoped>
.turno-item {
    border-radius: 2em !important;
    background-color: rgb(214, 214, 214);
}

.turno-list {
    -ms-overflow-style: none;
    scrollbar-width: none;
    height: 59vh;
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

.medicare-day-span {
    cursor: pointer;
}
</style>