<script setup lang="ts">
import Review from "@/components/Review.vue";
</script>

<template>
  <div class="totalCount">리뷰 0개</div>


  <ul class="reviews">
    <li class="comment">
      <Review />
    </li>

    <li class="review">
      <Review />
    </li>

    <li class="review">
      <Review />
    </li>
  </ul>
</template>



<style scoped>
.totalCount {
  font-size: 1.4rem;
}

label {
  font-size: 0.78rem;
}

.reviews {
  margin-top: 3rem;
  list-style: none;
  padding: 0;

  .review {
    margin-bottom: 2.4rem;

    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
