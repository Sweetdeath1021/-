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
                    <el-button size="mini" @click="synthesize(scope.row)" v-loading.fullscreen.lock="fullscreenLoading"
                    style="background-color: #24d3ff;color: #fff">综合分析</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
    </div>
    <div class="body" v-if="!flag">
        <div class="mainContent"
        v-loading="loading2"
        element-loading-text="正在加载"
        element-loading-background="rgba(255, 255, 255, 1)">
            <i class="el-icon-back" style="position: absolute;top: 20px;right: 57px;padding: 3px;
            font-size: 20px;background-color: #ffffffa3;border-radius: 5px;z-index: 10;"
            @click="back"></i>
            <h1><span>综合分析</span></h1>
            <div
                style="margin: 0 20px;background-color: #dcfff5;line-height: 25px;padding: 20px 30px;font-size: 14px;color: #00ce95;">
                当前页面展示的是{{ theLabel }}及本学期该科目的其他考试成绩，可比较直观地看出本学科的成绩进退，由于题目难度可能不同，请酌情参考。
            </div>
            <div style="padding: 0 20px;margin-top: 20px;">
                <el-table :data="testScore" border style="width: 100%">
                    <el-table-column label='试卷' :prop="key" align="center" :formatter="title1">
                    </el-table-column>
                    <el-table-column v-for="(value, key, index) in testScore[0]" :key="index"
                        :label="'第'+(index+1)+'次统考'" :prop="key" align="center">
                    </el-table-column>
                    <el-table-column :label='theLabel' :formatter="title2"  align="center">
                    </el-table-column>
                </el-table>
            </div>
            <div id="testScoreEchart" style="width: 100%;height: 400px;margin-top: 20px;"></div>
        </div>
    </div>
  </div>
</template>

<script>
import Pagination from '../../components/Pagination'
export default {
    name: 'synthesizeSmart',
    data() {
        return {
            flag:true,
            fullscreenLoading:false,
            listData: [], 
            formInline: {
                page: 1,
                limit: 10,
                paperId: '',
                paperName:''
            },
            loading: false,
            loading2:false,
            // 分页参数
            pageparm: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            imgList: [],
            testScore: [{
                a: 78,
                b: 83,
                c: 81,
                d: 85,
                e: 79
            }],
            theLabel: '',
            paperSubject: '',
            data:[]
        }
    },
    components: {
        Pagination
    },
    created() {
        this.getData()
    },
    methods: {
        title1() {
            return '分数'
        },
        title2() {
            if (this.paperSubject == '政治') {
                return 88
            }
            else if (this.paperSubject == '数学') {
                return 125
            }
            else {
                return 86
            }
        },
        // 分页插件事件
        callFather(parm) {
            this.formInline.page = parm.currentPage
            this.formInline.limit = parm.pageSize
            this.getData()
        },
        back() {
            this.flag = true
        },
        getData() {
            // 模拟数据开始
            // this.loading = false
            // this.listData = [
            //     {
            //         paperId: '12315465',
            //         school: 1,
            //         paperName: 1,
            //         paperSubject: '数学',
            //         createTime:'2024-01-30' 
            //     },
            //     {
            //         paperId: '12315465',
            //         school: 1,
            //         paperName: 1,
            //         paperSubject: '政治',
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
        synthesize(row) {
            // this.fullscreenLoading = true
            this.theLabel = row.paperName
            this.paperSubject = row.paperSubject
            if (this.paperSubject == '政治') {
                this.testScore = [{
                a: 78,
                b: 83,
                c: 81,
                d: 85,
                e: 79
                }]
            this.data = [78,83,81,85,79,88]
            }
            else if (this.paperSubject == '数学') {
                this.testScore = [{
                a: 120,
                b: 125,
                c: 119,
                d: 115,
                e: 121
                }]
            this.data = [120,125,119,115,121,125]
            }
            else {
                this.testScore = [{
                a: 85,
                b: 87,
                c: 81,
                d: 79,
                e: 83
                }]
            this.data = [85,87,81,79,83,86]
            }
            // this.theLabel = '政治期末考'
            this.flag = false
            this.$nextTick(() => {
                this.echarts4()
            })
        },
        echarts4() {
            var testScoreEchart = this.$echarts.init(document.getElementById('testScoreEchart'));
            testScoreEchart.setOption({
                xAxis: {
                    type: 'category',
                    data: ['第1次统考','第2次统考','第3次统考','第4次统考','第5次统考',this.theLabel],
                    axisLabel: {
                        fontWeight: 700,
                        margin: 20,
                        rotate: 17,
                        align: 'center'
                    }
                },
                yAxis: {
                    type: 'value'
                },
                grid: {
                    top:'10%',
                    bottom: '20%',
                    left: '5%',
                    right:'5%'
                },
                series: [
                    {
                        name: '',
                        data: this.data,
                        type: 'line',
                        label: {
                            show: true,
                            position: "top"
                        },
                        emphasis: {
                            focus: 'series'
                        },
                    },
                ],
                legend: {
                    show: true,
                    left: 'center',
                    top:'top'
                },
                tooltip: {
                    show: true,
                    trigger:'axis'
                },
                dataZoom: [{
                    type: 'slider',
                }]
            })
        },
    }
}
</script>

<style scoped>
#base {
  position: relative;
  width: calc(100vw - 150px);
  margin: 70px 0;
  height: calc(100vh - 140px);
  overflow: auto;
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
.mainContent h1 {
    height: 30px;
    width: 100%;
    color: #333;
    font-weight: 700;
    font-size: 18px;
    padding: 0 0 15px;
    margin: 0;
    line-height: 20px;
}

.mainContent h1::before {
    display: inline-block;
    content: '';
    height: 20px;
    width: 4px;
    margin: 5px 16px 0 0;
    background-color: #00ce95;
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
</style>