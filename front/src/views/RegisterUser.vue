<script setup lang="ts">
import {reactive, ref} from "vue";

import axios from 'axios';
import Register from "@/entity/user/Register";
import {container} from "tsyringe";
import UserRepository from "@/repository/UserRepository";
import router from "@/router";
import HttpError from "@/http/HttpError";
import {ElMessage} from "element-plus";

const USER_REPOSITORY =container.resolve(UserRepository)

const state = reactive({
      register: new Register()
    }
)

function doRegister() {

  USER_REPOSITORY.register(state.register)
      .then(()=>{
        router.replace('/login')
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })
}
</script>



<template>
  <el-row class="mt-5">
    <el-col :span="10" :offset="7">
      <el-form label-position="top">
        <el-form-item label="아이디">
          <el-input v-model="state.register.loginId"></el-input>
        </el-form-item>

        <el-form-item label="이메일">
          <el-input v-model="state.register.email"></el-input>
        </el-form-item>

        <el-form-item label="비밀번호">
          <el-input type="password" v-model="state.register.password"></el-input>
        </el-form-item>

        <el-form-item label="비밀번호 확인">
          <el-input type="password" v-model="state.register.passwordCheck"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="doRegister()">로그인</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>