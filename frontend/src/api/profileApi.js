import axiosInstance from './axiosInstance.js'

export const profileApi = {
  getProfile() {
    return axiosInstance.get('/profile')
  },
  updateProfile(payload) {
    return axiosInstance.put('/profile', payload)
  },
  uploadAvatar(file) {
    const formData = new FormData()
    formData.append('file', file)
    return axiosInstance.post('/profile/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
  changePassword(payload) {
    return axiosInstance.put('/profile/change-password', payload)
  },
  deleteAccount() {
    return axiosInstance.delete('/profile')
  },
  exportAccountData() {
    return axiosInstance.get('/profile/export')
  },
  logoutAllDevices() {
    return axiosInstance.post('/profile/logout-all-devices')
  },
  getNotificationPreferences() {
    return axiosInstance.get('/profile/notifications')
  },
  updateNotificationPreferences(payload) {
    return axiosInstance.put('/profile/notifications', payload)
  },
}

export default profileApi
