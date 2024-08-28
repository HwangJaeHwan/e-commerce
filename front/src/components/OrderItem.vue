<script setup lang="ts">
import { useRouter } from 'vue-router';
import Order from "@/entity/order/Order";
import {container} from "tsyringe";
import OrderRepository from "@/repository/OrderRepository";
import {ElMessage} from "element-plus";
import HttpError from "@/http/HttpError";

const props = defineProps<{
  map: Map<string, string[]>,
  order: Order
}>();

const router = useRouter();
const ORDER_REPOSITORY = container.resolve(OrderRepository)

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

function handleWriteReview(itemUUID: string) {
  router.push({
    name: 'writeReview',
    query: {
      orderUUID: props.order.orderUUID,
      itemUUID: itemUUID
    }
  });
}

function handleCancelOrder() {
  // 주문 취소 로직 추가

  ORDER_REPOSITORY.cancelOrder(props.order.orderId)
      .then(()=>{
        ElMessage({ type: 'success', message: '주문을 취소했습니다.' })
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() });
      })
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

        <!-- 리뷰 작성 버튼 -->
        <div v-if="props.order.orderStatus === 'COMPLETED'" class="review-button-wrapper" @click.stop>
          <el-button type="primary" @click="handleWriteReview(orderItem.itemUUID)">
            리뷰 작성
          </el-button>
        </div>
      </div>
    </div>

    <!-- 주문 취소 버튼 -->
    <div v-if="props.order.orderStatus === 'ORDER'" class="cancel-button-wrapper">
      <el-button type="danger" @click="handleCancelOrder">
        주문 취소
      </el-button>
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
  cursor: pointer;
  transition: background-color 0.3s;
}

.order-item-row:hover {
  background-color: #f0f0f0;
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
  position: relative;
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

.review-button-wrapper {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

.cancel-button-wrapper {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
}

.order-info-button-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.order-info-button {
  align-self: flex-end;
}
</style>