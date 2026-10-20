<template>
    <div class="base">
        <div class="aside" style="position: relative;display: flex;flex-direction: column;align-items: center">
            <div class="taskHeader" style="background-color: #FAFAFA;">
                <span style="flex: 3;margin-right: 4px;text-align: center;">题组</span>
                <span style="flex: 2;margin-right: 4px;text-align: center;">科目</span>
                <span style="flex: 2;text-align: center;">进度</span>
            </div>
            <div class="list" style="width: 100%;" @click="click($event)">
                <div class="taskList" v-for="elem in listData" :key="elem.paperId" @click="getRight(elem)">
                    <span style="flex: 3;margin-right: 4px;text-align: center;">{{ elem.paperName }}</span>
                    <span style="flex: 2;margin-right: 4px;text-align: center;">{{ elem.paperSubject }}</span>
                    <el-progress :text-inside="true" color="#a6dbff" :stroke-width="20"
                        :percentage="elem.completedNum&&elem.sheetNumelem?completedNum/elem.sheetNum:0" style="flex: 2;"></el-progress>
                </div>
            </div>
            <div style="position: absolute; bottom: 5px;">
                <el-pagination small @current-change="handleCurrentChange" layout="prev, pager, next" :total="listTotal"
                    background style="margin: 0 auto;">
                </el-pagination>
            </div>
        </div>
        <div class="main">
            <div class="header">
                <div class="subject">评卷任务</div>
            </div>
            <div class="child1">
                <div style="float: left;">
                    <span class="title">题组任务量：</span>
                    <span class="content">{{ groupTotal }}</span>
                    <span class="title">评卷进度：</span>
                    <span class="content">{{ groupComplete }}</span>
                    <span class="title">未评：</span>
                    <span class="content">{{ groupTotal?groupTotal - groupComplete:'' }}</span>
                </div>
                <div style="float: right;">
                    <span>教师任务量规则：</span>
                    <div class="rule">自定义分配&nbsp;<i class="el-icon-s-tools"></i></div>
                </div>
            </div>
            <div class="child2">
                <!-- 搜索筛选 -->
                <el-form :inline="true" :model="formInline" class="user-search">
                    <el-form-item label="搜索：">
                        <el-input size="small" v-model="formInline.paperId" placeholder="输入教师账号或姓名"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" icon="el-icon-search">搜索</el-button>
                        <!-- <el-button size="small" type="primary" icon="el-icon-plus" @click="handleEdit()">添加</el-button> -->
                    </el-form-item>
                    <div class="refresh"><i class="el-icon-refresh"></i>刷新</div>
                </el-form>
                <!--列表-->
                <el-table size="small" :data="taskList" highlight-current-row v-loading="loading" border
                    element-loading-text="拼命加载中" style="width: 100%;" class="elTable" height="calc(100vh - 425px)">
                    <template slot="empty">
                        <el-empty description="暂无数据"></el-empty>
                    </template>
                    <el-table-column align="center" type="selection" width="60">
                    </el-table-column>
                    <el-table-column prop="taskId" label="任务ID" width="95">
                    </el-table-column>
                    <el-table-column prop="taskName" label="任务名" width="100">
                    </el-table-column>
                    <el-table-column sortable prop="regionNumber" label="区域号" width="90">
                    </el-table-column>
                    <el-table-column prop="userName" label="姓名" width="100">
                    </el-table-column>
                    <el-table-column prop="userPhone" label="手机号" width="100">
                    </el-table-column>
                    <el-table-column sortable prop="completedNum" label="已评" width="80">
                    </el-table-column>
                    <el-table-column sortable :formatter="formatter" label="剩余" width="80">
                    </el-table-column>
                    <el-table-column align="center" label="操作" min-width=150>
                        <template slot-scope="scope">
                            <el-button size="mini" @click="handleEdit(scope.row, scope.$index)">编辑</el-button>
                            <el-button size="mini" type="danger"
                                @click="deleteUser(scope.row, scope.$index)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- 分页组件 -->
                <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
            </div>
        </div>
    </div>
</template>

<script>
import Pagination from '../../components/Pagination'
export default {
    name: 'DetailsTask',
    data() {
        return {
            dialogFormVisible: false,//控制分发页面
            loading: false, //是显示加载
            formInline: {
                page: 1,
                limit: 10,
            },
            userparm: [], //搜索权限
            listData: [], //列表数据
            taskList: [],//右边数据
            // 分页参数
            pageparm: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            //编辑
            form: {
                name: 'a',
                text: 'b'
            },
            listTotal: '',//左边列表的数目
            school: localStorage.getItem('school'),
            groupTotal: '',//右边上面的评卷任务量
            groupComplete:''//右边上面的评卷进度
        }
    },
    components: {
        Pagination
    },
    created() {
        this.getData(1)
    },
    methods: {
        getData(e) {
            this.$.ajax({
                url: this.baseUrl + "/grading/grade/list",
                type: 'post',
                data: JSON.stringify({
                    schoolName: localStorage.getItem('school'),
                    current: e,
                    size:10
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    this.loading = false
                    console.log(res);
                    if (res.status == 200) {
                        this.listData = res.data.records
                        this.listTotal = res.data.total
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
        getRight(e) {
            this.taskList = []
            this.groupTotal = e.sheetNum
            this.groupComplete = e.completedNum
            this.loading = true
            this.$.ajax({
                url: this.baseUrl + "/grading/task/taskList",
                type: 'post',
                data: JSON.stringify({
                    paperId: e.paperId,
                    current: this.formInline.page,
                    size: this.formInline.limit
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    this.loading = false
                    console.log(res);
                    if (res.status == 200) {
                        this.taskList = res.data.records
                    }
                    else {
                        this.$message.warning(res.msg)
                    }
                    this.pageparm.currentPage = this.formInline.page
                    this.pageparm.pageSize = this.formInline.limit
                    this.pageparm.total = res.total
                },
                error: () => {
                    this.loading = false
                    this.$message.error('请求发送失败，请稍后再试！')
                }
            })
        },
        // 分页插件事件
        callFather(parm) {
            this.formInline.page = parm.currentPage
            this.formInline.limit = parm.pageSize
            this.getRight()
        },
        handleCurrentChange(e) {
            this.getData(e)

        },
        click(e) {
            console.log(e.target.tagName);
            if (e.target.tagName == 'SPAN') {
                this.$(e.target).parent().addClass('active').siblings().removeClass('active')
            } else if (e.target.tagName == 'DIV') {
                this.$(e.target).addClass('active').siblings().removeClass('active')
            }
        },
        //计算右边表格的剩余列的值
        formatter(row) {
            return row.taskNum-row.completedNum
        }
    }
}
</script>


<style scoped>
.el-table--border{
    max-height: 370px;
}
.base {
    display: flex;
    position: relative;
    width: 90vw;
    height: 80vh;
    margin: 70px 0;
    padding: 0 40px;
}
.aside {
    overflow: hidden;
    width: 25%;
    border-radius: 12px;
    margin-right: 20px;
    background-color: #ffffffc2;
    border: 1px solid #86868659;
}
.aside .active {
    background: #d6f3ff;
}
.main {
    overflow: hidden;
    width: 74%;
    border-radius: 12px;
    background-color: #ffffffc2;
    border: 1px solid #86868659;
}
.taskHeader {
    display: flex;
    width: 100%;
    padding: 16px 10px;
    color: #bfbfbf;
    background-color: #FAFAFA;
    background-blend-mode: normal;
}
.taskList {
    display: flex;
    width: 99%;
    padding: 16px 10px;
    color: #bfbfbf;
    margin: 3px 2px;
    border-radius: 8px;
    background-blend-mode: normal;
    cursor: pointer;
}
.taskList:hover {
    background-color: #d6f3ff;
}

.header {
    padding: 0 20px;
    height: 40px;
    background-color: #FAFAFA;
}
.subject { 
    color: #5096ff;
    height: 100%;
    line-height: 40px;
    width: 64px;
    border-bottom: 2px solid #5096ff;
}
.child1 {
    height: 80px;
    padding-left: 20px;
    padding-right: 20px;
    border-bottom: 1px solid #f0f0f0;
}
.title {
    display: inline-block;
    height: 100%;
    line-height: 80px;
    margin-right: 10px;
}
.content {
    display: inline-block;
    min-width: 60px;
    height: 100%;
    line-height: 80px;
}
.rule {
    height: 100%;
    line-height: 80px;
    display: inline-block;
    color: #5096ff;
}
.child2 {
    height: 500px;
    padding-left: 20px;
    padding-right: 20px;
}


.elTable /deep/ .cell{
    overflow: visible;
}
.uploadStatus{
    padding: 8px 10px;
    border: 2px solid #24d3ff;
    color: #24d3ff;
    border-radius: 5px;
}
.refresh {
    float: right;
    margin-top: 5px;
    cursor: pointer;
    color: #409EFF;
}
.refresh:hover {
    color: #66B1FF;
}
.user-search {
    margin-top: 20px;
}

.userRole {
    width: 100%;
}

.box {
    width: 56px;
    padding: 2px 10px;
    /* margin: 0 auto; */
    border-radius: 5px;
    background-color: aqua;
}
</style>