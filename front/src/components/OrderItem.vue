<script setup lang="ts">
import { useRouter } from 'vue-router';
import Order from "@/entity/order/Order";

const props = defineProps<{
  map: Map<string, string[]>,
  order: Order
}>();

const router = useRouter();

function navigateToItemDetail(itemUUID: string) {
  router.push({ name: 'item', params: { itemUUID } });
}

function getOrderStatusText(status: string) {
  switch (status) {
    case 'ORDER':
      return '배송 준비';
    case 'PROCESSING':
      return '배송 중';
    case 'COMPLETED':
      return '배송 완료';
    case 'CANCELLED':
      return '취소';
    default:
      return '알 수 없는 상태';
  }
}
</script>

<template>
  <div class="order-container">
    <div class="order-status">
      {{ getOrderStatusText(props.order.orderStatus) }}
    </div>
    <div v-for="(orderItem, index) in props.order.items" :key="index" class="order-item-row" @click="navigateToItemDetail(orderItem.itemUUID)">
      <div class="order-item-image">
        <img :src="props.map.get(orderItem.itemUUID)?.[0] || '/images/dog.jpg'" alt="logo" class="order-item-img"/>
      </div>

      <div class="order-item-details">
        <div class="order-item-name mb-2">
          {{ orderItem.name }}
        </div>
        <div class="order-item-quantity mb-2">
          수량: {{ orderItem.quantity }}
        </div>
        <div class="order-item-price mb-2">
          가격: {{ orderItem.price }}
        </div>
      </div>
    </div>

    <!-- 주문 상세 정보로 이동하는 버튼 -->
    <div class="order-info-button-wrapper">
      <router-link :to="{ name: 'orderInfo', params: { orderId: String(props.order.orderId) } }">
        <el-button type="primary" class="order-info-button">주문 상세보기</el-button>
      </router-link>
    </div>
  </div>
</template>

<style scoped>
.order-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 20px;
  padding: 10px;
}

.order-status {
  font-weight: bold;
  font-size: 24px;
  margin-bottom: 10px;
}

.order-item-row {
  display: flex;
  align-items: center;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 10px;
  cursor: pointer; /* 클릭 가능하게 커서 변경 */
  transition: background-color 0.3s; /* 마우스 오버 시 배경색 변화 */
}

.order-item-row:hover {
  background-color: #f0f0f0; /* 마우스 오버 시 배경색 변경 */
}

.order-item-image {
  width: 150px;
  height: 150px;
  position: relative;
  flex-shrink: 0;
  margin-right: 20px;
}

.order-item-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.order-item-details {
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex-grow: 1;
}

.order-item-name {
  font-weight: bold;
  font-size: 18px;
  word-break: break-all;
}

.order-item-quantity,
.order-item-price {
  font-size: 16px;
  word-break: break-all;
}

.mb-2 {
  margin-bottom: 10px;
}

.order-info-button-wrapper {
  display: flex;
  justify-content: flex-end; /* 왼쪽 아래에 위치 */
  margin-top: 20px;
}

.order-info-button {
  align-self: flex-end;
}
</style>