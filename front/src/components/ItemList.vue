<script setup lang="ts">
import { onMounted, computed } from "vue";
import type Item from "@/entity/item/Item";

const props = defineProps<{
  map: Map<string, string[]>,
  item: Item
}>();

const roundedScore = computed(() => {
  return props.item.score ? Math.round(props.item.score * 10) / 10 : null;
});

function getTest() {
  console.log("크랙!");
  console.log(JSON.stringify(props.map.get(props.item.itemUUID)));
  console.log(JSON.stringify(props.item.itemUUID));
}

onMounted(() => {
  getTest();
});

const hasScore = computed(() => props.item.score != null);
</script>

<template>
  <router-link :to="{ name: 'item', params: { itemUUID: props.item.itemUUID } }">
    <div class="item-list-container">
      <div class="item-list-image-wrapper mb-3">
        <img :src="props.map.get(props.item.itemUUID)?.[0] || '/images/dog.jpg'" alt="logo" class="item-list-image"/>
      </div>
      <div class="item-list-info">
        <div class="item-list-name mb-3">
          {{ props.item.name }}
        </div>
        <div class="item-list-price mb-3">
          {{ props.item.price }}
        </div>
        <div v-if="hasScore" class="item-list-score mb-3">
          <el-rate
              v-model="roundedScore"
              disabled
              show-score
              text-color="#ff9900"
              score-template="({value})"
          />
        </div>
        <div v-else class="item-list-placeholder mb-3"></div>
      </div>
    </div>
  </router-link>
</template>

<style scoped>
.item-list-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.item-list-container:hover {
  transform: translateY(-10px);
}

.item-list-image-wrapper {
  width: 100%;
  height: 200px;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.item-list-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain; /* 이미지를 컨테이너에 맞추되 잘리지 않도록 설정 */
}

.item-list-info {
  text-align: left; /* 왼쪽 정렬 */
  margin-top: 15px;
  min-height: 150px; /* 최소 높이 설정 */
  width: 100%; /* 전체 너비 사용 */
}

.item-list-name {
  font-size: 1.2rem;
  font-weight: bold;
  color: #333;
  text-align: left; /* 왼쪽 정렬 */
}

.item-list-price {
  font-size: 1.1rem;
  color: #E91E63;
  text-align: left; /* 왼쪽 정렬 */
}

.item-list-score {
  display: flex;
  justify-content: flex-start; /* 왼쪽 정렬 */
  align-items: center;
  gap: 5px;
  text-align: left; /* 왼쪽 정렬 */
}

.item-list-placeholder {
  height: 24px; /* 별점 컴포넌트의 높이와 일치 */
}
</style>