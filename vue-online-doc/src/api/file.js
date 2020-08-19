import request from '@/utils/request'
import qs from 'qs';

export default {
  getCreation() {
    return request({
      url:'/doc/getCreation',
      method:'get',
    })
  },
  getDesktop(){
    return request({
      url: '/doc/getRelativeFile',
      method: 'get'
    })
  },
  getHistory(fileId){
    return request({
      url: '/doc/getHistoryFile/'+fileId,
      method: 'get'
    })
  },
  getHistoryFile(fileId,modifyCnt){
    return request({
      url: '/doc/getHistoryFile/'+fileId+'/'+modifyCnt,
      method: 'get'
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
  getTeamFile(teamName){
    return request({
      url: '/doc/getTeamFile/' + teamName,
      method: 'get'
    })
  },
  saveTeamFile(docTitle, docBody,teamName){
    return request({
      url: '/doc/saveTeamFile',
      method: 'post',
      data: qs.stringify({'docTitle': docTitle, 'docBody': docBody,'teamName':teamName})
    })
  },
  sendDocument(docTitle, docBody) {
    return request({
      url: '/doc/saveFile',
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

  // 回收站相关
  Deleted(fileId) {  
    return request({
      url: '/doc/delete/' + fileId,
      method: 'delete',
    })
  },
  getDeleted() {
    return request({
      url: '/doc/getDelete',
      method: 'get'
    })
  },
  recoverDeleted(fileId){
    return request({
      url: '/doc/recoverDelete/' + fileId,
      method: 'post'
    })
  },
  foreverDeleted(fileId){
    return request({
      url: '/doc/foreverDelete/' + fileId,
      method: 'post'
    })
  },

  // 编辑锁相关
  isEditable(docId){ 
    return request({
      url: '/doc/' + docId + '/isEditable',
      method: 'post',
    })
  },
  isEditing(docId){   
    return request({
      url: '/doc/' + docId + '/isEditing',
      method: 'post',
    })
  },
  updateAuth(docId,role,auth){
    return request({
      url: '/doc/' + docId + '/updateAuth',
      method: 'post',
      data: qs.stringify({'role':role,'auth':auth})
      // data: {auth:auth}
    })
  },
  collectDocument(docId){
    return request({
      url: '/doc/collect/' + docId,
      method: 'post'
    })
  },
  removeCollectedDocument(docId){
    return request({
      url: '/doc/removeCollectedDoc/' + docId,
      method: 'delete'
    })
  },
  getMyCollecting(){
    return request({
      url: '/doc/getCollect',
      method: 'get'
    })
  },
}
