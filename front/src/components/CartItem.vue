<script setup lang="ts">

import {
  Delete,
} from '@element-plus/icons-vue'
import {container} from "tsyringe";
import UserRepository from "@/repository/UserRepository";
import Item from "@/entity/item/Item";
import ShoppingCartItem from "@/entity/item/ShoppingCartItem";
import CartMessage from "@/entity/data/CartMessage";
import {reactive} from "vue";
import router from "@/router";
import type {ImageProps} from "element-plus";

const USER_REPOSITORY = container.resolve(UserRepository)

const props = defineProps<{
  url: string
 item: ShoppingCartItem
}>()


const state = reactive({
  item: {...props.item}
})
const emits = defineEmits(['remove', 'update']);

function changeItemQuantity() {

  const message = new CartMessage(state.item.itemUUID,state.item.quantity)
  USER_REPOSITORY.cartMessage(message)
  emits('update', state.item)
}

function deleteItem(){
  const message = new CartMessage(state.item.itemUUID,0)
  USER_REPOSITORY.cartMessage(message)
  emits('remove', props.item.itemUUID)
}

</script>

<template>

  <div class="background" style="width: 700px">
    <div class="el-image m-2" style="width: 30%">
      <el-image style=" height: 100px" :src=props.url fit = contain />
<!--      <img :src=props.url alt="logo" class="image"/>-->
    </div>


    <div class="zzz" style="width: 100%">

      <div class="mt-2 sss">
        <div style="word-break: break-all">
          {{state.item.name}}
<!--          testasdzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz-->
        </div>
        <div>
          <el-button type="danger" :icon="Delete" circle @click="deleteItem"/>
        </div>
      </div>

      <div class="mb-3">
          <h3>{{ state.item.itemPrice }}</h3>
<!--        <h3>3000</h3>-->
          <el-input-number v-model="state.item.quantity" size="small" :min="1" :max="100" @change="changeItemQuantity"/>

      </div>

    </div>

  </div>

</template>

<style>

.zzz {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.sss{
  display: flex;
  justify-content: space-between;
}

.background{
  border: #797979 solid 1px;
  display: flex;
  margin-bottom: -1px;
}




</style>