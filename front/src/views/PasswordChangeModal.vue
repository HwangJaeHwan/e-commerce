<script setup lang="ts">
import { reactive, defineEmits } from 'vue';
import type PasswordChange from "@/entity/user/PasswordChange";
import {container} from "tsyringe";
import UserRepository from "@/repository/UserRepository";
import {ElMessage} from "element-plus";

const emit = defineEmits(['close', 'submit']);
const USER_REPISITORY = container.resolve(UserRepository)

type StateType = {
  passwordChange : PasswordChange
}

const state = reactive<StateType>({
  passwordChange : new PasswordChange()
});

function closeModal() {
  emit('close');
}

function submitPasswordChange() {
  if (state.newPassword !== state.confirmPassword) {
    ElMessage({ type: 'error', message: '새 비밀번호와 비밀번호 확인이 일치하지 않습니다' });
    return;
  }
  USER_REPISITORY.passwordChange(state.passwordChange)
  closeModal();
}
</script>

<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <button class="close-button" @click="closeModal">X</button>
      <h2>비밀번호 변경</h2>
      <el-form label-width="150px">
        <el-form-item label="현재 비밀번호">
          <el-input type="password" v-model="state.passwordChange.currentPassword"></el-input>
        </el-form-item>
        <el-form-item label="새 비밀번호">
          <el-input type="password" v-model="state.passwordChange.newPassword"></el-input>
        </el-form-item>
        <el-form-item label="비밀번호 확인">
          <el-input type="password" v-model="state.passwordChange.confirmPassword"></el-input>
        </el-form-item>
      </el-form>
      <div class="modal-footer">
        <el-button type="primary" @click="submitPasswordChange">확인</el-button>
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