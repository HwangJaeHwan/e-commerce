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
const value = ref(3.7)

const props = defineProps<{
  itemId: number
}>()


// onMounted(() => (
//     alert(props.itemId)
// ))

type StateType = {
  item: Item,
  reviewList: Paging<Review>,
  imageUrl: string | null
}

const state = reactive<StateType>({
  item: new Item(),
  reviewList: new Paging<Review>(),
  imageUrl: null
})

const ITEM_REPOSITORY = container.resolve(ItemRepository)
const IMAGE_REPOSITORY = container.resolve(ImageRepository)

function getItem() {
  ITEM_REPOSITORY.get(props.itemId)
      .then((item: Item) => {
        state.item = item
      })
      .catch((e:HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })
}

const REVIEW_REPOSITORY = container.resolve(ReviewRepository)

function getReviews() {
  REVIEW_REPOSITORY.getList('item-uuid-1')
      .then((reviewList) => {
        console.log(">>>>",reviewList)
        state.reviewList = reviewList
        console.log(">zz>>",state.reviewList)
      })
}

async function getImage() {
  console.log("씨발")
  IMAGE_REPOSITORY.get("9d43e3bd-610d-4987-a359-22b57d3ff461.PNG")
      .then(response =>{
        console.log(typeof response)
        const blob = response.data
        state.imageUrl = URL.createObjectURL(blob)
      })
      .catch((e) => {
        console.log(e)
      })

}

onMounted(() => {
  getReviews()
  getImage()
})


</script>

<template>
  <div class="item-box">

    <div class="w-40 image test2">
      <img :src='state.imageUrl' alt="logo" class="img"/>
    </div>

    <div class="w-60 test">
      <div>
        <p style="word-break: break-all" class="mt-2">상품명asdasdasdasdasdasdasdasdasdasdasdasdasdasd</p>
        <p class="mt-2">10000</p>
        <el-rate
            v-model="value"
            disabled
            show-score
            text-color="#ff9900"
            score-template="{value} points"
        />

      </div>

      <div>

        <div class="mt-2 box">
          <el-form-item class="ml">
            <el-input type="number" style="width: 80px" value="1" >1</el-input>
          </el-form-item>

          <el-button type="primary" style="width: 40%">상품 주문</el-button>
          <el-button type="primary" style="width: 40%">장바구니</el-button>
        </div>

      </div>

    </div>

  </div>

  <div>
    <p>상품평</p>
  </div>
  <div>
    <Reviews :paging="state.reviewList"/>
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

@media screen and (max-width: 1000px) {
  .img {
    height: auto;
  }
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