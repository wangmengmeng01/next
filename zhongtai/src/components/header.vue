<template>
  <div class="header">
    <div>
      <span>韵达科技</span>
      <span>欢迎回来,yunda18140547028</span>
    </div>
    <div>
      <span @click="edit">密码修改</span>
    </div>
		<el-dialog
			title=""
			:visible.sync="flag"
			width="700px"
			center>
			<el-form  :hide-required-asterisk="false" :rules="rules" ref="ruleForm" label-position="left" label-width="100px" :model="fromData">
				<el-form-item label="新密码输入"  prop="pwd">
					<el-input v-model.trim="fromData.pwd" show-password></el-input>
				</el-form-item>
				<el-form-item label="新密码确认" prop="pwd2">
					<el-input v-model.trim="fromData.pwd2" show-password></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer">
				<el-button type="primary" @click="submit('ruleForm')">提交</el-button>
				<el-button type="primary" @click="flag = false">取 消</el-button>
				<el-button type="primary" @click="resetForm('ruleForm')">重置</el-button>
			</span>
		</el-dialog>
  </div>
</template>

<script>
export default {
	data() {
		return {
			flag: false,
			rules: {
				pwd: [
					{ required: true, message: '请输入密码',trigger: 'change'},
				],
				pwd2: [
					{ required: true, message: '请输入密码',trigger: 'change'},
					{ validator: this.validateCode, trigger: 'change'},
				],
			},
			fromData: {
				pwd:'',
				pwd2:''
			}
		}
	},
  props: {
    msg: String
  },
	methods: {
		edit() {
			this.flag = true;
		},
		validateCode(rule, value, callback) {
			if(value !== this.fromData.pwd){
				callback(new Error('两次密码不一致!'))
			}else {
				callback()
			}
		},
		resetForm(formName) {
			this.$refs[formName].resetFields();
		},
		submit(formName) {
			this.$refs[formName].validate(async (valid) => {
				if (valid) {
					let res = await this.$http.post(Object.assign({},this.fromData,{act:'update_password',}),'user.php');
					if(res.message === "success") {
						this.flag = false;
						this.$message({
							type: 'success',
							message: '修改成功'
						});
					}else {
						this.$message({
							type: 'error',
							message: res.message
						});
					}
				} else {
					console.log('error submit!!');
					return false;
				}
			});
		}
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.header {
  height: 110px;
  background: #FFCC33;
  overflow: hidden;
  &>div:first-child {
    display: flex;
    justify-content: space-between;
    color: #333333;
    margin-top: 20px;
    align-items: center;
    span:first-child {
      margin-left: 25px;
      font-size: 48px;
      font-family: Elephant;
      line-height: 48px;
    }
    span:last-child {
      font-weight: bold;
      font-size: 14px;
      font-family: Arial;
      margin-right: 55px;
    }
  }
  &>div:nth-child(2) {
    font-size: 0;
    font-family: Arial;
    color: #169BD5;
    text-align: right;
    margin-top: 10px;
    span {
			cursor: pointer;
      font-size: 13px;
      margin-right:25px;
      display: inline-block;
    }
  }
}
</style>
