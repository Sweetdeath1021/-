<template>
    <div class="base">
        <tui-image-editor :include-ui="useDefaultUI" :options="options" class="imageEditor" ref="tuiImageEditor"
            @objectAdded="add">
        </tui-image-editor>
        <div class="header"
            style="position: absolute;top: 0;height: 55px;line-height: 55px;display: flex;align-items: center;">
            <i class="el-icon-arrow-left" style="font-size: large;font-weight: 900;color: #1890ff;padding: 20px;"
                @click="backer"></i>
            <img src="../assets/img/应用.svg" alt="" style="width: 40px;height: 40px;margin-right: 50px;">
            <span style="font-size: large;">任务总数:</span>
            <span style="font-size: 25px;color: #00d9b2;margin-right: 20px;">{{ message.taskNum }}</span>
            <span style="font-size: large;">已阅数量:</span>
            <span style="font-size: 25px;color: #00d9b2;">{{ message.completedNum }}</span>
        </div>

        <button class="startRead"
            style="position: absolute;top: 7px;right: 270px;width: 120px;height: 40px;" @click="oneKey">一键评阅</button>
        <button class="imageBtn"
            style="position: absolute;top: 7px;right: 146px;background-color: #dcd8d8;border-color: #dcd8d8;"
            @click="prePaper">上一张</button>
        <button class="imageBtn" style="position: absolute;top: 7px;right: 20px;" @click="nextPaper">下一张</button>
        <div class="left">
            <div style="width: 17.9vw;height: 50px;background-color: #ffffffa3;border-radius: 0 5px 0 0;">
                <h3
                    style="display: inline-block;height: 50px;line-height: 50px;margin-bottom: 0;position:absolute;left: 50%;transform: translateX(-50%);margin-top: 0px;">
                    试卷列表</h3>
                <i class="el-icon-s-fold" style="font-size: 24px;float: right;margin: 10px 5px;"></i>
            </div>
            <div style="display: flex;flex-direction: column;align-items: center;overflow: scroll;" class="imgList">
                <img v-for="(elem, index) in imgList" :key="index" :src="elem.regionImage" alt="" class="imgs"
                    @click="getImg($event)"
                    :style="{ border: (elem.isGraded ? '2px solid #ff5656' :'2px solid #cdcdcd') }">
            </div>
        </div>
        <div class="right">
            <div
                style="width: 100%;height: 50px;background: #ffffffa3;display:flex;border-radius: 40px;position: relative;margin-top: 1px;">
                <div class="tabSon active" @click="tabMove(1)">
                    <span>题目打分</span>
                </div>
                <div class="tabSon" @click="tabMove(2)">
                    <span>上传答案</span>
                </div>
                <div class="tabSon" @click="tabMove(3)">
                    <span>AI评阅</span>
                </div>
                <div class="tabMove"></div>
            </div>
            <div v-show="flag == 1" class="score">
                <div style="background-color: #ffffff80;border-radius: 5px;padding: 10px 0;width: 100%;">
                    <div style="font-size: 14px;font-weight: 700;margin-bottom: 5px;text-align: center;">AI推荐分数</div>
                    <div style="font-size: 32px;font-weight: 700;color: #00ce95;text-align: center;" v-if="aiScore[imgIndex]">{{ aiScore[imgIndex] }}</div>
                    <div style="font-size: 16px;font-weight: 700;color: #00ce95;text-align: center;" v-else>暂未评阅</div>
                </div>
                <div
                    style="background-color: #ffffff80;padding: 10px 0 20px;display: flex;flex-direction: column;align-items: center;width: 100%;">
                    <el-input-number v-model="regionScore" :step="1" step-strictly :min="0"
                        style="width: 90%;"></el-input-number>
                    <button class="startRead" @click="changeScore" style="margin-top: 20px;">确定</button>
                </div>
            </div>
            <div v-show="flag == 2">
                <div style="margin-top: 10px;">
                    <el-dropdown trigger="click" class="eldropdown" @visible-change="dropdown1($event)">
                        <span class="el-dropdown-link">
                            <span style="color: #999;" v-if="this.subject.length == 0">请选择科目</span><span
                                class="checkboxList" v-else>{{ this.subject.join(' ') }}</span><i
                                class="el-icon-arrow-down el-icon--right icon1"
                                style="line-height: 55px;transition: all 0.3s;"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-checkbox-group v-model="subject" size="small" class="checkbox">
                                <el-checkbox label="语文" border></el-checkbox>
                                <el-checkbox label="数学" border></el-checkbox>
                                <el-checkbox label="英语" border></el-checkbox>
                                <el-checkbox label="物理" border></el-checkbox>
                                <el-checkbox label="化学" border></el-checkbox>
                                <el-checkbox label="生物" border></el-checkbox>
                            </el-checkbox-group>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
                <div style="margin-top: 10px;">
                    <el-dropdown trigger="click" class="eldropdown" @visible-change="dropdown2($event)">
                        <span class="el-dropdown-link">
                            <span style="color: #999;" v-if="this.readStyle.length == 0">请选择评阅风格</span><span
                                class="checkboxList" v-else>{{ this.readStyle.join(' ') }}</span><i
                                class="el-icon-arrow-down el-icon--right icon2"
                                style="line-height: 55px;transition: all 0.3s;"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-checkbox-group v-model="readStyle" size="small" class="checkbox">
                                <el-checkbox label="诙谐" border></el-checkbox>
                                <el-checkbox label="严谨" border></el-checkbox>
                                <el-checkbox label="简洁" border></el-checkbox>
                            </el-checkbox-group>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
                <div style="margin-top: 10px;">
                    <el-upload class="upload-demo" drag
                        action="#"
                        :on-change="changeFile"
                        :on-remove="removeFile"
                        :auto-upload="false"
                        :limit="1">
                        <img class="icon2" src="../assets/img/上传.svg" alt="" style="width: 50px;height: 50px;">
                        <div class="el-upload__text">请上传标准答案</div>
                    </el-upload>
                </div>
                <div class="view" style="margin-top: 10px;">
                    <el-image 
                    :src="srcList[0]"
                    class="elImg"
                    fit="cover"
                    :preview-src-list="srcList"></el-image>
                    <!-- <i class="el-icon-view"></i> -->
                    <div class="viewSubmit">点击预览</div>
                </div>
            </div>
            <div v-show="flag == 3">
                <div style="margin-top: 10px;display: flex;justify-content: center;">
                    <button class="startRead" @click="readSmart(regionId[imgIndex],imgList[imgIndex].regionImage)">开始评阅</button>
                </div>
                <div style="margin-top: 10px;">
                    <!-- <el-input type="textarea" placeholder="请输入内容" v-model="textarea" resize="none" class="textarea">
                    </el-input> -->
                    <div style="white-space: pre-wrap;" class="textarea" v-html="textarea" v-loading="textareaLoading"></div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
const locale_zh = {
  ZoomIn: '放大',
  ZoomOut: '缩小',
  Hand: '手掌',
  History: '历史',
  Resize: '调整宽高',
  Crop: '裁剪',
  DeleteAll: '全部删除',
  Delete: '删除',
  Undo: '撤销',
  Redo: '反撤销',
  Reset: '重置',
  Flip: '镜像',
  Rotate: '旋转',
  Draw: '画',
  Shape: '形状标注',
  Icon: '图标标注',
  Text: '文字标注',
  Mask: '遮罩',
  Filter: '滤镜',
  Bold: '加粗',
  Italic: '斜体',
  Underline: '下划线',
  Left: '左对齐',
  Center: '居中',
  Right: '右对齐',
  Color: '颜色',
  'Text size': '字体大小',
  Custom: '自定义',
  Square: '正方形',
  Apply: '应用',
  Cancel: '取消',
  'Flip X': 'X 轴',
  'Flip Y': 'Y 轴',
  Range: '区间',
  Stroke: '描边',
  Fill: '填充',
  Circle: '圆',
  Triangle: '三角',
  Rectangle: '矩形',
  Free: '曲线',
  Straight: '直线',
  Arrow: '箭头',
  'Arrow-2': '箭头2',
  'Arrow-3': '箭头3',
  'Star-1': '星星1',
  'Star-2': '星星2',
  Polygon: '多边形',
  Location: '定位',
  Heart: '心形',
  Bubble: '气泡',
  'Custom icon': '自定义图标',
  'Load Mask Image': '加载蒙层图片',
  Grayscale: '灰度',
  Blur: '模糊',
  Sharpen: '锐化',
  Emboss: '浮雕',
  'Remove White': '除去白色',
  Distance: '距离',
  Brightness: '亮度',
  Noise: '噪音',
  'Color Filter': '彩色滤镜',
  Sepia: '棕色',
  Sepia2: '棕色2',
  Invert: '负片',
  Pixelate: '像素化',
  Threshold: '阈值',
  Tint: '色调',
  Multiply: '正片叠底',
  Blend: '混合色',
  Width: '宽度',
  Height: '高度',
  'Lock Aspect Ratio': '锁定宽高比例'
}

const customTheme = {
  "common.bi.image": "../assets/img/阅卷.svg", // 左上角logo图片
  "common.bisize.width": "0px",
  "common.bisize.height": "0px",
  "common.backgroundImage": "none",
  "common.backgroundColor": "#f3f4f6",
  // "common.border": "1px solid #333",

  // header
  "header.backgroundImage": "none",
  "header.backgroundColor": "#fff",
  "header.border": "0px",
  
  // load button
  "loadButton.backgroundColor": "#fff",
  "loadButton.border": "1px solid #ddd",
  "loadButton.color": "#222",
  "loadButton.fontFamily": "NotoSans, sans-serif",
  "loadButton.fontSize": "12px",
  "loadButton.display": "none", // 隐藏

  // download button
  "downloadButton.backgroundColor": "#fdba3b",
  "downloadButton.border": "1px solid #fdba3b",
  "downloadButton.color": "#fff",
  "downloadButton.fontFamily": "NotoSans, sans-serif",
  "downloadButton.fontSize": "12px",
  "downloadButton.display": "none", // 隐藏

  // icons default
  "menu.normalIcon.color": "#8a8a8a",
  "menu.activeIcon.color": "#555555",
  "menu.disabledIcon.color": "#ccc",
  "menu.hoverIcon.color": "#e9e9e9",
  "submenu.normalIcon.color": "#8a8a8a",
  "submenu.activeIcon.color": "#e9e9e9",

  "menu.iconSize.width": "24px",
  "menu.iconSize.height": "24px",
  "submenu.iconSize.width": "32px",
  "submenu.iconSize.height": "32px",

  // submenu primary color
  "submenu.backgroundColor": "#1e1e1e",
  "submenu.partition.color": "#858585",

  // submenu labels
  "submenu.normalLabel.color": "#858585",
  "submenu.normalLabel.fontWeight": "lighter",
  "submenu.activeLabel.color": "#fff",
  "submenu.activeLabel.fontWeight": "lighter",

  // checkbox style
  "checkbox.border": "1px solid #ccc",
  "checkbox.backgroundColor": "#fff",

  // rango style
  "range.pointer.color": "#fff",
  "range.bar.color": "#666",
  "range.subbar.color": "#d1d1d1",

  "range.disabledPointer.color": "#414141",
  "range.disabledBar.color": "#282828",
  "range.disabledSubbar.color": "#414141",

  "range.value.color": "#fff",
  "range.value.fontWeight": "lighter",
  "range.value.fontSize": "11px",
  "range.value.border": "1px solid #353535",
  "range.value.backgroundColor": "#151515",
  "range.title.color": "#fff",
  "range.title.fontWeight": "lighter",

  // colorpicker style
  "colorpicker.button.border": "1px solid #1e1e1e",
  "colorpicker.title.color": "#fff",
};
import 'tui-color-picker/dist/tui-color-picker.css';
import 'tui-image-editor/dist/tui-image-editor.css';
import { ImageEditor } from '@toast-ui/vue-image-editor';
import { normalize } from 'gsap';
import localforage from 'localforage';

export default {
    name: 'PhotoEditor',
    components: {
        'tui-image-editor': ImageEditor,
    },
    data() {
        return {
            tabPosition:'first',
            useDefaultUI: true,
            options: {
                // for tui-image-editor component's "options" prop
                cssMaxWidth: 900,
                cssMaxHeight: 600,
                includeUI: {
                    loadImage: {
                      path: '',
                      name: 'image'
                    },
                    menu: ['resize', 'crop',  'draw', 'shape', 'icon', 'text'], // 底部菜单按钮列表 隐藏镜像flip和遮罩mask
                    initMenu: '', // 默认打开的菜单项
                    menuBarPosition: 'bottom', // 菜单所在的位置
                    locale: locale_zh, // 本地化语言为中文
                    theme: customTheme, // 自定义样式
                },
            },
            // imgList: [
            //     {
            //         regionId: 1,
            //         isGraded:false,
            //         regionImage: 'https://ts1.cn.mm.bing.net/th/id/R-C.22fc123aa05330a4f68c998766f43e94?rik=lCrVWHF%2fQMpiLA&riu=http%3a%2f%2fimg-download.pchome.net%2fdownload%2f1k1%2fee%2f2k%2folyjst-mvf.jpg&ehk=i84XSCytuXQg0nab4oH5qgZX6ckUsCCvIPIbEH8fu6Y%3d&risl=&pid=ImgRaw&r=0',
            //     },
            //     {
            //         regionId: 2,
            //         isGraded:true,
            //         regionImage: 'https://img-baofun.zhhainiao.com/pcwallpaper_ugc/static/50706fe64cbbdddc2c8c99d27eb64d4d.jpeg?x-oss-process=image%2fresize%2cm_lfit%2cw_3840%2ch_2160',
            //     },
            //     {
            //         regionId: 3,
            //         isGraded: false,
            //         regionImage: 'https://ts1.cn.mm.bing.net/th/id/R-C.4c848530d5db0372269a3d9eee485151?rik=RaV6%2fsh0GmVMtw&riu=http%3a%2f%2fimgs.aixifan.com%2flive%2f1483416197972%2f1483416197972.jpg&ehk=hedtNts0LGHxuGmui0GMdUsvMrEONZz410kBlMoZfSc%3d&risl=1&pid=ImgRaw&r=0',
            //     },
            //     {
            //         regionId: 4,
            //         isGraded: false,
            //         regionImage: 'https://tse3-mm.cn.bing.net/th/id/OIP-C.3ufuty4KNLVNgXcVL3pUKAHaEK?w=316&h=182&c=7&r=0&o=5&dpr=1.4&pid=1.7',
            //     },
            //     {
            //         regionId: 5,
            //         isGraded: false,
            //         regionImage: 'https://img-baofun.zhhainiao.com/pcwallpaper_ugc/static/50706fe64cbbdddc2c8c99d27eb64d4d.jpeg?x-oss-process=image%2fresize%2cm_lfit%2cw_3840%2ch_2160',
            //     }
            // ],
            imgList:[],
            imgIndex: 0,//当前选中的图片的次序
            subject: [],//多选的科目
            readStyle: [],
            textarea: '',
            message: {},//开始阅卷页面传过来的信息
            regionScore: 0,//当前题目的分数
            flag: 1,//用来控制tap栏
            canvasWidth: 0,//当前画布的宽度
            regionId: [],//当前图片Id的集合
            sampleFileList: '',//标准答案
            srcList: [],//图片预览
            aiScore: [],//AI评阅建议分数
            onekeyNum: 0,//一键评阅完成的题目数
            textareaLoading:false,
        };
    },
    methods: {
        add(props) {
            this.id = props.id
        },
        getImg(e) {
            // console.log(this.$(e.target).index());
            this.imgIndex = this.$(e.target).index()
            this.$(e.target).addClass('active').siblings().removeClass('active')
            this.$refs.tuiImageEditor.invoke("loadImageFromURL", this.$(e.target).prop('src'), '试卷').then(() => {
                this.canvasWidth = this.$refs.tuiImageEditor.invoke("getCanvasSize").width
            })
            this.getScore()
            this.getContent()
        },
        prePaper() {
            if (this.imgIndex == 0) {
                this.$message.warning('当前已是第一张')
            } else {
                this.imgIndex--
                this.$('.imgs').eq(this.imgIndex).addClass('active').siblings().removeClass('active')
                this.$refs.tuiImageEditor.invoke("loadImageFromURL", this.imgList[this.imgIndex].regionImage, '试卷').then(() => {
                    this.canvasWidth = this.$refs.tuiImageEditor.invoke("getCanvasSize").width
                })
                this.getScore()
                this.getContent()
            }
        },
        nextPaper() {
            if (this.imgIndex == this.imgList.length-1) {
                this.$message.warning('当前已是最后一张')
            } else {
                this.imgIndex++
                this.$('.imgs').eq(this.imgIndex).addClass('active').siblings().removeClass('active')
                this.$refs.tuiImageEditor.invoke("loadImageFromURL", this.imgList[this.imgIndex].regionImage, '试卷').then(() => {
                    this.canvasWidth = this.$refs.tuiImageEditor.invoke("getCanvasSize").width
                })
                this.getScore()
                this.getContent()
            }
        },
        tabMove(index) {
            this.$('.tabSon').eq(index - 1).addClass('active').siblings('.tabSon').removeClass('active')
            this.flag = index
            if (index == 1) {
                this.$('.tabMove').css('left',0)
            } else if(index ==2){
                this.$('.tabMove').css('left', this.$('.tabSon').outerWidth())
            } else {
                this.$('.tabMove').css('left', this.$('.tabSon').outerWidth()*2)
            }
        },
        dropdown1(e) {
            if (e) {
                this.$('.icon1').addClass('el-icon')
            } else {
                this.$('.icon1').removeClass('el-icon')
            }
        },
        dropdown2(e) {
            if (e) {
                this.$('.icon2').addClass('el-icon')
            } else {
                this.$('.icon2').removeClass('el-icon')
            }
        },
        backer() {
            this.$router.push('/teacherMark')
        },
        getScore() {
            this.$.ajax({
                url: this.baseUrl + "/grading/region/getScore",
                type: 'post',
                data: JSON.stringify({ regionId: this.regionId[this.imgIndex] }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    console.log(res);
                    if (res.status == 200) {
                        this.regionScore = res.data
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
        putScore() {
            this.$.ajax({
                url: this.baseUrl + "/grading/region/putScore",
                type: 'post',
                data: JSON.stringify({
                    regionId: this.regionId[this.imgIndex],
                    regionScore: this.regionScore
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
                        this.$message.success('打分成功')
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
        changeScore() {
            this.addText()
            this.putScore()
            this.getNumber()
        },
        addText() {
            if (this.imgList[this.imgIndex].isGraded) {
                this.$refs.tuiImageEditor.invoke('clearObjects').then(() => {
                    // console.log(this.canvasWidth/48 - this.canvasWidth/16);
                    this.$refs.tuiImageEditor.invoke('addText', this.regionScore + '分', {
                        styles: {
                            fill: 'red',
                            fontSize: this.canvasWidth/48,
                            fontWeight: 'bold',
                        },
                        position: {
                            x: this.canvasWidth - this.canvasWidth/16,
                            y: this.canvasWidth/64
                        }
                    }).then(() => {
                        console.log(2);
                        // this.$refs.tuiImageEditor.invoke('toDataURL').then(value => {
                        //     localforage.setItem('dataUrl', value)
                        // })
                    })
                })
            } else {
                this.$refs.tuiImageEditor.invoke('addText', this.regionScore + '分', {
                    styles: {
                        fill: 'red',
                        fontSize: this.canvasWidth/48,
                        fontWeight: 'bold',
                    },
                    position: {
                        x: this.canvasWidth - this.canvasWidth/16,
                        y: this.canvasWidth/64
                    }
                }).then(() => {
                    console.log(3);
                    // this.$refs.tuiImageEditor.invoke('toDataURL').then(value => {
                    //     localforage.setItem('dataUrl', value)
                    // })
                })
                this.imgList[this.imgIndex].isGraded = true
            }
        },
        getNumber() {
            this.$.ajax({
                url: this.baseUrl + "/grading/region/getNumber",
                type: 'post',
                data: JSON.stringify({
                    userId: this.message.userId,
                    paperId: this.message.paperId,
                    regionNumber: this.message.regionNumber
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    if (res.status == 200) {
                        this.message.completedNum = res.data
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
        changeFile(file, fileList) {
            this.sampleFileList = fileList[0].raw
            this.srcList.push(URL.createObjectURL(this.sampleFileList))
        },
        removeFile(file, fileList) {
            this.sampleFileList = ''
            this.srcList = []
        },
        readSmart(regionId,regionImage) {
            if (this.sampleFileList.uid) {
                const loginMessage = this.$message({
                    type: 'success',
                    duration: 0,
                    dangerouslyUseHTMLString: true,
                    iconClass: 'el-icon-loading',
                    customClass: 'messageLoading',
                    message: '&nbsp;&nbsp;正在评阅'
                })
                const formData = new FormData();
                    const data = {
                    regionId:regionId,
                    regionImage: regionImage
                }
                // for (let i = 0; i < this.sampleFileList; i++) {
                //     formData.append('images', this.sampleFileList[i])
                    // }
                formData.append('images', this.sampleFileList)
                formData.append('entity', new Blob([JSON.stringify(data)], { type: 'application/json' }));
                this.$.ajax({
                  url: this.baseUrl + "/grading/region/putIntelScore",
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
                      loginMessage.close()
                    if (res.status == 200) {
                        this.$message.success('AI评阅成功')
                        if (res.data) {
                            this.textarea = res.data.content.replace(/\\n/g, '<br>')
                            this.aiScore[this.imgIndex] = res.data.score
                        }
                    } else if (res.status == 500) {
                      this.$message.error(res.msg)
                    } else {
                      this.$message.error('评阅失败')
                    }
                  },
                  error: () => {
                    loginMessage.close()
                    this.$message.error('评阅失败，请稍后再试')
                  }
                });
            } else {
                this.$message.error('请上传标准答案')
            }
        },
        onekeySmart(regionId,regionImage,index) {
            const formData = new FormData();
                const data = {
                regionId:regionId,
                regionImage: regionImage
            }
            formData.append('images', this.sampleFileList)
            formData.append('entity', new Blob([JSON.stringify(data)], { type: 'application/json' }));
            this.$.ajax({
              url: this.baseUrl + "/grading/region/putIntelScore",
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
                    this.aiScore[index] = res.data.score
                    this.onekeyNum++
                    this.$notify({
                        title: this.onekeyNum == this.imgList.length ? 'AI评阅完成' : 'AI评阅进行中...',
                        dangerouslyUseHTMLString: true,
                        message: `<strong>当前已阅数目 <i style="color:#00ce95">${this.onekeyNum}</i> </strong>`,
                        position: 'top-left',
                        duration: 4500
                    });
                }
              }
            });
        },
        oneKey() {
            this.onekeyNum = 0
            if (this.sampleFileList.uid) {
                this.$notify({
                    title: 'AI评阅进行中...',
                    dangerouslyUseHTMLString: true,
                    message: `<strong>当前已阅数目 <i style="color:#00ce95">${this.onekeyNum}</i> </strong>`,
                    position: 'top-left',
                    duration: 4500
                });
                this.imgList.forEach((item, index) => {
                    console.log(item,index);
                    this.onekeySmart(item.regionId,item.regionImage,index)
                })
            } else {
                this.$message.error('请上传标准答案')
            }
        },
        getContent() {
            this.textareaLoading = true
            this.textarea = ''
            this.$.ajax({
              url: this.baseUrl + "/grading/region/getContent",
              type: 'post',
              data: JSON.stringify({regionId:this.regionId[this.imgIndex]}),
              headers: {
                'ngrok-skip-browser-warning': 'true',
                'token': localStorage.getItem('token')
              },
              contentType: "application/json",
              success: res => {
                  console.log(res);
                this.textareaLoading = false
                if (res.status == 200) {
                //   this.$message.success(res.msg)
                  this.textarea = res.data.replace(/\\n/g, '<br>')
                } else if (res.status == 500) {
                  this.$message.error(res.msg)
                } else {
                  this.$message.error('加载评语失败')
                }
              },
                error: () => {
                this.textareaLoading = false
                this.$message.error('加载评语失败，请稍后再试')
              }
            });
        },
        
    },
    created() {
        this.message = this.$route.params
        // console.log(this.message, this.$route.params);
        if (this.message.regionNumber) {
            localStorage.setItem('gradingMessage', JSON.stringify(this.message))
        } else {
            this.message = JSON.parse(localStorage.getItem('gradingMessage'))
        }
        this.getNumber()
        this.$.ajax({
            url: this.baseUrl + "/grading/region/imageList",
            type: 'post',
            data: JSON.stringify({
                userId: this.message.userId,
                paperId: this.message.paperId,
                regionNumber:this.message.regionNumber
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
                    this.imgList = res.data
                    this.$refs.tuiImageEditor.invoke("loadImageFromURL", this.imgList[0].regionImage, '试卷').then(() => {
                        this.canvasWidth = this.$refs.tuiImageEditor.invoke("getCanvasSize").width
                    })
                    this.$nextTick(() => {
                        this.$('.imgs').first().addClass('active')
                    })
                    this.regionId = res.data.map(item => item.regionId)
                    this.getScore()
                    this.getContent()
                    // this.options.includeUI.loadImage.path = this.imgList[0].regionImage
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
        // localforage.getItem('dataUrl').then(value => {
        //     this.$refs.tuiImageEditor.invoke("loadImageFromURL", value, '试卷')
        // })
    },
    beforeDestroy() {
    // 在组件销毁前释放 Blob URL
    if (this.srcList) {
       if (this.srcList) {
        this.srcList.forEach(item => {
        URL.revokeObjectURL(item);
      })
    }
    }
  }
};
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
.el-icon-arrow-left:hover {
    background-color: #ececec;
}

.imageEditor /deep/.tui-image-editor{
    top: 0!important;
}

.imageEditor /deep/ .tui-image-editor-submenu {
    display: none!important;
}
.base {
    position: relative;
    width: 100vw;
    height: 100vh;
    background: -webkit-linear-gradient(top,#ebfffb,#F6F7F8 70%);
}
.imageEditor {
    /* border-radius: 12px; */
    background: transparent;
}
.imageBtn {
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
    background-color: #81ffcd;
    border: 1px solid #81ffcd;
    color: #fff;
    font-family: 'Noto Sans', sans-serif;
    font-size: 12px;
}
.left {
    display: flex;
    flex-direction: column;
    align-items: center;
    position: absolute;
    top: 64px;
    height: calc(100vh - 130px);
    width: 18vw;
    /* background-color: #ffffff; */
    border-radius: 0 5px 5px 0;
    border: 1px solid #ababab59;
}
.imgList::-webkit-scrollbar {
    display: none;
}
.left .active {
    border: 3px solid #88d7ff!important;
}
.imgs {
    width: 80%;
    border: 2px solid #cdcdcd;
    border-radius: 7px;
    margin-bottom: 20px;
}
.imgs:hover {
    border: 3px solid #88d7ff!important;
}
.imgs:first-child {
    margin-top: 10px;
}
.right {
    position: absolute;
    right: 0;
    top: 64px;
    height: calc(100vh - 130px);
    width: 18vw;
    padding: 1px;
    /* background-color: #ededed; */
    border-radius:  5px 0 0 5px ;
    border: 1px solid #86868659;
}
.icon {
    width: 24px;
    height: 24px;
}
.tabSon {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0 10px;
    flex: 1;
    color: #bfbfbf;
    transition: color .2s ease-in-out;
    z-index: 2;
    cursor: pointer;
}
.active {
    color: #393939;
}
.active svg path {
    fill: #393939;
}
svg path {
    transition: color .2s ease-in-out;
}
.tabMove {
    position: absolute;
    left: 0;
    width: 33.333%;
    height: 50px;
    border-radius: 40px;
    background-image: linear-gradient(315deg, #a9ffb3 0, #87e3ff 100%);
    transition: left .1s ease-in-out;
}
.eldropdown {
    display: block;
    padding: 0;
    height: 55px;
}
.el-dropdown-link {
    display: flex;
    justify-content: space-between;
    padding: 0 10px;
    line-height: 55px;
    text-align: center;
    border-radius: 8px;
    font-size: 16px;
    color: #555;
    font-weight: 700;
    background: #ffffffa3;
    border: 1px solid #86868659;
    box-shadow: none;
    cursor: pointer;
}
.el-dropdown-link:hover {
    border-color: #18c3b1;
}
.checkbox /deep/ .el-checkbox__inner {
    display: none;
}

.checkbox /deep/ .el-checkbox__label {
    padding-left: 0;
}
.el-icon {
    transform: rotate(180deg);
}
.el-dropdown-menu {
    padding: 10px 10px 0 10px;
    max-width: 238px;
}
.el-checkbox {
    margin: 0 13px 10px !important;
}
.checkboxList {
    height: 55px;
    display: inline-block;
    overflow: hidden;
}
.startRead {
    border: 2px solid #86868659;
    width: 90%;
    height: 50px;
    border-radius: 40px;
    transition: all .1s;
    background-image: linear-gradient(315deg, #a9ffb3 0, #87e3ff 100%);
}
.startRead:hover {
    border-color: #21c2df;
}
.startRead:active {
    border-color: #07eb80;
}

.textarea {
    overflow: auto;
    background-color: #ffffffa3;
    height: calc(100vh - 256px);
    border: 1px solid #e1e1e1;
    border-radius: 4px;
    padding: 5px 10px;
    color: #414141;
    font-family: 'Courier New', Courier, monospace;
}
.textarea:hover {
    border: 1px solid #00ce95;
}
.imageEditor /deep/ .tui-image-editor-container .tui-image-editor-main-container {
    background: #ffffffc2 !important;
}
.score {
    display: flex;
    flex-direction:column;
    align-items: center;
}
.upload-demo /deep/ .el-upload{
    width: 100%;
}
.upload-demo /deep/ .el-upload-dragger {
    background-color: #ffffffa3;
    width: auto;
    height: auto;
}
.viewSubmit {
    position: absolute;
  pointer-events: none;
  color: #989ba1;
  z-index: 1;
}
.view {
  display: flex;
  flex-direction: column;
  position: relative;
  width: 100%;
  height: 100px;
  border-radius: 6px;
  border: 1px solid #e1e1e1;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  overflow: hidden;
  background-color: #ffffffa3;
}
.view:hover {
  border: 1px solid #00ce95;
}
.elImg /deep/ .el-image__error {
  display: none;
}
</style>
<style>
/* 隐藏滚动条 */
html {
	overflow: -moz-hidden-unscrollable;
	height: 100%;
}
body::-webkit-scrollbar {
	display: none;
}
body {
	width: calc(100vw + 18px);
	height: 100%;
	-ms-overflow-style: none;
	overflow: auto;
}

.tui-image-editor-container .tui-image-editor-controls {
    background: #ffffffc2;
}

.tui-image-editor-wrap::-webkit-scrollbar {
    display: none; 
}
.tui-image-editor-download-btn {
    display: none!important;
}
.tui-image-editor-container .tui-image-editor-main {
  right: initial;
  left: 50%;
  transform: translateX(-50%);
  width: 63vw;
  margin: 0 auto;
  border: 2px solid #cdcdcd;
  border-radius: 5px;
}
.tui-image-editor-container .tui-image-editor-main-container {
  background: none!important;;
}
</style>