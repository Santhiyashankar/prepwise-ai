
import { Routes, Route } from 'react-router-dom'

import LandingPage from '../pages/LandingPage.jsx'
import Login from '../pages/Login.jsx'
import Register from '../pages/Register.jsx'
import ForgotPassword from '../pages/ForgotPassword.jsx'
import ResetPassword from '../pages/ResetPassword.jsx'
import VerifyEmail from '../pages/VerifyEmail.jsx'

import Dashboard from '../pages/Dashboard.jsx'
import Profile from '../pages/Profile.jsx'
import EditProfile from '../pages/EditProfile.jsx'
import Settings from '../pages/Settings.jsx'

import ResumeDashboard from '../pages/resume/ResumeDashboard.jsx'
import ResumeAnalysisPage from '../pages/resume/ResumeAnalysisPage.jsx'
import ResumeHistoryPage from '../pages/resume/ResumeHistoryPage.jsx'
import ResumeComparisonPage from '../pages/resume/ResumeComparisonPage.jsx'

import InterviewDashboard from '../pages/interview/InterviewDashboard.jsx'
import InterviewSessionPage from '../pages/interview/InterviewSessionPage.jsx'
import InterviewReportPage from '../pages/interview/InterviewReportPage.jsx'
import InterviewHistoryPage from '../pages/interview/InterviewHistoryPage.jsx'

import CodingDashboard from '../pages/coding/CodingDashboard.jsx'
import ProblemListPage from '../pages/coding/ProblemListPage.jsx'
import ProblemSolvePage from '../pages/coding/ProblemSolvePage.jsx'
import DailyChallengePage from '../pages/coding/DailyChallengePage.jsx'
import TimedAssessmentPage from '../pages/coding/TimedAssessmentPage.jsx'
import CodingHistoryPage from '../pages/coding/CodingHistoryPage.jsx'
import SubmissionDetailPage from '../pages/coding/SubmissionDetailPage.jsx'

import GDDashboard from '../pages/gd/GDDashboard.jsx'
import GDSessionPage from '../pages/gd/GDSessionPage.jsx'
import GDReportPage from '../pages/gd/GDReportPage.jsx'
import GDHistoryPage from '../pages/gd/GDHistoryPage.jsx'

import DashboardLayout from '../components/dashboard/DashboardLayout.jsx'
import ProtectedRoute from '../components/common/ProtectedRoute.jsx'
import PublicRoute from '../components/common/PublicRoute.jsx'

import StudyPlanPage from '../pages/dashboard/study-plan/StudyPlanPage.jsx'
import AIMentorPage from '../pages/dashboard/ai-mentor/AIMentorPage.jsx'
import CareerRoadmapPage from '../pages/dashboard/CareerRoadmapPage.jsx'
import LeaderboardPage from '../pages/dashboard/LeaderboardPage.jsx'

function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/verify-email" element={<VerifyEmail />} />

      <Route element={<PublicRoute />}>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
      </Route>

      <Route element={<ProtectedRoute />}>
        <Route path="/dashboard" element={<DashboardLayout />}>
          <Route index element={<Dashboard />} />

          <Route path="profile" element={<Profile />} />
          <Route path="profile/edit" element={<EditProfile />} />
          <Route path="settings" element={<Settings />} />

          <Route path="resume" element={<ResumeDashboard />} />
          <Route path="resume/history" element={<ResumeHistoryPage />} />
          <Route path="resume/compare" element={<ResumeComparisonPage />} />
          <Route
            path="resume/analysis/:resumeId"
            element={<ResumeAnalysisPage />}
          />

          <Route path="interviews" element={<InterviewDashboard />} />
          <Route path="interviews/history" element={<InterviewHistoryPage />} />
          <Route
            path="interviews/session/:sessionId"
            element={<InterviewSessionPage />}
          />
          <Route
            path="interviews/report/:sessionId"
            element={<InterviewReportPage />}
          />

          <Route path="coding" element={<CodingDashboard />} />
          <Route path="coding/problems" element={<ProblemListPage />} />
          <Route path="coding/problems/:slug" element={<ProblemSolvePage />} />
          <Route path="coding/daily" element={<DailyChallengePage />} />
          <Route
            path="coding/timed-assessment"
            element={<TimedAssessmentPage />}
          />
          <Route path="coding/history" element={<CodingHistoryPage />} />
          <Route
            path="coding/submissions/:submissionId"
            element={<SubmissionDetailPage />}
          />

          <Route path="group-discussion" element={<GDDashboard />} />
          <Route
            path="group-discussion/session/:sessionId"
            element={<GDSessionPage />}
          />
          <Route
            path="group-discussion/report/:sessionId"
            element={<GDReportPage />}
          />
          <Route
            path="group-discussion/history"
            element={<GDHistoryPage />}
          />

          <Route path="study-plan" element={<StudyPlanPage />} />
          <Route path="ai-mentor" element={<AIMentorPage />} />
          <Route path="career-roadmap" element={<CareerRoadmapPage />} />
          <Route path="leaderboard" element={<LeaderboardPage />} />

          <Route path="history" element={<InterviewHistoryPage />} />
        </Route>
      </Route>
    </Routes>
  )
}

export default AppRouter

