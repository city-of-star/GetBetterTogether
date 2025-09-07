<template>
  <div class="calendar" :class="gender">
    <!-- 日历头部 - 月份导航 -->
    <div class="calendar-header">
      <button @click="prevMonth" class="nav-button" :class="gender">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16">
          <path fill-rule="evenodd" d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"/>
        </svg>
      </button>
      <div class="month-display">
        <h2>{{ formattedCurrentMonth }}</h2>
        <div class="month-indicator" :class="gender"></div>
      </div>
      <button @click="nextMonth" class="nav-button" :class="gender">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16">
          <path fill-rule="evenodd" d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"/>
        </svg>
      </button>
    </div>

    <!-- 星期标题 -->
    <div class="weekdays">
      <div v-for="day in weekdays" :key="day" class="weekday">{{ day }}</div>
    </div>

    <!-- 日历主体 -->
    <div class="days-grid">
      <div
          v-for="(day, index) in calendarDays"
          :key="index"
          class="day"
          :class="{
          'current-month': day.isCurrentMonth,
          'today': isToday(day.date),
          'selected': isSelected(day.date),
          [gender]: true
        }"
          @click="selectDate(day)"
      >
        <span class="date-number">{{ day.date.getDate() }}</span>
        <div v-if="hasEvents(day.date)" class="event-indicator" :class="gender"></div>
        <div v-if="isToday(day.date)" class="today-glow" :class="gender"></div>
      </div>
    </div>
    
    <!-- 日历底部装饰 -->
    <div class="calendar-footer">
      <div class="legend">
        <div class="legend-item">
          <div class="legend-dot today" :class="gender"></div>
          <span>今天</span>
        </div>
        <div class="legend-item">
          <div class="legend-dot event" :class="gender"></div>
          <span>有事件</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, defineProps, defineEmits } from 'vue';

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  gender: {
    type: String,
    default: 'male',
    validator: value => ['male', 'female'].includes(value)
  }
});

const emit = defineEmits(['date-selected']);

const today = new Date();
const weekdays = ['日', '一', '二', '三', '四', '五', '六'];

// 当前展示的月份和年份
const currentMonth = ref(today.getMonth());
const currentYear = ref(today.getFullYear());

// 选中的日期
const selectedDate = ref(null);

// 模拟一些事件数据
const events = ref([
  new Date(currentYear.value, currentMonth.value, 15),
  new Date(currentYear.value, currentMonth.value, 20),
  new Date(currentYear.value, currentMonth.value, 25)
]);

// 格式化显示的月份（例：2025年8月）
const formattedCurrentMonth = computed(() => {
  return `${currentYear.value}年${currentMonth.value + 1}月`;
});

// 判断日期是否有事件
const hasEvents = (date) => {
  return events.value.some(eventDate =>
      eventDate.toDateString() === date.toDateString()
  );
};

// 判断是否是今天
const isToday = (date) => {
  return date.toDateString() === today.toDateString();
};

// 判断是否被选中
const isSelected = (date) => {
  return selectedDate.value && date.toDateString() === selectedDate.value.toDateString();
};

// 选择日期
const selectDate = (day) => {
  if (day.isCurrentMonth) {
    selectedDate.value = day.date;
    emit('date-selected', day.date);
  }
};

// 生成日历天数数组
const calendarDays = computed(() => {
  const days = [];

  // 当月第一天
  const firstDayOfMonth = new Date(currentYear.value, currentMonth.value, 1);
  // 当月最后一天
  const lastDayOfMonth = new Date(currentYear.value, currentMonth.value + 1, 0);
  // 当月第一天是星期几（0代表星期日）
  const firstDayOfWeek = firstDayOfMonth.getDay();

  // 上个月的最后几天（需要显示在当前月的日历中）
  const daysFromPrevMonth = firstDayOfWeek;
  for (let i = daysFromPrevMonth; i > 0; i--) {
    const date = new Date(currentYear.value, currentMonth.value, -i + 1);
    days.push({ date, isCurrentMonth: false });
  }

  // 当前月的所有天数
  const daysInMonth = lastDayOfMonth.getDate();
  for (let i = 1; i <= daysInMonth; i++) {
    const date = new Date(currentYear.value, currentMonth.value, i);
    days.push({ date, isCurrentMonth: true });
  }

  // 下个月的前几天（补全日历视图）
  const daysRemaining = 42 - days.length; // 6行x7列=42个日期单元
  for (let i = 1; i <= daysRemaining; i++) {
    const date = new Date(currentYear.value, currentMonth.value + 1, i);
    days.push({ date, isCurrentMonth: false });
  }

  return days;
});

// 导航到上个月
const prevMonth = () => {
  if (currentMonth.value === 0) {
    currentMonth.value = 11;
    currentYear.value--;
  } else {
    currentMonth.value--;
  }
};

// 导航到下个月
const nextMonth = () => {
  if (currentMonth.value === 11) {
    currentMonth.value = 0;
    currentYear.value++;
  } else {
    currentMonth.value++;
  }
};
</script>

<style scoped lang="scss">
.calendar {
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  max-width: 420px;
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

  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: radial-gradient(circle at 50% 0%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
    pointer-events: none;
  }

  &.male {
    &::before {
      background: linear-gradient(90deg, #a7e1f8, #64b5f6, #42a5f5);
    }
    
    &::after {
      background: radial-gradient(circle at 50% 0%, rgba(100, 181, 246, 0.05) 0%, transparent 50%);
    }
  }

  &.female {
    &::before {
      background: linear-gradient(90deg, #f8c8dc, #f48fb1, #f06292);
    }
    
    &::after {
      background: radial-gradient(circle at 50% 0%, rgba(244, 143, 177, 0.05) 0%, transparent 50%);
    }
  }
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  position: relative;
  z-index: 2;
}

.month-display {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  position: relative;
  
  h2 {
    margin: 0;
    font-size: 1.4rem;
    font-weight: 700;
    color: #2c3e50;
    background: linear-gradient(135deg, #2c3e50, #34495e);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
  
  .month-indicator {
    width: 40px;
    height: 3px;
    border-radius: 2px;
    transition: all 0.3s ease;
    
    &.male {
      background: linear-gradient(90deg, #64b5f6, #42a5f5);
      box-shadow: 0 2px 8px rgba(100, 181, 246, 0.3);
    }
    
    &.female {
      background: linear-gradient(90deg, #f48fb1, #f06292);
      box-shadow: 0 2px 8px rgba(244, 143, 177, 0.3);
    }
  }
}

.nav-button {
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(0, 0, 0, 0.1);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    border-radius: 50%;
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  &.male {
    color: #1976d2;
    
    &::before {
      background: linear-gradient(135deg, rgba(100, 181, 246, 0.1), rgba(66, 165, 245, 0.1));
    }
    
    &:hover {
      transform: scale(1.1);
      box-shadow: 0 4px 16px rgba(100, 181, 246, 0.3);
      
      &::before {
        opacity: 1;
      }
    }
  }

  &.female {
    color: #d81b60;
    
    &::before {
      background: linear-gradient(135deg, rgba(244, 143, 177, 0.1), rgba(240, 98, 146, 0.1));
    }
    
    &:hover {
      transform: scale(1.1);
      box-shadow: 0 4px 16px rgba(244, 143, 177, 0.3);
      
      &::before {
        opacity: 1;
      }
    }
  }
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  margin-bottom: 16px;
  position: relative;
  z-index: 2;
}

.weekday {
  text-align: center;
  font-weight: 600;
  color: #5a6c7d;
  padding: 12px 0;
  font-size: 0.9rem;
  position: relative;
  
  &::after {
    content: '';
    position: absolute;
    bottom: 4px;
    left: 50%;
    transform: translateX(-50%);
    width: 20px;
    height: 2px;
    background: linear-gradient(90deg, transparent, #e0e0e0, transparent);
    border-radius: 1px;
  }
}

.days-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8px;
  position: relative;
  z-index: 2;
}

.day {
  position: relative;
  text-align: center;
  padding: 12px 0;
  border-radius: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  height: 44px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    border-radius: 14px;
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  &.current-month {
    color: #2c3e50;
  }

  &:not(.current-month) {
    color: #bdc3c7;
    opacity: 0.6;
  }

  &.male:hover:not(.selected) {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(100, 181, 246, 0.2);
    
    &::before {
      background: linear-gradient(135deg, rgba(100, 181, 246, 0.1), rgba(66, 165, 245, 0.1));
      opacity: 1;
    }
  }

  &.female:hover:not(.selected) {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(244, 143, 177, 0.2);
    
    &::before {
      background: linear-gradient(135deg, rgba(244, 143, 177, 0.1), rgba(240, 98, 146, 0.1));
      opacity: 1;
    }
  }
}

.day.today {
  font-weight: 700;
  position: relative;
  
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

.day.selected {
  font-weight: 700;
  transform: scale(1.05);
  z-index: 3;
  
  &.male {
    background: linear-gradient(135deg, #64b5f6, #42a5f5);
    color: white;
    box-shadow: 0 6px 20px rgba(100, 181, 246, 0.4);
  }
  
  &.female {
    background: linear-gradient(135deg, #f48fb1, #f06292);
    color: white;
    box-shadow: 0 6px 20px rgba(244, 143, 177, 0.4);
  }
}

.date-number {
  z-index: 2;
  position: relative;
}

.today-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
  border-radius: 14px;
  z-index: 1;
  animation: pulse 2s infinite;
  
  &.male {
    background: radial-gradient(circle, rgba(100, 181, 246, 0.2) 0%, transparent 70%);
  }
  
  &.female {
    background: radial-gradient(circle, rgba(244, 143, 177, 0.2) 0%, transparent 70%);
  }
}

.event-indicator {
  position: absolute;
  bottom: 6px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  z-index: 2;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  
  &.male {
    background: linear-gradient(135deg, #1976d2, #1565c0);
  }
  
  &.female {
    background: linear-gradient(135deg, #d81b60, #c2185b);
  }
}

.calendar-footer {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  position: relative;
  z-index: 2;
}

.legend {
  display: flex;
  justify-content: center;
  gap: 20px;
  flex-wrap: wrap;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.8rem;
  color: #5a6c7d;
  font-weight: 500;
}

.legend-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  
  &.today {
    &.male {
      background: linear-gradient(135deg, #64b5f6, #42a5f5);
    }
    
    &.female {
      background: linear-gradient(135deg, #f48fb1, #f06292);
    }
  }
  
  &.event {
    &.male {
      background: linear-gradient(135deg, #1976d2, #1565c0);
    }
    
    &.female {
      background: linear-gradient(135deg, #d81b60, #c2185b);
    }
  }
}

// 动画效果
@keyframes fadeIn {
  from { 
    opacity: 0; 
    transform: translateY(10px); 
  }
  to { 
    opacity: 1; 
    transform: translateY(0); 
  }
}

@keyframes pulse {
  0%, 100% { 
    transform: translate(-50%, -50%) scale(1); 
    opacity: 0.8;
  }
  50% { 
    transform: translate(-50%, -50%) scale(1.1); 
    opacity: 0.4;
  }
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.day {
  animation: fadeIn 0.4s ease;
}

.calendar-header {
  animation: slideIn 0.6s ease;
}

.weekdays {
  animation: slideIn 0.8s ease;
}

.calendar-footer {
  animation: slideIn 1s ease;
}

// 响应式设计
@media (max-width: 480px) {
  .calendar {
    max-width: 100%;
    padding: 20px;
    margin: 0 10px;
  }
  
  .month-display h2 {
    font-size: 1.2rem;
  }
  
  .nav-button {
    width: 36px;
    height: 36px;
  }
  
  .day {
    height: 40px;
    padding: 10px 0;
  }
  
  .legend {
    gap: 16px;
  }
  
  .legend-item {
    font-size: 0.75rem;
  }
}
</style>