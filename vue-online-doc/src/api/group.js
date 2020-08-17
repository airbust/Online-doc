import request from '@/utils/request'
import qs from 'qs';

export default {
  createGroup(groupName) {
    return request({
      url: '/team/create/' + groupName,
      method: 'post'
    })
  },
  deleteGroup(teamName){
    return request({
      url: '/team/deleteGroup',
      method: 'post',
      data: qs.stringify({'teamName':teamName})
    })
  },
  attendGroup(groupName) {
    return request({
      url: '/team/attend/' + groupName,
      method: 'post'
    })
  },
  getGroups() {
    return request({
      url: '/team/getGroups',
      method: 'get',
    })
  },
  addMem(userName,teamName){
    return request({
      url: '/team/addMem',
      method: 'post',
      data: qs.stringify({'userName': userName,'teamName':teamName})
    })
  },
  removeMem(teamName,userName){
    return request({
      url: '/team/removeMem',
      method: 'post',
      data: qs.stringify({'teamName':teamName,'userName': userName})
    })
  },
  getGroupMem(teamName){
    return request({
      url: '/team/getGroupMem/'+teamName,
      method: 'get',
    })
  }
}
