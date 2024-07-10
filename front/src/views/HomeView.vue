<script setup lang="ts">

import {container} from "tsyringe";
import ItemRepository from "@/repository/ItemRepository";
import {reactive} from "vue";
import type Item from "@/entity/item/Item";
import Paging from "@/entity/data/Paging";

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
    .then((postList => {
      console.log(postList);
      state.itemList = postList;
    }))
}


</script>

<template>
  <div>
    <li v-for="item in state.itemList.items" :key="item.id">

    </li>

  </div>

</template>
