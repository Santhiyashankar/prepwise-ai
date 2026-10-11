import axiosInstance from './axiosInstance.js'

export const mentorApi = {
  chat(message) {
    return axiosInstance.post('/mentor/chat', {
      message,
    })
  },
}

export default mentorApi