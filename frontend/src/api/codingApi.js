import axiosInstance from './axiosInstance.js'

export const codingApi = {
  listProblems(params) {
    return axiosInstance.get('/coding/problems', { params })
  },
  getProblem(slug) {
    return axiosInstance.get(`/coding/problems/${slug}`)
  },
  getDailyChallenge() {
    return axiosInstance.get('/coding/daily-challenge')
  },
  generateTimedAssessment(payload) {
    return axiosInstance.post('/coding/timed-assessment', payload)
  },
  runCode(payload) {
    return axiosInstance.post('/coding/run', payload)
  },
  submitCode(payload) {
    return axiosInstance.post('/coding/submit', payload)
  },
  getHistory() {
    return axiosInstance.get('/coding/history')
  },
  getSubmission(submissionId) {
    return axiosInstance.get(`/coding/submissions/${submissionId}`)
  },
}

export default codingApi
