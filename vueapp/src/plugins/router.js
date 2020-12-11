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
import Error403 from "@/views/error/403";
import Error404 from "@/views/error/404";
import Error500 from "@/views/error/500";

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
        path: '/verify/:token',
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
        component: Login,
        meta:{
            hideNav:true
        }
    }, {
        path: '/signup',
        name: 'Signup',
        component: Signup,
        meta:{
            hideNav:true
        }
    },
    {
        path: '/signup/doctor',
        name: 'SignupDoctor',
        component: SignupDoctor,
        meta:{
            hideNav:true
        }
    },
    {
        path: '/signup/patient',
        name: 'SignupPatient',
        component: SignupPatient,
        meta:{
            hideNav:true
        }
    },
    {
        path: '/doctor/home',
        name: 'MedicHome',
        component: MedicHome
    },
    {
        path: '/doctor/profile',
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
    },
    {
        path:"/404",
        name:"Error404",
        component:Error404
    },
    {
        path:"/403",
        name:"Error403",
        component:Error403
    },
    {
        path:"/500",
        name:"Error500",
        component:Error500
    },
    {
        path:"*",
        name:"Error404",
        component:Error404
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
});

export default router;
