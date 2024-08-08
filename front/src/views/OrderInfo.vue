<script setup lang="ts">
import { reactive } from 'vue';

type Product = {
  name: string;
  price: number;
  quantity: number;
  image: string;
};

type Order = {
  orderNumber: string;
  orderDate: string;
  deliveryStatus: string;
  products: Product[];
  recipient: {
    name: string;
    phone: string;
    address: string;
    request: string;
  };
  payment: {
    method: string;
    totalPrice: number;
    discount: number;
    deliveryFee: number;
    finalAmount: number;
  };
};

const state = reactive<Order>({
  orderNumber: '3100056407154',
  orderDate: '2024. 7. 3',
  deliveryStatus: '배송완료 - 7/3(수) 도착',
  products: [
    {
      name: '성주 당도선별 미니참외, 1.5kg(6입~9입), 1봉',
      price: 6206,
      quantity: 1,
      image: '/images/mini_chamoe.jpg',
    },
    {
      name: '샐러드미인 사과푸딩, 1kg, 1개',
      price: 5315,
      quantity: 1,
      image: '/images/apple_pudding.jpg',
    },
    {
      name: '공곰 냉동 블루베리, 1kg, 1개',
      price: 7949,
      quantity: 1,
      image: '/images/frozen_blueberry.jpg',
    },
  ],
  recipient: {
    name: '황재환',
    phone: '010-2606-5527',
    address: '서울특별시 관악구 신림동 1433-22 다솜 401호',
    request: '문 앞',
  },
  payment: {
    method: '국민카드 / 일시불',
    totalPrice: 24470,
    discount: 5000,
    deliveryFee: 0,
    finalAmount: 19470,
  },
});
</script>

<template>
  <div class="container">
    <header>
      <h1>주문 상세</h1>
      <p>{{ state.orderDate }} 주문 · 주문번호 {{ state.orderNumber }}</p>
    </header>

    <section class="order-info">
      <h2>{{ state.deliveryStatus }}</h2>
      <div v-for="(product, index) in state.products" :key="index" class="product-item">
        <img :src="product.image" alt="product.name" class="product-image" />
        <div class="product-details">
          <h3>{{ product.name }}</h3>
          <p>{{ product.price.toLocaleString() }} 원 · {{ product.quantity }}개</p>
        </div>
        <div class="product-actions">
          <button class="btn">장바구니 담기</button>
          <button class="btn">배송조회</button>
          <button class="btn">교환, 반품 신청</button>
          <button class="btn">리뷰 작성하기</button>
        </div>
      </div>
    </section>

    <section class="recipient-info">
      <h2>받는사람 정보</h2>
      <p>받는사람: {{ state.recipient.name }}</p>
      <p>연락처: {{ state.recipient.phone }}</p>
      <p>받는주소: {{ state.recipient.address }}</p>
      <p>배송요청사항: {{ state.recipient.request }}</p>
    </section>

    <section class="payment-info">
      <h2>결제 정보</h2>
      <p>결제수단: {{ state.payment.method }}</p>
      <div class="payment-details">
        <p>총 상품가격: {{ state.payment.totalPrice.toLocaleString() }} 원</p>
        <p>할인금액: {{ state.payment.discount.toLocaleString() }} 원</p>
        <p>배송비: {{ state.payment.deliveryFee.toLocaleString() }} 원</p>
        <p>총 결제금액: {{ state.payment.finalAmount.toLocaleString() }} 원</p>
      </div>
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

header {
  border-bottom: 2px solid #4CAF50;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.order-info {
  margin-bottom: 20px;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.product-image {
  width: 100px;
  height: 100px;
  margin-right: 20px;
}

.product-details {
  flex: 1;
}

.product-actions {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.product-actions .btn {
  padding: 5px 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.product-actions .btn:hover {
  background-color: #45a049;
}

.recipient-info,
.payment-info {
  margin-bottom: 20px;
}

.payment-details p {
  margin: 5px 0;
}
</style>