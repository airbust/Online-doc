<template>
  <div class="bt">
    <el-input placeholder="keyword" v-model="keyword" suffix="el-icon-search"  @change="getFileData"></el-input>&nbsp;

    <el-table :data="FileData" style="width: 100%">

      <el-table-column prop="id" label="#" width="180">
      </el-table-column>

      <el-table-column prop="leader" label="领导者" width="800">
      </el-table-column>

      <el-table-column prop="member"  label="组员" width="490">
      </el-table-column>

    </el-table>
  </div>
</template>

<script>
  import user from '@/api/user'
  //import axios from 'axios'
  export default {
    name: "TeamFile",
    data(){
      return{
        FileData:[],
        keyword:''
      }
    },
    //初始化表格，使打开界面即可看到表格信息
    created(keyword) {
      //axios.get('https://www.easy-mock.com/mock/5ce57090f2fc446b5d8ffe18/blog/blogdata')
      user.getFileData(keyword).then((res)=>{this.FileData=res.data.message;})
    },
    methods:{
      //当输入查找条件时，要把查找的的条件与后台的数据进行比较，找到对应条件的后台数据给表格显示
      getFileData(keyword){
        //axios.get('https://www.easy-mock.com/mock/5ce57090f2fc446b5d8ffe18/blog/blogdata')
        user.getFileData(keyword)
          .then((res)=>{
            this.FileData=[];
            for(var i=0;i<res.data.total;i++){
              if(this.keyword==res.data.message[i].id||this.keyword == res.data.message[i].leader||this.keyword==res.data.message[i].member){
                this.FileData.push(res.data.message[i]);
              }
              if(this.keyword==''){
                this.FileData=res.data.message;
              }
            }
          })
      }
    }
  }
</script>

<style scoped>

</style>
