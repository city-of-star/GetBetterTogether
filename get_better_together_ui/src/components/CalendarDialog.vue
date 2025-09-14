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

        <!-- 运动记录表单 -->
        <div class="sport-form">
          <div class="form-section">
            <div class="section-title">
              <span class="section-icon">🏃‍♂️</span>
              <span class="section-text">运动记录</span>
            </div>
            
            <!-- 运动项目选择 -->
            <div class="sport-selection">
              <div class="input-group">
                <span class="input-icon">🎯</span>
                <span class="input-label">运动项目</span>
              </div>
              
              <!-- Element Plus Tree 组件 -->
              <div class="sport-tree-container">
                <el-tree
                  ref="sportTreeRef"
                  :data="treeData"
                  :props="treeProps"
                  :expand-on-click-node="false"
                  :highlight-current="true"
                  :check-strictly="true"
                  node-key="sportId"
                  class="sport-tree"
                  @node-click="handleNodeClick"
                >
                  <template #default="{ data }">
                    <div class="tree-node">
                      <span class="node-icon">
                        <el-icon v-if="data.isDirectory === '0'">
                          <Folder />
                        </el-icon>
                        <el-icon v-else>
                          <Trophy />
                        </el-icon>
                      </span>
                      <span class="node-label">{{ data.sportName }}</span>
                      <span v-if="data.isDirectory === '0'" class="folder-tag">分类</span>
                    </div>
                  </template>
                </el-tree>
              </div>
            </div>

            <!-- 核心数值输入 -->
            <div v-if="selectedSport" class="core-values">
              <div v-if="selectedSport.coreName1" class="input-row">
                <div class="input-group">
                  <span class="input-icon">📊</span>
                  <span class="input-label">{{ selectedSport.coreName1 }}</span>
                </div>
                <input
                  v-model="coreValue1"
                  type="number"
                  step="0.1"
                  :placeholder="`请输入${selectedSport.coreName1}`"
                  class="form-input"
                />
                <span class="unit-label">{{ selectedSport.coreUnit1 }}</span>
              </div>
              
              <div v-if="selectedSport.coreName2" class="input-row">
                <div class="input-group">
                  <span class="input-icon">⏱️</span>
                  <span class="input-label">{{ selectedSport.coreName2 }}</span>
                </div>
                <input
                  v-model="coreValue2"
                  type="number"
                  step="0.1"
                  :placeholder="`请输入${selectedSport.coreName2}`"
                  class="form-input"
                />
                <span class="unit-label">{{ selectedSport.coreUnit2 }}</span>
              </div>
            </div>

            <!-- 备注输入 -->
            <div class="input-row">
              <div class="input-group">
                <span class="input-icon">📝</span>
                <span class="input-label">备注</span>
              </div>
              <input
                v-model="sportNotes"
                type="text"
                placeholder="请输入备注（可选）"
                class="form-input"
              />
            </div>

            <!-- 保存按钮 -->
            <div class="input-row">
              <button
                @click="saveSportRecord"
                class="btn btn-save-sport"
                :class="gender"
                :disabled="!selectedSportId || (!coreValue1 && !coreValue2)"
              >
                保存运动记录
              </button>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, defineProps, defineEmits, ref, onMounted } from 'vue'
import axios from '@/utils/axios'
import { ElMessage, ElTree } from "element-plus"
import { Folder, Trophy } from '@element-plus/icons-vue'

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

// 运动记录数据
const sportTree = ref([])
const selectedSportId = ref(null)
const selectedSport = ref(null)
const coreValue1 = ref('')
const coreValue2 = ref('')
const sportNotes = ref('')
const sportTreeRef = ref()

// Tree 组件配置
const treeProps = {
  children: 'children',
  label: 'sportName'
}

// 处理树形数据
const treeData = computed(() => {
  return sportTree.value.map(category => ({
    ...category,
    children: category.children || []
  }))
})

// 格式化日期显示
const formattedDate = computed(() => {
  if (!props.date) return ''
  const year = props.date.getFullYear()
  const month = props.date.getMonth() + 1
  const day = props.date.getDate()
  return `${year}年${month}月${day}日`
})

// 获取运动定义树
const getSportTree = async () => {
  try {
    const response = await axios.get('/user/getSportTree')
    sportTree.value = response.sportTree || []
  } catch (error) {
    console.error('获取运动项目失败:', error)
    ElMessage({
      message: '获取运动项目失败',
      type: 'error',
    })
  }
}

// 处理树节点点击
const handleNodeClick = (data, node) => {
  // 如果是目录，则切换展开状态
  if (data.isDirectory === '0') {
    node.expanded = !node.expanded
    return
  }
  
  // 选择具体的运动项目
  selectedSportId.value = data.sportId
  selectedSport.value = data
  coreValue1.value = ''
  coreValue2.value = ''
}

// 保存体重
const saveWeight = async () => {
  if (!weight.value) return
  
  try {
    // 创建本地日期时间，避免时区转换问题
    const localDate = new Date(props.date.getFullYear(), props.date.getMonth(), props.date.getDate(), 12, 0, 0)
    
    await axios.post('/user/setUserInfo', {
      userId: props.userId,
      weight: parseFloat(weight.value),
      date: localDate.toISOString()
    })
    ElMessage({
      message: '体重保存成功',
      type: 'success',
    })
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage({
      message: '体重保存失败',
      type: 'error',
    })
  }
}

// 保存运动记录
const saveSportRecord = async () => {
  if (!selectedSportId.value || (!coreValue1.value && !coreValue2.value)) {
    ElMessage({
      message: '请选择运动项目并输入至少一个数值',
      type: 'warning',
    })
    return
  }
  
  try {
    // 创建本地日期时间，避免时区转换问题
    const localDate = new Date(props.date.getFullYear(), props.date.getMonth(), props.date.getDate(), 12, 0, 0)
    
    await axios.post('/user/addSportRecord', {
      userId: props.userId,
      sportId: selectedSportId.value,
      date: localDate.toISOString(),
      coreValue1: coreValue1.value ? parseFloat(coreValue1.value) : null,
      coreValue2: coreValue2.value ? parseFloat(coreValue2.value) : null,
      notes: sportNotes.value || null
    })
    
    ElMessage({
      message: '运动记录保存成功',
      type: 'success',
    })
    
    // 清空表单
    selectedSportId.value = null
    selectedSport.value = null
    coreValue1.value = ''
    coreValue2.value = ''
    sportNotes.value = ''
  } catch (error) {
    console.error('保存运动记录失败:', error)
    ElMessage({
      message: '运动记录保存失败',
      type: 'error',
    })
  }
}

// 关闭弹窗
const closeDialog = () => {
  weight.value = ''
  selectedSportId.value = null
  selectedSport.value = null
  coreValue1.value = ''
  coreValue2.value = ''
  sportNotes.value = ''
  emit('update:visible', false)
}

// 组件挂载时获取运动项目数据
onMounted(() => {
  getSportTree()
})
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
  margin: 0 0 24px 0;
}

// 运动记录表单样式
.sport-form {
  max-width: 500px;
  margin: 0;
}

.form-section {
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.08);
}

.section-icon {
  font-size: 18px;
}

.section-text {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
}

.core-values {
  margin: 16px 0;
  padding: 16px;
  background: rgba(0, 0, 0, 0.02);
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.unit-label {
  font-size: 14px;
  color: #666;
  margin-left: 8px;
  white-space: nowrap;
}

// 运动选择器样式
.sport-selection {
  margin-bottom: 20px;
}

.sport-tree-container {
  margin-top: 8px;
  border: 1px solid #e1e5e9;
  border-radius: 12px;
  background: #fff;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  max-height: 300px;
  overflow-y: auto;
}

.sport-tree {
  padding: 8px 0;
  
  :deep(.el-tree-node) {
    .el-tree-node__content {
      height: 40px;
      padding: 0 16px;
      border-radius: 6px;
      margin: 2px 8px;
      transition: all 0.2s ease;
      
      &:hover {
        background: #f5f7fa;
      }
    }
    
    &.is-current > .el-tree-node__content {
      background: #e3f2fd;
      color: #1976d2;
      font-weight: 600;
      
      .node-label {
        color: #1976d2;
      }
    }
    
    .el-tree-node__expand-icon {
      color: #666;
      font-size: 14px;
      
      &.is-leaf {
        color: transparent;
      }
    }
  }
}

.tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.node-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  color: #666;
  
  .el-icon {
    font-size: 16px;
  }
}

.node-label {
  flex: 1;
  font-size: 14px;
  color: #333;
  transition: all 0.2s ease;
}

.folder-tag {
  font-size: 11px;
  color: #999;
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 12px;
  font-weight: 500;
}

.btn-save-sport {
  width: 100%;
  margin-top: 8px;
  
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
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
  
  .sport-form {
    max-width: 100%;
  }
  
  .form-section {
    padding: 16px;
  }
  
  .input-row {
    flex-direction: column;
    gap: 8px;
  }
  
  .btn {
    width: 100%;
  }
  
  .sport-tree-container {
    border-radius: 8px;
    max-height: 250px;
  }
  
  .sport-tree {
    :deep(.el-tree-node) {
      .el-tree-node__content {
        height: 36px;
        padding: 0 12px;
        margin: 1px 6px;
      }
    }
  }
  
  .node-label {
    font-size: 13px;
  }
  
  .node-icon {
    width: 16px;
    height: 16px;
    
    .el-icon {
      font-size: 14px;
    }
  }
  
  .unit-label {
    margin-left: 0;
    margin-top: 4px;
  }
}
</style>