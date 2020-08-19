<template>
  <el-card class="box-card">
    <a href="javascript:void(0)"  class="nav first">金刚䂖文档</a>
    <a href="javascript:void(0)"  class="nav"></a>
    <a href="javascript:void(0)"  class="nav"></a>
    <span style="float: right;" class="name">{{name}}</span>

    <el-dropdown @command="handleCommand_info" class="avatar">
      <span class="el-dropdown-link">
        <img v-if="validAvatar()" :src="avatar" class="avatar" alt=""  slot="reference">
        <img v-else src="../../static/avatar.svg" alt=""  slot="reference" >
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="goUserInfo">个人中心</el-dropdown-item>
        <el-dropdown-item divided ><a @click="loginout">退出登陆</a></el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <!-- <el-dropdown class="avatar" style="margin-right: 20px" :trigger="hover"> -->
      <span  class="bell" @click="handleCommand_notice" >
        <img v-if="unreadCnt" style="width:33px;height:33px" src="../../static/bell2.png" alt=""  slot="reference" >
        <img v-else src="../../static/bell.png" style="width:34px;height:34px" alt=""  slot="reference" >
      </span>
    <!-- </el-dropdown> -->

    <!--实现全屏功能-->
    <el-button type="primary" class="button" icon="el-icon-full-screen" size="small" @click="screen" circle></el-button>

    <!--侧栏消息抽屉-->
     <el-drawer  :visible.sync="notice_drawer"  :show-close="true" :with-header="false" size="27%" :append-to-body="true" :modal="false"
          style="margin-top:70px;">
      <el-tabs v-model="activeName_notice" type="border-card" style="padding:5px">
        <el-tab-pane label="文档评论" name="0">
          <div style=" width: 100%;height: 90vh;">
            <el-scrollbar style="height:100%">
              <el-timeline style="margin-left: -40px">
                <el-timeline-item v-for="comment in commentList" :key="comment.discuss.discussId"  placement="top" :timestamp="dateFormat(comment.discuss.discussTime)">
                  <div style="height: 80px">
                    <div class="commentList">
                      <span class="left p1">
                        <img src="../../static/doc1.png">
                      </span>
                      <span class="right p1">
                        <div class="rightTop">
                          <span style="font-size: 16px;margin-left:17px"> {{comment.user.name}}&nbsp;&nbsp;评论了</span>
                          <span style="font-size:16px; font-weight:bold">「 {{comment.fileName}}」</span>
                        </div>
                        <div class="rightCenter" style="font-size:14px;">{{comment.discuss.discussBody}}</div>
                      </span>
                    </div>
                  </div>
                  <el-divider style="magin-top:20px"></el-divider>
                </el-timeline-item>
                <el-timeline-item v-if="commentList.length == 0" placement="top">
                  <el-card>
                    <span style="font-size: 16px">空空如也~</span>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-scrollbar>
          </div>
        </el-tab-pane>
        <el-tab-pane label="系统通知" name="2">
          <div style=" width: 100%;height: 90vh;">
            <el-scrollbar style="height:100%">
              <el-timeline style="width:100%; margin-left: -40px;">
                <el-timeline-item v-for="notice in noticeList" :key="notice.noticeId"  placement="top" :timestamp="dateFormat(notice.time)">
                  <div style="height: 80px; width:100%">
                    <div class="commentList">
                      <span class="left p1">
                        <img src="../../static/doc1.png">
                      </span>
                      <span class="right p1">
                        <div class="rightTop">
                          <span style="font-size: 16px;margin-left:17px"> {{notice.userName}}&nbsp;&nbsp;{{notice.info}}</span>
                          <span style="font-size:16px; font-weight:bold">「 {{notice.groupName}}」</span>
                        </div>
                        <div class="rightCenter" style="font-size:14px;">
                          <span style="float:left">
                            <el-button size="mini" v-if="showNoticeButton(notice.isRead,notice.info)" @click="permit(notice.noticeId)">同意</el-button>
                          </span>
                        </div>
                      </span>
                    </div>
                  </div>
                  <el-divider style="magin-top:20px"></el-divider>
                </el-timeline-item>
                <el-timeline-item v-if="noticeList.length == 0" placement="top">
                  <el-card>
                    <span style="font-size: 16px">空空如也~</span>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-scrollbar>
          </div>
        </el-tab-pane>
        <el-tab-pane :label="unreadTitle" name="1">
          <div style=" width: 100%;height: 90vh;">
            <el-scrollbar style="height:100%">
              <el-timeline style="margin-left: -40px">
                <el-timeline-item v-for="unread in unreadList" :key="unread.id" placement="top" :timestamp="dateFormat(unread.time)">
                  <div style="height: 80px">
                    <div class="commentList">
                      <span class="left p1">
                        <img src="../../static/doc1.png">
                      </span>
                      <span class="right p1">
                        <div class="rightTop">
                          <span style="font-size: 16px;margin-left:17px"> {{unread.user}}&nbsp;&nbsp;{{unread.action}}</span>
                          <span style="font-size:16px; font-weight:bold">「 {{unread.item}}」</span>
                        </div>
                        <div class="rightCenter" style="font-size:14px;">
                          <span v-if="showUnreadButton(unread.info,unread.action)">
                            <el-button style="float:left" size="mini" @click="permit(unread.uid)">同意</el-button>
                          </span>
                          <span v-else>{{unread.info}}</span>
                          <span @click="readComment(unread.uid)" class="el-icon-bell" style="float:right;font-size:20px"></span>
                        </div>
                      </span>
                    </div>
                  </div>
                  <el-divider style="magin-top:20px"></el-divider>
                </el-timeline-item>
                <el-timeline-item v-if="unreadList.length == 0" placement="top">
                  <el-card>
                    <span style="font-size: 16px">空空如也~</span>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-scrollbar>
          </div>
        </el-tab-pane>


      </el-tabs>
    </el-drawer>

    <!--侧栏个人信息抽屉-->
    <el-drawer  :visible.sync="info_drawer"  :show-close="true" :with-header="false" size="30%" :append-to-body="true"
          style="height: 100%; ">
      <el-tabs style="margin-top: 50px; height: 100%;" type="border-card" tab-position="left" v-model="activeName_info">
          <el-tab-pane label="个人中心" name="0">
            <span slot="label"><i class="el-icon-user-solid"></i> 个人中心</span>

            <el-form label-position="left" :model="userInfo" label-width="100px" ref="userInfo">
              <el-form-item label="用户头像" :label-width="labelWidth">
                <el-upload
                  class="avatar-uploader"  action="123"
                  :show-file-list="false"
                  :on-change="fileChange"
                  :http-request="SubbmitFile" >
                  <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar"
                      onerror="javascript:this.src='../../static/avatar.png'">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </el-form-item>
              <el-form-item label="性别" :label-width="labelWidth">
                <el-radio v-model="userInfo.gender" label="男" border size="medium">男</el-radio>
                <el-radio v-model="userInfo.gender" label="女" border size="medium">女</el-radio>
              </el-form-item>
              <el-form-item label="生日" :label-width="labelWidth">
                <el-date-picker
                  v-model="userInfo.birth" value-format="yyyy-MM-dd"
                  type="date" placeholder="选择日期">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="邮箱" :label-width="labelWidth" prop="email">
                <div style="width: 100%">{{userInfo.email}}</div>
              </el-form-item>
              <el-form-item label="职业" :label-width="labelWidth">
                <el-input v-model="userInfo.job" style="width: 100%"></el-input>
              </el-form-item>
              <el-form-item label="简介" :label-width="labelWidth">
                <el-input
                  type="textarea"
                  :autosize="{ minRows: 5, maxRows: 10}"
                  placeholder="请输入内容"
                  style="width: 100%"
                  v-model="userInfo.summary">
                </el-input>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="submitForm()">保 存</el-button>
              </el-form-item>

            </el-form>
          </el-tab-pane>
          <el-tab-pane label="密码邮箱" name="5">
            <span slot="label"><i class="el-icon-s-tools"></i> 修改密码</span>
            <el-collapse v-model="activeNames">
              <el-collapse-item title="更改密码" name="1">
              </el-collapse-item>
            </el-collapse>
            <el-form ref="form" label-position="left" :model="userInfo" label-width="100px" >
              <el-form-item label="旧密码" :label-width="labelWidth" prop="oldPwd">
                <el-input type="password" v-model="oldPassword" style="width: 100%"></el-input>
              </el-form-item>
              <el-form-item label="新密码" :label-width="labelWidth" prop="newPwd">
                <el-input type="password" v-model="newPassword" style="width: 100%"></el-input>
              </el-form-item>
              <el-form-item label="重复密码" :label-width="labelWidth" prop="newPwd2">
                <el-input type="password" v-model="confirmPassword" style="width: 100%"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="updatePwd()">提 交</el-button>
              </el-form-item>
            </el-form>

            <el-collapse v-model="activeNames">
              <el-collapse-item title="改绑邮箱" name="1">
              </el-collapse-item>
            </el-collapse>
            <el-form ref="form" label-position="left" :model="userInfo" label-width="100px" >
              <el-form-item label="新邮箱">
                <el-input v-model="newMail" placeholder="新邮箱"></el-input>
                <el-button type="text" style="width: 85%;" @click="updateMailSendMailToNew()">
                  发送验证码
                  <i class="el-icon-loading" v-if="updateMailToNewSendFlag"/>
                </el-button>
              </el-form-item>
              <el-form-item label="新邮箱验证码">
                <el-input v-model="newMailCode" placeholder="新邮箱验证码"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="updateMail()">改绑邮箱</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
      </el-tabs>
    </el-drawer>

  </el-card>
</template>

<script>
import user from '@/api/user'
import message from '@/api/message'
import axios from 'axios'
import screenfull from 'screenfull'//重新安装package.json中的依赖时，一定要注意其版本，发现在5.0上版本时全屏不可用，可用重新安装依赖: npm install --save screenfull@4.2.1

export default {
  name: "Header",
  data(){
    return{
      isFullscreen:false,//全屏的状态
      name:'未登录',
      avatar: '',
      total: 0, //总评论数量
      unreadCnt: 0,
      activeNames: ['1', '2'], //激活的折叠面板
      activeName_info: "0", //默认标签
      activeName_notice: "1",
      noticeList: [],//系统通知
      unread: {user:'',action: '', item: '', info: '', time: ''},
      unreadList: [],
      commentList: [],//用户评论
      userInfo: {
        name: '', gender: '',  birth: '', email: '', job:'', summary:'', avatar: ''
      },
      notice_drawer: false,
      info_drawer:false,
      file: {}, //头像图片
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
      updatePwdMailCode: '', // 修改密码验证码
      mail: '', //用户原邮箱
      newMail: '',    //新邮箱
      newMailCode: '',  //新邮箱验证码
      updatePwdSendFlag: false,
      updateMailToOldSendFlag: false,
      updateMailToNewSendFlag: false,
      //侧栏基础元素
      activeNames: ['1', '2'], //激活的折叠面板
      activeName: "0", // 激活的标签
      labelWidth: "85px",
    }
  },
  computed:{
    unreadTitle() {return '未读('+this.unreadCnt+')'}
  },
  components:{ },
  created(){
    this.name=localStorage.getItem('name')
    this.getUserInfo()
    this.getMessage()
  },
  methods:{
    
    screen(){//设置全屏操作
      // 如果不允许进入全屏，发出不允许提示
      if (!screenfull.enabled) {
        this.$message('无法全屏');
        return false;
      }
      screenfull.toggle();
    },
    permit(id){
      console.log('同意')
      message.permit(id).then(res=>{
        console.log(res.message)
        this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
        this.getMessage()
      })
    },
    getMessage(){
      console.info('getMessage')
      this.unreadCnt = 0
      this.unreadList = []
      message.getAllDiscuss().then(res=>{
        // console.log(res.data)
        this.commentList = res.data
        this.total = res.data.length
        for(var i=0;i<this.total;i++){
          if(this.commentList[i].discuss.isRead==0 ){
            let u = {
              uid: -res.data[i].discuss.discussId,
              user:res.data[i].user.name,
              action: '评论了',
              item: res.data[i].fileName,
              info: res.data[i].discuss.discussBody,
              time: res.data[i].discuss.discussTime
            }
            this.unreadList.push(u);
            this.unreadCnt ++;
          }
        }
      })
      message.getNotice().then(res=>{
        // console.log(res.data)
        this.noticeList = res.data
        this.total = res.data.length
        for(var i=0;i<this.total;i++){
          if(this.noticeList[i].userName == this.$store.state.name )
            this.noticeList[i].userName = res.data[i].groupAdmin
          if(this.noticeList[i].isRead==0 ){
            let u = {
              uid: res.data[i].noticeId,
              user:res.data[i].groupAdmin,
              action: res.data[i].info,
              item: res.data[i].groupName,
              time: res.data[i].time,
              info: '',
            }
            if(u.user == this.$store.state.name)
                u.user = res.data[i].userName
            this.unreadList.push(u)
            this.unreadCnt ++
          }
        }
      })
      // console.log(this.unreadList)
    },
    dateFormat(time) {
      var date=new Date(time);
      var year=date.getFullYear();
      var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
      var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
      var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
      var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
      var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
      return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
    },
    readComment(id){
      console.log('read'+id)
      if(id>0)
        message.readNotice(id).then(res=>{ 
          console.log(res.message)
          this.getMessage()
         })
      else
        message.readComment(-id).then(res=>{ 
          console.log(res.message) 
          this.getMessage()
        })
    },
    validAvatar(){
      if(this.avatar == null || this.avatar == undefined) return false
      return this.avatar.length>0?true:false
    },
    showUnreadButton(discuss, action){
      if(discuss!='') return false
      if(action.substring(0,1)=='已') return false
      return true
    },
    showNoticeButton(isRead,info){
      if(isRead==1) return false;
      if(info.substring(0,1)=='已') return false
      return true
    },
    loginout(){
      user.logout().then(res=>{
        this.avatar = ''
        this.$store.commit('logout')//清除token等信息
        this.$router.push({path: '/Login'})
      })
    },
    handleCommand_notice(){
      this.notice_drawer = true
    },
    handleCommand_info(command) {// 点击头像触发的动作
      switch (command) {
      case "logout" : {
        this.logout();
      };break;
      case "goUserInfo" : {
        this.info_drawer = true;
      };break;
      }
    },
    getUserInfo(){ //用户信息
      if(this.$store.state.token){
        user.getUserInfo().then(response=>{
          var a = response.data
          this.userInfo.name = a.name
          this.userInfo.gender = a.gender
          this.userInfo.birth = a.birth
          this.userInfo.email = a.mail
          this.userInfo.job = a.job
          this.userInfo.summary = a.info
          this.userInfo.avatar = a.avatar
          this.avatar = a.avatar
        })
      }
    },
    submitForm() { //修改用户信息
      var a = this.userInfo
      user.editUser(a.gender,a.birth,a.job,a.summary).
      then(response => {
        if(response.code == 200) {
          this.$message({
            type: "success",
            message: '更新成功'
          })
        } else {
          this.$message({
            type: "error",
            message: '更新失败'
          })
        }
      });
    },
    updatePwd() {//修改密码
      if (this.oldPassword.length <= 0) {
        this.$notify({title: '提示',type: 'warning',message: '原密码不能为空',duration: 2000 });   return;
      }
      if (this.newPassword != this.confirmPassword) {
        this.$notify({title: '提示',type: 'warning',message: '两次输入密码不一致',duration: 2000 });   return;
      }
      user.updatePassword(this.oldPassword, this.newPassword).then(res => {
        this.$notify({title: '提示',type: 'success',message: '修改成功',duration: 2500 });
        this.drawer=false
        this.$store.commit('logout')//清除token等信息
        this.$router.push({path: '/Login'})
      })
    },
    updateMailSendMailToNew() {  //更改密码发送验证码到新邮箱
        this.updateMailToNewSendFlag = true
        user.sendmail(this.newMail).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 2000 })
          this.updateMailToNewSendFlag = false;
        }).catch(error=> {console.log(error), this.updateMailToNewSendFlag = false;})
      },
    updateMail() {//修改邮箱
      var reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);
      if (!reg.test(this.newMail)) {//检测字符串是否符合正则表达式
        this.$notify({title: '提示',type: 'warning',message: '邮箱格式不正确',duration: 2000 });   return;
      }
      if (this.newMailCode.length <= 0) {//
        this.$notify({title: '提示',type: 'warning',message: '请填写验证码',duration: 2000 });     return;
      }
      user.updateMail(this.newMail, this.newMailCode).then(res => {
        this.$notify({title: '提示',type: 'success',message: '改绑成功',duration: 2500 });
        this.drawer=false
        this.newMail = '';
        this.newMailCode = '';
        this.getUserInfo()
      })
    },
    fileChange(e,list){//上传头像.子函数
        this.file=e;
    },
    SubbmitFile(){//上传头像
        let param = new FormData();
        param.append("file", this.file.raw);
        let config = {
          headers: { 'Content-Type': 'multipart/form-data' },
        }
        config.headers['Authorization'] = this.$store.state.token
        console.log(param)
        axios.post("/api/user/uploadAvatar/", param, config,{timeout:900000})
        .then(response => {
          if (response) {
            this.$router.go(0)
            this.file={}
            // console.log(response.data);
          } else {
            alert(response.data.msg);
          }
        })
        .catch(function (err) {
          console.log(err);
        });
      },
  }
}
</script>

<style scoped>
  .box-card{
    height: 60px;
    width: 100%;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border: 0 !important;
  }
  .nav{
  margin-top: -20px;
  margin-left: 40px;
  font-family: PingFangSC-Semibold;
  font-size: 20px;
  font-weight: bold;
  color: black;
  text-decoration: none;
  }
  a:hover{
    color:#0098ea;
  }
  .first{
    color:#0098ea;
  }
  .avatar{
    float: right;
    width: 30px;
    height:30px;
  }
  .bell{
    float: right;
    width: 30px;
    height:30px;
    margin-right: 20px;
  }
  button{
    float: right;
    margin-right: 21px;
    margin-top: -7px;
  }
  img{
    width: 30px;
    margin-top: -7px;
  }
  .name{
    margin-left: 10px;
    font-size: 14px;
    font-weight: 500;
    color: #424e67;
  }

  .commentList {
    width: 100%;
    margin: 0 auto;
  }
  .commentList .p1 {
    float: left;
  }
  .commentList .left {
    display: inline-block;
    width: 10%;
    height: 100%;
  }
  .commentList .left img {
    margin-top: 10px;
    width: 100%;
  }
  .commentList .right {
    display: inline-block;
    width: 85%;
    margin-left: 15px;
  }
  .commentList .rightTop {
    height: 30px;
    margin-top: 3px;
    margin-left: 3px;
  }
  .commentList .rightCenter {
    margin-left: 20px;
    margin-top: 10px;
    line-height: 30px;
    height: 37px;
  }

  .avatar{
    width:30px;
    height:30px;
    border-radius: 50%;
  }

  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
</style>
