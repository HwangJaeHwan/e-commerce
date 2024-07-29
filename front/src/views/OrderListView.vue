<script setup lang="ts">


import {onMounted, reactive, UnwrapRef} from "vue";
import type Order from "@/entity/order/Order";
import {container} from "tsyringe";
import OrderRepository from "@/repository/OrderRepository";
import HttpError from "@/http/HttpError";
import {ElMessage} from "element-plus";
import Paging from "@/entity/data/Paging";
import ImageListRequest from "@/entity/image/ImageListRequest";
import ImageResponse from "@/entity/image/ImageResponse";
import ImageRepository from "@/repository/ImageRepository";
import OrderItem from "@/components/OrderItem.vue";

const ORDER_REPOSITORY =container.resolve(OrderRepository)
const IMAGE_REPOSITORY = container.resolve(ImageRepository)

type StateType = {
  orderList: Paging<Order>,
  imageMap: Map<string,string[]>
}

const state = reactive<StateType>({
  orderList: new Paging<Order>(),
  imageMap: new Map<string, string[]>
})


function getOrders() {
  ORDER_REPOSITORY.getOrders()
      .then((orders)=>{
        console.log(">>>order = {}",orders)
        state.orderList = orders
        getImages()
      })
      // .catch((e: HttpError) => {
      //   console.log("시발")
      //   ElMessage({ type: 'error', message: e.getMessage() })
      // })


}


function getImages(){

  const request = new ImageListRequest()

  request.imageType = 'ITEM'
  state.orderList.items.forEach(order => {
    order.items.forEach(orderItem => {
      request.uuids.push(orderItem.itemUUID);
    })
  })

  console.log("request = "+JSON.stringify(request))

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



onMounted(()=>{
  getOrders()
})

</script>



<template>


  <div class="d-flex align-items-center justify-content-center mb-5">
    <h1>주문목록</h1>
  </div>
  <div class="zz">

    <div v-for="(order,index) in state.orderList.items" :key="index" >
      <OrderItem :map="state.imageMap" :order= "order"/>
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