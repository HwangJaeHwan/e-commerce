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
    <div class="item-list-container">

      <div class="item-list-image-wrapper mb-3">
        <img :src="props.map.get(props.item.itemUUID)?.[0] || '/images/dog.jpg'" alt="logo" class="item-list-image"/>
      </div>

      <div>
        <div class="item-list-name mb-3">
          {{props.item.name}}
        </div>
        <div class="item-list-price mb-3">
          {{props.item.price }}
        </div>

        <div v-if="props.item.score != null" class="item-list-score mb-3">
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
.item-list-container {
  display: flex;
  flex-direction: column;
  height: 400px;
}

.item-list-image-wrapper {
  height: 200px;
  position: relative;
  overflow: hidden; /* 이미지가 컨테이너 범위를 넘지 않도록 설정 */
}

.item-list-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.item-list-name {
  word-break: break-all;
}

.item-list-price {
  word-break: break-all;
}

.item-list-score {
  word-break: break-all;
}
</style>