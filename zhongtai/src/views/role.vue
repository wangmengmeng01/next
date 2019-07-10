<template>
  <div class="bg">
		<div class="search">
			<div class="item">
				角色
				<el-input style="width:200px;margin-left:15px;" v-model.trim="search.role_name" placeholder="请输入"></el-input>
			</div>
			<!-- <div class="item">
				权限状态
			  <el-input style="width:200px;margin-left:15px;" v-model.trim="search.num" placeholder="请输入权限状态"></el-input>
			</div> -->
			<div class="item">
				权限状态
				<el-select style="width:200px;margin-left:15px;" v-model="search.status" placeholder="请选择">
					<el-option
						v-for="item in [{key:0 ,value:'未分配权限'},{key:1 ,value:'已分配权限'}]"
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
					prop="role_name"
					label="角色名称"
					align="center"
					>
				</el-table-column>
				<el-table-column
					prop="remark"
					label="角色说明"
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
					prop="status"
					label="权限状态"
					align="center"
					>
					<template slot-scope="scope">
						<span>{{scope.row.status === 0 ? '未分配权限' : '已分配权限'}}</span>
					</template>
				</el-table-column>
				<el-table-column
					fixed='right'
					label="操作"
					align="center"
					width="200px"
					>
					<template slot-scope="scope">
						<el-button @click="edit(scope.row)" type="text" size="small">修改    </el-button>
						<el-button @click="go(scope.row)" type="text" size="small">权限分配     </el-button>
						<el-button @click="del(scope.row)" type="text" size="small">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div>
			<el-dialog
				title="选择权限"
				:visible.sync="treeFlag"
				width="700px"
				:modal-append-to-body="false"
				:append-to-body="true"
				@close="$refs.tree.setCheckedKeys([]);"
				center>
				<div style="max-height: 500px;">
					<el-tree
						:data="treeData"
						show-checkbox
						default-expand-all
						node-key="id"
						ref="tree"
						highlight-current
						:props="defaultProps">
					</el-tree>
				</div>
				<span slot="footer">
					<el-button type="primary" @click="submitTree()">提交</el-button>
					<!-- <el-button type="primary" @click="resetForm('addRoleForm')">重置</el-button> -->
					<el-button type="primary" @click="treeFlag = false">取消</el-button>
				</span>
			</el-dialog>
			<el-dialog
				title="角色添加"
				:visible.sync="addFlag"
				width="700px"
				:modal-append-to-body="false"
				:append-to-body="true"
				@close="resetForm('addRoleForm')"
				center>
				<el-form :model="addForm" :rules="rules" ref="addRoleForm" label-width="80px" class="demo-ruleForm">
					<el-form-item label="名称" prop="role_name">
						<el-input v-model.trim="addForm.role_name" placeholder="请输入"></el-input>
					</el-form-item>
					<el-form-item label="角色说明" prop="remark">
						<div style="height: 152px;overflow: auto;">
							<textarea v-model.trim="addForm.remark" autocomplete="off" style="min-height: 150px;resize:none;" class="el-textarea__inner" placeholder="请输入"></textarea>
						</div>
					</el-form-item>
				</el-form>
				<span slot="footer">
					<el-button type="primary" @click="submitForm('addRoleForm')">提交</el-button>
					<el-button type="primary" @click="resetForm('addRoleForm')">重置</el-button>
					<el-button type="primary" @click="addFlag = false">取消</el-button>
				</span>
			</el-dialog>
			<el-dialog
				title="角色修改"
				:visible.sync="editFlag"
				width="700px"
				:modal-append-to-body="false"
				:append-to-body="true"
				center>

				<el-form :model="editForm" :rules="rules" ref="editRoleForm" label-width="80px" class="demo-ruleForm">
					<el-form-item label="名称" prop="role_name">
						<el-input v-model.trim="editForm.role_name" placeholder="请输入"></el-input>
					</el-form-item>
					<el-form-item label="角色说明" prop="remark">
						<div style="height: 152px;overflow: auto;">
							<textarea v-model.trim="editForm.remark" autocomplete="off" style="min-height: 150px;resize:none;" class="el-textarea__inner" placeholder="请输入"></textarea>
						</div>
					</el-form-item>
				</el-form>
				<span slot="footer">
					<el-button type="primary" @click="submitForm('editRoleForm')">提交</el-button>
					<el-button type="primary" @click="resetForm('editRoleForm')">重置</el-button>
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
			treeFlag: false,
			treeData: [],
			  defaultProps: {
			    children: 'status',
			    label: 'role_name'
			  },
			rules: {
				role_name: [
					{ required: true, message: '请输入名称', trigger: 'blur' },
				],
				remark: [
					{ required: true, message: '请输入角色说明', trigger: 'blur' },
				],
			},
			editFlag:false,
			editForm: {
				role_name: '',
				remark: '',
			},
			addForm: {
				role_name: '',
				remark: '',
			},
			addFlag:false,
			search: {
				role_name: '',
				status: '',
			},
			tableData: []
    }
  },
  components: {
  },
  created() {
		this.get()
		this.getTree()
  },
  methods: {
		submitForm(formName) {
			this.$refs[formName].validate(async (valid) => {
				if (valid) {
					if(formName === 'addRoleForm') {
						this.addForm.act = 'create';
						let res = await this.$http.post(this.addForm,'role.php');
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
					if(formName === 'editRoleForm') {
						this.editForm.act = 'edit';
						let res = await this.$http.post(this.editForm,'role.php');
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
		resetForm(formName) {
			this.$refs[formName].resetFields();
		},
		async get() {
			let res = await this.$http.post(Object.assign({},{act:'index',}),'role.php');
			if(res.message === "success") {
				this.tableData = res.data;
			}else {
				this.$message({
					type: 'error',
					message: res.message
				});
			}
		},
		async getTree() {
			let res = await this.$http.post(Object.assign({},{act:'powers',}),'role.php');
			if(res.message === "success") {
				this.treeData = res.data;
			}else {
				this.$message({
					type: 'error',
					message: res.message
				});
			}
		},
		async submitTree() {
			let arr = this.$refs.tree.getCheckedKeys();
			// this.$refs.tree.setCheckedKeys([]);
			this.editForm.act = 'edit';
			let res = await this.$http.post(arr,'role.php');
			if(res.message === "success") {
				this.treeFlag = false;
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
		},
		go(row) {
			this.treeFlag = true;
		},
		edit(row) {
			this.editForm = JSON.parse(JSON.stringify(row));
			this.editFlag = true;
		},
		del(row) {
			console.log(row);
			this.$confirm('删除后该角色所有用户都将成为默认用户，请确认是否删除？', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(async () => {
				let res = await this.$http.post({id: row.id, act: 'delete'},'role.php');
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
    async look() {
			let res = await this.$http.post(Object.assign({},this.search,{act:'index',}),'role.php');
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
			this.search.role_name = '';
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