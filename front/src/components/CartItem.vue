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

const USER_REPOSITORY = container.resolve(UserRepository)

const props = defineProps<{
 item: ShoppingCartItem
}>()


const state = reactive({
  item: {...props.item}
})

function changeItemQuantity() {

  const message = new CartMessage(state.item.itemUUID,state.item.quantity)
  USER_REPOSITORY.cartMessage(message)


}

function deleteItem(){
  const message = new CartMessage(state.item.itemUUID,0)
  USER_REPOSITORY.cartMessage(message)
}

</script>

<template>

  <div class="background" style="width: 50%">
    <div class="el-image m-2" style="width: 30%">
      <img src="/images/logo.png" alt="logo" class="img"/>
    </div>


    <div class="zzz" style="width: 70%">

      <div class="mt-2 sss">
        <div style="word-break: break-all">
          {{state.item.name}}
        </div>
        <div>
          <el-button type="danger" :icon="Delete" circle @click="deleteItem"/>
        </div>
      </div>

      <div class="mb-3">
          <h3>{{ state.item.itemPrice }}</h3>
          <el-input-number v-model="state.item.quantity" :min="1" :max="10" @change="changeItemQuantity"/>

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