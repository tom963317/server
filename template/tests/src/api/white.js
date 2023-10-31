import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/SysWhite/getList',
    method: 'get',
    params
  })
}

export function add(params) {
  return request({
    url: '/SysWhite/add',
    method: 'post',
    params
  })
}
export function update(params) {
  return request({
    url: '/SysWhite/update',
    method: 'post',
    params
  })
}

