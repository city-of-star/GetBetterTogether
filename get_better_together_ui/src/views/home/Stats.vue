<template>
  <div class="stats-container">
    <!-- 月份选择器 -->
    <div class="month-selector">
      <el-select v-model="selectedMonth" @change="fetchAllData" placeholder="选择月份" style="width: 120px;">
        <el-option
          v-for="month in months"
          :key="month.value"
          :label="month.label"
          :value="month.value">
        </el-option>
      </el-select>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <!-- 体重变化趋势图 -->
      <div class="chart-card">
        <div class="chart-header">
          <h3>体重变化趋势</h3>
        </div>
        <div class="chart-wrapper">
          <div ref="weightChart" class="chart"></div>
        </div>
      </div>

      <!-- 运动记录统计图 -->
      <div class="chart-card">
        <div class="chart-header">
          <h3>运动记录统计</h3>
        </div>
        <div class="chart-wrapper">
          <div ref="sportChart" class="chart"></div>
        </div>
      </div>
    </div>

    <!-- 运动记录详情展示 -->
    <div class="sport-records-section">
      <div class="section-header">
        <h3>运动记录详情</h3>
        <el-tabs v-model="activeTab" @tab-change="handleTabChange">
          <el-tab-pane label="男生记录" name="boy"></el-tab-pane>
          <el-tab-pane label="女生记录" name="girl"></el-tab-pane>
        </el-tabs>
      </div>
      
      <div class="records-container">
        <!-- 按运动类型分组的卡片式展示 -->
        <div v-if="groupedSportRecords.length > 0" class="sport-cards">
          <div 
            v-for="group in groupedSportRecords" 
            :key="group.sportId" 
            class="sport-card"
          >
            <div class="sport-card-header">
              <div class="sport-info">
                <h4 class="sport-name">{{ group.sportName }}</h4>
                <span class="record-count">{{ group.records.length }}条记录</span>
              </div>
              <div class="sport-fields">
                <span v-if="group.coreName1" class="field-label">{{ group.coreName1 }}</span>
                <span v-if="group.coreName2" class="field-label">{{ group.coreName2 }}</span>
              </div>
            </div>
            
            <div class="records-list">
              <div 
                v-for="record in group.records" 
                :key="record.recordId" 
                class="record-item"
              >
                <div class="record-date">{{ formatDate(record.date) }}</div>
                <div class="record-values">
                  <div v-if="record.coreValue1" class="value-item">
                    <span class="value">{{ record.coreValue1 }}</span>
                    <span class="unit">{{ group.coreUnit1 }}</span>
                  </div>
                  <div v-if="record.coreValue2" class="value-item">
                    <span class="value">{{ record.coreValue2 }}</span>
                    <span class="unit">{{ group.coreUnit2 }}</span>
                  </div>
                </div>
                <div v-if="record.notes" class="record-notes">{{ record.notes }}</div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 空状态 -->
        <div v-else class="empty-state">
          <div class="empty-icon">🏃‍♂️</div>
          <p>暂无运动记录</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import axios from '@/utils/axios'

// 响应式数据
const selectedMonth = ref(new Date().getMonth() + 1) // 默认当前月份
const weightChart = ref(null)
const sportChart = ref(null)
let weightChartInstance = null
let sportChartInstance = null
const sportMap = ref({}) // 存储运动项目ID到名称的映射
const sportDetailMap = ref({}) // 存储运动项目详细信息
const boySportRecords = ref([]) // 男生运动记录
const girlSportRecords = ref([]) // 女生运动记录
const activeTab = ref('boy') // 当前选中的标签页
const tableLoading = ref(false) // 表格加载状态

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

// 获取所有数据
const fetchAllData = async () => {
  await Promise.all([
    fetchSportTree(),
    fetchWeightData(),
    fetchSportData()
  ])
}

// 获取运动项目树
const fetchSportTree = async () => {
  try {
    const response = await axios.get('/user/getSportTree')
    if (response && response.sportTree) {
      // 构建运动项目ID到名称的映射
      const buildSportMap = (nodes) => {
        nodes.forEach(node => {
          if (node.isDirectory === '1') { // 只映射非目录的运动项目
            sportMap.value[node.sportId] = node.sportName
            sportDetailMap.value[node.sportId] = {
              sportName: node.sportName,
              coreName1: node.coreName1,
              coreName2: node.coreName2,
              coreUnit1: node.coreUnit1,
              coreUnit2: node.coreUnit2
            }
          }
          if (node.children && node.children.length > 0) {
            buildSportMap(node.children)
          }
        })
      }
      buildSportMap(response.sportTree)
    }
  } catch (error) {
    console.error('获取运动项目树失败:', error)
  }
}

// 获取体重数据
const fetchWeightData = async () => {
  try {
    const response = await axios.post('/user/getUserWeightList', {
      month: selectedMonth.value
    })

    if (response && response.boyWeights && response.girlWeights) {
      updateWeightChart(response.boyWeights, response.girlWeights)
    }
  } catch (error) {
    console.error('获取体重数据失败:', error)
  }
}

// 获取运动记录数据
const fetchSportData = async () => {
  try {
    tableLoading.value = true
    const response = await axios.post('/user/getUserSportRecordList', {
      month: selectedMonth.value
    })

    if (response && response.boySportRecords && response.girlSportRecords) {
      // 保存原始数据
      boySportRecords.value = response.boySportRecords.map(record => ({
        ...record,
        sportName: sportMap.value[record.sportId] || `运动${record.sportId}`
      }))
      girlSportRecords.value = response.girlSportRecords.map(record => ({
        ...record,
        sportName: sportMap.value[record.sportId] || `运动${record.sportId}`
      }))
      
      updateSportChart(response.boySportRecords, response.girlSportRecords)
    }
  } catch (error) {
    console.error('获取运动记录数据失败:', error)
  } finally {
    tableLoading.value = false
  }
}


// 更新体重图表
const updateWeightChart = (boyWeights, girlWeights) => {
  if (!weightChartInstance) return

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

  weightChartInstance.setOption(option)
}

// 更新运动记录图表
const updateSportChart = (boySportRecords, girlSportRecords) => {
  if (!sportChartInstance) return

  // 统计运动类型
  const sportTypeCount = {}
  
  // 处理男生运动记录
  boySportRecords.forEach(record => {
    const sportId = record.sportId
    if (!sportTypeCount[sportId]) {
      sportTypeCount[sportId] = { 
        boy: 0, 
        girl: 0, 
        sportName: sportMap.value[sportId] || `运动${sportId}` 
      }
    }
    sportTypeCount[sportId].boy++
  })

  // 处理女生运动记录
  girlSportRecords.forEach(record => {
    const sportId = record.sportId
    if (!sportTypeCount[sportId]) {
      sportTypeCount[sportId] = { 
        boy: 0, 
        girl: 0, 
        sportName: sportMap.value[sportId] || `运动${sportId}` 
      }
    }
    sportTypeCount[sportId].girl++
  })

  // 构建图表数据
  const sportTypes = Object.keys(sportTypeCount)
  const boyData = sportTypes.map(sportId => sportTypeCount[sportId].boy)
  const girlData = sportTypes.map(sportId => sportTypeCount[sportId].girl)
  const sportNames = sportTypes.map(sportId => sportTypeCount[sportId].sportName)

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: function(params) {
        let result = `运动类型: ${params[0].axisValue}<br/>`
        params.forEach(param => {
          if (param.value > 0) {
            result += `${param.seriesName}: ${param.value}次<br/>`
          }
        })
        return result
      }
    },
    legend: {
      data: ['男生', '女生'],
      top: 10
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
      data: sportNames,
      axisLabel: {
        interval: 0,
        rotate: 45,
        fontSize: 10
      }
    },
    yAxis: {
      type: 'value',
      name: '运动次数',
      axisLabel: {
        formatter: function(value) {
          return value + '次'
        }
      }
    },
    series: [
      {
        name: '男生',
        type: 'bar',
        data: boyData,
        itemStyle: {
          color: '#409EFF'
        }
      },
      {
        name: '女生',
        type: 'bar',
        data: girlData,
        itemStyle: {
          color: '#F56C6C'
        }
      }
    ]
  }

  sportChartInstance.setOption(option)
}

// 计算属性：当前显示的运动记录
const currentSportRecords = computed(() => {
  return activeTab.value === 'boy' ? boySportRecords.value : girlSportRecords.value
})

// 计算属性：按运动类型分组的记录
const groupedSportRecords = computed(() => {
  const records = currentSportRecords.value
  if (!records || records.length === 0) return []
  
  // 按运动ID分组
  const grouped = {}
  records.forEach(record => {
    const sportId = record.sportId
    if (!grouped[sportId]) {
      const sportDetail = sportDetailMap.value[sportId]
      grouped[sportId] = {
        sportId: sportId,
        sportName: record.sportName,
        coreName1: sportDetail?.coreName1,
        coreName2: sportDetail?.coreName2,
        coreUnit1: sportDetail?.coreUnit1,
        coreUnit2: sportDetail?.coreUnit2,
        records: []
      }
    }
    grouped[sportId].records.push(record)
  })
  
  // 转换为数组并按记录数量排序
  return Object.values(grouped).sort((a, b) => b.records.length - a.records.length)
})

// 标签页切换处理
const handleTabChange = (tabName) => {
  activeTab.value = tabName
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return `${date.getMonth() + 1}/${date.getDate()}`
}

// 初始化图表
const initCharts = () => {
  nextTick(() => {
    // 初始化体重图表
    if (weightChart.value) {
      weightChartInstance = echarts.init(weightChart.value)
    }

    // 初始化运动记录图表
    if (sportChart.value) {
      sportChartInstance = echarts.init(sportChart.value)
    }

    // 监听窗口大小变化
    window.addEventListener('resize', () => {
      if (weightChartInstance) {
        weightChartInstance.resize()
      }
      if (sportChartInstance) {
        sportChartInstance.resize()
      }
    })

    // 初始加载数据
    fetchAllData()
  })
}

onMounted(() => {
  initCharts()
})
</script>

<style scoped lang="scss">
.stats-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.month-selector {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.chart-card {
  background: #fafafa;
  border-radius: 8px;
  padding: 15px;
  border: 1px solid #e4e7ed;
}

.chart-header {
  margin-bottom: 15px;

  h3 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
    text-align: center;
  }
}

.chart-wrapper {
  width: 100%;
  height: 300px;
}

.chart {
  width: 100%;
  height: 100%;
  min-height: 300px;
}

// 响应式设计
@media (max-width: 1200px) {
  .charts-grid {
    grid-template-columns: 1fr;
    gap: 15px;
  }
}

@media (max-width: 768px) {
  .stats-container {
    padding: 15px;
  }

  .chart-card {
    padding: 10px;
  }

  .chart-wrapper {
    height: 250px;
  }

  .chart {
    min-height: 250px;
  }

  .chart-header h3 {
    font-size: 14px;
  }
}

// 运动记录详情部分样式
.sport-records-section {
  margin-top: 20px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.section-header {
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

.records-container {
  min-height: 200px;
}

.sport-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.sport-card {
  background: #fafafa;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  
  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.sport-card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e4e7ed;
}

.sport-info {
  .sport-name {
    margin: 0 0 4px 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
  }
  
  .record-count {
    color: #909399;
    font-size: 12px;
    background: #f0f2f5;
    padding: 2px 8px;
    border-radius: 12px;
  }
}

.sport-fields {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: flex-end;
  
  .field-label {
    font-size: 12px;
    color: #606266;
    background: #e8f4fd;
    padding: 2px 6px;
    border-radius: 4px;
    white-space: nowrap;
  }
}

.records-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.record-item {
  background: #fff;
  border-radius: 8px;
  padding: 12px;
  border: 1px solid #f0f0f0;
  transition: all 0.2s ease;
  
  &:hover {
    border-color: #409eff;
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
  }
}

.record-date {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
  font-weight: 500;
}

.record-values {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
}

.value-item {
  display: flex;
  align-items: center;
  gap: 4px;
  
  .value {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
  
  .unit {
    font-size: 12px;
    color: #909399;
  }
}

.record-notes {
  font-size: 12px;
  color: #606266;
  background: #f8f9fa;
  padding: 6px 8px;
  border-radius: 4px;
  border-left: 3px solid #409eff;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  color: #909399;
  
  .empty-icon {
    font-size: 48px;
    margin-bottom: 16px;
    opacity: 0.6;
  }
  
  p {
    margin: 0;
    font-size: 14px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .sport-records-section {
    padding: 15px;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;

    h3 {
      font-size: 16px;
    }
  }

  .sport-cards {
    grid-template-columns: 1fr;
    gap: 15px;
  }

  .sport-card {
    padding: 12px;
  }

  .sport-card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .sport-fields {
    align-items: flex-start;
    flex-direction: row;
    gap: 8px;
  }

  .record-values {
    flex-direction: column;
    gap: 8px;
  }
}
</style>