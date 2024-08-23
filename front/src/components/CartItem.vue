<script setup lang="ts">
import { Delete } from '@element-plus/icons-vue';
import { container } from "tsyringe";
import UserRepository from "@/repository/UserRepository";
import ShoppingCartItem from "@/entity/item/ShoppingCartItem";
import CartMessage from "@/entity/data/CartMessage";
import { reactive } from "vue";
import { useRouter } from "vue-router";

const USER_REPOSITORY = container.resolve(UserRepository);

const props = defineProps<{
  url: string;
  item: ShoppingCartItem;
}>();

const state = reactive({
  item: { ...props.item }
});

const emits = defineEmits(['remove', 'update']);
const router = useRouter();

function changeItemQuantity() {
  const message = new CartMessage(state.item.itemUUID, state.item.quantity);
  USER_REPOSITORY.cartMessage(message);
  emits('update', state.item);
}

function deleteItem() {
  const message = new CartMessage(state.item.itemUUID, 0);
  USER_REPOSITORY.cartMessage(message);
  emits('remove', props.item.itemUUID);
}

function navigateToItemDetail(itemUUID: string) {
  router.push({ name: 'item', params: { itemUUID } });
}
</script>

<template>
  <div class="order-item-row">
    <div class="order-item-image">
      <el-image :src="props.url" fit="contain" alt="Item Image" class="order-item-img" @click="navigateToItemDetail(state.item.itemUUID)" />
    </div>

    <div class="order-item-details">
      <div class="order-item-name mb-2">
        <span class="hoverable" @click="navigateToItemDetail(state.item.itemUUID)">
          {{ state.item.name }}
        </span>
      </div>
      <div class="order-item-quantity mb-2">
        <el-input-number
            v-model="state.item.quantity"
            size="small"
            :min="1"
            :max="100"
            @change="changeItemQuantity"
            style="width: 100px;"
        />
      </div>
      <div class="order-item-price mb-2">
        {{ state.item.itemPrice }}원
      </div>
    </div>

    <div @click.stop>
      <el-button type="danger" :icon="Delete" circle @click="deleteItem" />
    </div>
  </div>
</template>

<style scoped>
.order-item-row {
  display: flex;
  align-items: center;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  padding: 10px;
  /* 여기서 div에는 별도의 hover 효과를 추가하지 않습니다 */
}

.order-item-image {
  width: 150px;
  height: 150px;
  position: relative;
  flex-shrink: 0;
  margin-right: 20px;
}

.order-item-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
  transition: transform 0.3s ease-in-out;
}


.order-item-details {
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex-grow: 1;
}

.order-item-name {
  font-weight: bold;
  font-size: 18px;
  word-break: break-all;
}

.hoverable {
  cursor: pointer; /* 마우스를 올렸을 때 커서가 손가락 모양으로 변합니다 */
  transition: color 0.3s, text-decoration 0.3s ease-in-out;
}

.hoverable:hover {
  color: #3498db; /* 글자에 마우스를 올렸을 때 색상 변경 */
  text-decoration: underline; /* 글자에 마우스를 올렸을 때 밑줄 추가 */
}

.order-item-quantity {
  font-size: 16px;
  word-break: break-all;
}

.order-item-price {
  font-size: 22px; /* 상품 이름보다 약간 큰 크기 */
  font-weight: bold;
  color: #000; /* 검은색으로 변경 */
  word-break: break-all;
}

.mb-2 {
  margin-bottom: 10px;
}
</style>