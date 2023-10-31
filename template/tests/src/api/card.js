import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/card/list',
    method: 'get',
    params
  })
}
export function getByUid(params) {
  return request({
    url: '/card/getByUid',
    method: 'get',
    params
  })
}

export function getSyncList(params) {
  return request({
    url: '/card/syncList',
    method: 'get',
    params
  })
}

export function postSyncAgree(params) {
  return request({
    url: '/card/syncAgree',
    method: 'get',
    params
  })
}
export function deleteData(params) {
  return request({
    url: '/card/delete',
    method: 'post',
    params
  })
}
export function getToken() {
  return request({
    url: '/card/getToken',
    method: 'get'
  })
}
export function setSetting(params) {
  return request({
    url: '/card/setSetting',
    method: 'post',
    params
  })
}
export function getSetting() {
  return request({
    url: '/card/getSetting',
    method: 'get'
  })
}
export function updateSetting(params) {
  return request({
    url: '/card/updateSetting',
    method: 'post',
    params
  })
}
