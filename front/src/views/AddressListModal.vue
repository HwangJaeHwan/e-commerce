<script setup lang="ts">
import {defineProps, onMounted, reactive} from 'vue'
import AddressRequest from "@/entity/address/AddressRequest";
import {container} from "tsyringe";
import AddressRepository from "@/repository/AddressRepository";
import type Address from "@/entity/address/Address";
import AddressEditModal from "@/views/AddressEditModal.vue";


const props = defineProps<{ addresses: AddressRequest[] }>()

const ADDRESS_REPOSITORY = container.resolve(AddressRepository)

const emit = defineEmits(['close', 'select','submit'])

type StateType = {
  addresses: Address[]
  showEditModal: boolean
  editingAddress: Address | null
}

const state = reactive<StateType>({
  addresses: [],
  showEditModal: false,
  editingAddress: null,
})

onMounted(() =>{
  getList()

})

function getList(){
  ADDRESS_REPOSITORY.getAddresses()
      .then((addresses =>{
        state.addresses = addresses
      }))

}

function selectAddress(address: Address) {
  emit('select', address)
}
function deleteAddress(address:Address){
  // ADDRESS_REPOSITORY.

}

function editAddress(address: Address) {
  state.editingAddress = address
  state.showEditModal = true
}



function closeEdit() {
  // Handle address submit logic here
  emit('submit')
  state.showEditModal = false
}

</script>



<template>
  <div class="list-modal">
    <div class="list-modal-content">
      <span class="list-close" @click="$emit('close')">&times;</span>
      <h2>주소 리스트</h2>
      <ul>
        <li v-for="(address, index) in props.addresses" :key="index">
          <h3> {{ address.name }}</h3>
          <p> {{ address.address }}, {{address.detailAddress}}</p>
          <p> {{ address.phoneNumber }}</p>
          <button @click="selectAddress(address)">선택</button>
          <button @click="editAddress(address)">수정</button>
        </li>
      </ul>
      <AddressEditModal v-if="state.showEditModal" :address="state.editingAddress" @close="state.showEditModal = false" @submit="closeEdit" />
    </div>
  </div>
</template>


<style>
.list-modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.list-modal-content {
  background-color: #fefefe;
  padding: 20px;
  border-radius: 5px;
  width: 80%;
  max-width: 600px;
}

.list-close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

.list-close:hover,
.list-close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>