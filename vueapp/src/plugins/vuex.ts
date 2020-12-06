import Vue from 'vue';
import Vuex from 'vuex';
import users from '../store/users';
import localities from '../store/localities';
import doctorSpecialties from '~/store/doctorSpecialties';

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        localities,
        users,
        doctorSpecialties
    }
});

export default store;
