import request from '@/utils/request'
import qs from 'qs';

export default {
  getTemplateList() { 
    return request({
      url:'/template/getTemplateList',
      method:'get',
    })
  },
  sendDocument(templateTitle, tempalteBody) {
    return request({
      url: '/template',
      method: 'post',
      data: qs.stringify({'templateTitle': templateTitle, 'templateBody': templateBody})
    })
  },
  updateDocument(templateId,templateTitle, templateBody) {
    return request({
      url: '/template/update',
      method: 'post',
      data: qs.stringify({'templateId': templateId,'templateTitle': templateTitle, 'templateBody': templateBody})
    })
  },
  getTemplate(tempalteId) {
    return request({
      url: '/template/'+templateId,
      method: 'get'
    })
  },
  deletTemplate(templateId) {    //放入回收站
    return request({
      url: '/template/delete/' + templateId,
      method: 'delete',
    })
  },
}