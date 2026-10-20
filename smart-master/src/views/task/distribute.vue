<template>
    <div>
        <div class="base" v-show="flag">
            <div class="body" v-loading="loadingBody">
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
                    <el-table-column sortable prop="paperId" label="任务ID" width="150">
                    </el-table-column>
                    <!-- <el-table-column sortable label="预览" width="200">
                        <template slot-scope="scope">
                            <el-image 
                              style="width: 180px; height: 100px"
                              :src="scope.row.aocFid.url"
                              :preview-src-list="scope.row.aocFid.srcList">
                            </el-image>
                        </template>
                    </el-table-column> -->
                    <el-table-column prop="paperName" label="试卷名" width="150">
                    </el-table-column>
                    <el-table-column prop="paperSubject" label="科目名" width="150">
                    </el-table-column>
                    <!-- <el-table-column label="基本信息" width="150">
                        <template slot-scope="scope">
                            <div>试卷名：{{ scope.row.paperName }}</div>
                            <div>科目名：{{ scope.row.paperSubject }}</div>
                        </template>
                    </el-table-column> -->
                    <el-table-column label="上传状态" width="150">
                        <template slot-scope="scope">
                            <span class="uploadStatus"
                                :style="{color:scope.row.paperStatus&&scope.row.sheetStatus ? '#24d3ff' : '#F56C6C',borderColor: scope.row.paperStatus && scope.row.sheetStatus ? '#24d3ff' : '#F56C6C' }">{{scope.row.paperStatus&&scope.row.sheetStatus
                                ? '上传成功' : '进行中'}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column sortable prop="createTime" label="创建时间" width="150">
                    </el-table-column>
                    <el-table-column align="center" label="操作" min-width=150>
                        <template slot-scope="scope">
                            <el-button size="mini" @click="taskDistribute(scope.row)"
                                style="background-color: #24d3ff;color: #fff">划分题目</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- 分页组件 -->
                <Pagination v-bind:child-msg="pageparm" @callFather="callFather"></Pagination>
            </div>
        </div>
        <div class="base2" v-if="!flag">
            <tui-image-editor :include-ui="useDefaultUI" :options="options" class="imageEditor" ref="tuiImageEditor"
                @objectAdded="add"></tui-image-editor>
            <div class="back" @click="backers"><i class="el-icon-back"></i>返回</div>
        </div>
    </div>
</template>

<script>
import Pagination from '../../components/Pagination'
import 'tui-color-picker/dist/tui-color-picker.css';
import 'tui-image-editor/dist/tui-image-editor.css';
import { ImageEditor } from '@toast-ui/vue-image-editor';
import { Main } from 'element-ui';

export default {
    name: 'listSmart',
    data() {
        return {
            flag: true,//任务分割页面
            loading: true, //是显示加载
            loadingBody:false,
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
                    loadImage: {
                        path: '',
                        name: 'SampleImage.png'
                    }
                }
            },
            selectedId: '',//任务分配的paperId
            id: '',//存有添加的对象(矩形框等)的最大id
            position: [],//所有矩形框的位置信息
            num: 1,//用来添加编号
            canvasWidth: 0,//画布大小
            file: '',//上传的原图
            paperPoint: '',//之前的框位置
        }
    },
    components: {
        'tui-image-editor': ImageEditor,
        Pagination
    },
    created() {
        // this.getUser(this.data)
        this.getData()
        // this.options.includeUI.loadImage.path = 'https://ts1.cn.mm.bing.net/th/id/R-C.9de53f9726576696b318a8d95c0946cb?rik=sWB3V9KSxHbThw&riu=http%3a%2f%2fpic.bizhi360.com%2fbbpic%2f95%2f9995_1.jpg&ehk=GcPUjJED69TBvg9XxQr2klzDzfRsQWhAfLKlIAUWHJQ%3d&risl=&pid=ImgRaw&r=0'
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
            //         paperId: '12315465',
            //         paperStatus: 1,
            //         sheetStatus: 1,
            //         paperSubject: '666',
            //         paperName: '很ok',
            //         createTime:'2024-01-30' 
            //     },
            //     {
            //         paperId: '12315465',
            //         paperStatus: 1,
            //         sheetStatus: 0,
            //         paperSubject: '666',
            //         paperName: '很ok',
            //         createTime:'2024-01-30' 
            //     }
            // ]
            // this.pageparm.currentPage = this.formInline.page
            // this.pageparm.pageSize = this.formInline.limit
            // this.pageparm.total = 1
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
                    this.loading = false
                    // console.log(res);
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
        preAoc(row) {
            this.aocFormVisible = true
            this.aocLoading = true
            this.febId = row.aocId
            this.$.ajax({
                url: this.baseUrl + "/feb/get2",
                type: 'post',
                data: JSON.stringify({ febAoc: row.aocId, userId: localStorage.getItem('userId') }),
                contentType: "application/json",
                success: res => {
                    // console.log(res);
                    this.tableData = res
                    this.aocLoading = false
                },
                error: () => {
                    this.$message.error("请求发送失败，请检查网络连接或服务器状态。")
                }
            })
        },
        preAoc2() {
            this.aocFormVisible = true
            this.aocLoading = true
            this.$.ajax({
                url: this.baseUrl + "/feb/get2",
                type: 'post',
                data: JSON.stringify({ febAoc: this.febId, userId: localStorage.getItem('userId') }),
                contentType: "application/json",
                success: res => {
                    console.log(res);
                    this.tableData = res
                    this.aocLoading = false
                },
                error: () => {
                    this.$message.error("请求发送失败，请检查网络连接或服务器状态。")
                }
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
        add(props) {
            this.id = props.id
            const e = this.$refs.tuiImageEditor.invoke('getObjectProperties', this.id, ['left', 'top', 'width', 'height'])
            this.$refs.tuiImageEditor.invoke('addText', 'no.' + this.num, {
                styles: {
                    fill: '#000',
                    fontSize: this.canvasWidth / 47,
                    fontWeight: 'bold'
                },
                position: {
                    x: e.left - e.width / 2 + e.width / 8,
                    y: e.top - e.height / 2 + e.height / 70
                }
            })
            this.num++
        },
        backers() {
            this.flag = true
        },
        dataURLtoFile(dataurl, filename) {
            var arr = dataurl.split(','),
                mime = arr[0].match(/:(.*?);/)[1],
                bstr = atob(arr[1]),
                n = bstr.length,
                u8arr = new Uint8Array(n);
            while (n--) {
                u8arr[n] = bstr.charCodeAt(n);
            }
            return new File([u8arr], filename, { type: mime });
        },

        // await this.$refs.tuiImageEditor.invoke('loadImageFromURL', 'http://picture.gptkong.com/20240519/1632a366adbd874554b316f8311eb869bb.png', '答题卡')

        async btnInit() {
            this.file = await this.$refs.tuiImageEditor.invoke('toDataURL')
            this.canvasWidth = await this.$refs.tuiImageEditor.invoke("getCanvasSize").width
        },
        async init() {
            this.file = await this.$refs.tuiImageEditor.invoke('toDataURL')
            this.canvasWidth = await this.$refs.tuiImageEditor.invoke("getCanvasSize").width
            console.log(this.canvasWidth, this.file);
            console.log(this.paperPoint);
            if (this.paperPoint) {
                const length = this.paperPoint.length
                for (let i = 0; i <= length - 1; i++) {
                    const e = this.paperPoint[i]
                    await this.$refs.tuiImageEditor.invoke('addShape', 'rect', {
                        fill: 'transparent',
                        stroke: 'red',
                        strokeWidth: 3,
                        width: e.width,
                        height: e.height,
                        left: e.left + e.width / 2,
                        top: e.top + e.height / 2,
                        isRegular: true
                    }).then(object => {
                        this.id = object.id
                    })
                    // const e = await this.$refs.tuiImageEditor.invoke('getObjectProperties', this.id, ['left', 'top', 'width', 'height'])
                    await this.$refs.tuiImageEditor.invoke('addText', 'no.' + this.num, {
                        styles: {
                            fill: '#000',
                            fontSize: this.canvasWidth / 47,
                            fontWeight: 'bold'
                        },
                        position: {
                            x: e.left + e.width / 8,
                            y: e.top + e.height / 70
                        }
                    })
                    this.num++
                }
            }
            console.log(this.num);
        },
        import() {
            this.$('.tui-image-editor-load-btn').change(() => {
                setTimeout(() => {
                    this.btnInit()
                }, 100)
            })
            setTimeout(() => {
                this.loadingBody = false
                this.init()
            },500)
            //自定义download
            const download = document.createElement('button')
            download.innerHTML = '发送'
            this.$(download).addClass('imageEditorDownload')
            document.querySelector('.tui-image-editor-header-buttons').appendChild(download)
            //修改按钮的文字为上传
            var textNodes = this.$(".tui-image-editor-header-buttons div").contents().filter(function () {
                return this.nodeType === 3;
            });
            textNodes[0].nodeValue = "上传"
            this.$('.tie-shape-button .rect').click(() => {
                this.$('.tui-image-editor-main').removeClass('tui-image-editor-menu-shape')
                this.$('.tie-btn-shape').removeClass('active')
                this.$('.tie-shape-button').removeClass('rect')
            })
            this.$('.imageEditorDownload').click(() => {
                this.position = []
                for (let i = 0; i <= this.id; i++) {
                    const e = this.$refs.tuiImageEditor.invoke('getObjectProperties', i, ['left', 'top', 'width', 'height', 'type'])
                    if (e) {
                        // console.log(e);
                        if (e.type == 'rect') {
                            this.position.push({
                                left: e.left - e.width / 2,
                                top: e.top - e.height / 2,
                                width: e.width,
                                height: e.height
                            })
                        }
                    }
                }
                console.log(this.position);
                localStorage.setItem('position', JSON.stringify(this.position))
                const imgMessage = this.$message({
                    type: 'success',
                    duration: 0,
                    dangerouslyUseHTMLString: true,
                    iconClass: 'el-icon-loading',
                    customClass: 'messageLoading',
                    message: '&nbsp;&nbsp;正在发送'
                })
                const formData = new FormData();
                const data = {
                    paperId: this.selectedId,
                    pointList: this.position
                }
                const file = this.dataURLtoFile(this.file, this.$refs.tuiImageEditor.invoke('getImageName'));
                formData.append('entity', new Blob([JSON.stringify(data)], { type: 'application/json' }));
                formData.append('template', file)
                this.$.ajax({
                    url: this.baseUrl + "/grading/region/add",
                    type: 'post',
                    data: formData,
                    contentType: false,
                    processData: false,
                    headers: {
                        'ngrok-skip-browser-warning': 'true',
                        'token': localStorage.getItem('token')
                    },
                    success: res => {
                        console.log(res);
                        if (res.status == 200) {
                            this.$message.success('发送成功')
                            imgMessage.close()
                        } else if (res.status == 500) {
                            imgMessage.close()
                            this.$message.error(res.msg)
                        } else {
                            imgMessage.close()
                            this.$message.error('发送失败')
                        }
                    },
                    error: () => {
                        imgMessage.close()
                        this.$message.error('发送失败，请检查网络')
                    }
                })
            })
        },
        taskDistribute(row) {
        // console.log(row);
        this.selectedId = row.paperId
        // this.flag = false
            this.num = 1
        this.loadingBody = true
        this.$.ajax({
            url: this.baseUrl + "/grading/region/point/get",
            type: 'post',
            data: JSON.stringify({ paperId: this.selectedId }),
            contentType: "application/json",
            headers: {
                'ngrok-skip-browser-warning': 'true',
                'token': localStorage.getItem('token')
            },
            success: res => {
                console.log(res);
                this.loadingBody = false
                if (res.status == 200) {
                    this.$message.success('导入划分结果成功')
                    this.paperPoint = JSON.parse(res.data.paperPoint)
                    this.options.includeUI.loadImage.path = res.data.tmpImage
                } else if (res.status == 500) {
                    this.$message.success(res.msg)
                    this.paperPoint = '',
                    this.options.includeUI.loadImage.path = ''
                } else {
                    this.$message.error('获取失败')
                    this.paperPoint = '',
                    this.options.includeUI.loadImage.path = ''
                }
                // console.log(res);
                this.flag = false
                this.$nextTick(() => {
                    this.import()
                })
            },
            error: () => {
                this.loadingBody = false
                this.$message.error('获取失败，请检查网络')
                this.paperPoint = '',
                this.options.includeUI.loadImage.path = ''
            }
        })
        },
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
.messageLoading{
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
.back {
    display: inline-block;
    position: absolute;
    width: 120px;
    height: 40px;
    right: 300px;
    top: 9px;
    padding: 0;
    line-height: 40px;
    outline: none;
    border-radius: 20px;
    border: 1px solid #ddd;
    font-family: 'Noto Sans', sans-serif;
    font-size: 12px;
    font-weight: bold;
    cursor: pointer;
    vertical-align: middle;
    letter-spacing: .3px;
    text-align: center;
    background-color: #cccccc;
    border: 1px solid #cccccc;
    color: #fff;
    font-family: 'Noto Sans', sans-serif;
    font-size: 12px;
}
.el-form--label-top .el-form-item__label{
    padding: 0;
}
.el-form-item {
    margin-bottom: 38px;
}
.left .el-form-item:last-child {
    margin-bottom: 0;
}


.uploadStatus{
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
.base2 {
    position: relative;
    width: 90vw;
    height: 80vh;
    margin: 70px 0;
    padding: 0 40px;
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
<style>
.tui-image-editor-container .tui-image-editor-controls {
    background: #ffffffc2;
}
.tui-image-editor-container .tui-image-editor-main-container {
    background: #ffffffc2!important;
}
.tui-image-editor-wrap::-webkit-scrollbar {
    display: none; 
}
.tui-image-editor-download-btn {
    display: none!important;
}
.tui-image-editor-container .tui-image-editor-header-logo {
    width: 23%;
}
.tui-image-editor-container .tui-image-editor-help-menu.top {
    left: 43%;
}
.imageEditorDownload {
    display: inline-block;
    position: relative;
    width: 120px;
    height: 40px;
    padding: 0;
    line-height: 40px;
    outline: none;
    border-radius: 20px;
    border: 1px solid #ddd;
    font-family: 'Noto Sans', sans-serif;
    font-size: 12px;
    font-weight: bold;
    cursor: pointer;
    vertical-align: middle;
    letter-spacing: .3px;
    text-align: center;
    background-color: #fdba3b;
    border: 1px solid #fdba3b;
    color: #fff;
    font-family: 'Noto Sans', sans-serif;
    font-size: 12px;
}
</style>