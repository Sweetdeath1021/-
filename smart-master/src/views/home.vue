<template>
    <el-container class="index-con">
        <!-- <el-aside :class="showclass">
          <leftnav></leftnav>
        </el-aside> -->
        <el-container class="main-con">
          <el-header class="index-header">
            <navcon></navcon>
          </el-header>
          <el-main class="index-main">
            <router-view>
            </router-view>
          </el-main>
        </el-container>
    </el-container>
</template>

<script>
import navcon from '../components/navcon.vue'
export default {
    name:'IndexSmart',
    data() {
        return {

        }
  },
  components: {
      navcon
    }
}
</script>

<style>
.center {
  position: relative;
}
/* .center::after {
    position: absolute;
    content: '';
    display: block;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
} */
.el-main {
  padding: 0;
}
/* 隐藏滚动条 */
html {
	overflow: -moz-hidden-unscrollable;
	height: 100%;
}
body::-webkit-scrollbar {
	display: none;
}
body {
	/* width: calc(100vw + 18px); */
	height: 100%;
	-ms-overflow-style: none;
	overflow: auto;
}

.index-con {
  box-sizing: border-box;
}

.index-header {
  position: sticky;
  top: 0;
  height: 64px!important;
  width: 100%;
  padding: 0;
  background-color: #fff;
  z-index: 19;
  border-bottom: 1px solid #86868659;
  /* box-shadow: rgba(129, 129, 141, 0.2) 0px 0px 20px 0px; */
}

.index-main {
  overflow: hidden;
  width: 100%;
  height: 91.8vh;
  /* backdrop-filter: blur(10px); */
  /* background: #ffffff81; */
  /* -webkit-backdrop-filter: blur(10px); */
  background: -webkit-linear-gradient(top,#ebfffb,#F6F7F8 70%);
}
</style>
<style scoped>
.center {
  background-image:
        radial-gradient(closest-side, rgb(116, 240, 147), rgba(166, 230, 230, 0)),
        radial-gradient(closest-side, rgb(165, 241, 188), rgba(149, 243, 227, 0)),
        radial-gradient(closest-side, rgb(135, 221, 236),rgba(176, 238, 195, 0)),
        radial-gradient(closest-side, rgb(142, 240, 230),rgba(154, 241, 199, 0)),
        radial-gradient(closest-side, rgb(133, 212, 234),rgba(152, 227, 181, 0))!important;
  
}
</style>