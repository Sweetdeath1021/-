<template>
    <div class="base">
        <div class="body">
            <!-- 搜索筛选 -->
            <div v-show="flag == 1">
                <el-form :inline="true" :model="formInline" class="user-search">
                    <el-form-item label="搜索：">
                        <el-input size="small" v-model="formInline.paperId" placeholder="输入试卷编号"></el-input>
                    </el-form-item>
                    <el-form-item label="">
                        <el-input size="small" v-model="formInline.paperName" placeholder="输入试卷名称"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" icon="el-icon-search" @click="search">搜索</el-button>
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
                                :style="{ color: scope.row.paperStatus && scope.row.sheetStatus ? '#24d3ff' : '#F56C6C', borderColor: scope.row.paperStatus && scope.row.sheetStatus ? '#24d3ff' : '#F56C6C' }">{{
                                scope.row.paperStatus && scope.row.sheetStatus
                                ? '上传成功' : '进行中' }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column sortable prop="createTime" label="创建时间" width="150">
                    </el-table-column>
                    <el-table-column align="center" label="操作" min-width=150>
                        <template slot-scope="scope">
                            <el-button size="mini" @click="handleEdit(scope.row, scope.$index)">编辑</el-button>
                            <el-button size="mini" type="danger"
                                @click="deleteUser(scope.row, scope.$index)">删除</el-button>
                            <el-button size="mini" @click="taskDistribute(scope.row)"
                                style="background-color: #24d3ff;color: #fff">任务分配</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- 分页组件 -->
                <Pagination v-bind:child-msg="pageparm" @callFather="callFather" v-show="flag == 1"></Pagination>
            </div>
            <div v-show="flag == 2" class="flag2">
                <el-button size="mini" @click="backer2"
                    style="height: 40px;position: absolute;left: 0;border-radius: 5px;"><i
                        class="el-icon-back"></i>返回</el-button>
                <div style="height: 570px;"  v-if="!areaFlag"></div>
                <div style="position: relative;" v-if="areaFlag">
                    <img :src="downloadImg" alt="" usemap="#plantmap" class="answerImg" />
                    <div v-for="(item,index) in areaList" :key="index"
                        :style="{left:item.left/scale+'px',top:item.top/scale+'px',width:item.width/scale+'px',height:item.height/scale+'px'}"
                        class="answer" @click="imgClick(index+1)">
                        <el-tooltip class="item" effect="dark" :content="'这是第'+(index+1)+'个区域'" placement="top">
                            <span> </span>
                        </el-tooltip>
                    </div>
                </div>
                <!-- area 定义图像映射中的区域 -->
                <!-- <map name="plantmap">
                    <area shape="rect" v-for="(item,index) in areaList" :key="index" :coords="[item.left/scale, item.top/scale, (item.left + item.width)/scale, (item.top+item.height)/scale]" :alt="'第'+index+1+'个'" @click="imgClick(index+1)" class="area"/>
                </map> -->
            </div>
            <div v-show="flag == 3">
                <!-- 任务分配 -->
                <div style="height: 40px;">试卷名称：学科</div>
                <div style="height: 40px;" class="backer">
                    <el-button size="mini" @click="backer"><i class="el-icon-back"></i>返回</el-button>
                </div>
                <div style="display: flex;">
                    <div class="left">
                        <el-form :model="form" ref="aocForm" label-position="top" style="width: 100%;">
                            <el-form-item prop="cities" autocomplete="off">
                                <div style="height: 50px;padding: 0 0 10px;line-height: 40px;">
                                    <span>任务接收人</span>
                                    <el-input size="small" v-model="teacherName" placeholder="输入教师姓名"
                                        style="display: inline-block;width: 35%;margin: 0 10px;"
                                        @input="searchName"></el-input>
                                    <!-- <el-button size="small" type="primary" icon="el-icon-search" @click="searchName">搜索</el-button> -->
                                    <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll"
                                        @change="handleCheckAllChange" style="float: right;"
                                        class="checkAll">全选</el-checkbox>
                                </div>
                                <div class="worker">
                                    <el-checkbox-group v-model="checkedBox">
                                        <el-checkbox v-for="(elem, index) in cities" :label="elem.userName" :key="index"
                                            class="checkSon" border @change="changeBox">
                                            <img :src=elem.userImage alt="" class="img">
                                            <div class="name">{{ elem.userName }}</div>
                                        </el-checkbox>
                                    </el-checkbox-group>
                                </div>
                            </el-form-item>
                        </el-form>
                    </div>
                    <div class="middle">
                        <div style="height: 50px;padding: 0 0 10px;line-height: 40px;position: relative;">
                            <span>已选{{ checkedBox.length }}人</span>
                            <span style="position: absolute;left: 110px;top: -25px;">待分配任务总数：{{ sheetNum }}</span>
                            <span style="position: absolute;left: 110px;top: 0px;">剩余任务总数：{{ surplusNum }}</span>
                        </div>
                        <div class="people">
                            <div v-for="(elem, index) in checkedList" :key="index" class="checkedPeople">
                                <span style="margin-right: 15%;line-height: 28px;">{{ elem.userName }}</span>
                                <el-input-number size="mini" v-model="elem.num" :min="0" :max="50" placeholder="阅卷数量"
                                    @change="changeNum(elem.num, index)"></el-input-number>
                                <i class="el-icon-delete" @click="deleteChecked(index)"
                                    style="float: right;line-height: 28px;"></i>
                            </div>
                        </div>
                    </div>
                    <div class="right">
                        <el-form :model="form" ref="aocForm" label-position="top">
                            <el-form-item label="任务分配规则：" :label-width="formLabelWidth" prop="userIds"
                                autocomplete="off">
                                <select name="1" id="select" @change="changeSelect">
                                    <option value="1">自定义分配</option>
                                    <option value="2">平均分配</option>
                                </select>
                            </el-form-item>
                            <el-form-item label="任务结束时间：" :label-width="formLabelWidth" prop="userIds"
                                autocomplete="off">
                                <el-date-picker type="datetime" v-model="date" placeholder="选择日期时间" style="width: 100%;"
                                    value-format="yyyy-MM-dd HH:mm:ss">
                                </el-date-picker>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
                <div style="text-align: right">
                    <el-button type="primary" @click="submitAoc"><i
                            class="el-icon-s-promotion"></i>&nbsp;&nbsp;发送</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Pagination from '../../components/Pagination'
export default {
    name:'listSmart',
    data() {
        return {
            flag: 1,//任务分发页面
            loadingFlag:false,
            dialogFormVisible: false,//控制分发页面
            loading: true, //是显示加载
            areaFlag:false,//区域框的显示
            date: '',//任务结束时间
            teacherName: '',
            sheetNum: '',//答题卡总数
            checkAll: false,//全选
            isIndeterminate: false,
            formLabelWidth: '140px',
            formInline: {
                page: 1,
                limit: 10,
                paperId: '',
                paperName:''
            },
            userparm: [], //搜索权限
            listData: [], //用户数据
            // 分页参数
            pageparm: {
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            //编辑
            form: {
                aocTitle: '',
                aocContent: '',
                userIds: [],//多选被选中者
            },
            cities: [],//随搜索而变化
            //所有老师
            // datas: [{
            //     userImage: 'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/avatar/fc8c965b-d034-4d2c-84bf-33356fd84c5d.png',
            //     userName: '张三',
            //     userId: 1
            // }, {
            //     userImage: 'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/avatar/ed063c00-0bbb-4c01-9fdb-497e89db7288.png',
            //     userName: '李四',
            //     userId: 2
            // }, {
            //     userImage: 'https://modox.oss-cn-hangzhou.aliyuncs.com/grade/avatar/0189d3c5-cf76-46d5-b470-e409e46940c5.png',
            //     userName: '王五',
            //     userId: 3
            //     }],
            datas:[],//所有老师
            checkedBox: [],//被选中的框
            checkedList: [],//被选中的框（包括名字和阅卷数量）
            checkedNames: [],//被选中的人的名字,
            paperId: [],//被选中的试卷,
            taskName: '',//任务名称
            taskList: [],//任务提交
            // 区域坐标
            areaList: '',
            downloadImg:'',//区域图片
            areaId: '',//区域的id
            scale: 1,//原图与页面图缩放比例
            naturalWidth: '',//原图宽
            distributedIds: [],//已被分配的区域的id 给这些区域加上边框
        }
    },
    components: {
        Pagination
    },
    created() {
        //模拟老师
        // this.cities = this.datas

        // this.getUser(this.data)
        this.getData()
        // for (let i = 0; i < this.datas.length; i++) {
        //     this.$set(this.datas[i], 'num', 0)
        // }
        // this.cities = JSON.parse(JSON.stringify(this.datas))
        // console.log(this.cities,this.datas);
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
                    // this.pageparm.currentPage = this.formInline.page
                    // this.pageparm.pageSize = this.formInline.limit
                    // this.pageparm.total = res.total
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
                    url: this.baseUrl+"/aoc/delete",
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
        submitAoc() {
            this.taskList = []
            this.checkedList.forEach(elem => {
                for (let i = 0; i < this.datas.length; i++) {
                    if (elem.userName == this.datas[i].userName) {
                        this.taskList.push({
                            userId: this.datas[i].userId,
                            paperId: this.paperId,
                            regionNumber:this.areaId,
                            taskName: this.taskName,
                            taskNum: elem.num,
                            endTime: this.date
                        })
                    }
                }
            })
            const loginMessage = this.$message({
						type: 'success',
						duration: 0,
						dangerouslyUseHTMLString: true,
						iconClass: 'el-icon-loading',
						customClass: 'messageLoading',
						message: '&nbsp;&nbsp;正在发送'
					})
            this.$.ajax({
                url: this.baseUrl + "/grading/task/addList",
                type: 'post',
                data: JSON.stringify({
                    taskList:this.taskList
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    console.log(res);
                    if (res.status == 200) {
                        loginMessage.close()
                        this.$message.success(res.data)
                    } else {
                        loginMessage.close()
                        this.$message.warning(res.msg)
                    }
                },
                error: () => {
                    loginMessage.close()
                    this.$message.error('请求发送失败，请稍后再试！')
                }
            })
        },
        lookAoc() {},
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
                url: this.baseUrl +"/feb/get2",
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
        changeBox() {
            console.log(this.checkedBox);
            this.$nextTick(() => {
                this.changeSelect()
            })
            let checkedCount = this.checkedBox.length;
            this.checkAll = checkedCount === this.datas.length;
            this.isIndeterminate = checkedCount > 0 && checkedCount < this.datas.length;
        },
        backer() {
            this.flag = 2
            this.checkedBox = []
            this.date = ''
            this.isIndeterminate = false
            for (let i = 0; i < this.datas.length; i++) {
                this.cities[i].num = 0
            }
            this.checkAll = false
            this.$('#select').val('1')
            this.$nextTick(() => {
                this.changeSelect()
            })
        },
        backer2() {
            this.flag = 1
        },
        deleteChecked(index) {
            this.checkedBox.splice(index, 1)
            let checkedCount = this.checkedBox.length;
            this.checkAll = checkedCount === this.datas.length;
            this.isIndeterminate = checkedCount > 0 && checkedCount < this.datas.length;
        },
        searchName() {
            if(this.teacherName == '') {
                this.cities = this.datas
                this.$('.checkAll').show()
            } else {
                this.$('.checkAll').hide()
                this.cities = this.datas.filter(item => item.userName == this.teacherName)
            }
        },
        handleCheckAllChange(val) {
            console.log(this.flag);
            if (this.datas.length) {
                console.log(this.datas);
                let arr = this.datas.map(item => item.userName)
                this.checkedBox = val ? arr : []
                this.isIndeterminate = false;
                this.$nextTick(() => {
                    this.changeSelect()
                })
            }
        },
        changeSelect() {
            if(this.$('#select').val() == '1') {
                // this.$('.el-input-number').show()
            } else {
                // this.$('.el-input-number').hide()
                this.checkedList.forEach(item => {
                    item.num = this.sheetNum/this.checkedBox.length
                })
                if(this.sheetNum%this.checkedBox.length != 0) {
                    const i = this.sheetNum % this.checkedBox.length
                    for(let j = 0; j < i; j++) {
                        this.checkedList[j].num += 1
                    }
                }
            }
        },
        changeNum(num, a) {
            if (this.surplusNum < 0) {
                this.$nextTick(() => {
                    this.checkedList[a].num = 0
                })
            }
        },
        taskDistribute(row) {
            // localforage.getItem('downloadImg').then(value => {
            //     this.downloadImg = value
            //         let image = new Image()
            //         //原图片原始地址（用于获取原图片的真实宽高，当<img>标签指定了宽、高时不受影响）
            //         image.src = this.downloadImg
            //         //获取原图片与页面的图片的缩放比
            //         image.onload = () => {
            //             console.log(image.width);
            //             this.naturalWidth = image.width
            //             this.$('.answerImg').width(this.naturalWidth/this.scale)
            //         }
            // })
            this.flag = 2
            this.areaFlag = false
            const loading = this.$loading({
              lock: true,
              text: '加载中',
              spinner: 'el-icon-loading',
              background: 'rgba(255, 255, 255, 0.7)'
            });
            this.loadingFlag = true
            this.$.ajax({
              url: this.baseUrl + "/grading/region/point/get",
              type: 'post',
              data: JSON.stringify({paperId:row.paperId}),
              contentType: "application/json",
              headers: {
                'ngrok-skip-browser-warning': 'true',
                'token': localStorage.getItem('token')
              },
              success: res => {
                  console.log(res);
                  this.loadingFlag = false
                  if (res.status == 200) {
                    this.downloadImg = res.data.tmpImage
                    this.areaList = JSON.parse(res.data.paperPoint)
                    let image = new Image()
                    //原图片原始地址（用于获取原图片的真实宽高，当<img>标签指定了宽、高时不受影响）
                    image.src = this.downloadImg
                    //获取原图片与页面的图片的缩放比
                    image.onload = () => {
                      console.log(image.width);
                      this.naturalWidth = image.width
                      // this.$('.answerImg').width(this.naturalWidth/this.scale)
                      this.scale = this.naturalWidth / 850
                      this.areaFlag = true
                      loading.close()
                      }
                  } else {
                        this.$message.error(res.msg)
                        loading.close()
                }
              },
                error: () => {
                loading.close()
                this.$message.error('请求失败，请稍后再试')
              }
            });
            console.log(row);
            this.paperId = row.paperId
            this.taskName = row.paperName + '阅卷任务'
            this.sheetNum = row.sheetNum
            const data = {
                school: localStorage.getItem('school'),
                subject: row.paperSubject
            }

            this.$.ajax({
              url: this.baseUrl + "/grading/task/teacherList",
              type: 'post',
              data: JSON.stringify(data),
              contentType: "application/json",
              headers: {
                'ngrok-skip-browser-warning': 'true',
                'token': localStorage.getItem('token')
              },
              success: res => {
                console.log(res);
                if (res.status == 200) {
                    this.datas = res.data
                    for (let i = 0; i < this.datas.length; i++) {
                        this.$set(this.datas[i], 'num', 0)
                    }
                    this.cities = JSON.parse(JSON.stringify(this.datas))
                }
              },
              error: () => {
                this.$message.error('请求失败，请稍后再试')
              }
            });
        },
        imgClick(a) {
            console.log(1);
            this.flag = 3
            this.areaId = a
            this.$message.success("当前选择第"+a+"区域")
        }
    },
    mounted() {
        
    },
    computed: {
        surplusNum() {
            let sum = 0
            this.checkedList.forEach(item => {
                sum += item.num
            })
            return (this.sheetNum - sum)
        }
    },
    watch: {
        surplusNum(newValue) {
            if(newValue < 0) {
                this.$message.warning("超出答题卡上限！")
            }
        },
        checkedBox(newValue) {
            this.datas.forEach((item) => {
                let check = this.checkedList.map(item => item.userName)
                if(newValue.includes(item.userName)&&!check.includes(item.userName)) {
                    this.checkedList.push({ userName: item.userName })
                    if (this.checkedList[this.checkedList.length-1].num == undefined) {
                        this.$set(this.checkedList[this.checkedList.length-1], 'num', 0)
                    }
                }
                if(!newValue.includes(item.userName)&&check.includes(item.userName)) {
                    this.checkedList.splice(check.indexOf(item.userName), 1)
                }
            })
        },
        flag(newValue) {
        }
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
/* 初始图片宽度为0 */
.answerImg {
    width: 850px;
}

.item {
    position: absolute;
    width: 100%;
    height: 100%;
    padding-left: 50px;
}
.answer {
    position: absolute;
    cursor: pointer;
    border-radius: 4px;
    border: 1px solid #ff7e7e;
}
.answer:hover {
    box-shadow: 0 0 20px #828282;
}
.area {
    cursor: pointer;
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
.flag2{
    display: flex;
    justify-content: center;
    position: relative;
}

#select{
    width: 100%;
    height: 40px;
    padding-left: 5px;
    background-color: #FFF;
    background-image: none;
    border-radius: 4px;
    border: 1px solid #DCDFE6;
    box-sizing: border-box;
    outline: none;
}
.left {
  display: flex;
  justify-content: center;
  position: relative;
  flex: 5;
  border-radius: 12px;
  padding: 10px 10px 10px 0;
}
.middle {
    padding: 10px;
    flex: 2;
}
.people {
    min-height: 400px;
    padding: 10px 5px;
    background-color: #fff;
    border-radius: 4px;
    border: 1px solid #DCDFE6;
}
.checkedPeople {
    width: 100%;
    line-height: 20px;
    padding: 5px;
    border-radius: 4px;
}
.checkedPeople:hover {
    background-color: #e1fcff;
}
.el-icon-delete {
    cursor: pointer;
}
.el-icon-delete:hover {
    color: #409EFF;
}
.right {
    flex: 2;
    padding: 10px;
}
.textarea /deep/ .el-textarea__inner {
    min-height: 105px!important;
}
.icon:hover path {
    fill: #24d3ff;
}
.icon:hover {
    cursor: pointer;
}
.worker {
    min-height: 400px;
    background-color: #fff;
    border-radius: 4px;
    border: 1px solid #DCDFE6;
}
.checkSon {
    position: relative;
    margin: 5px;
    padding: 3px;
    cursor: pointer;
    height: 80px;
}
.checkSon /deep/ .el-checkbox__input {
    display: none!important;;
  }
  .checkSon /deep/ .el-checkbox__label {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    padding: 0;
  }
.worker .checkSon {
  margin-left: 5px!important;
}
.checkSon .img {
  width: 50px;
  height: 50px;
}
.checkSon .name {
  margin-top: 2px;
  /* letter-spacing:5px;  */
}
.elTable /deep/ .cell{
    overflow: visible;
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