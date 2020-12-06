import Vue from 'vue';
import Vuex from 'vuex';
import users from '../store/users';
import localities from '../store/localities';

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        localities,
        users
    }
});

export default store;
