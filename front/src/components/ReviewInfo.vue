<script setup lang="ts">
import { ref, computed } from 'vue';
import type ReviewList from "@/entity/review/ReviewList";
import { ElDialog } from "element-plus";
import { useProfileStore } from "@/entity/user/Profile";

const props = defineProps<{
  review: ReviewList,
  imageMap: Map<string, string[]>
}>()

const profileStore = useProfileStore();
const dialogVisible = ref(false);
const selectedImageUrl = ref('');

const canDelete = computed(() => {
  return props.review.userUUID === profileStore.profile?.userUUID;
});

function openImageDialog(url: string) {
  selectedImageUrl.value = url;
  dialogVisible.value = true;
}
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
      <div class="delete" v-if="canDelete">삭제</div>
    </div>
    <div class="images">
      <div v-for="(url, index) in props.imageMap.get(props.review.reviewUUID)" :key="index" class="image-container">
        <img :src="url" alt="review image" class="image" @click="openImageDialog(url)">
      </div>
    </div>
    <div class="content">{{ props.review.content }}</div>
  </div>

  <ElDialog v-model="dialogVisible" width="50%">
    <img :src="selectedImageUrl" alt="Expanded image" class="expanded-image" v-if="dialogVisible" />
  </ElDialog>
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
  align-items: flex-start;
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
  margin-top: 5px;
}

.details .el-rate {
  margin-right: 10px;
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
  flex-wrap: wrap;
  margin-bottom: 20px;
  justify-content: flex-start; /* 이미지들을 왼쪽 정렬 */
}

.image-container {
  margin: 5px;
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
  cursor: pointer;
}

.content {
  font-size: 1rem;
  color: #333;
  margin-top: 12px;
  text-align: left;
}

.expanded-image {
  max-width: 80%;  /* 다이얼로그 크기의 80%를 최대 크기로 설정 */
  max-height: 80vh; /* 화면 높이의 80%를 최대 높이로 설정 */
  border-radius: 8px;
  object-fit: contain; /* 이미지가 잘리지 않도록 설정 */
  margin: 0 auto;
  display: block;
}
</style>