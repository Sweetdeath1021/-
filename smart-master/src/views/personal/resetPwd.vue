<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="旧密码" prop="userPassword">
      <el-input v-model="user.userPassword" placeholder="请输入旧密码" type="password" />
    </el-form-item>
    <el-form-item label="新密码" prop="userFirm">
      <el-input v-model="user.userFirm" placeholder="请输入新密码" type="password" />
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" placeholder="请确认密码" type="password" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import $ from 'jquery'

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.userFirm !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      test: "1test",
      user: {
        userPassword: undefined,
        userFirm: undefined,
        confirmPassword: undefined,
      },
      // 表单校验
      rules: {
        userPassword: [
          { required: true, message: "旧密码不能为空", trigger: "blur" }
        ],
        userFirm: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
          { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // updateUserPwd(this.user.oldPassword, this.user.newPassword).then(
          //   response => {
          //     if (response.code === 200) {
          //       this.msgSuccess("修改成功");
          //     }
          //   }
          // );
          const data = {
            rawPassword: this.user.userPassword,
            newPassword: this.user.userFirm
          }
          $.ajax({
            url: this.baseUrl + '/grading/user/change',
            type: 'put',
            data: JSON.stringify(data),
            contentType: "application/json",
            headers: {
              'ngrok-skip-browser-warning': 'true',
              'token': localStorage.getItem('token')
            },
            success: res => {
              console.log(res);
              if (res.status === 200) {
                this.$message({
                  message: '修改成功',
                  type: 'success'
                })
              } else {
                this.$message({
                  message: '请输入正确的旧密码',
                  type: 'error'
                })
              }
            },
            error: () => {
              alert("请求发送失败，请检查网络连接或服务器状态。")
            }
          })
        }
      });
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    }
  }
};
</script>
