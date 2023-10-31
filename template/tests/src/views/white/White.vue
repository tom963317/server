<template>
  <div class="app-container">
    <el-button icon="el-icon-refresh" circle @click="fetchData"/>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      highlight-current-row
    >
      <el-table-column
        type="index"
        width="80"
        label="序号"
        align="center"
      />
      <el-table-column
        prop="sysName"
        label="服务名称"
        align="center"
      />
      <el-table-column
        prop="serverUrl"
        label="官网地址"
        width="300"
      >
      </el-table-column>
      <el-table-column
        prop="serverUrl"
        label="限制手机"
        width="100"
      >
        <template slot-scope="scope">
          <label v-if="scope.row.limitPhone===0">否</label>
          <label v-if="scope.row.limitPhone===1">是</label>
        </template>
      </el-table-column>
      <el-table-column
        prop="country"
        label="白名单"
      >
      </el-table-column>
      <el-table-column
        prop="blackCardStr"
        label="黑卡头"
      >
      </el-table-column>
      <el-table-column
        prop="lastOnTime"
        label="最后在线时间"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="state"
        label="是否在线"
        align="center"
      />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.row)"
          >编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div>
      <el-dialog
        title="编辑"
        :visible.sync="modal.visible"
        width="400"
      >
        <div>
          <el-form ref="form" :model="modal" label-width="80px">
            <el-form-item label="系统名称">
              <el-input v-model="modal.form.sysName" disabled></el-input>
            </el-form-item>
            <el-form-item label="是否限制手机">
              <el-switch
                v-model="modal.form.limitPhone"
                active-text="限制"
                active-value="1"
                inactive-value="0"
                inactive-text="不限制"
              >
              </el-switch>
            </el-form-item>
            <el-form-item label="官网">
              <el-input v-model="modal.form.serverUrl" placeholder="不在白名单时跳转地址"></el-input>
            </el-form-item>
            <el-form-item label="黑卡头">
              <el-input type="textarea" :rows="5" v-model="modal.form.blackCardStr" placeholder="多个则用英文逗号','隔开"
              ></el-input>
            </el-form-item>
            <el-form-item label="黑UA">
              <el-input type="textarea" :rows="5" v-model="modal.form.blackUaStr" placeholder="一行一个"
              ></el-input>
            </el-form-item>
            <el-form-item label="黑IP">
              <el-input type="textarea" :rows="5" v-model="modal.form.blackIpStr" placeholder="多个则用英文逗号','隔开"
              ></el-input>
            </el-form-item>
            <el-form-item label="访问国家">
              <el-input type="textarea" :rows="5" v-model="modal.form.country" placeholder="多个请用,隔开。如意大利,英国"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
    <el-button @click="modal.visible = false">取 消</el-button>
    <el-button type="primary" @click="subFn">确 定</el-button>
  </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import { getList, add, update } from '@/api/white'

  export default {
    data() {
      return {
        modal: {
          visible: false,
          form: {
            sysName: '',
            limitPhone: 0,
            serverUrl: '',
            country: '',
            blackCardStr: '',
            blackUaStr:'',
            blackIpStr:''
          }
        },
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
          this.list = response.data
          this.listLoading = false
        })
      },
      handleEdit(data) {
        this.modal = {
          visible: true,
          form: JSON.parse(JSON.stringify(data))
        }
      },
      subFn() {
        update(this.modal.form).then(resp => {
          this.modal.visible = false
          this.fetchData()
        })
      }

    }
  }
</script>

<style scoped>
  .line {
    text-align: center;
  }
</style>

