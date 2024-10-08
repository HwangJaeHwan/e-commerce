<script setup lang="ts">
import { onMounted, reactive } from 'vue';
import Order from "@/entity/order/Order";
import { container } from "tsyringe";
import OrderRepository from "@/repository/OrderRepository";
import ImageListRequest from "@/entity/image/ImageListRequest";
import ImageResponse from "@/entity/image/ImageResponse";
import HttpError from "@/http/HttpError";
import { ElMessage } from "element-plus";
import ImageRepository from "@/repository/ImageRepository";

const props = defineProps<{
  orderId: number;
}>();

const ORDER_REPOSITORY = container.resolve(OrderRepository);
const IMAGE_REPOSITORY = container.resolve(ImageRepository);

type StateType = {
  order: Order;
  imageMap: Map<string, string[]>;
};

const state = reactive<StateType>({
  order: new Order(),
  imageMap: new Map<string, string[]>(),
});

function getOrder() {
  ORDER_REPOSITORY.getOrder(props.orderId)
      .then((order) => {
        state.order = order;
        getImages();
      });
}

function getImages() {
  const request = new ImageListRequest();
  request.imageType = 'ITEM';
  request.uuids = state.order.items.map(item => item.itemUUID);

  IMAGE_REPOSITORY.getImages(request)
      .then((imageList: ImageResponse[]) => {
        for (const image of imageList) {
          for (const imageInfo of image.imageInfos) {
            const url = base64ToImage(imageInfo.imageData, imageInfo.mimeType);
            if (!state.imageMap.has(image.uuid)) {
              state.imageMap.set(image.uuid, []);
            }
            state.imageMap.get(image.uuid)?.push(url);
          }
        }
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() });
      });
}

function base64ToImage(base64String: string, mimeType: string) {
  const byteCharacters = atob(base64String);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);
  const blob = new Blob([byteArray], { type: mimeType });

  return URL.createObjectURL(blob);
}

function getOrderStatusText(status: string) {
  switch (status) {
    case 'ORDER':
      return '배달 준비';
    case 'PROCESSING':
      return '배달 중';
    case 'COMPLETED':
      return '배달 완료';
    case 'CANCELLED':
      return '취소';
    default:
      return '알 수 없는 상태';
  }
}

onMounted(() => {
  getOrder();
});
</script>

<template>
  <div class="container">
    <header class="header">
      <h1>주문 정보</h1>
      <div class="order-details">
        <div class="order-date">
          <strong>주문 시간:</strong> {{ state.order.getDisplaySimpleRegDate() }}
        </div>
        <div class="order-id">
          <strong>주문 번호:</strong> {{ state.order.orderId }}
        </div>
      </div>
    </header>

    <section class="order-info">
      <h2>{{ getOrderStatusText(state.order.orderStatus) }}</h2>
      <div v-for="(item, index) in state.order.items" :key="index" class="product-item">
        <img :src="state.imageMap.get(item.itemUUID)?.[0] || '/images/dog.jpg'" alt="product.name" class="product-image" />
        <div class="product-details">
          <h3>{{ item.name }}</h3>
          <p>{{ item.price.toLocaleString() }} 원 · {{ item.quantity }}개</p>
        </div>
      </div>
    </section>

    <section class="total-price">
      <h2>총 가격: {{ state.order.totalPrice.toLocaleString() }} 원</h2>
    </section>

    <section class="recipient-info">
      <h2>받는사람 정보</h2>
      <p>받는사람: {{ state.order.name }}</p>
      <p>연락처: {{ state.order.phoneNumber }}</p>
      <p>받는주소: {{ state.order.address }}</p>
    </section>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.header {
  border-bottom: 2px solid #4CAF50;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.order-details {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.order-date, .order-id {
  font-size: 14px;
}

.order-info {
  margin-bottom: 20px;
}

.product-item {
  display: flex;
  align-items: flex-start;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.product-image {
  width: 150px;
  height: 150px;
  object-fit: cover;
  margin-right: 20px;
}

.product-details {
  flex: 1;
  text-align: left;
}

.total-price {
  display: flex;
  justify-content: flex-end; /* 오른쪽 아래에 배치 */
  margin-top: 20px;
  font-size: 18px;
  font-weight: bold;
}

.recipient-info,
.payment-info {
  margin-bottom: 20px;
}

.payment-details p {
  margin: 5px 0;
}
</style>