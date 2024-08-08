<script setup lang="ts">
import { onMounted } from 'vue';
import { useProfileStore } from "@/entity/user/Profile";

const profileStore = useProfileStore();

onMounted(async () => {
  await profileStore.fetchProfile();
});

function handleLogout() {
  profileStore.setProfile(null); // 로그아웃 시 프로필 초기화
}
</script>

<template>
  <div class="header-container">
    <div class="header">
      <img src="/images/logo.png" alt="logo" class="logo" />
      <div class="title">테스트 헤드</div>
      <div class="login-info">
        <router-link class="link" to="test2" v-if="!profileStore.profile">
          회원가입
        </router-link>
        <router-link class="link" to="tmp" v-else>
          {{ profileStore.profile?.loginId }}
        </router-link>
        <router-link class="link" to="test" v-if="!profileStore.profile">
          로그인
        </router-link>
        <router-link class="link" to="tmp" v-else @click="handleLogout">
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

.login-info {
  display: flex;
  gap: 1rem;
}

.link {
  color: #007bff;
  text-decoration: none;
  font-weight: 500;
}

.link:hover {
  text-decoration: underline;
}
</style>