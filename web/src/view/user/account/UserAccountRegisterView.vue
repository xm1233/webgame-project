<template>
  <ContentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="register">
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
          </div>
          <div class="mb-3">
            <label for="confirmedPassWord" class="form-label">确认密码</label>
            <input v-model="confirmedPassWord" type="password" class="form-control" id="confirmedPassWord" placeholder="请再次输入密码">
          </div>
          <div class="error_msg">{{ error_msg }}</div>
          <button type="submit" class="btn btn-primary">注册</button>
        </form>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField";
import {ref} from "vue";
import $ from 'jquery';
import router from "@/router";

export default {
  name: "UserAccountRegisterView",
  components:{
    ContentField
  },
  setup(){
    let username=ref('');
    let password=ref('');
    let confirmedPassWord=ref('');
    let error_msg=ref('');

    const register=()=>{
      $.ajax({
        url:"http://localhost:3000/user/account/register/",
        type:"post",
        data:{
          username: username.value,
          password: password.value,
          confirmedPassWord: confirmedPassWord.value
        },
        success(resp){
          if(resp.error_msg==="success"){
            router.push({name:"user_account_login"});
          }
          else{
            error_msg.value=resp.error_msg
          }
        }
      })
    }
    return {username,password,confirmedPassWord,error_msg,register};
  }
}
</script>

<style scoped>
div.error_msg{
  color: red;
}
button{
  width: 100%;
}

</style>