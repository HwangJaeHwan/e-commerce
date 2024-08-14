<script setup lang="ts">
import { onMounted, reactive } from 'vue';
import { useProfileStore } from "@/entity/user/Profile";
import AddressModal from '@/views/AddressModal.vue';
import AddressListModal from '@/views/AddressListModal.vue';

const profileStore = useProfileStore();

const state = reactive({
  userProfile: {
    id: 0,
    loginId: '',
    email: ''
  },
  showAddressModal: false,
  showAddressListModal: false,
});

onMounted(async () => {
  await profileStore.fetchProfile();
  if (profileStore.profile) {
    state.userProfile = profileStore.profile;
  }
});

function handleChangePassword() {
  // 비밀번호 변경 페이지로 이동
}

function handleViewOrders() {
  // 주문 목록 페이지로 이동
}

function handleAddAddress() {
  state.showAddressModal = true;
}

function handleViewAddressList() {
  state.showAddressListModal = true;
}

function closeAddressModal() {
  state.showAddressModal = false;
}

function closeAddressListModal() {
  state.showAddressListModal = false;
}
</script>

<template>
  <div class="mypage-container">
    <h1 class="header">마이페이지</h1>

    <div class="profile-section">
      <h2 class="section-title">내 정보</h2>
      <div class="profile-details">
        <p><strong>아이디:</strong> {{ state.userProfile.loginId }}</p>
        <p><strong>이메일:</strong> {{ state.userProfile.email }}</p>
      </div>
    </div>

    <div class="actions">
      <el-button class="action-button" type="primary" @click="handleViewOrders">주문 목록 확인</el-button>
      <el-button class="action-button" type="warning" @click="handleChangePassword">비밀번호 변경</el-button>
      <el-button class="action-button" type="success" @click="handleAddAddress">주소지 등록</el-button>
      <el-button class="action-button" type="info" @click="handleViewAddressList">주소지 리스트 보기</el-button>
    </div>

    <!-- 주소지 등록 모달 -->
    <AddressModal v-if="state.showAddressModal" @close="closeAddressModal" />

    <!-- 주소지 리스트 모달 -->
    <AddressListModal v-if="state.showAddressListModal" @close="closeAddressListModal" />
  </div>
</template>

<style scoped>
.mypage-container {
  width: 400px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header {
  text-align: center;
  margin-bottom: 20px;
  font-size: 1.5rem;
  color: #333;
}

.profile-section {
  margin-bottom: 20px;
}

.section-title {
  font-size: 1.2rem;
  margin-bottom: 10px;
  color: #555;
  text-align: center;
}

.profile-details {
  background-color: #fff;
  padding: 15px;
  border-radius: 5px;
  border: 1px solid #ddd;
  text-align: left;
}

.profile-details p {
  margin: 5px 0;
}

.actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center; /* 버튼 중앙 정렬 */
}

.action-button {
  width: 100%;
  height: 50px; /* 동일한 버튼 크기 */
  font-size: 1rem;
  display: flex;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  margin: 0 !important; /* 기본 스타일 덮어쓰기 */
}
</style>