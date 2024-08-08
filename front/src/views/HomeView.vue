<script setup lang="ts">

import {container} from "tsyringe";
import ItemRepository from "@/repository/ItemRepository";
import {computed, onMounted, reactive, ref} from "vue";
import type Item from "@/entity/item/Item";
import Paging from "@/entity/data/Paging";
import ItemList from "@/components/ItemList.vue";
import ImageRepository from "@/repository/ImageRepository";
import ImageResponse from "@/entity/image/ImageResponse";
import ImageListRequest from "@/entity/image/ImageListRequest";
import {hColgroup} from "element-plus/es/components/table/src/h-helper";
import HttpError from "@/http/HttpError";
import {ElMessage} from "element-plus";


const ITEM_REPOSITORY = container.resolve(ItemRepository)
const IMAGE_REPOSITORY = container.resolve(ImageRepository)

type StateType = {
  itemList: Paging<Item>,
  imageMap: Map<string,string[]>
}

const state = reactive<StateType>({
    itemList: new Paging<Item>(),
    imageMap: new Map<string,string[]>
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
  const request = new ImageListRequest()

  request.imageType = 'ITEM'
  request.uuids = state.itemList.items.map(item => item.itemUUID)

  console.log("request = " + JSON.stringify(request))

  IMAGE_REPOSITORY.getImages(request)
      .then((imageList:ImageResponse[]) =>{

        for (const image of imageList) {

          for (const imageInfo of image.imageInfos) {

            const url = base64ToImage(imageInfo.imageData, imageInfo.mimeType);
            if (!state.imageMap.has(image.uuid)) {

              state.imageMap.set(image.uuid, []);
            }

            state.imageMap.get(image.uuid)?.push(url);

          }


        }
        console.log("ㅡㅡ =" + JSON.stringify(state.imageMap.get(imageList[0].uuid)))

      })
      .catch((e:HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })


}


function base64ToImage(base64String, mimeType) {

  const byteCharacters = atob(base64String);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);
  const blob = new Blob([byteArray], { type: mimeType });

  return URL.createObjectURL(blob)


}



onMounted(() =>{
  getList()

})


</script>
<!--style="height: 100% width= 100%"-->
<template>
  <div class="home-grid">

<!--    <item-list :item ="state.itemList.items" :map ="state.imageMap"/>-->
    <div v-for="(item,index) in state.itemList.items" :key="index">
      <item-list :map ="state.imageMap" :item ="item"/>
    </div>



  </div>

</template>

<style>

.home-grid {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;

}

</style>
