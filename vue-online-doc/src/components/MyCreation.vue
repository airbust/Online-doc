<template>
  <div style="height:75vh">
    <!-- 平铺视图 -->
    <el-row  v-if="layout" type="flex" :gutter="0" class="el-row" >
      <el-col :span="1" class="el-col" v-for="(o, fileId) in FileData" :key="fileId" :offset="1" >
          <div style="width: 120px;" @mouseenter="pEnter(fileId)" @mouseleave="pLeave(fileId)">
            <div style="height:20px" >
              <el-dropdown @command="handleCommand">
                <div style="width:100px">
                    <i v-if="showOption[fileId]" class="el-icon-s-tools" style="float:right;font-size: 17px; color: grey"></i>
                </div>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="open">新标签页打开</el-dropdown-item>
                  <el-dropdown-item command="collect" divided>收藏</el-dropdown-item>
                  <el-dropdown-item command="share">分享</el-dropdown-item>
                  <el-dropdown-item command="delete">删除</el-dropdown-item>
                  <el-dropdown-item command="rename" divided>重命名</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <img src="@/assets/doc1.png" @click="goto(o.fileId)" class="image">
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
                    @click="goto(scope.row.fileId)">查看</el-button>
                <el-button size="mini" type="success"
                    @click="collectFile(scope.row.fileId)">收藏</el-button>
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
    name: "MyCreation",
    data(){
      return{
        FileData:[
          // {"fileId":1,"fileName":"123","fileInfo":null,"fileBody":"<p>111</p>","modifyTime":"2020-08-14T00:00:00.000+00:00","modifyCnt":0,"userId":1,"groupId":0,"isEdit":0,"isDelete":0},
          // {"fileId":2,"fileName":"demo","fileInfo":null,"fileBody":"<p>111</p>","modifyTime":"2020-08-14T00:00:00.000+00:00","modifyCnt":0,"userId":1,"groupId":0,"isEdit":0,"isDelete":0},
          // {"fileId":3,"fileName":"test","fileInfo":null,"fileBody":"<p>111</p>","modifyTime":"2020-08-14T00:00:00.000+00:00","modifyCnt":0,"userId":1,"groupId":0,"isEdit":0,"isDelete":0},
        ],
        index: 0, //当前高亮的图标
        showOption: [],
        total: 0,
        keyword:''
      }
    },
    computed:{
      layout() { 
        return this.$store.state.layout==1
      }
    },
    created() {
      this.getFile()
    },
    methods:{
      pEnter(index) { 
        this.$set(this.showOption,index,1)
        this.index = index
      },
      pLeave(index) {
        this.$set(this.showOption,index,0)
      },
      handleCommand(command) {
        if(command == 'open') {}
        else if(command == 'collect') {this.collectFile(this.FileData[this.index].fileId)}
        else if(command == 'share') {}
        else if(command == 'delete') {this.deleteFile(this.FileData[this.index].fileId)}
        else if(command == 'rename') {}
      },
      getFile(){
        file.getCreation().then((res)=>{
          this.FileData=res.data
          this.total = res.data.length
          for(var i=0;i<this.total;++i) this.showOption[i]=0 //暂未获取文章数total
        })
      },
      deleteFile(id){
        file.Deleted(id).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1700 });
          this.getFile()
        })
      },
      collectFile(id){
        file.collectDocument(id).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 });
        })
      },
      getFileData(keyword){
        file.getCreation().then((res)=>{
          this.FileData=[];
          this.total = res.data.length
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
  
</style>
