<template>
  <el-container>
      <el-header>
        <el-input v-model="title" placeholder="请输入标题" v-if="flag" style="width:60%"></el-input>
        <div class="hd" v-if="!flag">{{this.title}}</div>
      </el-header>
      
      <el-main style="width: 80%">
        <div class="bd" v-if="!flag" v-html="this.content">{{this.content}}</div>
        <quill-editor
        v-model="content"
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

  import file from '@/api/file'
  export default {
    name: "EditTeam",
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
                ['bold', 'italic', 'underline', 'strike'],    //加粗，斜体，下划线，删除线
                ['blockquote', 'code-block'],     //引用，代码块

                [{ 'header': 1 }, { 'header': 2 }],        // 标题，键值对的形式；1、2表示字体大小
                [{ 'list': 'ordered'}, { 'list': 'bullet' }],     //列表
                [{ 'script': 'sub'}, { 'script': 'super' }],   // 上下标
                [{ 'indent': '-1'}, { 'indent': '+1' }],     // 缩进
                [{ 'direction': 'rtl' }],             // 文本方向


                [{ 'size': ['small', false, 'large', 'huge'] }], // 字体大小
                [{ 'header': [1, 2, 3, 4, 5, 6, false] }],     //几级标题


                [{ 'color': [] }, { 'background': [] }],     // 字体颜色，字体背景颜色
                [{ 'font': [] }],     //字体
                [{ 'align': [] }],    //对齐方式


                ['clean'],    //清除字体样式
                ['image','video']    //上传图片、上传视频
              ]
            }
          },
      }
    },
    created(){

    },
    mounted() {
      addQuillTitle();
    },
    methods:{
      saveFile(){
        console.log('save begin')
        file.saveTeamFile(this.title,this.content,this.$store.state.groupName)
        .then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 });
        })
      },
      startEdit(){
        this.flag=true;
      },
      endEdit(){
        this.flag=false;
      },
      onEditorReady (editor) {
        // 准备编辑器
        console.log('111')
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
    text-align: center;
    line-height: 50px;
  }
  .bd{
    width: 75%;
    margin-left: 50px;
    margin-top: 30px;
  }
  .el-header{
    width: 100%;
    box-shadow:  0 2px 6px 0 rgba(0,0,0,.05);
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

</style>
