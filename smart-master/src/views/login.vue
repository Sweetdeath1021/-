<template>
	<div class="center">
		<div class="logon">
			<div class="loginLeft">
				<ul class="leftList">
					<li>
						<svg class="icon iconLogin" aria-hidden="true">
							<use xlink:href="#icon-gaoxiao"></use>
						</svg>
						<em>高效评阅</em>
						<span>深度学习技术精准快速阅卷</span>
					</li>
					<li>
						<svg class="icon iconLogin" aria-hidden="true">
							<use xlink:href="#icon-zhinengshuju"></use>
						</svg>
						<em>智能评阅</em>
						<span>文心大模型提供个性化学科指导</span>
					</li>
					<li>
						<svg class="icon iconLogin" aria-hidden="true">
							<use xlink:href="#icon-genzong"></use>
						</svg>
						<em>学情跟踪</em>
						<span>数字化记录学生进步情况</span>
					</li>
					<li>
						<svg class="icon iconLogin" aria-hidden="true">
							<use xlink:href="#icon-qiehuan"></use>
						</svg>
						<em>学科切换</em>
						<span>更具专业性和差异化的评阅方式</span>
					</li>
				</ul>
			</div>
			<div class="loginRight">
				<button class="back" v-if="!login" @click="back"><i class="el-icon-back"></i>返回</button>
				<div class="overlaylong-Signin">
					<h3 class="overlaylongH2" v-if="login">智能阅卷</h3>
					<h3 class="overlaylongH2" v-else>找回密码</h3>
					<!-- <input type="text" placeholder="账号">
					<input type="text" placeholder="密码">
					<input type="text" placeholder="确认密码"> -->
					<el-form :model="form" ref="form" :rules="rules" class="form" v-show="login">
						<el-form-item prop="userAccount">
							<el-input v-model="form.userAccount" placeholder="手机号/学号"></el-input>
						</el-form-item>
						<el-form-item prop="userPassword">
							<el-input v-model="form.userPassword" placeholder="密码" type="password" autocomplete="off" show-password></el-input>
						</el-form-item>
					</el-form>
					<el-form :model="getBack" ref="getBack" :rules="rules2" class="form" v-show="!login">
						<el-form-item prop="userAccount">
							<el-input v-model="getBack.userAccount" placeholder="手机号/学号" autocomplete="off"></el-input>
						</el-form-item>
						<el-form-item prop="userEmail">
							<el-input v-model="getBack.userEmail" placeholder="邮箱" autocomplete="off"></el-input>
						</el-form-item>
						<el-form-item prop="newPassword">
							<el-input v-model="getBack.newPassword" placeholder="新密码" autocomplete="off"></el-input>
						</el-form-item>
					</el-form>
					<div class="row">
						<span @click="toggle1" v-if="login">立即注册</span>
						<span @click="toggle2" v-if="login">忘记密码</span>
					</div>
					<button class="inupbutton" @click="loginSubmit" v-if="login">登录</button>
					<button class="inupbutton" @click="backSubmit" v-else>找回密码</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	name: 'LoginSmart',
	data() {
		return {
			login: true,
			form: {
				userAccount: '',
				userPassword: ''
			},
			getBack: {
				userAccount: '',
				userEmail: '',
				newPassword: ''
			},
			rules: {
				userAccount: [
					{ required: true, message: '请输入手机号或学号', trigger: 'blur' },
					// { pattern: /^1[3|5|7|8|9]\d{9}$/, message: '请输入正确的号码格式', trigger: 'blur' }
				],
				userPassword: [
					{ required: true, message: "请输入密码", trigger: "blur" },
					{ min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
				]
			},
			rules2: {
				userAccount: [
					{ required: true, message: '请输入手机号', trigger: 'blur' },
					{ pattern: /^1[3|5|7|8|9]\d{9}$/, message: '请输入正确的号码格式', trigger: 'blur' }
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
				newPassword: [
					{ required: true, message: "请输入新密码", trigger: "blur" },
					{ min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
				]
			}
		}
	},
	methods: {
		toggle1() {
			this.$refs.form.resetFields()
			this.$router.push('/register')
		},
		toggle2() {
			this.$refs.form.clearValidate()
			this.login = false
		},
		loginSubmit() {
			// this.$router.push('/grading')
			// this.$router.push('/home')
			this.$refs.form.validate(valid => {
				if (valid) {
					const loginMessage = this.$message({
						type: 'success',
						duration: 0,
						dangerouslyUseHTMLString: true,
						iconClass: 'el-icon-loading',
						customClass: 'messageLoading',
						message: '&nbsp;&nbsp;正在登录'
					})
					this.$.ajax({
						url: this.baseUrl + "/grading/user/login",
						type: 'post',
						data: JSON.stringify(this.form),
						contentType: "application/json",
						success: res => {
							console.log(res);
							if (res.status == 200) {
								loginMessage.close()
								this.$message.success('登录成功')
								this.$router.push('/index')
								localStorage.setItem('token', res.data.token)
								localStorage.setItem('school', res.data.school)
								localStorage.setItem('userId', res.data.userId)
								localStorage.setItem('roleId',res.data.roleId)
							}
							else {
								loginMessage.close()
								this.$message.error('手机号/学号或密码错误')
							}
						},
						error: () => {
							loginMessage.close()
							this.$message.error('登录失败，请稍后再试')
						}
					})
				}
			})
		},
		backSubmit() {
			this.$refs.getBack.validate(valid => {
				if (valid) {
					const data = {
						userAccount: this.getBack.userAccount,
						userEmail: this.getBack.userEmail,
						userPassword: this.getBack.newPassword
					}
					const getBackMessage = this.$message({
						type: 'success',
						duration: 0,
						dangerouslyUseHTMLString: true,
						iconClass: 'el-icon-loading',
						customClass: 'messageLoading',
						message: '&nbsp;&nbsp;正在修改'
					})
					this.$.ajax({
						url: this.baseUrl + "/grading/user/forget",
						type: 'post',
						data: JSON.stringify(data),
						contentType: "application/json",
						success: res => {
							console.log(res);
							getBackMessage.close()
							if (res.status == 200) {
								this.$message.success('修改成功')
								this.login = true
								this.form.userAccount = this.getBack.userAccount
							}
							else {
								this.$message.error('手机号或邮箱错误')
							}
						},
						error: () => {
							getBackMessage.close()
							this.$message.error('找回失败，请稍后再试')
						}
					})
				}
			})
		},
		back() {
		this.login = true
		this.$refs.getBack.resetFields()
		}
	},
	mounted() {
		
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
.center {
	display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
	min-height: 100vh;
}
.back {
	position: absolute;
	top: 5%;
	left: 5%;
	color: #999;
	background-color: transparent;
	outline: none;
	border: none;
	cursor: pointer;
}
.back:hover {
	color: #666;
}
.el-input{
	width: 270px;
	border: #666;
}
.form /deep/ .el-input__inner {
	background: #f6f6f6b6;
}

.identity  {
	display: flex;
	justify-content: space-around;
	width: 98%;
    margin-bottom: 20px;
    padding: 4px;
    background: #f6f6f6b6;
    border-radius: 12px;
    overflow: hidden;
	text-align: center;
}
.identity li {
	width: 50%;
	height: 34px;
	line-height: 34px;
	border-radius: 10px;
    cursor: pointer;
    transition: all .2s linear;
}
.identity li:hover {
	color: #00a6a7;
}
.identity .active {
	color: #00a6a7;
    background: #fff;
    cursor: default;
}
.row {
	display: flex;
	width: 270px;
	justify-content: space-between;
}
.row span {
	font-size: 13px;
	color: #A6A6A6;
	cursor: pointer;
}
.row span:hover {
	color: #838383;
}
.iconLogin {
	float: left;
}

.leftList {
	padding-left: 25px;
	padding-right: 25px;
}

.leftList em {
	display: block;
	text-align: left;
	margin-left: 53px;
	font-size: 14px;
	font-weight: 500;
	font-style: normal;
	color: #666;
}

.leftList span {
	display: block;
	text-align: left;
	margin-left: 53px;
	margin-top: 2px;
	font-size: 13px;
	color: #999;
}

.leftList li {
	margin-top: 45px;
}

.logon {
	background-color: transparent;
	border-radius: 15px;
	/* box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22); */
	box-shadow: 0 0px 20px rgba(0, 0, 0, 0.20), 0 0px 20px rgba(0, 0, 0, 0.22);
	/* position: relative;
		overflow: hidden; */
	width: 720px;
	max-width: 100%;
	min-height: 480px;
	display: flex;
	background: -webkit-linear-gradient(right, #8dbaf4, #78ecd4);
	/* z-index: 10; */
}

.loginRight {
	position: relative;
	border-radius: 0px 10px 10px 0px;
	width: 60%;
	background-color: #ffffff94;
	/* transform: translateX(100%);
		transition: transform 0.6s ease-in-out; */
	display: flex;
	align-items: center;
	justify-content: center;
}

.overlaytitleH2 {
	font-size: 30px;
	color: #fff;
	margin-top: 20px;
}

.overlaytitleP {
	font-size: 15px;
	color: #fff;
	margin-top: 20px;
}

.loginLeft {
	border-radius: 0px 10px 10px 0px;
	width: 40%;
	background-color: rgba(0, 0, 0, 0);
	display: flex;
	flex-direction: column;
	align-items: center;
	/* justify-content: center; */
	/* transform: translateX(-100%);
		transition: transform 0.6s ease-in-out; */
}

.overlaytitle-Signup {
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
}

.buttongohs {
	width: 180px;
	height: 40px;
	border-radius: 50px;
	border: 1px solid #fff;
	color: #fff;
	font-size: 15px;
	text-align: center;
	line-height: 40px;
	margin-top: 40px;
}

.overlaylongH2 {
	font-size: 25px;
	color: black;
	font-weight: 500;
	margin-bottom: 20px;
	/* width: 250px; */
}

.overlaylong-Signin {
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
}

.overlaylong-Signup {
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
}

/* input {
	background-color: #eee;
	border: none;
	padding: 12px 15px;
	margin: 10px 0;
	width: 240px;
} */

h3 {
	font-size: 10px;
	margin-top: 10px;
	cursor: default;
}

.inupbutton {
	background-color: #29eac4;
	border: none;
	width: 180px;
	height: 40px;
	border-radius: 50px;
	font-size: 15px;
	color: #fff;
	text-align: center;
	line-height: 40px;
	margin-top: 30px;
	cursor: pointer;
}
.inupbutton:hover {
	background-color: #25f1c9;
}
</style>