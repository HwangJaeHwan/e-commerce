<script setup lang="ts">

import type Order from "@/entity/order/Order";

const props = defineProps<{
  map: Map<string,string[]>,
  order: Order
}>()


</script>



<template>

  <div v-for="(orderItem,index) in props.order.items" :key="index" >
    <div class="img-size image mb-3">
<!--      <img :src="props.map.get(orderItem.itemUUID)" alt="logo" class="img"/>-->
      <img :src="props.map.get(orderItem.itemUUID)?.[0] || '/images/dog.jpg'" alt="logo" class="img"/>
    </div>

    <div>
      <div style="word-break: break-all" class="mb-3">
        test
      </div>
      <div class="mb-3">
        {{orderItem.quantity}}
      </div>
      <div style="word-break: break-all" class="mb-3">
        {{orderItem.price}}
      </div>


    </div>
  </div>


</template>




<style>


</style>