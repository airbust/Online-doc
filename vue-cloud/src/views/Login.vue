<template>
    <div class="main">


                <el-card class="box-card" v-loading="loading" element-loading-text="正在登陆，请稍后">
                    <div slot="header" class="clearfix">
                        <span>登陆</span>
                        <el-button style="float: right; padding: 3px 0" type="text" @click="register">还没有账号？去注册
                        </el-button>
                    </div>
                    <el-row type="flex" justify="center">
                        <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  class="demo-ruleForm">
                        <el-form-item prop="name">
                            <el-input v-model="ruleForm.name "  placeholder="账号"></el-input>
                        </el-form-item>
                            <el-form-item  prop="pass">
                                <el-input v-model="ruleForm.pass" show-password placeholder="密码"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="login('ruleForm')" class="login">登陆</el-button>
                            </el-form-item>

                        </el-form>

                    </el-row>
                </el-card>



    </div>
</template>

<script>


    export default {
        name: "Login",

        data() {
            var name = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入账号'));
                }  else {
                    callback();
                }
            };
            var pass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                }  else {
                    callback();
                }
            };
            return {
      loading:false,
                ruleForm: {
                    name: '',
                    pass:''
                },
                rules: {
                    name: [
                        { validator: name, trigger: 'blur' },

                    ],
                    pass: [
                        { validator: pass, trigger: 'blur' },
                    ],

                }

            }
        }
        ,
        methods: {
            login(ruleForm) {
                this.$refs[ruleForm].validate((valid) => {
                    if (valid) {
                      this.loading=true
                        this.$http.post(this.$HOST+"v1/login", this.$qs.stringify({
                            "username": this.ruleForm.name,
                            "password": this.ruleForm.pass,
                            "sign": this.$sign
                        })).then(res => {
                            if(res.data.code==0){
                              this.loading=false
                              localStorage.setItem('uid',this.$md5(this.ruleForm.name))
                              localStorage.setItem('name',this.ruleForm.name)
                              localStorage.setItem('nick',res.data.data[0].nick)
                              this.$alert('登陆成功', '提示', {
                                confirmButtonText: '确定',
                                callback: action => {
                                  this.$router.push({path:'Home'})
                                }
                              })
                                setTimeout(()=>{
                                    this.$router.push({path: '/Home'})
                                },1000)
                            }
                            else if(res.data.code==1){
                              this.loading=false
                              this.$alert('用户名不存', '提示', {
                                confirmButtonText: '确定',
                              })
                            }
                            else if(res.data.code==2){
                              this.loading=false
                              this.$alert('密码错误', '提示', {
                                confirmButtonText: '确定',
                              })
                            }

                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });


            },
            resetForm(ruleForm) {
                this.$refs[ruleForm].resetFields();
            },
            register() {
                this.$router.push({path: 'Register'})
            },
            home() {
                this.$router.push({path: '/Home'})
            }
        }
    }
</script>

<style scoped>
    a{
        color: white;
        text-decoration: none;
    }
    a:hover{
        color: gray;
    }
  .main{
    /*background:url("../assets/bg.jpg") no-repeat;*/
    /*width:100%;*/
    /*height:100%;*/
    /*background-size:100%100%;*/
  }

.box-card{
 max-width: 400px;
  min-width: 320px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);

}
  .login{
    width: 100%;
  }

</style>

