<template>
    <div class="container w-100 justify-content-end">
        <router-link :to='getUrl("signup/doctor")' class="header-a-element nav-link mx-3">{{ $t('AreYouMedic') }}
        </router-link>
        <router-link :to='getUrl("signup")' class="header-a-element nav-link mx-3">{{ $t('SignUp') }}</router-link>
        <router-link :to='getUrl("login")' class="header-btn-element btn btn-light rounded mx-3">{{ $t('Login') }}
        </router-link>
    </div>
</template>

<script lang="ts">
import {Component, Vue} from 'vue-property-decorator';

@Component
export default class NavbarNotLogged extends Vue {
}
</script>