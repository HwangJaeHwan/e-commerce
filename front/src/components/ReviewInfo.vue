<script setup lang="ts">
import type Review from "@/entity/review/Review";

const props = defineProps<{
  review: Review,
  imageMap: Map<string, string[]>
}>()
</script>

<template>
  <div class="comment">
    <div class="header">
      <div class="author-section">
        <div class="author">{{ props.review.loginId }}</div>
        <div class="details">
          <ElRate :model-value="props.review.score" disabled />
          <div class="regDate">{{ props.review.getDisplaySimpleRegDate() }}</div>
        </div>
      </div>
      <div class="delete">삭제</div>
    </div>
    <div class="images">
      <div v-for="(url, index) in props.imageMap.get(props.review.reviewUUID)" :key="index" class="image-container">
        <img :src="url" alt="review image" class="image">
      </div>
    </div>
    <div class="content">{{ props.review.content }}</div>
  </div>
</template>

<style scoped>
.comment {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 16px;
  margin-bottom: 16px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start; /* align-items를 flex-start로 설정 */
  margin-bottom: 12px;
}

.author-section {
  display: flex;
  flex-direction: column;
}

.author {
  font-weight: 600;
  font-size: 1.2rem;
  text-align: left;
}

.details {
  display: flex;
  align-items: center;
  margin-top: 5px; /* 상단 여백 추가 */
}

.details .el-rate {
  margin-right: 10px; /* 점수와 날짜 사이의 여백 */
}

.regDate {
  color: #797979;
  font-size: 0.88rem;
}

.delete {
  font-size: 0.88rem;
  color: red;
  cursor: pointer;
}

.images {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.image-container {
  width: 100px;
  height: 100px;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.content {
  font-size: 1rem;
  color: #333;
  margin-top: 12px; /* 텍스트 부분에 마진 추가 */
  text-align: left; /* 텍스트를 왼쪽 정렬 */
}
</style>