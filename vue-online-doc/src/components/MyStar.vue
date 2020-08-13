<template>
  <div class="bt">
    <el-input placeholder="keyword" v-model="keyword" suffix="el-icon-search"  @change="getFileData"></el-input>&nbsp;

    <el-table :data="FileData" style="width: 100%">
      <el-table-column label="#" width="100">
          <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.fileId }}</span>
          </template>
        </el-table-column>
      <el-table-column prop="fileName" label="文件名" width="200">
      </el-table-column>
      <el-table-column prop="modifyCnt" label="修改次数" width="200">
      </el-table-column>
      <el-table-column prop="modifyTime"  label="最后修改时间" width="300">
      </el-table-column>
      <el-table-column prop="fileInfo"  label="简介" width="200">
      </el-table-column>
      <el-table-column label="操作" width="160">
          <template slot-scope="scope">
              <el-button size="mini" type="primary"
                  @click="EditFile(scope.row.fileId)">编辑</el-button>
              <el-button size="mini" type="danger" 
                  @click="DeleteFile(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
    </el-table>
    
  </div>
</template>

<script>
  import file from '@/api/file'
  export default {
    name: "MyStar",
    data(){
      return{
        FileData:[
          { "fileId": 1, "fileName": "文档名1","fileInfo": "这里是文档基本信息","modifyTime": "2020-01-01 00:00:00","modifyCnt": 8 },
          { "fileId": 2,"fileName": "文档名2","fileInfo": "这里是文档基本信息","modifyTime": "2020-01-01 00:00:00","modifyCnt": 13 } 
        ],
        keyword:''
      }
    },
    created() {
      // file.getStar().then((res)=>{this.FileData=res.data,  this.total=res.data.total})
    },
    methods:{
      getFileData(keyword){
        file.getStar()
        .then((res)=>{
          this.FileData=[];
          for(var i=0;i<res.data.total;i++){
            if(this.keyword==res.data.file[i].name||this.keyword==res.data.file[i].date)
              this.FileData.push(res.data.file[i]);
            if(this.keyword=='') 
              this.FileData=res.data.file;
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
