import request from '@/utils/request'
import qs from 'qs';

export default {
  getCreation() {
    return request({
      url:'/doc/getCreation',
      method:'get',
    })
  },
  getStar() {
    return request({
      url:'/doc/getStar',
      method:'get',
    })
  },
  getRecent() {
    return request({
      url:'/doc/getRecent',
      method:'get',
    })
  },
  getRecycle() {
    return request({
      url:'/doc/getRecycle',
      method:'get',
    })
  },
  sendDocument(docTitle, docBody) {
    return request({
      url: '/doc',
      method: 'post',
      data: qs.stringify({'docTitle': docTitle, 'docBody': docBody})
    })
  },
  updateDocument(docId,docTitle, docBody) {
    return request({
      url: '/doc/update',
      method: 'post',
      data: qs.stringify({'docId': docId, 'docTitle': docTitle, 'docBody': docBody})
    })
  },
  getDocument(fileId) {
    return request({
      url: '/doc/'+fileId,
      method: 'get'
    })
  },
  deleteDocument(fileId) {    //放入回收站
    return request({
      url: '/doc/delete/' + fileId,
      method: 'delete',
    })
  },
  getDeletedDocment() {
    return request({
      url: '/doc/deletedDoc/',
      method: 'get'
    })
  },
  recoverDeletedDocumentById(fileId){
    return request({
      url: '/doc/deletedDoc/' + fileId,
      method: 'get'
    })
  },
  isEditable(docId){  //获取是否具有编辑权限
    return request({
      url: '/doc/' + docId + '/isEditable',
      method: 'get',
    })
  },
  isEditing(docId){   //获取文档当前状态
    return request({
      url: '/doc/' + docId + '/isEditing',
      method: 'get',
    })
  },
  updateAuth(docId,role,auth){
    return request({
      url: '/doc/' + docId + '/updateAuth',
      method: 'post',
      data: qs.stringify({'role':role,'auth':auth})
      // data: {auth:auth}
    })
  }
}
