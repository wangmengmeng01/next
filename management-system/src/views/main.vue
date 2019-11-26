 <template>
  <div class="bg">
  	<el-container>
		  <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
		    <el-menu
		    	:unique-opened="false"
		    	:default-active="activeMenu"
		    	:default-openeds="showArr"
          active-text-color="#F56C6C"
        >
					<my-side v-for="(item,i) in option" :item="item" :base="item.path"
					:key="item.path" popper-append-to-body></my-side>
		    </el-menu>
		  </el-aside>

		  <el-container>
		    <el-main>
		    	<div>
		    		<el-breadcrumb separator="/">
						  <el-breadcrumb-item v-for="(item) in breadcrumb">{{item}}</el-breadcrumb-item>
						</el-breadcrumb>
		    	</div>
          {{activeMenu}}
		    	<el-alert>222222222</el-alert>
		      <router-view :key="key"></router-view>
		    </el-main>
		  </el-container>
		</el-container>
  </div>
</template>

<script>
// @ is an alias to /src
import mySide from '@/components/mySide.vue'
// console.log(mySide)
export default {
  name: 'bbbb',
  components:{
  	mySide
  },
  data() {
    return {
    	tableData: [],
    	breadcrumb:[],
    	showArr:[],
    }
  },
  computed: {
  	option() {
  		return this.$router.options.routes;
  	},
		key() {
      return this.$route.path
    },
		activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      // if (meta.activeMenu) {
      //   return meta.activeMenu
      // }
      console.log(this.$route.path,6)
      return this.$route.path
    },
  },
  watch:{
    $route(to,from) {
    	this.breadcrumb = [];
    	this.showArr = [];
      console.log(this.$route,'this.$route')
      this.$route.matched.forEach((item) => {
      	this.breadcrumb.push(item.meta.title)
      	this.showArr.push(item.path)
      })
      console.log(this.showArr)
    },
  },
  created() {
  	// this.option = this.$router.options.routes;
  	// console.log(this.$router,'this.$router')
  	// console.log(this.$route,'this.$route')
  },
  methods: {
    get(i) {
    }
  }
}
</script>
<style lang="scss" scoped>

</style>