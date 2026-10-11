import axiosInstance from './axiosInstance.js'

export const authApi = {
  register(payload) {
    return axiosInstance.post('/auth/register', payload)
  },
  login(payload) {
    return axiosInstance.post('/auth/login', payload)
  },
  logout(refreshToken) {
    return axiosInstance.post('/auth/logout', { refreshToken })
  },
  refreshToken(refreshToken) {
    return axiosInstance.post('/auth/refresh-token', { refreshToken })
  },
  getCurrentUser() {
    return axiosInstance.get('/auth/me')
  },
  forgotPassword(email) {
    return axiosInstance.post('/auth/forgot-password', { email })
  },
  resetPassword(payload) {
    return axiosInstance.post('/auth/reset-password', payload)
  },
}

export default authApi