<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {container} from "tsyringe";
import ItemRepository from "@/repository/ItemRepository";
import type HttpError from "@/http/HttpError";
import {ElMessage} from "element-plus";
import Item from "@/entity/item/Item";
import Reviews from "@/components/Reviews.vue";
const value = ref(3.7)

// const props = defineProps<{
//   itemId: number
// }>()
//
// onMounted(() => (
//     alert(props.itemId)
// ))
//
// type Statetype = {
//   item: Item
// }
//
// const state = reactive<Statetype>({
//   item: new Item()
// })
//
// const ITEM_REPOSITORY = container.resolve(ItemRepository)
//
// function getItem() {
//   ITEM_REPOSITORY.get(props.itemId)
//       .then((item: Item) => {
//         state.item = item
//       })
//       .catch((e:HttpError) => {
//         ElMessage({ type: 'error', message: e.getMessage() })
//       })
// }


</script>

<template>
  <div class="item-box">

    <div class="w-40 image test2">
      <img src="/images/logo.png" alt="logo" class="img"/>
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
    <Reviews/>
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
  height: 100%;
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