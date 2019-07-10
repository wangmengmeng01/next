<template>
  <div class="bg">
		<div class="search">
			<div class="item">
				应用名称
				<el-input style="width:200px;margin-left:15px;" v-model.trim="search.app_name" placeholder="请输入"></el-input>
			</div>
			<div class="item">
				应用编码
			  <el-input style="width:200px;margin-left:15px;" v-model.trim="search.app_code" placeholder="请输入"></el-input>
			</div>
			<div class="item">
				应用状态
				<el-select style="width:200px;margin-left:15px;" v-model="search.app_status" placeholder="请选择应用状态">
					<el-option
						v-for="item in [{key:0 ,value:'已关闭'},{key:1 ,value:'已开启'}]"
						:key="item.key"
						:label="item.value"
						:value="item.key">
					</el-option>
				</el-select>
			</div>
		</div>
	  <div style="margin-top: 30px;display: flex;width: 100%;justify-content: space-between;">
			<div>
				<el-button @click.native="look" type="primary">查询</el-button>
				<el-button @click.native="reset" type="primary">重置</el-button>
			</div>
			<div>
				<el-button @click.native="get(1)" type="primary">更新</el-button>
			</div>
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
					prop="app_name"
					label="应用名称"
					align="center"
					>
				</el-table-column>
				<el-table-column
					prop="app_code"
					label="应用编码"
					align="center"
					>
				</el-table-column>
				<el-table-column
					prop="remark"
					label="应用说明"
					align="center"
					>
				</el-table-column>
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
					prop="app_status"
					label="状态"
					align="center"
					>
					<template slot-scope="scope">
						<span>{{scope.row.status === 0 ? '已关闭' : '已开启'}}</span>
					</template>
				</el-table-column>
				<el-table-column
					fixed='right'
					label="操作"
					align="center"
					width="200px"
					>
					<template slot-scope="scope">
						<el-button @click="close(scope.row)" type="text" size="small">关闭</el-button>
						<el-button @click="open(scope.row)" type="text" size="small">开启 </el-button>
						<el-button @click="edit(scope.row)" type="text" size="small">修改</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div>
			<el-dialog
				title="修改"
				:visible.sync="editFlag"
				width="700px"
				:modal-append-to-body="false"
				:append-to-body="true"
				center>
				<el-form :model="editForm" :rules="rules" ref="editAppForm" label-width="100px"
				>
					<el-form-item label="应用编码" prop="app_code">
						<el-input v-model="editForm.app_code" placeholder="请输入"></el-input>
					</el-form-item>
					<el-form-item label="应用说明" prop="remark">
						<div style="height: 152px;overflow: auto;">
							<textarea v-model="editForm.remark" autocomplete="off" style="min-height: 150px;resize:none;" class="el-textarea__inner" placeholder="请输入"></textarea>
						</div>
					</el-form-item>
				</el-form>
				<span slot="footer">
					<el-button type="primary" @click="submitForm('editAppForm')">提交</el-button>
					<el-button type="primary" @click="resetForm('editAppForm')">重置</el-button>
					<el-button type="primary" @click="editFlag = false">取消</el-button>
				</span>
			</el-dialog>
		</div>
  </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'role',
  data() {
    return {
			rules: {
				app_code: [
					{ required: true, message: '请输入应用编码', trigger: 'blur' },
				],
				remark: [
					{ required: true, message: '请输入应用说明', trigger: 'blur' },
				],
			},
			editFlag:false,
			editForm: {
			},
			search: {
				app_name: '',
				app_code: '',
				app_status: '',
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
  	async get(flag) {
			let params = {
				act:'index',
			}
			let res = await this.$http.post(params,'app.php');
			if(res.message === "success") {
				this.tableData = res.data;
				if (flag) {
					this.$message({
						type: 'success',
						message: '更新成功'
					});
				}
			}else {
				this.$message({
					type: 'error',
					message: res.message
				});
			}
		},
		edit(row) {
			this.editForm = JSON.parse(JSON.stringify(row));
			this.editFlag = true;
		},
		resetForm(formName) {
			this.$refs[formName].resetFields();
		},
		submitForm(formName) {
			this.$refs[formName].validate(async (valid) => {
				if (valid) {
					if(formName === 'editAppForm') {
						this.editForm.act = 'edit';
						let res = await this.$http.post(this.editForm,'app.php');
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
			this.$confirm('请确认是否开启？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(async () => {
				let res = await this.$http.post({id: row.id, status: 1, act: 'status'},'app.php');
				if(res.message === "success") {
					this.get();
					this.$message({
						type: 'success',
						message: '关闭成功'
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
		close(row) {
			this.$confirm('请确认是否关闭？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(async () => {
				let res = await this.$http.post({id: row.id, status: 0, act: 'status'},'app.php');
				if(res.message === "success") {
					this.get();
					this.$message({
						type: 'success',
						message: '关闭成功'
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
			let res = await this.$http.post(this.search,'app.php');
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
			this.search.app_name = '';
			this.search.app_status = '';
			this.search.app_code = '';
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
		.item:last-child {
			display: flex;
			align-items: center;
			margin-right: 0px;
		}
	}
}
</style>