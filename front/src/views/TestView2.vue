<template>
  <div id="app">
    <button @click="state.showListModal = true">주소 리스트 보기</button>
    <AddressListModal
        v-if="state.showListModal"
        :addresses="state.addresses"
        @close="state.showListModal = false"
        @select="handleAddressSelect"
        @submit = "closeList"
    />
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import AddressRequest from "@/entity/address/AddressRequest";
import AddressListModal from "@/views/AddressListModal.vue";
import type Address from "@/entity/address/Address";


// Sample data
const sampleAddresses = [
  new AddressRequest(),
  new AddressRequest(),
  // Add more sample addresses as needed
]

// Populate sample data for demonstration
sampleAddresses[0].name = '홍길동'
sampleAddresses[0].address = '서울시 강남구'
sampleAddresses[0].detailAddress = '서울'
sampleAddresses[0].zipcode = '12345'
sampleAddresses[0].phoneNumber = '010-1234-5678'

sampleAddresses[1].name = '이몽룡'
sampleAddresses[1].address = '부산시 해운대구'
sampleAddresses[1].detailAddress = '부산'
sampleAddresses[1].zipcode = '54321'
sampleAddresses[1].phoneNumber = '010-8765-4321'

const state = reactive({
  showListModal: false,
  addresses: sampleAddresses,
  selectedAddress: null
})

function handleAddressSelect(address: Address) {
  state.selectedAddress = address
  state.showListModal = false
}

function closeList(){
  state.showListModal = false
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>