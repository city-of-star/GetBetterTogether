<template>
  <div class="home-view" :class="themeClass">
    <header class="app-header">
      <div class="header-content">
        <div class="tabs">
          <button
              v-for="tab in tabs"
              :key="tab.id"
              :class="['tab-button', { active: $route.path === tab.path }]"
              @click="navigateTo(tab.path)"
          >
            <span class="tab-icon">{{ tab.icon }}</span>
            {{ tab.label }}
          </button>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view></router-view>
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
const tabs = [
  { id: 1, label: '日历', icon: '📅', path: '/calendar' },
  { id: 2, label: '统计', icon: '📊', path: '/stats' },
  { id: 3, label: '目标', icon: '🎯', path: '/goals' },
  { id: 4, label: '基本信息', icon: '👤', path: '/basic-info' }
];

// 导航到指定路径
const navigateTo = (path) => {
  router.push(path);
};
</script>

<style scoped lang="scss">
.home-view {
  min-height: 100vh;
  padding: 0;
  position: relative;
  overflow-x: hidden;

  &.default-theme {
    background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  }

  &.male-theme {
    background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 50%, #90caf9 100%);

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: radial-gradient(circle at 20% 80%, rgba(100, 181, 246, 0.1) 0%, transparent 50%),
      radial-gradient(circle at 80% 20%, rgba(33, 150, 243, 0.1) 0%, transparent 50%);
      pointer-events: none;
    }
  }

  &.female-theme {
    background: linear-gradient(135deg, #fce4ec 0%, #f8bbd9 50%, #f48fb1 100%);

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: radial-gradient(circle at 20% 80%, rgba(244, 143, 177, 0.1) 0%, transparent 50%),
      radial-gradient(circle at 80% 20%, rgba(233, 30, 99, 0.1) 0%, transparent 50%);
      pointer-events: none;
    }
  }
}

.app-header {
  position: relative;
  z-index: 2;
  padding: 30px 20px;
  text-align: center;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
}

.tabs {
  display: inline-flex;
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.tab-button {
  padding: 12px 20px;
  border: none;
  background: none;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
  overflow: hidden;

  .tab-icon {
    font-size: 1.1rem;
  }

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
    transition: left 0.5s;
  }

  &:hover::before {
    left: 100%;
  }
}

.male-theme .tab-button.active {
  background: linear-gradient(135deg, #64b5f6, #42a5f5);
  color: white;
  box-shadow: 0 4px 16px rgba(100, 181, 246, 0.4);
}

.female-theme .tab-button.active {
  background: linear-gradient(135deg, #f48fb1, #f06292);
  color: white;
  box-shadow: 0 4px 16px rgba(244, 143, 177, 0.4);
}

.default-theme .tab-button.active {
  background: linear-gradient(135deg, #64b5f6, #42a5f5);
  color: white;
  box-shadow: 0 4px 16px rgba(100, 181, 246, 0.4);
}

.tab-button:not(.active):hover {
  background-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-2px);
}

.main-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 40px;
}

@media (max-width: 968px) {
  .tabs {
    flex-wrap: wrap;
    justify-content: center;
  }

  .tab-button {
    padding: 10px 16px;
    font-size: 0.9rem;
  }
}

@media (max-width: 768px) {
  .app-header {
    padding: 20px 15px;
  }

  .main-content {
    padding: 0 15px 30px;
  }
}
</style>