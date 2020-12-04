import Vue from 'vue';
import Vuex from 'vuex';
import countries from '../store/countries';
import provinces from '../store/provinces';
import users from '../store/users';

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        countries,
        provinces,
        users
    }
});
