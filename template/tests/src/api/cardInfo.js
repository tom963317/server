import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/TbCardInfo/getList',
    method: 'get',
    params
  })
}
export function deleteFn(params) {
  return request({
    url: '/TbCardInfo/delete',
    method: 'post',
    params
  })
}
export function update(params) {
  return request({
    url: '/TbCardInfo/update',
    method: 'post',
    params
  })
}

