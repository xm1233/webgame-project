<template>
  <ContentField>
    排行榜
    <table class="table table-striped table-hover">
      <thead>
      <tr style="text-align: center">
        <th>用户</th>
        <th>rank分数</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.id" style="text-align: center;">
        <td>
          <img :src="user.photo" alt="" class="user-photo">
          &nbsp;
<!--          数据库中字段有下划线用驼峰命名-->
          <span class="user-username">{{user.userName}}</span>
        </td>
        <td>{{user.rating}}</td>
      </tr>
      </tbody>
    </table>
    <nav aria-label="Page navigation example">
      <ul class="pagination" style="float: right">
        <li class="page-item">
          <a class="page-link" href="#" aria-label="Previous" @click="click_page(-2)">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li :class="'page-item '+page.is_active" v-for="page in pages" :key="page.number">
          <a class="page-link" href="#" @click="click_page(page.number)">{{ page.number }}</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#" aria-label="Next" @click="click_page(-1)">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField";
import $ from 'jquery';
import {ref} from "vue";
import {useStore} from 'vuex';

export default {
  name: "RankListIndexView",
  components:{
    ContentField
  },
  setup(){
    const store=useStore();
    let current_page=1;
    let users=ref([]);
    // eslint-disable-next-line no-unused-vars
    let total_users=0;
    let pages=ref([]);

    const click_page=page=>{
      if(page===-2)page=current_page-1;
      else if(page===-1)page=current_page+1;
      let max_page=parseInt(Math.ceil(total_users/10))
      if(page>=1&&page<=max_page){
        current_page=page;
        pull_page(current_page);
      }
    }

    const update_pages=()=>{
      let max_page=parseInt(Math.ceil(total_users/10))
      let newpages=[];
      for(let i=current_page-2;i<=current_page+1;i++){
        if(i>=1&&i<=max_page){
          newpages.push({
            number:i,
            is_active:current_page===i?"active":""
          })
        }
      }
      pages.value=newpages;
    }

    const pull_page = page =>{ //传入参数page
      $.ajax({
            url:"http://localhost:3000/ranklist/getlist/",
            data:{
              page,
            },
            type:"get",
            headers:{
              Authorization:"Bearer "+store.state.user.token
            },
            success(resp){
              users.value=resp.ranklist;
              total_users=resp.user_count;
              update_pages();
            },
            error(resp){
              console.log(resp);
            }
          }
      )
    }

    pull_page(current_page);

    return {users,click_page,pages};


  }
}
</script>

<style scoped>
img.user-photo{
  border-radius: 50%;
  width: 4.5vh;
}
</style>