<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import Header from "@/components/Header.vue";
import Menu from "@/components/Menu.vue";
import Content from "@/components/Content.vue";
</script>

<template>
  <el-row>
    <el-col :span="12" :offset="6">

      <Header />
    </el-col>

    <el-col :span="12" :offset="6">
      <Menu />
    </el-col>

  </el-row>
  <el-col :span="12" :offset="6">
   <Content/>
  </el-col>

</template>

<style>
html {
  font-size: 16px;
  color: #383838;
}

body {
  background-color: #f9f9f9;
}
</style>
