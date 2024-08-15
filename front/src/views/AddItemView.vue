<script setup lang="ts">
import { reactive } from "vue";
import ItemAdd from "@/entity/item/ItemAdd";
import ItemRepository from "@/repository/ItemRepository";
import { container } from "tsyringe";
import { ElMessage } from "element-plus";
import HttpError from "@/http/HttpError";
import router from "@/router";
import ImageRepository from "@/repository/ImageRepository";
import { v4 as uuidv4 } from "uuid";

const state = reactive({
  itemAdd: new ItemAdd(),
  imageFiles: [] as File[],
  imagePreviews: [] as string[],
});

const options = [
  { category: 'FASHION', label: '패션' },
  { category: 'BEAUTY', label: '뷰티' },
  { category: 'FOOD', label: '푸드' },
  { category: 'SPORTS', label: '스포츠' },
  { category: 'HEALTH', label: '건강' },
  { category: 'ETC', label: '기타' },
];

const ITEM_REPOSITORY = container.resolve(ItemRepository);
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
  state.itemAdd.itemUUID = uuidv4();

  const jsonData = {
    uuid: state.itemAdd.itemUUID,
    imageType: 'ITEM',
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
    ITEM_REPOSITORY.write(state.itemAdd)
        .then(() => {
          ElMessage({ type: 'success', message: '상품을 등록했습니다.' });
          router.replace('/item/' + state.itemAdd.itemUUID);
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
    <div class="form-group">
      <label for="itemName">상품 이름:</label>
      <el-input id="itemName" v-model="state.itemAdd.name" placeholder="상품 이름을 입력해주세요." />
    </div>
  </div>

  <div class="mt-5">
    <div class="form-group">
      <label for="itemPrice">가격:</label>
      <el-input id="itemPrice" v-model="state.itemAdd.price" placeholder="가격을 입력하세요." type="number" />
    </div>
  </div>

  <div class="mt-5">
    <div class="form-group">
      <label for="itemStock">수량:</label>
      <el-input id="itemStock" v-model="state.itemAdd.stock" placeholder="수량을 입력하세요." type="number" />
    </div>
  </div>

  <div class="mt-5">
    <div class="form-group">
      <label for="itemCategory">카테고리:</label>
      <el-select
          id="itemCategory"
          v-model="state.itemAdd.category"
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
  </div>

  <div class="mt-5">
    <div class="form-group">
      <label for="itemDescription">상품 설명:</label>
      <el-input
          id="itemDescription"
          v-model="state.itemAdd.itemDescription"
          type="textarea"
          rows="10"
          placeholder="상품에 대한 설명을 입력해주세요."
      />
    </div>
  </div>

  <div class="mt-5">
    <h3>이미지 등록</h3>
    <input type="file" @change="handleImageChange" accept="image/*" multiple />
    <div v-if="state.imagePreviews.length > 0" class="mt-3 image-preview-container">
      <div v-for="(preview, index) in state.imagePreviews" :key="index" class="image-preview">
        <img :src="preview" alt="이미지 미리보기" />
        <button @click="handleRemove(index)" class="remove-button">X</button>
      </div>
    </div>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="write()">상품 등록</el-button>
  </div>
</template>

<style scoped>
.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

label {
  width: 120px;
  margin-right: 10px;
  text-align: right;
  font-weight: 500;
}

.image-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center; /* 이미지 미리보기를 중앙에 배치 */
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