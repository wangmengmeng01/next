import { setToken, setRefreshToken, removeToken, removeRefreshToken } from '@/util/auth'
import { setStore, getStore } from '@/util/store'
import { isURL, validatenull } from '@/util/validate'
import { deepClone } from '@/util/util'
import webiste from '@/config/website'
import { getUserInfo, logout, refreshToken, getRoutes, getButtons } from '@/api/user'

function addPath(ele, first) {
  const menu = webiste.menu;
  const propsConfig = menu.props;
  const propsDefault = {
    label: propsConfig.label || 'name',
    path: propsConfig.path || 'path',
    icon: propsConfig.icon || 'icon',
    children: propsConfig.children || 'children'
  }
  const icon = ele[propsDefault.icon];
  ele[propsDefault.icon] = validatenull(icon) ? menu.iconDefault : icon;
  const isChild = ele[propsDefault.children] && ele[propsDefault.children].length !== 0;
  if (!isChild) ele[propsDefault.children] = [];
  if (!isChild && first && !isURL(ele[propsDefault.path])) {
    ele[propsDefault.path] = ele[propsDefault.path] + '/index'
  } else {
    ele[propsDefault.children].forEach(child => {
      addPath(child);
    })
  }

}

const user = {
  state: {
    tenantId: getStore({ name: 'tenantId' }) || '',
    userInfo: getStore({ name: 'userInfo' }) || [],
    permission: getStore({ name: 'permission' }) || {},
    roles: [],
    menu: getStore({ name: 'menu' }) || [],
    menuAll: [],
    token: getStore({ name: 'token' }) || '',
    refreshToken: getStore({ name: 'refreshToken' }) || '',
    subsystemId: getStore({ name: 'subsystemId' }) || '',
  },
  actions: {
    // 获取button
    GetButtons({ commit }, subsystemId) {
      return new Promise((resolve) => {
        getButtons(subsystemId).then(res => {
          const data = res.data.data;
          commit('SET_PERMISSION', data);
          resolve();
        })
      })
    },
    //获取用户信息
    GetUserInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getUserInfo().then((res) => {
          const data = res.data.data;
          commit('SET_ROLES', data.roles);
          resolve(data);
        }).catch(err => {
          reject(err);
        })
      })
    },
    //刷新token
    refreshToken({ state, commit }) {
      console.log('handle refresh token')
      return new Promise((resolve, reject) => {
        refreshToken(state.refreshToken, state.tenantId).then(res => {
          const data = res.data;
          commit('SET_TOKEN', data.access_token);
          commit('SET_REFRESH_TOKEN', data.refresh_token);
          resolve();
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    LogOut({ commit }) {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          commit('SET_TOKEN', '')
          commit('SET_MENU', [])
          commit('SET_ROLES', [])
          commit('DEL_ALL_TAG');
          commit('CLEAR_LOCK');
          removeToken()
          removeRefreshToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    //注销session
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        commit('SET_MENU', [])
        commit('SET_ROLES', [])
        commit('DEL_ALL_TAG');
        commit('CLEAR_LOCK');
        removeToken()
        removeRefreshToken()
        resolve()
      })
    },
    //获取系统菜单
    GetMenu({ commit, dispatch }, subsystemId = getStore({ name: 'subsystemId' })) {
      return new Promise(resolve => {
        getRoutes(subsystemId).then((res) => {
          const data = res.data.data
          let menu = deepClone(data);
          menu.forEach((ele) => {
            addPath(ele, true);
          })
          commit('SET_MENU', menu)
          commit('SET_SUBSYSTEM_ID', subsystemId)
          dispatch('GetButtons', subsystemId);
          resolve(menu)
        })
      })
    },
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      setToken(token)
      state.token = token;
      setStore({ name: 'token', content: state.token, type: 'session' })
    },
    SET_REFRESH_TOKEN: (state, refreshToken) => {
      setRefreshToken(refreshToken)
      state.refreshToken = refreshToken;
      setStore({ name: 'refreshToken', content: state.refreshToken, type: 'session' })
    },
    SET_TENANT_ID: (state, tenantId) => {
      state.tenantId = tenantId;
      setStore({ name: 'tenantId', content: state.tenantId, type: 'session' })
    },
    SET_SUBSYSTEM_ID: (state, subsystemId) => {
      state.subsystemId = subsystemId;
      setStore({ name: 'subsystemId', content: state.subsystemId, type: 'session' })
    },
    SET_USER_INFO: (state, userInfo) => {
      state.userInfo = userInfo;
      setStore({ name: 'userInfo', content: state.userInfo })
    },
    SET_MENU: (state, menu) => {
      state.menu = menu
      setStore({ name: 'menu', content: state.menu, type: 'session' })
    },
    SET_MENU_ALL: (state, menuAll) => {
      state.menuAll = menuAll;
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles;
    },
    SET_PERMISSION: (state, permission) => {
      let result = [];

      function getCode(list) {
        list.forEach(ele => {
          if (typeof (ele) === 'object') {
            const chiildren = ele.children;
            const code = ele.code;
            if (chiildren) {
              getCode(chiildren)
            } else {
              result.push(code);
            }
          }

        })
      }

      getCode(permission);
      state.permission = {};
      result.forEach(ele => {
        state.permission[ele] = true;
      });
      setStore({ name: 'permission', content: state.permission, type: 'session' })
    }
  }

}
export default user
