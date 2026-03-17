import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

request.interceptors.response.use(
  (response) => response,
  (error) => {
    const message =
      error?.response?.data?.message || error?.message || '请求失败'
    return Promise.reject(new Error(message))
  }
)

export const getEmployees = async (params) => {
  const response = await request.get('/employees', { params })
  return response.data
}

export const createEmployee = async (data) => {
  const response = await request.post('/employees', data)
  return response.data
}

export const updateEmployee = async (id, data) => {
  const response = await request.put(`/employees/${id}`, data)
  return response.data
}

export const deleteEmployee = async (id) => {
  const response = await request.delete(`/employees/${id}`)
  return response.data
}

