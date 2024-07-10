<script setup lang="ts">

import {reactive} from "vue";
import ItemAdd from "@/entity/item/ItemAdd";
import ItemRepository from "@/repository/ItemRepository";
import {container} from "tsyringe";
import {ElMessage} from "element-plus";
import HttpError from "@/http/HttpError";
import router from "@/router";

const state = reactive({
    itemAdd: new ItemAdd(),
})


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

function write() {
  ITEM_REPOSITORY.write(state.itemAdd)
      .then(() => {
        ElMessage({type: 'success', message: '상품을 등록했습니다.'})
        router.replace('/')
      })
      .catch((e:HttpError)=>{
        ElMessage({ type: 'error', message: e.getMessage() })
      })

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
  <div class="mt-2">
    <el-button type="primary" @click="write()">상품 등록</el-button>
  </div>
</template>


<style>

</style>
