<script setup lang="ts">

import type Review from "@/entity/review/Review";

const props = defineProps<{
  review: Review
}>()
</script>

<template>
  <div class="comment">
    <div class="header">
      <div class="section">
        <div class="author">{{props.review.loginId}}</div>
        <div class="regDate">{{props.review.getDisplaySimpleRegDate()}}</div>
      </div>

      <div class="delete">삭제</div>
    </div>
    <img src="/images/logo.png" alt="logo" class="logo"/>
    <img src="/images/logo.png" alt="logo" class="logo"/>
    <img src="/images/logo.png" alt="logo" class="logo"/>
    <div class="content">{{ props.review.content }}</div>
  </div>
</template>

<style scoped>
.comment {
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
}

.section {
  display: flex;
  flex-direction: column;
}

.author {
  font-weight: 600;
  font-size: 1.2rem;
}

.regDate {
  margin-top: 5px;
  color: #797979;
  font-size: 0.88rem;
}

.content {
  margin-top: 0.8rem;
  margin-bottom: 0.8rem;
}

.delete {
  font-size: 0.78rem;
  color: red;
}
</style>
