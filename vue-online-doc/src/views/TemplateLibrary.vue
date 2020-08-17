<template>
  <div style="height:75vh">
    <!-- 平铺视图 -->
    <el-row type="flex" :gutter="0" class="el-row" >
      <el-col :span="1" class="el-col" v-for="(o, templateId) in templateData" :key="templateId" :offset="1" >
          <div style="width: 120px;" @mouseenter="pEnter(templateId)" @mouseleave="pLeave(templateId)">
            <img src="@/assets/doc1.png" @click="goto(o.templateId)" class="image">
            <div style="margin-top: 14px; text-align: center">
              <a>{{o.templateName}}</a>
            </div>
          </div>
      </el-col>
    </el-row>    
  </div>
</template>

<script>
  import template from '@/api/template'
  export default {
    name: "MyCreation",
    data(){
      return{
        templateData:[
          // {"templateId":1,"templateName":"123","templateInfo":null,"templateBody":"<p>111</p>","modifyTime":"2020-08-14T00:00:00.000+00:00","modifyCnt":0,"userId":1,"groupId":0,"isEdit":0,"isDelete":0},
          // {"templateId":2,"templateName":"demo","templateInfo":null,"templateBody":"<p>111</p>","modifyTime":"2020-08-14T00:00:00.000+00:00","modifyCnt":0,"userId":1,"groupId":0,"isEdit":0,"isDelete":0},
          // {"templateId":3,"templateName":"test","templateInfo":null,"templateBody":"<p>111</p>","modifyTime":"2020-08-14T00:00:00.000+00:00","modifyCnt":0,"userId":1,"groupId":0,"isEdit":0,"isDelete":0},
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
        template.getCreation().then((res)=>{
          this.templateData=res.data
          this.total = res.data.length
          for(var i=0;i<this.total;++i) this.showOption[i]=0 //暂未获取文章数total
        })
      },
      getTemplateData(keyword){
        template.getCreation().then((res)=>{
          this.templateData=[];
          this.total = res.data.length
          for(var i=0;i<this.total;i++){
            if(this.keyword==res.data[i].templateName)
              this.templateData.push(res.data[i]);
            if(this.keyword=='') 
              this.templateData=res.data;
          }
        })
      },
      goto(id){
        this.$router.push({path: '/template/'+id})
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
