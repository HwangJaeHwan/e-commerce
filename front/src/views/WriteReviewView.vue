<script setup lang="ts">
import { reactive } from "vue";
import { container } from "tsyringe";
import { ElMessage } from "element-plus";
import HttpError from "@/http/HttpError";
import router from "@/router";
import ImageRepository from "@/repository/ImageRepository";
import ReviewRepository from "@/repository/ReviewRepository";
import WriteReview from "@/entity/review/WriteReview";
import { v4 as uuidv4 } from "uuid";

const props = defineProps<{
  itemUUID: string
}>();

const state = reactive({
  writeReview: new WriteReview(),
  imageFiles: [] as File[],
  imagePreviews: [] as string[],
});

// 기본값 설정
state.writeReview.score = 1;

const REVIEW_REPOSITORY = container.resolve(ReviewRepository);
const IMAGE_REPOSITORY = container.resolve(ImageRepository);

function handleImageChange(event: Event) {
  const files = (event.target as HTMLInputElement).files;
  if (files) {
    state.imageFiles = Array.from(files);

    state.imagePreviews = [];
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const reader = new FileReader();
      reader.onload = e => {
        state.imagePreviews.push((e.target as FileReader).result as string);
      };
      reader.readAsDataURL(file);
    }
  }
}

function handleRemove(index: number) {
  state.imageFiles.splice(index, 1);
  state.imagePreviews.splice(index, 1);
}

function uploadImages() {
  const formData = new FormData();
  state.writeReview.reviewUUID = uuidv4();

  const jsonData = {
    uuid: state.writeReview.reviewUUID,
    imageType: 'REVIEW',
  };

  const jsonBlob = new Blob([JSON.stringify(jsonData)], { type: 'application/json' });
  formData.append('info', jsonBlob);

  for (const file of state.imageFiles) {
    formData.append('images', file);
  }

  return IMAGE_REPOSITORY.upload(formData)
      .then(() => true)
      .catch(() => {
        ElMessage({ type: 'error', message: '이미지 업로드 실패' });
        return false;
      });
}

async function write() {
  const imagesUploaded = await uploadImages();
  if (imagesUploaded) {
    REVIEW_REPOSITORY.write(state.writeReview, props.itemUUID)
        .then(() => { ElMessage({ type: 'success', message: '리뷰를 등록했습니다.' });
          router.replace('/item/' + props.itemUUID);
        })
        .catch((e: HttpError) => {
          ElMessage({ type: 'error', message: e.getMessage() });
        });
  }
}
</script>

<template>
  <div class="container">
    <h1 class="title">리뷰 등록</h1>

    <div class="form-group">
      <el-input
          v-model="state.writeReview.content"
          rows="30"
          type="textarea"
          class="review-content"
          placeholder="리뷰를 작성해주세요."
      />
    </div>

    <div class="form-group d-flex align-items-center">
      <span class="mr-3">별점 등록:</span>
      <el-rate v-model="state.writeReview.score" />
    </div>

    <div class="form-group image-upload">
      <h3>이미지 등록</h3>
      <input type="file" @change="handleImageChange" accept="image/*" multiple class="upload-input"/>
      <div v-if="state.imagePreviews.length > 0" class="image-preview-container">
        <div v-for="(preview, index) in state.imagePreviews" :key="index" class="image-preview">
          <img :src="preview" alt="이미지 미리보기"/>
          <button @click="handleRemove(index)" class="remove-button">X</button>
        </div>
      </div>
    </div>

    <div class="form-group">
      <el-button type="primary" @click="write()">리뷰 등록</el-button>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}
.title {
  text-align: center;
}
.form-group {
  margin-top: 20px;
}
.mr-3 {
  margin-right: 10px;
}
.review-content {
  height: 700px !important;
  overflow-y: auto;
  resize: none; /* 사용자가 크기 변경 불가능하도록 설정 */
}
.image-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.upload-input {
  margin-bottom: 10px;
}
.image-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}
.image-preview {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.remove-button {
  position: absolute;
  top: 5px;
  right: 5px;
  background: rgba(255, 255, 255, 0.7);
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}
</style>