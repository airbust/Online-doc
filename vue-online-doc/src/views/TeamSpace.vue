<template>
  <el-container>
    <el-aside style="width:75%; margin-right:50px">
      <el-tabs v-model="activeName" type="" @tab-click="handleClick">
        <el-tab-pane label="团队文档" name="TeamFile"><team-file/></el-tab-pane>
        <el-tab-pane label="团队信息" name="MyTeam"><my-team/></el-tab-pane>
      </el-tabs>
    </el-aside>
    <el-main style=" background: rgb(247, 247, 247);">
      <el-tooltip class="item" effect="dark" content="平铺" placement="top">
        <span><i @click="layoutTile()" style="font-size: 25px; color: grey" class="el-icon-menu"></i></span>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="列表" placement="top">
        <span><i @click="layoutList()" style="font-size: 25px; color: grey" class="el-icon-s-unfold"></i></span>
      </el-tooltip>

      <div style="margin-top: 40px"><el-button style="width: 170px" type="info">新建</el-button></div>
      <div style="margin-top: 15px"><el-button style="width: 170px">模板库</el-button></div>
      <div style="margin-top: 15px"><el-button style="width: 170px">导入</el-button></div>

    </el-main>

  </el-container>

</template>



<script>
  import TeamFile from "../components/TeamFile"
  import MyTeam from "../components/MyTeam"
  export default {
    name:"TeamSpace",
    components: {
      TeamFile,
      MyTeam
    },
    data() {
      return {
        activeName: 'TeamFile'
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
    },
    methods: {
      layoutTile(){
        console.log('切换为平铺布局')
        this.$store.dispatch('setLayoutStatus',1)
      },
      layoutList(){
        console.log('切换为列表布局')
        this.$store.dispatch('setLayoutStatus',0)
      },
      handleClick(tab, event) {
        // console.log(tab, event);
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
</style>
