<script setup lang="ts">
import {ref} from "vue";

import axios from 'axios';

const name = ref("")
const itemDescription = ref("")
const price = ref("")
const stock = ref("")
const category = ref("")


const write = function () {
  console.log(name.value, itemDescription.value, price.value, stock.value, category.value)
}

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


</script>

<template>

  <div class="d-flex align-items-center justify-content-center">
    <h1>상품 등록</h1>
  </div>

  <div class="mt-5">
    <el-input v-model="name" placeholder="상품 이름을 입력해주세요."/>
  </div>

  <div class="mt-5">
    <el-input v-model="price" type="number" placeholder="상품 가격을 입력해주세요."/>
  </div>

  <div class="mt-5">
    <el-input v-model="stock" type="number" placeholder="상품 수량을 입력해주세요."/>
  </div>

  <div class="mt-5">
    <el-select
        v-model="category"
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
    <el-input v-model="itemDescription" type="textarea" rows="30" placeholder="상품에 대한 설명을 입력해주세요.">

    </el-input>
  </div>
  <div class="mt-2">
    <el-button type="primary" @click="write()">상품 등록</el-button>
  </div>
</template>


<style>

</style>
