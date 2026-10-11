
import axiosInstance from './axiosInstance.js'

const leaderboardApi = {
  getLeaderboard() {
    return axiosInstance.get('/leaderboard')
  },
}

export default leaderboardApi

