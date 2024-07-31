<script setup lang="ts">
import CartItem from "@/components/CartItem.vue"
import {container, inject} from "tsyringe";
import UserRepository from "@/repository/UserRepository";
import ShoppingCartItem from "@/entity/item/ShoppingCartItem";
import {onMounted} from "vue";
import {reactive} from "vue";

const USER_REPOSITORY =container.resolve(UserRepository)


type StateType = {
  itemList: ShoppingCartItem[]
}

const state = reactive<StateType>({
  itemList: []
})



function getCartItems() {

  USER_REPOSITORY.getCartItems()
      .then((itemList) => {
          state.itemList = itemList
        console.log(">>>" +state.itemList)
      })

}

function removeItem(itemUUID: string) {
  state.itemList = state.itemList.filter(item => item.itemUUID !== itemUUID)
}

function requestPay() {
  const IMP = window.IMP; // Iamport 객체
  IMP.init('imp14316717'); // Iamport 가맹점 식별코드

  const sum = state.itemList.reduce((total, cartItem) => total + cartItem.price, 0);

  IMP.request_pay({
    pg: 'html5_inicis', // 결제 모듈 종류
    pay_method: 'card', // 결제 수단
    merchant_uid: `mid_${new Date().getTime()}`, // 주문번호
    name: '결제 테스트', // 결제창에서 보여질 상품명
    amount: sum, // 금액
  }, (rsp) => { // callback
    if (rsp.success) {
      // 결제 성공 시 로직
      console.log('결제 성공', rsp);
      alert('결제가 성공적으로 완료되었습니다.');
    } else {
      // 결제 실패 시 로직
      console.log('결제 실패', rsp);
      alert('결제에 실패하였습니다.');
    }
  });
}

onMounted(()=>{
  getCartItems()
})

</script>



<template>


  <div class="d-flex align-items-center justify-content-center mb-5">
    <h1>장바구니</h1>
  </div>
<div class="zz">

  <div v-for="(item,index) in state.itemList" :key="index" @remove="removeItem">
    <CartItem :item="item"/>
  </div>


  <div class="tq" style="width: 50%">
    <div>
      <h2>20000원</h2>

      <el-button style="width: 150px" type="primary" @click="requestPay">구매하기</el-button>

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