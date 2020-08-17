<template>
  <el-container>
    <el-main>
      <el-menu
        :default-active="this.$route.path"
        class="el-menu-vertical-demo"
        :router="true"
      >
      <el-menu-item index="/" >
        <i class="el-icon-tickets"></i>
        <span class="font" slot="title">工作台</span>
      </el-menu-item>
      <el-menu-item index="3">
        <i class="el-icon-message"></i>
        <span class="font" slot="title">收件箱</span>
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
    }
</script>

<style scoped>
.el-menu{
  background-color:  #f7f7f7;
  border: 0;
  /*text-align: center;*/
}
.el-container{
  height:100%;
  background-color:  #f7f7f7;
}
.item{
  color:#424e67;
  font-size: 14px;
}
.font{
  font-size: 17px; color:rgb(90, 90, 90)
}
</style>
