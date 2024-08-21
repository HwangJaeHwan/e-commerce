<script setup lang="ts">
import { reactive, onMounted } from "vue";
import ItemRepository from "@/repository/ItemRepository";
import { container } from "tsyringe";
import { ElMessage } from "element-plus";
import HttpError from "@/http/HttpError";
import router from "@/router";
import ImageRepository from "@/repository/ImageRepository";
import { useRoute } from "vue-router";
import ItemUpdate from "@/entity/item/ItemUpdate";
import ImageUrl from "@/entity/image/ImageUrl";
import type ImageInfo from "@/entity/image/ImageInfo";

const props = defineProps<{ itemUUID: string }>();

type StateType = {
  item: ItemUpdate;
  imageUrl: ImageUrl[];
  imagePreviews: string[];
  imageFiles: File[];
  deleteIds: number[]; // 삭제할 이미지 ID를 추적하는 배열
};

const state = reactive<StateType>({
  item: new ItemUpdate(),
  imageUrl: [],
  imagePreviews: [],
  imageFiles: [],
  deleteIds: [], // 초기화
});

const route = useRoute();
const ITEM_REPOSITORY = container.resolve(ItemRepository);
const IMAGE_REPOSITORY = container.resolve(ImageRepository);

const options = [
  { category: "FASHION", label: "패션" },
  { category: "BEAUTY", label: "뷰티" },
  { category: "FOOD", label: "푸드" },
  { category: "SPORTS", label: "스포츠" },
  { category: "HEALTH", label: "건강" },
  { category: "ETC", label: "기타" },
];

onMounted(() => {
  getItem();
});

function getItem() {
  ITEM_REPOSITORY.get(props.itemUUID)
      .then((item) => {
        state.item.name = item.name
        state.item.itemDescription = item.itemDescription
        state.item.itemUUID = item.itemUUID
        state.item.stock = item.stock
        state.item.price = item.price
        state.item.category = item.category
        getImages();
      })
      .catch((e: HttpError) => {
        ElMessage({ type: "error", message: e.getMessage() });
      });
}

function getImages() {
  IMAGE_REPOSITORY.getItemImage(state.item.itemUUID)
      .then((response) => {
        for (const imageInfo of response.imageInfos) {
          base64ToImage(imageInfo);
        }
      })
      .catch((e: HttpError) => {
        ElMessage({ type: "error", message: e.getMessage() });
      });
}

function base64ToImage(imageInfo :ImageInfo) {
  const byteCharacters = atob(imageInfo.imageData);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);
  const blob = new Blob([byteArray], { type: imageInfo.mimeType });
  const url = URL.createObjectURL(blob);
  const imageUrl =  new ImageUrl();

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
    URL.revokeObjectURL(removedImage.url); // 이미지 URL 해제
    state.deleteIds.push(removedImage.id); // 삭제할 이미지 ID 추가
  }
}

function handleRemoveNewImage(index: number) {
  state.imageFiles.splice(index, 1);
  state.imagePreviews.splice(index, 1);
}
function updateItem() {


  if (state.imageFiles.length > 0 || state.deleteIds.length > 0) {
    uploadImages();
  }

  // ITEM_REPOSITORY.update(state.item)
  //     .then(()=>{
  //   ElMessage({ type: "success", message: "아이템이 수정되었습니다." });
  //   router.replace("/item/" + state.item.itemUUID);
  // })
  //     .catch((e: HttpError) => {
  //       ElMessage({ type: 'error', message: e.getMessage() });
  //     })


}

function uploadImages() {
  const formData = new FormData();
  console.log(JSON.stringify(state.deleteIds))
  const jsonData = {
    ids: state.deleteIds,
    imageType: "ITEM",
  };

  const jsonBlob = new Blob([JSON.stringify(jsonData)], { type: "application/json" });
  formData.append("info", jsonBlob);

  for (const file of state.imageFiles) {
    formData.append("images", file);
  }

  return IMAGE_REPOSITORY.update(formData,props.itemUUID)
      .then(() => true)
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
        return false
      });
}
</script>

<template>
  <div class="d-flex align-items-center justify-content-center">
    <h1>아이템 수정</h1>
  </div>

  <div class="mt-5">
    <div class="form-group">
      <label for="itemName">상품 이름:</label>
      <el-input id="itemName" v-model="state.item.name" placeholder="상품 이름을 입력해주세요." />
    </div>
  </div>

  <div class="mt-5">
    <div class="form-group">
      <label for="itemPrice">가격:</label>
      <el-input id="itemPrice" v-model="state.item.price" placeholder="가격을 입력하세요." type="number" />
    </div>
  </div>

  <div class="mt-5">
    <div class="form-group">
      <label for="itemStock">수량:</label>
      <el-input id="itemStock" v-model="state.item.stock" placeholder="수량을 입력하세요." type="number" />
    </div>
  </div>

  <div class="mt-5">
    <div class="form-group">
      <label for="itemCategory">카테고리:</label>
      <el-select
          id="itemCategory"
          v-model="state.item.category"
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
          v-model="state.item.itemDescription"
          type="textarea"
          rows="10"
          placeholder="상품에 대한 설명을 입력해주세요."
      />
    </div>
  </div>

  <div class="mt-5">
    <h3>기존 이미지</h3>
    <div v-if="state.imageUrl.length > 0" class="mt-3 image-preview-container">
      <div v-for="(image, index) in state.imageUrl" :key="index" class="image-preview">
        <img :src="image.url" alt="기존 이미지 미리보기" />
        <button @click="handleRemoveExistingImage(index)" class="remove-button">X</button>
      </div>
    </div>
  </div>

  <div class="mt-5">
    <h3>새 이미지 등록</h3>
    <input type="file" @change="handleImageChange" accept="image/*" multiple />
    <div v-if="state.imagePreviews.length > 0" class="mt-3 image-preview-container">
      <div v-for="(preview, index) in state.imagePreviews" :key="index" class="image-preview">
        <img :src="preview" alt="새 이미지 미리보기" />
        <button @click="handleRemoveNewImage(index)" class="remove-button">X</button>
      </div>
    </div>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="updateItem()">아이템 수정</el-button>
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
  width: 150px; /* 더 큰 미리보기 영역 */
  height: 150px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: contain; /* 이미지가 잘리지 않고 완전히 보이도록 설정 */
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