<script setup lang="ts">
import CartItem from "@/components/CartItem.vue";
</script>


<template>


  <div class="d-flex align-items-center justify-content-center mb-5">
    <h1>장바구니</h1>
  </div>
<div class="zz">

<CartItem/>
  <CartItem/>
  <CartItem/>
  <CartItem/>
  <CartItem/>
  <CartItem/>
  <CartItem/>
  <CartItem/>
  <CartItem/>
  <CartItem/>
  <CartItem/>
  <CartItem/>

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