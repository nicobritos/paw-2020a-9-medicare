import Vue from 'vue';
import VueRouter from 'vue-router';
import MedicList from '@/views/medicList';
import Landing from '@/views/landing';
import Unverified from '@/views/unverified';
import Login from '@/views/authentication/login';
import Signup from '@/views/authentication/signup';
import SignupPatient from '@/views/authentication/signupPatient';
import SignupDoctor from '@/views/authentication/signupDoctor';
import MedicHome from '@/views/medic/home';
import MedicProfile from '@/views/medic/profile';
import PatientProfile from '@/views/patient/profile';
import PatientHome from '@/views/patient/home';
import SelectAppointment from '@/views/selectAppointment';
import RequestAppointment from '@/views/patient/requestAppointment';


Vue.use(VueRouter);
const routes = [
    // example
    // TODO: CHECK best behavior for component
    // {
    //   path: '/about',
    //   name: 'About',
    //   // route level code-splitting
    //   // this generates a separate chunk (about.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    // }
    {
        path: '/mediclist/:page',
        name: 'MedicList',
        component: MedicList
    }, {
        path: '/mediclist',
        redirect: '/mediclist/1'
    },
    {
        path: '/unverified',
        name: 'Unverified',
        component: Unverified
    },
    {
        path: '/',
        name: 'Landing',
        component: Landing
    }, {
        path: '/login',
        name: 'Login',
        component: Login
    }, {
        path: '/signup',
        name: 'Signup',
        component: Signup
    },
    {
        path: '/signup/doctor',
        name: 'SignupDoctor',
        component: SignupDoctor
    },
    {
        path: '/signup/patient',
        name: 'SignupPatient',
        component: SignupPatient
    },
    {
        path: '/staff/home',
        name: 'MedicHome',
        component: MedicHome
    },
    {
        path: '/staff/profile',
        name: 'MedicProfile',
        component: MedicProfile
    },
    {
        path: '/patient/profile',
        name: 'PatientProfile',
        component: PatientProfile
    },
    {
        path: '/patient/home',
        name: 'PatientHome',
        component: PatientHome
    },
    {
        path: '/selectAppointment',
        name: 'SelectAppointment',
        component: SelectAppointment
    },
    {
        path: '/requestAppointment',
        name: 'RequestAppointment',
        component: RequestAppointment
    }
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router;
