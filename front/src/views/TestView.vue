<script setup lang="ts">
import AddressModal from "@/views/AddressModal.vue";
import {reactive} from "vue";

const state = reactive({
  showModal: false,
  submittedAddress: null
})

function handleAddressSubmit(address) {
  state.submittedAddress = address
}
</script>

<template>
  <div id="app">
    <button @click="state.showModal = true">주소 입력</button>
    <AddressModal v-if="state.showModal" @close="state.showModal = false" @submit="handleAddressSubmit" />
  </div>
</template>


<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>