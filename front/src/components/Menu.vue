<script setup lang="ts"></script>

<template>



    <el-menu class="menu" mode="horizontal" router>

      <el-menu-item  index="/add?Category=Fashion">패션</el-menu-item>
      <el-menu-item  index="/login">뷰티</el-menu-item>
      <el-menu-item  index="/register">푸드</el-menu-item>
      <el-menu-item  index="/item">스포츠</el-menu-item>
      <el-menu-item  index="/review/write">건강</el-menu-item>
      <el-menu-item  index="/cart">기타</el-menu-item>

    </el-menu>

</template>

<style scoped>
.menu {
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: space-around;
  align-items: center;
}


</style>