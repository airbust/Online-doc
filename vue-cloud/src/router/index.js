import Vue from 'vue'
import Router from 'vue-router'

import Edit from "../views/Edit";
import Login from "../views/Login";
import Register from "../views/Register";
import WorkStation from "../views/WorkStation";
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'WorkStation',
      component: WorkStation,
      // children:[
      //   {
      //     path: 'WorkStation',
      //     name: 'WorkStation',
      //     component: WorkStation
      //   }
      // ]
    },
    {
      path: '/Edit',
      name: 'Edit',
      component: Edit
    },
    {
      path: '/Login',
      name: 'Login',
      component: Login
    },
    {
      path: '/Register',
      name: 'Register',
      component: Register
    }
  ]
})
