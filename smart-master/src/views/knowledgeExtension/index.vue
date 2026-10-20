<template>
    <div id="base">
        <div class="body" v-show="flag">
        <!-- 搜索筛选 -->
        <el-form :inline="true" :model="formInline" class="user-search">
            <el-form-item label="搜索：">
                <el-input size="small" v-model="formInline.paperId" placeholder="输入任务ID"></el-input>
            </el-form-item>
            <el-form-item label="">
                <el-input size="small" v-model="formInline.paperName" placeholder="输入试卷名称"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button size="small" type="primary" icon="el-icon-search">搜索</el-button>
                <!-- <el-button size="small" type="primary" icon="el-icon-plus" @click="handleEdit()">添加</el-button> -->
            </el-form-item>
            <div class="refresh"><i class="el-icon-refresh"></i>刷新</div>
        </el-form>
        <!--列表-->
        <el-table size="small" :data="listData" highlight-current-row v-loading="loading" border
            element-loading-text="拼命加载中" style="width: 100%;" class="elTable" height="calc(100vh - 325px)">
            <template slot="empty">
                <el-empty description="暂无数据"></el-empty>
            </template>
            <el-table-column align="center" type="selection" width="60">
            </el-table-column>
            <el-table-column sortable prop="paperId" label="任务ID" width="150">
            </el-table-column>
            <el-table-column prop="schoolName" label="学校" width="150">
            </el-table-column>
            <el-table-column prop="paperName" label="试卷名" width="150">
            </el-table-column>
            <el-table-column prop="paperSubject" label="科目名" width="150">
            </el-table-column>
            <el-table-column sortable prop="createTime" label="创建时间" width="150">
            </el-table-column>
            <el-table-column align="center" label="操作" min-width=150>
                <template slot-scope="scope">
                    <el-button size="mini" @click="knowledgeExtension(scope.row)"
                    background='rgba(0, 0, 0, 0.7)'
                    style="background-color: #24d3ff;color: #fff">知识延展</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
    </div>
    <div v-if="!flag" style="display: flex;position: relative;">
        <div class="left">
            <i class="el-icon-back" style="position: absolute;top: 15px;left: 20px;padding: 3px;
            font-size: 20px;background-color: #ffffff80;border-radius: 5px;z-index: 10;"
            @click="back"></i>
            <el-collapse v-model="activeName" accordion class="el-collapse">
              <el-collapse-item title="政治" name="政治">
                <div class="children" v-for="(item,index) in politics" :key="index" :data-id="item.paperId" @click="childClick(item.paperId)">{{ item.paperName }}</div>
              </el-collapse-item>
              <el-collapse-item title="物理" name="物理">
                <div class="children" v-for="(item,index) in physics" :key="index" :data-id="item.paperId" @click="childClick(item.paperId)">{{ item.paperName }}</div>
              </el-collapse-item>
              <el-collapse-item title="数学" name="数学">
                <div class="children" v-for="(item,index) in math" :key="index" :data-id="item.paperId" @click="childClick(item.paperId)">{{ item.paperName }}</div>
              </el-collapse-item>
            </el-collapse>
        </div>
        <div class="right" v-loading="recordLoading">
            <h5 class="text-uppercase" id="ai">知识延展</h5>
            <div id="chatBox111">
                <div class="other-message111">我可以为您提供学习上的帮助</div>
                <div v-for="(item,index) in records" :key="index" :class="(index+1)%2==0?'other-message111':'message111'">{{ item }}</div>
            </div>
            <div class="input-box">
                <textarea id="messageInput" placeholder="请输入您的要求" class="text" v-model="textarea"></textarea>
                <div id="messageButton">
                    <button type="button" class="button sendMessageButton" :disabled="disabled">发送</button>
                </div>
                <!-- <button type="button" id="button" data-toggle='modal' data-target='#modalSmartDiagnose'>智能</button> -->
            </div>
        </div>
    </div>
    </div>
</template>

<script>
import Typed from 'typed.js';
import Pagination from '../../components/Pagination'
export default {
    name: 'knowledgeExtension',
    data() {
        return {
            textarea: '请分析本张试卷的知识点',
            flag:true,
            listData: [], 
            formInline: {
                page: 1,
                limit: 10,
                paperId: '',
                paperName:''
            },
            loading: false,
            // 分页参数
            pageparm: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            answer: '',
            paperId: '',
            activeName: '政治',
            politics: [],
            physics: [],
            math: [],
            records: [],//历史对话,渲染用的
            newRecords:[],//最新历史对话，在逐字打完之前先存好，发请求用的
            recordLoading: false,
            disabled: false,
            sendId:'',//标记发送给ai的id，切换试卷时如果现有id也就是paperId跟sendId不一致则不执行后面的操作
        }
    },
    components: {
        Pagination
    },
    created() {
        this.getData()
        this.getSidebar('政治')
        this.getSidebar('物理')
        this.getSidebar('数学')
    },
    mounted() {
        
    },
    methods: {
        sendMessage() {
            const input = document.querySelector('.input-box')
            if (this.textarea.trim() !== "") {
                this.disabled = true
                document.querySelector('.button').style.cursor = 'not-allowed'
                this.appendMessage()
                this.textarea = ''
                input.style.height = '56px';
            }
        },
        appendMessage() {
            this.records.push(this.textarea)
            this.records.push('')
            const chatBox = document.getElementById("chatBox111");
            const loadElement = document.createElement("div");
            loadElement.classList.add('loading111')
            this.$nextTick(() => {
                const otherMessage = document.querySelectorAll('.other-message111')
                otherMessage[otherMessage.length - 1].appendChild(loadElement)
                document.querySelector('#base').scrollTop = chatBox.clientHeight
                this.sendId = this.paperId
                this.$.ajax({
                url: this.baseUrl + "/grading/comment/extend",
                type: 'post',
                data: JSON.stringify({
                    paperId:this.paperId
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    // console.log(res);
                    this.disabled = false
                    document.querySelector('.button').style.cursor = 'pointer'
                    if (res.status == 200) {
                        if (this.sendId == this.paperId) {
                            this.answer = res.data
                            this.newRecords = JSON.parse(JSON.stringify(this.records))
                            this.newRecords[this.newRecords.length - 1] = res.data
                            this.changeRecords()
                            otherMessage[otherMessage.length-1].removeChild(document.querySelector('.loading111'));
                            var typed = new Typed(otherMessage[otherMessage.length-1], {
                                strings: [this.answer],//输出的文字
                                typeSpeed: 10,//打字的速度
                                cursorChar: '_',
                                onComplete: () => {
                                    this.records[this.records.length - 1] = res.data
                                }
                            });
                        }
                    }
                    else {
                        this.records[this.records.length - 1] = '请求出错'
                        this.disabled = false
                        document.querySelector('.button').style.cursor = 'pointer'
                        this.$message.warning(res.msg)
                    }
                },
                error: () => {
                    this.records[this.records.length - 1] = '请求发送失败，请稍后再试！'
                    this.disabled = false
                    document.querySelector('.button').style.cursor = 'pointer'
                    this.$message.error('请求发送失败，请稍后再试！')
                }
                })
            })
        },
        changeRecords() {
            this.$.ajax({
                url: this.baseUrl + "/grading/comment/change",
                type: 'post',
                data: JSON.stringify({
                    paperId:this.paperId,
                    paperContent:this.newRecords
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    // console.log(res);
                    if (res.status == 200) {
                        console.log();
                    }
                    else {
                        this.$message.warning(res.msg)
                    }
                },
                error: () => {
                    this.$message.error('请求发送失败，请稍后再试！')
                }
            })
        }
        ,
        back() {
            this.flag = true
        },
        // 分页插件事件
        callFather(parm) {
            this.formInline.page = parm.currentPage
            this.formInline.limit = parm.pageSize
            this.getData()
        },
        getData() {
            this.loading = true
            // 模拟数据开始
            // this.loading = false
            // this.listData = [
            //     {
            //         paperId: '12315465',
            //         school: 1,
            //         paperName: 1,
            //         paperSubject: '666',
            //         createTime:'2024-01-30' 
            //     },
            //     {
            //         paperId: '12315465',
            //         school: 1,
            //         paperName: 1,
            //         paperSubject: '999',
            //         createTime:'2024-01-30' 
            //     }
            // ]
            // this.pageparm.currentPage = this.formInline.page
            // this.pageparm.pageSize = this.formInline.limit
            // this.pageparm.total = 2
            // 模拟数据结束
            
            this.$.ajax({
                url: this.baseUrl + "/grading/grade/list",
                type: 'post',
                data: JSON.stringify({
                    schoolName: localStorage.getItem('school'),
                    current: this.formInline.page,
                    size: this.formInline.limit
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    console.log(res);
                    this.loading = false
                    if (res.status == 200) {
                        this.listData = res.data.records
                        this.pageparm.currentPage = this.formInline.page
                        this.pageparm.pageSize = this.formInline.limit
                        this.pageparm.total = res.data.total
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
        getSidebar(subject) {
            this.$.ajax({
                url: this.baseUrl + "/grading/comment/paperList",
                type: 'post',
                data: JSON.stringify({
                    schoolName: localStorage.getItem('school'),
                    paperSubject:subject
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    console.log(res);
                    if (res.status == 200) {
                        const subjectList = res.data.testPaperPage.map((value, index) => {
                            return {
                                paperId: value.paperId,
                                paperName: value.paperName,
                                createTime:value.createTime
                            }
                        })
                        if (subject == '政治') {
                            this.politics = subjectList
                        } else if (subject == '物理') {
                            this.physics = subjectList
                        } else {
                            this.math = subjectList
                        }
                    }
                    else {
                        this.$message.warning(res.msg)
                    }
                },
                error: () => {
                    this.$message.error('请求发送失败，请稍后再试！')
                }
            })
        }, 
        getRecord() {
            this.recordLoading = true
            this.records = []
            this.$.ajax({
                url: this.baseUrl + "/grading/comment/question",
                type: 'post',
                data: JSON.stringify({
                    paperId: this.paperId,
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    console.log(res);
                    this.recordLoading = false
                    if (res.status == 200) {
                        if (res.data) {
                            this.records = res.data
                        } else {
                            this.textarea = '请分析本张试卷的知识点'
                        }
                    }
                    else {
                        this.$message.warning(res.msg)
                    }
                },
                error: () => {
                    this.$message.error('请求发送失败，请稍后再试！')
                }
            })
        },
        knowledgeExtension(row) {
            this.paperId = row.paperId
            this.activeName = row.paperSubject
            this.getRecord()
            this.flag = false
            // setTimeout(() => {
            //     this.appendMessage('我可以为您提供学习上的帮助', 'other-message111');
            //     document.querySelector('.other-message111').removeChild(document.querySelector('.loading111'))
            // }, 700);
            this.$nextTick(() => {
                this.$(`.children[data-id=${this.paperId}]`).addClass('active')
                const input = document.querySelector('.input-box')
                const textarea = document.querySelector('#messageInput')
                const text = document.querySelector('.text')
                //动态改变输入框的高度
                textarea.addEventListener('input', (e) => {
                    input.style.height = '56px';
                    input.style.height = e.target.scrollHeight + 'px';
                    console.log(textarea.value.length)
                    if(textarea.value.length > 110){
                        textarea.style.paddingRight = '55px'
                    }else {
                        textarea.style.paddingRight = '140px'
                    }
                });

                const sendMessageButton = document.querySelector('.sendMessageButton')
                sendMessageButton.addEventListener("click", () => {
                    this.sendMessage();
                });
                text.addEventListener("keyup", function (e) {
                    if (e.shiftKey && e.keyCode === 13) {
                        return;
                    }
                });
            })
        },
        childClick(paperId) {
            this.disabled = false
            document.querySelector('.button').style.cursor = 'pointer'
            this.paperId = paperId
            this.$('.children').removeClass('active')
            this.$(`.children[data-id=${paperId}]`).addClass('active')
            this.getRecord()
            document.querySelector('#base').scrollTop = 0
        }
    }
}
</script>

<style scoped>
#base {
    height: calc(100vh - 64px);
    overflow: auto;
    display: flex;
    flex-direction: column;
    align-content: center;
}
.body {
    width: 90%;
    margin: 30px auto;
    padding: 22px 28px 22px 28px;
    background: #ffffffc2;
    border-radius: 12px;
    border: 1px solid #86868659;
}
.user-search {
    margin-top: 20px;
}
.refresh {
    float: right;
    cursor: pointer;
    color: #409EFF;
}
.refresh:hover {
    color: #66B1FF;
}
.elTable /deep/ .cell{
    overflow: visible;
}
.input-box {
    position:fixed;
    display: flex;
    /*        margin:0 auto;*/
    left: 58.33%;
    bottom:20px;
    width:60%;
    height:56px;
    border-radius: 10px;
    max-height:200px;
    z-index:1;
    transform:translateX(-50%);
}
textarea[class="text"] {
    flex: 1;
    padding: 15px 140px 15px 15px;
    border: 1px solid rgba(100, 100, 111, 0.2);
    border-radius: 10px;
    outline:none;
    resize:none;
    box-shadow: 0 0 10px 0px rgba(100, 100, 111, 0.2);
}
#messageButton {
    position: absolute;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    outline: none !important;
    right: 10px;
    bottom: 13px;
    outline:none!important;
}
#messageButton img {
    width:100%;
    height:100%;
}
.button {
    height:30px;
    width:66px;
    outline:none!important;
    border-radius: 5px;
    border:none;
    padding:0 5px;
    cursor: pointer;
    background-color:#cfcfcf;
}
.button:hover {
    background-color:#00ce95;
    color:#fff;
}
/*    #E1E1E9*/
.empty {
    height:100px;
}

.down {
    height:100px;
}
.row1 {
    width:107%;
    height:62px;
    /*        box-shadow: 0 0 10px 0px rgba(100, 100, 111, 0.2);*/
    border:1px solid #aba5a56b;
    border-radius:10px;
    transform:translateX(-1.5%);
}
#ai {
    margin: 0;
    padding: 20px;
    font-size: 16px;
    text-align:center;
    background-color: #ffffff60;
    border-bottom: 1px solid #ececec;
}
.page-content {
    padding-bottom:1px!important;
}
.custom-radio {
    margin-right: 10px; /* 调整单选按钮之间的距离 */
}
.info-value {
    margin-right: 80px; /* 'John Doe' 和 '性别：' 之间的额外间距，可以根据需要调整 */
}

.typed-cursor {
    display:'none';
}
#messageInput {
    font-size: 15px;
    line-height: 24px;
}
.el-icon-back {
    box-shadow: -1px -1px 3px rgb(235, 248, 255),
    -1px -1px 2px rgba(255, 255, 255, 0.5),
    inset 1px 1px 4px rgba(255, 255, 255, 0.1),
    1px 1px 3px rgba(0, 0, 0, 0.15);
}
.el-icon-back:active {
    box-shadow: inset -1px -1px 3px rgb(235, 248, 255),
    inset -1px -1px 2px rgba(255, 255, 255, 0.5),
    inset 1px 1px 4px rgba(255, 255, 255, 0.1),
    inset 1px 1px 3px rgba(0, 0, 0, 0.15);
}

.left{
    flex: 1;
    position: sticky;
    top: 0;
    height: calc(100vh - 64px);
    background-color: #e5e5e53e;
}
.right{
    flex: 5;
}
.el-collapse{
    margin-top: 60px;
    padding: 0 10px;
}
.el-collapse /deep/ .el-collapse-item__header {
    background-color: transparent;
    font-size: 14px;
}
.el-collapse /deep/ .el-collapse-item__wrap {
    background-color: transparent;
}
.el-collapse /deep/ .el-collapse-item__content {
    padding-bottom: 0px;
}
.children {
    padding: 5px 10px;
    margin-bottom: 2px;
    border-radius: 5px;
    cursor: pointer;
    transition: all linear .2s;
}
.el-collapse .active {
    background-color: #d6d6d684;
}
.children:hover {
    background-color: #d6d6d684;
}
</style>
<style>
/* 加111是为了防止影响其他页面 */
#chatBox111 {
    margin-bottom: 100px;
}
.message111 {
    position:relative;
    white-space: pre-wrap;
    padding: 1.5rem 200px 1.5rem 230px;
    /*        background-color: rgba(136,127,127,0.72);*/
    border: none;
    background-color:#EFFDFA;
    font: 16px/1.5 "Helvetica Neue",Helvetica,Arial,"Microsoft Yahei","Hiragino Sans GB","Heiti SC","WenQuanYi Micro Hei",sans-serif;
}
.message111::before {
    display:block;
    position:absolute;
    content:'A';
    line-height:36px;
    text-align:center;
    top:18px;
    left:175px;
    width:36px;
    height:36px;
    background-color:#d586ae;
}
.message111::after {
    display:block;
    position:absolute;
    content:'';
    top:26px;
    right:175px;
    width:20px;
    height:20px;
    background-image: url(../.././assets/img/人.svg);
    background-size: contain; /* 使用contain值确保图像适应盒子内 */
    background-repeat: no-repeat; /* 防止图像重复显示 */
    background-color:#f7f7ff;
}
.other-message111 {
    position:relative;
    white-space: pre-wrap;
    padding: 1.5rem 200px 1.5rem 230px;
    background-color:#ffffffc2;
    font: 16px/1.5 "Helvetica Neue",Helvetica,Arial,"Microsoft Yahei","Hiragino Sans GB","Heiti SC","WenQuanYi Micro Hei",sans-serif;
}
.other-message111::before {
    display:block;
    position:absolute;
    content:'';
    top:18px;
    left:175px;
    width:36px;
    height:36px;
    background-image: url(../../assets/img/ai.svg);
    background-size: contain; /* 使用contain值确保图像适应盒子内 */
    background-repeat: no-repeat; /* 防止图像重复显示 */
    background-color:#E2EDFF;
}
.other-message111::after {
    display:block;
    position:absolute;
    content:'';
    top:26px;
    right:175px;
    width:20px;
    height:20px;
    background-image: url(../../assets/img/机器人.svg);
    background-size: contain; /* 使用contain值确保图像适应盒子内 */
    background-repeat: no-repeat; /* 防止图像重复显示 */
    /*        background-color:#f7f7ff;*/
}
.loading111 {
    width:27px;
    height:27px;
    padding-top:5px;
}
.loading111::before {
    top:50%;
    content:'';
    display:block;
    width:15px;
    height:15px;
    border-radius:7.5px;
    background-color:black;
    animation:loading 0.8s linear infinite forwards alternate;
}
@keyframes loading{
    0% {
        transform: scale(1);
    }
    100% {
        transform: scale(0.7);
    }
}
.typed-cursor.typed-cursor--blink {
    display: none;
}

</style>