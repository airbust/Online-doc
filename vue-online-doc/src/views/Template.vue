<template>
  <el-container>
      <el-header>
        <div class="input-group">
          <label>
            <input v-model="title" placeholder="请输入标题" v-if="flag" style="width:60%"></input>&nbsp;
          </label>
        </div>
        <!--        <el-input v-model="title" placeholder="请输入标题" v-if="flag" style="width:60%"></el-input>-->
        <div class="hd" v-if="!flag">{{this.title}}</div>
      </el-header>

      <el-main style="width: 80%">
        <div class="ql-snow">
          <div class="ql-editor" v-if="!flag" v-html="this.content">{{this.content}}</div>
        </div>
        <quill-editor
        v-model="content" style="height: 60vh"
        ref="myQuillEditor"
        :options="editorOption"
        @blur="onEditorBlur($event)"
        @focus="onEditorFocus($event)"
        @ready="onEditorReady($event)"
        v-if="flag"
        ></quill-editor>
      </el-main>
      <el-footer>
        <el-button @click="startEdit" v-if="!flag">编辑</el-button>
        <el-button @click="endEdit" v-if="flag">预览</el-button>
        <el-button @click="saveFile">保存</el-button>
      </el-footer>
  </el-container>
</template>

<script>
  // 引入样式
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'

  //引入组件，可以直接使用这个组件
  import { quillEditor } from 'vue-quill-editor'
  import { addQuillTitle } from '../quill-title.js'
  import Quill from 'quill' //引入编辑器
  import { ImageDrop } from 'quill-image-drop-module'
  Quill.register('modules/imageDrop', ImageDrop);
  var fonts = ['Microsoft-YaHei','SimSun', 'SimHei','KaiTi','Arial','Times-New-Roman'];
  var Font = Quill.import('formats/font');
  Font.whitelist = fonts; //将字体加入到白名单
  import file from '@/api/file'
  import template from '@/api/template'
  export default {
    name: "Template",
    components:{ quillEditor },
    data() {
      return {
        title: '',
        flag:true,
        content:null,
        editorOption:{
            theme:'snow',
            modules:{
              imageDrop:true,
              toolbar:[
                [{ 'size': ['12px', false , '20px' ,'24px', '32px','48px']}],
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
      this.loadTemplate();
    },
    mounted() {
      addQuillTitle();
    },
    methods:{
      loadTemplate(){
        console.log('begin')
        template.getTemplate(this.$route.params.templateId).then(res=>{
        this.title = res.data.name
        this.content = res.data.body
        })
      },
      saveFile(){
        if(this.title=='') this.$notify({title: '提示',type: 'warning',message: '请填写标题',duration: 2500 })
        else if(this.content==null || this.content=='') 
        this.$notify({title: '提示',type: 'warning',message: '请输入文档内容',duration: 2500 })
        else{
          file.sendDocument(this.title,this.content)
          .then(res=>{
            this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 });
            this.$router.push({path:'/'})
          })
        }
      },
      startEdit(){
        this.flag=true;
      },
      endEdit(){
        this.flag=false;
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
        event.enable(this.flag);
      },
      onEditorChange () {
        // 内容改变事件
        console.log('333')
      }
    }
  }
</script>

<style scoped>
  .hd{
    text-align: left;
    font-size: 30px;
    font-weight: bold;
    line-height: 50px;
    width:75%
  }
  .bd{
    margin-left: 50px;
    margin-top: 30px;
  }
  .el-header{
    width: 100%;
    /* box-shadow:  0 2px 6px 0 rgba(0,0,0,.05); */
  }
  .el-container{
    height: 100%;
  }
  .el-aside{
    background-color:  #f7f7f7;
    max-width: 194px;
  }
  .el-main{
    height: calc(100% - 62px);
    max-width: calc(100% - 194px);
  }
  .input-group {display: flex;align-items: center;justify-content: flex-start;}
  .input-group label {margin: 0;flex: 1;}

  label {display: block;margin-bottom: 24px;width: 100%;}

  input {border: 0;outline: 0;font-size: 16px;border-radius: 320px;padding: 1rem;background-color: #EBECF0;text-shadow: 1px 1px 0 #FFF;}
  input {margin-right: 8px;box-shadow: inset 2px 2px 5px #BABECC, inset -5px -5px 10px #FFF;width: 100%;box-sizing: border-box;transition: all 0.2s ease-in-out;appearance: none;-webkit-appearance: none;}
  input:focus {box-shadow: inset 1px 1px 2px #BABECC, inset -1px -1px 2px #FFF;}

  button{border: 0;outline: 0;font-size: 16px;border-radius: 320px;padding: 1rem;background-color: #F7F7F7;}
  /*#F7F7F7 #EBECF0*/
  button{color: #61677C;box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #BABECC;transition: all 0.2s ease-in-out;cursor: pointer;font-weight: 600;}
  button:hover {box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #BABECC;}
  button:hover{
    transform: scale(0.95);
  }
  button:active {box-shadow: inset 1px 1px 2px #BABECC, inset -1px -1px 2px #FFF;}

</style>
