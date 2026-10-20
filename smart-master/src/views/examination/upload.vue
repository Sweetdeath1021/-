<template>
    <div class="base">
      <el-form label-position="top" label-width="80px" :model="formLabelAlign" class="elForm" :rules="rules" ref="form">
        <div class="left">
          <div class="fixed border">
            <div class="extra">填写试卷基本信息</div>
            <el-form-item label="试卷名称" prop="paperName">
              <el-input v-model="formLabelAlign.paperName"></el-input>
            </el-form-item>
            <el-form-item label="学科" prop="paperSubject">
              <el-input v-model="formLabelAlign.paperSubject"></el-input>
            </el-form-item>
            <el-form-item label="学校">
              <el-input v-model="school" disabled></el-input>
            </el-form-item>
          </div>
          <div class="submit" @click="submitForm">提交</div>
        </div>
        <div class="right">
          <el-form-item label="上传答题卡" class="background border">
            <el-upload class="upload-demo" drag multiple
            action="#"
            :file-list="elForm.answerFileList"
            :on-change="changeAnswerFile"
            :on-remove="removeAnswerFile"
            :auto-upload="false">
                <img class="icon" src="../../assets/img/文件夹.svg" alt="">
                <div class="texts">支持文件格式：zip、7z、tar、rar、gz等<br/>压缩包命名规范：<em>xx班_学科</em></div>
              </el-upload>
          </el-form-item>
          <el-form-item label="上传样卷" class="background border">
            <el-upload class="upload-demo" drag multiple
            action="#"
            :file-list="elForm.sampleFileList"
            :on-change="changeSampleFile"
            :on-remove="removeSampleFile"
            :auto-upload="false">
              <img class="icon2" src="../../assets/img/上传.svg" alt="">
              <div class="el-upload__text">请上传样卷</div>
            </el-upload>
          </el-form-item>
          <el-form-item label="查看样卷" class="background border">
            <div class="view">
              <el-image 
              :src="srcList[0]"
              class="elImg"
              fit="cover"
              :preview-src-list="srcList"></el-image>
              <!-- <i class="el-icon-view"></i> -->
              <div class="viewSubmit">点击预览</div>
            </div>
          </el-form-item>
        </div>
    </el-form>
    </div>
</template>
 
<script>
export default {
    name: 'UploadSmart',
    data() {
        return {
          formLabelAlign: {
            paperName: '',
            paperSubject: '',
            paperSchool:'赛鸭脖',
          },
          elForm: {
            answerFileList: [],
            sampleFileList: [],
          },
          activeName: 'first',
          rules:{
            paperName: [
              { required: true, message: '请输入试卷名称', trigger: 'blur' },
            ],
            paperSubject: [
              { required: true, message: '请输入学科', trigger: 'blur' },
            ]
          },
          srcList: [],
          school:localStorage.getItem('school')
        }
  },
  methods: {
    changeAnswerFile(file, fileList) {
      this.elForm.answerFileList = fileList.map(item => {
        // console.log(item.name);
        if (/.+_.+/.test(item.name)) {
          if (item.raw) {
            return item.raw;
          }else {
            return item
          }
        } else {
          this.$message.error('压缩包命名规范：xx班_学科')
        }
      }).filter(item => item !== undefined);
      // console.log(this.elForm.answerFileList,file,fileList);
    },
    changeSampleFile(file,fileList) {
      this.elForm.sampleFileList = fileList.map(item => {
        if (item.raw) {
          return item.raw;
        }
        else {
          return item
        }
      })
      this.srcList = []
      this.elForm.sampleFileList.forEach(item => {
        // const reader = new FileReader();
        // reader.readAsDataURL(item);
        // reader.onload = () => {
        //   this.srcList.push(reader.result)
        // };
        this.srcList.push(URL.createObjectURL(item))
      })
      // console.log(this.srcList);
    },
    removeAnswerFile(file, fileList) {
      this.elForm.answerFileList = fileList.map(item => {
        if (item.raw) {
          return item.raw;
        }
        else {
          return item
        }
      });
    },
    removeSampleFile(file, fileList) {
      this.elForm.sampleFileList = fileList.map(item => {
        if (item.raw) {
          return item.raw;
        }
        else {
          return item
        }
      });
      this.srcList = []
      this.elForm.sampleFileList.forEach(item => {
        // const reader = new FileReader();
        // reader.readAsDataURL(item);
        // reader.onload = () => {
        //   this.srcList.push(reader.result)
        // };
        this.srcList.push(URL.createObjectURL(item))
      })
      console.log(this.srcList);
    },
    submitForm() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if(this.elForm.sampleFileList.length == 0 || this.elForm.answerFileList.length == 0) {
            this.$message.error('请上传答题卡、样卷')
          } else {
            const loginMessage = this.$message({
						type: 'success',
						duration: 0,
						dangerouslyUseHTMLString: true,
						iconClass: 'el-icon-loading',
						customClass: 'messageLoading',
						message: '&nbsp;&nbsp;正在提交'
					})
            const formData = new FormData();
            const data = {
              paperName: this.formLabelAlign.paperName,
              paperSubject: this.formLabelAlign.paperSubject,
              schoolName: localStorage.getItem('school'),
              paperStatus: 0,
              sheetStatus: 0
            }
            for (let i = 0; i < this.elForm.answerFileList.length; i++) {
              formData.append('sheet', this.elForm.answerFileList[i])
            }
            for (let i = 0; i < this.elForm.sampleFileList.length; i++) {
              formData.append('paper', this.elForm.sampleFileList[i])
            }
            formData.append('entity', new Blob([JSON.stringify(data)], { type: 'application/json' }));
            this.$.ajax({
              url: this.baseUrl + "/grading/grade/upload",
              type: 'post',
              data: formData,
              contentType: false,
              processData: false,
              headers: {
                'ngrok-skip-browser-warning': 'true',
                'token': localStorage.getItem('token')
              },
              success: res => {
                console.log(res);
                if (res.status == 200) {
                  loginMessage.close()
                  this.$message.success(res.data)
                  this.$refs['form'].resetFields()
                  this.elForm.answerFileList = []
                  this.elForm.sampleFileList = []
                } else if (res.status == 500) {
                  loginMessage.close()
                  this.$message.error(res.msg)
                } else {
                  loginMessage.close()
                  this.$message.error('上传失败')
                }
              },
              error: () => {
                loginMessage.close()
                this.$message.error('上传失败，请稍后再试')
              }
            });
          }
        }
      })
    }
  },
  mounted() {
    
  },
  beforeDestroy() {
    // 在组件销毁前释放 Blob URL
    if (this.srcList) {
      this.srcList.forEach(item => {
        URL.revokeObjectURL(item);
      })
    }
  }
}
</script>

<style>
.messageLoading{
	background-color: #f0f9eb;
    border-color: #e1f3d8;
	color: #67C23A;
	min-width: 140px;
}
</style>

<style scoped>
.border {
  border: 1px solid #86868659;
}
.extra {
  width: 100%;
  font-size: 16px;
  height: 50px;
  line-height: 50px;
  text-align: center;
  border-radius: 14px 14px 0 0;
  background-color: #f5fff6a1;
}
.base {
  position: relative;
  height: 82vh;
  margin: 70px 0;
  overflow: scroll;
}
.base::-webkit-scrollbar{
         display:none
    }
.tabs {
  background: #fff;
  margin: 30px 10px 30px 25px;
  padding: 22px 40px 22px 28px;
  border-radius: 12px;
}
.el-form {
  display: flex;
  margin: 0 40px 30px 40px;
  background: transparent;
  /* padding: 22px 40px 22px 28px; */
  border-radius: 12px;
}
.left {
  display: flex;
  justify-content: center;
  position: relative;
  width: 27vw;
  margin-right: 20px;
}
.left .fixed {
  position: fixed;
  height: 70vh;
  width: 27vw;
  background-color: #ffffffc2;
  border-radius: 12px;
}
.right {
  width: 56vw;
}

.el-form .el-form-item {
  /* background: #fff; */
  padding: 22px 30px 22px 30px;
  border-radius: 12px;
  margin-bottom: 25px;
  /* background: #ffffffc2; */
  /*backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  -moz-backdrop-filter: blur(5px);
  -ms-backdrop-filter: blur(5px);*/
}
.left .el-form-item {
  margin-bottom: 0;
  padding: 10px 30px 10px 30px;
}
.el-form .background {
  background: #ffffffc2;
}
.elForm /deep/ .el-form-item__label {
  padding: 0!important;
}
.elForm /deep/ .el-upload {
  width: 100%;
}
.elForm /deep/ .el-upload-dragger {
  width: 100%;
}
/* .elForm /deep/ .el-input__inner {
  background-color: transparent;
} */
/* .el-icon-folder-opened:before{
  font-size: 67px;
  color: #C0C4CC;
  line-height: 50px;
}
i.el-icon-folder-opened {
  margin: 40px 0 0;
} */
.icon {
  margin-top: 24px;
  width: 67px;
  height: 67px;
}
.icon2 {
  margin-top: 24px;
  width: 76px;
  height: 76px;
}
.texts {
  line-height: 24px;
  color: #989ba1;
}
.texts em {
  color: #409EFF;
}
.view {
  display: flex;
  flex-direction: column;
  position: relative;
  width: 100%;
  height: 180px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  overflow: hidden;
  height: 450px;
}
.view:hover {
  border: 1px solid #00ce95;
}
.el-upload__text {
  color:#989ba1;
}
.el-icon-view {
  margin-bottom: 7px;
  z-index: 1;
}
.el-icon-view:before{
  font-size: 67px;
  color: #C0C4CC;
  line-height: 50px;
}
.elImg {
  position: absolute;
  width: 100%;
  height: 100%;
  margin-right: 0;
  /* filter: blur(5px); */
}
.elImg /deep/ .el-image__error {
  display: none;
}
.viewSubmit {
  pointer-events: none;
  color: #989ba1;
  z-index: 1;
}
/* .submit{
  margin-top: 20px;
  width:25vw;
  height:35px;
  background-color:#00ce95;
  color:#fff;
  border-radius:5px;
  line-height:35px;
  text-align:center;
  border:none;
  outline:none;
  cursor: pointer;
  position: fixed;
  bottom: 15px;
}
.submit:hover{
  background-color: #00de9f;
} */
.submit {
    /* 相对定位 */
    position: relative;
    width:25vw;
    height:50px;
    border-radius:5px;
    line-height:50px;
    text-align:center;
    border:none;
    outline:none;
    cursor: pointer;
    position: fixed;
    bottom: 22px;
    text-decoration: none;
    text-transform: uppercase;
    font-size: 24px;
    color: #fff;
    /* 圆角属性 */
    border-radius: 50px;
    /* 背景渐变色 */
    background-image: linear-gradient(to right, #03f46fb1, #41ebf4b1, #f2ff3bb1, #09f434b1);
    /* 背景渐变色大小 */
    background-size: 400%;
    z-index: 1;
}

/* 下面设计 发光效果 */
.submit::before {
    content: '';
    position: absolute;
    top: -5px;
    bottom: -5px;
    left: -5px;
    right: -5px;
    border-radius: 50px;
    /* 背景渐变色 */
    background-image: linear-gradient(to right, #03f46fb1, #41ebf4b1, #f2ff3bb1, #09f434b1);
    /* 背景渐变色大小 */
    background-size: 400%;
    /* 元素的位置 底层或者顶层  -值表示底层 + 值表示顶层 */
    z-index: -1;
    /* 设置模糊度 显示发光效果 */
    filter: blur(15px);
}

.submit:hover {
    /* 动画 名称 时间 infinite 是无限次播放 */
    animation: sun 8s infinite linear;
}

.submit:hover::before {
    animation: sun 8s infinite;
}

@keyframes sun {
    100% {
        /* 背景位置 */
        background-position: -400% 0;
    }
}
</style>    