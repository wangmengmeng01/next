<template>
  <div class="home">
    <myHeader></myHeader>
    <section>
      <div class="side">
        <ul>
          <li>
            <div @click="ZTFlag = !ZTFlag">
              <img v-show="!ZTFlag" src="../assets/imgs/hide.png">
              <img v-show="ZTFlag" src="../assets/imgs/show.png">
              <span>韵达中台</span>
            </div>
            <div v-show="ZTFlag">
              <span :class="{active: index === 0}" @click="get(0,'business')">技术中台</span>
              <span :class="{active: index === 1}" @click="get(1,'business')">数据中台</span>
              <span :class="{active: index === 2}" @click="get(2,'business')">移动中台</span>
              <span :class="{active: index === 3}" @click="get(3,'business')">业务中台</span>
              <span :class="{active: index === 4}" @click="get(4,'business')">用户体验中台</span>
            </div>
          </li>
          <li>
            <div @click="searchFlag = !searchFlag">
              <img v-show="!searchFlag" src="../assets/imgs/hide.png">
              <img v-show="searchFlag" src="../assets/imgs/show.png">
              <span>用户管理</span>
            </div>
            <div v-show="searchFlag">
              <span :class="{active: index === 5}" @click="get(5,'user')">账户管理</span>
              <span :class="{active: index === 6}" @click="get(6,'role')">角色管理</span>
            </div>
          </li>
					<li>
            <div @click="get(7,'app')">
              <img v-show="false" src="../assets/imgs/hide.png">
              <img v-show="false" src="../assets/imgs/show.png">
              <span :class="{active: index === 7}" style="margin-left: 18px;">应用管理</span>
            </div>
            <!-- <div v-show="appFlag">
              <span :class="{active: index === 7}" @click="get(7,'user')">应用管理</span>
            </div> -->
          </li>
        </ul>
      </div>
      <div class="containt">
				<router-view/>
      </div>
    </section>
    <div class="bottom">
      东普信息科技有限公司
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import myHeader from '@/components/header.vue'

export default {
  name: 'home',
  data() {
    return {
      appFlag:true,
      ZTFlag:true,
      searchFlag:true,
      index:-1,
    }
  },
  components: {
    myHeader
  },
  created() {
  },
  methods: {
    get(i,path) {
      this.index = i;
			this.$router.push(`/home/${path}`)
    }
  }
}
</script>
<style lang="scss" scoped>
.home {
  height: 100%;
  section {
    position: fixed;
    left: 0;
    top: 110px;
    bottom: 35px;
    width: 100%;
    display: flex;
		background: #F2F2F2;
    .side {
      width: 250px;
      height: 100%;
      border-right: 1px solid #E4E4E4;
      ul {
        list-style: none;
        margin: 0;
        padding: 0;
        color: #333333;
        font-weight: bold;
        font-size: 14px;
        li {
					cursor: pointer;
          margin-top: 10px;
          &>div:first-child {
            padding-left: 30px;
            img {
              margin-right: 5px;
              vertical-align: middle;
            }
          }
          &>div:nth-child(2) {
            line-height: 20px;
            padding-left: 60px;
            span {
              display: block;
            }
          }
          .active {
            color: #000000;
            font-weight: bold;
          }
        }
      }
    }
    .containt {
      width: calc(100% - 250px);
      height: 100%;
    }
  }
  .bottom {
    background: #FFCC33;
    position: fixed;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 35px;
    line-height: 35px;
    text-align: center;
    font-size: 14px;
    color: #333;
  }
}
</style>