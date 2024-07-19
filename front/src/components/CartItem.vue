<script setup lang="ts">

import {
  Delete,
} from '@element-plus/icons-vue'

</script>

<template>

  <div class="background" style="width: 50%">
    <div class="el-image m-2" style="width: 30%">
      <img src="/images/logo.png" alt="logo" class="img"/>
    </div>


    <div class="zzz" style="width: 70%">

      <div class="mt-2 sss">
        <div style="word-break: break-all">
          상품 이름
        </div>
        <div>
          <el-button type="danger" :icon="Delete" circle />
        </div>
      </div>

      <div class="mb-3">
          <h3>상품 가격</h3>
          <el-input-number  :min="1" :max="10" />

      </div>

    </div>

  </div>

</template>

<style>

.zzz {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.sss{
  display: flex;
  justify-content: space-between;
}

.background{
  border: #797979 solid 1px;
  display: flex;
  margin-bottom: -1px;
}



</style>