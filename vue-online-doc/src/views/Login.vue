<template>
    <div class="main">
        
    <div class="login_box">
            <div slot="header" class="clearfix">
                <span style="font-size:120%; color: #fff">登录</span>
                <el-button style="float: right; padding: 3px 0" type="text" @click="register">还没有账号？去注册
                </el-button>
            </div>
            <el-row type="flex" justify="center">
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  class="demo-ruleForm">
                <el-form-item prop="name" style="margin-top:40px">
                    <el-input v-model="ruleForm.name "  placeholder="账号" prefix-icon="el-icon-user"></el-input>
                </el-form-item>
                    <el-form-item  prop="pass">
                        <el-input v-model="ruleForm.pass" show-password placeholder="密码" prefix-icon="el-icon-key"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="login('ruleForm')" class="login">登录</el-button>
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
import store from '@/store/store'
import user from '@/api/user'
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
                name: '', pass:''
            },
            rules: {
                name: [{ validator: name, trigger: 'blur' } ],
                pass: [{ validator: pass, trigger: 'blur' }],
            },
            loginForm: {
        userName: "",
        passWord: ""
      },
      loginRules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        passWord: [{ required: true, message: "请输入密码", trigger: "blur" }]
      }
        }
    },
    methods: {
        login(ruleForm) {
            this.$refs[ruleForm].validate((valid) => {
                if (valid) {
                  this.loading=true
                  user.login(this.ruleForm.name,this.ruleForm.pass)
                  .then(res => {
                      this.loading=false
                      if(res.message=='登录成功'){
                        this.$store.commit('login', res.data)//存储token
                        this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 });
                        
                        this.$router.push({path: '/'})
                        this.$router.go(0)
                      }
                      else {
                        this.$notify({title: '提示',type: 'error',message: res.message,duration: 1500 });
                      }
                    })
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
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
  height: 100%;
  background-image: url('../../static/Starry.jpg');
  background-size: cover;
}

.login_box {
  margin: auto;
  padding: 20px 20px 20px 20px;
  width: 290px;
  height: 270px;
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
  .login{
    width: 100%;
  }

</style>

