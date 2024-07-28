<script setup lang="ts">

import Review from "@/entity/review/Review";
import type Item from "@/entity/item/Item";
import {onMounted} from "vue";

const props = defineProps<{
  map: Map<string,string[]>,
  item: Item
}>()

const score = props.item.score

function getTest() {
  console.log("크랙!")
  console.log(JSON.stringify(props.map.get(props.item.itemUUID)))
  console.log(JSON.stringify(props.item.itemUUID))

}

onMounted(() =>{
  getTest()

})

</script>



<template>

  <router-link :to="{ name: 'item', params: { itemUUID: item.itemUUID } }">
  <div class="item-list">

    <div class="img-size image mb-3">
      <img :src="props.map.get(props.item.itemUUID)?.[0] || '/images/dog.jpg'" alt="logo" class="img"/>
    </div>

    <div>
      <div style="word-break: break-all" class="mb-3">
        {{props.item.name}}
      </div>
      <div style="word-break: break-all" class="mb-3">
        {{props.item.price }}
      </div>

      <div v-if="props.item.score != null" style="word-break: break-all" class="mb-3">
        <el-rate
            v-model="score"
            disabled
            show-score
            text-color="#ff9900"
            score-template="({value})"
        />
      </div>


    </div>



  </div>

  </router-link>


</template>




<style>

.item-list{
  display: flex;
  flex-direction: column;
  height: 400px;
}
.img-size {
  height: 200px;
}


</style>