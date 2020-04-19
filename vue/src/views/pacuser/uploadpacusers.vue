<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-upload
            :http-request="uploadFile"
            class="upload-demo"
            :action="UploadUrl()"
            :before-upload="beforeUploadFile"
            :on-change="fileChange"
            :on-exceed="exceedFile"
            :on-preview="handlePreview"
            :on-success="handleSuccess"
            :on-error="handleError"
            :file-list="fileList"
            :limit="limitNum"
          >
            <el-button size="small" type="primary" @click="selectfile">导入文件</el-button>
          </el-upload>
          <!-- <el-button size="small" type="primary" @click="uploadFile">上传文件</el-button> -->
        </el-form-item>
      </el-form>
    </div>
    <span>导入结果: {{ msg }}</span>
  </div>
</template>

 
<script>
export default {
  data() {
    return {
      msg: "",
      limitNum: 1, // 上传excell时，同时允许上传的最大数
      fileList: [] // excel文件列表
    };
  },
  methods: {
    // 文件超出个数限制时的钩子
    exceedFile(files, fileList) {
      this.$message.warning(
        `只能选择 ${this.limitNum} 个文件，当前共选择了 ${files.length +
          fileList.length} 个`
      );
    },
    // 文件状态改变时的钩子
    fileChange(file, fileList) {
      // console.log(file);
      this.fileList.push(file.raw);
      // console.log(this.fileList);
      // this.uploadFile()
    },
    // 上传文件之前的钩子, 参数为上传的文件,若返回 false 或者返回 Promise 且被 reject，则停止上传
    beforeUploadFile(file) {
      // console.log('before upload');
      // console.log(file);
      let extension = file.name.substring(file.name.lastIndexOf(".") + 1);
      let size = file.size / 1024 / 1024;
      // if (extension !== "xlsx") {
      //   this.$message.warning("只能上传后缀是.xlsx的文件");
      // }
      if (size > 10) {
        this.$message.warning("文件大小不得超过10M");
      }
    },
    // 文件上传成功时的钩子
    handleSuccess(res, file, fileList) {
      console.log("文件上传成功");

      this.$message.success("文件上传成功");
    },
    // 文件上传失败时的钩子
    handleError(err, file, fileList) {
      console.log(err);

      this.$message.error("文件上传失败");
    },
    UploadUrl: function() {
      // 因为action参数是必填项，我们使用二次确认进行文件上传时，直接填上传文件的url会因为没有参数导致api报404，所以这里将action设置为一个返回为空的方法就行，避免抛错
      return "";
    },
    //上传方法
    uploadFile() {
      let form = new FormData();
      console.log(this.fileList);
      form.append("uploadFile", this.fileList[0]); //有多少条数据appeng的多少次
      this.api({
        url: "/pacuser/uploadPacusers",
        method: "post",
        data: form,
        headers: { "Content-type": "multipart/form-data" }
      })
        .then(res => {
          console.log("文件上传成功");
          console.log("返回值"+res);
          this.$message({
            type: "success",
            message: "导入成功"
          });
          this.fileList = [];
          this.msg = res;
        })
        .catch(err => {
          console.log(err);
        });
    },
    //选择文件
    selectfile() {
      if (this.fileList.length === 0) {
        // this.$message.warning('请上传文件');
      } else {
        this.fileList = []; 
      }
    },
    handlePreview(file) {
      console.log("preview!");
      console.log(file);
    }
  }
};
</script>
 
