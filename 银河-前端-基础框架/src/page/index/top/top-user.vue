<template>
  <div class="topUser">
    <el-tooltip popper-class="ui-tooltip" placement="bottom">
      <div class="right-content">
        <img src="../../../../public/img/main//default.png" alt />
        <p class="name-dot">
          <span class="userName">{{userInfo.userName}}</span>
          <span class="netName">登录网点：{{userInfo.cpName}}</span>
        </p>
      </div>
      <div slot="content" class="tooltip-content">
        <p>姓名：{{userInfo.userName}}</p>
        <p>工号：{{userInfo.soaCode}}</p>
        <p>所在网点：{{userInfo.cpName}}</p>
        <p>部门信息：{{userInfo.dpmentName}}</p>
        <p class="exit">
          <span @click="logout">退出</span>
        </p>
      </div>
    </el-tooltip>
  </div>
</template>
<script>
import { getUserInfo } from "@/api/user";
export default {
  data() {
    return {
      userInfo: {}
    };
  },
  created() {
    getUserInfo().then(res => {
      this.userInfo = res.data.data;
    });
  },
  methods: {
    logout() {
      this.$confirm(this.$t("logoutTip"), this.$t("tip"), {
        confirmButtonText: this.$t("submitText"),
        cancelButtonText: this.$t("cancelText"),
        type: "warning"
      }).then(() => {
        this.$store.dispatch("LogOut").then(() => {
          if (this.baseUrl) {
            window.location.href = `${this.baseUrl}/galaxy-login/index.html#/login`;
          } else {
            window.location.href = "http://10.18.61.39:1888/#/login";
          }
        });
      });
    }
  }
};
</script>

<style lang="scss">
@import "../../../styles/variables.scss";
.right-content {
  display: flex;
  margin-left: 20px;
  cursor: pointer;
  box-sizing: border-box;
  img {
    position: relative;
    top: 4px;
    width: 36px;
    height: 36px;
    margin-right: 10px;
  }
  .name-dot {
    display: flex;
    flex-direction: column;
    padding-top: 4px;
    font-size: 12px;
    .userName {
      color: $color66;
      margin-bottom: 4px;
    }
    .netName {
      color: $color66;
    }
  }
}
.ui-tooltip {
  width: 200px;
  top: 50px !important;
  // box-shadow: -5px 5px 10px #666;
  background-color: rgba(28, 36, 48, 0.8);
  border: none;
  font-size: 14px;
  .tooltip-content {
    p {
      line-height: 30px;
      text-align: left;
    }
  }
  .exit {
    margin-top: 4px;
    border-top: 1px solid #fff;
    span {
      display: inline-block;
      line-height: 25px;
      padding: 0 5px;
      margin-top: 6px;
      text-align: center;
      background-color: $themeColor;
      border: 1px solid $themeColor;
      border-radius: 4px;
      cursor: pointer;
      color: $color33;
    }
    span:hover {
      background-color: $hoverColor;
      border: 1px solid $hoverColor;
    }
  }
}
</style>
