<script setup lang="ts">
import { onMounted, reactive } from "vue";
import type Order from "@/entity/order/Order";
import { container } from "tsyringe";
import OrderRepository from "@/repository/OrderRepository";
import ImageListRequest from "@/entity/image/ImageListRequest";
import ImageResponse from "@/entity/image/ImageResponse";
import ImageRepository from "@/repository/ImageRepository";
import OrderItem from "@/components/OrderItem.vue";
import Paging from "@/entity/data/Paging";

const ORDER_REPOSITORY = container.resolve(OrderRepository)
const IMAGE_REPOSITORY = container.resolve(ImageRepository)

type StateType = {
  orderList: Paging<Order>,
  imageMap: Map<string, string[]>
}

const state = reactive<StateType>({
  orderList: new Paging<Order>(),
  imageMap: new Map<string, string[]>()
})

function getOrders() {
  ORDER_REPOSITORY.getOrders()
      .then((orders) => {
        state.orderList = orders
        getImages()
      })
}

function getImages() {
  const request = new ImageListRequest()
  request.imageType = 'ITEM'
  state.orderList.items.forEach(order => {
    order.items.forEach(orderItem => {
      request.uuids.push(orderItem.itemUUID);
    })
  })

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
}

function base64ToImage(base64String, mimeType) {
  const byteCharacters = atob(base64String);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);
  const blob = new Blob([byteArray], { type: mimeType });

  return URL.createObjectURL(blob)
}

onMounted(() => {
  getOrders()
})
</script>

<template>
  <div class="order-list-container">
    <div class="order-list-header">
      <h1>주문목록</h1>
    </div>
    <div class="order-list-content">
      <div v-for="(order, index) in state.orderList.items" :key="index" class="order-item-wrapper">
        <OrderItem :map="state.imageMap" :order="order" />
      </div>
      <div class="order-summary">
        <div>
          <h2>20000원</h2>
          <el-button style="width: 150px" type="primary">구매하기</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.order-list-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 50px;
}

.order-list-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.order-list-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.order-item-wrapper {
  width: 80%;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.order-summary {
  width: 50%;
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.order-summary div {
  text-align: right;
}
</style>