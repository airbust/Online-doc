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
        <span slot="title">工作台</span>
      </el-menu-item>
      <el-menu-item index="3">
        <i class="el-icon-message"></i>
        <span slot="title">收件箱</span>
      </el-menu-item>
      <el-menu-item index="4">
        <i class="el-icon-menu"></i>
        <span slot="title">团队空间</span>
      </el-menu-item>
      <el-menu-item index="5">
        <i class="el-icon-delete"></i>
        <span slot="title">回收站</span>
      </el-menu-item>
      <el-menu-item index="/Edit">
        <i class="el-icon-tickets"></i>
        <span slot="title">文档编辑(测试)</span>
      </el-menu-item>
     </el-menu>

    </el-main>
  </el-container>
</template>

<script>
    export default {
        name: "Left",
      data(){
          return {
            use:0,
            usesize:'0'
          }
      },
      created(){
        this.$http.post(this.$HOST+'v2/getusesize',this.$qs.stringify({
          sign:this.$sign
        })).then(res=>{
          var MB=524288000;
         var  persent=parseInt(res.data.data.size/MB)
          if(persent==0){
            this.use=1
          }else{
            this.use=persent
          }
          this.usesize=res.data.data.realsize

        })
      },
      methods:{

      }
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
</style>
