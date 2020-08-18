<template>
    <div class="main">
      <div class="login_box">
        <div slot="header" class="clearfix">
            <span style="font-size:120%; color: #fff">注册</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="login">已经有账号？去登陆
            </el-button>
        </div>
        <el-row type="flex" justify="center" >
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="demo-ruleForm">
                <el-form-item  prop="name" style="margin-top:40px" >
                    <el-input v-model="ruleForm.name" placeholder="账号"></el-input>
                </el-form-item>
                <el-form-item prop="pass">
                    <el-input v-model="ruleForm.pass" show-password placeholder="密码" ></el-input>
                </el-form-item>
                <el-form-item prop="repass">
                    <el-input v-model="ruleForm.repass" show-password placeholder="确认密码" ></el-input>
                </el-form-item>

              <el-form-item prop="mail">
                <el-col :span="16">
                  <el-input v-model="ruleForm.mail" style="" placeholder="邮箱" ></el-input>
                </el-col>
                <el-col :span="8">
                  <el-button type="primary" @click="getcode()" id="yzm" style="width: 100%; float:right" >获取验证码</el-button >
                </el-col>
              </el-form-item>
              <el-form-item  prop="yzm" id="yzm">
                <el-input v-model="ruleForm.yzm" placeholder="验证码"></el-input>
              </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="register('ruleForm')" class="register">注册</el-button>
                </el-form-item>
            </el-form>
        </el-row>
    </div>

    <vue-particles
        color="#dedede"
        :particleOpacity="0.7"
        :particlesNumber="120"
        shapeType="circle"
        :particleSize="4"
        linesColor="#dedede"
        :linesWidth="1"
        :lineLinked="true"
        :lineOpacity="0.4"
        :linesDistance="150"
        :moveSpeed="3"
        :hoverEffect="true"
        hoverMode="grab"
        :clickEffect="true"
        clickMode="push"
    >
    </vue-particles>
  </div>
</template>

<script>
import user from '@/api/user'
export default {
    name: "Register",
    data() {
        var name = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入账号'));
            } else if (value.length<3) {
                callback(new Error('至少位三个字符'));
            } else {
                callback();
            }
        };
        var pass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else if (value.length<6) {
                callback(new Error('至少位六个字符'));
            } else {
                callback();
            }
        };
        var repass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else if (value !== this.ruleForm.pass) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        var mail= (rule, value, callback) => {
          var reg = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/);
          if (!reg.test(value)) {//检测字符串是否符合正则表达式
            this.$notify({title: '提示',type: 'warning',message: '邮箱格式不正确',duration: 3000 });
            callback(new Error('请输入正确的邮箱'));;
          }
          else {callback(); }
        };
      var yzm= (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入验证码'));
        } else if (value.length!==6) {
          callback(new Error('请输入6位数字验证码'));
        } else {
          callback();
        }
      };
        return {
          loading:false,
            ruleForm: {
                name: '',
                pass:'',
                repass:'',
                yzm:'',
                mail:'',
                loading_text:''
            },
            rules: {
                name: [
                    { validator: name, trigger: 'blur' },
                ],
                pass: [
                    { validator: pass, trigger: 'blur' },
                ],
                repass:[
                    { validator: repass, trigger: 'blur' },
                ],
              mail: [
                { validator: mail, trigger: 'blur' }
                ],
            }
        }
    }
    ,
    methods: {
        login() {
            this.$router.push({path:'/Login'})
        },
        register(ruleForm){
            this.$refs[ruleForm].validate((valid) => {
                if (valid) {
                  this.loading=true
                  this.loading_text='正在注册，请稍后'
                  user.register(this.ruleForm.name,this.ruleForm.pass,this.ruleForm.mail,this.ruleForm.yzm)
                  .then(res => {
                    this.loading=false
                      if(res.message=='注册成功'){
                        this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 })
                        this.$router.push({path: '/Login'})
                      }
                      else
                        this.$notify({title: '提示',type: 'error',message: res.message,duration: 2000 })
                  })
                }
                else {
                    this.$notify({title: '提示',type: 'error',message:'表单填写有误',duration: 2000 })
                }
            })
        },

        getcode() {
          user.sendmail(this.ruleForm.mail)
          .then(res=>{this.$notify({title: '提示',type: 'success',message: res.message,duration: 2000 });
          }).catch(error=> {console.log(error)})}
        },

        home(){
            this.$router.push({path:'/'})
        },
        resetForm(ruleForm){
          this.$refs[ruleForm].resetFields();
      },
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
  height: 100%;
  background-image: url('../../static/Starry.jpg');
  background-size: cover;
}
.login_box {
  margin: auto;
  padding: 20px 20px 20px 20px;
  width: 290px;
  height: 420px;
  background-color: rgba(95, 115, 231, 0.493);
  box-shadow: 0px 0px 15px 15px rgba(6, 17, 47, 0.7);
  border-radius: 5px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.box-card{
  max-width: 400px;
  min-width: 320px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%,-50%);
}
.register{
  width: 100%;
}
</style>
