import request from '@/router/axios';
import { baseUrl } from '@/config/env';
export const refreshToken = (refresh_token, tenantId) => request({
  url: '/api/blade-auth/oauth/token',
  method: 'post',
  headers: {
    'Tenant-Id': tenantId
  },
  params: {
    tenantId,
    refresh_token,
    grant_type: "refresh_token",
    scope: "all",
  }
})

export const getButtons = (subsystemId) => request({
  url: '/api/blade-system/menu/buttons',
  method: 'get',
  params: {
    subsystemId,
  }
});
export const getRoutes = (subsystemId) => request({
  url: '/api/blade-system/menu/routes',
  method: 'get',
  params: {
    subsystemId,
  }
});

export const getUserInfo = () => request({
  url: '/api/blade-user/user-info',
  method: 'get'
});

export const logout = () => request({
  url: baseUrl + '/user/logout',
  method: 'get'
})
