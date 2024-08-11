<script setup lang="ts">
import CartItem from "@/components/CartItem.vue"
import {container, inject} from "tsyringe";
import UserRepository from "@/repository/UserRepository";
import ShoppingCartItem from "@/entity/item/ShoppingCartItem";
import {computed, onMounted} from "vue";
import {reactive} from "vue";
import router from "@/router";
import ImageRepository from "@/repository/ImageRepository";
import ImageListRequest from "@/entity/image/ImageListRequest";
import ImageResponse from "@/entity/image/ImageResponse";
import HttpError from "@/http/HttpError";
import {ElMessage} from "element-plus";

const USER_REPOSITORY =container.resolve(UserRepository)
const IMAGE_REPOSITORY =container.resolve(ImageRepository)


type StateType = {
  itemList: ShoppingCartItem[],
  imageMap: Map<string,string[]>
}

const state = reactive<StateType>({
  itemList: [],
  imageMap: new Map<string,string[]>
})



function getCartItems() {

  USER_REPOSITORY.getCartItems()
      .then((itemList) => {
          state.itemList = itemList
        for (const itemListElement of itemList) {
          console.log(JSON.stringify(itemListElement))
        }
        getImages()
      })

}

const totalPrice = computed(() => {
  return state.itemList.reduce((total, cartItem) => total + cartItem.itemPrice * cartItem.quantity, 0);
});

function getImages() {
  const request = new ImageListRequest()

  request.imageType = 'ITEM'
  request.uuids = state.itemList.map(item => item.itemUUID)
  console.log(JSON.stringify(state.itemList))

  console.log("request = " + JSON.stringify(request))

  IMAGE_REPOSITORY.getImages(request)
      .then((imageList: ImageResponse[]) => {

        for (const image of imageList) {

          for (const imageInfo of image.imageInfos) {

            const url = base64ToImage(imageInfo.imageData, imageInfo.mimeType);
            if (!state.imageMap.has(image.uuid)) {

              state.imageMap.set(image.uuid, []);
            }

            state.imageMap.get(image.uuid)?.push(url);

          }


        }
        console.log("ㅡㅡ =" + JSON.stringify(state.imageMap.get(imageList[0].uuid)))
        console.log("zz =" + imageList[0].uuid)
      })
      .catch((e: HttpError) => {
        ElMessage({type: 'error', message: e.getMessage()})
      })
}


  function base64ToImage(base64String, mimeType) {

    const byteCharacters = atob(base64String);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: mimeType });

    return URL.createObjectURL(blob)


  }

function removeItem(itemUUID: string) {
  console.log("list = ", state.itemList)
  state.itemList = state.itemList.filter(item => item.itemUUID !== itemUUID)
}

function updateItem(updatedItem: ShoppingCartItem) {
  const index = state.itemList.findIndex(item => item.itemUUID === updatedItem.itemUUID);
  console.log("인덱스 = ",index)
  console.log("item = ",updatedItem)
  if (index !== -1) {
    state.itemList[index] = updatedItem;
  }
}

function toPayment() {
  const encodedParam = btoa(JSON.stringify(state.itemList))
  router.push({ name: "PaymentForm", params: { items: encodedParam } });
}

function refreshPage() {
  router.go(0)  // 현재 페이지 새로고침
}


onMounted(()=>{
  getCartItems()
})

</script>



<template>


  <div class="d-flex align-items-center justify-content-center mb-5">
    <h1>장바구니</h1>
  </div>
<div class="zz">

  <div v-for="(item,index) in state.itemList" :key="index">
    <CartItem :item="item" :url ="state.imageMap.get(item.itemUUID)?.[0] || '/images/dog.jpg'" @remove="removeItem" @update="updateItem"/>
  </div>


  <div class="tq" style="width: 700px">
    <div>
      <h2>{{totalPrice}}</h2>

      <el-button style="width: 150px" type="primary" @click="toPayment">구매하기</el-button>

    </div>

  </div>
</div>



</template>



<style>
.zz{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.tq{
  display: flex;
  justify-content: right;
}


</style>