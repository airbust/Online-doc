<template>
  <el-container>
    <el-header><Header></Header></el-header>
    <el-container>
    <el-aside><Left></Left></el-aside>
    <el-main> <quill-editor
    v-model="content"
    ref="myQuillEditor"
    :options="editorOption"
    @blur="onEditorBlur($event)"
    @focus="onEditorFocus($event)"
    @ready="onEditorReady($event)"
    ></quill-editor>
    </el-main>
    </el-container>
  </el-container>
</template>

<script>
import  Header from '../components/Header'
import  Right from '../components/Right'
import  Left from '../components/Left'
// 引入样式
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
 
//引入组件，可以直接使用这个组件
import { quillEditor } from 'vue-quill-editor'
    export default {
      name: "Home",
      components:{
        Header,
        Right,
        Left,
        quillEditor
      },
      data() {
        return {
          content:null,
          editorOption:{}
        }
      },
      created(){
        if(localStorage.getItem('uid')==''||localStorage.getItem('uid')==undefined){
          this.$confirm('您还未登陆/或者登陆已过期', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$router.push({path: '/'})
          }).catch(() => {
            this.$router.push({path: '/'})
          });
        }

      },
        methods:{
          onEditorReady (editor) {
            // 准备编辑器
            console.log('111')
          },
          onEditorBlur () {
            // 失去焦点事件
            console.log('111')
          },
          onEditorFocus () {
            // 获得焦点事件
            console.log('222')
          },
          onEditorChange () {
            // 内容改变事件
            console.log('333')
          }
        }
    }
</script>

<style scoped>
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
