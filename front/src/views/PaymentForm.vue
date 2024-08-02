<script setup lang="ts">

import {useRoute} from "vue-router";
import ShoppingCartItem from "@/entity/item/ShoppingCartItem";
import {onMounted, reactive} from "vue";

const route = useRoute();

type StateType = {
  itemList: ShoppingCartItem[]
}

const state = reactive<StateType>({
  itemList: []
})

onMounted(() => {
  console.log("시;발")
  console.log(route.params.items as string)
  console.log("ㅋㅋ")
  const encodedItems = route.params.items as string;
  if (route.params.items) {
    state.itemList = JSON.parse(atob(encodedItems)) as ShoppingCartItem[]
    console.log("Received items:", state.itemList);
  }
});

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



</script>

<template>

  <div class="bottom-line-2px">
    <h1>주문/결제</h1>
  </div>

  <div class="bottom-line-1px">
    <h3>구매자정보</h3>
  </div>

    <div class="payment-flex">
      <div class="payment-title">
        아이디
      </div>
      <div class="payment-info">
        zxcv0069
      </div>
    </div>

    <div class="payment-flex">
      <div class="payment-title">
        이메일
      </div>
      <div class="payment-info">
        test@naver.com
      </div>
    </div>

  <div class="bottom-line-1px">
    <h3>주소지정보</h3>
  </div>

  <div class="payment-flex">
    <div class="payment-title">
      이름
    </div>
    <div class="payment-info">
      백민석
    </div>
  </div>

  <div class="payment-flex">
    <div class="payment-title">
      배송주소
    </div>
    <div class="payment-info">
      사랑시 고백구 행복동
    </div>
  </div>

  <div class="payment-flex">
    <div class="payment-title">
      연락처
    </div>
    <div class="payment-info">
      010-1234-5678
    </div>
  </div>

  <div>
    <h3>상품정보</h3>
  </div>
  <div class="payment-items">
    <div class="payment-item">
      <p>성주 꿀 참외 산지직송 당도선별 가정용, 10kg(중과)</p>
    </div>
    <div class="payment-item">
      <p>성주 꿀 참외 산지직송 당도선별 가정용, 10kg(중과)</p>
    </div>
  </div>


  <div class="bottom-line-1px">
    <h3>총결제금액</h3>
    <h2>30000 원</h2>
  </div>

  <div>
    <el-button type="primary" @click="requestPay">결제하기</el-button>
  </div>



</template>

<style>
.bottom-line-2px{
  border-bottom: 2px solid black;
}
.bottom-line-1px {
  border-bottom: 1px solid #60ff83;
}

.payment-flex {
  display: flex;
  justify-content: left;
  border-bottom: 1px solid blue;
}
.payment-title {
  display: flex;
  justify-content: right;
  width: 100px;
  height: 30px;
  align-items: center;
  padding-right: 5px;
  margin-right: 5px;
  border-right: 1px solid #797979;
}
.payment-info {
  display: flex;
  align-items: center;
  width: 300px;
  height: 30px;
}
.payment-items {
  border-top:1px solid black;
  border-right:1px solid black;
  border-left:1px solid black;
}
.payment-item {
  border-bottom: 1px solid black;
}


</style>