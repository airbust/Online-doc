import request from '@/utils/request'
import qs from 'qs';

export default{
    //about user
    login(name, password){
        return request({
          url: '/user/login',
          method: 'post',
          data: qs.stringify({'name': name, 'password': password})
        })
    },
    register(name, password, mail, mailCode) {
        return request({
          url: '/user/register',
          method: 'post',
          data: qs.stringify({
            'name': name, 'password': password,
            'mail': mail, 'mailCode': mailCode
          })
        })
    },
    sendMail(mail) {
        return request({
          url: '/user/sendMail',
          method: 'post',
          data: qs.stringify({'mail': mail})
        })
    },
    editUser(gen,bir,qq,job,summary){
        return request({
          url:'/user/edituser',
          method:'post',
          data: qs.stringify({
            'gender': gen,'birth':bir,
            'qq':qq,'job':job,'summary':summary
          })
        })
    },
    getUserInfo(){
        return request({
          url:'user/getUser',
          method:'get'
        })
    },
    forgetPassword(userName, mailCode, newPassword) {
        return request({
          url: '/user/forgetPassword',
          method: 'post',
          data: qs.stringify({'userName': userName, 'mailCode': mailCode, 'newPassword': newPassword})
        })
    },
    getUserMail() {
        return request({
          url: '/user/mail',
          method: 'get',
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
    logout(){
        return request({
          url: '/user/logout',
          method: 'get',
        })
    },

    //about document
    sendDocument(docTitle, docBody) {  
        return request({
          url: '/doc',
          method: 'post',
          data: qs.stringify({'docTitle': docTitle, 'docBody': docBody})
        })
    },
    editDocument(docId, docTitle, docBody) {  
        return request({
          url: '/doc/' + docId,
          method: 'put',
          data: qs.stringify({'docId':docId,'docTitle': docTitle, 'docBody': docBody})
        })
    },
    deleteDocument(docId) {    //放入回收站  
        return request({
          url: '/doc/delete/' + docId,
          method: 'delete',
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
    updateEditingT(docId){  //更改文档状态
        return request({
            url: '/doc/' + docId + '/updateEditingT',
            method: 'put',
        })
    },
    updateEditingF(docId){
        return request({
            url: '/doc/' + docId + '/updateEditingF',
            method: 'put',
        })
    },
    getDeletedDocment(page, showCount) {
        return request({
          url: '/doc/deletedDoc/' + page + '/' + showCount,
          method: 'get'
        })
    },
    recoverDeletedDocumentById(docId){
        return request({
            url: '/doc/deletedDoc/' + docId,
            method: 'get'
        })
    },
    getRecentVeiwingDocument(page, showCount){
        return request({
            url: '/doc/recentVeiwingDoc' + page + '/' + showCount,
            method: 'get'
        })
    },
    getMyDocument(page, showCount){
        return request({
            url: '/doc/MyDoc/' + page + '/' + showCount,
            method: 'get'
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
    getMyCollectingDocument(page, showCount){
        return request({
            url: '/doc/MyCollectingDoc/' + page + '/' + showCount,
            method: 'get'
        })
    },
    getMyTeamDocument(page, showCount){
        return request({
            url: '/doc/MyTeamDoc/' + page + '/' + showCount,
            method: 'get'
        })
    },
    sendDiscuss(docId, discussBody) {  
        return request({
          url: '/discuss/' + docId,
          method: 'post',
          data: qs.stringify({'discussBody': discussBody})
        })
    },
    userDeleteDiscuss(discussId) { 
        return request({
          url: '/discuss/' + discussId,
          method: 'delete'
        })
    },
    getDiscussByDocId(docId, page, showCount) {
        return request({
          url: '/discuss/' + docId + '/' + page + '/' + showCount,
          method: 'get'
        })
    },

    //about team
    joinTeam(teamId){
        return request({
            url: '/team/join/' + teamId,
            method: 'post'
        })
    },
    quitTeam(teamId){
        return request({
            url: '/team/quit' + teamId,
            method: 'delete'
        })
    },
    getMyTeamInfo(){ 
        return request({
            url: '/team/myTeamInfo/' + page + '/' + showCount,
            method: 'get'
        })
    },

    //about team admin
    setupTeam(teamName){
        return request({
            url: '/team/setup',
            method: 'post',
            data: qs.stringify({
              'teamName': teamName
            })
        })
    },
    dismissTeam(teamId){
        return request({
            url: '/team/dismiss',
            method: 'delete',
        })
    },
    addTeamMemder(userId,teamId){
        return request({
            url: '/team/' + teamId + '/' + userId,
            method: 'post',
        })
    },
    removeTeamMemder(userId,teamId){
        return request({
            url: '/team/' + teamId + '/' + userId,
            method: 'delete',
        })
    }
}