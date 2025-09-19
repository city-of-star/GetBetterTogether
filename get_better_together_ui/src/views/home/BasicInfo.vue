<template>
  <div class="basic-info-view">
    <div class="couple-info-container">
      <!-- 男性信息卡片 -->
      <div class="info-card male">
        <div class="card-header">
          <div class="gender-icon male">♂</div>
          <h2>他的信息</h2>
        </div>
        <div class="card-content">
          <div class="info-item">
            <span class="label">昵称</span>
            <span class="value">
              <input v-if="editingMale" v-model="maleForm.nickname" class="edit-input">
              <span v-else class="info">{{ maleInfo.nickname || '未设置' }}</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">性别</span>
            <span class="value">
              <select v-if="editingMale" v-model="maleForm.sex" class="edit-input">
                <option :value="1">男</option>
                <option :value="0">女</option>
              </select>
              <span v-else>{{ maleInfo.sex === 1 ? '男' : maleInfo.sex === 0 ? '女' : '男' }}</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">年龄</span>
            <span class="value">
              <input v-if="editingMale" v-model="maleForm.age" type="number" class="edit-input">
              <span v-else>{{ maleInfo.age || '--' }}岁</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">初始身高</span>
            <span class="value">
              <input v-if="editingMale" v-model="maleForm.initialHeight" type="number" class="edit-input">
              <span v-else>{{ maleInfo.initialHeight || '--' }}cm</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">初始体重</span>
            <span class="value">
              <input v-if="editingMale" v-model="maleForm.initialWeight" type="number" class="edit-input">
              <span v-else>{{ maleInfo.initialWeight || '--' }}kg</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">BMI</span>
            <span class="value">{{ calculateBMI(maleForm.initialHeight, maleForm.initialWeight) || '--' }}</span>
          </div>
        </div>
        <div class="card-footer">
          <template v-if="editingMale">
            <button class="save-button male" @click="saveMaleInfo">保存</button>
            <button class="cancel-button" @click="cancelEdit('male')">取消</button>
          </template>
          <button v-else class="edit-button male" @click="startEdit('male')">编辑信息</button>
        </div>
      </div>

      <!-- 中间装饰元素 -->
      <div class="couple-connector">
        <div class="heart-pulse">💕</div>
        <div class="connector-line"></div>
      </div>

      <!-- 女性信息卡片 -->
      <div class="info-card female">
        <div class="card-header">
          <div class="gender-icon female">♀</div>
          <h2>她的信息</h2>
        </div>
        <div class="card-content">
          <div class="info-item">
            <span class="label">昵称</span>
            <span class="value">
              <input v-if="editingFemale" v-model="femaleForm.nickname" class="edit-input">
              <span v-else>{{ femaleInfo.nickname || '未设置' }}</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">性别</span>
            <span class="value">
              <select v-if="editingFemale" v-model="femaleForm.sex" class="edit-input">
                <option :value="1">男</option>
                <option :value="0">女</option>
              </select>
              <span v-else>{{ femaleInfo.sex === 1 ? '男' : femaleInfo.sex === 0 ? '女' : '女' }}</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">年龄</span>
            <span class="value">
              <input v-if="editingFemale" v-model="femaleForm.age" type="number" class="edit-input">
              <span v-else>{{ femaleInfo.age || '--' }}岁</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">初始身高</span>
            <span class="value">
              <input v-if="editingFemale" v-model="femaleForm.initialHeight" type="number" class="edit-input">
              <span v-else>{{ femaleInfo.initialHeight || '--' }}cm</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">初始体重</span>
            <span class="value">
              <input v-if="editingFemale" v-model="femaleForm.initialWeight" type="number" class="edit-input">
              <span v-else>{{ femaleInfo.initialWeight || '--' }}kg</span>
            </span>
          </div>
          <div class="info-item">
            <span class="label">BMI</span>
            <span class="value">{{ calculateBMI(femaleForm.initialHeight, femaleForm.initialWeight) || '--' }}</span>
          </div>
        </div>
        <div class="card-footer">
          <template v-if="editingFemale">
            <button class="save-button female" @click="saveFemaleInfo">保存</button>
            <button class="cancel-button" @click="cancelEdit('female')">取消</button>
          </template>
          <button v-else class="edit-button female" @click="startEdit('female')">编辑信息</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useStore } from 'vuex';
import axios from '@/utils/axios.js'

const store = useStore();

const maleInfo = computed(() => store.state.user.user1 || {});
const femaleInfo = computed(() => store.state.user.user2 || {});

const editingMale = ref(false);
const editingFemale = ref(false);
const maleForm = ref({});
const femaleForm = ref({});

const calculateBMI = (height, weight) => {
  if (!height || !weight || height <= 0 || weight <= 0) return null;
  const heightInMeter = height / 100;
  return (weight / (heightInMeter * heightInMeter)).toFixed(1);
};

const startEdit = (type) => {
  if (type === 'male') {
    maleForm.value = { ...maleInfo.value };
    editingMale.value = true;
  } else {
    femaleForm.value = { ...femaleInfo.value };
    editingFemale.value = true;
  }
};

const cancelEdit = (type) => {
  if (type === 'male') {
    editingMale.value = false;
  } else {
    editingFemale.value = false;
  }
};

const updateUser = async (userId, formData) => {
  try {
    await axios.post('/user/update', { userId, ...formData });
    await fetchUserInfo();
    return true;
  } catch (error) {
    console.error('更新用户信息失败:', error);
    return false;
  }
};

const saveMaleInfo = async () => {
  await updateUser(maleInfo.value.userId, maleForm.value);
  editingMale.value = false;
};

const saveFemaleInfo = async () => {
  await updateUser(femaleInfo.value.userId, femaleForm.value);
  editingFemale.value = false;
};

const fetchUserInfo = async () => {
  await store.dispatch('user/getUserInfo');
};

fetchUserInfo();
</script>

<style scoped lang="scss">
.basic-info-view {
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.couple-info-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 50px;
  max-width: 1200px;
  width: 100%;
}

.info-card {
  flex: 1;
  max-width: 450px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.1);
  padding: 28px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.2);

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 5px;
    border-radius: 20px 20px 0 0;
  }

  &.male {
    &::before {
      background: linear-gradient(90deg, #a7e1f8, #64b5f6, #42a5f5);
    }

    &::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: radial-gradient(circle at 50% 0%, rgba(100, 181, 246, 0.05) 0%, transparent 50%);
      pointer-events: none;
    }
  }

  &.female {
    &::before {
      background: linear-gradient(90deg, #f8c8dc, #f48fb1, #f06292);
    }

    &::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: radial-gradient(circle at 50% 0%, rgba(244, 143, 177, 0.05) 0%, transparent 50%);
      pointer-events: none;
    }
  }
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 24px;
  position: relative;
  z-index: 2;

  .gender-icon {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.2rem;
    font-weight: bold;

    &.male {
      background: linear-gradient(135deg, #e3f2fd, #bbdefb);
      color: #1976d2;
      box-shadow: 0 4px 12px rgba(100, 181, 246, 0.3);
    }

    &.female {
      background: linear-gradient(135deg, #fce4ec, #f8bbd9);
      color: #d81b60;
      box-shadow: 0 4px 12px rgba(244, 143, 177, 0.3);
    }
  }

  h2 {
    margin: 0;
    font-weight: 600;
    color: #2c3e50;
    font-size: 1.4rem;
  }
}

.card-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
  position: relative;
  z-index: 2;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .label {
    font-weight: 500;
    color: #5a6c7d;
  }

  .value {
    font-weight: 600;
    color: #2c3e50;
  }
}

.card-footer {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  position: relative;
  z-index: 2;
}

.edit-button {
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;

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

  &.male {
    background: linear-gradient(135deg, #64b5f6, #42a5f5);
    color: white;
    box-shadow: 0 4px 16px rgba(100, 181, 246, 0.4);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(100, 181, 246, 0.6);
    }
  }

  &.female {
    background: linear-gradient(135deg, #f48fb1, #f06292);
    color: white;
    box-shadow: 0 4px 16px rgba(244, 143, 177, 0.4);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(244, 143, 177, 0.6);
    }
  }
}

.couple-connector {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding-top: 100px;
  position: relative;

  .heart-pulse {
    font-size: 2.5rem;
    color: #ff6b9d;
    animation: pulse 2s infinite;
    z-index: 2;
    position: relative;
  }

  .connector-line {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 2px;
    height: 60px;
    background: linear-gradient(to bottom, transparent, #ff6b9d, transparent);
    opacity: 0.6;
  }
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.info-card {
  animation: fadeIn 0.6s ease;
}

// 响应式设计
@media (max-width: 968px) {
  .couple-info-container {
    flex-direction: column;
    align-items: center;
    gap: 30px;
  }

  .couple-connector {
    padding: 20px 0;

    .connector-line {
      width: 60px;
      height: 2px;
      background: linear-gradient(to right, transparent, #ff6b9d, transparent);
    }
  }

  .info-card {
    max-width: 100%;
    width: 100%;
  }
}

@media (max-width: 480px) {
  .basic-info-view {
    padding: 10px;
  }

  .info-card {
    padding: 20px;
  }

  .card-header {
    h2 {
      font-size: 1.2rem;
    }
  }

  .info-item {
    padding: 10px 12px;
    font-size: 0.9rem;
  }

  .edit-button {
    padding: 10px 20px;
    font-size: 0.9rem;
  }
}

.edit-input {
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  width: 120px;
  font-size: inherit;
  background: rgba(255, 255, 255, 0.9);

  &:focus {
    outline: none;
    border-color: #42a5f5;
    box-shadow: 0 0 0 2px rgba(66, 165, 245, 0.2);
  }
}

select.edit-input {
  width: 130px;
}

.save-button {
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-right: 10px;

  &.male {
    background: linear-gradient(135deg, #64b5f6, #42a5f5);
    color: white;
    box-shadow: 0 4px 16px rgba(100, 181, 246, 0.4);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(100, 181, 246, 0.6);
    }
  }

  &.female {
    background: linear-gradient(135deg, #f48fb1, #f06292);
    color: white;
    box-shadow: 0 4px 16px rgba(244, 143, 177, 0.4);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(244, 143, 177, 0.6);
    }
  }
}

.cancel-button {
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f5f5f5;
  color: #666;

  &:hover {
    background: #eee;
    transform: translateY(-2px);
  }
}

.info-item {
  .value {
    min-width: 120px;
    text-align: right;
  }
}
</style>