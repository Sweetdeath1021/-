<template>
  <div class="wrap">
    <div class="container">
      <div class="new-guide-page">
        <div class="page-content">
          <div class="new-guide-common">
            <div class="new-guide-common__left">
              <img src=".././assets/img/研究  (1).svg" alt="" v-if="type == 1" class="background">
              <img src=".././assets/img/读书  (1).svg" alt="" v-if="type == 2" class="background">
              <img src=".././assets/img/知识  (2).svg" alt="" v-if="type == 3" class="background">
            </div>
            <div class="new-guide-common__right">
              <div class="new-guide-common__cont">
                <div v-if="type == 1" class="new-guide-common__title">基础信息 <i style="color: #999;">1/3</i></div>
                <div v-if="type == 2" class="new-guide-common__title">账号信息 <i style="color: #999;">2/3</i></div>
                <div v-if="type == 3" class="new-guide-common__title">上传头像 <i style="color: #999;">3/3</i></div>
                <div class="new-guide-common__progress">
                  <div class="new-guide-common__progress-inner"></div>
                </div>
                <el-form label-position="top" label-width="80px" :model="user" ref="form" :rules="rules">
                  <div class="new-guide-base-info " v-show="type == 1">
                    <div class="form-item form-item-required">
                      <div class="el-form-item el-form-ite">
                        <div class="item-label">角色</div>
                        <div class="el-form-item__content">
                          <div role="radiogroup" class="el-radio-group" @click="click1">
                            <label role="radio" tabindex="-1" class="el-radio-button narrow radio_checked">
                              <input type="radio" tabindex="-1" class="el-radio-button__orig-radio">
                              <span class="radio-input">阅卷科目组长</span>
                            </label>
                            <label role="radio" tabindex="-1" class="el-radio-button narrow">
                              <input type="radio" tabindex="-1" class="el-radio-button__orig-radio">
                              <span class="radio-input">老师</span>
                            </label>
                            <label role="radio" tabindex="-1" class="el-radio-button narrow right-button">
                              <input type="radio" tabindex="-1" class="el-radio-button__orig-radio">
                              <span class="radio-input">学生</span>
                            </label>
                          </div>
                        </div>
                      </div>
                      <el-form-item label="学校" class="el-form-ite" prop="userSchool">
                        <el-input v-model="user.userSchool" class="el-form-item__conten"></el-input>
                      </el-form-item>
                      <div style="display: flex;justify-content: space-between;" v-if="flag">
                        <el-form-item label="班级" class="el-form-ite" prop="teacherClass"
                          style="flex: 1;margin-right: 5%;">
                          <el-dropdown trigger="click" class="eldropdown" @visible-change="dropdown1($event)">
                            <span class="el-dropdown-link">
                              <span style="color: #999;" v-if="this.user.teacherClass.length == 0">筛选标签</span><span
                                class="checkboxList" v-else>{{ this.user.teacherClass.join(' ') }}</span><i
                                class="el-icon-arrow-down el-icon--right icon1"
                                style="line-height: 55px;transition: all 0.3s;"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                              <el-checkbox-group v-model="user.teacherClass" size="small" class="checkbox">
                                <el-checkbox label="智科1班" border></el-checkbox>
                                <el-checkbox label="智科2班" border></el-checkbox>
                                <el-checkbox label="土木1班" border></el-checkbox>
                                <el-checkbox label="土木2班" border></el-checkbox>
                                <el-checkbox label="软件1班" border></el-checkbox>
                                <el-checkbox label="软件2班" border></el-checkbox>
                              </el-checkbox-group>
                            </el-dropdown-menu>
                          </el-dropdown>
                          <!-- <el-input v-model="user.teacherClass" class="el-form-item__conten" placeholder="多个班级请用“，”分隔开"></el-input> -->
                        </el-form-item>
                        <el-form-item label="科目" class="el-form-ite" prop="subject" style="flex: 1;">
                          <el-dropdown trigger="click" class="eldropdown" @visible-change="dropdown2($event)">
                            <span class="el-dropdown-link">
                              <span style="color: #999;" v-if="this.user.subject.length == 0">筛选标签</span><span
                                class="checkboxList" v-else>{{ this.user.subject.join(' ') }}</span><i
                                class="el-icon-arrow-down el-icon--right icon2"
                                style="line-height: 55px;transition: all 0.3s;"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                              <el-checkbox-group v-model="user.subject" size="small" class="checkbox">
                                <el-checkbox label="高数" border></el-checkbox>
                                <el-checkbox label="英语" border></el-checkbox>
                                <el-checkbox label="c语言" border></el-checkbox>
                                <el-checkbox label="python" border></el-checkbox>
                                <el-checkbox label="html" border></el-checkbox>
                                <el-checkbox label="java" border></el-checkbox>
                              </el-checkbox-group>
                            </el-dropdown-menu>
                          </el-dropdown>
                          <!-- <el-input v-model="user.subject" class="el-form-item__conten" placeholder="多个科目请用“，”分隔开"></el-input> -->
                        </el-form-item>
                      </div>
                      <el-form-item label="用户名" class="el-form-ite" prop="userName">
                        <el-input v-model="user.userName" class="el-form-item__conten"></el-input>
                      </el-form-item>
                      <div class="el-form-item el-form-ite">
                        <div class="item-label">性别</div>
                        <div class="el-form-item__content">
                          <div role="radiogroup" class="el-radio-group" @click="click2">
                            <label role="radio" tabindex="-1" class="el-radio-button gender radio_checked">
                              <input type="radio" tabindex="-1" class="el-radio-button__orig-radio">
                              <span class="radio-input">女</span>
                            </label>
                            <label role="radio" tabindex="-1" class="el-radio-button gender">
                              <input type="radio" tabindex="-1" class="el-radio-button__orig-radio">
                              <span class="radio-input">男</span>
                            </label>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="new-guide-base-info" v-show="type == 2">
                    <div class="form-item form-item-required">
                      <el-form-item label="手机号" class="el-form-ite marginBottom" prop="userAccount" v-if="!isStudent">
                        <el-input v-model="user.userAccount" class="el-form-item__conten"></el-input>
                      </el-form-item>
                      <el-form-item label="学号" class="el-form-ite marginBottom" prop="studentNumber" v-else>
                        <el-input v-model="user.studentNumber" class="el-form-item__conten"></el-input>
                      </el-form-item>
                      <el-form-item label="邮箱" class="el-form-ite marginBottom" prop="userEmail">
                        <el-input v-model="user.userEmail" class="el-form-item__conten"></el-input>
                      </el-form-item>
                      <el-form-item label="密码" class="el-form-ite marginBottom" prop="userPassword">
                        <el-input v-model="user.userPassword" class="el-form-item__conten" type="password"
                          autocomplete="off" show-password></el-input>
                      </el-form-item>
                      <el-form-item label="确认密码" class="el-form-ite marginBottom" prop="confirm">
                        <el-input v-model="user.confirm" class="el-form-item__conten" type="password" autocomplete="off"
                          show-password></el-input>
                      </el-form-item>
                    </div>
                  </div>
                </el-form>
                <div class="new-guide-base-info" v-show="type == 3">
                  <div class="new-guide-upload-avatar">
                    <div class="new-guide-upload-avatar__main">
                      <div>
                        <input type="file" class="upload-input">
                        <div class="custom-box">
                          <div>
                            <div class="new-guide-upload-avatar__upload-img">
                              <userAvatar ref="userAvatar" @save="saveAvatar" :imageUrl="imageUrl"></userAvatar>
                            </div>
                            <button type="button" class="new-guide-upload-avatar__upload-btn" @click="openAvatar">
                              上传头像
                            </button>
                          </div>
                        </div>
                      </div>
                      <p class="new-guide-upload-avatar__desc">点击上传头像，从电脑相册中选择一张头像 或者从下方选择默认头像</p>
                    </div>
                    <ul class="new-guide-upload-avatar__default" @click="selectAvatar">
                      <li class="new-guide-upload-avatar__item">
                        <img src=".././assets/img/5.jpg" alt="" v-if="user.userSex" data-id="1">
                        <img src=".././assets/img/1.jpg" alt="" v-else data-id="5">
                      </li>
                      <li class="new-guide-upload-avatar__item">
                        <img src=".././assets/img/6.jpg" alt="" v-if="user.userSex" data-id="2">
                        <img src=".././assets/img/2.jpg" alt="" v-else data-id="6">
                      </li>
                      <li class="new-guide-upload-avatar__item">
                        <img src=".././assets/img/7.jpg" alt="" v-if="user.userSex" data-id="3">
                        <img src=".././assets/img/3.jpg" alt="" v-else data-id="7">
                      </li>
                      <li class="new-guide-upload-avatar__item">
                        <img src=".././assets/img/8.jpg" alt="" v-if="user.userSex" data-id="4">
                        <img src=".././assets/img/4.jpg" alt="" v-else data-id="8">
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <footer>
          <div class="new-guide-page__operation">
            <button type="button" v-if="[2,3].includes(type)" class="btn" @click="previousStep">
              上一步
            </button>
            <!-- <el-upload
            ref="upload"
            action="https://welcomed-guiding-hippo.ngrok-free.app/grading/user/register"
            :file-list="fileList"
            :headers="headers"
            :show-file-list="false"
            :data="formdata"
            :multiple="false"
            :auto-upload="false"
            style="position: absolute;">
            </el-upload> -->
            <el-button type="button" class="btn btn-primary next" @click="submitForm">
              <i v-if="type == 3">完成</i>
              <i v-else>下一步</i>
            </el-button>
          </div>
        </footer>
      </div>
    </div>
  </div>

</template>

<script>
import userAvatar from './personal/userAvatar.vue';

export default {
  name: 'RegisterSmart',
  data() {
    return {
      user: {
        userName: '',
        userSex: 0,
        userAccount: '',
        userPassword: '',
        userEmail:'',
        userSchool: '',
        confirm: '',
        teacherClass: [],//班级
        subject: [],//科目
        studentNumber: '',
      },
      roleId: 1,
      photo: '',
      imageUrl:'',
      type: 1,
      fileName: '',
      flag: false,//如果是老师就为true然后显示俩个框
      isStudent: false,//是否是学生
      
      rules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        userSchool: [
          { required: true, message: '请输入学校', trigger: 'blur' }
        ],
        teacherClass: [
          { required: true, message: '请输入班级', trigger: 'blur' }
        ],
        subject: [
          { required: true, message: '请输入科目', trigger: 'blur' }
        ],
        userAccount: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/
            , message: '请输入正确的号码格式', trigger: 'blur' }
        ],
        studentNumber: [
          { required: true, message: '请输入学号', trigger: 'blur' },
          { pattern: /^\d{11}$/
            , message: '请输入正确的学号格式', trigger: 'blur' }
        ],
        userEmail: [
          {
            required: true,
            message: "请输入邮箱",
            trigger: "blur"
          },
          {
            validator: function (rule, value, callback) {
              const reg = /^([a-zA-Z0-9]+[-_]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
              if (value == '' || value == undefined || value == null) {
                callback();
              } else {
                if (!reg.test(value)) {
                  callback(new Error('请输入正确的邮箱'));
                } else {
                  callback();
                }
              }
            },
            trigger: "blur"
          },
        ],
        userPassword: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
        ],
        confirm: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          {
            validator: (rule, value, callback) => {
              if (this.user.userPassword !== value) {
                callback(new Error("两次输入的密码不一致"));
              } else {
                callback();
              }
          } ,trigger: "blur" }
        ]
      }
    }
  },
  components: {
    userAvatar
  },
  mounted() {
    setTimeout(() => {
      this.topLength()
    }, 100);
  },
  methods: {
    topLength() {
      setTimeout(() => {
        const length = document.querySelector('.new-guide-common__progress-inner')
        if (this.type == 1) {
          length.style.width = '186.66px'
        } else if (this.type == 2) {
          length.style.width = '373.33px'
        } else {
          length.style.width = '560px'
        }
      }, 1);
        
    },
    previousStep() {
      this.type--
      this.topLength()
    },
    submitForm() {
      if (this.type == 3) {
        this.$refs.form.validate((valid) => {
          if (valid) {
            const data = {
              user: {
                userName: this.user.userName,
                userSex: this.user.userSex,
                userAccount: this.roleId == 3 ?this.user.studentNumber:this.user.userAccount,
                userPassword: this.user.userPassword,
                userEmail: this.user.userEmail,
                userSchool: this.user.userSchool,
              },
              roleId: this.roleId,
              teacher: {
                teacherClass: this.user.teacherClass,
                subject: this.user.subject
              },
              student: {
                studentNumber: this.user.studentNumber
              }
            }
            console.log(data);
            const formData = new FormData();
            const blob = new Blob([this.photo], { type: 'image/*' });
            const file = new File([blob], this.fileName, { type: 'image/*' });
            formData.append('file', file);
            formData.append('entity', new Blob([JSON.stringify(data)], { type: 'application/json' }));
            this.$.ajax({
              url: this.baseUrl + "/grading/user/register",
              type: 'post',
              data: formData,
              contentType: false,
              processData: false,
              headers: {
                'ngrok-skip-browser-warning': 'true',
              },
              success: res => {
                console.log(res);
                if (res.status == 200) {
                  this.$message.success('注册成功')
                  this.$refs.form.resetFields()
                  this.$router.push('/login')
                } else if(res.status == 500) {
                  this.$message.error(res.msg)
                } else {
                  this.$message.error('注册失败')
                }
              },
              error: () => {
                this.$message.error('注册失败，请稍后再试')
              }
            });
          } else {
            this.$message.error('请检查必填项')
          }
          })
      } else {
        this.type++
        this.topLength()
      }
    },
    click1(e) {
      this.$refs.form.clearValidate()
      if (e.target.tagName == 'LABEL') {
        // console.log(this.$(e.target));
        this.$(e.target).addClass('radio_checked').siblings().removeClass('radio_checked')
        this.roleId = this.$(e.target).index() + 1
      } else if (e.target.tagName == 'SPAN') {
        // console.log(e.target.parentNode);
        this.$(e.target.parentNode).addClass('radio_checked').siblings().removeClass('radio_checked')
        this.roleId = this.$(e.target.parentNode).index() + 1
      }
      if (this.roleId == 2) {
        this.flag = true
      } else {
        this.flag = false
      }
      if (this.roleId == 3) {
        this.isStudent = true
      } else {
        this.isStudent = false
      }
      // console.log(this.user);
    },
    click2(e) {
      if (e.target.tagName == 'LABEL') {
        // console.log(this.$(e.target).index());
        this.$(e.target).addClass('radio_checked').siblings().removeClass('radio_checked')
        this.user.userSex = this.$(e.target).index()
      } else if (e.target.tagName == 'SPAN') {
        // console.log(e.target.parentNode);
        this.$(e.target.parentNode).addClass('radio_checked').siblings().removeClass('radio_checked')
        this.user.userSex = this.$(e.target.parentNode).index()
      }
      // console.log(this.user);
    },
    openAvatar() {
      this.$refs.userAvatar.openAvatar()
    },
    saveAvatar(res) {
      this.photo = res.data
      this.imageUrl = res.imageUrl
      if (res.fileName) {
        this.fileName = res.fileName
      }
    },
    // 选择默认头像
    selectAvatar(e) {
      // console.log(e.target);
      if (e.target.tagName == 'IMG') {
        this.$refs.userAvatar.selectAvatar(this.$(e.target).prop('src'))
        this.imageUrl = this.$(e.target).prop('src')
        this.fileName = this.$(e.target).data('id') + '.jpg'
      }
    },
    dropdown1(e) {
      if (e) {
        this.$('.icon1').addClass('el-icon')
      } else {
        this.$('.icon1').removeClass('el-icon')
      }
    },
    dropdown2(e) {
      if (e) {
        this.$('.icon2').addClass('el-icon')
      } else {
        this.$('.icon2').removeClass('el-icon')
      }
    },
  }
}
</script>
<style scoped>
.checkboxList{
  height: 55px;
  display: inline-block;
  overflow: hidden;
}
.el-dropdown-menu {
  padding: 10px 10px 0 10px;
  max-width: 268px;
}
.el-checkbox {
  margin: 0 5px 10px!important;
}
.checkbox /deep/ .el-checkbox__inner {
  display: none;
}
.checkbox /deep/ .el-checkbox__label {
  padding-left: 0;
}
.el-dropdown-link {
  display: flex;
  justify-content: space-between;
  padding: 0 10px;
  width: 100%;
  line-height: 55px;
  text-align: center;
  border-radius: 8px;
  font-size: 16px;
  color: #222;
  font-weight: 700;
  background: #f8f8f8;
  border: 0.8px solid #f8f8f8;
  box-shadow: none;
  cursor: pointer;
}
.el-dropdown-link:hover {
  border-color: #18c3b1;
}
.el-icon{
  transform: rotate(180deg);
}
.eldropdown {
  display: block;
  padding: 0;
  height: 55px;
}
.background {
  width: 426px;
  height: 500px;
}
.form-item /deep/ .el-input__suffix{
  height: 60px;
}
.el-form /deep/.el-form-item__error {
  top: 88%;
}
/* .marginBottom{
  margin-bottom: 0px!important;
} */
.new-guide-upload-avatar__upload-img /deep/ .img-circle{
  width: 160px;
  height: 160px;
}
.narrow {
  width: 174px!important;
}
.right-button {
  margin-left: 0px!important;
}

div::-webkit-scrollbar {
  width: 5px;
}
div::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}
div::-webkit-scrollbar-thumb {
  background-color: #ddd;
}

a:active, a:focus, a:hover, button, input, textarea {
    outline: 0;
}
form {
      display: block;
      margin: 0;
      padding: 0;
}
.clearfix:after {
    content: "";
    overflow: hidden;
}

.wrap {
      background-color: rgb(0, 190, 189);
      background-size: cover;
      background-repeat: no-repeat;
      background-position: top center;
      height: 100vh;
      min-height: 712px;
      min-width: 1200px;
      display: flex;
      position: relative;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      }
.container {
      /* position: relative;
      border-radius: 16px;
      display: flex;
      justify-content: center;
      align-items: center;
      box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
      width: 1200px;
      max-width: 100%;
      min-height: 800px;
      background: -webkit-linear-gradient(right, #8dbaf4, #78ecd4); */
    /* position: relative;
      overflow: hidden; */
      position: relative;
      width: 1184px;
      height: calc(100vh - 80px);
      min-height: 675px;
      border-radius: 16px;
      overflow: hidden;
    }
.new-guide-page {
      height: 100%;
}
footer {
      position: absolute;
      bottom: 0;
      right: 0;
      width: 692px;
      height: 120px;
      background: linear-gradient(180deg,hsla(0,0%,100%,0),#fff 40% 100%);
      z-index: 9;
}
.new-guide-page__operation {
      position: absolute;
      right: 100px;
      bottom: 40px;
}
.new-guide-page__operation .btn.next {
      margin-left: 20px;
}
.new-guide-page__operation .btn-primary {
      background: #00bebd;
      color: #fff;
      cursor: pointer;
}
.new-guide-page__operation .btn.btn-primary {
      min-width: 96px;
      height: 36px;
      color: #fff;
      border: 1px solid #00bebd;
      font-weight: 700;
      font-size: 16px;
      border-radius: 4px;
}
.new-guide-page__operation .btn.btn-primary:hover {
      background: #00a6a7;
      border-color: #00a6a7;
      color: #fff;
}

.btn {
    margin: 0;
    padding: 9px 15px;
    line-height: 1;
    font-size: 14px;
    background-color: #fff;
    border: 1px solid #e3e7ed;
    color: #414a60;
    vertical-align: middle;
    cursor: pointer;
    text-align: center;
    border-radius: 0;
    -webkit-appearance: none;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    transition: background-color .1s linear,box-shadow .1s linear;
    outline: none;
    /* user-select: none; */
}
.btn, .btn i {
    display: inline-block;
}
.new-guide-page__operation .btn {
    min-width: 96px;
    height: 36px;
    color: #00bebd;
    border: 1px solid #00bebd;
    font-weight: 700;
    font-size: 16px;
    border-radius: 4px;
}
.page-content {
      position: relative;
      background-color: #fff;
      height: 100%;
}
.new-guide-common {
      display: flex;
      width: 100%;
      height: 100%;
}
.new-guide-common__left {
      position: relative;
      padding: 80px 0 40px 0px;
      width: 35%;
      height: 100%;
      background: linear-gradient(180deg,#fff8f0,#e5f8f8);
}
.new-guide-common__right {
      position: relative;
      padding-top: 104px;
      flex: 1;
      height: 100%;
}
.new-guide-common__cont {
      padding: 20px 100px 0;
      height: 520px;
      overflow-y: scroll;
}
.new-guide-common__title {
      position: absolute;
      top: 40px;
      left: 100px;
      margin-bottom: 16px;
      font-size: 16px;
      font-weight: 700;
      color: #222;
      line-height: 22px;
}
.new-guide-common__progress {
      position: absolute;
      top: 78px;
      left: 100px;
      margin-bottom: 36px;
      width: 560px;
      height: 4px;
      background: #ededed;
      border-radius: 2px;
}
.new-guide-common__progress-inner {
      position: absolute;
      left: 0;
      top: -1px;
      content: "";
      height: 6px;
      width: 0;
      background: #00bebd;
      border-radius: 3px;
      transition: width .2s linear;
}
.new-guide-base-info {
      width: 100%;
}

.el-form-ite {
  margin-bottom: 18px;
}
.el-form-ite /deep/ .el-form-item__label {
      display: block;
      padding: 0px;
      color: #999;
}

.el-form-item__conten /deep/ .el-input__inner {
      padding: 5px 40px 5px 16px;
      height: 55px;
      border-radius: 8px;
      font-size: 16px;
      color: #222;
      line-height: 22px;
      font-weight: 700;
      background: #f8f8f8;
      border-color: #f8f8f8;
      box-shadow: none;
}
.el-form-item__conten /deep/ .el-input__inner:hover {
      border-color: #18c3b1;
}
.el-form-item__conten {
      padding-bottom: 15px;
}
.el-form-item__conten /deep/ .el-radio-button__inner {
      width: 235px;
      height: 54px;
      font-size: 16px;
      line-height: 54px;
      border-radius: 8px;
      background: #f8f8f8;
      border-color: #f8f8f8;
      color: #222;
      font-weight: 700;
}
.el-form-ite .item-label {
      float: left;
      padding: 0px;
      color: #999;
      font-size: 14px;
}
.el-radio-group .radio-item {
  width: 235px;
    height: 54px;
    font-size: 16px;
    line-height: 54px;
    border-radius: 8px;
    background: #f8f8f8;
    border-color: #f8f8f8;
    color: #222;
    font-weight: 700;
}
.el-form-item .item-label {
    color: #999;
    line-height: 20px;
    font-size: 14px;
    float: none;
    padding: 0 0 10px;
    text-align: left;
    vertical-align: middle;
    box-sizing: border-box;
}
.el-radio-button {
    width: 260px;
    height: 54px;
    font-size: 16px;
    line-height: 54px;
    border-radius: 8px;
    background: #f8f8f8;
    border-color: #f8f8f8;
    color: #222;
    font-weight: 700;
    display: inline-block;
    border: 1px solid #e3e7ed;
    min-width: 148px;
    text-align: center;
    cursor: pointer;
    margin-right: 19px;
    white-space: nowrap;
    position: relative;
    vertical-align: middle;
}
.el-radio-button:last-child {
    margin-right: 0px;
    margin-left: 20px;
}
.el-radio-group .radio_checked {
    background-color: #e1f7f7;
    border-color: #00bebd;
}
.el-radio-group .el-radio-button:hover {
  border-color: #18c3b1;
}
.el-textare /deep/ .el-textarea__inner {
  min-height: 400px;
  padding: 5px 16px 0;
  font-size: 16px;
  background-color: #f8f8f8;
}
.el-textare /deep/ .el-textarea__inner:hover {
  border-color: #f8f8f8;
}
.el-selec {
    width: 260px;
    height: 54px;
    font-size: 16px;
    line-height: 54px;
    border-radius: 8px;
    background: #f8f8f8;
    border-color: #f8f8f8;
    color: #222;
    font-weight: 700;
    display: inline-block;
    border: 1px solid #e3e7ed;
    min-width: 148px;
    text-align: center;
    cursor: pointer;
    margin-right: 20px;
    white-space: nowrap;
    position: relative;
    vertical-align: middle;
}
.el-selec  {
    margin-right: 0px;
}
.el-selec /deep/ .el-input__inner {
  -webkit-appearance: none;
    background-color: #f8f8f8;
    background-image: none;
    border-radius: 8px;
    border: 1px solid #e3e7ed;
    box-sizing: border-box;
    color: #222;
    display: inline-block;
    font-size: 16px;
    height: 54px;
    line-height: 54px;
    outline: 0;
    padding: 0px 35px;
    transition: border-color .2s cubic-bezier(.645,.045,.355,1);
    width: 100%;
}
.el-selec {
    margin-right: 0px;
}
.el-selec:last-child {
    margin-left: 44px;
}
.new-guide-upload-avatar__main {
    text-align: center;
}
.upload-input {
    display: none;
}
.new-guide-upload-avatar__upload-img {
    margin: 0 auto 20px;
    width: 160px;
    height: 160px;
    cursor: pointer;
}
.new-guide-upload-avatar__upload-img img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
}
img {
    border: none;
    overflow-clip-margin: content-box;
    overflow: clip;
}
.new-guide-upload-avatar__upload-btn {
    position: relative;
    width: 96px;
    height: 32px;
    background: #00bebd;
    border-radius: 4px;
    border: none;
    color: #fff;
    cursor: pointer;
}
.new-guide-upload-avatar__desc {
  font-size: 16px;
  line-height: 1.5;
    margin: 40px auto 34px;
    width: 320px;
}
.new-guide-upload-avatar__default {
    display: flex;
    justify-content: space-between;
}
.new-guide-upload-avatar__item {
    width: 102px;
    height: 102px;
    line-height: 102px;
    border: 2px solid #fff;
    border-radius: 50%;
    text-align: center;
    box-sizing: border-box;
    cursor: pointer;
}
.new-guide-upload-avatar__item img {
    margin-top: -5px;
    display: inline-block;
    width: 90px;
    height: 90px;
    border-radius: 50%;
}
.new-guide-upload-avatar__item--active, .new-guide-upload-avatar__item:hover {
    border: 2px solid #00bebd;
}

</style>

