<script setup lang="ts">
import {container} from "tsyringe";
import UserRepository from "@/repository/UserRepository";
import UserProfile from "@/entity/user/UserProfile";
import {onMounted, provide, reactive} from "vue";

const USER_REPOSITORY = container.resolve(UserRepository)


type StateType = {
  profile: UserProfile | null
}

const state = reactive<StateType>({
  profile: null
})

onMounted(async () => {
  USER_REPOSITORY.getProfile()
      .then((profile) => {
        console.log(profile)
        state.profile = profile
      })
})


</script>



<template>
  <div class="login-info">
    <router-link style="margin-right: 8px;" to="tmp" v-if="state.profile === null">
      회원가입
    </router-link>
    <router-link style="margin-right: 8px;" to="tmp" v-else>
      {{state.profile.loginId}}
    </router-link>


    <router-link to="tmp" v-if="state.profile === null">
      로그인
    </router-link>
    <router-link to="tmp" v-else>
      로그아웃
    </router-link>


  </div>
  <div class="header">
    <img src="/images/logo.png" alt="logo" class="logo"/>
    <div class="title">테스트 헤드</div>
  </div>
</template>


<style scoped>
.header {
  height: 120px;
  margin: 1rem 2rem;
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo {
  width: 104px;
  object-fit: cover;
}

.title {
  font-size: 2rem;
  font-weight: 300;
  margin-left: 5px;
}

.login-info{
  display: flex;
  justify-content: right;
}
</style>