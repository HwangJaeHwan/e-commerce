<script setup lang="ts">

import {container} from "tsyringe";
import ItemRepository from "@/repository/ItemRepository";
import {computed, onMounted, reactive, ref, watch} from "vue";
import type Item from "@/entity/item/Item";
import Paging from "@/entity/data/Paging";
import ItemList from "@/components/ItemList.vue";
import ImageRepository from "@/repository/ImageRepository";
import ImageResponse from "@/entity/image/ImageResponse";
import ImageListRequest from "@/entity/image/ImageListRequest";
import {hColgroup} from "element-plus/es/components/table/src/h-helper";
import HttpError from "@/http/HttpError";
import {useRoute, useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import router from "@/router";


const ITEM_REPOSITORY = container.resolve(ItemRepository)
const IMAGE_REPOSITORY = container.resolve(ImageRepository)

const route = useRoute()


type StateType = {
  itemList: Paging<Item>,
  imageMap: Map<string,string[]>,
  currentPage: number
}

const state = reactive<StateType>({
    itemList: new Paging<Item>(),
    imageMap: new Map<string,string[]>,
    currentPage: 1
}
)


function buildQueryParams(search?: string, category?: string, page?: number) {
  const params: Record<string, string | number> = {};

  if (search) {
    params.search = search;
  }

  if (category) {
    params.category = category;
  }

  if (page && page !== 1) { // page가 1일 경우, 백엔드에서 기본값으로 처리할 수 있다면 보내지 않음
    params.page = page;
  }

  return params;
}


function getList() {
  const { search, category, page } = route.query

  const params = buildQueryParams(
      search as string | undefined,
      category as string | undefined,
      page ? parseInt(page as string, 10) : undefined
  );
  const queryString = new URLSearchParams(params).toString();
  console.log("시발 = ", JSON.stringify(params))

  ITEM_REPOSITORY.getList(queryString)
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

function handlePageChange(page: number) {
  state.currentPage = page;
  router.push({ query: { ...route.query, page } });
}



onMounted(() =>{
  getList()

})

watch([() => route.query.search, () => route.query.category, () => route.query.page], () => {
  console.log("Query parameters changed:", route.query);
  getList();
});


</script>
<!--style="height: 100% width= 100%"-->
<template>
  <div class="home-grid-container">
    <div class="home-grid">
      <div v-for="(item,index) in state.itemList.items" :key="index">
        <item-list :map="state.imageMap" :item="item"/>
      </div>
    </div>

    <el-pagination
        v-if="state.itemList.totalElement > 0"
        class="pagination"
        layout="prev, pager, next"
        :current-page="state.currentPage"
        :page-size="10"
        :total="state.itemList.totalElement"
        @current-change="handlePageChange"
    />
  </div>
</template>

<style scoped>
.home-grid-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.home-grid {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  gap: 20px;
  width: 100%;
  max-width: 1200px; /* 원하는 최대 너비로 설정 */
  margin-bottom: 20px; /* 상품 리스트와 페이지네이션 사이에 여백 추가 */
}

.pagination {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-top: 20px;
}
</style>
