<template>
  <div id="app">
    <button @click="showModal = true">주소 입력</button>
    <AddressModal v-if="showModal" @close="showModal = false" @submit="handleAddressSubmit" />
    <div v-if="submittedAddress">
      <h3>입력된 주소:</h3>
      <p>우편번호: {{ submittedAddress.zipcode }}</p>
      <p>주소: {{ submittedAddress.addressLine1 }}</p>
      <p>상세 주소: {{ submittedAddress.addressLine2 }}</p>
      <p>도시: {{ submittedAddress.city }}</p>
      <p>주/도: {{ submittedAddress.state }}</p>
      <p>국가: {{ submittedAddress.country }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import AddressModal from "@/views/AddressModal.vue";

const showModal = ref(false)
const submittedAddress = ref(null)

function handleAddressSubmit(address) {
  submittedAddress.value = address
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