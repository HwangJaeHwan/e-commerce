<script setup lang="ts">
import { reactive } from 'vue'

import Login from '@/entity/user/Login'
import { useRouter } from 'vue-router'
import type HttpError from '@/http/HttpError'
import UserRepository from '@/repository/UserRepository'
import {container} from "tsyringe";
import {ElMessage} from "element-plus";
import Token from "@/entity/data/Token";

const state = reactive({
  login: new Login()
})
const router = useRouter()

const USER_REPOSITORY = container.resolve(UserRepository)

function doLogin() {

  USER_REPOSITORY.login(state.login)
    .then((token) => {
      ElMessage({type: 'success', message: '환영합니다.'})
      console.log(token)
      localStorage.setItem('token',token.token)
      console.log(">>>>>>"+localStorage.getItem('token'))
      router.replace('/')
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}



</script>
<!--//:span="10" :offset="7"-->
<template >
  <div class="login-box">
  <el-row style="width: 300px" class="mt-5">
    <el-col>
      <el-form label-position="top">
        <el-form-item label="아이디">
          <el-input v-model="state.login.loginId"></el-input>
        </el-form-item>

        <el-form-item label="비밀번호">
          <el-input type="password" v-model="state.login.password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="doLogin()">로그인</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
  </div>
</template>

<style>
.login-box{
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
