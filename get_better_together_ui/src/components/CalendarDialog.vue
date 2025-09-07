<template>
  <transition name="modal">
    <div v-if="visible" class="modal-mask" @click.self="close">
      <div class="modal-container" :class="gender">
        <div class="modal-header">
          <h3>{{ formattedDate }}</h3>
          <button class="modal-close" @click="close">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
              <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
            </svg>
          </button>
        </div>
        <div class="modal-content">
          <p>这是 {{ gender === 'male' ? '男生' : '女生' }} 的日程详情</p>
          <!-- 这里可以添加更多日程详情内容 -->
          <div class="placeholder-content">
            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="currentColor" viewBox="0 0 16 16">
              <path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm.93-9.412-1 4.705c-.07.34.029.533.304.533.194 0 .487-.07.686-.246l-.088.416c-.287.346-.92.598-1.465.598-.703 0-1.002-.422-.808-1.319l.738-3.468c.064-.293.006-.399-.287-.47l-.451-.081.082-.381 2.29-.287zM8 5.5a1 1 0 1 1 0-2 1 1 0 0 1 0 2z"/>
            </svg>
            <p>日程详情功能开发中...</p>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="close">关闭</button>
          <button class="btn-primary" :class="gender">添加日程</button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { computed, defineProps, defineEmits } from 'vue';

const props = defineProps({
  visible: Boolean,
  date: Date,
  gender: {
    type: String,
    default: 'male'
  }
});

const emit = defineEmits(['update:visible']);

const formattedDate = computed(() => {
  if (!props.date) return '';
  return props.date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  });
});

const close = () => {
  emit('update:visible', false);
};
</script>

<style scoped>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: opacity 0.3s ease;
}

.modal-container {
  width: 90%;
  max-width: 500px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  transition: all 0.3s ease;
}

.modal-container.male {
  border-top: 4px solid #64b5f6;
}

.modal-container.female {
  border-top: 4px solid #f48fb1;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.3rem;
  font-weight: 600;
}

.modal-close {
  background: none;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.modal-close:hover {
  background-color: #f5f5f5;
}

.modal-content {
  padding: 24px;
  min-height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.placeholder-content {
  text-align: center;
  color: #888;
}

.placeholder-content svg {
  margin-bottom: 16px;
  opacity: 0.5;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid #eee;
}

button {
  padding: 10px 20px;
  border-radius: 8px;
  border: none;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-secondary {
  background-color: #f5f5f5;
  color: #333;
}

.btn-secondary:hover {
  background-color: #e0e0e0;
}

.btn-primary {
  color: white;
}

.btn-primary.male {
  background-color: #64b5f6;
}

.btn-primary.male:hover {
  background-color: #42a5f5;
}

.btn-primary.female {
  background-color: #f48fb1;
}

.btn-primary.female:hover {
  background-color: #f06292;
}

/* 动画效果 */
.modal-enter-from {
  opacity: 0;
}

.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  transform: scale(1.1);
}
</style>