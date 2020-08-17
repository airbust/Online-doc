import request from '@/utils/request'

export default {
  getTemplateList() { 
    return request({
      url:'/template/getAll',
      method:'get',
    })
  },
  getTemplate(templateId) {
    return request({
      url: '/template/'+templateId,
      method: 'get'
    })
  },
}