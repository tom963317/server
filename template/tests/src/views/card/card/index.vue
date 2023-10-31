<template>
  <div class="app-container">
    <div style="display: flex">
      <el-input type="text" v-model="p.carPrefix" placeholder="输入卡头关键字搜索" style="width: 200px"></el-input>
      <div style="margin-left: 20px">
        <el-button type="primary" icon="el-icon-search" @click="fetchData">搜索
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
      <el-table-column label="卡头" align="center">
        <template slot-scope="scope">
          {{ scope.row.carPrefix }}
        </template>
      </el-table-column>
      <el-table-column label="卡种类" align="center">
        <template v-slot="scope">
          <label>{{scope.row.cardType }}</label>
        </template>
      </el-table-column>
      <el-table-column label="储蓄/信用卡" align="center">
        <template v-slot="scope">
          <label>{{scope.row.debit }}</label>
        </template>
      </el-table-column>
      <el-table-column label="卡等级" align="center">
        <template v-slot="scope">
          <label>{{scope.row.cardLevel }}</label>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center">
        <template v-slot="scope">
          <label>{{scope.row.remark }}</label>
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
          <label class="c-label">卡头：</label>
          <el-input style="width: 200px"  v-model="modal.form.carPrefix"></el-input>
        </div>
        <div class="c-item">
          <label class="c-label">类型：</label>
          <el-input style="width: 200px"  v-model="modal.form.cardType"></el-input>
        </div>

        <div class="c-item">
          <label class="c-label">储蓄/信用卡：</label>
          <el-input style="width: 200px"  v-model="modal.form.debit"></el-input>
        </div>
        <div class="c-item">
          <label class="c-label">等级：</label>
          <el-input style="width: 200px"  v-model="modal.form.cardLevel"></el-input>
        </div>
        <div class="c-item">
          <label class="c-label">备注：</label>
          <el-input style="width: 200px"  v-model="modal.form.remark"></el-input>
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
  import { getList,deleteFn ,update} from '@/api/cardInfo'

  export default {
    data() {
      return {
        show:{
          visible:false,
          content:'',
          id:''
        },
        modal:{
          visible:false,
          form:{
            carPrefix:'',
            cardType:'',
            debit:'',
            cardLevel:'',
            remark:''
          }
        },
        p: {
          cardFilter: 0,
          pageNo: 1,
          pageSize: 20,
          dataCount: 0,
          carPrefix:''
        },
        dataList: [],
        listLoading: true
      }
    },
    mounted() {
      this.fetchData()
    },
    methods: {
      editFn(data){
        Object.assign(this.modal,{
          visible:true,
          form: JSON.parse(JSON.stringify(data))
        })
      },
      saveFn(){
        update(this.modal.form).then(resp=>{
          this.modal.visible=false;
          this.fetchData();
        })
      },
      deleteData(data){
        this.show={
          content:'是否删除：'+data.carPrefix+'?',
          visible:true,
          id:data.id
        }
      },
      sureDeleteFn(){
        deleteFn({id:this.show.id}).then(resp=>{
          this.show.visible=false;
          this.fetchData();
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
      },
    }
  }
</script>
<style >

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
