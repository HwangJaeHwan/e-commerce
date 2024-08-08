<script setup lang="ts">
import {defineProps, onMounted, reactive} from 'vue'
import AddressRequest from "@/entity/address/AddressRequest";
import {container} from "tsyringe";
import AddressRepository from "@/repository/AddressRepository";
import type Address from "@/entity/address/Address";
import AddressEditModal from "@/views/AddressEditModal.vue";

const ADDRESS_REPOSITORY = container.resolve(AddressRepository)

const emit = defineEmits(['close', 'select', 'submit'])

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

onMounted(() => {
  getList()
})

function getList() {
  ADDRESS_REPOSITORY.getAddresses()
      .then((addresses => {
        state.addresses = addresses
      }))
}

function selectAddress(address: Address) {
  emit('select', address)
}

function editAddress(address: Address) {
  state.editingAddress = address
  state.showEditModal = true
}

function handleEditSubmit(updatedAddress) {
  if (updatedAddress) {
    const index = state.addresses.findIndex(addr => addr.id === updatedAddress.id);
    if (index !== -1) {
      state.addresses[index] = updatedAddress;
    }
  } else {
    state.addresses = state.addresses.filter(addr => addr.id !== state.editingAddress.id);
  }
  state.showEditModal = false;
}

</script>

<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <button class="close-button" @click="$emit('close')">&times;</button>
      <h2>주소 리스트</h2>
      <div class="list-container">
        <ul>
          <li v-for="(address, index) in state.addresses" :key="index">
            <h3>{{ address.name }}</h3>
            <p>{{ address.address }}, {{ address.detailAddress }}</p>
            <p>{{ address.phoneNumber }}</p>
            <el-button type="success" @click="selectAddress(address)">선택</el-button>
            <el-button type="info" @click="editAddress(address)">수정</el-button>
          </li>
        </ul>
      </div>
      <AddressEditModal v-if="state.showEditModal" :address="state.editingAddress" @close="state.showEditModal = false" @submit="handleEditSubmit" />
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  position: relative;
  width: 500px;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.list-container {
  max-height: 400px;
  overflow-y: auto;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 20px;
}

button {
  margin-right: 10px;
}
</style>