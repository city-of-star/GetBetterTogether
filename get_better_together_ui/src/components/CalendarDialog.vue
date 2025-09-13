<template>
  <div v-if="visible" class="dialog-overlay" @click="closeDialog">
    <div class="dialog-container" :class="gender" @click.stop>
      <!-- 弹窗头部 -->
      <div class="dialog-header">
        <div class="date-display">
          <span class="date-text">{{ formattedDate }}</span>
        </div>
        <button class="close-button" :class="gender" @click="closeDialog">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" viewBox="0 0 16 16">
            <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
          </svg>
        </button>
      </div>
      
      <!-- 弹窗内容区域 -->
      <div class="dialog-content">
        <!-- 体重记录表单 -->
        <div class="weight-form">
          <div class="input-row">
            <div class="input-group">
              <span class="input-icon">⚖️</span>
              <span class="input-label">体重</span>
            </div>
            <input
              v-model="weight"
              type="number"
              step="0.1"
              placeholder="请输入体重"
              class="form-input"
            />
            <button
              @click="saveWeight"
              class="btn btn-save"
              :class="gender"
            >
              保存
            </button>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, defineProps, defineEmits, ref } from 'vue'
import axios from '@/utils/axios'
import { ElMessage } from "element-plus";

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  date: {
    type: Date,
    default: () => new Date()
  },
  gender: {
    type: String,
    default: 'male'
  },
  userId: {
    type: Number,
    default: 1
  }
})

const emit = defineEmits(['update:visible'])

// 体重数据
const weight = ref('')

// 格式化日期显示
const formattedDate = computed(() => {
  if (!props.date) return ''
  const year = props.date.getFullYear()
  const month = props.date.getMonth() + 1
  const day = props.date.getDate()
  return `${year}年${month}月${day}日`
})

// 保存体重
const saveWeight = async () => {
  if (!weight.value) return
  
  try {
    await axios.post('/user/setUserInfo', {
      userId: props.userId,
      weight: parseFloat(weight.value)
    })
    ElMessage({
      message: '保存成功',
      type: 'success',
    })
  } catch (error) {
    console.error('保存失败:', error)
  }
}

// 关闭弹窗
const closeDialog = () => {
  weight.value = ''
  emit('update:visible', false)
}
</script>

<style scoped lang="scss">
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.dialog-container {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  width: 80vw;
  height: 80vh;
  overflow: hidden;
  position: relative;
  border: 1px solid rgba(255, 255, 255, 0.2);
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    border-radius: 16px 16px 0 0;
  }
  
  &.male::before {
    background: linear-gradient(90deg, #64b5f6, #42a5f5);
  }
  
  &.female::before {
    background: linear-gradient(90deg, #f48fb1, #f06292);
  }
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  background: rgba(255, 255, 255, 0.8);
}

.date-display {
  .date-text {
    font-size: 1.1rem;
    font-weight: 600;
    color: #2c3e50;
  }
}

.close-button {
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(0, 0, 0, 0.1);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  
  &.male {
    color: #1976d2;
    
    &:hover {
      background: linear-gradient(135deg, rgba(100, 181, 246, 0.1), rgba(66, 165, 245, 0.1));
      transform: scale(1.05);
      box-shadow: 0 4px 12px rgba(100, 181, 246, 0.2);
    }
  }
  
  &.female {
    color: #d81b60;
    
    &:hover {
      background: linear-gradient(135deg, rgba(244, 143, 177, 0.1), rgba(240, 98, 146, 0.1));
      transform: scale(1.05);
      box-shadow: 0 4px 12px rgba(244, 143, 177, 0.2);
    }
  }
}

.dialog-content {
  padding: 24px;
  height: calc(80vh - 80px);
  overflow-y: auto;
}

// 体重表单样式
.weight-form {
  max-width: 350px;
  margin: 0;
}

.input-row {
  display: flex;
  gap: 10px;
  align-items: center;
}

.input-group {
  display: flex;
  align-items: center;
  gap: 6px;
  white-space: nowrap;
}

.input-icon {
  font-size: 16px;
}

.input-label {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.form-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  height: 36px;
  
  &:focus {
    outline: none;
    border-color: #64b5f6;
  }
}


.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  height: 36px;
}

.btn-save {
  color: white;
  
  &.male {
    background: #64b5f6;
  }
  
  &.female {
    background: #f48fb1;
  }
}

// 响应式设计
@media (max-width: 480px) {
  .dialog-container {
    width: 90vw;
    height: 85vh;
    margin: 0 10px;
  }
  
  .dialog-header {
    padding: 16px 20px;
  }
  
  .dialog-content {
    padding: 20px;
    height: calc(85vh - 80px);
  }
  
  .date-text {
    font-size: 1rem;
  }
  
  .close-button {
    width: 32px;
    height: 32px;
  }
  
  .weight-form {
    max-width: 100%;
  }
  
  .input-row {
    flex-direction: column;
    gap: 8px;
  }
  
  .btn {
    width: 100%;
  }
}
</style>