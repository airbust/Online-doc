<template>
  <div>

    <!-- 平铺视图 -->
    <el-row  v-if="layout" type="flex" :gutter="0" class="el-row" >
      <el-col :span="1" class="el-col" v-for="(o, fileId) in FileData" :key="fileId" :offset="1" >
        <div style="width: 120px;" @mouseenter="pEnter(fileId)" @mouseleave="pLeave(fileId)">
          <div style="height:20px" >
            <el-dropdown @command="unCollectFile(o.fileId)">
              <div style="width:100px">
                <i v-if="showOption[fileId]" class="el-icon-s-tools" style="float:right;font-size: 17px; color: grey"></i>
              </div>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item >新标签页打开</el-dropdown-item>
                <el-dropdown-item divided>取消收藏</el-dropdown-item>
                <el-dropdown-item>分享</el-dropdown-item>
                <el-dropdown-item divided>重命名</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <img src="@/assets/doc1.png" @click="goto(fileId)" class="image">
          <div style="margin-top: 14px; text-align: center">
            <a>{{o.fileName}}</a>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 表格视图 -->
    <div v-if="!layout">
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
        <el-table-column label="操作" width="250">
          <template slot-scope="scope">
            <el-button size="mini" type="primary"
                       @click="goto(scope.row.fileId)">编辑</el-button>
            <el-button size="mini" type="success"
                       @click="unCollectFile(scope.row.fileId)">取消收藏</el-button>
            <el-button size="mini" type="danger"
                       @click="deleteFile(scope.row.fileId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
  import file from '@/api/file'
  export default {
    name: "MyStar",
    data(){
      return{
        FileData:[],
        showOption: [],
        total: 3,
        keyword:''
      }
    },
    computed:{
      layout() {
        return this.$store.state.layout==1
      }
    },
    created() {
      file.getMyCollectingDocument().then((res)=>{
        this.FileData=res.data
        console.log(this.FileData)
        for(var i=0;i<30;++i) this.showOption[i]=0 //暂未获取文章数total
      })
    },
    methods:{
      pEnter(index) {
        this.$set(this.showOption,index,1)
      },
      pLeave(index) {
        this.$set(this.showOption,index,0)
      },
      handleCommand(command) {
      },
      deleteFile(id){
        console.info('delete file: id='+id)
        file.deleteDocument(id).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1700 });
          file.getMyCollectingDocument().then((res)=>{this.FileData=res.data})
        })
      },
      unCollectFile(id){
        file.removeCollectedDocument(id).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1700 });
          file.getMyCollectingDocument().then((res)=>{this.FileData=res.data})
        })
      },
      getFileData(){
        file.getMyCollectingDocument().then((res)=>{
          this.FileData=[];
          for(var i=0;i<this.total;i++){
            if(this.keyword==res.data[i].fileName)
              this.FileData.push(res.data[i]);
            if(this.keyword=='')
              this.FileData=res.data;
          }
        })
      },
      goto(id){
        this.$router.push({path: '/File/'+id})
      }
    }
  }
</script>

<style scoped>
  .time {
    font-size: 13px;
    color: #999;
  }
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }
  .button {
    padding: 0;
    float: right;
  }
  .image {
    margin-left: 24px;
    width: 70px;
    cursor:pointer;
    display: block;
  }
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

  .el-row {
    margin-top: 50px;
    display: flex;
    flex-wrap: wrap
  }
  .el-col {
    width:120px;
    border-radius: 4px;
    align-items: stretch;
    margin-bottom: 40px;
    /* background: #333; */
  }
  .el-col :hover{
    background: rgb(247, 247, 247);
  }

  .zhuanti img { width: 100%; -moz-transition: all .5s ease; -webkit-transition: all .5s ease; -ms-transition: all .5s ease; -o-transition: all .5s ease; transition: all .5s ease; opacity: 0.5 }
  .zhuanti p { position: absolute; top: 30%; left: 0; right: 0; color: #FFF; text-align: center; font-size: 15px; overflow: hidden; margin-top: 5px; padding: 0 40px; }
  .zhuanti p a { color: #fff; }
  .zhuanti span { width: 80px; margin: 10px auto; background: transparent; font-size: 12px; border: 1px solid #FFF; border-radius: 40px; padding: 4px 0; color: #FFF; display: block; clear: both; -webkit-transition: all .3s ease; -moz-transition: all .3s ease; -ms-transition: all .3s ease; -o-transition: all .3s ease; transition: all .3s ease; }
  .zhuanti li:hover img { opacity: 0.6 }
  .zhuanti li:hover span { background: #FFF; }
  .zhuanti li:hover span a { color: #333 }

</style>
