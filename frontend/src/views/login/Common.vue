<template>
  <div class="container">
    <div class="content" style="padding-top: 200px">
      <div class="top">
        <div class="header">
          <span style="font-size: 30px;"></span>
        </div>
      </div>
      <component :is="componentName" @regist="handleRegist" class="main-content"></component>
    </div>
    <global-footer :copyright="copyright" />
  </div>
</template>

<script>
import GlobalFooter from '../common/GlobalFooter'
import Login from './Login'
import Regist from './Regist'

export default {
  name: 'Common',
  components: {GlobalFooter, Login, Regist},
  data () {
    return {
      componentName: 'Login'
    }
  },
  computed: {
    systemName () {
      return this.$store.state.setting.systemName
    },
    copyright () {
      return this.$store.state.setting.copyright
    }
  },
  methods: {
    handleRegist (val) {
      this.componentName = val
    }
  }
}
</script>

<style lang="less" scoped>
  .container {
    display: flex;
    flex-direction: column;
    height: 100vh;
    overflow: auto;
    background: #f0f2f5 url(../../../static/img/river-6968614.png) no-repeat center ;
    background-size: contain;
    background-color: white;
    .content {
      padding: 32px 0;
      flex: 1;
      @media (min-width: 768px){
        padding: 116px 0 10px;
      }
      .top {
        text-align: center;
        .header {
          height: 50px;
          line-height: 50px;
          a {
            text-decoration: none;
          }
          .logo {
            width: 40px;
            height: 19px;
            vertical-align: center;
            margin-right: 16px;
          }
          .title {
            font-size: 28px;
            color: rgba(0,0,0,.85);
            font-family: 'Myriad Pro', 'Helvetica Neue', Arial, Helvetica, sans-serif;
            font-weight: 600;
            position: relative;
            top: 6px;
          }
        }
        .desc {
          font-size: 14px;
          color: rgba(0,0,0,.45);
          margin-top: 12px;
          margin-bottom: 40px;
        }
      }
      .main-content {
        width: 368px;
        margin: 0 auto;
        @media screen and (max-width: 576px) {
          width: 95%;
        }
        @media screen and (max-width: 320px) {
          .captcha-button{
            font-size: 14px;
          }
        }
      }
    }
  }
</style>
