<template>
  <div style="height:75vh">
    <!-- 分享框 -->
    <el-dialog title="分享"  :visible.sync="dialogVisible"  width="30%">
      <el-input v-model="url" :readonly="true">
        <el-button slot="append" v-clipboard:copy="url" v-clipboard:success="onCopy" v-clipboard:error="onError">复制链接</el-button>
      </el-input>
    </el-dialog>
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
                  <!-- <el-dropdown-item command="rename" divided>重命名</el-dropdown-item> -->
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <img src="../../static/doc1.png" @click="goto(o.fileId)" class="image">
            <div style="margin-top: 14px; text-align: center">
              <a>{{o.fileName}}</a>
            </div>
          </div>
      </el-col>
    </el-row>

    <!-- 表格视图 -->
    <div v-if="!layout">
      <div class="input-group">
        <label>
          <input placeholder="keyword" v-model="keyword" suffix="el-icon-search"  @change="getFileData"></input>&nbsp;
        </label>
      </div>&nbsp;
      <div style="height:650px">
        <el-scrollbar style="height:100%">
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
                    <el-button v-waves size="mini" type="primary"
                        @click="goto(scope.row.fileId)">查看</el-button>
                    <el-button v-waves size="mini" type="success"
                        @click="collectFile(scope.row.fileId)">收藏</el-button>
                    <el-button v-waves size="mini" type="danger"
                        @click="deleteFile(scope.row.fileId)">删除</el-button>
                </template>
              </el-table-column>
          </el-table>
        </el-scrollbar>
      </div>
    </div>

  </div>
</template>

<script>
import CryptoJS from "crypto-js"
  import file from '@/api/file'
  import waves from "../assets/waves/waves"
  export default {
    name: "MyCreation",
    directives:{waves},
    data(){
      return{
        url: '',
        FileData:[],
        index: 0, //当前高亮的图标
        showOption: [],
        dialogVisible:false,
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
        if(command == 'open') {
          var iid = this.FileData[this.index].fileId
          var str = iid.toString()
          var fileId = CryptoJS.AES.encrypt(str,"123").toString()
          while(fileId.indexOf("/") != -1)
            fileId = CryptoJS.AES.encrypt(tmp,"123").toString()
          let routeUrl = this.$router.resolve({path: '/File/'+fileId});
          window.open(routeUrl.href, '_blank') }
        else if(command == 'collect') {this.collectFile(this.FileData[this.index].fileId)}
        else if(command == 'share') {
          var idd = this.FileData[this.index].fileId
          var tmp = idd.toString()
          var fileId = CryptoJS.AES.encrypt(tmp,"123").toString()
          while(fileId.indexOf("/") != -1){
            fileId = CryptoJS.AES.encrypt(tmp,"123").toString()
          } 
          this.url = 'http://39.107.228.168/#/File/'+fileId
          this.dialogVisible = true
        }
        else if(command == 'delete') {this.deleteFile(this.FileData[this.index].fileId)}
        else if(command == 'rename') {}
      },
      getFile(){
        file.getCreation().then((res)=>{
          console.log(res.message)
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
        var tmp = id.toString()
        var fileId = CryptoJS.AES.encrypt(tmp,"123").toString()
        while(fileId.indexOf("/") != -1){
          fileId = CryptoJS.AES.encrypt(tmp,"123").toString()
        }
        this.$router.push({path: '/File/'+fileId})
      },
      onCopy(){
        this.$message({
          message: '复制成功',
          type: 'success'
        });
      },
      onError(){
        this.$message.error('复制失败');
      },
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
    background: #E9E9E9;
  }

  .el-col :hover.image{
    transform: scale(0.95);
  }
  /*修改el-button默认颜色*/
  .el-button--primary,.el-button--primary:focus,.el-button--primary.is-active, .el-button--primary:active{background: #afbfc8;}
  .el-button--success,.el-button--success:focus,.el-button--success.is-active, .el-button--success:active{background: #c3c89d;}
  .el-button--danger,.el-button--danger:focus,.el-button--danger.is-active, .el-button--danger:active{background: #fcc4b5;}

  /*.el-button--primary:hover {
    background: #8cd8da;
    border-color: #8cd8da;
    color: #FFF;
  }
  .el-button--success:hover {
     background: #8cd8da;
     border-color: #8cd8da;
     color: #FFF;
   }
  .el-button--danger:hover {
    background: #f5d7b9;
    border-color: #f5d7b9;
    color: #FFF;
  }*/

  .input-group {display: flex;align-items: center;justify-content: flex-start;}
  .input-group label {margin: 0;flex: 1;}

  label {display: block;margin-bottom: 24px;width: 100%;}

  input {border: 0;outline: 0;font-size: 16px;border-radius: 320px;padding: 1rem;background-color: #EBECF0;text-shadow: 1px 1px 0 #FFF;}
  input {margin-right: 8px;box-shadow: inset 2px 2px 5px #BABECC, inset -5px -5px 10px #FFF;width: 100%;box-sizing: border-box;transition: all 0.2s ease-in-out;appearance: none;-webkit-appearance: none;}
  input:focus {box-shadow: inset 1px 1px 2px #BABECC, inset -1px -1px 2px #FFF;}

</style>
