<script setup lang="ts">

import {container} from "tsyringe";
import ItemRepository from "@/repository/ItemRepository";
import {reactive} from "vue";
import type Item from "@/entity/item/Item";
import Paging from "@/entity/data/Paging";
import ItemList from "@/components/ItemList.vue";

const ITEM_REPOSITORY = container.resolve(ItemRepository)

type StateType = {
  itemList: Paging<Item>
}

const state = reactive({
    itemList: new Paging<Item>()
}
)

function getList() {
ITEM_REPOSITORY.getList()
    .then((itemList => {
      console.log(itemList);
      state.itemList = itemList;
    }))
}


</script>

<template>
  <div class="tmp" style="height: 100% width= 100%">
    <item-list/>
    <item-list/>
    <item-list/>
    <item-list/>

    <item-list/>
    <item-list/>
    <item-list/>
    <item-list/>


  </div>

</template>

<style>

.tmp {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;

}

</style>
