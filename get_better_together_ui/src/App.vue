<template>
  <div id="app" :class="themeClass">
    <router-view/>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex';

const store = useStore();

// 根据用户性别确定全局主题
const themeClass = computed(() => {
  const userInfo = store.state.user?.userInfo || {};
  const userSex = userInfo.sex;
  
  if (userSex === 1) return 'male-theme';
  if (userSex === 0) return 'female-theme';
  return 'default-theme';
});
</script>

<style lang="scss">
// 全局样式重置
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  margin: 0;
  padding: 0;
  font-family: 'PingFang SC', 'Microsoft YaHei', 'Helvetica Neue', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  line-height: 1.6;
}

#app {
  min-height: 100vh;
  transition: all 0.3s ease;
  
  // 默认主题
  &.default-theme {
    background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
    color: #2c3e50;
  }
  
  // 男生主题 - 浅蓝色系
  &.male-theme {
    background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 50%, #90caf9 100%);
    color: #2c3e50;
    
    // 全局主题变量
    --primary-color: #64b5f6;
    --primary-light: #a7e1f8;
    --primary-dark: #1976d2;
    --accent-color: #42a5f5;
    --background-gradient: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 50%, #90caf9 100%);
    --card-background: rgba(255, 255, 255, 0.95);
    --text-primary: #2c3e50;
    --text-secondary: #5a6c7d;
    --border-color: rgba(100, 181, 246, 0.2);
    --shadow-color: rgba(100, 181, 246, 0.3);
  }
  
  // 女生主题 - 淡粉色系
  &.female-theme {
    background: linear-gradient(135deg, #fce4ec 0%, #f8bbd9 50%, #f48fb1 100%);
    color: #2c3e50;
    
    // 全局主题变量
    --primary-color: #f48fb1;
    --primary-light: #f8c8dc;
    --primary-dark: #d81b60;
    --accent-color: #f06292;
    --background-gradient: linear-gradient(135deg, #fce4ec 0%, #f8bbd9 50%, #f48fb1 100%);
    --card-background: rgba(255, 255, 255, 0.95);
    --text-primary: #2c3e50;
    --text-secondary: #5a6c7d;
    --border-color: rgba(244, 143, 177, 0.2);
    --shadow-color: rgba(244, 143, 177, 0.3);
  }
}

// 全局组件样式
.el-button {
  border-radius: 8px !important;
  font-weight: 500 !important;
  transition: all 0.3s ease !important;
  
  &.el-button--primary {
    background: var(--primary-color, #64b5f6) !important;
    border-color: var(--primary-color, #64b5f6) !important;
    
    &:hover {
      background: var(--primary-dark, #1976d2) !important;
      border-color: var(--primary-dark, #1976d2) !important;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px var(--shadow-color, rgba(100, 181, 246, 0.3)) !important;
    }
  }
}

.el-input {
  .el-input__wrapper {
    border-radius: 8px !important;
    transition: all 0.3s ease !important;
    
    &:hover {
      box-shadow: 0 2px 8px var(--shadow-color, rgba(100, 181, 246, 0.2)) !important;
    }
    
    &.is-focus {
      box-shadow: 0 4px 12px var(--shadow-color, rgba(100, 181, 246, 0.3)) !important;
    }
  }
}

.el-select {
  .el-select__wrapper {
    border-radius: 8px !important;
    transition: all 0.3s ease !important;
    
    &:hover {
      box-shadow: 0 2px 8px var(--shadow-color, rgba(100, 181, 246, 0.2)) !important;
    }
  }
}

.el-input-number {
  .el-input-number__decrease,
  .el-input-number__increase {
    border-radius: 8px !important;
  }
}

// 滚动条样式
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: var(--primary-color, #64b5f6);
  border-radius: 3px;
  opacity: 0.6;
  
  &:hover {
    opacity: 0.8;
  }
}

// 全局动画
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

// 工具类
.fade-in {
  animation: fadeIn 0.6s ease-out;
}

.slide-in-left {
  animation: slideInLeft 0.6s ease-out;
}

.slide-in-right {
  animation: slideInRight 0.6s ease-out;
}

.scale-in {
  animation: scaleIn 0.4s ease-out;
}

// 响应式断点
@media (max-width: 1200px) {
  #app {
    padding: 0 10px;
  }
}

@media (max-width: 768px) {
  #app {
    padding: 0 5px;
  }
}

@media (max-width: 480px) {
  #app {
    padding: 0;
  }
}
</style>
