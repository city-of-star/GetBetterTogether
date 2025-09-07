# 前端API使用指南

## 概述

本文档描述了前端如何与后端API进行交互，特别是JWT令牌验证相关的功能。

## 核心文件结构

```
src/
├── api/
│   └── user.js          # 用户相关API接口
├── utils/
│   ├── axios.js         # Axios配置和拦截器
│   └── auth.js          # 认证工具类
├── store/
│   └── modules/
│       └── user.js      # 用户状态管理
├── router/
│   └── index.js         # 路由配置和守卫
└── views/
    └── LoginView.vue    # 登录页面
```

## 认证工具类 (AuthUtils)

### 主要方法

#### `validateToken()`
验证当前token是否有效
```javascript
import AuthUtils from '@/utils/auth'

const result = await AuthUtils.validateToken()
if (result.isValid) {
  console.log('Token有效，用户ID:', result.userId)
} else {
  console.log('Token无效:', result.errorMessage)
}
```

#### `checkTokenValidity(redirectToLogin = true)`
检查token有效性并自动处理无效token
```javascript
const isValid = await AuthUtils.checkTokenValidity()
// 如果token无效且redirectToLogin为true，会自动跳转到登录页
```

#### `handleInvalidToken()`
处理无效token（清除数据并跳转登录页）
```javascript
await AuthUtils.handleInvalidToken()
```

#### `getToken()`
获取当前token
```javascript
const token = AuthUtils.getToken()
```

#### `setToken(token)`
设置token
```javascript
AuthUtils.setToken('your-jwt-token')
```

#### `clearToken()`
清除token
```javascript
AuthUtils.clearToken()
```

#### `isLoggedIn()`
检查是否已登录
```javascript
if (AuthUtils.isLoggedIn()) {
  // 用户已登录
}
```

## API接口 (userApi)

### 用户认证

#### 登录
```javascript
import { userApi } from '@/api/user'

const response = await userApi.login({
  username: 'testuser',
  password: 'password123'
})
// response包含token和用户信息
```

#### 注册
```javascript
const response = await userApi.register({
  username: 'newuser',
  password: 'password123',
  confirmPassword: 'password123'
})
```

#### 验证Token
```javascript
const response = await userApi.validateToken({
  token: 'your-jwt-token'
})
// 返回验证结果对象
```

### 用户信息

#### 获取用户信息
```javascript
const userInfo = await userApi.getUserInfo()
```

#### 更新用户信息
```javascript
const response = await userApi.updateUser({
  userId: 1,
  realName: '张三',
  age: 25,
  // ... 其他字段
})
```

#### 获取用户和恋人信息
```javascript
const userWithPartner = await userApi.getUserWithPartnerInfo(1)
```

## 状态管理 (Vuex)

### 用户模块

#### Actions

##### `login(data)`
用户登录
```javascript
await store.dispatch('user/login', {
  username: 'testuser',
  password: 'password123'
})
```

##### `register(data)`
用户注册
```javascript
await store.dispatch('user/register', {
  username: 'newuser',
  password: 'password123',
  confirmPassword: 'password123'
})
```

##### `isValidToken()`
验证token（已集成到AuthUtils中）
```javascript
const result = await store.dispatch('user/isValidToken')
```

##### `getUserInfo()`
获取用户信息
```javascript
const userInfo = await store.dispatch('user/getUserInfo')
```

##### `logout()`
用户登出
```javascript
await store.dispatch('user/logout')
```

#### Mutations

##### `SET_TOKEN(token)`
设置token
```javascript
store.commit('user/SET_TOKEN', 'your-jwt-token')
```

##### `SET_USERINFO(user)`
设置用户信息
```javascript
store.commit('user/SET_USERINFO', {
  userId: 1,
  realName: '张三',
  // ... 其他字段
})
```

##### `CLEAR_AUTH()`
清除认证信息
```javascript
store.commit('user/CLEAR_AUTH')
```

## 路由守卫

### 自动Token验证

路由守卫会自动验证token有效性：

```javascript
// 在router/index.js中
router.beforeEach(async (to, from, next) => {
  // 白名单路径直接放行
  if (whiteList.includes(to.path)) {
    next()
    return
  }

  // 检查token是否存在
  const token = store.state.user.token || localStorage.getItem('token')
  if (!token) {
    next(`/login?redirect=${to.path}`)
    return
  }

  // 验证token是否有效
  const isValid = await AuthUtils.checkTokenValidity(false)
  if (!isValid) {
    next(`/login?redirect=${to.path}`)
    return
  }
  
  next()
})
```

## 错误处理

### 网络错误
- 自动处理401未授权错误
- 自动处理网络超时
- 自动处理断网情况

### Token相关错误
- Token过期自动跳转登录页
- Token格式错误自动清除
- 用户不存在自动清除token

## 使用示例

### 完整的登录流程
```javascript
import { userApi } from '@/api/user'
import AuthUtils from '@/utils/auth'
import store from '@/store'

// 1. 用户登录
const loginData = {
  username: 'testuser',
  password: 'password123'
}

try {
  const response = await userApi.login(loginData)
  
  // 2. 设置token
  AuthUtils.setToken(response.token)
  
  // 3. 获取用户信息
  await store.dispatch('user/getUserInfo')
  
  // 4. 跳转到首页
  router.push('/')
  
} catch (error) {
  console.error('登录失败:', error)
}
```

### 定期验证Token
```javascript
// 在应用启动时或定时检查token有效性
setInterval(async () => {
  const isValid = await AuthUtils.checkTokenValidity()
  if (!isValid) {
    console.log('Token已过期，需要重新登录')
  }
}, 5 * 60 * 1000) // 每5分钟检查一次
```

## 注意事项

1. **Token存储**: Token同时存储在Vuex state和localStorage中
2. **自动清理**: 无效token会自动从localStorage中清除
3. **重定向**: 登录页面支持redirect参数，登录成功后自动跳转
4. **错误提示**: 使用Element Plus的ElMessage组件显示错误信息
5. **进度条**: 使用NProgress显示页面加载进度

## 配置说明

### Axios配置
- 基础URL: `http://localhost:3000`
- 超时时间: 15秒
- 自动添加Authorization头
- 统一错误处理

### Token配置
- 支持Bearer前缀
- 自动从localStorage读取
- 验证失败自动清除
