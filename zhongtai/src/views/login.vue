<template>
  <div class="bg">
    <el-form class="form" :model="ruleForm" label-position="left" :rules="rules" ref="Form" label-width="0px">
      <el-form-item label="" prop="name">
        <el-input v-model.trim="ruleForm.name" suffix-icon="el-icon-user-solid" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="" prop="pwd">
        <el-input v-model.trim="ruleForm.pwd" type="password" :show-password="false" suffix-icon="el-icon-s-goods" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="" prop="code">
	    <el-row :gutter="20">
				<el-col :span="15">
					<el-input v-model.trim="ruleForm.code" placeholder="请输入验证码"></el-input>
				</el-col>
				<el-col :span="9" @click.native="code">
					<div style="height: 40px;color: blue;background: #666;text-align: center;font-size: 18px;letter-spacing: 6px;">{{str}}</div>
				</el-col>
			</el-row>
      </el-form-item>
      <el-form-item>
        <el-button style="width:100%;" type="primary" @click="submitForm('Form')">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
// @ is an alias to /src
export default {
  name: 'login',
  data() {
    return {
			ruleForm: {
				name: '',
				pwd: '',
				code: '',
			},
			rules: {
				name: [
				  { required: true, message: '请输入用户名', trigger: 'change' },
				],
				pwd: [
				  { required: true, message: '请输入密码', trigger: 'change' }
				],
				code: [
				  { validator: this.validateCode, trigger: 'change' },
				],
			},
			str:'',
    }
  },
  components: {
  },
  created() {
		this.code()
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.ruleForm.act = 'index';
          let res = await this.$http.post(this.ruleForm,'app.php');
          this.code();
          if(res.message === "success") {
            this.$router.push(`/home/business`);
          }else {
            this.$message({
              type: 'error',
              message: res.message
            });
          }
        } else {
          return false;
        }
      });
    },
    validateCode(rule, value, callback) {
			if(value.toLowerCase() !== this.str.toLowerCase()){
				callback(new Error('请输入正确的验证码!'))
			}else {
				callback()
			}
    },
		resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    code() {
			var random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',
					 'S','T','U','V','W','X','Y','Z');
			this.str = '';
			for(var i = 0; i < 4;i++){
				var index = Math.floor(Math.random()*random.length);//取得随机数的索引（0~35）
				this.str += random[index];
			}
    },
  }
}
</script>
<style lang="scss" scoped>
.bg {
  overflow: hidden;
	width: 100%;
	height: 100%;
	background: url(../assets/imgs/loginbg.jpg) no-repeat;
	background-size: 100% 100%;
  text-align: right;
  .form {
    display: inline-block;
    border-radius: 10px;
    background: #fff;
    width: 300px;
    padding: 20px;
    margin-top: 150px;
    margin-right: 20%;
  }
}
</style>