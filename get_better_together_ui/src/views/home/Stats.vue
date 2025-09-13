<template>
  <div class="stats-container">
    <div class="chart-header">
      <h3>体重变化趋势</h3>
      <el-select v-model="selectedMonth" @change="fetchWeightData" placeholder="选择月份" style="width: 120px;">
        <el-option
          v-for="month in months"
          :key="month.value"
          :label="month.label"
          :value="month.value">
        </el-option>
      </el-select>
    </div>

    <div class="chart-wrapper">
      <div ref="weightChart" class="weight-chart"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import axios from '@/utils/axios'

// 响应式数据
const selectedMonth = ref(new Date().getMonth() + 1) // 默认当前月份
const weightChart = ref(null)
let chartInstance = null

// 月份选项
const months = ref([
  { label: '1月', value: 1 },
  { label: '2月', value: 2 },
  { label: '3月', value: 3 },
  { label: '4月', value: 4 },
  { label: '5月', value: 5 },
  { label: '6月', value: 6 },
  { label: '7月', value: 7 },
  { label: '8月', value: 8 },
  { label: '9月', value: 9 },
  { label: '10月', value: 10 },
  { label: '11月', value: 11 },
  { label: '12月', value: 12 }
])

// 获取体重数据
const fetchWeightData = async () => {
  try {
    const response = await axios.post('/user/getUserWeightList', {
      month: selectedMonth.value
    })

    if (response && response.boyWeights && response.girlWeights) {
      updateChart(response.boyWeights, response.girlWeights)
    }
  } catch (error) {
    console.error('获取体重数据失败:', error)
  }
}

// 更新图表
const updateChart = (boyWeights, girlWeights) => {
  if (!chartInstance) return

  // 处理男生数据
  const boyData = boyWeights.map(item => ({
    date: new Date(item.date).getDate(),
    weight: item.weight
  })).sort((a, b) => a.date - b.date)

  // 处理女生数据
  const girlData = girlWeights.map(item => ({
    date: new Date(item.date).getDate(),
    weight: item.weight
  })).sort((a, b) => a.date - b.date)

  // 获取所有日期（去重并排序）
  const allDates = [...new Set([...boyData.map(item => item.date), ...girlData.map(item => item.date)])]
    .sort((a, b) => a - b)

  // 构建图表数据
  const boyWeightsArray = allDates.map(date => {
    const boyItem = boyData.find(item => item.date === date)
    return boyItem ? boyItem.weight : null
  })

  const girlWeightsArray = allDates.map(date => {
    const girlItem = girlData.find(item => item.date === date)
    return girlItem ? girlItem.weight : null
  })

  // 计算Y轴范围
  const allWeights = [...boyWeightsArray, ...girlWeightsArray].filter(weight => weight !== null)
  const minWeight = Math.min(...allWeights)
  const maxWeight = Math.max(...allWeights)
  const weightRange = maxWeight - minWeight

  // 如果体重变化范围很小（小于5kg），则使用更小的间隔
  const yAxisMin = Math.max(0, minWeight - (weightRange < 5 ? 0.5 : 2))
  const yAxisMax = maxWeight + (weightRange < 5 ? 0.5 : 2)

  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: function(params) {
        let result = `日期: ${params[0].axisValue}日<br/>`
        params.forEach(param => {
          if (param.value !== null) {
            result += `${param.seriesName}: ${param.value}kg<br/>`
          }
        })
        return result
      }
    },
    legend: {
      data: ['男生', '女生'],
      top: 30
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: allDates.map(date => `${date}`),
      axisLabel: {
        interval: 0,
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '体重(kg)',
      min: yAxisMin,
      max: yAxisMax,
      splitNumber: 8, // 设置分割段数，让刻度更精细
      axisLabel: {
        formatter: function(value) {
          return value.toFixed(1) + 'kg' // 显示一位小数
        }
      }
    },
    series: [
      {
        name: '男生',
        type: 'line',
        data: boyWeightsArray,
        smooth: true,
        lineStyle: {
          color: '#409EFF',
          width: 3
        },
        itemStyle: {
          color: '#409EFF'
        },
        symbol: 'circle',
        symbolSize: 6,
        connectNulls: false
      },
      {
        name: '女生',
        type: 'line',
        data: girlWeightsArray,
        smooth: true,
        lineStyle: {
          color: '#F56C6C',
          width: 3
        },
        itemStyle: {
          color: '#F56C6C'
        },
        symbol: 'circle',
        symbolSize: 6,
        connectNulls: false
      }
    ]
  }

  chartInstance.setOption(option)
}

// 初始化图表
const initChart = () => {
  nextTick(() => {
    if (weightChart.value) {
      chartInstance = echarts.init(weightChart.value)

      // 监听窗口大小变化
      window.addEventListener('resize', () => {
        if (chartInstance) {
          chartInstance.resize()
        }
      })

      // 初始加载数据
      fetchWeightData()
    }
  })
}

onMounted(() => {
  initChart()
})
</script>

<style scoped lang="scss">
.stats-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  h3 {
    margin: 0;
    color: #303133;
    font-size: 18px;
    font-weight: 600;
  }
}

.chart-wrapper {
  width: 100%;
  height: 400px;
}

.weight-chart {
  width: 100%;
  height: 100%;
  min-height: 400px;
}

// 响应式设计
@media (max-width: 768px) {
  .stats-container {
    padding: 15px;
  }

  .chart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;

    h3 {
      font-size: 16px;
    }
  }

  .chart-wrapper {
    height: 350px;
  }

  .weight-chart {
    min-height: 350px;
  }
}
</style>