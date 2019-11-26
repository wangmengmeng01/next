<template>
  <div class="bg" v-if="!item.hidden">
  	<router-link v-if="!item.children" :to="resolveP()">
			<el-menu-item :index="resolveP()">
				<svg-icon iconClass="date"></svg-icon>
				<span class="name">{{item.meta.title+resolveP()}}</span>
			</el-menu-item>
  	</router-link>
		<el-submenu v-else :index="resolveP()">
      <template slot="title" >
	      <svg-icon iconClass="date"></svg-icon>
				<span class="name">{{item.meta.title}}</span>
	    </template>
			<mySide v-for="(t,i) in item.children" :item="t" :key="t.path" :base="resolveP(t.path)"></mySide>
    </el-submenu>
  </div>
</template>

<script>
// @ is an alias to /src
import path from 'path'
import fs from 'fs'
console.log(path,'path')
console.log(fs,'fs')
export default {
  name: 'mySide',
  props: {
  	item: {
  		required:true,
  	},
  	base: {
  		default: '',
  		required:false,
  	}
  },
  data() {
    return {
    }
  },
  computed: {
  },
  components: {
  },
	watch: {
  },
  created() {
  	this.option = this.$router.options.routes;
  },
  mounted() {
  	// this.get()

  },
  methods: {
    get(child) {
	    	fs.writeFile(__dirname + '/a.txt', new Date(), (error) => {
		  console.log(error);
		});
    },
    resolveP(base) {
    	if(!base) base = ''
    	return path.resolve(this.base,base)
    }
  }
}
</script>
<style lang="scss" scoped>
.name {
	margin-left: 10px;
	// color: #303133;
}
</style>