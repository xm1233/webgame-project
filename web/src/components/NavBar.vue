<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <router-link class="navbar-brand" to="/">King Of Bot</router-link>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link :class="route_name==='pk_index'? 'nav-link active':'nav-link'" to="/pk/">对战</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name==='ranklist_index'?'nav-link active':'nav-link'" to="/ranklist/">对战列表</router-link>
          </li>
          <li class="nav-item">
            <router-link :class="route_name==='record_index'?'nav-link active':'nav-link'" to="/record/">排行榜</router-link>
          </li>
        </ul>
        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              {{$store.state.user.username}}
            </a>
            <ul class="dropdown-menu">
              <li>
                <router-link class="dropdown-item" to="/user/bot/">我的Bot</router-link>
              </li>
              <li><hr class="dropdown-divider"></li>
              <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav" v-else-if="$store.state.user.show_content">
          <li class="nav-item">
            <a class="nav-link" href="/user/account/login/" role="button" >登录</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/user/account/register/" role="button" >注册</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import {useRoute} from 'vue-router'
import {computed} from "vue"
import {useStore} from 'vuex'

export default {
  name: "NavBar",
  setup:()=>{
    const store=useStore();
    const route=useRoute();
    let route_name=computed(()=>route.name);
    const logout=()=>{
      store.dispatch("log_out")
    }
    return {route_name,logout};
  }
}
</script>

<style scoped>

</style>