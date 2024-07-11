<script setup lang="ts">


import ReviewInfo from "@/components/ReviewInfo.vue";
import type Review from "@/entity/review/Review";
import type Paging from "@/entity/data/Paging";

const props = defineProps<{
  paging : Paging<Review>
}>()

</script>

<template>
  <div class="totalCount">리뷰: {{paging.totalElement}}</div>


  <ul class="reviews">
    <li v-for="review in paging.items" :key="review.reviewUUID"  class="comment">
      <ReviewInfo :review="review"/>
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
