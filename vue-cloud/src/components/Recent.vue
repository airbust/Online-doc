<template>
  <div >
    <div class="bt" >

      <el-button type="text" size="medium" icon="el-icon-arrow-left" @click="back" v-if="path=='/'?false:true">返回上一级</el-button>

      <el-dialog
        title="输入文件名字"
        :visible.sync="addfolder"
        width="20%"
      >
        <el-input v-model="input" placeholder="请输入内容"></el-input>
        <span slot="footer" class="dialog-footer">
    <el-button @click="addfolder = false">取 消</el-button>
    <el-button type="primary" @click="newfolder">确 定</el-button>
  </span>
      </el-dialog>

      <div id="search"><input placeholder="请输入内容" class="search" v-model="keywords"/><i class="el-icon-search"></i>
      </div>
    </div>
    <el-table :data="search(keywords)"  :height="height">
      <el-table-column prop="name" label="文件名" width="800">
        <template slot-scope="{row,$index}">
          <img :src="'../../static/img/'+row.img" alt="" style="cursor: default;
            display: block;
            height: 26px;
            width: 26px;
            position: absolute;
            left:0px;
            top: 10px;">
          <a href="javascript:void(0)" style="position: absolute;
            left:40px;top: 12px;" @click="next(row.name)">{{row.name}}</a>

        </template>
      </el-table-column >
      <el-table-column prop="size" label="大小" width="270">
      </el-table-column>
      <el-table-column prop="time" label="修改日期" width="220">
      </el-table-column>
    </el-table>

  </div>

</template>

<script>
  export default {
    name: 'Recent',
    data() {
      return {
        input:'',
        height: window.innerHeight -62 -80 -40 ,
        tableData: [],
        keywords: '',
        dialogVisible: false,
        loading:false,
        path:'/',
        username:localStorage.getItem('name'),
        addfolder:false


      }
    },
    created() {

      this.init()
    },
    methods: {
      init(){
        localStorage.setItem('path','/')
        this.$http.post(this.$HOST + 'v2/filelist', this.$qs.stringify({
          sign: this.$sign,
          username:localStorage.getItem('name')
        })).then(res => {

          res.data.data.dir.forEach(item => {
            if (item.size == '') {
              var size = '-'
            } else {
              if (item.size < 1048576) {
                var size = (item.size / 1024).toFixed(2) + 'KB'
              } else if (item.size > 1048576 && item.size < 1073741824) {
                var size = (item.size / 1024 / 1024).toFixed(2) + 'MB'
              } else if (item.size > 1073741824) {
                var size = (item.size / 1024 / 1024 / 1024).toFixed(2) + 'GB'
              }
            }
            this.tableData.push({name: item.name, time: item.mtime, img: item.img, size: size})
          })
          res.data.data.file.forEach(item => {
            if (item.size == '') {
              var size = '-'
            } else {
              if (item.size < 1048576) {
                var size = (item.size / 1024).toFixed(2) + 'KB'
              } else if (item.size > 1048576 && item.size < 1073741824) {
                var size = (item.size / 1024 / 1024).toFixed(2) + 'MB'
              } else if (item.size > 1073741824) {
                var size = (item.size / 1024 / 1024 / 1024).toFixed(2) + 'GB'
              }
            }
            this.tableData.push({name: item.name, time: item.mtime, img: item.img, size: size})
          })
        })
      },
      search(key) {
        //搜索
        var newlist = []
        this.tableData.forEach(item => {
          if (item.name.indexOf(key) != -1) {
            newlist.push(item)
          }
        })
        return newlist
      },
      handleClose(done) {
        done();
        // this.$confirm('确认关闭？')
        //   .then(_ => {
        //     done();
        //   })
        //   .catch(_ => {
        //   });
      },
      next(name)  {
        var newpath=localStorage.getItem('path')+name+'/'
        this.path=newpath
        this.$http.post(this.$HOST + 'v2/filelist', this.$qs.stringify({
          sign: this.$sign,
          username:localStorage.getItem('name'),
          path:newpath

        })).then(res => {
          localStorage.setItem('path',newpath)
          this.tableData=[]
          res.data.data.dir.forEach(item => {
            if (item.size == '') {
              var size = '-'
            } else {
              if (item.size < 1048576) {
                var size = (item.size / 1024).toFixed(2) + 'KB'
              } else if (item.size > 1048576 && item.size < 1073741824) {
                var size = (item.size / 1024 / 1024).toFixed(2) + 'MB'
              } else if (item.size > 1073741824) {
                var size = (item.size / 1024 / 1024 / 1024).toFixed(2) + 'GB'
              }
            }
            this.tableData.push({name: item.name, time: item.mtime, img: item.img, size: size})
          })
          res.data.data.file.forEach(item => {
            if (item.size == '') {
              var size = '-'
            } else {
              if (item.size < 1048576) {
                var size = (item.size / 1024).toFixed(2) + 'KB'
              } else if (item.size > 1048576 && item.size < 1073741824) {
                var size = (item.size / 1024 / 1024).toFixed(2) + 'MB'
              } else if (item.size > 1073741824) {
                var size = (item.size / 1024 / 1024 / 1024).toFixed(2) + 'GB'
              }
            }
            this.tableData.push({name: item.name, time: item.mtime, img: item.img, size: size})
          })
        })


      },
      back(){
        // console.log( localStorage.getItem('path').split('/'))
        var str=localStorage.getItem('path').split('/')
        str.splice(0,1)
        str.splice(str.length-1,1)
        str.splice(str.length-1,1)
        var backpath='/'
        str.forEach(item=>{
          backpath+=item+'/'
        })
        this.path=backpath
        this.$http.post(this.$HOST + 'v2/filelist', this.$qs.stringify({
          sign: this.$sign,
          username:localStorage.getItem('name'),
          path:backpath

        })).then(res => {
          localStorage.setItem('path',backpath)
          this.tableData=[]
          res.data.data.dir.forEach(item => {
            if (item.size == '') {
              var size = '-'
            } else {
              if (item.size < 1048576) {
                var size = (item.size / 1024).toFixed(2) + 'KB'
              } else if (item.size > 1048576 && item.size < 1073741824) {
                var size = (item.size / 1024 / 1024).toFixed(2) + 'MB'
              } else if (item.size > 1073741824) {
                var size = (item.size / 1024 / 1024 / 1024).toFixed(2) + 'GB'
              }
            }
            this.tableData.push({name: item.name, time: item.mtime, img: item.img, size: size})
          })
          res.data.data.file.forEach(item => {
            if (item.size == '') {
              var size = '-'
            } else {
              if (item.size < 1048576) {
                var size = (item.size / 1024).toFixed(2) + 'KB'
              } else if (item.size > 1048576 && item.size < 1073741824) {
                var size = (item.size / 1024 / 1024).toFixed(2) + 'MB'
              } else if (item.size > 1073741824) {
                var size = (item.size / 1024 / 1024 / 1024).toFixed(2) + 'GB'
              }
            }
            this.tableData.push({name: item.name, time: item.mtime, img: item.img, size: size})
          })
        })

      }
    }
  }
</script>
<style scoped>

  .bt {
    max-width: 100%;
    background-color: white;
    height: 40px;
    font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";
    line-height: 30px;
  }
  .nav{
    max-width: 100%;
    background-color: white;
    height: 20px;
    /*font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";*/
    font-size: 8px;
    line-height: 20px;
  }

  .el-table-column {
    max-height: 48px;
    line-height: 48px;
  }

  .el-table {
    max-width: 100%;
    font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";
  }

  #search {
    width: 315px;
    border-radius: 33px;
    background-color: #f7f7f7;
    float: right;
    text-align: center;
    height: 32px;
    line-height: 32px;

  }

  .search {
    border: none;
    background-color: #f7f7f7;
    height: 30px;
    width: 248px;
  }

  img {
    width: 30px;
    height: 30px;
  }

  a {
    color: #424e67;
    text-decoration: none;
  }

  a:hover {
    color: #09AAFF;
  }
  .el-icon-delete{
    color:#F56C6C;
  }
</style>
