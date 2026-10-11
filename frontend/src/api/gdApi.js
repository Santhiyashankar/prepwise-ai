import axiosInstance from './axiosInstance.js'

export const gdApi = {
  start(payload) {
    return axiosInstance.post('/group-discussions/start', payload)
  },
  sendMessage(sessionId, content) {
    return axiosInstance.post(`/group-discussions/${sessionId}/messages`, { content })
  },
  getSession(sessionId) {
    return axiosInstance.get(`/group-discussions/${sessionId}`)
  },
  finish(sessionId) {
    return axiosInstance.post(`/group-discussions/${sessionId}/finish`)
  },
  getReport(sessionId) {
    return axiosInstance.get(`/group-discussions/${sessionId}/report`)
  },
  getHistory() {
    return axiosInstance.get('/group-discussions/history')
  },
  retry(sessionId) {
    return axiosInstance.post(`/group-discussions/${sessionId}/retry`)
  },
  delete(sessionId) {
    return axiosInstance.delete(`/group-discussions/${sessionId}`)
  },
}

export default gdApi
