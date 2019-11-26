<template>
  <div id="app">
    <myMain></myMain>
    <!-- <router-view :key="key"></router-view> -->
  </div>
</template>

<script>
// @ is an alias to /src
import myMain from './views/main.vue'
export default {
  name: 'wwww',
  components: {
    myMain
  },
  props: {
  },
  data() {
    return {
    }
  },
  computed: {
    key() {
      return this.$route.path
    }
  },
  watch: {
  },
  created() {
    this.option = this.$router.options.routes;
  },
  methods: {
    get(child) {
      if(child.length) {

      }
    }
  }
}
</script>
<style lang="scss" scoped>
#app {
  height: 100%;
  box-sizing: border-box;
}
</style>
