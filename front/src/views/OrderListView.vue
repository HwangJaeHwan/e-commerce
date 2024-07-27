<script setup lang="ts">


import {onMounted, reactive} from "vue";
import type Order from "@/entity/order/Order";
import {container} from "tsyringe";
import OrderRepository from "@/repository/OrderRepository";
import HttpError from "@/http/HttpError";
import {ElMessage} from "element-plus";

const ORDER_REPOSITORY =container.resolve(OrderRepository)


type StateType = {
  orderList: Order[]
}

const state = reactive<StateType>({
  orderList: []
})
function getOrders() {
  ORDER_REPOSITORY.getOrders()
      .then((orders)=>{
        console.log(">>>order = {}",orders)
        state.orderList = orders
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })


}



onMounted(()=>{
  getOrders()
})

</script>



<template>


  <div class="d-flex align-items-center justify-content-center mb-5">
    <h1>주문목록</h1>
  </div>
  <div class="zz">

    <div v-for="(item,index) in state.orderList" :key="index" >
      <CartItem :item="item"/>
    </div>


    <div class="tq" style="width: 50%">
      <div>
        <h2>20000원</h2>

        <el-button style="width: 150px" type="primary">구매하기</el-button>

      </div>

    </div>
  </div>



</template>



<style>
.zz{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.tq{
  display: flex;
  justify-content: right;
}


</style>