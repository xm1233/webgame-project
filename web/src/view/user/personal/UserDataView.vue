<template>
  <div class="container">
    <div class="row" style="text-align: center">
      <div class="col-12">
        <div class="card" style="margin-top: 2mm">

          <div class="card-body">
            <table class="table table-striped table-hover">
              <thead>
              <tr>
                <th>用户头像</th>
                <th>用户名</th>
                <th>操作</th>
              </tr>
              </thead>
              <tbody>
              <tr style="text-align: center">
                <td>
                  <img :src="$store.state.user.photo">
                </td>
                <td>{{$store.state.user.username}}</td>
                <td>
                  <button type="button" class="btn btn-dark" style="margin-right: 10px"
                          data-bs-toggle="modal" :data-bs-target="'#update-user-data'">修改</button>
                  <div class="modal fade" :id="'update-user-data'" tabindex="-1">
                    <div class="modal-dialog modal-xl">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h1 class="modal-title fs-5">修改用户信息</h1>
                          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                          <div class="mb-3">
                            <label for="update-user-name" class="form-label">用户姓名</label>
                            <input v-model="username" type="text" class="form-control" id="update-user-name">
                          </div>
                          <div class="mb-3">
                            <label for="update-user-photo" class="form-label">用户头像</label>
                            <textarea v-model="photo" class="form-control" id="update-user-photo" rows="3"></textarea>
                          </div>
                          <div class="mb-3">
                            <label for="update-user-password" class="form-label">密码</label>
                            <textarea v-model="password" class="form-control" id="update-user-password" rows="3"></textarea>
                          </div>
                          <div class="mb-3">
                            <label for="update-user-confirmpassword" class="form-label">确认密码</label>
                            <textarea v-model="confirmpassword" class="form-control" id="update-user-confirmpassword" rows="3"></textarea>
                          </div>
                        </div>
                        <div class="modal-footer">
                          <div class="error_msg">{{error_msg}}</div>
                          <button type="button" class="btn btn-primary" @click="updateUser">修改</button>
                          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from "jquery";
import {Modal} from 'bootstrap/dist/js/bootstrap';
import {ref} from "vue";
import {useStore} from 'vuex'

export default {
  name: "UserDataView",
  setup(){
    let username=ref('');
    let password=ref('');
    let photo=ref('');
    let confirmpassword=ref('');
    let error_msg=ref('');
    const store=useStore();


    const updateUser=()=>{
      $.ajax({
        url:"http://localhost:3000/user/personal/update/",
        type:"post",
        data:{
          user_username: username.value,
          photo:photo.value,
          password: password.value,
          confirmpassword: confirmpassword.value
        },
        headers:{
          Authorization:"Bearer "+store.state.user.token
        },
        success(resp){
          if(resp.error_msg==="success"){
            console.log(resp)
            Modal.getInstance("#update-user-data").hide();
            store.commit("updateUserData",{
              username:username.value,
              photo:photo.value,
              password:password.value
            })
          }
          else{
            error_msg.value=resp.error_msg
          }
        },
        error(resp){
          error_msg.value=resp.error_msg
        }
      })
    }
    return {photo,username,password,confirmpassword,error_msg,updateUser};
  }
}
</script>

<style scoped>
div.error_msg{
  color: red;
}

</style>