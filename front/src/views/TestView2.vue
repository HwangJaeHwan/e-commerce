<script setup lang="ts">
import { reactive, onMounted } from "vue";
import { container } from "tsyringe";
import { ElMessage } from "element-plus";
import HttpError from "@/http/HttpError";
import router from "@/router";
import ImageRepository from "@/repository/ImageRepository";
import ReviewRepository from "@/repository/ReviewRepository";
import ReviewUpdate from "@/entity/review/ReviewUpdate";
import ImageUrl from "@/entity/image/ImageUrl";
import type ImageInfo from "@/entity/image/ImageInfo";

const props = defineProps<{ reviewUUID: string }>();

type StateType = {
  reviewUpdate: ReviewUpdate,
  imageUrl: ImageUrl[],
  imagePreviews: string[],
  imageFiles: File[],
  deleteIds: number[], // 삭제할 이미지 ID를 추적하는 배열
};

const state = reactive<StateType>({
  reviewUpdate: new ReviewUpdate(),
  imageUrl: [],
  imagePreviews: [],
  imageFiles: [],
  deleteIds: [], // 초기화
});

const REVIEW_REPOSITORY = container.resolve(ReviewRepository);
const IMAGE_REPOSITORY = container.resolve(ImageRepository);

onMounted(() => {
  getReview();
});

function getReview(){
  REVIEW_REPOSITORY.get(props.reviewUUID)
      .then((review) => {
        state.reviewUpdate.content = review.content;
        state.reviewUpdate.score = review.score;
        getImages()
      })
      .catch((e: HttpError) => {
        ElMessage({ type: "error", message: e.getMessage() });
      });
}

function getImages() {
  IMAGE_REPOSITORY.getReviewImage(props.reviewUUID)
      .then((response) => {
        for (const imageInfo of response.imageInfos) {
          base64ToImage(imageInfo);
        }
      })
      .catch((e: HttpError) => {
        ElMessage({ type: "error", message: e.getMessage() });
      });
}

function base64ToImage(imageInfo: ImageInfo) {
  const byteCharacters = atob(imageInfo.imageData);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);
  const blob = new Blob([byteArray], { type: imageInfo.mimeType });
  const url = URL.createObjectURL(blob);
  const imageUrl = new ImageUrl();

  imageUrl.id = imageInfo.id;
  imageUrl.url = url;

  state.imageUrl.push(imageUrl);
}

function handleImageChange(event: Event) {
  const files = (event.target as HTMLInputElement).files;
  if (files) {
    state.imageFiles = Array.from(files);
    state.imagePreviews = [];
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const reader = new FileReader();
      reader.onload = (e) => {
        state.imagePreviews.push((e.target as FileReader).result as string);
      };
      reader.readAsDataURL(file);
    }
  }
}

function handleRemoveExistingImage(index: number) {
  const removedImage = state.imageUrl.splice(index, 1)[0];
  if (removedImage) {
    state.deleteIds.push(removedImage.id); // 삭제할 이미지의 ID를 추가합니다.
  }
}

function handleRemoveNewImage(index: number) {
  state.imageFiles.splice(index, 1);
  state.imagePreviews.splice(index, 1);
}

function updateReview() {
  if (state.imageFiles.length > 0 || state.deleteIds.length > 0) {
    uploadImages();
  }

  REVIEW_REPOSITORY.update(state.reviewUpdate, props.reviewUUID)
      .then(() => {
        ElMessage({ type: "success", message: "리뷰가 수정되었습니다." });
        router.replace("/orders");
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() });
      });
}

function uploadImages() {
  const formData = new FormData();
  const jsonData = {
    ids: state.deleteIds,
    imageType: "REVIEW",
  };

  const jsonBlob = new Blob([JSON.stringify(jsonData)], { type: "application/json" });
  formData.append("info", jsonBlob);

  for (const file of state.imageFiles) {
    formData.append("images", file);
  }

  return IMAGE_REPOSITORY.update(formData,props.reviewUUID)
      .then(() => true)
      .catch(() => {
        ElMessage({ type: "error", message: "이미지 업로드 실패" });
        return false;
      });
}
</script>

<template>
  <div class="container">
    <h1 class="title">리뷰 수정</h1>


    <div class="form-group">
      <el-input
          v-model="state.reviewUpdate.content"
          rows="30"
          type="textarea"
          class="review-content"
          placeholder="리뷰를 작성해주세요."
      />
    </div>

    <div class="form-group d-flex align-items-center">
      <span class="mr-3">별점 수정:</span>
      <el-rate v-model="state.reviewUpdate.score" />
    </div>

    <div class="form-group image-upload">
      <h3>기존 이미지</h3>
      <div v-if="state.imageUrl.length > 0" class="image-preview-container">
        <div v-for="(image, index) in state.imageUrl" :key="index" class="image-preview">
          <img :src="image.url" alt="기존 이미지 미리보기" />
          <button @click="handleRemoveExistingImage(index)" class="remove-button">X</button>
        </div>
      </div>

      <h3>새 이미지 등록</h3>
      <input type="file" @change="handleImageChange" accept="image/*" multiple class="upload-input" />
      <div v-if="state.imagePreviews.length > 0" class="image-preview-container">
        <div v-for="(preview, index) in state.imagePreviews" :key="index" class="image-preview">
          <img :src="preview" alt="이미지 미리보기" />
          <button @click="handleRemoveNewImage(index)" class="remove-button">X</button>
        </div>
      </div>
    </div>

    <div class="form-group">
      <el-button type="primary" @click="updateReview">리뷰 수정</el-button>
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
  resize: none;
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