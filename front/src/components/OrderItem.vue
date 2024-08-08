<script setup lang="ts">

import type Order from "@/entity/order/Order";

const props = defineProps<{
  map: Map<string, string[]>,
  order: Order
}>()

</script>

<template>
  <div class="order-container">
    <div v-for="(orderItem, index) in props.order.items" :key="index" class="order-item-row">
      <div class="order-item-image">
        <img :src="props.map.get(orderItem.itemUUID)?.[0] || '/images/dog.jpg'" alt="logo" class="order-item-img"/>
      </div>

      <div class="order-item-details">
        <div class="order-item-name mb-2">
          {{ orderItem.itemUUID }}
        </div>
        <div class="order-item-quantity mb-2">
          Quantity: {{ orderItem.quantity }}
        </div>
        <div class="order-item-price mb-2">
          Price: {{ orderItem.price | currency }}
        </div>
      </div>
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

.order-item-row {
  display: flex;
  align-items: center;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 10px;
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
</style>