import axios from 'axios'

const service = axios.create({
    baseURL: 'http://localhost:3000',
    timeout: 15000,
    headers: {
        'Content-Type': 'application/json'
    }
})

service.interceptors.response.use(
    response => {
        return response.data
    },
)

export default service