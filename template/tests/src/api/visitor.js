import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/visitor/list',
    method: 'get',
    params
  })

}export function statics(params) {
  return request({
    url: '/visitor/statics',
    method: 'get',
    params
  })
}
