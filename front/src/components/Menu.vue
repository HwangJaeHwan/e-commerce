<script setup lang="ts">
</script>

<template>
  <el-menu class="menu" mode="horizontal" router>
    <el-menu-item class="el-menu-item" index="/items?category=FASHION">패션</el-menu-item>
    <el-menu-item index="/items?category=BEAUTY">뷰티</el-menu-item>
    <el-menu-item index="/items?category=FOOD">푸드</el-menu-item>
    <el-menu-item index="/items?category=SPORT">스포츠</el-menu-item>
    <el-menu-item index="/items?category=HEALTH">건강</el-menu-item>
    <el-menu-item index="/items?category=ETC">기타</el-menu-item>
  </el-menu>
</template>

<style scoped>
.menu {
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: space-around;
  align-items: center;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid #eee;
}

.el-menu-item {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 20px;
  height: 50px; /* 메뉴 항목이 일정한 높이를 가지도록 설정 */
  font-size: 1rem;
  font-weight: 500;
  color: #333;
  transition: color 0.3s, background-color 0.3s;
  cursor: pointer; /* 커서를 포인터로 변경하여 클릭 가능함을 표시 */
}

.el-menu-item:hover {
  color: #fff;
  background-color: #007BFF;
}

.el-menu-item.is-active {
  color: #fff;
}
</style>