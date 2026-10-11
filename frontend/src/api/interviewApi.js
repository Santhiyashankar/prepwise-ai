import axiosInstance from './axiosInstance.js'

export const interviewApi = {
  start(payload) {
    return axiosInstance.post('/interviews/start', payload)
  },
  submitAnswer(sessionId, questionNumber, payload) {
    return axiosInstance.post(`/interviews/${sessionId}/questions/${questionNumber}/answer`, payload)
  },
  getSession(sessionId) {
    return axiosInstance.get(`/interviews/${sessionId}`)
  },
  getReport(sessionId) {
    return axiosInstance.get(`/interviews/${sessionId}/report`)
  },
  getHistory() {
    return axiosInstance.get('/interviews/history')
  },
  retry(sessionId) {
    return axiosInstance.post(`/interviews/${sessionId}/retry`)
  },
  finish(sessionId) {
    return axiosInstance.post(`/interviews/${sessionId}/finish`)
  },
  delete(sessionId) {
    return axiosInstance.delete(`/interviews/${sessionId}`)
  },
}

export default interviewApi
