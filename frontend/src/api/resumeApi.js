import axiosInstance from './axiosInstance.js'

export const resumeApi = {
  upload(file, onUploadProgress) {
    const formData = new FormData()
    formData.append('file', file)
    return axiosInstance.post('/resume/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress,
    })
  },
  analyze(resumeId) {
    return axiosInstance.post('/resume/analyze', { resumeId })
  },
  reanalyze(resumeId) {
    return axiosInstance.post('/resume/reanalyze', { resumeId })
  },
  getLatest() {
    return axiosInstance.get('/resume/latest')
  },
  getHistory() {
    return axiosInstance.get('/resume/history')
  },
  getById(resumeId) {
    return axiosInstance.get(`/resume/${resumeId}`)
  },
  delete(resumeId) {
    return axiosInstance.delete(`/resume/${resumeId}`)
  },
  compare(firstId, secondId) {
    return axiosInstance.get('/resume/compare', { params: { firstId, secondId } })
  },
  download(resumeId) {
    return axiosInstance.get(`/resume/${resumeId}/download`, { responseType: 'blob' })
  },
}

export default resumeApi
