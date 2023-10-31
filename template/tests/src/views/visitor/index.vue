<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border

      highlight-current-row
    >
      <el-table-column
        width="40px"
        prop="id"
        label="ID"
      />
      <el-table-column
        width="180px"
        prop="ip"
        label="IP地址"
      />

      <el-table-column
        width="120px"
        prop="ua"
        label="设备类型"
      >
        <template v-slot="scope">
          <el-tag>
            {{ UAJiex(scope.row.ua) }}
          </el-tag>

        </template>
      </el-table-column>
      <el-table-column
        width="150px"
        prop="start_time"
        label="最早访问时间"
      >
        <template v-slot="scope">
          <el-tag
            effect="plain"
          >

            {{ ToDate(scope.row.start_time) }}
          </el-tag>

        </template>
      </el-table-column>
      <el-table-column
        width="150px"
        prop="end_time"
        label="最后访问时间"
      >
        <template v-slot="scope">
          <el-tag
            type="success"
            effect="plain"
          >

            {{ ToDate(scope.row.end_time) }}
          </el-tag>

        </template>
      </el-table-column>
      <el-table-column
        width="80px"
        prop="count"
        label="总访问次数"
      />
      <el-table-column
        prop="ua"
        label="UA"
      />
    </el-table>
  </div>
</template>

<script>
import { getList } from '@/api/visitor'

export default {
  data() {
    return {
      list: null,
      listLoading: true
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getList().then(response => {
        this.list = response.data.data
        console.log(response.data.data)
        this.listLoading = false
      })
    },
    ToDate(time) {
      var date = new Date(parseInt(time))
      return `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`
    },
    UAJiex(ua) {
      if (ua.toString().indexOf('Mac') !== -1) {
        return 'Mac'
      }
      if (ua.toString().indexOf('Android') !== -1) {
        return 'Android'
      }
      if (ua.toString().indexOf('iPhone') !== -1) {
        return 'iPhone'
      }
      if (ua.toString().indexOf('Windows') !== -1) {
        return 'Windows'
      } else {
        return '其他设备'
      }
    }
  }
}
</script>

<style scoped>
.line {
  text-align: center;
}
</style>

