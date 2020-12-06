import Vue from 'vue'
import App from './App.vue'
import router from './plugins/router'
import i18n from './plugins/i18n'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import store from '~/plugins/vuex';

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);

Vue.config.productionTip = false;

new Vue({
  router,
  i18n,
  store,
  render: h => h(App)
}).$mount('#app');
