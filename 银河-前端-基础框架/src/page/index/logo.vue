<template>
  <div class="avue-logo">
    <transition name="fade">
      <span v-if="keyCollapse" class="avue-logo_subtitle" key="0">
        <img src="img/bg/img-logo.png" width="30" />
      </span>
    </transition>
    <transition-group name="fade">
      <template v-if="!keyCollapse">
        <span class="avue-logo_title" key="1">
          <div>
            <img src="img/bg/img-logo.png" width="30" alt />
            <span>{{website.indexTitle}}</span>
            <span class="version">1.0.0</span>
          </div>
        </span>
      </template>
    </transition-group>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "logo",
  data() {
    return {};
  },
  created() {},
  computed: {
    ...mapGetters(["website", "keyCollapse"])
  },
  methods: {}
};
</script>

<style lang="scss">
.fade-leave-active {
  transition: opacity 0.2s;
}
.fade-enter-active {
  transition: opacity 2.5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.avue-logo {
  position: fixed;
  top: 0;
  left: 0;
  width: 240px;
  height: 64px;
  line-height: 64px;
  background-color: #3e3a3a;
  font-size: 22px;
  overflow: hidden;
  box-sizing: border-box;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.15);
  color: rgba(255, 255, 255, 0.8);
  z-index: 1024;
  &_title {
    display: block;
    text-align: center;
    font-weight: bold;
    font-size: 22px;
    font-style: italic;
    color: #fcc600;
    div {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      img {
        margin-right: 5px;
      }
    }
  }
  &_subtitle {
    padding-top: 10px;
    display: block;
    text-align: center;
    font-size: 18px;
    font-weight: bold;
    color: #fff;
  }
  .version {
    margin-left: 6px;
    font-weight: normal;
    line-height: 30px;
    position: relative;
    top: 5px;
    font-size: 14px;
    letter-spacing: 1px;
  }
}
</style>
