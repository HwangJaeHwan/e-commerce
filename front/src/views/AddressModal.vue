<script setup lang="ts">
import { reactive, defineEmits } from 'vue';
import AddressRequest from "@/entity/address/AddressRequest";
import { container } from "tsyringe";
import AddressRepository from "@/repository/AddressRepository";

const emit = defineEmits(['close', 'submit']);

const ADDRESS_REPOSITORY = container.resolve(AddressRepository);

type StateType = {
  address: AddressRequest,
}

const state = reactive<StateType>({
  address: new AddressRequest(),
});

function closeModal() {
  emit('close');
}

function submitAddress() {
  ADDRESS_REPOSITORY.addAddress(state.address);
  closeModal();
}

function sample4_execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      state.address.zipcode = data.zonecode;
      state.address.address = data.roadAddress;
    }
  }).open();
}

// 전화번호 입력 시 자동으로 "-" 추가
function formatPhoneNumber() {
  let phoneNumber = state.address.phoneNumber.replace(/[^0-9]/g, ''); // 숫자 이외의 문자 제거

  if (phoneNumber.length < 4) {
    state.address.phoneNumber = phoneNumber;
  } else if (phoneNumber.length < 7) {
    state.address.phoneNumber = phoneNumber.slice(0, 3) + '-' + phoneNumber.slice(3);
  } else if (phoneNumber.length < 11) {
    state.address.phoneNumber = phoneNumber.slice(0, 3) + '-' + phoneNumber.slice(3, 6) + '-' + phoneNumber.slice(6);
  } else {
    state.address.phoneNumber = phoneNumber.slice(0, 3) + '-' + phoneNumber.slice(3, 7) + '-' + phoneNumber.slice(7, 11);
  }
}

</script>

<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <button class="close-button" @click="closeModal">X</button>
      <h2>주소 입력</h2>
      <el-form :model="state.address" label-width="100px">
        <el-form-item label="이름">
          <el-input v-model="state.address.name"></el-input>
        </el-form-item>
        <el-form-item label="우편번호">
          <el-row :gutter="10">
            <el-col :span="14">
              <el-input v-model="state.address.zipcode" disabled></el-input>
            </el-col>
            <el-col :span="8">
              <el-button type="primary" @click="sample4_execDaumPostcode">우편번호 찾기</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="주소">
          <el-input v-model="state.address.address" disabled></el-input>
        </el-form-item>
        <el-form-item label="상세 주소">
          <el-input v-model="state.address.detailAddress"></el-input>
        </el-form-item>
        <el-form-item label="전화번호">
          <el-input v-model="state.address.phoneNumber" @input="formatPhoneNumber"></el-input>
        </el-form-item>
      </el-form>
      <div class="modal-footer">
        <el-button type="primary" @click="submitAddress">확인</el-button>
        <el-button @click="closeModal">취소</el-button>
      </div>
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

.modal-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>