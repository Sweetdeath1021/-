<template>
    <el-container style="overflow: auto;height: calc(100vh - 64px);scroll-behavior: smooth;" class="elContainer">
        <el-aside class="aside">
            <el-menu :default-active="defaultActive" class="el-menu-vertical-demo" ref="elMenu" @select="menuSelect">
                <el-submenu index="1" class="elSubmenu1">
                    <template slot="title">
                        <i class="el-icon-location"></i>
                        <span>成绩分析</span>
                    </template>
                    <el-menu-item index="1-1">考情总览</el-menu-item>
                    <el-menu-item index="1-2">各班平均分</el-menu-item>
                    <el-menu-item index="1-3">成绩分布情况</el-menu-item>
                    <el-menu-item index="1-4">学业等级分布</el-menu-item>
                </el-submenu>
                <el-submenu index="2" class="elSubmenu2">
                    <template slot="title">
                        <i class="el-icon-location"></i>
                        <span>试卷分析</span>
                    </template>
                    <el-menu-item index="2-1">试卷质量</el-menu-item>
                    <el-menu-item index="2-2">试题得分率</el-menu-item>
                    <el-menu-item index="2-3">知识点得分率</el-menu-item>
                    <el-menu-item index="2-4">试卷讲评</el-menu-item>
                </el-submenu>
                <el-menu-item index="3">
                    <i class="el-icon-document"></i>
                    <span slot="title">小题分</span>
                </el-menu-item>
            </el-menu>
            <div style="position: absolute;bottom: 55px;left: 35px;width: 180px;" class="classes">
                <span style="display: inline-block;margin-bottom: 5px;margin-left: 5px;">班级</span>
                <el-select v-model="selectedClass">
                  <el-option
                    v-for="item in classes"
                    :key="item"
                    :label="item"
                    :value="item">
                  </el-option>
                </el-select>
            </div>
        </el-aside>
        <div class="rightMain">
            <div style="background-color: #ECFFFB;height: 30px;width: 1200px;position: fixed;z-index: 10;"></div>
            <div
                style="position: fixed;top: 85px;height: 50px;width: 1200px;background-color: #fafafa;border-radius: 10px;box-shadow: 0 4px 8px 1px #0000000a;z-index: 10;">
                <el-tabs class="elTab" @tab-click="tabClick" v-model="activeSubject">
                    <el-tab-pane label="物理" name="first"></el-tab-pane>
                    <el-tab-pane label="生物" name="second"></el-tab-pane>
                </el-tabs>
            </div>
            <div style="position: relative;margin-top: 70px;" v-show="flag==1" class="mainBox1">
                <div class="mainContent" id="1-1" 
                v-loading="loading"
                element-loading-text="正在加载"
                element-loading-background="rgba(255, 255, 255, 1)">
                    <h1><span>考情总览</span></h1>
                    <div style="width: 100%;display: flex;margin-top: 5px;">
                        <div class="scoreChild" style="margin-left: 40px;">
                            <span class="scoreTitle">满分</span>
                            <span class="scoreContent">{{ data[0] }}</span>
                        </div>
                        <div class="scoreChild">
                            <span class="scoreTitle">最高分</span>
                            <span class="scoreContent">{{ data[1] }}</span>
                        </div>
                        <div class="scoreChild">
                            <span class="scoreTitle">平均分</span>
                            <span class="scoreContent">{{ data[2] }}</span>
                        </div>
                    </div>
                    <div style="width: 1200px;height: 160px;" id="scoreChart"></div>
                    <div class="scoreAnalysis">
                        <div class="scoreAnalysisTitle">成绩分析</div>
                        <span style="font-weight: 700;">本班</span>共<span class="number">{{ data[3] }}</span>名学生参加考试，参与分析人数为<span
                            class="number">{{ data[4] }}</span>人， 最高分为<span class="number">{{ data[5] }}</span>分，排名全校 第<span
                            class="number">{{ data[6] }}</span>；
                        本班平均分<span class="number">{{ data[7] }}</span>； 排名第<span class="number">{{ data[8] }}</span>。 优秀率 <span
                            class="number">{{ data[9] }}</span> 共
                        <span class="number">{{ data[10] }}</span>人，排名第 <span class="number">{{ data[11] }}</span>；
                        良好率 <span class="number">{{ data[12] }}</span> 共
                        <span class="number">{{ data[13] }}</span>人，排名第 <span class="number">{{ data[14] }}</span>； 及格率 <span
                            class="number">{{ data[15] }}</span> 共 <span class="number">{{ data[16] }}</span>人，排名第 <span
                            class="number">{{ data[17] }}</span>；
                        低分率 <span class="number">{{ data[18] }}</span> 共 <span class="number">{{ data[19] }}</span>人，排名第 <span
                            class="number">{{ data[20] }}</span>。
                    </div>
                </div>
                <div class="mainContent" id="1-2"
                v-loading="loading"
                element-loading-text="正在加载"
                element-loading-background="rgba(255, 255, 255, 1)">
                    <h1><span>各班平均分</span></h1>
                    <div
                        style="margin: 0 20px;background-color: #dcfff5;line-height: 40px;padding: 0 30px;margin-top: 5px;font-size: 15px;color: #00ce95;">
                        本校所有班级平均分情况，蓝色为本班数据，超过本班的为红色，小于等于本班为绿色，虚线为全校均分。</div>
                    <div style="width: 1200px;height: 400px;" id="classChart"></div>
                    <div class="scoreAnalysis">
                        <div class="scoreAnalysisTitle">成绩分析</div>
                        共<span class="number">5</span>个班级、<span class="number">21</span>名学生参与分析， 最高分为<span
                            class="number">96</span>分，所在班级为<span class="number">高二3班</span>；
                        全校平均分为<span class="number">60.34</span>分，排名前三的班级分别是：
                        <span style="font-weight: 700;">高二3班、高二1班、高二2班</span>。
                    </div>
                </div>
                <div class="mainContent" id="1-3"
                v-loading="loading"
                element-loading-text="正在加载"
                element-loading-background="rgba(255, 255, 255, 1)">
                    <h1><span>成绩分布情况</span></h1>
                    <div
                        style="margin: 0 20px;background-color: #dcfff5;line-height: 40px;padding: 0 30px;margin-top:5px;font-size: 15px;color: #00ce95;">
                        分数段默认为
                        <div style="display: inline-block;position: relative;"><el-input class="elInput"
                                v-model="distributeInput" style="height: 32px;width: 100px;"
                                size="small"></el-input><span
                                style="position: absolute;top:0;right: 8px;color: #909399;">分/段</span>
                        </div>
                        ，分数段默认上限为最高分，下限为最低分。
                    </div>
                    <div style="width: 1200px;height: 400px;" id="distributeChart"></div>
                    <div class="scoreAnalysis">
                        <div class="scoreAnalysisTitle">分析解读</div>
                        本次考试成绩总共分为<span class="number">7</span>段，最高分区间为<span class="number">[90,100)</span>， 共 <span
                            class="number">2</span>
                        人，占比总人数的 <span class="number">9.52%</span>。峰值出现在 <span class="number">[70,80)</span> 分区间，共
                        <span class="number">7</span> 人，占比总人数的 <span class="number">33.33%</span>。
                    </div>
                </div>
                <div class="mainContent" id="1-4"
                v-loading="loading"
                element-loading-text="正在加载"
                element-loading-background="rgba(255, 255, 255, 1)">
                    <h1><span>学业等级分布</span></h1>
                    <div
                        style="margin: 0 20px;background-color: #dcfff5;line-height: 32px;padding: 0 30px;margin-top: 5px;font-size: 15px;color: #00ce95;">
                        本次考试设置的学业等级按 学生成绩/学生名次 分为5段，指标定义为： 。<br />
                        注：中括号代表包含，小括号代表不包含。</div>
                    <el-empty description="暂无数据"></el-empty>
                </div>
            </div>
            <div style="position: absolute;top: 70px;" v-show="flag==2" class="mainBox2">
                <div class="mainContent" id="2-1"
                v-loading="loading2"
                element-loading-text="正在加载"
                element-loading-background="rgba(255, 255, 255, 1)">
                    <h1><span>试卷质量</span></h1>
                    <div
                        style="margin: 0 20px;background-color: #dcfff5;line-height: 32px;padding: 0 30px;margin-top: 5px;font-size: 14px;color: #00ce95;">
                        试卷质量分析主要从试卷的难度、信度、和区分度三个指标进行分析。<br />
                        难度系数是指能够正确解答试题的人数和参加考试的总人数之比，即题的通过率或答对率。试题难度系数与试题试卷难易程度正好相反，难度系数越高，题目越简单。一般认为，难度系数在0.4~0.7之间比较合适，低于 0.4 说明试题太难，高于 0.7 说明试题太易，通常情况下，整套试卷难度系数配置比重达到 7:2:1 或 6:2:2
                        比较符合学校的习惯或测试的要求。<br />
                        信度是指试卷的稳定性和可靠程度。本测试采用分半信度法，信度的取值为 0~1，信度的值越大表示测验的可靠性越高。教育测量学理论认为，对于测验专业理论和能力的试卷，其信度应大于
                        0.70，信度在 0.70 以下的试卷不宜在该区域使用。当信度大于 0.70 时，测验可用于团体间的比较；当信度大于 0.85 时，测验可用于个体之间的比较。<br />
                        区分度是指试题区分考生能力的程度，它基于一个假设：能力高的考生比能力低的考生更有可能正确回答试题，区分度的取值在-1~1之间，区分度指数越高，试题的区分度就越好。一般认为，区分度指数在0.5 以上表明此题区分度很好，0.3~0.5 之间表明此题的区分度较好，0.2~0.3 之间表明此题的区分度不太好，需要修改，0.2
                        以下表明此题的区分度不好，应该淘汰。<br />
                    </div>
                    <div style="width: 100%;display: flex;margin-top: 20px;">
                        <div class="scoreChild" style="margin-left: 40px;">
                            <span class="scoreTitle">满分</span>
                            <span class="scoreContent">{{ data2[0] }}</span>
                        </div>
                        <div class="scoreChild">
                            <span class="scoreTitle">人数</span>
                            <span class="scoreContent">{{ data2[1] }}</span>
                        </div>
                        <div class="scoreChild">
                            <span class="scoreTitle">难度</span>
                            <span class="scoreContent">{{ data2[2] }}</span>
                        </div>
                        <div class="scoreChild">
                            <span class="scoreTitle">信度</span>
                            <span class="scoreContent">{{ data2[3] }}</span>
                        </div>
                        <div class="scoreChild">
                            <span class="scoreTitle">区分度</span>
                            <span class="scoreContent">{{ data2[4] }}</span>
                        </div>
                    </div>
                    <p style="margin: 20px 0;padding: 0 20px;">本学科试卷共<span class="number">{{ data2[5] }}</span> 道题目，总分 <span
                            class="number">{{ data2[6] }}</span> ， 主观题 <span class="number">{{ data2[7] }}</span> 道，分值占比 <span
                            class="number">{{ data2[8] }}</span>
                        ，客观题 <span class="number">{{ data2[9] }}</span> 道，分值占比 <span class="number">{{ data2[10] }}</span> 。</p>
                    <div style="padding: 0 20px;">
                        <el-table :data="tableData" border style="width: 100%">
                            <el-table-column prop="dimension" label="维度" align="center">
                            </el-table-column>
                            <el-table-column prop="peopleNum" label="人数" align="center">
                            </el-table-column>
                            <el-table-column prop="highest" label="最高分" align="center">
                            </el-table-column>
                            <el-table-column prop="average" label="平均分" align="center">
                            </el-table-column>
                            <el-table-column prop="standard" label="标准差" align="center">
                            </el-table-column>
                            <el-table-column prop="mode" label="众数" align="center">
                            </el-table-column>
                            <el-table-column prop="middle" label="中位数" align="center">
                            </el-table-column>
                            <el-table-column prop="difficulty" label="难度" align="center">
                            </el-table-column>
                            <el-table-column prop="distinction" label="区分度" align="center">
                            </el-table-column>
                            <el-table-column prop="reliability" label="信度" align="center">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="mainContent" id="2-2"
                v-loading="loading2"
                element-loading-text="正在加载"
                element-loading-background="rgba(255, 255, 255, 1)">
                    <h1><span>试题得分率</span></h1>
                    <div style="padding: 0 20px;">
                        <el-table :data="testScore" border style="width: 100%">
                            <el-table-column v-for="(value, key, index) in testScore[0]" :key="key"
                                :label='label(key, index)' :prop="key" align="center" :fixed="index==0">
                            </el-table-column>
                        </el-table>
                    </div>
                    <div id="testScoreEchart" style="width: 1200px;height: 400px;margin-top: 20px;"></div>
                </div>
                <div class="mainContent" id="2-3"
                v-loading="loading2"
                element-loading-text="正在加载"
                element-loading-background="rgba(255, 255, 255, 1)">
                    <h1><span>知识点得分率</span></h1>
                    <div style="padding: 0 20px;">
                        <el-table :data="pointScore" border style="width: 100%">
                            <el-table-column label='维度' prop="dimension" align="center">
                            </el-table-column>
                            <el-table-column v-for="(item, index) in pointTitle" :key="index" :label='item'
                                :prop="'point' + (index + 1)" align="center">
                            </el-table-column>
                        </el-table>
                    </div>
                    <div id="pointScoreEchart" style="width: 1200px;height: 400px;margin-top: 20px;"></div>
                </div>
                <div class="mainContent" id="2-4"
                v-loading="loading2"
                element-loading-text="正在加载"
                element-loading-background="rgba(255, 255, 255, 1)">
                    <h1><span>试卷讲评</span></h1>
                    <div class="evaluate">
                        <div style="padding: 0 20px;line-height: 30px;display: flex;align-items: center;">
                            <span style="margin-right: 20px;">班级得分率</span>
                            <span class="easy"><span class="easyColor"
                                    style="background-color: #F22222;"></span>极难</span>
                            <span class="easy"><span class="easyColor"
                                    style="background-color: #F66969;"></span>较难</span>
                            <span class="easy"><span class="easyColor"
                                    style="background-color: #8DBCE4;"></span>适中</span>
                            <span class="easy"><span class="easyColor"
                                    style="background-color: #008FE0;"></span>较易</span>
                            <span class="easy"><span class="easyColor"
                                    style="background-color: #0FC3B5;"></span>极易</span>
                        </div>
                        <div
                            style="margin: 20px 0 40px;height: 50px;display: flex;align-items: center;background-color: #fafafa;box-shadow: rgb(241 241 241) 0px 1px 7px 2px;overflow: auto">
                            <span class="topicChild" v-for="(item,index) in topicChild" :key="item.title"
                                :style="{ color: (index==0?'#fff':color[item.form-1]),borderColor:color[item.form-1]}"
                                @click="activeTopic($event, color[item.form - 1],item,index)">{{ item.title }}</span>
                        </div>
                        <div class="topicTop">
                            <span
                                style="width: 80px;text-align: center;border-radius: 5px;color: #fff;font-weight: 600;background-color: #00c089;">{{
                                currentTopic }}</span>
                            <span>分值：{{ topic[0] }}</span><span>平均分：{{ topic[1] }}</span><span>最高分：{{ topic[2] }}</span><span>学校得分率：{{ topic[3] }}</span><span>班级得分率：{{ topic[4] }}</span>
                        </div>
                        <div style="padding: 0 20px;">
                            <el-table :data="answerList" border style="width: 100%">
                                <el-table-column prop="answer" label="答案" width="50" fixed>
                                </el-table-column>
                                <el-table-column prop="sum" label="做答人数" width="80">
                                </el-table-column>
                                <el-table-column label="占比" width="230">
                                    <template slot-scope="scope">
                                        <el-progress :percentage="scope.row.percent"
                                            :color="scope.row.correct ? '#0FC3B5' : '#F22222'"></el-progress>
                                    </template>
                                </el-table-column>
                                <el-table-column label="做答学生(试题分)">
                                    <template slot-scope="scope">
                                        <span v-for="(item,index) in scope.row.students" :key="index">{{ item
                                            }}<span>({{scope.row.grade}}分)</span>{{ index==scope.row.sum-1?'':'、'
                                            }}</span>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </div>
            </div>
            <div style="position: absolute;top: 70px;" v-show="flag==3" class="mainBox3">
                <div class="mainContent" style="padding-top: 20px;padding-bottom: 0;">
                    <div
                        style="margin: 0 20px;background-color: #dcfff5;line-height: 25px;padding: 20px 30px;font-size: 14px;color: #00ce95;">
                        带有红色色块的分数表示该科为该生相对弱势学科，需要加强练习，提升成绩位次。<br />
                        带有蓝色色块的分数表示该科为该生相对优势学科，需要保持成绩，适当分配学习时间到弱势科目的学习中。<br />
                        弱势学科：科目成绩击败率低于总分成绩击败率超过5%的科目。优势学科：科目成绩击败率高于总分成绩击败率超过5%的科目。<br />
                        击败率：测验中低于目标个体成绩的人数百分比，公式为：击败率=(有效参考学生人数-目标名次) /（有效参考学生人数-1）。<br />
                        点击学生姓名可查看学生个人报告。<br />
                    </div>
                    <div style="padding: 20px;">
                        <el-input
                          placeholder="请输入姓名或准考证号查询"
                          v-model="input"
                          clearable
                          suffix-icon="el-icon-search"
                          style="width: 220px;">
                        </el-input>
                    </div>
                    <div style="padding: 0 20px 20px;">
                        <el-table :data="answerList" border style="width: 100%" ref="topicScore" class="topicScore">
                                <el-table-column prop="answer" label="序号" width="50" align="center">
                                </el-table-column>
                                <el-table-column prop="sum" label="学号" width="100" align="center">
                                </el-table-column>
                                <el-table-column label="准考证号" width="100" align="center">
                                </el-table-column>
                                <el-table-column label="姓名" width="100" align="center" fixed="">
                                </el-table-column>
                                <el-table-column label="班级" width="100" align="center">
                                </el-table-column>
                                <el-table-column label="分数" width="60" align="center">
                                </el-table-column>
                                <el-table-column label="方向校名次" width="100" align="center">
                                </el-table-column>
                                <el-table-column label="方向班名次" width="100" align="center">
                                </el-table-column>
                                <el-table-column v-for="(item,index) in xTitle" :key="index" :label="item" width="100" align="center">
                                </el-table-column>
                            </el-table>
                    </div>
                </div>
            </div>
        </div>
    </el-container>
</template>

<script>
export default {
    name: 'ExaminationSmart',
    data() {
        return {
            loading: false,
            loading2:false,
            flag: 1,
            classes: [],
            selectedClass:'高二3班',
            defaultActive:'1-1',
            activeSubject: 'first',
            distributeInput: 10,//各班学习情况的分段长度
            // tableData: [{
            //     dimension: '本校',
            //     peopleNum: 253,
            //     highest: 96,
            //     average: 60.34,
            //     standard: 18.05,
            //     mode: 62,
            //     middle: 62,
            //     difficulty: 0.6,
            //     distinction: 0.44,
            //     reliability:0.71
            // }, {
            //     dimension: '本班',
            //     peopleNum: 51,
            //     highest: 96,
            //     average: 73.29,
            //     standard: 12.87,
            //     mode: 84,
            //     middle: 82,
            //     difficulty: 0.73,
            //     distinction: 0.31,
            //     reliability: 0.54
            //     }],
            tableData:[],
            testScore: [
                {
                    dimension: '本校',
                    radio1: 81.03,
                    radio2: 67.59,
                    radio3: 63.64,
                    radio4: 71.15,
                    radio5: 54.15,
                    radio6: 58.1,
                    radio7: 81.42,
                    multiple8: 37.75,
                    multiple9:22.73,
                    multiple10:84.78,
                    multiple11:68.77,
                    multiple12: 45.06,
                    major13: 62.9,
                    major14: 83.63,
                    major15: 47.97,
                    major16:50.94
                },
                {
                    dimension:'本班',
                    radio1: 94.12,
                    radio2: 74.51,
                    radio3: 70.59,
                    radio4: 82.35,
                    radio5: 58.82,
                    radio6: 76.47,
                    radio7: 84.31,
                    multiple8: 50,
                    multiple9: 13.73,
                    multiple10: 95.1,
                    multiple11: 79.41,
                    multiple12: 67.65,
                    major13: 76.72,
                    major14: 90.85,
                    major15: 72.79,
                    major16: 67.03
                }
            ],
            pointScore: [
                {
                    dimension: '本校',
                    point1:22.73,
                    point2:52.37,
                    point3:47.97,
                    point4:64.43,
                    point5:75.1,
                    point6:63.64,
                    point7:72.45,
                    point8:62.9
                },
                {
                    dimension: '本班',
                    point1: 13.73,
                    point2: 68.92,
                    point3: 72.79,
                    point4: 74.51,
                    point5: 81.86,
                    point6: 70.59,
                    point7: 82.84,
                    point8: 76.72
                }
            ],
            pointTitle: [
                '牛顿第二定律',
                '振动及描述振动的物理量',
                '单摆 单摆的回复力和周期',
                '波动图像',
                '波长、频率和波速',
                '波的叠加问题',
                '光的折射',
                '测定玻璃的折射率'
            ],
            topicChild: [
                {
                    title: '单选1',//内容
                    form:5//类型 代表不同难度
                },
                {
                    title: '单选2',
                    form:4
                },
                {
                    title: '单选3',
                    form:4
                },
                {
                    title: '单选4',
                    form:4
                },
                {
                    title: '单选5',
                    form:3
                },
                {
                    title: '单选6',
                    form:4
                },
                {
                    title: '单选7',
                    form:4
                },
                {
                    title: '多选8',
                    form:3
                },
                {
                    title: '多选9',
                    form:1
                },
                {
                    title: '多选10',
                    form:5
                },
                {
                    title: '多选11',
                    form:5
                },
                {
                    title: '多选12',
                    form:3
                },
                {
                    title: '13',
                    form:4
                },
                {
                    title: '14',
                    form:5
                },
                {
                    title: '15',
                    form:4
                },
                {
                    title: '16',
                    form:3
                },
            ],//试卷讲评的小方块的内容
            xTitle: [],//试题得分率的x轴的title
            color: ['#F22222', '#F66969', '#8DBCE4', '#008FE0', '#0FC3B5'],//试卷讲评的相关颜色
            currentTopic: '',
            topicIndex: '',
            //试卷讲评
            answerList: [
                {
                    answer: 'C',
                    sum: 3,
                    percent: 5.88,
                    students: ['刘佳鑫','刘胜喆','张俊业'],
                    grade: 0,
                    correct:false
                },
                {
                    answer: 'D',
                    sum: 48,
                    percent: 94.12,
                    students: ['付易达', '何宜波', '俞舒瀚','刘沐宇','李鑫','禹乐','康师傅','李华','高启强','安欣','小明','小美'],
                    grade: 4,
                    correct:true
                }
            ],
            input: '',//小题分查询框
            sign: false,//是否加载过第二部分
            data: [],
            data2: [],
            topic:['4','3.76','4','81.03%','94.12%']
        }
    },
    methods: {
        tabClick(tab) {
            if (tab.index == 0) {
                if (this.activeSubject !== 'first') {
                    this.activeSubject = 'first'
                }
            } else if (tab.index == 1) {
                if (this.activeSubject !== 'second') {
                    this.activeSubject = 'second'
                }
            }
        },
        label(key,index) {
            const radio = new RegExp('radio\\d+')
            const multiple = new RegExp('multiple\\d+')
            if (index == 0) {
                return '维度'
            }
            else if (radio.test(key)) {
                return '单选'+index
            } else if (multiple.test(key)){
                return '多选'+index
            } else {
                return ''+index
            }
        },
        //获取折线图x轴的内容
        getX() {
            const radio = new RegExp('radio\\d+')
            const multiple = new RegExp('multiple\\d+')
            Object.keys(this.testScore[0]).forEach((item, index) => {
                if (index !== 0) {
                    if (radio.test(item)) {
                        this.xTitle[index-1] =  '单选' + index
                    } else if (multiple.test(item)) {
                        this.xTitle[index - 1] = '多选' + index
                    } else {
                        this.xTitle[index - 1] = ''+index
                }
                }
            })
        },
        activeTopic(e, color, item, index) {
            if (index == 0) {
                this.answerList = [{
                    answer: 'C',
                    sum: 3,
                    percent: 14.29,
                    students: ['刘佳鑫','刘胜喆','张俊业'],
                    grade: 0,
                    correct:false
                },
                {
                    answer: 'D',
                    sum: 18,
                    percent: 85.71,
                    students: ['付易达', '何宜波', '俞舒瀚','刘沐宇','李鑫','禹乐','康师傅','李华','高启强','安欣','小刘','小明','小美','小李','威少','小银','晓宇','小红'],
                    grade: 4,
                    correct:true
                    }],
                this.topic = ['4','3.76','4','81.03%','85.71%']
            } else {
                this.answerList = [{
                    answer: 'C',
                    sum: 5,
                    percent: 23.81,
                    students: ['刘佳鑫','刘沐宇','刘胜喆','张俊业','禹乐'],
                    grade: 0,
                    correct:false
                },
                {
                    answer: 'D',
                    sum: 16,
                    percent: 76.19,
                    students: ['付易达', '何宜波', '俞舒瀚','李鑫','康师傅','李华','高启强','安欣','小明','小美'],
                    grade: 4,
                    correct:true
                    }],
                this.topic = ['4','2.98','4','67.59%','76.19%']
            }
            this.$nextTick(() => {
                const activeColor = this.$('.active').css('borderColor')
                this.$('.active').css({ 'backgroundColor': 'transparent', 'color': activeColor, 'fontWeight': 400 })
                this.$(e.target).css({ 'backgroundColor': color, 'color': '#fff', fontWeight: 700 })
                this.$(e.target).addClass('active').siblings().removeClass('active')
                this.topicIndex = index
            })
        },
        menuSelect(index, indexPath) {
            //mainContent
            // console.log(index, indexPath);
            this.flag = indexPath[0]
            if (this.flag == 2 && this.sign == false) {
                this.loading2=true
                setTimeout(() => {
                    this.loading2 = false
                    this.data2 = ['100','21','0.73','0.54','0.31','16','100','4','52%','12','48%']
                    this.tableData = [{
                        dimension: '本校',
                        peopleNum: 253,
                        highest: 96,
                        average: 60.34,
                        standard: 18.05,
                        mode: 62,
                        middle: 62,
                        difficulty: 0.6,
                        distinction: 0.44,
                        reliability:0.71
                    }, {
                        dimension: '本班',
                        peopleNum: 21,
                        highest: 96,
                        average: 73.29,
                        standard: 12.87,
                        mode: 84,
                        middle: 82,
                        difficulty: 0.73,
                        distinction: 0.31,
                        reliability: 0.54
                    }],
                    this.echarts4()
                    this.echarts5()
                }, 500);
                this.sign = true
            }
            this.$nextTick(() => {
                this.$(`.elContainer`).scrollTop(this.$(`.mainContent[id=${index}]`).position().top + 1)
            })
        },
        echarts1() {
            // 基于准备好的dom，初始化echarts实例
        var scoreChart = this.$echarts.init(document.getElementById('scoreChart'));
        // 绘制图表
        scoreChart.setOption({
            title: [
                {
                    text: '优秀率',
                    left: '215px',
                    top: '35%',
                    textStyle: {
                        fontSize: 14,
                        fontStyle: "normal",
                        fontWeight: "normal"
                    }
                },
                {
                    text: '良好率',
                    left: '452px',
                    top: '35%',
                    textStyle: {
                        fontSize: 14,
                        fontStyle: "normal",
                        fontWeight: "normal"
                    }
                }, {
                    text: '及格率',
                    left: '691px',
                    top: '35%',
                    textStyle: {
                        fontSize: 14,
                        fontStyle: "normal",
                        fontWeight: "normal"
                    }
                },
                {
                    text: '低分率',
                    left: '932px',
                    top: '35%',
                    textStyle: {
                        fontSize: 14,
                        fontStyle: "normal",
                        fontWeight: "normal"
                    }
                }
            ],
            series: [
                {
                    type: 'pie',
                    data: [
                        {
                            value: 35,
                            name: 'A',
                        },
                        {
                            value: 65,
                            name: 'B',
                            silent: false,
                            itemStyle: {
                                color: '#E6EBF8'
                            }
                        }
                    ],
                    center:['20%','50%'],
                    radius: ['68%', '92%'],
                    labelLine: {
                        show: false
                    },
                    label: {
                        position: 'center',
                        fontSize: 15,
                        fontStyle: "normal",
                        fontWeight: 700,
                        color:'#5470C6',
                        formatter: '\n\n\n占比35.29%'
                    },
                    itemStyle: {
                        borderRadius: 10,
                    },
                    avoidLabelOverlap: false,
                    legend: {
                        show:false
                    },
                    emptyCircleStyle: {
                        color: '#E6EBF8'
                    },
                    colorBy: 'series',
                    startAngle:95
                },
                {
                    type: 'pie',
                    data: [
                        {
                            value: 66,
                            name: 'A',
                        },
                        {
                            value: 34,
                            name: 'B',
                            silent: false,
                            itemStyle: {
                                color: '#E6EBF8'
                            }
                        }
                    ],
                    center: ['40%', '50%'],
                    radius: ['68%', '92%'],
                    labelLine: {
                        show: false
                    },
                    label: {
                        position: 'center',
                        fontSize: 15,
                        fontStyle: "normal",
                        fontWeight: 700,
                        color: '#91CC75',
                        formatter: '\n\n\n占比66.67%'
                    },
                    itemStyle: {
                        borderRadius: 10,
                    },
                    avoidLabelOverlap: false,
                    legend: {
                        show: false
                    },
                    emptyCircleStyle: {
                        color: '#E6EBF8'
                    },
                    colorBy: 'series',
                    startAngle: 95
                },
                {
                    type: 'pie',
                    data: [
                        {
                            value: 82,
                            name: 'A',
                        },
                        {
                            value: 18,
                            name: 'B',
                            silent: false,
                            itemStyle: {
                                color: '#E6EBF8'
                            }
                        }
                    ],
                    center: ['60%', '50%'],
                    radius: ['68%', '92%'],
                    labelLine: {
                        show: false
                    },
                    label: {
                        position: 'center',
                        fontSize: 15,
                        fontStyle: "normal",
                        fontWeight: 700,
                        color: '#FAC858',
                        formatter: '\n\n\n占比82.35%'
                    },
                    itemStyle: {
                        borderRadius: 10,
                    },
                    avoidLabelOverlap: false,
                    legend: {
                        show: false
                    },
                    emptyCircleStyle: {
                        color: '#E6EBF8'
                    },
                    colorBy: 'series',
                    startAngle: 95
                },
                {
                    type: 'pie',
                    data: [
                        {
                            value: 4.5,
                            name: 'A',
                        },
                        {
                            value: 95.5,
                            name: 'B',
                            silent: false,
                            itemStyle: {
                                color: '#E6EBF8'
                            }
                        }
                    ],
                    center: ['80%', '50%'],
                    radius: ['68%', '92%'],
                    labelLine: {
                        show: false
                    },
                    label: {
                        position: 'center',
                        fontSize: 15,
                        fontStyle: "normal",
                        fontWeight: 700,
                        color: '#EE6666',
                        formatter: '\n\n\n占比0%'
                    },
                    itemStyle: {
                        borderRadius: 10,
                    },
                    avoidLabelOverlap: false,
                    legend: {
                        show: false
                    },
                    emptyCircleStyle: {
                        color: '#E6EBF8'
                    },
                    colorBy: 'series',
                    startAngle: 95
                }
            ]
        });
        },
        echarts2() {
            var classChart = this.$echarts.init(document.getElementById('classChart'));
        classChart.setOption({
            xAxis: {
                data: ['高二3班','高二1班','高二2班','高二4班','高二5班'],
                axisLabel: {
                    fontWeight: 700,
                    margin: 20,
                    rotate: 17,
                    align:'center'
                }
            },
            yAxis: {},
            series: [
                {
                    name:'平均分',
                    type: 'bar',
                    data: [{
                        value: 73.29,
                        itemStyle: {
                            color:'#38BFDD'
                        }
                    }, 69.79, 66.81, 52.22, 40.02],
                    markLine: {
                        data: [{
                            yAxis:60.34
                        }],
                        lineStyle: {
                            color:'#909399'
                        }
                    },
                    barWidth: '16%',
                    itemStyle: {
                    color:'#91CC75'
                    },
                    label: {
                        show: true,
                        position: "top"
                    }
                },
                
            ],
            tooltip: {
            }
        })
        },
        echarts3() {
            var distributeChart = this.$echarts.init(document.getElementById('distributeChart'));
        distributeChart.setOption({
            xAxis: {
                data: ['[90,100]', '[80,90)', '[70,80)', '[60,70)', '[50,60)','[40,50)','[30,40)'],
                axisLabel: {
                    fontWeight: 700,
                    margin: 20,
                    rotate: 17,
                    align: 'center'
                }
            },
            yAxis: {},
            series: [
                {
                    name: '平均分',
                    type: 'bar',
                    data: [2, 5, 7, 4, 2, 1, 0],
                    barWidth: '20%',
                    itemStyle: {
                        color: '#91CC75'
                    },
                    label: {
                        show: true,
                        position: "top"
                    }
                },

            ],
            tooltip: {
            }
        })
        },
        //知识点得分率
        echarts4() {
            var testScoreEchart = this.$echarts.init(document.getElementById('testScoreEchart'));
            testScoreEchart.setOption({
                xAxis: {
                    type: 'category',
                    data: this.xTitle,
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
                        name: '本校',
                        data: Object.values(this.testScore[0]).slice(1),
                        type: 'line',
                        label: {
                            show: true,
                            position: "top"
                        },
                        emphasis: {
                            focus: 'series'
                        },
                    },
                    {
                        name:'本班',
                        data: Object.values(this.testScore[1]).slice(1),
                        type: 'line',
                        label: {
                            show: true,
                            position: "top"
                        },
                        emphasis: {
                            focus: 'series'
                        },
                    }
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
        //知识点得分率
        echarts5() {
            var pointScoreEchart = this.$echarts.init(document.getElementById('pointScoreEchart'));
            pointScoreEchart.setOption({
                xAxis: {
                    type: 'category',
                    data: this.pointTitle,
                    axisLabel: {
                        fontWeight: 700,
                        margin: 30,
                        rotate: 15,
                        align: 'center'
                    }
                },
                yAxis: {
                    type: 'value'
                },
                grid: {
                    top: '10%',
                    bottom: '20%',
                    left: '5%',
                    right: '5%'
                },
                series: [
                    {
                        name: '本校',
                        data: Object.values(this.pointScore[0]).slice(1),
                        type: 'line',
                        label: {
                            show: true,
                            position: "top"
                        },
                        emphasis: {
                            focus: 'series'
                        },
                    },
                    {
                        name: '本班',
                        data: Object.values(this.pointScore[1]).slice(1),
                        type: 'line',
                        label: {
                            show: true,
                            position: "top"
                        },
                        emphasis: {
                            focus: 'series'
                        },
                    }
                ],
                legend: {
                    show: true,
                    left: 'center',
                    top: 'top'
                },
                tooltip: {
                    show: true,
                    trigger: 'axis'
                },
            })
        }
    },
    created() {
        this.loading=true
        setTimeout(() => {
            this.loading = false
            this.data = ['100','95','81.66','22','21','96','1','73.29','1','35.29%','18','1','66.7%','34','1','82.35%','42','1','0%','0','4']
        },500)
        if (localStorage.getItem('roleId') == 1) {
            this.classes = ['高二3班','高二1班','高二2班','高二4班','高二5班']
        } else {
            this.classes = ['高二3班']
        }
        this.getX()
        this.currentTopic = this.topicChild[0].title
    },
    mounted() {
        console.log(this.$(`.mainBox1 .mainContent`).eq(3).position().top + this.$(`.mainBox1 .mainContent`).eq(3).height() - this.$('.elContainer').height());
        //active用来记录试卷讲评上一个高亮的小块
        const color = this.$('.topicChild').eq(0).css('borderColor')
        this.$('.topicChild').eq(0).addClass('active').css({ 'backgroundColor': color, 'color': '#fff', 'fontWeight': 700 })

        this.$('.elContainer').on('scroll', () => {
            if (this.flag == 1) {
                if (this.$(`.elContainer`).scrollTop() < this.$(`.mainBox1 .mainContent`).eq(1).position().top) {
                    this.defaultActive = '1-1'
                } else if (this.$(`.elContainer`).scrollTop() < this.$(`.mainBox1 .mainContent`).eq(2).position().top) {
                    this.defaultActive = '1-2'
                } else if (this.$(`.elContainer`).scrollTop() < this.$(`.mainBox1 .mainContent`).eq(3).position().top + this.$(`.mainBox1 .mainContent`).eq(3).height() - this.$('.elContainer').height()){
                    this.defaultActive = '1-3'
                } else {
                    this.defaultActive = '1-4'
                }
                this.$('.elSubmenu1 .is-active').focus()
            }
            else if (this.flag == 2) {
                if (this.$(`.elContainer`).scrollTop() < this.$(`.mainBox2 .mainContent`).eq(1).position().top) {
                    this.defaultActive = '2-1'
                } else if (this.$(`.elContainer`).scrollTop() < this.$(`.mainBox2 .mainContent`).eq(2).position().top) {
                    this.defaultActive = '2-2'
                } else if (this.$(`.elContainer`).scrollTop() < this.$(`.mainBox2 .mainContent`).eq(3).position().top + this.$(`.mainBox2 .mainContent`).eq(3).height() - this.$('.elContainer').height()) {
                    this.defaultActive = '2-3'
                } else {
                    this.defaultActive = '2-4'
                }
                this.$('.elSubmenu2 .is-active').focus()
            }
        })

        this.$refs.elMenu.open(1)
        this.$refs.elMenu.open(2)

        setTimeout(() => {
            this.echarts1()
            this.echarts2()
            this.echarts3()
        }, 500);
    }
}
</script>
<style scoped>
.classes /deep/ .el-input__inner{
    border: none;
}
.rightMain {
    position: relative;
    border-radius: 10px;
    width: 1200px;
}

.aside {
    width: 240px !important;
}

.el-main {
    flex: initial;
}

.main {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.el-menu-vertical-demo {
    position: fixed;
    margin: 20px 0 0 35px;
    border: none;
    border-radius: 10px;
    width: 180px;
    overflow: hidden;
    z-index: 10;
}

/* background: -webkit-linear-gradient(top,#a2f7b5,#f9fff9); */
.header {
    position: fixed;
    height: 50px !important;
    z-index: 5;
    margin: 10px 0;
    border-radius: 12px;
    width: 85%;
    background-color: #ffffffc2;
}

.el-submenu .el-menu-item {
    min-width: none!important;
}
.el-menu-item {
    display: flex;
    align-items: center;
}
.el-menu-item, .el-submenu__title {
    line-height: initial;
}
.el-menu-item.is-active {
    color: #00ce95;
}
.el-menu-item.is-active {
    background-color: #d4fff4;
}
.el-menu-item:focus, .el-menu-item:hover {
    background-color: #d4fff4;
}
.el-menu /deep/ .el-submenu__title:hover {
    background-color: #d4fff4;
}
.elTab /deep/ .el-tabs__item.is-active {
    color: #00ce95;
}

.elTab /deep/ .el-tabs__item:hover {
    color: #00ce95;
}

.elTab /deep/ .el-tabs__active-bar {
    background-color: #00ce95;
}
.elTab /deep/ .el-tabs__item {
    height: 50px;
    line-height: 50px;
    padding-left: 20px !important;
    padding-top: 2px !important;
}

.elTab /deep/ .el-tabs__nav-wrap::after {
    height: 0;
}
.elTab /deep/ .el-tabs__header {
    margin: 0;
}
.mainContent {
    width: 1200px;
    background-color: #fff;
    padding-bottom: 20px;
    margin-bottom: 20px;
    border-radius: 5px;
}
.mainContent h1 {
    height: 30px;
    width: 100%;
    color: #333;
    font-weight: 700;
    font-size: 18px;
    padding: 15px 0;
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
.el-menu-vertical-demo /deep/ .el-submenu__title {
    background-color: #fafafa;
}
.scoreChild {
    display: flex;
    flex-direction: column;
    margin: 0 80px 20px 0;
}
.scoreTitle {
    font-size: 14px;
    font-weight: 700;
    margin-bottom: 5px;
}
.scoreContent {
    font-size: 32px;
    font-weight: 700;
    color: #00ce95;
}
.scoreAnalysis {
    position: relative;
    margin: 20px 20px 0;
    padding: 40px 40px 20px;
    background-color: #dcfff5;
}
.scoreAnalysisTitle {
    position: absolute;
    top:0;
    left: 0;
    padding: 0 20px;
    line-height: 30px;
    font-weight: 700;
    color: #fff;
    background: #00c089;
}
.number {
    color: #00c089;
    padding: 0 5px;
}
.elInput /deep/ .el-input__inner {
    padding-right: 45px;
    font-size: 15px;
}
.easy{
    display: flex;
    align-items: center;
    margin-right: 20px;
}
.easyColor {
    display: inline-block;
    width: 40px;
    height: 20px;
    margin-right: 10px;
}
.topicChild {
    white-space: nowrap;
    height: 30px;
    line-height: 30px;
    padding: 0 12px;
    margin: 0 10px;
    border: 1px solid #0fc3b5;
    border-radius: 5px;
    color: #0fc3b5;
    cursor: pointer;
    transition: all .5s;
}
.topicTop {
    display: flex;
    align-items: center;
    width: 100%;
    height: 70px;
    padding: 20px 40px;
    margin-bottom: 20px;
    background-color: #D4FFF4;
}
.topicTop span {
    line-height: 30px;
    margin-right: 20px;
}
.topicScore {
    position: relative;
}
</style>
<style>
.topicScore thead tr:nth-child(1) th:nth-child(4), .topicScore tbody tr>td:nth-child(4) {
    position: sticky;
    left: 0;
}
.el-progress-bar {
    width: 98%;
}
</style>