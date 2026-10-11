import axiosInstance from './axiosInstance.js'

export const dashboardApi = {
  getSummary() {
    return axiosInstance.get('/dashboard/summary')
  },
  getRecentActivity() {
    return axiosInstance.get('/dashboard/recent-activity')
  },
  getProgress() {
    return axiosInstance.get('/dashboard/progress')
  },
  getTasks() {
    return axiosInstance.get('/dashboard/tasks')
  },
}

export default dashboardApi
