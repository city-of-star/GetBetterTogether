<template>
  <div class="couple-calendars">
    <div class="calendar-section male">
      <div class="section-header">
        <div class="gender-icon male">♂</div>
        <h2>他的日历</h2>
      </div>
      <Calendar
          gender="male"
          @date-selected="showDialog('male', $event)"
      />
    </div>

    <div class="couple-connector">
      <div class="heart-pulse">💕</div>
      <div class="connector-line"></div>
    </div>

    <div class="calendar-section female">
      <div class="section-header">
        <div class="gender-icon female">♀</div>
        <h2>她的日历</h2>
      </div>
      <Calendar
          gender="female"
          @date-selected="showDialog('female', $event)"
      />
    </div>
  </div>

  <!-- 日历对话框 -->
  <CalendarDialog
      v-model:visible="dialogVisible"
      :date="selectedDate"
      :gender="dialogGender"
  />
</template>

<script setup>
import Calendar from '@/components/Calendar.vue'
import CalendarDialog from "@/components/CalendarDialog.vue";
import { ref } from "vue";

const dialogVisible = ref(false);
const selectedDate = ref(null);
const dialogGender = ref('male');

const showDialog = (gender, date) => {
  dialogGender.value = gender;
  selectedDate.value = date;
  dialogVisible.value = true;
};
</script>

<style scoped lang="scss">
.couple-calendars {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 50px;
  margin-bottom: 40px;
}

.calendar-section {
  flex: 1;
  max-width: 450px;

  .section-header {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    margin-bottom: 20px;

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

// 响应式设计
@media (max-width: 968px) {
  .couple-calendars {
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
}

// 动画效果
@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}
</style>