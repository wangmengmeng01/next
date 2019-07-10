'use strict'

import axios from 'axios'
import Vue from 'vue'
window.eventBus = new Vue();
axios.interceptors.request.use(config => {
  eventBus.$emit('loading',true);
  return config
}, error => {
  eventBus.$emit('error',false);
  return Promise.reject(error)
})

axios.interceptors.response.use(response => {
  eventBus.$emit('loading',false);
  // let res = {},body = {};
  // body.result = false;
  // body.remark = '';
  // if(response.data.success){
  //   res = response.data;
  // }else {
  //   body.remark = response.data.msg;
  //   res.body = body;
  // }
  return response.data
}, error => {
  eventBus.$emit('error',false);
  return Promise.reject(error.response)
})


export default {
  post (data,url) {
    // let params = {
    //   // "action":data.action,
    //   "version":"V1.2.0",
    //   "req_time":new Date().getTime(),
    //   // "data":data.data,
    //   "appid":'w8k2e8oscyk74stl',
    //   "token":'CIJBCHmDmKvJJDjrvMkLDNCh',
    //   "appver": "6.0.0.02",
    //   // "sign_method": "3DES",
    //   // "sign": "E91DD23BCE045A4C8A0C1004D999B268",
    // }
		let params = data;
    return axios({
      method: 'post',
      // baseURL: '/gateway/interface',
      baseURL: `/api/${url}`,
      // url:url,
      data: params,
      timeout: 15000,
      headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
      }
    }).then(
      (response) => {
        return response
      }
    )
  },
  get (params) {
    return axios({
      method: 'get',
      baseURL:'/gateway/interface',
      //baseURL: '/appserver/interface.do',
      params, // get 请求时带的参数
      timeout: 15000,
      headers: {
        'X-Requested-With': 'XMLHttpRequest'
      }
    }).then(
      (response) => {
        return response
      }
    )
  }
}
