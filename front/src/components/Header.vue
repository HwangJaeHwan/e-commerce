<script setup lang="ts">
import { ref, onMounted, watchEffect } from 'vue';
import { useProfileStore } from "@/entity/user/Profile";
import { useRouter } from 'vue-router';

const profileStore = useProfileStore();
const router = useRouter();

const searchQuery = ref('');
const selectedCategory = ref('');

const options = [
  { category: 'FASHION', label: '패션' },
  { category: 'BEAUTY', label: '뷰티' },
  { category: 'FOOD', label: '푸드' },
  { category: 'SPORTS', label: '스포츠' },
  { category: 'HEALTH', label: '건강' },
  { category: 'ETC', label: '기타' },
];

function handleLogout() {
  profileStore.setProfile(null); // 로그아웃 시 프로필 초기화
  localStorage.removeItem('token');
}

function handleSearch() {
  router.push({
    path: '/items',
    query: {
      category: selectedCategory.value,
      search: searchQuery.value
    }
  });
}

onMounted(async () => {
  if (!profileStore.profile) {
    await profileStore.fetchProfile();  // 새로고침 시 서버로부터 프로필 데이터를 가져옴
  }
});

// watchEffect를 사용하여 프로필 상태가 변경될 때마다 뷰를 강제로 업데이트
watchEffect(() => {
  console.log("Profile updated:", profileStore.profile);
});
</script>

<template>
  <div class="header-container">
    <div class="header">
      <div class="logo-title">
        <img src="/images/logo.png" alt="logo" class="logo" />
        <div class="title">테스트 헤드</div>
      </div>

      <div class="search-container">
        <select v-model="selectedCategory" class="category-select">
          <option value="" disabled>카테고리 선택</option>
          <option v-for="option in options" :key="option.category" :value="option.category">
            {{ option.label }}
          </option>
        </select>
        <input
            v-model="searchQuery"
            type="text"
            class="search-input"
            placeholder="검색어를 입력하세요"
        />
        <button @click="handleSearch" class="search-button">검색</button>
      </div>

      <div class="login-info">
        <router-link class="link" to="/register" v-if="!profileStore.profile">
          회원가입
        </router-link>
        <router-link class="cart-link" to="/cart" v-if="profileStore.profile">
          🛒
        </router-link>
        <router-link class="link" to="/mypage" v-if="profileStore.profile">
          {{ profileStore.profile?.loginId }}
        </router-link>
        <router-link class="link" to="/login" v-if="!profileStore.profile">
          로그인
        </router-link>
        <router-link class="link" to="tmp" v-if="profileStore.profile" @click="handleLogout">
          로그아웃
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.header-container {
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
  padding: 1rem 2rem;
}

.header {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-title {
  display: flex;
  align-items: center;
}

.logo {
  width: 120px;
  object-fit: cover;
}

.title {
  font-size: 1.5rem;
  font-weight: 500;
  color: #343a40;
  margin-left: 10px;
}

.search-container {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.category-select {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-input {
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 200px;
}

.search-button {
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0056b3;
}

.login-info {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.link {
  color: #007bff;
  text-decoration: none;
  font-weight: 500;
}

.link:hover {
  text-decoration: underline;
}

.cart-link {
  font-size: 1.5rem; /* 이모티콘 크기 설정 */
  text-decoration: none; /* 밑줄 제거 */
}

.cart-link:hover {
  text-decoration: none; /* 마우스 오버 시에도 밑줄 제거 */
}
</style>