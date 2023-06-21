<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="card" style="margin-top: 2mm">
          <div class="card-body">
            <img :src="$store.state.user.photo" style="width: 100%">
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card" style="margin-top: 2mm">
          <div class="card-header">
            <span style="font-size: 130%">我的bot</span>
            <!-- Button trigger modal -->
            <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
              创建bot
            </button>
          </div>
          <!-- Modal -->
          <div class="modal fade" id="add-bot-btn" tabindex="-1">
            <div class="modal-dialog modal-xl">
              <div class="modal-content">
                <div class="modal-header">
                  <h1 class="modal-title fs-5" id="exampleModalLabel">创建bot</h1>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                  <div class="mb-3">
                    <label for="add-bot-title" class="form-label">名称</label>
                    <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入bot名称">
                  </div>
                  <div class="mb-3">
                    <label for="add-bot-description" class="form-label">简介</label>
                    <textarea v-model="botadd.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入bot简介"></textarea>
                  </div>
                  <div class="mb-3">
                    <label class="form-label">代码</label>
                    <VAceEditor
                        v-model:value="botadd.content"
                        @init="editorInit"
                        lang="c_cpp"
                        theme="textmate"
                        style="height: 300px" />
                  </div>
                </div>
                <div class="modal-footer">
                  <div class="error_msg">{{botadd.error_msg}}</div>
                  <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                </div>
              </div>
            </div>
          </div>
          <div class="card-body">
            <table class="table table-striped table-hover">
              <thead>
                <tr>
                  <th>bot名称</th>
                  <th>创建时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="bot in bots" :key="bot.id">
                  <td>{{bot.title}}</td>
                  <td>{{bot.createTime}}</td>
                  <td>
                    <button type="button" class="btn btn-dark" style="margin-right: 10px"
                            data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-'+bot.id">修改</button>
                    <button type="button" class="btn btn-danger" @click="remove_bot(bot)">删除</button>
                    <div class="modal fade" :id="'update-bot-modal-'+bot.id" tabindex="-1">
                      <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h1 class="modal-title fs-5">创建bot</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                          </div>
                          <div class="modal-body">
                            <div class="mb-3">
                              <label for="update-bot-title" class="form-label">名称</label>
                              <input v-model="bot.title" type="text" class="form-control" id="update-bot-title">
                            </div>
                            <div class="mb-3">
                              <label for="update-bot-description" class="form-label">简介</label>
                              <textarea v-model="bot.description" class="form-control" id="update-bot-description" rows="3"></textarea>
                            </div>
                            <div class="mb-3">
                              <label class="form-label">代码</label>
                              <VAceEditor
                                  v-model:value="bot.content"
                                  @init="editorInit"
                                  lang="c_cpp"
                                  theme="textmate"
                                  style="height: 300px" />
                            </div>
                          </div>
                          <div class="modal-footer">
                            <div class="error_msg">{{bot.error_msg}}</div>
                            <button type="button" class="btn btn-primary" @click="update_bot(bot)">修改</button>
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
import {ref,reactive} from "vue";
import $ from 'jquery';
import {useStore} from "vuex";
import {Modal} from 'bootstrap/dist/js/bootstrap';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default {
  components:{
    VAceEditor
  },
  setup(){
    ace.config.set(
        "basePath",
        "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
    const store=new useStore();
    let bots=ref([]);
    let error_msg=ref("");
    //表单封装的对象
    const botadd=reactive({
      title:"",
      description:"",
      content:"",
      error_msg:"",
    });

    const refresh_bot=()=>{
      $.ajax({
        url:"http://localhost:3000/user/bot/getlist/",
        type:"get",
        headers:{
          Authorization:"Bearer "+store.state.user.token
        },
          success(resp){
            bots.value=resp;
          }
      }
      )
    };

    refresh_bot();

    const add_bot=()=>{
      botadd.error_msg="";
      $.ajax({
        url:"http://localhost:3000/user/bot/add/",
        type:"post",
        data:{
          title:botadd.title,
          description:botadd.description,
          content:botadd.content
        },
        headers:{
          Authorization:"Bearer "+store.state.user.token
        },
        success(resp){
          if(resp.error_msg==="success"){
            botadd.title="";
            botadd.description="";
            botadd.content="";
            Modal.getInstance("#add-bot-btn").hide();
            refresh_bot();
          }
          else{
            botadd.error_msg=resp.error_msg;
          }
        },
        error(resp){
          botadd.error_msg=resp.error_msg;
        }
      })
    }

    const remove_bot=(bot)=>{

      $.ajax({
        url:"http://localhost:3000/user/bot/remove/",
        type:"post",
        data:{
          bot_id:bot.id,
        },
        headers:{
          Authorization:"Bearer "+store.state.user.token
        },
        success(resp){
          if(resp.error_msg==="success"){
            refresh_bot();
          }
          else{
            error_msg.value=resp.error_msg;
          }
        },
        error(resp){
          error_msg.value=resp.error_msg;
        }
      })
    }

    const update_bot=(bot)=>{
      botadd.error_msg="";
      $.ajax({
        url:"http://localhost:3000/user/bot/update/",
        type:"post",
        data:{
          bot_id:bot.id,
          title:bot.title,
          description:bot.description,
          content:bot.content
        },
        headers:{
          Authorization:"Bearer "+store.state.user.token
        },
        success(resp){
          if(resp.error_msg==="success"){
            Modal.getInstance("#update-bot-modal-"+bot.id).hide();
            refresh_bot();
          }
          else{
            bot.error_msg=resp.error_msg;
          }
        },
        error(resp){
          bot.error_msg=resp.error_msg;
        }
      })
    }

    return {bots,botadd,add_bot,remove_bot,update_bot};

  }
}
</script>

<style scoped>
div.error_msg{
  color: red;
}

</style>