<script setup lang="ts">
import {watchEffect, reactive} from "vue";
import ReviewInfo from "@/components/ReviewInfo.vue";
import type Review from "@/entity/review/Review";
import type Paging from "@/entity/data/Paging";
import {container} from "tsyringe";
import ImageRepository from "@/repository/ImageRepository";
import ImageListRequest from "@/entity/image/ImageListRequest";
import ImageResponse from "@/entity/image/ImageResponse";
import HttpError from "@/http/HttpError";
import {ElMessage} from "element-plus";

const props = defineProps<{
  paging : Paging<Review>
}>()

const IMAGE_REPOSITORY = container.resolve(ImageRepository)

type StateType = {
  imageMap: Map<string,string[]>
}

const state = reactive<StateType>({
  imageMap: new Map<string,string[]>
})

function getImages(){
  const request = new ImageListRequest()

  request.imageType = 'REVIEW'
  request.uuids = props.paging.items.map(item => item.reviewUUID)

  console.log("request = " + JSON.stringify(request))

  console.log("ㅋㅋㅋㅋ" + JSON.stringify(props.paging))

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
        console.log("ㅡㅡ =" + JSON.stringify(state.imageMap))

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

watchEffect(() => {
  if (props.paging.items.length > 0) {
    getImages();
  }
});
</script>

<template>
  <div class="totalCount">리뷰: {{props.paging.totalElement}}</div>

  <ul class="reviews">
    <li v-for="review in props.paging.items" :key="review.reviewUUID" class="comment">
      <ReviewInfo :review="review" :imageMap="state.imageMap"/>
    </li>
  </ul>
</template>

<style scoped>
.totalCount {
  font-size: 1.4rem;
}

label {
  font-size: 0.78rem;
}

.reviews {
  margin-top: 3rem;
  list-style: none;
  padding: 0;
}

.review {
  margin-bottom: 2.4rem;

  &:last-child {
    margin-bottom: 0;
  }
}
</style>