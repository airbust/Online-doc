<template>
  <el-container>
    <el-aside style="width:75%; margin-right:50px">
      <el-tabs v-model="activeName" type="" @tab-click="handleClick">
        <el-tab-pane label="我的回收" class="fontStyle" name="MyRecycle"><my-recycle/></el-tab-pane>
      </el-tabs>
    </el-aside>
    <el-main style=" background: rgb(247, 247, 247);">
      <el-tooltip class="item" effect="dark" content="平铺" placement="top">
        <span><i @click="layoutTile()" style="font-size: 25px; color: grey" class="el-icon-menu"></i></span>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="列表" placement="top">
        <span><i @click="layoutList()" style="font-size: 25px; color: grey" class="el-icon-s-unfold"></i></span>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="隐藏" placement="top">
        <span><i @click="isActive = !isActive" style="font-size: 25px; color: grey" class="el-icon-d-caret"></i></span>
      </el-tooltip>

      <collapse>
        <div class="collapse" v-show="isActive">
          <div style="margin-top: 40px"><el-button @click="newFile()" style="width: 170px" type="info">新建</el-button></div>
          <div style="margin-top: 15px"><el-button  @click="gotoTLibrary()" style="width: 170px">模板库</el-button></div>
        </div>
      </collapse>

    </el-main>

  </el-container>

</template>

<script>
import CryptoJS from "crypto-js"
  import MyRecycle from "../components/MyRecycle";
  import collapse from "../assets/collapse.js";
  export default {
    name: "Recycle",
    components: {MyRecycle,collapse},
    data(){
      return{
        isActive: true,//默认不隐藏
        activeName: 'MyRecycle'
      }
    },
    created() {
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
    methods:{
      newFile(){
        this.$router.push({path:'/Edit'})
      },
      gotoTLibrary(){
        this.$router.push({path:'/TemplateLibrary'})
      },
      layoutTile(){
        console.log('切换为平铺布局')
        this.$store.dispatch('setLayoutStatus',1)
      },
      layoutList(){
        console.log('切换为列表布局')
        this.$store.dispatch('setLayoutStatus',0)
      },
      handleClick(tab, event) {
        if(this.activeName=='MyStar') { this.refreshStar = Math.random() }
        else if(this.activeName=='Recent') { this.refreshRecent = Math.random() }
      }
    }
  }
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
  button{border: 0;outline: 0;font-size: 16px;border-radius: 320px;padding: 1rem;background-color: #F7F7F7;}
  /*#F7F7F7 #EBECF0*/
  button{color: #61677C;box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #BABECC;transition: all 0.2s ease-in-out;cursor: pointer;font-weight: 600;}
  button:hover {box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #BABECC;}
  button:hover{
    transform: scale(0.95);
  }
  button:active {box-shadow: inset 1px 1px 2px #BABECC, inset -1px -1px 2px #FFF;}
</style>
