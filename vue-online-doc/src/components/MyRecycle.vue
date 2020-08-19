<template>
  <div>

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
                <el-dropdown-item command="recover">恢复</el-dropdown-item>
                <el-dropdown-item command="delete">彻底删除</el-dropdown-item>
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
      <!--      <el-input placeholder="keyword" v-model="keyword" suffix="el-icon-search"  @change="getFileData"></el-input>&nbsp;-->
      <el-table :data="FileData" style="width: 100%">
        <el-table-column label="#" width="100">
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{ scope.row.fileId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="fileName" label="文件名" width="200">
        </el-table-column>
        <el-table-column prop="modifyCnt" label="修改次数" width="150">
        </el-table-column>
        <el-table-column prop="modifyTime"  label="最后修改时间" width="300">
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template slot-scope="scope">
            <el-button v-waves size="mini" type="primary"
                       @click="recoverFile(scope.row.fileId)">恢复</el-button>
            <el-button v-waves size="mini" type="danger"
                       @click="deleteFile(scope.row.fileId)">彻底删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
import CryptoJS from "crypto-js"
  import file from '@/api/file'
  import waves from "../assets/waves/waves";
  export default {
    name: "MyRecycle",
    directives:{waves},
    data() {
      return {
        FileData:[],
        showOption: [],
        index: 0,
        total: 0,
        keyword: ''
      }
    },
    computed: {
      layout() {
        return this.$store.state.layout == 1
      }
    },
    created() {
      file.getDeleted().then((res) => {
        this.FileData = res.data
        this.total = res.data.length
      })
    },
    methods: {
      pEnter(index) {
        this.$set(this.showOption, index, 1)
        this.index = index
      },
      pLeave(index) {
        this.$set(this.showOption, index, 0)
      },
      handleCommand(command) {
        if(command == 'recover') {this.recoverFile(this.FileData[this.index].fileId)}
        else if(command == 'delete') {this.deleteFile(this.FileData[this.index].fileId)}
      },
      recoverFile(id) {
        file.recoverDeleted(id).then(res => {
          this.$notify({title: '提示', type: 'success', message: res.message, duration: 1700});
          file.getDeleted().then((res) => {
            this.FileData = res.data
          })
        })
      },
      deleteFile(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            file.foreverDeleted(id).then(res => {
              this.$notify({title: '提示', type: 'success', message: res.message, duration: 1700});
              file.getDeleted().then((res) => {
                this.FileData = res.data
              })
            })
            done();
          })
          .catch(_ => {});
      },

      getFileData() {
        file.getRecycle().then((res) => {
          this.FileData = [];
          this.total = res.data.length
          for (var i = 0; i < this.total; i++) {
            if (this.keyword == res.data[i].fileName || this.keyword == res.data[i].modifyTime)
              this.FileData.push(res.data[i]);
            if (this.keyword == '')
              this.FileData = res.data;
          }
        })
      },
      goto(id) {
        var tmp = id.toString()
        var fileId = CryptoJS.AES.encrypt(tmp,"123").toString()
        while(fileId.indexOf("/") != -1){
          fileId = CryptoJS.AES.encrypt(tmp,"123").toString()
        }
        this.$router.push({path: '/File/'+fileId})
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
    background: #E9E9E9;
  }

  .el-col :hover.image{
    transform: scale(0.95);
  }
  /*修改el-button默认颜色*/
  .el-button--primary,.el-button--primary:focus,.el-button--primary.is-active, .el-button--primary:active{background: #afbfc8;}
  .el-button--success,.el-button--success:focus,.el-button--success.is-active, .el-button--success:active{background: #c3c89d;}
  .el-button--danger,.el-button--danger:focus,.el-button--danger.is-active, .el-button--danger:active{background: #fcc4b5;}
  .input-group {display: flex;align-items: center;justify-content: flex-start;}
  .input-group label {margin: 0;flex: 1;}

  label {display: block;margin-bottom: 24px;width: 100%;}

  input {border: 0;outline: 0;font-size: 16px;border-radius: 320px;padding: 1rem;background-color: #EBECF0;text-shadow: 1px 1px 0 #FFF;}
  input {margin-right: 8px;box-shadow: inset 2px 2px 5px #BABECC, inset -5px -5px 10px #FFF;width: 100%;box-sizing: border-box;transition: all 0.2s ease-in-out;appearance: none;-webkit-appearance: none;}
  input:focus {box-shadow: inset 1px 1px 2px #BABECC, inset -1px -1px 2px #FFF;}

  .zhuanti img { width: 100%; -moz-transition: all .5s ease; -webkit-transition: all .5s ease; -ms-transition: all .5s ease; -o-transition: all .5s ease; transition: all .5s ease; opacity: 0.5 }
  .zhuanti p { position: absolute; top: 30%; left: 0; right: 0; color: #FFF; text-align: center; font-size: 15px; overflow: hidden; margin-top: 5px; padding: 0 40px; }
  .zhuanti p a { color: #fff; }
  .zhuanti span { width: 80px; margin: 10px auto; background: transparent; font-size: 12px; border: 1px solid #FFF; border-radius: 40px; padding: 4px 0; color: #FFF; display: block; clear: both; -webkit-transition: all .3s ease; -moz-transition: all .3s ease; -ms-transition: all .3s ease; -o-transition: all .3s ease; transition: all .3s ease; }
  .zhuanti li:hover img { opacity: 0.6 }
  .zhuanti li:hover span { background: #FFF; }
  .zhuanti li:hover span a { color: #333 }

</style>
