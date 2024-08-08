<script setup lang="ts">
import { useRoute } from 'vue-router';
import ShoppingCartItem from '@/entity/item/ShoppingCartItem';
import { onMounted, reactive } from 'vue';
import AddressModal from '@/views/AddressModal.vue';
import Address from '@/entity/address/Address';
import AddressListModal from '@/views/AddressListModal.vue';
import AddressRequest from '@/entity/address/AddressRequest';

const route = useRoute();

const sampleAddresses = [
  new AddressRequest(),
  new AddressRequest(),
  new AddressRequest(),
  new AddressRequest(),
  // Add more sample addresses as needed
];

sampleAddresses[0].name = '홍길동';
sampleAddresses[0].address = '서울시 강남구';
sampleAddresses[0].detailAddress = '서울';
sampleAddresses[0].zipcode = '12345';
sampleAddresses[0].phoneNumber = '010-1234-5678';

sampleAddresses[1].name = '이몽룡';
sampleAddresses[1].address = '부산시 해운대구';
sampleAddresses[1].detailAddress = '부산';
sampleAddresses[1].zipcode = '54321';
sampleAddresses[1].phoneNumber = '010-8765-4321';

sampleAddresses[2].name = '이몽룡';
sampleAddresses[2].address = '부산시 해운대구';
sampleAddresses[2].detailAddress = '부산';
sampleAddresses[2].zipcode = '54321';
sampleAddresses[2].phoneNumber = '010-8765-4321';

sampleAddresses[3].name = '이몽룡';
sampleAddresses[3].address = '부산시 해운대구';
sampleAddresses[3].detailAddress = '부산';
sampleAddresses[3].zipcode = '54321';
sampleAddresses[3].phoneNumber = '010-8765-4321';

type StateType = {
  itemList: ShoppingCartItem[];
  showModal: boolean;
  showListModal: boolean;
  addresses: Address[];
  selectedAddress: Address | null;
};

const state = reactive<StateType>({
  itemList: [],
  showModal: false,
  showListModal: false,
  addresses: sampleAddresses,
  selectedAddress: null,
});

function handleAddressSelect(address: Address) {
  state.selectedAddress = address;
  state.showListModal = false;
}

function closeList() {
  state.showModal = false;
}

onMounted(() => {
  console.log('시;발');
  console.log(route.params.items as string);
  console.log('ㅋㅋ');
  const encodedItems = route.params.items as string;
  if (route.params.items) {
    state.itemList = JSON.parse(atob(encodedItems)) as ShoppingCartItem[];
    console.log('Received items:', state.itemList);
  }
});

function requestPay() {
  const IMP = window.IMP; // Iamport 객체
  IMP.init('imp14316717'); // Iamport 가맹점 식별코드

  const sum = state.itemList.reduce((total, cartItem) => total + cartItem.price, 0);

  IMP.request_pay(
      {
        pg: 'html5_inicis', // 결제 모듈 종류
        pay_method: 'card', // 결제 수단
        merchant_uid: `mid_${new Date().getTime()}`, // 주문번호
        name: '결제 테스트', // 결제창에서 보여질 상품명
        amount: sum, // 금액
      },
      (rsp) => {
        // callback
        if (rsp.success) {
          // 결제 성공 시 로직
          console.log('결제 성공', rsp);
          alert('결제가 성공적으로 완료되었습니다.');
        } else {
          // 결제 실패 시 로직
          console.log('결제 실패', rsp);
          alert('결제에 실패하였습니다.');
        }
      }
  );
}
</script>

<template>
  <div class="container">
    <header>
      <h1>주문/결제</h1>
    </header>

    <section class="buyer-info">
      <h3>구매자정보</h3>
      <div class="info-row">
        <div class="info-title">아이디</div>
        <div class="info-content">zxcv0069</div>
      </div>
      <div class="info-row">
        <div class="info-title">이메일</div>
        <div class="info-content">test@naver.com</div>
      </div>
    </section>

    <section class="address-info">
      <h3>주소지정보</h3>
      <div class="button-group">
        <button @click="state.showModal = true">주소 입력</button>
        <button @click="state.showListModal = true">주소 리스트 보기</button>
      </div>
      <AddressModal v-if="state.showModal" @close="state.showModal = false"/>
      <AddressListModal
          v-if="state.showListModal"
          :addresses="state.addresses"
          @close="state.showListModal = false"
          @select="handleAddressSelect"
          @submit="closeList"
      />
      <div class="info-row">
        <div class="info-title">이름</div>
        <div class="info-content">백민석</div>
      </div>
      <div class="info-row">
        <div class="info-title">배송주소</div>
        <div class="info-content">사랑시 고백구 행복동</div>
      </div>
      <div class="info-row">
        <div class="info-title">연락처</div>
        <div class="info-content">010-1234-5678</div>
      </div>
    </section>

    <section class="product-info">
      <h3>상품정보</h3>
      <div class="product-item">성주 꿀 참외 산지직송 당도선별 가정용, 10kg(중과)</div>
      <div class="product-item">성주 꿀 참외 산지직송 당도선별 가정용, 10kg(중과)</div>
    </section>

    <section class="total-amount">
      <h3>총결제금액</h3>
      <h2>30000 원</h2>
    </section>

    <div class="payment-button">
      <button @click="requestPay">결제하기</button>
    </div>
  </div>
</template>

<style>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

header {
  border-bottom: 2px solid black;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.buyer-info, .address-info, .product-info, .total-amount {
  border-bottom: 1px solid #60ff83;
  margin-bottom: 20px;
  padding-bottom: 10px;
}

h3 {
  margin-bottom: 10px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.info-title {
  font-weight: bold;
  width: 100px;
}

.info-content {
  flex: 1;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.product-item {
  padding: 10px 0;
}

.payment-button {
  text-align: center;
  margin-top: 20px;
}

.payment-button button {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}

.payment-button button:hover {
  background-color: #45a049;
}
</style>