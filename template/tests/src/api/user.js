import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/AccAdmin/login/',
    method: 'post',
    data
  })
}
export function getList(data) {
  return request({
    url: '/admin/getList/',
    method: 'post',
    data
  })
}
export function deleteFn(data) {
  return request({
    url: '/admin/delete',
    method: 'post',
    data
  })
}
export function update(data) {
  return request({
    url: '/admin/update',
    method: 'post',
    data
  })
}
export function add(data) {
  return request({
    url: '/admin/addAccount',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/AccAdmin/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/AccAdmin/doExit',
    method: 'post'
  })
}
