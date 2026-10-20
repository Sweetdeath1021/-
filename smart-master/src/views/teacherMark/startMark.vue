<template>
    <div>
        <div class="base" v-show="flag">
            <div class="body">
                <!-- 搜索筛选 -->
                <el-form :inline="true" :model="formInline" class="user-search">
                    <el-form-item label="搜索：">
                        <el-input size="small" v-model="formInline.paperId" placeholder="输入试卷编号"></el-input>
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
                    element-loading-text="拼命加载中" style="width: 100%;" class="elTable" height="calc(100vh - 350px)">
                    <template slot="empty">
                        <el-empty description="暂无数据"></el-empty>
                    </template>
                    <el-table-column align="center" type="selection" width="60">
                    </el-table-column>
                    <el-table-column sortable prop="taskId" label="任务ID" width="150">
                    </el-table-column>
                    <el-table-column prop="taskName" label="任务名称" width="150">
                    </el-table-column>
                    <el-table-column sortable prop="regionNumber" label="阅卷区域号" width="110">
                    </el-table-column>
                    <el-table-column sortable :formatter="formatter" label="待阅数量" width="130">
                    </el-table-column>
                    <el-table-column sortable prop="completedNum" label="已阅数量" width="130">
                    </el-table-column>
                    <el-table-column sortable prop="endTime" label="截止时间" width="150">
                    </el-table-column>
                    <el-table-column align="center" label="操作" min-width=150>
                        <template slot-scope="scope">
                            <el-button size="mini" @click="taskDistribute(scope.row)"
                                style="background-color: #24d3ff;color: #fff">开始阅卷</el-button>
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
    name: 'listSmart',
    data() {
        return {
            flag: true,//任务分发页面
            loading: true, //是显示加载
            formInline: {
                paperId: '',
                paperName: '',
                page: 1,
                limit: 10,
                varLable: '',
                varName: '',
                token: localStorage.getItem('logintoken')
            },
            userparm: [], //搜索权限
            listData: [], //用户数据
            // 分页参数
            pageparm: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            useDefaultUI: true,
            options: {
                // for tui-image-editor component's "options" prop
                cssMaxWidth: 1200,
                cssMaxHeight: 500,
                includeUI: {
                    initMenu: 'shape', // 默认打开的菜单项
                }
            },
            selectedId: '',//任务分配的paperId
            id: '',//存有添加的对象(矩形框等)的最大id
            position: [],//所有矩形框的位置信息
            num: 1,//用来添加编号
        }
    },
    components: {
        Pagination
    },
    created() {
        // this.getUser(this.data)
        this.getData()
    },
    /**
     * 里面的方法只有被调用才会执行
     */
    methods: {
        // 获取公司列表
        getData() {
            this.loading = true
            // 模拟数据开始
            // this.loading = false
            // this.listData = [
            //     {
            //         taskId: '12315465',
            //         taskName: '第一次联考',
            //         regionNumber: 1,
            //         taskNum: 66,
            //         completedNum: 0,
            //         endTime: '2024-01-30'
            //     },
            //     {
            //         taskId: '12315465',
            //         taskName: '第一次联考',
            //         regionNumber: 1,
            //         taskNum: 99,
            //         completedNum: 0,
            //         endTime: '2024-01-30'
            //     }
            // ]
            // this.pageparm.currentPage = this.formInline.page
            // this.pageparm.pageSize = this.formInline.limit
            // this.pageparm.total = 1
            // 模拟数据结束

            this.$.ajax({
                url: this.baseUrl + "/grading/task/taskPage",
                type: 'post',
                data: JSON.stringify({
                    userId: localStorage.getItem('userId'),
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
        // 分页插件事件
        callFather(parm) {
            this.formInline.page = parm.currentPage
            this.formInline.limit = parm.pageSize
            this.getData()
        },
        // 搜索事件
        search() {
            this.getData()
        },
        handleEdit: function (index, row) {
            this.form.name = row.taskName
        },

        // 删除
        deleteUser(row) {
            this.$confirm('确定要删除吗?', '信息', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.loading = false
                console.log(row.aocId);
                this.$.ajax({
                    url: this.baseUrl + "/aoc/delete",
                    type: 'post',
                    data: JSON.stringify({ aocId: row.aocId }),
                    contentType: "application/json",
                    success: res => {
                        this.$message.success('删除成功')
                        this.getData()
                        console.log(res);
                    },
                    error: () => {
                        this.$message.error("请求发送失败，请检查网络连接或服务器状态。")
                    }
                })
            })
        },
        delTable(row) {
            this.$confirm('确定要删除吗?', '信息', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$.ajax({
                    url: this.baseUrl + "/feb/delete",
                    type: 'post',
                    data: JSON.stringify({ febId: row.febId }),
                    contentType: "application/json",
                    success: res => {
                        console.log(res);
                        this.$message.success("删除成功！")
                        this.preAoc2(row)
                    },
                    error: () => {
                        this.$message.error("请求发送失败，请检查网络连接或服务器状态。")
                    }
                })
            })
        },
        taskDistribute(row) {
            const a = {
                userId: row.userId,
                paperId: row.paperId,
                regionNumber: row.regionNumber,
                taskNum: row.taskNum,
                completedNum: row.completedNum
            }
            this.$router.push({ name: '阅卷页面', params:a})
        },
        formatter(row) {
            return row.taskNum - row.completedNum
        }
    },
    mounted() {

    },
    computed: {
    },
    watch: {
    }
}
</script>

<style>
.messageLoading {
    background-color: #f0f9eb;
    border-color: #e1f3d8;
    color: #67C23A;
    min-width: 140px;
}
</style>
<style scoped>
.imageEditor {
    border-radius: 12px;
    background: transparent;
}

.el-form--label-top .el-form-item__label {
    padding: 0;
}

.el-form-item {
    margin-bottom: 38px;
}

.left .el-form-item:last-child {
    margin-bottom: 0;
}


.uploadStatus {
    display: inline-block;
    width: 71px;
    text-align: center;
    padding: 8px 0;
    border: 2px solid #24d3ff;
    color: #24d3ff;
    border-radius: 5px;
}

.body {
    width: 94%;
    margin: 0 auto;
    padding: 22px 28px 22px 28px;
    background: #ffffffc2;
    border-radius: 12px;
    border: 1px solid #86868659;
}

.refresh {
    float: right;
    cursor: pointer;
    color: #409EFF;
}

.refresh:hover {
    color: #66B1FF;
}

.base {
    position: relative;
    width: 90vw;
    margin: 70px 0;
}


.user-search {
    margin-top: 20px;
}
</style>