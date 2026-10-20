<template>
  <div id="base">
    <div class="body">
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
                    <el-button size="mini" @click="examInfo(scope.row)" v-loading.fullscreen.lock="fullscreenLoading"
                    style="background-color: #24d3ff;color: #fff">试卷查看</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
    </div>
  </div>
</template>

<script>
import Pagination from '../../components/Pagination'
export default {
    name: 'tableSmart',
    data() {
        return {
            fullscreenLoading:false,
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
            imgList:[]
        }
    },
    components: {
        Pagination
    },
    created() {
        this.getData()
    },
    methods: {
        // 分页插件事件
        callFather(parm) {
            this.formInline.page = parm.currentPage
            this.formInline.limit = parm.pageSize
            this.getData()
        },
        getData() {
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
            this.loading=true
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
        examInfo(row) {
            this.fullscreenLoading = true
            this.$.ajax({
                url: this.baseUrl + "/grading/region/regionList",
                type: 'post',
                data: JSON.stringify({
                    paperId:row.paperId
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    console.log(res);
                    if (res.status == 200) {
                        this.imgList = res.data
                        this.$router.push({ name: '试卷查看', params: this.imgList })
                        this.fullscreenLoading = false
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
        }
    }
}
</script>

<style scoped>
#base {
  position: relative;
  width: calc(100vw - 150px);
  margin: 70px 0;
}
.body {
    width: 94%;
    margin: 0 auto;
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
</style>