<template>
<div class="result-board">
  <div class="result-board-text" v-if="$store.state.pk.loser==='all'">
    Draw
  </div>
  <div class="result-board-text" v-else-if="$store.state.pk.loser==='a'&&$store.state.pk.a_id==$store.state.user.id">
    Lose
  </div>
  <div class="result-board-text" v-else-if="$store.state.pk.loser==='b'&&$store.state.pk.b_id==$store.state.user.id">
    Lose
  </div>
  <div class="result-board-text" v-else>
    Win
  </div>
  <div class="result-board-btn">
    <button  @click="restart" type="button" class="btn btn-success btn-lg">再次游戏</button>
  </div>
</div>
</template>

<script>
import {useStore} from "vuex";
export default {
  name: "ResultBoard",
  setup(){
    const store=useStore();
    const restart=()=>{
      store.commit("updateStatus","matching");
      store.commit("updateLoser","none");
      store.commit("updateOpponent",{
        username:"对手名称",photo:"https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
      })
    }
    return {restart};
  }
}
</script>

<style scoped>
div.result-board{
  height: 30vh;
  width: 60vh;
  background-color: rgba(50,50,50,0.5);
  position: absolute;
  top: 30vh;
  left: 35vw;
}
div.result-board-text{
  text-align: center;
  font-size: 60px;
  color: white;
  font-weight: bold;
  font-style: italic;
  padding-top: 5vh;
}
div.result-board-btn{
  text-align: center;
}

</style>