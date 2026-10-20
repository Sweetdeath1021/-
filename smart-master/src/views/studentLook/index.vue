<template>
    <div id="base">
        <div class="left">
            <i class="el-icon-back" style="position: absolute;top: 11px;left: 11px;padding: 3px;
            font-size: 20px;background-color: #ffffffa3;border-radius: 5px;z-index: 10;"
            @click="back"></i>
            <div style="width: 17.9vw;height: 50px;background-color: #ffffffa3;border-radius: 0 5px 0 0;position: relative;">
                <h3
                    style="display: inline-block;height: 50px;line-height: 50px;margin-bottom: 0;position:absolute;left: 50%;transform: translateX(-50%);margin-top: 0px;">
                    试卷列表</h3>
                <i class="el-icon-s-fold" style="font-size: 24px;float: right;margin: 10px 5px;"></i>
            </div>
            <div style="display: flex;flex-direction: column;align-items: center;overflow: scroll;" class="imgList">
                <img v-for="(elem, index) in imgList" :key="index" :src="elem.regionImage" alt="" class="imgs"
                    @click="getImg($event,index)">
            </div>
        </div>
        <div class="middle">
            <img :src="currentImg" alt="" class="bigImg">
        </div>
        <div class="right">
            <div style="width: 17.9vw;height: 50px;background-color: #ffffffa3;border-radius: 0 5px 0 0;position: relative;">
                <h3
                    style="display: inline-block;height: 50px;line-height: 50px;margin-bottom: 0;position:absolute;left: 50%;transform: translateX(-50%);margin-top: 0px;">
                    评分细则</h3>
                <i class="el-icon-s-unfold" style="font-size: 24px;float: left;margin: 10px 5px;"></i>
            </div>
            <div v-loading="loading">
                <div style="margin: 2px 0;background-color: #ffffff80;border-radius: 5px;padding: 10px 0;">
                <div style="font-size: 14px;font-weight: 700;margin-bottom: 5px;text-align: center;">本题得分</div>
                <div style="font-size: 32px;font-weight: 700;color: #00ce95;text-align: center;">{{ score }}</div>
                </div>
                <div style="padding: 1px;">
                    <div style="white-space: pre-wrap;" class="textarea" v-html="textarea"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name:'studentLook',
    data() {
        return {
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
            loading:false,
            imgList: [],
            currentImg: '',
            textarea: '',
            score:'',
            index:0,//当前为第几区域
        }
    },
    created() {
        this.imgList = this.$route.params
        console.log(this.imgList);
        this.currentImg = this.imgList[0].regionImage
        this.getContent()
    },
    mounted() {
        this.$('.imgs').first().addClass('active')
    },
    methods: {
        getImg(e, index) {
            this.index = index
            this.currentImg = this.$(e.target).prop('src')
            this.$(e.target).addClass('active').siblings().removeClass('active')
            this.getContent()
        },
        back() {
            this.$router.push('/studentTable')
        },
        getContent() {
            this.loading = true
            this.textarea = ''
            this.$.ajax({
                url: this.baseUrl + "/grading/region/getContentAndScore",
                type: 'post',
                data: JSON.stringify({
                    regionId:this.imgList[this.index].regionId
                }),
                headers: {
                    'ngrok-skip-browser-warning': 'true',
                    'token': localStorage.getItem('token')
                },
                contentType: "application/json",
                success: res => {
                    console.log(res);
                    if (res.status == 200) {
                        this.textarea = res.data.content.replace(/\\n/g, '<br>')
                        this.score = res.data.score
                        this.loading = false
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
    }
}
</script>

<style scoped>
#base {
    position: relative;
    display: flex;
    justify-content: space-between;
    padding-top: 5px;
}
.left {
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    height: calc(100vh - 70px);
    width: 18vw;
    /* background-color: #ffffff; */
    border-radius: 0 5px 5px 0;
    border: 2px solid #cbcbcb59;
}
.imgList::-webkit-scrollbar {
    display: none;
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

.middle{
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 63.5vw;
    border: 2px solid #cbcbcb59;
    border-radius: 5px;
    background-color: #f4fffd74;
}
.bigImg {
    max-width: 95%;
    max-height: 95%;
}
.left .active {
    border: 3px solid #88d7ff!important;
}

.right {
    height: calc(100vh - 70px);
    width: 18vw;
    padding: 1px;
    /* background-color: #ededed; */
    border-radius:  5px 0 0 5px ;
    border: 2px solid #cbcbcb59;
}
.textarea {
    overflow: auto;
    background-color: #ffffffa3;
    height: calc(100vh - 210px);
    border: 1px solid #e1e1e1;
    border-radius: 4px;
    padding: 5px 4px 5px 10px;
    color: #414141;
    font-family: 'Courier New', Courier, monospace;
}
.textarea:hover {
    border: 1px solid #00ce95;
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