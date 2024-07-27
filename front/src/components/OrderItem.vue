<script setup lang="ts">

import type Order from "@/entity/order/Order";

const props = defineProps<{
  map: Map<string,string>,
  order: Order
}>()


</script>



<template>

  <div v-for="(orderItem,index) in props.order.orderItems" :key="index" >
    <div class="img-size image mb-3">
<!--      <img :src="props.map.get(orderItem.itemUUID)" alt="logo" class="img"/>-->
      <img src="/images/logo.png" alt="logo" class="logo"/>
    </div>

    <div>
      <div style="word-break: break-all" class="mb-3">
        {{orderItem.name}}
      </div>
      <div style="word-break: break-all" class="mb-3">
        {{orderItem.price}}
      </div>


    </div>
  </div>


</template>




<style>


</style>