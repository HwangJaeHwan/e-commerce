<script setup lang="ts">
import CartItem from "@/components/CartItem.vue"
import {container} from "tsyringe";
import UserRepository from "@/repository/UserRepository";
import ShoppingCartItem from "@/entity/item/ShoppingCartItem";
import {onMounted} from "vue";
import {reactive} from "vue";

const USER_REPOSITORY =container.resolve(UserRepository)


type StateType = {
  itemList: ShoppingCartItem[]
}

const state = reactive<StateType>({
  itemList: []
})
function getCartItems() {

  USER_REPOSITORY.getCartItems()
      .then((itemList) => {
          state.itemList = itemList
        console.log(">>>" +state.itemList)
      })

}

function removeItem(itemUUID: string) {
  state.itemList = state.itemList.filter(item => item.itemUUID !== itemUUID)
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

  <div v-for="(item,index) in state.itemList" :key="index" @remove="removeItem">
    <CartItem :item="item"/>
  </div>


  <div class="tq" style="width: 50%">
    <div>
      <h2>20000원</h2>

      <el-button style="width: 150px" type="primary">구매하기</el-button>

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