import Vue from 'vue'
import Router from 'vue-router'

import Edit from "../views/Edit";
import File from "../views/File";
import Login from "../views/Login";
import Register from "../views/Register";
import WorkStation from "../views/WorkStation";
import TeamSpace from "../views/TeamSpace";
import Recycle from '../views/Recycle'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'WorkStation',
      component: WorkStation,
    },
    {
      path:'/TeamSpace',
      name:'TeamSpace',
      component: TeamSpace
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
    },
    {
      path: '/Recycle',
      name: 'Recycle',
      component: Recycle
    },
    {
      path: '/File/:fileId',
      name: 'File',
      component: File
    }
  ]
})
