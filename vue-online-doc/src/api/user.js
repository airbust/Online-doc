import request from '@/utils/request'
import qs from 'qs';

export default {
  hello() {
    return request({
      url: '/user/hello',
      method: 'get'
    })
  },
  login(name, pwd) {
    return request({
      url: '/user/login',
      method: 'post',
      data: qs.stringify({'name': name, 'password': pwd })
    })
  },
  register(name,pwd,mail,code) {
    return request({
      url: '/user/register',
      method: 'post',
      data: qs.stringify({'name': name, 'password': pwd,'mail':mail, 'code':code })
    })
  },
  logout(){
    return request({
      url: '/user/logout',
      method: 'get',
    })
  },
  sendmail(mail){
    return request({
      url: '/user/sendmail',
      method: 'post',
      data: qs.stringify({'mail': mail})
    })
  },
  getFileData(keyword) {
    return request({
      url:'/user/getFileData',
      method:'get',
      data: qs.stringify({'key':keyword})
    })
  },
  getUserInfo(){
    return request({
      url:'/user/getUserInfo',
      method:'get'
    })
  },
  getOtherInfo(userName){
    return request({
      url:'/user/getOtherInfo/'+userName,
      method:'get'
    })
  },
  editUser(gen,bir,job,summary){
    return request({
      url:'/user/edituser',
      method:'post',
      data: qs.stringify({
        'gender': gen,'birth':bir,
        'job':job,'summary':summary
      })
    })
  },
  updatePassword(oldPassword, newPassword) {
    return request({
      url: '/user/updatePassword',
      method: 'post',
      data: qs.stringify({'oldPassword': oldPassword, 'newPassword': newPassword})
    })
  },
  updateMail(newMail, newMailCode) {
    return request({
      url: '/user/updateMail',
      method: 'post',
      data: qs.stringify({'newMail': newMail, 'newMailCode': newMailCode})
    })
  },
}
