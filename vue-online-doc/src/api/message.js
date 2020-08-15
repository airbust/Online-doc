import request from '@/utils/request'
import qs from 'qs';

export default {
  getMessage(docId) {
    return request({
      url: '/discuss/getByDocId/' + docId,
      method: 'get'
    })
  },
  sendMessage(docId,messageBody) {
    return request({
      url: '/discuss/send/'+ docId,
      method: 'post',
      data: qs.stringify({'discussBody': messageBody})
    })
  },
  deleteMessage(discussId) {
    return request({
      url: '/discuss/delete/' + discussId,
      method: 'delete'
    })
  }
}
