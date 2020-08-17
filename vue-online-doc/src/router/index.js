import Vue from 'vue'
import Router from 'vue-router'

import Edit from "../views/Edit";
import EditTeam from "../views/EditTeam";
import File from "../views/File";
import Login from "../views/Login";
import Register from "../views/Register";
import WorkStation from "../views/WorkStation";
import TeamSpace from "../views/TeamSpace";
import Recycle from '../views/Recycle'
import EditFileFromTemplate from '../views/EditFileFromTemplate'
import EditTemplate from '../views/EditTemplate'

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
    {  //区别个人文档与团队文档，下策
      path: '/EditTeam',
      name: 'EditTeam',
      component: EditTeam
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
    },
    {
      path: '/EditFileFromTemplate',
      name: 'EditFileFromTemplate',
      component: EditFileFromTemplate
    },
    {
      path: '/EditTemplate',
      name: 'EditTemplate',
      component: EditTemplate
    }
  ]
})
