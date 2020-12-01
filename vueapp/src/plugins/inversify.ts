import container from './inversify.config';
import Vue from 'vue';
import {PluginObject} from 'vue/types/plugin';
import {Container} from 'inversify';
import {Vue as _Vue} from 'vue/types/vue';

export const inversifyPlugin: PluginObject<Container> = {
    install(Vue: typeof _Vue) {
        Vue.prototype.$container = container;
    }
};

Vue.use(inversifyPlugin);
