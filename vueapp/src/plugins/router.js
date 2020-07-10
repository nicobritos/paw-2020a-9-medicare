import Vue from 'vue'
import VueRouter from 'vue-router'
import MedicList from '@/views/medicList';
import Landing from '@/views/landing';
import Unverified from '@/views/unverified';

Vue.use(VueRouter)
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
    path:'/mediclist/:page',
    name:'MedicList',
    component: MedicList
  },
  {
    path:"/unverified",
    name:"Unverified",
    component:Unverified
  },
  {
    path:'/',
    name:'Landing',
    component: Landing
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
