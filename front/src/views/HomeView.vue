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

const state = reactive<StateType>({
    itemList: new Paging<Item>(),
    imageMap: new Map<string,string>
}
)

function getList() {
ITEM_REPOSITORY.getList()
    .then((itemList => {
      console.log("리스트>>"+ itemList);
      state.itemList = itemList;
      getImages()
    }))
}

function getImages(){
  const itemUUIDs = state.itemList.items.map(item => item.itemUUID)
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

})


</script>
<!--style="height: 100% width= 100%"-->
<template>
  <div class="tmp">
<!--    <item-list :item ="state.itemList.items" :map ="state.imageMap"/>-->
    <div v-for="(item,index) in state.itemList.items" :key="index">
      <item-list :map ="state.imageMap" :item ="item"/>
    </div>



  </div>

</template>

<style>

.tmp {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;

}

</style>
