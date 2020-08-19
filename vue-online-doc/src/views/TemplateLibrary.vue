<template>
  <div style="height:75vh">
    <!-- 平铺视图 -->
    <el-row type="flex" :gutter="0" class="el-row" >
      <el-col :span="4" class="el-col" v-for="(o, index) in templateData" :key="index" :offset="1" >
          <div @mouseenter="pEnter(index)" @mouseleave="pLeave(index)">
            <img :src="o.cover" @click="gotoTemplete(o.id)" class="image">
            <div style="margin-top: 14px; text-align: center">
              <a>{{o.name}}</a>
            </div>
          </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CryptoJS from "crypto-js"
  import template from '@/api/template'
  export default {
    name: "TempleteLibrary",
    data(){
      return{
        templateData:[],
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
      this.getTemplateList()
    },
    methods:{
      pEnter(index) {
        this.$set(this.showOption,index,1)
        this.index = index
      },
      pLeave(index) {
        this.$set(this.showOption,index,0)
      },
      getTemplateList(){
        template.getTemplateList().then((res)=>{
          this.templateData=res.data
          this.total = res.data.length
          for(var i=0;i<this.total;++i) this.showOption[i]=0 //暂未获取文章数total
        })
      },
      gotoTemplete(id){
        this.$router.push({path: '/Template/'+id})
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
    margin-left: 17px;
    width: 200px;
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
    /* width:120px;   */
    border-radius: 4px;
    align-items: stretch;
    margin-bottom: 40px;
    /* background: #333; */
  }
  /*.el-col :hover{
    background: rgb(247, 247, 247);
  }*/
  .el-col :hover{
    background: #E9E9E9;
  }

  .el-col :hover.image{
    transform: scale(0.95);
  }

</style>
