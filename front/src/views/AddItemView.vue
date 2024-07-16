<script setup lang="ts">

import {reactive, ref} from "vue";
import ItemAdd from "@/entity/item/ItemAdd";
import ItemRepository from "@/repository/ItemRepository";
import {container} from "tsyringe";
import {ElMessage} from "element-plus";
import HttpError from "@/http/HttpError";
import router from "@/router";
import ImageRepository from "@/repository/ImageRepository";
import ImageRequest from "@/entity/image/ImageRequest";

const state = reactive({
  itemAdd: new ItemAdd(),
  imageFiles: [] as File[],
  imagePreviews: [] as string[],
});

const options = [
  {
    category: 'FASHION',
    label: '패션',
  },
  {
    category: 'BEAUTY',
    label: '뷰티',
  },
  {
    category: 'FOOD',
    label: '푸드',
  },
  {
    category: 'SPORTS',
    label: '스포츠',
  },
  {
    category: 'HEALTH',
    label: '건강',
  },
  {
    category: 'ETC',
    label: '기타',
  }
]

const ITEM_REPOSITORY = container.resolve(ItemRepository)
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

function uploadImages(){

  const formData = new FormData()

  const jsonData = {
    uuid: 'test-UUID',
    userUUID: 'test-userUUID',
    imageType: 'ITEM'
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
  const imagesUploaded = uploadImages();
  if (imagesUploaded) {
    ITEM_REPOSITORY.write(state.itemAdd)
        .then(() => {
          ElMessage({ type: 'success', message: '상품을 등록했습니다.' });
          router.replace('/');
        })
        .catch((e: HttpError) => {
          ElMessage({ type: 'error', message: e.getMessage() });
        });
  }
}



</script>

<template>

  <div class="d-flex align-items-center justify-content-center">
    <h1>상품 등록</h1>
  </div>

  <div class="mt-5">
    <el-input v-model=state.itemAdd.name placeholder="상품 이름을 입력해주세요."/>
  </div>

  <div class="mt-5">
    <el-input v-model=state.itemAdd.price type="number" placeholder="상품 가격을 입력해주세요."/>
  </div>

  <div class="mt-5">
    <el-input v-model=state.itemAdd.stock type="number" placeholder="상품 수량을 입력해주세요."/>
  </div>

  <div class="mt-5">
    <el-select
        v-model=state.itemAdd.category
        placeholder="카테고리를 선택해주세요."
        style="width: 100%"
    >
      <el-option
          v-for="item in options"
          :key="item.category"
          :label="item.label"
          :value="item.category"
      />
    </el-select>


  </div>

  <div class="mt-5">
    <el-input v-model=state.itemAdd.itemDescription type="textarea" rows="30" placeholder="상품에 대한 설명을 입력해주세요.">

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
    <el-button type="primary" @click="write()">상품 등록</el-button>
  </div>
</template>


<style>

</style>
