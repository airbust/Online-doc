<template>
  <el-container>
    <el-main>
      <el-switch
      style="margin-left:23px"
        v-model="isCollapse"
        active-color="#909399"
        inactive-color="#8CC3fC">
      </el-switch>
      <el-menu
        :default-active="this.$route.path"
        class="el-menu-vertical-demo"
        :router="true"
        :collapse="isCollapse"
      >
      <el-menu-item index="/" >
        <i class="el-icon-tickets"></i>
        <span class="font" slot="title">工作台</span>
      </el-menu-item>
      <el-divider></el-divider>
      <el-menu-item index="/Desktop">
        <i class="el-icon-monitor"></i>
        <span class="font" slot="title">我的桌面</span>
      </el-menu-item>
      <el-menu-item index="/TeamSpace">
        <i class="el-icon-menu"></i>
        <span class="font" slot="title">团队空间</span>
      </el-menu-item>
      <el-menu-item index="/Recycle">
        <i class="el-icon-delete"></i>
        <span class="font" slot="title">回收站</span>
      </el-menu-item>
      <el-divider></el-divider>
     </el-menu>

    </el-main>
  </el-container>
</template>

<script>
    export default {
      name: "Left",
      data(){
        return {
          value2: 0,
          isCollapse: false
        }
      }
    }
</script>

<style scoped>
.el-menu{
  background-color:  #f4f4f4;
  border: 0;
  /*text-align: center;*/
}
.el-container{
  height:100%;
  background-color:  #f4f4f4;
}
.item{
  color:#424e67;
  font-size: 14px;
}
.font{
  font-size: 17px; color:rgb(90, 90, 90)
}
/*菜单栏收缩(关闭时),产生收缩动画*/
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
