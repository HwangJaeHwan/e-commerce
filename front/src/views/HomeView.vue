<script setup lang="ts">

import {container} from "tsyringe";
import ItemRepository from "@/repository/ItemRepository";
import {onMounted, reactive} from "vue";
import type Item from "@/entity/item/Item";
import Paging from "@/entity/data/Paging";
import ItemList from "@/components/ItemList.vue";
import ImageRepository from "@/repository/ImageRepository";

const ITEM_REPOSITORY = container.resolve(ItemRepository)
const IMAGE_REPOSITORY = container.resolve(ImageRepository)

type StateType = {
  itemList: Paging<Item>,
  imageMap: Map<string,string>
}

const state = reactive({
    itemList: new Paging<Item>(),
    imageMap: new Map<string,string>
}
)

function getList() {
ITEM_REPOSITORY.getList()
    .then((itemList => {
      console.log(itemList);
      state.itemList = itemList;
    }))
}

function getImages(){
  // const itemUUIDs = state.itemList.items.map(item => item.itemUUID)
  const itemUUIDs = ['test-UUID-1','test-UUID-2']
  const jsonItemUUIDs = JSON.stringify(itemUUIDs);
  IMAGE_REPOSITORY.getImages(jsonItemUUIDs)
      .then((response) =>{
        console.log("zasdasdasd>>>" + response)
        const imageResponse = response.data
        for (let json of response.data) {
          base64ToImage(json.imageData,json.mimeType,json.itemUUID)
          // const blob = new Blob([new Uint8Array(json.imageData)], { type: json.mimeType });
          // const url = URL.createObjectURL(blob);
          console.log("response >>>" +json)
          console.log("uuid >>>" +json.itemUUID)
          // console.log("url >>> "+ url)
          console.log("mimeType>>>"+ json.mimeType)
          // state.imageMap.set(json.itemUUIds,url)


        }

      })

}


function base64ToImage(base64String, mimeType, itemUUID) {

  const byteCharacters = atob(base64String);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);
  const blob = new Blob([byteArray], { type: mimeType });
  const url = URL.createObjectURL(blob)
 state.imageMap.set(itemUUID,url)

}


onMounted(() =>{
  getList()
  getImages()

})


</script>

<template>
  <div class="tmp" style="height: 100% width= 100%">
<!--    <item-list :item ="state.itemList.items" :map ="state.imageMap"/>-->
    <item-list :map ="state.imageMap"/>
    <item-list :map ="state.imageMap"/>
    <item-list :map ="state.imageMap"/>
    <item-list :map ="state.imageMap"/>
    <item-list :map ="state.imageMap"/>
    <item-list :map ="state.imageMap"/>
    <item-list :map ="state.imageMap"/>
    <item-list :map ="state.imageMap"/>
<!--    <item-list/>-->
<!--    <item-list/>-->

<!--    <item-list/>-->
<!--    <item-list/>-->
<!--    <item-list/>-->
<!--    <item-list/>-->


  </div>

</template>

<style>

.tmp {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;

}

</style>
