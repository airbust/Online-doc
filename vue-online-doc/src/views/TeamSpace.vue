<template>
  <el-container >
    <!-- 左侧文档视图 -->
    <el-aside style="width:75%; margin-right:50px ; ">
      <el-tabs v-model="activeName" type="border-card" style="height:85vh" :stretch="true"  @tab-click="handleClick">
        <el-tab-pane :label="o.groupName" :name="o.groupName" v-for="(o,id) in groups" :key="id">
          <team-file :groupName="activeName"/>
        </el-tab-pane>
      </el-tabs>
    </el-aside>
    <!-- 右侧信息栏 -->
    <el-main style=" background: rgb(247, 247, 247); ">
      <el-tooltip class="item" effect="dark" content="平铺" placement="top">
        <span><i @click="layoutTile()" style="font-size: 25px; color: grey" class="el-icon-menu"></i></span>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="列表" placement="top">
        <span><i @click="layoutList()" style="font-size: 25px; color: grey" class="el-icon-s-unfold"></i></span>
      </el-tooltip>

      <div style="margin-top: 40px"><el-button @click="teamEdit()" style="width: 170px" type="info">新建</el-button></div>
      <div style="margin-top: 15px"><el-button style="width: 170px">模板库</el-button></div>
      <div style="margin-top: 15px"><el-button @click="optionVisible = true" style="width: 170px">管理团队</el-button></div>
      <div style="margin-top: 15px"><el-button @click="attendVisible = true" style="width: 170px">加入团队</el-button></div>
      
      <el-card class="card">  
        <span style="font-size:18px;"><i style="margin-right:10px;font-size:22px;color:grey" class="el-icon-s-custom"></i>团队管理者</span>
        <div style="margin-top:-10px"><el-divider></el-divider></div>
        <div style="margin-top:-5px">
          <span class="left">
            <img  v-if="validAvatar(adminAvatar)" :src="adminAvatar" class="avatar" alt=""  slot="reference">
            <img  v-else src="@/assets/avatar.png" class="avatar" alt=""  slot="reference">
          </span>
          <span class="right">
            <div class="rightTop">
              <span class="userName" >{{adminName}}</span>
            </div>
            <div class="rightCenter">{{adminMail}}</div>
          </span>
        </div>
      </el-card>

      <el-card class="card" >
        <span style="font-size:18px"><i style="margin-right:10px;font-size:22px;color:grey" class="el-icon-user-solid"></i>团队成员</span>
        <div style="margin-top:-10px"><el-divider></el-divider></div>
        
        <div style="margin-top: -15px">
        <el-row style="display: flex; flex-wrap: wrap;" type="flex" :gutter="0" class="el-row" >
          <el-col :span="6" class="el-col" v-for="(o, id) in group.member" :key="id" :offset="1" >
              <div style="text-align:center;">
                <img  v-if="validAvatar(o.avatar)" :src="o.avatar" class="avatar" alt=""  slot="reference" style="width:50px">
                <img  v-else src="@/assets/avatar.png" class="avatar" alt=""  slot="reference">
              </div>
              <div style="text-align:center;margin-bottom:5px">{{o.name}}</div>
          </el-col>
        </el-row>
        </div>

      </el-card>

    </el-main>

    <!-- 加入/创建团队 -->
    <el-dialog title="申请团队" :visible.sync="attendVisible" width="26%" :before-close="handleClose" :show-close="false">
      <div style="margin: 0px 20px 20px 20px">
        <el-input placeholder="请输入团队名" v-model="group_keyword" suffix="el-icon-search"></el-input>
        <div style="margin-top:50px">
        <span slot="footer"><el-button @click="attendGroup()" class="button" type="primary">申请加入</el-button></span>
        <span slot="footer" ><el-button @click="createGroup()" class="button" type="info">创建团队</el-button></span>
        </div>
      </div>
    </el-dialog>

    <!-- 管理团队 -->
    <el-dialog title="协作空间" :visible.sync="optionVisible" top="7vh" width="37%" :before-close="handleClose" :show-close="false">
      <div style="margin: 0px 20px 20px 20px">
        <el-input placeholder="请输入协作者用户名/邮箱" v-model="user_keyword" suffix="el-icon-search"></el-input>
        <div style="margin-top: 30px; margin-bottom:30px">
          <div>
            <span style="font-size:16px">管理者</span>
            <div style="margin-top:-20px"><el-divider></el-divider></div>
            <el-table  :data="group.admin" style="width: 100%" :show-header="false">
              <el-table-column   label="头像" width="70">
                <!-- TODO 显示用户头像? -->
                <!-- <img v-if="validAvatar(adminAvatar)" :src="adminAvatar" class="avatar" alt=""  slot="reference"> -->
                <img class="avatar" src="@/assets/avatar.png">
              </el-table-column>
              <el-table-column  prop="name" label="用户名" width="120">
              </el-table-column>
              <el-table-column  prop="mail" label="邮箱" width="220">
              </el-table-column>
              <el-table-column label="操作"> <template><el-button v-if="isAdmin" class="button" plain >管理者</el-button>
              </template>
              </el-table-column>
            </el-table>
          </div>
          <div style="margin-top:30px">
            <span style="font-size:16px">协作者</span>
            <div style="margin-top:-20px"><el-divider></el-divider></div>
            <el-table  :data="group.member" style="width: 100%" :show-header="false">
              <el-table-column   label="头像" width="70">
                <!-- TODO 显示用户头像? -->
                <img class="avatar" src="@/assets/avatar.png">
              </el-table-column>
              <el-table-column  prop="name" label="用户名" width="120">
              </el-table-column>
              <el-table-column  prop="mail" label="邮箱" width="220">
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button v-if="isAdmin" plain @click="removeMem(scope.row.name)">移除协作者</el-button>
              </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <div>
        <span v-if="isAdmin" slot="footer" ><el-button  @click="addMem()" class="button" type="info">添加协作者</el-button></span>
        <span v-if="!isAdmin" slot="footer" ><el-button  @click="leaveGroup()" class="button" type="info">退出团队</el-button></span>
        <span v-if="isAdmin" slot="footer" ><el-button @click="deleteGroup()" class="button" >解散团队</el-button></span>
        </div>
      </div>
    </el-dialog>
    
  </el-container>  
</template>



<script>
  import TeamFile from "../components/TeamFile"
  import MyTeam from "../components/MyTeam"
  import group from '@/api/group'
  import Edit from '@/views/Edit'
  export default {
    name:"TeamSpace",
    components: { TeamFile, MyTeam, Edit},
    data() {
      return {
        user_keyword: '', //添加协作者用户名
        group_keyword: '', //申请加入/创建团队名
        optionVisible: false,
        attendVisible: false,
        activeName: '',
        //当前标签页团队属性
        adminName: '', 
        adminMail: '',
        adminAvatar: '',
        isAdmin: false,
        groups: [
          // {groupId:0,groupName: '团队1'},{groupId:1,groupName: '团队2'},{groupId:2,groupName: '团队3'}
          ],
        group:{
          id: 0, 
          admin: [
            // {id:1, name:'马壮',mail:'1302540061@qq.com'}
          ],
          member: [
            // {id: 1, name:'小明',mail:'1302540061@qq.com'},{id: 2, name:'小明',mail:'1302540061@qq.com'},
            // {id: 3, name:'小明',mail:'1302540061@qq.com'},{id: 4, name:'小明',mail:'1302540061@qq.com'},{id: 5, name:'小明',mail:'1302540061@qq.com'}
          ]
        }
      };
    },
    created(){
      if(localStorage.getItem('name')==''||localStorage.getItem('name')==undefined){
        this.$confirm('您还未登陆/或者登陆已过期', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$router.push({path: '/Login'})
        }).catch(() => {
          // this.$router.push({path: '/Login'})
        });
      }
      this.getGroups()
    },
    methods: {
      validAvatar(avatar){
        console.log(this.adminAvatar)
        if(avatar == null || avatar == undefined) return false
        return avatar.length>0?true:false
      },
      getGroupMem(groupName){
        group.getGroupMem(groupName).then(res=>{
          console.info('团队成员')
          console.log(res.data)
          this.group = res.data
          if(this.group.admin.length>0){
            this.adminName = this.group.admin[0].name
            this.adminMail = this.group.admin[0].mail
            this.adminAvatar = this.group.admin[0].avatar
            if(this.group.admin[0].name==this.$store.state.name) this.isAdmin = true
            else this.isAdmin = false
          }
        })
      },
      getGroups(){
        group.getGroups().then(res=>{
          console.log(res.data)
          this.groups = res.data
          if(this.groups.length>0) {
            this.$store.dispatch('setCurGroupName',this.groups[0].groupName)
          this.activeName = this.groups[0].groupName
          }
          this.getGroupMem(this.$store.state.groupName)
        })
      },
      attendGroup(){
        if(this.group_keyword!=''){
          group.attendGroup(this.group_keyword).then(res=>{
            this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
            this.group_keyword=''
            this.attendVisible =false
          })
        }
      },
      createGroup(){
        if(this.group_keyword!=''){
          group.createGroup(this.group_keyword).then(res=>{
            this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
            this.group_keyword=''
            this.attendVisible = false
          })
        }
      },
      addMem(){
        if(this.user_keyword!=''){
          group.addMem(this.user_keyword,this.$store.state.groupName).then(res=>{
            this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
            this.user_keyword=''
            this.attendVisible =false
          })
        }
      },
      removeMem(name){
        console.log('移除成员：'+name)
        group.removeMem(this.$store.state.groupName,name).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
        })
      },
      leaveGroup(){
        group.removeMem(this.$store.state.groupName,this.$store.state.name).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
          this.$router.go(0)
        })
      },
      deleteGroup(){
        group.deleteGroup(this.$store.state.groupName).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
          this.$router.go(0)
        })
      },
      teamEdit(){
        this.$router.push({path: 'EditTeam'})
      },
      layoutTile(){
        this.$store.dispatch('setLayoutStatus',1)
      },
      layoutList(){
        this.$store.dispatch('setLayoutStatus',0)
      },
      handleClick(tab, event) {
        console.info('切换团队成员信息')
        this.getGroupMem(this.activeName)
      },
      handleClose(done) {
        done();
      }
    }
  };
</script>
<style scoped>
  body{
    margin: 0;
    padding: 0;
  }
  .fontStyle{
    font-size: 17px;
    color:rgb(90, 90, 90)
  }

  .left {
    display: inline-block;
    margin-top: -40px;
    width: 22%;
    height: 100%;
  }
  .left img {
    margin: 0 auto;
    width: 50px;
    border-radius: 50%;
  }
  .right {
    display: inline-block;
    width: 70%;
  }
  .rightTop {
    text-align: left;
    margin-left: 5px;
    margin-top: -10px;
    height: 22px;
  }
  .rightTop .userName {
    text-align: left;
    color: #303133;
    margin-left: 10px;
    font-size: 16px;
    font-weight: bold;
  }
  .rightCenter {
    text-align: left;
    margin-left: 17px;
    height: 20px;
    margin-top: 15px;
  }
  .avatar{
    width: 50px;
    height: 50px;
    border-radius:50%
  }
  .card{
    margin-top:25px;
    border-radius: 5%
  }

  .button{
    margin-left:20px;
    width:170px
  }
</style>
