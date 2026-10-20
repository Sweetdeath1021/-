<template>
    <div id="base">
        <div class="box">
            <el-form ref="form" :model="form" label-width="82px">
                <el-form-item label="评语名称">
                  <el-input v-model="form.commentName" placeholder="例如：期中评语、期末评语"></el-input>
                </el-form-item>
                <el-form-item label="选择班级">
                  <el-radio-group v-model="form.studentClass" @input="getStudentList">
                    <el-radio v-for="(item,index) in classList" :key="index" :label="item"></el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="学生列表">
                  <el-radio-group v-model="form.student" class="studentsCheckbox">
                    <el-radio v-for="(item,index) in studentList" :key="index" :label="item.userName" border></el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="自定义评语参数" class="custom">
                  <el-button type="primary" @click="dialogVisible = true" plain>开始自定义</el-button>
                  <el-dialog
                    title="自定义评语参数"
                    :visible.sync="dialogVisible"
                    width="67%">
                    <div style="display: flex;">
                      <div class="dialogChildren">
                        <div class="littleBox">
                          <div style="display: flex;justify-content: space-between;align-items: center;">
                            <span class="theTitle">随机性</span>
                            <input :value="random" @input="random = +$event.target.value" type="number" :min="0" :max="1" :step="0.1"
                             style="width: 40px;height: 17px;outline: none;border: none;box-shadow: 0 1px 4px 0 #00000024;border-radius: 8px;padding: 4px 14px;font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;"
                             @change="change">
                          </div>
                          <p class="littleP">值越大，评语随机性越高，雷同率越小</p>
                          <el-slider v-model="random" :min="0" :max="1" :step="0.1" class="random"></el-slider>
                        </div>
                        <div class="littleBox">
                          <div style="display: flex;justify-content: space-between;align-items: center;">
                            <span class="theTitle">学科</span>
                          </div>
                          <p class="littleP">请选择你需要评阅的学科！</p>
                          <el-select v-model="theSubject" class="theSubject">
                            <el-option
                              v-for="(item,index) in subjects"
                              :key="index"
                              :label="item"
                              :value="item">
                            </el-option>
                          </el-select>
                        </div>
                        <div class="littleBox">
                          <div style="display: flex;justify-content: space-between;align-items: center;">
                            <span class="theTitle">评语要素</span>
                          </div>
                          <el-checkbox-group v-model="checkboxGroup" size="small" class="checkboxGroup" style="margin-top: 10px;">
                            <el-checkbox label="总分及要点分" border></el-checkbox>
                            <el-checkbox label="评分依据" border></el-checkbox>
                            <el-checkbox label="分数突出显示" border></el-checkbox>
                            <el-checkbox label="要点突出显示" border></el-checkbox>
                            <el-checkbox label="提升建议" border></el-checkbox>
                          </el-checkbox-group>
                        </div>
                      </div>
                      <div class="dialogChildren">
                        <div class="littleBox">
                          <div style="display: flex;justify-content: space-between;align-items: center;">
                          <span class="theTitle">评语风格</span>
                        </div>
                        <p class="littleP">选择评语风格</p>
                        </div>
                        <div class="radioParent">
                          <el-radio-group v-model="style1" size="small" class="radioboxGroup">
                            <el-radio label="1" border>正式</el-radio>
                            <el-radio label="2" border>非正式</el-radio>
                          </el-radio-group>
                        </div>
                        <div class="radioParent">
                          <el-radio-group v-model="style2" size="small" class="radioboxGroup">
                          <el-radio label="1" border>鼓励性</el-radio>
                          <el-radio label="2" border>批判性</el-radio>
                        </el-radio-group>
                        </div>
                        <div class="radioParent">
                          <el-radio-group v-model="style3" size="small" class="radioboxGroup">
                          <el-radio label="1" border>专业性</el-radio>
                          <el-radio label="2" border>通俗性</el-radio>
                        </el-radio-group>
                        </div>
                        <div class="radioParent">
                          <el-radio-group v-model="style4" size="small" class="radioboxGroup">
                          <el-radio label="1" border>启发性</el-radio>
                          <el-radio label="2" border>指导性</el-radio>
                        </el-radio-group>
                        </div>
                        <div class="radioParent">
                          <el-radio-group v-model="style5" size="small" class="radioboxGroup">
                          <el-radio label="1" border>有同情心地</el-radio>
                          <el-radio label="2" border>客观性</el-radio>
                        </el-radio-group>
                        </div>
                        <div class="radioParent">
                          <el-radio-group v-model="style6" size="small" class="radioboxGroup">
                          <el-radio label="1" border>简要地</el-radio>
                          <el-radio label="2" border>丰富地</el-radio>
                        </el-radio-group>
                        </div>
                      </div>
                      <div class="dialogChildren">
                        <div class="littleBox">
                          <div style="display: flex;justify-content: space-between;align-items: center;">
                          <span class="theTitle">其他评语要求</span>
                          </div>
                        </div>
                        <div class="littleBox">
                          <el-input
                            type="textarea"
                            :rows="15"
                            placeholder="请输入内容"
                            v-model="individuality"
                            class="individuality">
                          </el-input>
                        </div>
                      </div>
                    </div>
                    <span slot="footer" class="dialog-footer">
                      <el-button @click="dialogVisible = false">取 消</el-button>
                      <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
                    </span>
                  </el-dialog>
                </el-form-item>
                <el-form-item class="title">
                  <div style="font-size: 16px;color: #606266;">为&nbsp;<span style="color: #00CE95;">{{ form.student.length==0?"":form.student }}</span>&nbsp;添加评语关键词({{ form.labels.length }}/10)</div>
                  <div style="width: 100%;margin: 10px auto;border-radius: 8px;border: 1px solid #86868659;padding: 15px;">
                        <el-checkbox-group v-model="form.labels" class="studentsCheckbox" style="width: 100%;" @change="checkboxChange">
                          <el-form-item label="自定义标签">
                            <el-popover
                              placement="right"
                              width="200"
                              v-model="visible"
                              title="添加标签"
                              class="checkedLabels addLabels" style="border: 1px dashed #909090;padding: 0">
                              <span slot="reference" style="display: inline-block;padding: 9px 20px">+ 添加标签</span>
                              <el-input v-model="labelContent" placeholder="请输入内容" style="margin-bottom: 8px;"></el-input>
                              <div style="text-align: right; margin: 0">
                                <el-button circle icon="el-icon-close" size="mini" @click="visible=false,labelContent=''"></el-button>
                                <el-button type="success" circle icon="el-icon-check" size="mini" @click="addLabels"></el-button>
                              </div>
                            </el-popover>
                            <el-checkbox v-for="(item,index) in addedLabels" :key="index" :label="item" border style="position: relative;">{{ item }}<i class="el-icon-error cancelLabel" style="position: absolute;top: 1px;right: 1px;color: #a4a4a4;" @click="deleteLabels(item)"></i></el-checkbox>
                          </el-form-item>
                          <el-form-item label="学习成绩">
                            <el-checkbox label="成绩优异" border></el-checkbox>
                            <el-checkbox label="算数很棒" border></el-checkbox>
                            <el-checkbox label="有潜力" border></el-checkbox>
                            <el-checkbox label="学习认真" border></el-checkbox>
                            <el-checkbox label="普通话标准" border></el-checkbox>
                            <el-checkbox label="语言逻辑很棒" border></el-checkbox>
                            <el-checkbox label="有语言天分" border></el-checkbox>
                            <el-checkbox label="再接再励" border></el-checkbox>
                            <el-checkbox label="进步空间大" border></el-checkbox>
                          </el-form-item>
                          <el-form-item label="学习态度">
                            <el-checkbox label="积极注意力集中" border></el-checkbox>
                            <el-checkbox label="有学习热情" border></el-checkbox>
                            <el-checkbox label="自律性强" border></el-checkbox>
                            <el-checkbox label="大声朗读" border></el-checkbox>
                            <el-checkbox label="及时完成作业" border></el-checkbox>
                            <el-checkbox label="与同学互动容易" border></el-checkbox>
                            <el-checkbox label="喜欢拖延" border></el-checkbox>
                            <el-checkbox label="缺乏学习兴趣" border></el-checkbox>
                            <el-checkbox label="不认真完成作业" border></el-checkbox>
                          </el-form-item>
                          <el-form-item label="行为表现">
                            <el-checkbox label="遵纪守则" border></el-checkbox>
                            <el-checkbox label="有组织性" border></el-checkbox>
                            <el-checkbox label="诚实守信" border></el-checkbox>
                            <el-checkbox label="礼貌待人" border></el-checkbox>
                            <el-checkbox label="乐于助人" border></el-checkbox>
                            <el-checkbox label="整洁的个人卫生" border></el-checkbox>
                            <el-checkbox label="热爱劳动" border></el-checkbox>
                            <el-checkbox label="旷课" border></el-checkbox>
                          </el-form-item>
                          <el-form-item label="书写习惯">
                            <el-checkbox label="字迹清晰" border></el-checkbox>
                            <el-checkbox label="书写整齐" border></el-checkbox>
                            <el-checkbox label="字母工整" border></el-checkbox>
                            <el-checkbox label="书写规范" border></el-checkbox>
                            <el-checkbox label="需要控制书写速度" border></el-checkbox>
                            <el-checkbox label="书写不清晰" border></el-checkbox>
                            <el-checkbox label="书写不规范" border></el-checkbox>
                          </el-form-item>
                          <el-form-item label="课堂表现">
                            <el-checkbox label="主动回答问题" border></el-checkbox>
                            <el-checkbox label="积极参与讨论" border></el-checkbox>
                            <el-checkbox label="参与课堂提问" border></el-checkbox>
                            <el-checkbox label="积极参与课题活动" border></el-checkbox>
                            <el-checkbox label="注意力集中" border></el-checkbox>
                            <el-checkbox label="遵守课堂规则" border></el-checkbox>
                            <el-checkbox label="不提问" border></el-checkbox>
                            <el-checkbox label="很少回答问题" border></el-checkbox>
                          </el-form-item>
                          <el-form-item label="运动体能">
                            <el-checkbox label="热爱体育活动" border></el-checkbox>
                            <el-checkbox label="运动技能丰富" border></el-checkbox>
                            <el-checkbox label="身体协调能力强" border></el-checkbox>
                            <el-checkbox label="不爱运动" border></el-checkbox>
                          </el-form-item>
                        </el-checkbox-group>
                    <div style="border-top: 1px dashed #86868659;margin: 10px 0;padding-top: 15px;">
                      <div class="checkedLabels" v-for="(item,index) in form.labels" :key="index">{{ item }}<i class="el-icon-error cancelLabel" style="position: absolute;top: 1px;right: 1px;color: #a4a4a4;" @click="cancelLabel(item)"></i></div>
                    </div>
                    <div style="margin-top: 10px;text-align: right;">
                      <div>大致字数<el-input-number size="small" v-model="form.fontNum" :min="100" :max="300" style="margin: 0 10px;"></el-input-number><el-button type="primary" size="small" plain @click="intelComment">立即生成</el-button></div>
                    </div>
                  </div>
                </el-form-item>
                <div>智能生成&nbsp;<span style="color: #00CE95;">{{ form.student.length==0?"":form.student }}</span>&nbsp;的评语</div>
                <div style="margin: 20px 0;border: 1px solid #86868659;border-radius: 8px;position: relative;"
                    v-loading="commentLoading"
                    element-loading-text="正在生成"
                    element-loading-background="rgba(255, 255, 255, 0.5)">
                  <el-input
                    type="textarea"
                    :autosize="{ minRows: 10}"
                    v-model="form.textarea"
                    class="textarea">
                  </el-input>
                  <el-button round style="position: absolute;right: 10px;bottom: 10px;"
                    v-clipboard:copy="form.textarea" 
                    v-clipboard:success="onCopy" 
                    v-clipboard:error="onError" >一键复制<i class="el-icon-s-order" style="margin-left: 2px;"></i>
                  </el-button>
                </div>
            </el-form>
            <div style="text-align: right;">
              <el-button type="primary" plain @click="publish">发布并点评下一位同学</el-button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'multipleRemark',
    data() {
      return {
        visible: false,//添加标签弹出框的控制
        dialogVisible:false,//对话框
        form: {
          commentName: '',
          studentClass: '',
          student: '',//被选中的学生名
          studentId:'',//被选中的学生id
          labels: [],//被选中的标签
          fontNum: 100,
          textarea:''
        },
        commentLoading:false,
        studentList: [],
        classList:[],
        labelContent: '',
        addedLabels: [],//自定义的标签
        random: 0.5,
        subjects: ['语文', '数学', '英语', '物理', '化学', '生物', '历史', '政治', '地理', '其他（根据题目自行评阅）'],
        theSubject: '其他（根据题目自行评阅）',
        checkboxGroup: ['总分及要点分','评分依据','分数突出显示','要点突出显示','提升建议'],
        style1:"1",
        style2:"1",
        style3:"1",
        style4:"1",
        style5:"2",
        style6: "1",
        individuality: "回答更有结构化",//个性化要求
      }
    },
    methods: {
      intelComment() {
        this.commentLoading = true
        this.$.ajax({
          url: this.baseUrl + "/grading/comment/intelComment",
          type: 'post',
          data: JSON.stringify({
            userName: this.form.student,
            tags:this.form.labels
          }),
          headers: {
              'ngrok-skip-browser-warning': 'true',
              'token': localStorage.getItem('token')
          },
          contentType: "application/json",
          success: res => {
            console.log(this.studentId);
              console.log(res);
            if (res.status == 200) {
                this.commentLoading = false
                this.form.textarea = res.data
              }
              else {
                  this.$message.warning(res.msg)
              }
          },
          error: () => {
              this.loading = false
              this.$message.error('请求发送失败，请稍后再试！')
          }
        })
      },
      onCopy() {
        this.$message.success('复制成功')
      },
      onError() {
        this.$message.error('复制失败！')
      },
      cancelLabel(e) {
        this.form.labels = this.form.labels.filter(item => item != e)
      },
      checkboxChange(e) {
        if (this.form.student == '') {
          this.$message.error("请先选择学生")
          this.form.labels = []
        }
        if (e.length > 10) {
          this.$message.error("最多选10个")
          this.form.labels.pop()
        }
      },
      addLabels() {
        if (this.addedLabels.includes(this.labelContent)) {
          this.$message.warning('已添加过此标签')
        } else if (this.labelContent == '') {
          this.$message.warning('标签不能为空')
        } else {
          this.addedLabels.push(this.labelContent)
          if (this.form.student != ''&&this.form.labels.length < 10) {
            this.form.labels.push(this.labelContent)
          }
          this.visible = false
          this.labelContent = ''
        }
      },
      deleteLabels(e) {
        this.addedLabels = this.addedLabels.filter(item => item != e)
        this.form.labels = this.form.labels.filter(item => item != e)
      },
      change() {
        console.log(this.random);
      },
      getStudentList() {
        this.$.ajax({
          url: this.baseUrl + "/grading/comment/studentList",
          type: 'post',
          data: JSON.stringify({ studentClass: this.form.studentClass }),
          headers: {
              'ngrok-skip-browser-warning': 'true',
              'token': localStorage.getItem('token')
          },
          contentType: "application/json",
          success: res => {
              console.log(res);
              if (res.status == 200) {
                  this.studentList = res.data
              }
              else {
                  this.$message.warning(res.msg)
              }
          },
          error: () => {
              this.loading = false
              this.$message.error('请求发送失败，请稍后再试！')
          }
        })
      },
      publish() {
        this.studentList.forEach(item => {
          if (item.userName == this.form.student) {
            this.form.studentId = item.userId
          }
        })
        this.$.ajax({
          url: this.baseUrl + "/grading/comment/put",
          type: 'post',
          data: JSON.stringify({
            commentName: this.form.commentName,
            userId: this.form.studentId,
            commentContent:this.form.textarea
          }),
          headers: {
              'ngrok-skip-browser-warning': 'true',
              'token': localStorage.getItem('token')
          },
          contentType: "application/json",
          success: res => {
              if (res.status == 200) {
                this.studentList = res.data
                this.$message.success('评语发布成功')
              }
              else {
                  this.$message.warning(res.msg)
              }
          },
          error: () => {
              this.loading = false
              this.$message.error('请求发送失败，请稍后再试！')
          }
        })
      },
      getUserId(id) {
        this.form.studentId = id
        console.log(id);
      }
  },
  created() {
    this.$.ajax({
      url: this.baseUrl + "/grading/comment/selectClass",
      type: 'get',
      headers: {
        'ngrok-skip-browser-warning': 'true',
        'token': localStorage.getItem('token')
      },
      contentType: "application/json",
      success: res => {
          console.log(res);
          if (res.status == 200) {
              this.classList = res.data
          }
          else {
              this.$message.warning(res.msg)
          }
      },
      error: () => {
          this.loading = false
          this.$message.error('请求发送失败，请稍后再试！')
      }
    })
  },
  mounted() {
    const labels = this.$('.el-form-item__label')
    labels.each((index,elem) => {
        this.$(elem).height(this.$(elem).siblings('.el-form-item__content').height())
    })
  }
}
</script>

<style scoped>
/* 隐藏input的上下箭头 */
input[type=number]::-webkit-inner-spin-button, 
input[type=number]::-webkit-outer-spin-button { 
  -webkit-appearance: none;
  margin: 0; 
}

#base {
    overflow: auto;
    width: 100vw;
    height: calc(-64px + 100vh);
    padding: 20px 80px;
}
.box {
    padding: 40px;
    border-radius: 8px;
    background-color: #ffffffd8;
}
.custom /deep/ .el-form-item__label{
  width: 110px!important;
}

.addLabels /deep/ .el-input.is-active .el-input__inner, .el-input__inner:focus {
  border-color: #409EFF!important;
}
.studentsCheckbox /deep/ .el-radio__input {
    display: none;
}
.studentsCheckbox /deep/ .el-radio {
    background-color: #e5e5e5;
    margin-right: 10px;
    margin-bottom: 10px;
    transition: all linear .3s;
    border: none;
}
.studentsCheckbox /deep/ .el-radio:hover {
  background-color:#d0d0d0;
}
.studentsCheckbox /deep/ .el-radio__label {
    transition: all linear .3s;
}
.studentsCheckbox /deep/ .el-radio.is-bordered.is-checked {
    background-color: #b8b8b8 !important;
}
.studentsCheckbox /deep/ .el-radio__input.is-checked+.el-radio__label {
    color: #fff;
}
.el-radio.is-bordered+.el-radio.is-bordered {
    margin-left: 0;
}


.studentsCheckbox /deep/ .el-checkbox__input {
    display: none;
}
.studentsCheckbox /deep/ .el-checkbox {
    background-color: #f1f1f1;
    margin-left: 0!important;
    margin-right: 15px;
    margin-bottom: 10px;
    transition: all linear .3s;
    border: none;
}
.studentsCheckbox /deep/ .el-checkbox:hover {
  background-color:#d8d8d8;
}
.studentsCheckbox /deep/ .el-checkbox__label {
    transition: all linear .3s;
}
.studentsCheckbox /deep/ .el-checkbox.is-bordered.is-checked {
    background-color: #b8b8b8 !important;
}
.studentsCheckbox /deep/ .el-checkbox__input.is-checked+.el-checkbox__label {
    color: #fff;
}

.title {
  margin-bottom: 10px!important;
}
.title /deep/ .el-form-item__content {
  margin-left: 0!important;
}
/* .el-form-item .el-form-item {
    margin-bottom: 10px;
} */

.textarea /deep/ .el-textarea__inner {
  border: none;
  resize: none;
  border-radius: 8px;
  background-color: #f6f6f6;
}
.checkedLabels {
  display: inline-block;
  position: relative;
  background-color: #f1f1f1;
  margin-right: 15px;
  margin-bottom: 10px;
  border: none;
  box-sizing: border-box;
  padding: 9px 20px;
  border-radius: 4px;
  line-height: normal;
  height: 40px;
  color: #606266;
  font-weight: 500;
  font-size: 14px;
  cursor: pointer;
}
.cancelLabel:hover {
  color: #535353!important;
}
.addLabels:hover {
  background-color: #dfdfdf;
}
.textarea /deep/ .el-textarea__inner{
  padding-bottom: 60px;
  /* white-space: pre-wrap; */
}
.dialogChildren {
  display: flex;
  flex-direction: column;
  flex: 1;
  /* height: 300px; */
  border-radius: 8px;
  margin: 0 5px;
  background-color: #fff;
}
.custom /deep/ .el-dialog {
  margin-top: 15vh!important;
  background-color: #F9FAFB;
}
.custom /deep/ .el-dialog__header {
  padding-top: 10px;
}
.custom /deep/ .el-dialog__body {
  padding: 0 20px;
}
.littleBox {
  padding: 10px 10px 0;
}
.theTitle {
  padding: 0 5px;
  line-height: 30px;
  border-radius: 5px;
  font-weight: 700;
  font-size: 14px;
  background-color: #E0E7FF;
  color: #4E46E6;
}
.littleP {
  margin: 7px 0 0;
  font-weight: 300;
  font-size: 11px;
  line-height: 11px;
}
.random /deep/ .el-slider__bar {
  background-color:#4E46E6;
}
.random /deep/ .el-slider__button {
  border-color: #4E46E6;
}
.theSubject {
  margin: 10px 0;
  width: 100%;
}
.theSubject /deep/ .el-input__inner{
  border:none;
  border-radius: 8px;
  color: #3d3d3d;
  box-shadow: 0 1px 4px 0 #00000024;
}

.checkboxGroup /deep/ .el-checkbox{
  margin: 0!important;
  margin-right: 8px!important;
  border-radius: 6px;
  padding: 5px 10px;
  line-height: 22px;
  border: none!important;
  box-shadow: 0 1px 4px 0 #00000024;
  transition: background-color .25s;
}
.checkboxGroup /deep/ .el-checkbox.is-bordered.is-checked {
  background-color:#6266F1;
}
.checkboxGroup /deep/ .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color:#4E46E6;
  border-color: #4E46E6;
}
.checkboxGroup /deep/ .el-checkbox__input.is-checked+.el-checkbox__label {
  color: #fff;
}
.checkboxGroup /deep/ .el-checkbox__inner {
  margin-top: 1px;
}
.checkboxGroup /deep/ .el-checkbox__label {
  padding-left: 5px;
  font-size: 13px!important;
  transition: color .25s cubic-bezier(.71,-.46,.29,1.46);
}
.checkboxGroup /deep/ .el-checkbox__input.is-focus .el-checkbox_inner {
  border-color: #DCDFE6;
}


.radioParent {
  padding: 7px 10px;
  border-bottom: 1px solid #e7e6e6;
}
.radioParent:last-child {
  border: none;
}
.radioboxGroup /deep/ .el-radio{
  margin: 0!important;
  margin-right: 8px!important;
  border-radius: 0;
  padding: 5px 10px;
  line-height: 22px;
  border: none!important;
  box-shadow: 0 1px 4px 0 #00000024;
  transition: background-color .25s;
}
.radioboxGroup /deep/ .el-radio.is-bordered.is-checked {
  background-color:#6266F1;
}
.radioboxGroup /deep/ .el-radio__input.is-checked .el-radio__inner {
  background-color:#4E46E6;
  border-color: #4E46E6;
}
.radioboxGroup /deep/ .el-radio__input.is-checked+.el-radio__label {
  color: #fff;
}
.radioboxGroup /deep/ .el-radio__inner {
  margin-top: 1px;
}
.radioboxGroup /deep/ .el-radio__label {
  padding-left: 5px;
  font-size: 13px!important;
  transition: color .25s cubic-bezier(.71,-.46,.29,1.46);
}
.radioboxGroup /deep/ .el-radio__input.is-focus .el-radio_inner {
  border-color: #DCDFE6;
}
.individuality /deep/ .el-textarea__inner{
  border: none;
  box-shadow: 0 1px 4px 0 #00000024;
}
.individuality /deep/ .el-textarea__inner {
  color: #3d3d3d;
  font-weight: 600;
}
</style>