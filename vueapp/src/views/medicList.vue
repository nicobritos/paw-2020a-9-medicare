<template>
    <form>
        <div class="container h-75">
            <div class="row mt-4">
                <h4>
                    <!-- TODO:CHECK -->
                    {{ !!resultsMessageParam ? $t(resultsMessage, resultsMessageParam) : $t(resultsMessage) }}
                </h4>
            </div>
            <div class="row mt-4 justify-content-center">
                <input class="form-control w-100" type="text" name="name" :value="name" :placeholder='$t("Name")'/>
            </div>
            <div class="row mt-4">
                <div class="col-4 px-3">
                    <div class="row mt-4">
                        <select class="select-css form-control w-100" type="text" name="specialties"
                                id="selEspecialidad">
                            <option value="-1" disabled :selected="searchedSpecialties.length == 0">
                                {{ $t('Specialty') }}
                            </option>
                            <option value="-1">{{ $t('Any') }}</option>
                            <option v-for="specialty in specialties" :key="specialty.id"
                                    :value="specialty.id"
                                    :selected="searchedSpecialties.some(v => specialty.id == v.id)">
                                {{ specialty.name }}
                            </option>
                        </select>
                    </div>
                    <div class="row mt-4">
                        <select class="select-css form-control w-100" type="text" name="localities" id="localidad">
                            <option value="-1" disabled :selected="searchedLocalities.length == 0">
                                {{ $t('Locality') }}
                            </option>
                            <option value="-1">{{ $t('Any') }}</option>
                            <option v-for="locality in localities" :key="locality.id" :value="locality.id"
                                    :selected="searchedLocalities.some(v => locality.id == v.id)">
                                {{ locality.name }}
                            </option>
                        </select>
                    </div>
                    <div class="row mt-4">
                        <button type="submit" class="btn btn-info w-100 rounded-pill">{{ $t('Filter') }}</button>
                    </div>
                </div>
                <div class="col-1"></div>
                <div class="col">
                    <div v-if="paginator.totalPages != 0" id="paging"
                         class="p-3 d-flex container w-100 justify-content-center ">
                        <div v-if="page>2">
                            <button type="button" class="btn btn-info btn-sm mr-1 firstButton">{{ firstPage }}</button>
                        </div>
                        <div v-if="page > 1">
                            <button type="button" class="btn btn-info btn-sm prevButton">{{ prevPage }}</button>
                        </div>
                        <p class="d-inline mx-2">{{ $t('Page_of_totalPages', [page, paginator.totalPages]) }}</p>
                        <div v-if="paginator.remainingPages != 0">
                            <button type="button" class="btn btn-info btn-sm nextButton">{{ nextPage }}</button>
                        </div>
                        <div v-if="paginator.remainingPages > 1">
                            <button type="button" class="btn btn-info btn-sm ml-1 lastButton">{{ lastPage }}</button>
                        </div>
                    </div>
                    <ul class="list-group turno-list mr-2 w-100">
                        <!-- TODO:CHECK THIS IF -->
                        <div v-if="doctor.length == 0" class="container-fluid justify-content-center">
                            <p class="text-center" style="color:grey;">{{ $t('NoMedicsFound') }}</p>
                        </div>
                        <li v-for="member in doctor" :key="member.id" class="list-group-item turno-item mb-3">
                            <div class="container">
                                <div class="row">
                                    <div class="col-3 d-flex flex-column justify-content-center">
                                        <div class="profile-picture-container">
                                            <div style="margin-top: 100%;"></div>
                                            <!-- TODO:check src url -->
                                            <img
                                                class="profile-picture rounded-circle"
                                                :src='getUrl("profilePics/"+member.user.profilePictureId)'
                                                alt="profile pic"
                                            />
                                        </div>
                                    </div>
                                    <div class="col-6">
                                        <div class="row justify-content-start">
                                            <h5>{{ member.user.firstName + member.user.surname }}</h5>
                                        </div>
                                        <div class="row">
                                            <p class="m-0">
                                                <!-- TODO:super check this -->
                                                {{
                                                    member.doctorSpecialtyIds.map((v) => {
                                                        return getSpecialtyName(v);
                                                    }).join(', ')
                                                }}
                                            </p>
                                        </div>
                                        <div class="row">
                                            <p class="m-0">
                                                {{
                                                    member.office.street + ' - ' + getLocalityName(member.office.localityId)
                                                }}</p>
                                        </div>
                                        <a
                                            class="link"
                                            :href='"https://www.google.com/maps/search/?api=1&query="+getLocalityName(member.office.localityId) + "," + member.office.street'
                                            target="_blank"
                                        >
                                            <div class="row">
                                                <small class="m-0">{{ $t('SeeInGoogleMaps') }}</small>
                                            </div>
                                        </a>
                                    </div>
                                    <div class="col d-flex justify-content-center align-items-center">
                                        <RouterLink :to="getUrl(`appointment/${member.id}/0`)">
                                            <button type="button" class="btn btn-info available-appointments-button"
                                                    :data-id="member.id">
                                                {{ $t('AvailableAppointments') }}
                                            </button>
                                        </RouterLink>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <div v-if="paginator.totalPages != 0" id="paging"
                         class="p-3 d-flex container w-100 justify-content-center ">
                        <div v-if="page > 2">
                            <button type="button" class="btn btn-info btn-sm mr-1 firstButton">{{ firstPage }}</button>
                        </div>
                        <div v-if="page > 1">
                            <button type="button" class="btn btn-info btn-sm prevButton">{{ prevPage }}</button>
                        </div>
                        <p class="d-inline mx-2">{{ $t('Page_of_totalPages', [page, paginator.totalPages]) }}</p>
                        <div v-if="paginator.remainingPages != 0">
                            <button type="button" class="btn btn-info btn-sm nextButton">{{ nextPage }}</button>
                        </div>
                        <div v-if="paginator.remainingPages > 1">
                            <button type="button" class="btn btn-info btn-sm ml-1 lastButton">{{ lastPage }}</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</template>

<script lang="ts">
import {Component, Prop, Vue, Watch} from 'vue-property-decorator';
import {DoctorSpecialty} from '~/logic/models/DoctorSpecialty';
import {Locality} from '~/logic/models/Locality';
import {State} from 'vuex-class';
import {localityActionTypes} from '~/store/types/localities.types';
import {doctorSpecialtyActionTypes} from '~/store/types/doctorSpecialties.types';

import {createPath} from "~/logic/Utils";
import {DoctorService} from '~/logic/interfaces/services/DoctorService';
import TYPES from '~/logic/types';

@Component
export default class MedicList extends Vue {
    @State(state => state.localities.localities)
    private readonly localities: Locality[];
    @State(state => state.doctorSpecialties.doctorSpecialties)
    private readonly specialties: DoctorSpecialty[];
    private prevPage = '<';
    private firstPage = '<<';
    private nextPage = '>';
    private lastPage = '>>';
    /**
     * possible values = "NoResultsFound","SearchResults1","SearchResults2More"
     */
    private resultsMessage = 'NoResultsFound';
    private resultsMessageParam = null;
    private paginator = {
        remainingPages: 0,
        totalPages: 0,
    };
    private doctor = [];

    get page(): number {
        return this.$route.params.page;
    }

    get searchedSpecialties(): DoctorSpecialty[] {
        let aux = this.$route.query.specialties;
        let specialties: DoctorSpecialty[];

        if (typeof aux !== 'string') {
            specialties = [];
        } else {
            specialties = aux.split(',').map(v => {
                let filtered = this.specialties.filter(value => parseInt(v) === value.id);
                return filtered.length > 0 ? filtered[0] : null;
            }).filter(value => value != null);
        }

        return specialties;
    }

    get searchedLocalities() {
        let aux = this.$route.query.localities;
        let localities: Locality[];

        if (typeof aux !== 'string') {
            localities = [];
        } else {
            localities = aux.split(',').map(v => {
                let filtered = this.localities.filter(value => parseInt(v) === value.id);
                return filtered.length > 0 ? filtered[0] : null;
            }).filter(value => value != null);
        }

        return localities;
    }

    get name(): string {
        return this.$route.query.name || "";
    }

    getSpecialtyName(id: number): string {
        for (let specialty of this.specialties) {
            if (specialty.id === id) return specialty.name;
        }

        return id.toString();
    }

    getLocalityName(id: number): string {
        for (let locality of this.localities) {
            if (locality.id === id) return locality.name;
        }

        return id.toString();
    }

    getUrl(url:string):string{
        return createPath(url);
    }

    async search() {
        this.doctor = await this.getDoctorService().list({
            page: this.page,
            name: this.name,
            localities: this.searchedLocalities,
            specialties: this.searchedSpecialties
        });
    }

    // TODO: handle error
    mounted() {
        this.$store.dispatch('localities/loadLocalities', localityActionTypes.loadLocalities());
        this.$store.dispatch('doctorSpecialties/loadDoctorSpecialties', doctorSpecialtyActionTypes.loadDoctorSpecialties());
    }

    @Watch('staff')
    private updateResultsMessage() {
        //TODO: this is not the way
        if (this.doctor.length >= 2) {
            this.resultsMessage = 'SearchResults2More';
            this.resultsMessageParam = [this.doctor.length];
        } else if (this.doctor.length == 1) {
            this.resultsMessage = 'SearchResults1';
        } else {
            this.resultsMessage = 'NoResultsFound';
        }
    }

    private getDoctorService(): DoctorService {
        return this.$container.get(TYPES.Services.DoctorService);
    }
}
</script>

<style scoped>
html, body {
    height: 100vh;
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