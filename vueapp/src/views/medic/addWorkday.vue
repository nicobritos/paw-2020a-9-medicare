<template>
    <b-modal    v-model="showModal" 
                :hide-footer="true"
                :hide-header="true"
                :no-fade="true"
                body-class="p-0"
                content-class="p-0"
                @show="cleanValues"
                #default="{ok,cancel}">
        <form class="addturn-form border p-5 rounded" @submit="submitForm">
            <div class="row">
                <h6>Medicare <img :src='logo' id="logo" alt="logo"/></h6>
            </div>
            <div class="row justify-content-start">
                <h1 class="addturn-form-title">
                    {{$t("AddTurno")}}
                </h1>
            </div>


            <div class="form-group row">
                <div class="col">
                    <label>
                        {{$t("DayOfWeek")}}
                    </label>
                </div>
                <div class="col-8">
                    <b-form-group>
                        <b-form-radio v-model="dowSelected" name="some-radios" :value="0">{{$t("Monday")}}</b-form-radio>
                        <b-form-radio v-model="dowSelected" name="some-radios" :value="1">{{$t("Tuesday")}}</b-form-radio>
                        <b-form-radio v-model="dowSelected" name="some-radios" :value="2">{{$t("Wednesday")}}</b-form-radio>
                        <b-form-radio v-model="dowSelected" name="some-radios" :value="3">{{$t("Thursday")}}</b-form-radio>
                        <b-form-radio v-model="dowSelected" name="some-radios" :value="4">{{$t("Friday")}}</b-form-radio>
                        <b-form-radio v-model="dowSelected" name="some-radios" :value="5">{{$t("Saturday")}}</b-form-radio>
                        <b-form-radio v-model="dowSelected" name="some-radios" :value="6">{{$t("Sunday")}}</b-form-radio>
                    </b-form-group>
                </div>
            </div>

            <div class="form-group row">
                <div class="col">
                    <label for="startHour">
                        {{$t("StartingHour")}}
                    </label>
                </div>
                <div class="col-8">
                    <input v-model="startHour" class="form-control" type="time" name="startHour" id="startHour" />
                </div>
            </div>

            <div class="form-group row">
                <div class="col">
                    <label for="endHour">
                        {{$t("FinishingHour")}}
                    </label>
                </div>
                <div class="col-8">
                    <input v-model="endHour" class="form-control" type="time" name="endHour" id="endHour" />
                </div>
            </div>

            <div class="form-group row">
                <div class="col">
                    <label for="officeId">
                        {{$t("Office")}}
                    </label>
                </div>
                <div class="col-8">
                    <select v-model="officeSelected" class="form-control" name="officeId" id="officeId" >
                        <!-- TODO: check that this function call is okey -->
                        <option v-for="doctor in doctors" :key="doctor.office.id" :value="doctor.office.id">
                            {{doctor.office.name}}
                        </option>
                    </select>
                </div>
            </div>

            <div class="form-row justify-content-between">
                <button class="form-atras-btn btn" type="button" @click="cancel()">
                    {{$t("Back")}}
                </button>
                <button type="submit" class="btn btn-primary" @click="ok()">
                    {{$t("Add")}}
                </button>
            </div>
        </form>
    </b-modal>    
</template>
<script lang="ts">
import {Component, VModel, Vue} from 'vue-property-decorator';
import logo from "@/assets/logo.svg";
import {createPath} from "@/logic/Utils";
import {Doctor} from '~/logic/models/Doctor';
import {State} from 'vuex-class';
import TYPES from '~/logic/types';
import {WorkdayService} from '~/logic/interfaces/services/WorkdayService';
import { Day } from '~/logic/models/utils/DateRange';

@Component
export default class AddWorkday extends Vue {
    private readonly logo = logo;

    @VModel({type:Boolean,default:true})
    private showModal:boolean;

    private dowSelected = 0;
    private startHour = "";
    private endHour = "";
    //this is selected on mount and on clean values
    private officeSelected = 0;

    @State(state => state.auth.doctors)
    private readonly doctors: Doctor[];

    public getUrl(url:string):string{
        return createPath(url);
    }

    mounted(){
        if(this.doctors&&this.doctors[0]&&this.doctors[0].office&&this.doctors[0].office.id){
            this.officeSelected = this.doctors[0].office.id
        }
    }

    private cleanValues(){
        this.dowSelected = 0;
        this.startHour = "";
        this.endHour = "";
        if(this.doctors&&this.doctors[0]&&this.doctors[0].office&&this.doctors[0].office.id){
            this.officeSelected = this.doctors[0].office.id;
        }
    }

    submitForm(e:Event){
        e.preventDefault();
        e.stopPropagation();

        /*
            TODO: check this, supponsedly input type=time always returns 
                    hh:mm:ss with ss being optional but always in 24-hour format
        */
        let start = this.startHour.split(":").map(v => parseInt(v));
        let end = this.endHour.split(":").map(v => parseInt(v));
        //TODO:check
        //TODO:FIXME tira 500 por algun motivo
        let day = [ 'MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY'][this.dowSelected];
        this.getWorkdayService().createList([{
            //TODO:check
            day: day as Day,
            end: {
                hour: end[0],
                minute: end[1]
            },
            start: {
                hour: start[0],
                minute: start[1]
            }
        }]);
    }

    private getWorkdayService(): WorkdayService {
        return this.$container.get(TYPES.Services.WorkdayService);
    }
}
</script>

<style scoped>
body, html {
    height: 100%;
    background-color: rgba(0, 196, 186, 0.205);
}

.addturn-form {
    background-color: #fff;
    border-radius: 1em !important;
    box-shadow: 10px 9px 12px 0px rgba(0, 196, 186, 0.205);
}


.form-link:hover {
    text-decoration: none;
}

.addturn-form input, .addturn-form select {
    background-color: #f0f0f0;
}

.addturn-form input:focus, .addturn-form select:focus {
    background-color: #e0e0e0;
}

.addturn-form-title {
    margin-bottom: 1em;
}

.addturn-form #logo {
    width: 1em;
}

.addturn-form button {
    background-color: #00C4BA;
    color: white;
}

.addturn-form button:hover {
    background-color: rgb(1, 150, 142);
    color: #fafafa;
}

.form-atras-btn {
    background-color: grey !important;
    color: white;
}

.form-atras-btn:hover {
    background-color: rgb(94, 94, 94) !important;
    color: white;
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