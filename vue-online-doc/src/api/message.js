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
  },
  readComment(discussId){
    return request({
      url: '/discuss/read/' + discussId,
      method: 'post'
    })
  },
  getAllDiscuss(){
    return request({
      url: '/discuss/getAllDiscuss',
      method: 'get'
    })
  },
  getNotice(){
    return request({
      url: '/notice',
      method: 'get'
    })
  },
  readNotice(noticeId){
    return request({
      url: '/notice/read/' + noticeId,
      method: 'post'
    })
  },
  permit(noticeId){
    return request({
      url: '/notice/permit/' + noticeId,
      method: 'post'
    })
  }
}
