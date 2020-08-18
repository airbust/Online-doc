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
import Desktop from '../views/Desktop'
import TemplateLibrary from '../views/TemplateLibrary'
import Template from '../views/Template'
import Profile from '../views/Profile'
import layout from '@/layout/layout'
Vue.use(Router)

export default new Router({
  routes: [
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
      path: '/',
      name: 'WorkStation',
      component: layout,
      children: [{
        path: '',
        component: WorkStation,
      }]
    },
    {
      path:'/TeamSpace',
      name:'TeamSpace',
      component: layout,
      children: [{
        path: '',
        component: TeamSpace,
      }]
    },
    {
      path: '/Edit',
      name: 'Edit',
      component: layout,
      children: [{
        path: '',
        component: Edit,
      }]
    },
    {  //区别个人文档与团队文档，下策
      path: '/EditTeam',
      name: 'EditTeam',
      component: layout,
      children: [{
        path: '',
        component: EditTeam,
      }]
    },
    {
      path: '/Desktop',
      name: 'Desktop',
      component: layout,
      children: [{
        path: '',
        component: Desktop,
      }]
    },
    {
      path: '/Recycle',
      name: 'Recycle',
      component: layout,
      children: [{
        path: '',
        component: Recycle,
      }]
    },
    {
      path: '/File/:fileId',
      name: 'File',
      component: layout,
      children: [{
        path: '',
        component: File,
      }]
    },
    {
      path: '/Template/:templateId',
      name: 'Template',
      component: layout,
      children: [{
        path: '',
        component: Template,
      }]
    },
    {
      path: '/TemplateLibrary',
      name: 'TemplateLibrary',
      component: layout,
      children: [{
        path: '',
        component: TemplateLibrary,
      }]
    },
    {
      path: '/Profile/:userName',
      name: 'Profile',
      component: layout,
      children: [{
        path: '',
        component: Profile,
      }]
    }
  ]
})
