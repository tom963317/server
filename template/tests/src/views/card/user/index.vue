<template>
  <div class="app-container">
    <div style="display: flex">
      <div style="margin-left: 20px">
        <el-button type="primary" icon="el-icon-plus" @click="addUser">搜索
        </el-button>
        <el-button type="info" icon="el-icon-refresh" @click="p.carPrefix='';fetchData()">重置</el-button>
      </div>
    </div>
    <el-table
      style="margin-top: 20px"
      v-loading="listLoading"
      :data="dataList"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        type="index"
        label="序号"
        width="55"
      >
      </el-table-column>
      <el-table-column label="用户名" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center">
        <template v-slot="scope">
          <label>{{scope.row.createTime }}</label>
        </template>
      </el-table-column>
      <el-table-column label="上次登录时间" align="center">
        <template v-slot="scope">
          <label>{{scope.row.loginTime }}</label>
        </template>
      </el-table-column>
      <el-table-column label="登录IP" align="center">
        <template v-slot="scope">
          <label>{{scope.row.loginIp }}</label>
        </template>
      </el-table-column>
      <el-table-column label="登录次数" align="center">
        <template v-slot="scope">
          <label>{{scope.row.loginCount }}</label>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center">
        <template slot-scope="scope">
          <el-button type="info" size="mini" @click="editFn(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="deleteData(scope.row)">删除</el-button>
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
    <el-dialog title="编辑" :visible="modal.visible" width="500px">
      <el-form size="mini">
        <div class="c-item">
          <label class="c-label">登录名：</label>
          <el-input style="width: 200px" :disabled="modal.isEdit" v-model="modal.form.name"></el-input>
        </div>
        <div class="c-item">
          <label class="c-label">密码：</label>
          <el-input style="width: 200px" type="password" v-model="modal.form.password"></el-input>
        </div>
        <div class="c-item">
          <label class="c-label">确认密码：</label>
          <el-input style="width: 200px" type="password" v-model="modal.form.againPassword"></el-input>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
					<el-button @click="modal.visible=false">关闭</el-button>
					<el-button type="primary" @click="saveFn">保存</el-button>
				</span>
    </el-dialog>
    <el-dialog
      title="警告"
      :visible.sync="show.visible"
      width="300px"
    >
      <span>{{show.content}}</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="show.visible = false">取 消</el-button>
    <el-button type="primary" @click="sureDeleteFn">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
  import { getList, deleteFn,update,add } from '@/api/user'

  export default {
    data() {
      return {
        show: {
          visible: false,
          content: '',
          id: ''
        },
        modal: {
          visible: false,
          isEdit: false,
          form: {
            name: '',
            password: '',
            againPassword: ''
          }
        },
        p: {
          cardFilter: 0,
          pageNo: 1,
          pageSize: 20,
          dataCount: 0,
          carPrefix: ''
        },
        dataList: [],
        listLoading: true
      }
    },
    mounted() {
      this.fetchData()
    },
    methods: {
      editFn(data) {
        Object.assign(this.modal, {
          visible: true,
          isEdit: true,
          form: JSON.parse(JSON.stringify(data))
        })
      },
      saveFn() {
        let data = this.modal.form
        if (!data.name) {
          this.$message.error('填写用户名')
          return
        }
        if (!data.password || data.password.length < 6) {
          this.$message.error('输入密码或密码长度小于6')
          return
        }
        if (data.password!==data.againPassword){
          this.$message.error('两次输入的密码不一致')
          return;
        }
        data.password2=data.password;
      if (this.modal.isEdit){
        update(data).then(resp => {
          this.modal.visible = false
          this.fetchData()
        })
      } else {
        add(data).then(resp => {
          this.modal.visible = false
          this.fetchData()
        })
      }

      },
      deleteData(data) {
        this.show = {
          content: '是否删除【' + data.name + '】账号?',
          visible: true,
          id: data.id
        }
      },
      sureDeleteFn() {
        deleteFn({ id: this.show.id }).then(resp => {
          this.show.visible = false
          this.fetchData()
        })
      },
      addUser() {
        Object.assign(this.modal, {
          visible: true,
          isEdit: false,
          form: {
            name: '',
            password: ''
          }
        })
      },
      fetchData() {
        this.listLoading = true
        getList(this.p).then(response => {
          this.listLoading = false
          this.dataList = response.data
          this.p.pageNo = response.pageNo
          this.p.pageSize = response.pageSize
          this.p.dataCount = response.dataCount
        })
      },
      handleSizeChange(val) {
        this.p.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.p.pageNo = val
        this.fetchData()
      }
    }
  }
</script>
<style>

  /* 内容-item */
  .c-item {
    min-width: 270px;
    min-height: 32px;
    line-height: 32px;
    padding-right: 10px;
    display: inline-block;
    margin: 0.5em 0;
  }

  .c-item.br {
    display: block;
    margin: 14px 0;
  }

  /* label样式 */
  .c-item .c-label {
    width: 7em;
    color: #333;
    padding-right: 4px;
    display: inline-block;
    text-align: right;
    vertical-align: top;
  }

  /* input宽度等样式调整 */
  .c-item .el-input {
    width: 178px;
  }

  /* 禁用input的样式 */
  .c-item .el-input.is-disabled .el-input__inner {
    color: #999;
  }
</style>
