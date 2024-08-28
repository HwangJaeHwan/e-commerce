<script setup lang="ts">
import {useRoute} from 'vue-router';
import ShoppingCartItem from '@/entity/item/ShoppingCartItem';
import {computed, onMounted, reactive} from 'vue';
import AddressModal from '@/views/AddressModal.vue';
import Address from '@/entity/address/Address';
import AddressListModal from '@/views/AddressListModal.vue';
import {useProfileStore} from "@/entity/user/Profile";
import {container} from "tsyringe";
import OrderRepository from "@/repository/OrderRepository";
import OrderRequest from "@/entity/order/OrderRequest";
import OrderItem from "@/entity/order/OrderItem";
import router from "@/router";
import HttpError from "@/http/HttpError";
import {ElMessage} from "element-plus";
import PaymentRepository from "@/repository/PaymentRepository";
import PaymentValidate from "@/entity/payment/PaymentValidate";
import type PaymentValidateItem from "@/entity/payment/PaymentValidateItem";

const route = useRoute();

const user  = useProfileStore()
const ORDER_REPOSITORY =container.resolve(OrderRepository)
const PAYMENT_REPOSITORY = container.resolve(PaymentRepository)


type StateType = {
  itemList: ShoppingCartItem[];
  showModal: boolean;
  showListModal: boolean;
  selectedAddress: Address | null;
};

const state = reactive<StateType>({
  itemList: [],
  showModal: false,
  showListModal: false,
  selectedAddress: null,
});

const totalPrice = computed(() => {
  return state.itemList.reduce((total, item) => total + item.itemPrice * item.quantity, 0);
})

async function createOrder(impUid : string){

  console.log("저둥저둥")

  console.log("info >>" , JSON.stringify(state.itemList))

  if (!state.selectedAddress) {
    console.log("여기여기")
    ElMessage({ type: 'error', message: '주소를 선택해주세요.' })
    throw new Error('주소가 선택되지 않았습니다.');
  }

  const request = new OrderRequest()
  await user.fetchProfile()
  request.userUUID = user?.profile?.userUUID
  request.impUid = impUid
  request.name = state.selectedAddress?.name
  request.address = state.selectedAddress?.address
  request.detailAddress = state.selectedAddress?.detailAddress
  request.phoneNumber = state.selectedAddress?.phoneNumber
  request.zipcode = state.selectedAddress?.zipcode

  for (const item of state.itemList) {
    request.addItem(new OrderItem(item))
  }

  console.log("리퀘스트 =",JSON.stringify(request))

  ORDER_REPOSITORY.createOrder(request)
      .then((id) =>{
        console.log("아이디다 : " + id)
        router.push({ name: "orderInfo", params: { orderId: String(id) } })
      })
      .catch((e:HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })

}

function handleAddressSelect(address: Address) {
  state.selectedAddress = address;
  state.showListModal = false;
}

function closeList() {
  state.showModal = false;
}

onMounted(() => {
  const encodedItems = route.params.items as string;
  if (encodedItems) {
    try {
      const parsedItems = JSON.parse(atob(encodedItems));
      state.itemList = Array.isArray(parsedItems) ? parsedItems : [parsedItems];
      console.log('Received items:', state.itemList);
    } catch (error) {
      console.error('Failed to parse items:', error);
    }
  }
});

function requestPay() {
  const IMP = window.IMP; // Iamport 객체
  IMP.init('imp14316717'); // Iamport 가맹점 식별코드

  const sum = state.itemList.reduce((total, cartItem) => total + cartItem.price * cartItem.quantity, 0);

  const paymentValidate =  new PaymentValidate()


  const paymentItems: PaymentValidateItem[] = state.itemList.map(cartItem => {
    const paymentItem: PaymentValidateItem = {
      itemUUID: cartItem.itemUUID,
      quantity: cartItem.quantity
    };
    return paymentItem;
  });
  paymentValidate.items.push(...paymentItems)



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
          paymentValidate.impUid = rsp.imp_uid

          PAYMENT_REPOSITORY.validate(paymentValidate)

          createOrder(rsp.imp_uid)
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
      <h3>구매자 정보</h3>
      <div class="info-row">
        <div class="info-title">아이디</div>
        <div class="info-content">{{user.profile?.loginId}}</div>
      </div>
      <div class="info-row">
        <div class="info-title">이메일</div>
        <div class="info-content">{{user.profile?.email}}</div>
      </div>
    </section>

    <section class="address-info">
      <h3>주소지 정보</h3>
      <div class="button-group">
        <button class="btn" @click="state.showModal = true">주소 입력</button>
        <button class="btn" @click="state.showListModal = true">주소 리스트 보기</button>
      </div>
      <AddressModal v-if="state.showModal" @close="state.showModal = false"/>
      <AddressListModal
          v-if="state.showListModal"
          @close="state.showListModal = false"
          @select="handleAddressSelect"
          @submit="closeList"
      />
      <div class="info-row">
        <div class="info-title">이름</div>
        <div class="info-content">{{state.selectedAddress?.name}}</div>
      </div>
      <div class="info-row">
        <div class="info-title">배송 주소</div>
        <div class="info-content">{{state.selectedAddress?.address}}, {{state.selectedAddress?.detailAddress}}</div>
      </div>
      <div class="info-row">
        <div class="info-title">연락처</div>
        <div class="info-content">{{state.selectedAddress?.phoneNumber}}</div>
      </div>
    </section>

    <section class="product-info">
      <h3>상품 정보</h3>
      <div class="product-item" v-for="(item, index) in state.itemList" :key="index">
        <div class="product-card">
          <div class="product-details">
            <h4>{{ item.name }}</h4>
            <p>수량: {{ item.quantity }}개</p>
            <p>가격: {{ item.price }}원</p>
          </div>
        </div>
      </div>
    </section>

    <section class="total-amount">
      <h3>총 결제 금액</h3>
      <h2>{{totalPrice}}원</h2>
    </section>

    <div class="payment-button">
      <button class="btn-primary" @click="requestPay">결제하기</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

header {
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.buyer-info, .address-info, .product-info, .total-amount {
  border-bottom: 1px solid #ddd;
  margin-bottom: 20px;
  padding-bottom: 10px;
}

h3 {
  margin-bottom: 30px;
  color: #333;
}

.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.info-title {
  font-weight: bold;
  width: 100px;
  color: #555;
}

.info-content {
  flex: 1;
  color: #777;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.product-item {
  margin-bottom: 15px;
}

.product-card {
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.product-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
}


.payment-button {
  text-align: center;
  margin-top: 20px;
}

.btn {
  padding: 10px 20px;
  background-color: #f4f4f4;
  color: #333;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
}

.btn:hover {
  background-color: #e9e9e9;
}

.btn-primary {
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-primary:hover {
  background-color: #45a049;
}
</style>