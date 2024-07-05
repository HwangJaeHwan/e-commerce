<script setup lang="ts">
</script>

<template>
  <div class="item-box">

    <div class="w-60 image test2">
      <img  src="/images/logo.png" alt="logo" class="img"/>
    </div>

    <div class="w-40 test">
      <p class="mt-2">상품명</p>
      <p class="mt-2">10000</p>
      <div class="mt-2 box">

        <input type="number" style="width: 10%">

        <button style="width: 40%">상품 주문</button>
        <button style="width: 40%">장바구니</button>
      </div>

    </div>

  </div>
</template>


<style>

.item-box {
  display: flex;
  margin-top: 50px;
  height: 600px;
}

.img {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: contain;
}

@media screen and (max-width : 1000px) {
.img {
  height: auto;
}
}


.w-40 {
  width: 40%;
}

.w-60 {
  width: 60%;
}

.image {
  position: relative;
  height: 100%;
}

.box {
  display: flex;
  justify-content: space-between;
}

.test {
  background-color: aqua;
}

.test2 {
  background-color: chocolate;
}


</style>