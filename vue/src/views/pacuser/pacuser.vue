<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('article:add')">添加</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      :data="list"
      v-loading.body="listLoading"
      element-loading-text="拼命加载中"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="username" label="用户名" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="userphone" label="用户电话" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="wxname" label="其他联系方式" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="websites" label="购买的产品" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="proxyserver" label="代理服务器" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="comments" label="备注" style="width: 60px;"></el-table-column>

      <el-table-column align="center" label="创建时间" width="170">
        <template slot-scope="scope">
          <span>{{scope.row.createTime}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="200">
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
      layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        class="small-space"
        :model="tempPacuser"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="用户名">
          <el-input type="text" v-model="tempPacuser.username"></el-input>
        </el-form-item>
        <el-form-item label="用户电话">
          <el-input type="text" v-model="tempPacuser.userphone"></el-input>
        </el-form-item>
        <el-form-item label="其他联系方式">
          <el-input type="text" v-model="tempPacuser.wxname"></el-input>
        </el-form-item>
        <el-form-item label="购买的产品">
          <el-select v-model="tempPacuser.websites" multiple placeholder="请选择">
            <el-option
              v-for="item in websiteslist"
              :key="item.id"
              :label="item.websitename"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="代理服务器">
          <el-select v-model="tempPacuser.proxyserver" clearable placeholder="请选择">
            <el-option
              v-for="item in vpslist"
              :key="item.id"
              :label="item.vpsname"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="text" v-model="tempPacuser.comments"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createPacuser">创 建</el-button>
        <el-button type="primary" v-else @click="updatePacuser">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      totalCount: 0, //分页组件--数据总条数
      list: [], //表格的数据
      listLoading: false, //数据加载等待动画
      listQuery: {
        pageNum: 1, //页码
        pageRow: 50, //每页条数
        name: ""
      },
      dialogStatus: "create",
      dialogFormVisible: false,
      textMap: {
        update: "编辑",
        create: "创建新用户"
      },
      tempPacuser: {
        id: "",
        username: "",
        userphone: "",
        wxname: "",
        websites: [],
        proxyserver: "",
        comments: ""
      },
      vpslist: [
        {
          value: "1",
          label: "阿里01-佛罗里达"
        },
        {
          value: "2",
          label: "阿里02-俄勒冈"
        }
      ],
      websiteslist: []
    };
  },
  created() {
    this.getList();
    this.getWebsiteList();
    this.getVpsList();
  },
  methods: {
    getList() {
      //查询列表
      // if (!this.hasPerm('pacuser:list')) {
      //   return
      // }
      // this.listLoading = true;
      // this.api({
      //   url: "/pacuser/listPacuser",
      //   method: "get",
      //   params: this.listQuery
      // }).then(data => {
      //   this.listLoading = false;
      //   this.list = data.list;
      //   this.totalCount = data.totalCount;
      // })
    },
    getWebsiteList() {
      this.api({
          url: "/website/listWebsite",
          method: "get",         
        }).then(data => {
          this.websiteslist = data.list;
        })
    },
    getVpsList() {        
        this.api({
          url: "/vps/listVps",
          method: "get",
        }).then(data => {
          this.vpslist = data.list;
        })
      },
    handleSizeChange(val) {
      //改变每页数量
      this.listQuery.pageRow = val;
      this.handleFilter();
    },
    handleCurrentChange(val) {
      //改变页码
      this.listQuery.pageNum = val;
      this.getList();
    },
    getIndex($index) {
      //表格序号
      return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1;
    },
    showCreate() {
      //显示新增对话框
      this.tempPacuser.username = "";
      this.dialogStatus = "create";
      this.dialogFormVisible = true;
    },
    showUpdate($index) {
      //显示修改对话框
      this.tempPacuser.id = this.list[$index].id;
      this.tempPacuser.username = this.list[$index].username;
      this.dialogStatus = "update";
      this.dialogFormVisible = true;
    },
    createPacuser() {
      //保存新文章
      // this.api({
      //   url: "/pacuser/addPacuser",
      //   method: "post",
      //   data: this.tempPacuser
      // }).then(() => {
      //   this.getList();
      //   this.dialogFormVisible = false
      // })
    },
    updatePacuser() {
      //修改文章
      // this.api({
      //   url: "/pacuser/updatePacuser",
      //   method: "post",
      //   data: this.tempPacuser
      // }).then(() => {
      //   this.getList();
      //   this.dialogFormVisible = false
      // })
    }
  }
};
</script>
