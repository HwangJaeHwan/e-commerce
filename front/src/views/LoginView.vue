<script setup lang="ts">
import {reactive, ref} from "vue";

import axios from 'axios';
import Login from "@/entity/user/Login";

const state = reactive({
  login: new Login()
    }
)

function doLogin() {
  console.log(">>",state.login)
  axios.post('/api/user-service/login',state.login)
}
</script>



<template>
  <el-row class="mt-5">
    <el-col :span="10" :offset="7">
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
</template>