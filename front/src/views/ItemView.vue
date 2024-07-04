<script setup lang="ts">
</script>

<template>
  <div class="item-box">
    <div class="w-40 image test2">
      <img src="/images/dog.jpg" alt="logo" class="logo"/>
    </div>

    <el-row class="w-60 test">
      <el-col class="mt-2">상품명</el-col>
      <el-col class="mt-2">10000</el-col>
      <el-col class="mt-2">
        <el-button type="primary" style="width: 40%">상품 주문</el-button>
        <el-button type="primary" style="width: 40%">장바구니</el-button>
      </el-col>

    </el-row>
  </div>


</template>


<style>
.w-40{
  width: 40%;
}

.w-60{
  width: 40%;
}

.item-box {
  margin-top: 50px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 1200px;
}
.image {
  display: flex;
  justify-content: center;
  height: 50%;
}
.test{
  background-color: aqua;
}

.test2{
  background-color: chocolate;
}


</style>