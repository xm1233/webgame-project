<template>
<div>
  <NavBar></NavBar>
  <router-view></router-view>
</div>
</template>

<script>
import NavBar from "@/components/NavBar";
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
//import $ from 'jquery'

export default{
components:{
  NavBar
},
}
</script>

<style>
body{
  background-image: url("./assets/image/background.png");
  background-size: cover;
}
</style>
