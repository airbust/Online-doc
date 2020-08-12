<template>
  <el-container>
    <el-tabs v-model="activeName" type="" @tab-click="handleClick">
      <el-tab-pane label="最近浏览" name="first"><Right1></Right1></el-tab-pane>
      <el-tab-pane label="我的收藏" name="second"><Right2></Right2></el-tab-pane>
      <el-tab-pane label="我的创建" name="third"><Right3></Right3></el-tab-pane>
    </el-tabs>
  </el-container>

</template>


<script>
  import Right1 from "../components/Recent"
  import Right2 from "../components/MyStar"
  import Right3 from "../components/MyCreation"
  export default {
    name:"WorkStation",
    components: {
      Right1,
      Right2,
      Right3
    },
    data() {
      return {
        activeName: 'first'
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
      handleClick(tab, event) {
        console.log(tab, event);
      }
    }
  };
</script>
<style scoped>
 body{
  margin: 0;
  padding: 0;
}
</style>
