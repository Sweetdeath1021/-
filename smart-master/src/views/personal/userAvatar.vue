<template>
  <div class="userImg">
    <img :src="options.img" @click="editCropper()" title="点击上传头像" class="img-circle img-lg img-center" ref="img"/>
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body @opened="modalOpened">
      <el-row>
        <el-col :xs="12" :sm="12" :style="{height: '350px'}">
          <vue-cropper
              ref="cropper"
              :img="options.img"
              :info="true"
              :autoCrop="options.autoCrop"
              :autoCropWidth="options.autoCropWidth"
              :autoCropHeight="options.autoCropHeight"
              :fixedBox="options.fixedBox"
              @realTime="realTime"
              v-if="visible"
            />
        </el-col>
        <el-col :xs="12" :sm="12" :style="{height: '350px'}">
          <div class="avatar-upload-preview">
            <img :src="previews.url" :style="previews.img" />
          </div>
        </el-col>
      </el-row>
      <br />
      <el-row>
        <el-col :lg="2" :md="2">
          <el-upload action="#" :https-request="requestUpload" :show-file-list="false" :before-upload="beforeUpload">
            <el-button size="small">
              上传
              <i class="el-icon-upload el-icon--right"></i>
            </el-button>
          </el-upload>
        </el-col>
        <el-col :lg="{span: 1, offset: 2}" :md="2">
          <el-button icon="el-icon-plus" size="small" @click="changeScale(1)"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :md="2">
          <el-button icon="el-icon-minus" size="small" @click="changeScale(-1)"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :md="2">
          <el-button icon="el-icon-refresh-left" size="small" @click="rotateLeft()"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :md="2">
          <el-button icon="el-icon-refresh-right" size="small" @click="rotateRight()"></el-button>
        </el-col>
        <el-col :lg="{span: 2, offset: 7}" :md="2">
          <el-button type="primary" size="small" @click="uploadImg()">提 交</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<style>
.userImg {
  display: flex;
  justify-content: center;
}
.text-center {
  margin-bottom: 15px;
}
.avatar-upload-preview {
  height: 200px!important;
  width: 200px!important;
}
</style>

<script>
import { VueCropper } from "vue-cropper";

export default {
  components: { VueCropper },
  props: {
    user:{
      type: Number
    },
    imageUrl: {
      type: String
    }
  },
  data() {
    return {
      // 是否显示弹出层
      open: false,
      // 是否显示cropper
      visible: false,
      // 弹出层标题
      title: "修改头像",
      options: {
        img:require('../../assets/img/默认头像.svg'), //裁剪图片的地址 ||store.getters.avatar
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: 200, // 默认生成截图框宽度
        autoCropHeight: 200, // 默认生成截图框高度
        fixedBox: true // 固定截图框大小 不允许改变
      },
      previews: {},
      fileName: '',
      blobUrl: ''
    };
  },
  created() {
    if(this.user == 1){
      // this.getImg()
      this.user.imageUrl?this.options.img = this.user.imageUrl:1
    } else {
      if(this.imageUrl) {
        this.options.img = this.imageUrl
      }
    }
  },
  methods: {
    openAvatar() {
      this.open = true
    },
    selectAvatar(src) {
      this.options.img = src
      this.open = true
    },
    // 编辑头像
    editCropper() {
      this.open = true;
      console.log(this.options.img);
    },
    // 打开弹出层结束时的回调
    modalOpened() {
      this.visible = true;
    },
    // 覆盖默认的上传行为
    requestUpload() {
    },
    // 向左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft();
    },
    // 向右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
    // 图片缩放
    changeScale(num) {
      num = num || 1;
      this.$refs.cropper.changeScale(num);
    },
    // 上传预处理
    beforeUpload(file) {
      // console.log(file.name);
      this.fileName = file.name;
      if (file.type.indexOf("image/") == -1) {
        this.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.options.img = reader.result;
        };
      }
    },
    //获取图片
    getImage(url) {
      this.options.img = url
    },
    // getImg() {
    //   // 发起GET请求获取用户头像
    //   fetch(`https://welcomed-readily-spaniel.ngrok-free.app/user/getAvatar?userId=${localStorage.getItem('userId')}`, {
    //     method: 'get',
    //     headers: new Headers({
    //       'ngrok-skip-browser-warning': 'true',
    //     })
    //   })
    //     .then(response => {
    //       if (!response.ok) {
    //         throw new Error(`https error! Status: ${response.status}`);
    //       }
    //       return response.arrayBuffer();
    //     })
    //     .then(data => {
    //       // 将二进制数据转换为Blob
    //       const blob = new Blob([data], { type: 'image/*' });

    //       // 创建Blob URL并设置给<img>标签
    //       const imageUrl = URL.createObjectURL(blob);
    //       this.options.img = imageUrl;
    //       // 保存 Blob URL，以便稍后释放
    //       this.blobUrl = imageUrl;
    //     })
    //     .catch(() => {
    //       this.$message.error('上传失败，请稍后再试')
    //     });
    //   // this.options.img= `https://welcomed-readily-spaniel.ngrok-free.app/user/getAvatar?userId=${localStorage.getItem('userId')}`
    // },
    // 上传图片
    uploadImg() {
      // console.log(this.options.img);
      this.$refs.cropper.getCropBlob(data => {
        if(this.user == 1){
          const formData = new FormData();
          const blob = new Blob([data], { type: 'image/*' });
          const file = new File([blob], this.fileName, { type: 'image/*' });
          formData.append('file', file);
          this.$.ajax({
            url: this.baseUrl + '/grading/user/avatar',
            type: 'put',
            data: formData,
            headers: {
              'ngrok-skip-browser-warning': 'true',
              'token': localStorage.getItem('token')
            },
            contentType: false, // 使用默认的 contentType
            processData: false, // 不对数据进行处理，以便传递 FormData 对象
            success: res => {
              // console.log(res);
              if (res.status === 200) {
                this.open = false;
                this.$message.success("上传成功");
                
              }
              this.visible = false;
            },
            error: () => {
              this.$message.error('上传失败，请稍后再试')
            }
          });
        } else {
          // console.log(data);
          // 创建Blob URL并设置给<img>标签
          const imageUrl = URL.createObjectURL(data);
          this.options.img = imageUrl;
          // 保存 Blob URL，以便稍后释放
          this.blobUrl = imageUrl;
          this.open = false
          const emit= {
            data,
            imageUrl,
            fileName: this.fileName
          }
          this.$emit('save', emit)
          this.fileName = ''
          this.visible = false;
        }
      })
    },
    mounted() {
    },
    // 实时预览
    realTime(data) {
      this.previews = data;
      // console.log(this.previews.img);
    },
    beforeDestroy() {
      // 在组件销毁前释放 Blob URL
      if (this.blobUrl) {
        URL.revokeObjectURL(this.blobUrl);
      }
    }
  }
};
</script>
