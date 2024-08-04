<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {container} from "tsyringe";
import ItemRepository from "@/repository/ItemRepository";
import type HttpError from "@/http/HttpError";
import {ElMessage} from "element-plus";
import Item from "@/entity/item/Item";
import Reviews from "@/components/Reviews.vue";
import ReviewRepository from "@/repository/ReviewRepository";
import Paging from "@/entity/data/Paging";
import Review from "@/entity/review/Review";
import ImageRepository from "@/repository/ImageRepository";
import UserRepository from "@/repository/UserRepository";
import CartMessage from "@/entity/data/CartMessage";
import ShoppingCartItem from "@/entity/item/ShoppingCartItem";
import router from "@/router";

const props = defineProps<{
  itemUUID: string
}>()


type StateType = {
  item: Item,
  reviewList: Paging<Review>,
  imageUrl: string[] | null,
  quantity: number
}

const state = reactive<StateType>({
  item: new Item(),
  reviewList: new Paging<Review>(),
  imageUrl: [],
  quantity: 1
})

const ITEM_REPOSITORY = container.resolve(ItemRepository)
const IMAGE_REPOSITORY = container.resolve(ImageRepository)
const USER_REPOSITORY = container.resolve(UserRepository)
const REVIEW_REPOSITORY = container.resolve(ReviewRepository)
function getItem() {
  ITEM_REPOSITORY.get(props.itemUUID)
      .then((item) => {
        state.item = item
        console.log(JSON.stringify(item))
        getReviews()
        getImages()
      })
      .catch((e:HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })
}


function getReviews() {
  console.log("슈슉슈슉 >>>" + state.item.itemUUID)
  REVIEW_REPOSITORY.getList(state.item.itemUUID)
      .then((reviewList) => {
        console.log(">>>>",reviewList)
        state.reviewList = reviewList
        console.log(">zz>>",state.reviewList)
      })
}

function getImages() {

  IMAGE_REPOSITORY.getItemImage(state.item.itemUUID)
      .then(response =>{

        for (const imageInfo of response.imageInfos) {
          base64ToImage(imageInfo.imageData, imageInfo.mimeType);

        }
      })
      .catch((e) => {
        console.log(e)
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
  const url = URL.createObjectURL(blob)

  state.imageUrl?.push(url)

}

function addCart(){
  const request = new CartMessage(state.item.itemUUID,state.quantity)

  USER_REPOSITORY.cartMessage(request)
      .then(() => {
        ElMessage({ type: 'success', message: '장바구니에 등록했습니다.' });
      })
      .catch(()=>{
        ElMessage({ type: 'error', message: '장바구니에 등록하지못했습니다.' });
      })
}

function toPayment(){
  const param = new ShoppingCartItem()
  param.itemPrice =state.item.price
  param.name = state.item.name
  param.itemUUID = state.item.itemUUID
  param.quantity = state.quantity
  param.price = state.quantity * state.item.price

  const encodedParam = btoa(JSON.stringify(param))

  router.push({ name: "PaymentForm", params: { items: encodedParam} })
}



onMounted(() => {
  getItem()
})


</script>

<template>
  <div class="item-box">

    <div class="w-40 image test2">
      <el-carousel :interval="5000" arrow="always">
        <el-carousel-item v-for="(url, index) in state.imageUrl" :key="index">
          <img :src="url" alt="Image" class="img" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <div class="w-60 test">
      <div>
        <p style="word-break: break-all" class="mt-2">{{ state.item.name }}</p>
        <p class="mt-2">{{ state.item.price }}</p>
        <el-rate
            v-model="state.item.score"
            disabled
            show-score
            text-color="#ff9900"
            score-template="{value}"
        />

      </div>

      <div>

        <div class="mt-2 box">
          <el-form-item class="ml">
            <el-input v-model="state.quantity" type="number" style="width: 80px" ></el-input>
          </el-form-item>

          <el-button type="primary" style="width: 40%" @click="toPayment">상품 주문</el-button>
          <el-button type="primary" style="width: 40%" @click="addCart">장바구니</el-button>
        </div>

      </div>

    </div>

  </div>

  <div>
    <h2>상품평</h2>
    <p>{{state.item.itemDescription}}</p>
  </div>
  <div>
    <Reviews :paging = "state.reviewList"/>
  </div>
</template>


<style>

.item-box {
  display: flex;
  justify-content: center;
  margin-top: 50px;
  height: 600px;
}



.img {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: contain;
}



.w-40 {
  width: 40%;
}

.w-60 {
  width: 60%;
}

.image {
  position: relative;
}


.box {
  display: flex;
  justify-content: space-around;
}

.test {
  display: flex;
  flex-direction: column;
  justify-content: space-between;

}

.test2 {
}


</style>