<template>
  <div style="height:100%; width:80%" v-if="readable" >
    <div class="header">

      <div style="float:right">
        <el-button type="text" v-if="is_Edit">此文档正在被编辑</el-button>
        <el-button type="text" @click="showHistory()" v-if="writable">查看历史版本</el-button>
        <el-button @click="startEdit" v-if="!is_Edit&&writable&&!editing">编辑</el-button>
        <el-button @click="editing=false" v-if="writable&&editing">预览</el-button>
        <el-button @click="updateFile" v-if="writable&&editing">更新保存</el-button>

        <el-button @click="dialogVisible=true">分享</el-button>
        <el-dialog  title="分享"  :visible.sync="dialogVisible"  width="30%">
          <el-input v-model="url" :readonly="true">
            <el-button slot="append"
            v-clipboard:copy="url"
            v-clipboard:success="onCopy"
            v-clipboard:error="onError">复制链接</el-button>
          </el-input>
        </el-dialog>

        <!-- 历史版本 -->
        <el-dialog title="历史版本"  :visible.sync="historyVisible"  width="60%"  :before-close="handleClose">
          <el-table :data="historyFileList" style="width: 100%">
                <el-table-column prop="modifyCnt" label="修改次数" width="180">
                </el-table-column>
                <el-table-column prop="fileName" label="文件名" width="250">
                </el-table-column>
                <el-table-column prop="modifyTime"  label="最后修改时间" width="300">
                </el-table-column>
                <el-table-column label="操作" width="250">
                  <template slot-scope="scope">
                      <el-button size="mini" type="primary"
                          @click="gotoHistory(scope.row.fileId,scope.row.modifyCnt)">查看</el-button>
                  </template>
                </el-table-column>
           </el-table>
        </el-dialog>

        <el-dialog title="历史文档"  :visible.sync="historyFileVisible"  top="50px" width="75%"  :before-close="handleClose">
          <div style="height:700px;width:80%;margin-left:10%">
            <el-scrollbar style="height:100%">
              <div class="hd" >{{this.historyFile.fileName}}</div>
              <div class="ql-snow"><div class="ql-editor" v-html="this.historyFile.fileBody">{{this.historyFile.fileBody}}</div></div>
            </el-scrollbar>
          </div>
        </el-dialog>

        <!-- 权限设置 -->
        <el-cascader v-if="authable&&!editing"
          style="width:120px"
          v-model="value"
          :options="options"
          @change="handleChange">
        </el-cascader>
      </div>

      <el-input v-model="title" placeholder="请输入标题" v-if="writable&&editing"></el-input>
      <div class="hd" v-if="!editing">{{this.title}}</div>
      <!-- <p style="display: none">{{fileId = this.$route.params.fileId}}</p> -->
    </div>
    <el-divider content-position="right">䂖墨文档 </el-divider>
    <div>
      <div class="ql-snow">
          <div class="ql-editor" style="min-height:700px" v-if="!editing" v-html="this.content">{{this.content}}</div>
        </div>
      <div>
        <quill-editor style="height:60vh;"
        v-model="content"
        ref="myQuillEditor"
        :options="editorOption"
        @blur="onEditorBlur($event)"
        @focus="onEditorFocus($event)"
        @ready="onEditorReady($event)"
        v-if="editing&&writable"
        ></quill-editor>
      </div>
      <el-divider></el-divider>
      <div v-if="false">
        <div style="margin-left:50px">[调试]当前您的身份是：{{this.$store.state.roles}}</div>
        <div style="margin-left:50px">[调试]当前文档权限是：{{auth}}</div>
      </div>

      <el-card v-if="discussable&&!editing" style="margin-top: 50px" show="never">
        <!-- 发评论 -->
        <div class="commentBox">
          <span class="right">
            <textarea id="textpanel" class="textArea" placeholder="既然来了，那就留下些什么吧~" v-model="messageBody"></textarea>
          </span>
        </div>
        <div class="bottom">
          <el-button class="p2" v-waves style="margin-right:80px" type="primary"  @click="sendMessage">发送评论</el-button>
          <el-button class="p2" v-waves type="info" @click="cancelSendMessage()">取消评论</el-button>
        </div>
        <!-- 评论列表 -->
        <div class="message_infos">
          <div v-for="message in messageList" :key="message.discuss.discussId">
            <div class="commentList">
              <span class="left p1" @click="gotoUserPage(message.user.name)">
                <img v-if="!message.user.avatar" src="../../static/avatar.svg">
                <img v-else :src="message.user.avatar"  style="width:50px; height:50px"
                  onerror="javascript:this.src='../../static/avatar.svg'" />
              </span>
              <span class="right p1">
                <div class="rightTop" v-if="message.user.id">
                  <el-link class="userName" :underline="false">{{message.user.name}}</el-link>
                  <span class="timeAgo" >{{dateFormat(message.discuss.discussTime)}}</span>
                </div>
                <div class="rightCenter">{{message.discuss.discussBody}}</div>
                <div class="rightBottom">
                    <el-divider content-position="right">
                    <el-link :underline="false" class="el-icon-delete" v-if="myComment(message.user.name)"
                        @click="deleteMessage(message.discuss.discussId)"/>
                    </el-divider>
                </div>
              </span>
            </div>
          </div>
          <div class="noComment" v-if="messageList.length ==0">还没有评论，快来抢沙发吧！</div>
        </div>
      </el-card>
    </div>

    <el-popover style="position: fixed; bottom: 40px; right: 40px"
      placement="top-start" title="文档信息" width="300" trigger="hover" :content="fileInfo">
      <div v-html="fileInfo">{{fileInfo}}</div>
      <el-button slot="reference"><a class="cd-top">Info</a></el-button>
    </el-popover>

    </div>
</template>

<script>
  // 引入样式
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'

  import waves from "../assets/waves/waves";

  //引入组件，可以直接使用这个组件
  import { quillEditor } from 'vue-quill-editor'
  import { addQuillTitle } from '../quill-title.js'
  import Quill from 'quill' //引入编辑器
  import { ImageDrop } from 'quill-image-drop-module'
  Quill.register('modules/imageDrop', ImageDrop);
  var fonts = ['Microsoft-YaHei','SimSun', 'SimHei','KaiTi','Arial','Times-New-Roman'];
  var Font = Quill.import('formats/font');
  Font.whitelist = fonts;

  import file from '@/api/file'
  import CryptoJS from "crypto-js";
  import message from '@/api/message'
  export default {
    name: "Edit",
    directives:{waves},
    components:{ quillEditor },
    data() {
      return {
        fileId: 0,
        fileInfo: '',
        url: '',
        historyFileList:[],
        historyFile: {},
        value: ['other','RD'],
        options: [{
          value: 'other', label: '权限设置-其他用户',
          children: [{value: 'RD',label: '允许查看与评论'}, {value: 'R',label: '仅允许查看'}, {value: 'N',label: '不允许查看'}]
          }, {
          value: 'group', label: '权限设置-团队用户',
          children: [{ value: 'RDW',label: '允许查看评论与编辑'}, { value: 'RD',label: '允许查看与评论'}, { value: 'R',label: '仅允许查看'}]
        } ],
        authList: [],
        messageList: [ ],   //当前页数据
        messageBody: '',
        loading: true, //是否加载中
        ////////////////
        title: '',
        originalTitle: '',
        originalContent: '',
        editing:false,
        readable:false,
        writable:false,
        authable: false,
        discussable: false,
        is_Edit:false,
        auth: {},
        content:null,
        dialogVisible: false,
        historyVisible: false,
        historyFileVisible: false,
        editorOption:{
            theme:'snow',
            modules:{
              imageDrop:true,
              toolbar:[
                [{ 'size': ['12px', false ,'20px','24px', '32px','48px']}],
                ['bold', 'italic', 'underline', 'strike'],    //加粗，斜体，下划线，删除线
                ['blockquote', 'code-block'],     //引用，代码块
                [{ 'header': 1 }, { 'header': 2 }],        // 标题，键值对的形式；1、2表示字体大小
                [{ 'list': 'ordered'}, { 'list': 'bullet' }],     //列表
                [{ 'script': 'sub'}, { 'script': 'super' }],   // 上下标
                [{ 'indent': '-1'}, { 'indent': '+1' }],     // 缩进
                [{ 'direction': 'rtl' }],             // 文本方向
                [{ 'font': fonts }],
                //[{ 'size': ['small', false, 'large', 'huge'] }], // 字体大小
                [{ 'header': [1, 2, 3, 4, 5, 6, false] }],     //几级标题
                [{ 'color': [] }, { 'background': [] }],     // 字体颜色，字体背景颜色
                //[{ 'font': [] }],     //字体
                [{ 'align': [] }],    //对齐方式
                ['clean'],    //清除字体样式
                ['image','video']    //上传图片、上传视频
              ]
            }
          },
      }
    },
    created(){
      this.loadFile();
      this.loadMessage()
      this.url = 'http://39.107.228.168/#'+this.$route.path;
    },
    mounted() {
      addQuillTitle()
      window.onbeforeunload = e => {this.endEdit() }
    },
    destroyed(){this.endEdit()},
    methods:{
      gotoHistory(id,modifyCnt){//跳转至历史版本文档
        this.historyFileVisible = true
        file.getHistoryFile(id,modifyCnt).then(res=>{
          this.historyFile = res.data
          console.log(res.message)
        })
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
      showHistory(){
        this.historyVisible = true
        file.getHistory(this.fileId).then(res=>{
          console.log(res.message)
          this.historyFileList = res.data
          for(var i=0;i<res.data.length;++i)
            this.historyFileList[i].modifyTime = this.dateFormat(res.data[i].modifyTime)
        })
      },
      myComment(name){
        return this.$store.state.name == name ? true : false
      },
      handleChange(){
        // console.log(this.value)
        file.updateAuth(this.fileId,this.value[0],this.value[1]).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 })
        })
      },
      loadMessage () {
        message.getMessage(this.fileId).then(res => {
          this.messageList = res.data
          console.log(res.data)
          this.loading = false
        })
      },
      sendMessage () {
        if (this.messageBody.length <= 0) {
          this.$notify({title: '提示',type: 'error',message: '字段不完整',duration: 2000 });
          return;
        }
        message.sendMessage(this.fileId,this.messageBody).then(res => {
          this.$notify({title: '提示',type: 'success',message: '留言成功',duration: 1500 });
          this.messageBody = ''
          this.loadMessage()
        })
      },
      cancelSendMessage() {
        this.messageBody = '';
      },
      deleteMessage(id) {
        this.$confirm('是否删除此留言?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          message.deleteMessage(id).then(res => {
            this.$notify({title: '提示',type: 'success',message: '删除成功',duration: 2000 });
            this.loadMessage();
          })
        }).catch(() => {
        });
      },
      loadFile(){
        var tmp = this.$route.params.fileId
        var bytes = CryptoJS.AES.decrypt(tmp,"123")
        this.fileId = bytes.toString(CryptoJS.enc.Utf8)
        file.getDocument(this.fileId).then(res=>{
          console.log(res.data)
          this.$store.commit('login', res.data.map)//存储token
          var a = res.data
          this.auth = a.role
          this.title = a.file.fileName
          this.content = a.file.fileBody
          if(a.file.isEdit == 1) {this.is_Edit =true
          console.info('fuck')}
          var sort = a.file.userId != 0 ? '个人文档': '团队文档'
          var updateTime = this.dateFormat(a.file.modifyTime)
          var info = '文档名：'+a.file.fileName+'<br/>类别：'+sort+'<br/>修改次数：'+a.file.modifyCnt+'<br/>更新时间：'+ updateTime
          this.fileInfo = info
          this.init_authority();
        })
      },
      dateFormat(time) {
        var date=new Date(time);
        var year=date.getFullYear();
        var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
        var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
        var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
        var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
        var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
        return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
      },
      init_authority(){
        //初始化其他用户权限设置选项  ！！bug:不能初始化团队选项
        if(this.auth.otherRead==0) this.value = ['other','N']
        else if(this.auth.otherDiscuss===0)  this.value = ['other','R']

        if(this.$store.state.roles=="USER"){
          this.readable=true
          this.writable=true
          this.authable=true
          this.discussable=true;
        }
        else if(this.$store.state.roles=="OTHER"){
          if(this.auth.otherRead==1) this.readable=true;
          else this.readable=false;
          if(this.auth.otherDiscuss==1) this.discussable=true;
          else this.discussable=false
        }
        else if(this.$store.state.roles=="GROUP"){
          this.readable=true;
          if(this.auth.groupDiscuss==1) this.discussable=true;
          else this.discussable=false
          if(this.auth.groupWrite==1){
            this.writable=true;
          }else this.writable=false
        }
        if(this.readable==false){
          this.$notify({title: '提示',type: 'error',message: '无访问权限',duration: 2500 });
        }
      },
      startEdit(){
        this.editing=true;
        file.isEditing(this.fileId).then(res=>{
          console.log('change is_edit to 1')
        })
      },
      endEdit(){
        this.editing=false;
        file.isEditable(this.fileId).then(res=>{
          console.log('change is_edit to 0')
        })
      },
      updateFile(){   //change is_edit to 0
        console.log('save begin')
        file.updateDocument(this.fileId,this.title,this.content)
        .then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 });
        })
        this.endEdit()  //将所有版本(同id)文档置is_Edit=0
      },
      handleClose(done) {
        done()
      },
      gotoUserPage(name){
        this.$router.push({path:'/Profile/'+name})
      },
      onEditorReady (editor) {
        // 准备编辑器
        // console.log('111')
      },
      onEditorBlur () {
        // 失去焦点事件
        console.log('111')
      },
      onEditorFocus (event) {
        // 获得焦点事件
        // event.enable(false);
      },
      onEditorChange () {
        // 内容改变事件
        console.log('333')
      },

    }
  }
</script>

<style scoped>
  .cd-top {
    display: inline-block; height: 40px; width: 40px; position: fixed; bottom: 40px;  right: 40px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.05); overflow: hidden; text-indent: 100%; white-space: nowrap;
    background: rgba(0, 0, 0, 0.8)  url(../../static/info.png) no-repeat center;
   }
  .commentList {
    width: 100%;
    margin: 0 auto;
  }
  .commentList .p1 {
    float: left;
  }
  .commentList .left {
    display: inline-block;
    width: 5%;
    height: 100%;
  }
  .commentList .left img {
    margin: 0 auto;
    width: 100%;
    border-radius: 50%;
  }
  .commentList .right {
    display: inline-block;
    width: 93%;
    margin-left: 5px;
  }
  .commentList .rightTop {
    text-align: left;
    margin-left: 5px;
    height: 30px;
    margin-top: 2px;
  }
  .commentList .rightTop .userName {
    text-align: left;
    color: #303133;
    margin-left: 10px;
    font-size: 16px;
    font-weight: bold;
  }
  .commentList .rightTop .timeAgo {
    text-align: left;
    color: #909399;
    margin-left: 10px;
    font-size: 15px;
  }
  .commentList .rightCenter {
    text-align: left;
    margin-left: 20px;
    height: 50px;
    margin-top: 15px;
  }
  .commentList .rightBottom {
    margin-left: 10px;
    height: 30px;
  }
  .commentBox {
    min-width: 700px;
    width: 100%;
    height: 100px;
    margin: 0 auto;
  }
  .commentBox .left {
    display: inline-block;
    width: 4%;
    height: 100%;
    padding-top: 3px;
  }
  .commentBox .left img {
    cursor: pointer;
    margin: 0 auto;
    width: 90%;
    border-radius: 50%;
  }
  .commentBox .right {
    display: inline-block;
    margin: 5px 2px 0 0;
    width: 95%;
    height: 100%;
  }
  textarea::-webkit-input-placeholder {
    color: #909399;
  }
  .commentBox .right textarea {
    color: #606266;
    padding:10px 5px 5px 10px;
    resize: none;
    width: 95%;
    height: 100%;
  }
  .bottom {
    position: relative;
    min-width: 690px;
    width: 98%;
    height: 60px;
    line-height: 40px;
    margin-top: 20px;
  }
  .bottom .p2 {
    float: right;
    margin-top: 15px;
    margin-right: 17px;
  }
  .hd{
    text-align: left;
    font-size: 30px;
    font-weight: bold;
    line-height: 50px;
    width:75%
  }
  .bd{
    min-height:70vh;
    margin-left: 50px;
    margin-top: 30px;
    width:85%
  }
  .header{
    width: 100%;
    height: 100px;
    /* box-shadow:  0 2px 6px 0 rgba(0,0,0,.05); */
  }
  .el-header{
    width: 80%;
    box-shadow:  0 2px 6px 0 rgba(0,0,0,.05);
  }
  .el-aside{
    background-color:  #f7f7f7;
    max-width: 194px;
  }
  .el-main{
    height: calc(100% - 62px);
    max-width: calc(100% - 194px);
  }
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
</style>
