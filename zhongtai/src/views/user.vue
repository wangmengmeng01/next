<template>
  <div class="bg">
		<div class="search">
			<div class="item">
				姓名
				<el-input style="width:200px;margin-left:15px;" v-model.trim="search.real_name" placeholder="请输入"></el-input>
			</div>
			<div class="item">
				工号
			  <el-input style="width:200px;margin-left:15px;" v-model.trim="search.job_num" placeholder="请输入"></el-input>
			</div>
			<div class="item">
				状态
				<el-select style="width:200px;margin-left:15px;" v-model="search.status" placeholder="请选择">
					<el-option
						v-for="item in [{key:0 ,value:'锁定'},{key:1 ,value:'开启'}]"
						:key="item.key"
						:label="item.value"
						:value="item.key">
					</el-option>
				</el-select>
			</div>
		</div>
	  <div style="margin-top: 30px;">
			<el-button @click.native="look" type="primary">查询</el-button>
			<el-button @click.native="reset" type="primary">重置</el-button>
			<el-button @click.native="add" type="primary">添加</el-button>
		</div>
		<div style="margin-top: 10px;">
			<el-table
				:data="tableData"
				border
				style="width: 100%">
				<el-table-column
					fixed
					prop="id"
					label="序号"
					align="center"
					>
					<template slot-scope="scope">
						<span>{{scope.$index}}</span>
					</template>
				</el-table-column>
				<el-table-column
					prop="real_name"
					label="姓名"
					align="center"
					>
				</el-table-column>
				<el-table-column
					prop="job_num"
					label="工号"
					align="center"
					>
				</el-table-column>
				<!-- <el-table-column
					prop="city"
					label="密码"
					align="center"
					>
				</el-table-column> -->
				<el-table-column
					prop="updatetime"
					label="更新时间"
					align="center"
					>
					<template slot-scope="scope">
						<span>{{scope.row.updatetime.split(' ')[0]}}</span>
					</template>
				</el-table-column>
				<el-table-column
					prop="status"
					label="状态"
					align="center"
					>
					<template slot-scope="scope">
						<span>{{scope.row.status === 0 ? '锁定' : '开启'}}</span>
					</template>
				</el-table-column>
				<el-table-column
					prop="role_id"
					label="角色"
					align="center"
					>
				</el-table-column>
				<el-table-column
					prop="mobile"
					label="手机号"
					align="center"
					>
				</el-table-column>
				<el-table-column
					prop="email"
					label="邮箱"
					align="center"
					>
				</el-table-column>
				<el-table-column
					fixed='right'
					label="操作"
					align="center"
					width="200px"
					>
					<template slot-scope="scope">
						<el-button @click="lock(scope.row)" type="text" size="small">锁定</el-button>
						<el-button @click="open(scope.row)" type="text" size="small">开启    </el-button>
						<el-button @click="edit(scope.row)" type="text" size="small">修改</el-button>
						<el-button @click="del(scope.row)" type="text" size="small">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div>
			<el-dialog
				title="账号添加"
				:visible.sync="addFlag"
				width="700px"
				:modal-append-to-body="false"
				:append-to-body="true"
				@close="resetForm('addUserForm')"
				center>
				<el-form :model="addForm" :rules="rules" ref="addUserForm" label-width="70px" class="demo-ruleForm">
					<el-row :gutter="20">
						<el-col :span="12">
							<el-form-item label="工号" prop="job_num">
								<el-input v-model.trim="addForm.job_num" placeholder="请输入"></el-input>
							</el-form-item>
							<el-form-item label="姓名" prop="real_name">
								<el-input v-model.trim="addForm.real_name" placeholder="请输入"></el-input>
							</el-form-item>
							<!-- <el-form-item label="密码" prop="name">
								<el-input v-model="addForm.name"></el-input>
							</el-form-item> -->
							<el-form-item label="角色" prop="role_id">
								<el-select style="width: 100%;" v-model="addForm.role_id" placeholder="请选择">
									<el-option
										v-for="item in [{key:0 ,value:'普通用户'},{key:1 ,value:'管理员'}]"
										:key="item.key"
										:label="item.value"
										:value="item.key">
									</el-option>
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="状态" prop="status">
								<el-select style="width:100%;" v-model="addForm.status" placeholder="请选择">
									<el-option
										v-for="item in [{key:0 ,value:'锁定'},{key:1 ,value:'开启'}]"
										:key="item.key"
										:label="item.value"
										:value="item.key">
									</el-option>
								</el-select>
							</el-form-item>
							<el-form-item label="手机号" prop="mobile">
								<el-input v-model.trim="addForm.mobile" placeholder="请输入"></el-input>
							</el-form-item>
							<el-form-item label="邮箱" prop="email">
								<el-input v-model.trim="addForm.email" placeholder="请输入"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>
				<span slot="footer">
					<el-button type="primary"  @click="resetForm('addUserForm')">重置</el-button>
					<el-button type="primary" @click="submitForm('addUserForm')">提交</el-button>
				</span>
			</el-dialog>
			<el-dialog
				title="账号修改"
				:visible.sync="editFlag"
				width="700px"
				:modal-append-to-body="false"
				:append-to-body="true"
				center>
				<el-form :model="editForm" :rules="rules" ref="editUserForm" label-width="70px" class="demo-ruleForm">
					<el-row :gutter="20">
						<el-col :span="12">
							<el-form-item label="工号" prop="job_num">
								<el-input v-model.trim="editForm.job_num" placeholder="请输入"></el-input>
							</el-form-item>
							<el-form-item label="姓名" prop="real_name">
								<el-input v-model.trim="editForm.real_name" placeholder="请输入"></el-input>
							</el-form-item>
							<!-- <el-form-item label="密码" prop="name">
								<el-input v-model="editForm.name"></el-input>
							</el-form-item> -->
							<el-form-item label="角色" prop="role_id">
								<el-select style="width: 100%;" v-model="editForm.role_id" placeholder="请选择">
									<el-option
										v-for="item in [{key:0 ,value:'普通用户'},{key:1 ,value:'管理员'}]"
										:key="item.key"
										:label="item.value"
										:value="item.key">
									</el-option>
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="状态" prop="status">
								<el-select style="width:100%;" v-model="editForm.status" placeholder="请选择">
									<el-option
										v-for="item in [{key:0 ,value:'锁定'},{key:1 ,value:'开启'}]"
										:key="item.key"
										:label="item.value"
										:value="item.key">
									</el-option>
								</el-select>
							</el-form-item>
							<el-form-item label="手机号" prop="mobile">
								<el-input v-model.trim="editForm.mobile" placeholder="请输入"></el-input>
							</el-form-item>
							<el-form-item label="邮箱" prop="email">
								<el-input v-model.trim="editForm.email" placeholder="请输入"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>
				<span slot="footer">
					<el-button type="primary"  @click="resetForm('editUserForm')">重置</el-button>
					<el-button type="primary" @click="submitForm('editUserForm')">提交</el-button>
				</span>
			</el-dialog>
		</div>
  </div>
</template>

<script>
// @ is an alias to /src
import validator from '@/assets/js/validator.js'
export default {
  name: 'user',
  data() {
    return {
			rules: {
				real_name: [
					{ required: true, message: '请输入姓名', trigger: 'change' },
				],
				job_num: [
					{ required: true, message: '请输入工号', trigger: 'change' },
				],
				status: [
					{ required: true, message: '请选择状态', trigger: 'change' },
				],
				role_id: [
					{ required: true, message: '请输入角色', trigger: 'change' },
				],
				mobile: [
					{ required: false, message: '请输入手机号', trigger: 'change' },
					{ validator: validator.mobile, trigger: 'change' },
				],
				email: [
					{ required: false, message: '请输入邮箱', trigger: 'change' },
					{ validator: validator.email, trigger: 'change' },
				],
			},
			editFlag: false,
			editForm: {
				email: '',
				mobile: '',
				role_id: '',
				status: '',
				updatetime: '',
				job_num: '',
				real_name: '',
			},
			addForm: {
				email: '',
				mobile: '',
				role_id: '',
				status: '',
				updatetime: '',
				job_num: '',
				real_name: '',
			},
			addFlag:false,
			search: {
				real_name: '',
				job_num: '',
				status: '',
			},
			tableData: []
    }
  },
  components: {
  },
  created() {
		this.get();
  },
  methods: {
		async get() {
			let params = {
				act:'index',
			}
			let res = await this.$http.post(params,'user.php');
			if(res.message === "success") {
				this.tableData = res.data;
			}else {
				this.$message({
					type: 'error',
					message: res.message
				});
			}
		},
		resetForm(formName) {
			this.$refs[formName].resetFields();
		},
		submitForm(formName) {
			this.$refs[formName].validate(async (valid) => {
				if (valid) {
					if(formName === 'addUserForm') {
						this.addForm.act = 'create';
						let res = await this.$http.post(this.addForm,'user.php');
						if(res.message === "success") {
							this.get();
							this.addFlag = false;
							this.$message({
								type: 'success',
								message: '添加成功'
							});
						}else {
							this.$message({
								type: 'error',
								message: res.message
							});
						}
					}
					if(formName === 'editUserForm') {
						this.editForm.act = 'edit';
						let res = await this.$http.post(this.editForm,'user.php');
						if(res.message === "success") {
							this.get();
							this.editFlag = false;
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
					}
				} else {
					console.log('error submit!!');
					return false;
				}
			});
		},
		open(row) {
			this.$confirm('开启后改用户可以登陆，请确认是否开启？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(async () => {
				let res = await this.$http.post({id: row.id, status: 1, act: 'status'},'user.php');
				if(res.message === "success") {
					this.get();
					this.addFlag = false;
					this.$message({
						type: 'success',
						message: '开启成功'
					});
				}else {
					this.$message({
						type: 'error',
						message: res.message
					});
				}
			}).catch(() => {
			});
		},
		edit(row) {
			this.editForm = JSON.parse(JSON.stringify(row));
			this.editFlag = true;
		},
		del(row) {
			this.$confirm('删除后改用户无法登陆，请确认是否删除？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(async () => {
				let res = await this.$http.post({id: row.id, act: 'delete'},'user.php');
				if(res.message === "success") {
					this.get();
					this.addFlag = false;
					this.$message({
						type: 'success',
						message: '删除成功'
					});
				}else {
					this.$message({
						type: 'error',
						message: res.message
					});
				}
			}).catch(() => {
			});
		},
		lock(row) {
			this.$confirm('锁定后改用户无法登陆，请确认是否锁定？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(async () => {
				let res = await this.$http.post({id: row.id, status: 0, act: 'status'},'user.php');
				if(res.message === "success") {
					this.get();
					this.addFlag = false;
					this.$message({
						type: 'success',
						message: '锁定成功'
					});
				}else {
					this.$message({
						type: 'error',
						message: res.message
					});
				}
			}).catch(() => {
			});
		},
    async look() {
			this.search.act = 'index';
			let res = await this.$http.post(this.search,'user.php');
			if(res.message === "success") {
				this.tableData = res.data;
				this.$message({
					type: 'success',
					message: '搜索成功'
				});
			}else {
				this.$message({
					type: 'error',
					message: res.message
				});
			}
    },
		reset() {
			this.search.real_name = '';
			this.search.job_num = '';
			this.search.status = '';
    },
		add() {
			this.addFlag = true;
    },
  }
}
</script>
<style lang="scss" scoped>
.bg {
	padding: 30px 40px;
	.search {
		display: flex;
		.item {
			display: flex;
			align-items: center;
			margin-right: 30px;
		}
	}
}
</style>