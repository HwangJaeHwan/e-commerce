<script setup lang="ts">
</script>



<template>

  <router-link to="to">
  <div class="item-list">

    <div class="img-size image mb-3">
      <img src="/images/tmp2.jpg" alt="logo" class="img"/>
    </div>

    <div>
      <div style="word-break: break-all" class="mb-3">
        상품 이름
      </div>
      <div style="word-break: break-all" class="mb-3">
        상품 가격
      </div>

      <div style="word-break: break-all" class="mb-3">
        평점
      </div>


    </div>



  </div>

  </router-link>


</template>




<style>

.item-list{
  display: flex;
  flex-direction: column;
  height: 400px;
}
.img-size {
  height: 200px;
}


</style>