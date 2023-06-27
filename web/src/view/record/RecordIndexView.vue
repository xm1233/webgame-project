<template>
  <ContentField>
    对局列表
    <table class="table table-striped table-hover">
      <thead>
      <tr style="text-align: center">
        <th>玩家A</th>
        <th>玩家B</th>
        <th>对局结果</th>
        <th>创建对局时间</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="record in records" :key="record.record.id" style="text-align: center;">
        <td>
          <img :src="record.a_photo" alt="" class="record-user-photo">
          &nbsp;
          <span class="record-user-username">{{record.a_username}}</span>
        </td>
        <td>
          <img :src="record.b_photo" alt="" class="record-user-photo">
          &nbsp;
          <span class="record-user-username">{{record.b_username}}</span>
        </td>
        <td>{{record.result}}</td>
        <td>{{record.record.createtime}}</td>
        <td>
          <button type="button" class="btn btn-danger" @click="open_record_content(record.record.id)">查看录像</button>
        </td>
      </tr>
      </tbody>
    </table>
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField";
import $ from 'jquery';
import {useStore} from 'vuex'
import {ref} from "vue";
import router from "@/router";

export default {
  name: "RecordIndexView",
  components:{
    ContentField
  },
  setup(){
    const store=useStore();
    let current_page=1;
    let records=ref([]);
    // eslint-disable-next-line no-unused-vars
    let total_records=0;

    const pull_page = page =>{ //传入参数page
      $.ajax({
            url:"http://localhost:3000/record/getlist/",
            data:{
              page,
            },
            type:"get",
            headers:{
              Authorization:"Bearer "+store.state.user.token
            },
            success(resp){
              records.value=resp.records;
              total_records=resp.records_count;
            },
            error(resp){
              console.log(resp);
            }
          }
      )
    }

    const stringTo2D=map=>{
      let g=[];
      for(let i=0,k=0;i<13;i++){
        let line=[];
        for(let j=0;j<14;j++,k++){
          if(map[k]==='0')line.push(0);
          else line.push(1);
        }
        g.push(line);
      }
      return g;
    }

    const open_record_content = recordId=>{
      for(const record of records.value){
        if(record.record.id===recordId){
          store.commit("updateIsRecord",true);
          router.push({name:"record_content",params:{
            recordId
          }}
          )
          store.commit("updateGame",{
            map:stringTo2D(record.record.map),
            a_id:record.record.aid,
            b_id:record.record.bid,
            a_sx:record.record.asx,
            a_sy:record.record.asy,
            b_sx:record.record.bsx,
            b_sy:record.record.bsy,
          })
          store.commit("updateRecordLoser",record.record.loser)
          store.commit("updateSteps",{
            a_steps:record.record.asteps,
            b_steps:record.record.bsteps,
          })
          break;
        }
      }

    }

    pull_page(current_page);
    return {records,open_record_content}
  }
}
</script>

<style scoped>
img.record-user-photo{
  border-radius: 50%;
  width: 4.5vh;
}
</style>