import Vue from 'vue';
import Vuex from 'vuex';
import users from '~/store/users';
import localities from '~/store/localities';
import doctorSpecialties from '~/store/doctorSpecialties';
import auth from '~/store/auth';

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        auth,
        localities,
        users,
        doctorSpecialties
    }
});

export default store;
