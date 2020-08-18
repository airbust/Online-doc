<template >
  <el-card  style="margin:40px 40px 40px 40px" :body-style="{padding:'70px 20px 20px 20px'}">
    <el-container style="height:80%;padding:50px">
      <el-aside width:200px class="border">
        <div>
          <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar">
          <img v-else src="../../static/avatar.png" class="avatar">
        </div>
        <div style="text-align:center; font-size:17px;font-weight:bold">
          {{userInfo.name}}
        </div>
      </el-aside>
      <el-main style="margin-left:40px">
        <div class="row">
          <span class="span"><i style="margin-right:16px;font-size:20px" class="el-icon-user-solid"></i>用户名</span>
          <span>{{userInfo.name}}</span>
        </div>
        <div class="row">
          <span class="span"><i style="margin-right:16px;font-size:20px" class="el-icon-info"></i>性别</span>
          <span>{{userInfo.gender}}</span>
        </div>
        <div class="row">
          <span class="span"><i style="margin-right:16px;font-size:20px" class="el-icon-s-opportunity"></i>生日</span>
          <span>{{userInfo.birth}}</span>
        </div>
        <div class="row">
          <span class="span"><i style="margin-right:16px;font-size:20px" class="el-icon-s-comment"></i>邮箱</span>
          <span>{{userInfo.mail}}</span>
        </div>
        <div class="row">
          <span class="span"><i style="margin-right:16px;font-size:20px" class="el-icon-s-finance"></i>职业</span>
          <span>{{userInfo.job}}</span>
        </div>
        <div class="row">
          <span class="span"><i style="margin-right:16px;font-size:20px" class="el-icon-s-data"></i>简介</span>
          <span>{{userInfo.info}}</span>
        </div>
      </el-main>
    </el-container>
  </el-card>
</template>

<script>
import user from '@/api/user'
export default {
    name: "Profile",
    components: {},
    data() {
        return {
          labelWidth: "85",
          name:'未登录',
          avatar: '',
          userInfo: {},
        }
    },
    created() {
        this.name = this.$route.params.userName
        console.log(this.name)
        this.getUserInfo(this.name)
    },
    methods: {
        validAvatar(){
            if(this.avatar == null || this.avatar == undefined) return false
            return this.avatar.length>0?true:false
        },
        getUserInfo(name){ //用户信息 
            user.getOtherInfo(name).then(res=>{
              var a = res.data
              var info = {name:a.name, gender: a.gender, birth: a.birth, job: a.job, avatar: a.avatar, info: a.info, mail: a.mail}
              if(a.gender==null) info.gender='男'
              if(a.birth==null) info.birth='未设置'
              if(a.job==null) info.job='无'
              if(a.info==null) info.info='还没有简介'
              if(a.avatar==null) info.avatar=''
              this.userInfo = info
              console.log(this.userInfo)
            })
        }
    }
}
</script>

<style scoped>
  .row{
    height: 45px;

  }
  .span{
    display:inline-block;
    width:150px
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
  .bell{
    float: right;
    width: 30px;
    height:30px;
    margin-right: 20px;
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

  .border{
    width: 100px;
    border-right: 2px solid rgb(218, 218, 218)
  }

  .avatar{
    width:90px;
    height:90px;
    margin-left: 100px;
    margin-top: 60px;
    margin-bottom:30px;
    border-radius: 50%;
  }
</style>
