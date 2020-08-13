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
  }
}