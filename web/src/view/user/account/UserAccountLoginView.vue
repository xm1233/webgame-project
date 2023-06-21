<template>
  <ContentField v-if="$store.state.user.show_content">
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
          </div>
          <div class="error_msg">{{ error_msg }}</div>
          <button type="submit" class="btn btn-primary">登录</button>
        </form>
      </div>
    </div>
  </ContentField>

</template>

<script>
import ContentField from "@/components/ContentField";
import {useStore} from 'vuex';
import {ref} from "vue";
import router from "@/router/index";

export default {
  name: "UserAccountLoginView",
  components:{
    ContentField
  },
  setup(){
    const store=useStore();
    let username=ref('');
    let password=ref('');
    let error_msg=ref('');

    const jwt_token=localStorage.getItem("jwt_token");
    if(jwt_token){
      //dispatch是去触发actions中的方法
      //commit是去触发mutations中的方法
      store.commit("updateToken",jwt_token)
      store.dispatch("getinfo",{
        success(){
          router.push({name:'home'});
          store.commit("updateShowContent",false)
        },
        error(){
          store.commit("updateShowContent",true)
        }
      })
    }
    else{
      store.commit("updateShowContent",true)
    }

    const login=()=>{
      error_msg.value='';
      store.dispatch("login",{
        username:username.value,
        password:password.value,
        success(){
          store.dispatch("getinfo",{
            success(){
              router.push({name:'home'});
            },
            error(){

            }
          })
        },
        error(){
          error_msg.value="用户名或密码输入错误";
        }
      })

    };

    return {username,password,error_msg,login};
  }
}
</script>

<style scoped>
button{
  width: 100%;
}
div.error_msg{
  color: red;
}

</style>