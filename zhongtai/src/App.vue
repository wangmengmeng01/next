<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<script type="text/javascript">
export default {
  data() {
    return {
      flag: false,
    }
  },
  mounted() {
    let loading = this.$loading({
			lock: true,
			text: '拼命加载中',
			spinner: 'el-icon-loading',
			background: 'rgba(0,0,0,.7)'
		});
		loading.close();
    eventBus.$on('error',() => {
			loading.close();
      this.$alert('网络异常', {
        confirmButtonText: '确定',
      });
    })
    eventBus.$on('loading',(flag) => {
      if(flag) {
        loading = this.$loading({
          lock: true,
          text: '拼命加载中',
          spinner: 'el-icon-loading',
          background: 'rgba(0,0,0,.7)'
        });
      }else {
        loading.close();
      }
    })
  }
}
</script>
<style lang="scss">
#app {
}
</style>
<style type="text/css">
body,html,#app {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: Arial;
}
</style>