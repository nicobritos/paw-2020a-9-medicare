import Vue from 'vue';
import Vuex from 'vuex';
import users from '~/store/users';
import localities from '~/store/localities';
import doctorSpecialties from '~/store/doctorSpecialties';
import auth from '~/store/auth';
import doctors from '~/store/doctors';

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        auth,
        localities,
        users,
        doctorSpecialties,
        doctors
    }
});

export default store;
