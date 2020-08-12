<template>
  <el-card class="box-card">
    <a href="javascript:void(0)"  class="nav first">金刚石文档</a>
    <a href="javascript:void(0)"  class="nav"></a>
    <a href="javascript:void(0)"  class="nav"></a>
    <span style="float: right;" class="name">{{name}}</span>
      <el-dropdown @command="handleCommand" class="avatar">
  <span class="el-dropdown-link">
     <img src="../assets/avatar.svg" alt=""  slot="reference" >

  </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="goUserInfo">个人中心</el-dropdown-item>
        <el-dropdown-item>设置</el-dropdown-item>
        <el-dropdown-item divided ><a @click="loginout">退出登陆</a></el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <!--侧栏抽屉-->
        <el-drawer  :visible.sync="drawer"  :show-close="true" :with-header="false" size="30%" :append-to-body="true"
              style="height: 100%; ">
          <el-tabs style="margin-top: 50px; height: 100%;" type="border-card" tab-position="left" v-model="activeName" @tab-click="handleClick">
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
                          onerror="javascript:this.src='../../static/images/defaultAvatar.png'">
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
                  <el-form-item label="QQ号" :label-width="labelWidth" prop="qqNumber">
                    <el-input v-model="userInfo.qq" style="width: 100%"></el-input>
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
    export default {
        name: "Header",
      data(){
        return{
        name:'未登录',
        userInfo: {
          name: '',
          gender: '',
          birth: '',
          email: '',
          qq: '',
          job:'',
          summary:'',
          avatar: ''
        },
        drawer:false
        }
      },
      components:{

      },
      created(){
    this.name=localStorage.getItem('name')

      },
      methods:{
        loginout(){
          localStorage.setItem('uid','')
          this.$router.push({path: '/'})
        },
        handleCommand(command) {// 点击头像触发的动作
          switch (command) {
          case "logout" : {
            this.logout();
          };break;
          case "goUserInfo" : {
            // 打开抽屉
            this.drawer = true;
          };break;
          }
        },
        getUserInfo(){ //用户信息 
          if(this.$store.state.token){
            user.getUserInfo().then(response=>{
              // console.info(response.data)
              var a = response.data
              this.userInfo.name = a.name
              this.userInfo.gender = a.gender
              this.userInfo.birth = a.birth
              this.userInfo.email = a.mail
              this.userInfo.qq = a.qq
              this.userInfo.job = a.job
              this.userInfo.summary = a.summary
              this.userInfo.avatar = a.avatar
            })
          }
        },
        submitForm() { //修改用户信息
          var a = this.userInfo
          user.editUser(a.gender,a.birth,a.qq,a.job,a.summary).
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
        }
      }
    }
</script>

<style scoped>
  .box-card{
height: 60px;
width: 100%;
    box-shadow: 0 0 !important;
    border: 0 !important;

  }
.nav{
  margin-top: -20px;
  margin-left: 40px;
  font-family: PingFangSC-Semibold;
  font-size: 16px;
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
  img{
    width: 30px;
    margin-top: -5px;
  }
  .name{
    margin-left: 10px;
    font-size: 14px;
    font-weight: 500;
    color: #424e67;
  }


</style>
