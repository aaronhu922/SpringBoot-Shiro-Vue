<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('article:add')">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="vpsname" label="服务器名称" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="vpsipport" label="服务器地址" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="创建时间" width="170">
        <template slot-scope="scope">
          <span>{{scope.row.createTime}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="200" >
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow"
      :total="totalCount"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempVps" label-position="left" label-width="100px"
               style='width: 500px; margin-left:50px;'>
        <el-form-item label="名称">
          <el-input type="text" v-model="tempVps.vpsname">
          </el-input>
      </el-form-item>
      <el-form-item label="地址(IP:Port)">
          <el-input type="text" v-model="tempVps.vpsipport">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createVps">创 建</el-button>
        <el-button type="primary" v-else @click="updateVps">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 50,//每页条数
          name: ''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '创建服务器'
        },
        tempVps: {
          id: "",
          vpsname: "",
          vpsipport: ""
        }
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        //查询列表
        // if (!this.hasPerm('vps:list')) {
        //   return
        // }
        this.listLoading = true;
        this.api({
          url: "/vps/listVps",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showCreate() {
        //显示新增对话框
        this.tempVps.vpsname = "";
        this.tempVps.vpsipport = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        //显示修改对话框
        this.tempVps.id = this.list[$index].id;
        this.tempVps.vpsname = this.list[$index].vpsname;
        this.tempVps.vpsipport = this.list[$index].vpsipport;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createVps() {
        //保存Vps
        this.api({
          url: "/vps/addVps",
          method: "post",
          data: this.tempVps
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateVps() {
        //修改vps
        this.api({
          url: "/vps/updateVps",
          method: "post",
          data: this.tempVps
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
    }
  }
</script>
