import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    loadingFlag: true
  },
  mutations: {
    setLoading(content,flag) {
      content.loadingFlag = flag;
    }
  },
  actions: {

  }
})
