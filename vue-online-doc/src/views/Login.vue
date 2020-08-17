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

