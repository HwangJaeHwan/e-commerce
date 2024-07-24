<script setup lang="ts">

import {reactive, ref} from "vue";
import {container} from "tsyringe";
import {ElMessage} from "element-plus";
import HttpError from "@/http/HttpError";
import router from "@/router";
import ImageRepository from "@/repository/ImageRepository";
import ReviewRepository from "@/repository/ReviewRepository";
import WriteReview from "@/entity/review/WriteReview";
import {v4 as uuidv4} from "uuid";


const props = defineProps<{
  itemUUID: string
}>()


const state = reactive({
  writeReview: new WriteReview(),
  imageFiles: [] as File[],
  imagePreviews: [] as string[],
});


const REVIEW_REPOSITORY = container.resolve(ReviewRepository)
const IMAGE_REPOSITORY = container.resolve(ImageRepository)




function handleImageChange(event: Event) {
  const files = (event.target as HTMLInputElement).files;
  if (files) {
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      state.imageFiles.push(file);

      const reader = new FileReader();
      reader.onload = e => {
        state.imagePreviews.push((e.target as FileReader).result as string);
      };
      reader.readAsDataURL(file);
    }
  }
}

function uploadImages() {

  const formData = new FormData();

  state.writeReview.reviewUUID = uuidv4()

  const jsonData = {
    uuid: state.writeReview.reviewUUID,
    imageType: 'REVIEW'
  };


  const jsonBlob = new Blob([JSON.stringify(jsonData)], { type: 'application/json' });

  formData.append('info', jsonBlob);

  for (const file of state.imageFiles) {

    formData.append('images', file);

  }

  IMAGE_REPOSITORY.upload(formData)
      .then()
      .catch(() => {
        ElMessage({ type: 'error', message: '이미지 업로드 실패' });
        return false;
      })


  return true;
}




function write() {

  if (uploadImages()) {
    REVIEW_REPOSITORY.write(state.writeReview,props.itemUUID)
        .then(() => {
          ElMessage({ type: 'success', message: '리뷰를 등록했습니다.' });
          router.replace('/item/'+props.itemUUID);
        })
        .catch((e: HttpError) => {
          ElMessage({ type: 'error', message: e.getMessage() });
        });
  }
}



</script>

<template>

  <div class="d-flex align-items-center justify-content-center">
    <h1>리뷰 등록</h1>
  </div>

  <div class="mt-5">
    <el-input v-model=state.writeReview.title placeholder="리뷰 제목을 입력해주세요."/>
  </div>

  <div class="mt-5">
    <el-input v-model=state.writeReview.score type="number" placeholder="상품에 대한 점수를 매겨주세요."/>
  </div>

  <div class="mt-5">
    <el-input v-model=state.writeReview.content type="textarea" rows="30" placeholder="리뷰를 작성해주세요.">

    </el-input>
  </div>

  <div class="mt-5">
    <input type="file" @change="handleImageChange" accept="image/*" multiple/>
    <div v-if="state.imagePreviews.length > 0" class="mt-3">
      <div v-for="(preview, index) in state.imagePreviews" :key="index" class="image-preview">
        <img :src="preview" alt="이미지 미리보기" style="width: 100px; height: 100px;"/>
      </div>
    </div>
  </div>


  <div class="mt-2">
    <el-button type="primary" @click="write()">리뷰 등록</el-button>
  </div>
</template>


<style>

</style>
