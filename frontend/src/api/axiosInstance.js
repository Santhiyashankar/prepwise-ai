import axios from 'axios'
import tokenStorage from '../utils/tokenStorage.js'

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api'

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

axiosInstance.interceptors.request.use(
  (config) => {
    const accessToken = tokenStorage.getAccessToken()
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`
    }
    return config
  },
  (error) => Promise.reject(error),
)

let isRefreshing = false
let refreshSubscribers = []

function subscribeTokenRefresh(callback) {
  refreshSubscribers.push(callback)
}

function onRefreshed(newAccessToken) {
  refreshSubscribers.forEach((callback) => callback(newAccessToken))
  refreshSubscribers = []
}

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config
    const status = error.response?.status
    const isAuthEndpoint = originalRequest?.url?.includes('/auth/login') ||
      originalRequest?.url?.includes('/auth/register') ||
      originalRequest?.url?.includes('/auth/refresh-token')

    if (status === 401 && !originalRequest._retry && !isAuthEndpoint) {
      const refreshToken = tokenStorage.getRefreshToken()

      if (!refreshToken) {
        tokenStorage.clear()
        window.location.href = '/login'
        return Promise.reject(error)
      }

      if (isRefreshing) {
        return new Promise((resolve) => {
          subscribeTokenRefresh((newAccessToken) => {
            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
            resolve(axiosInstance(originalRequest))
          })
        })
      }

      originalRequest._retry = true
      isRefreshing = true

      try {
        const { data } = await axios.post(`${BASE_URL}/auth/refresh-token`, { refreshToken })
        const authData = data.data

        tokenStorage.setSession({
          accessToken: authData.accessToken,
          refreshToken: authData.refreshToken,
          user: authData.user,
        })

        isRefreshing = false
        onRefreshed(authData.accessToken)

        originalRequest.headers.Authorization = `Bearer ${authData.accessToken}`
        return axiosInstance(originalRequest)
      } catch (refreshError) {
        isRefreshing = false
        refreshSubscribers = []
        tokenStorage.clear()
        window.location.href = '/login'
        return Promise.reject(refreshError)
      }
    }

    return Promise.reject(error)
  },
)

export default axiosInstance