import {
  LogOut,
  Mic,
  FileText,
  Code2,
  Brain,
  BookOpen,
  ArrowRight,
  TrendingUp,
  Clock3,
  Target,
} from 'lucide-react'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'
import interviewApi from '../api/interviewApi.js'

function Dashboard() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const [interviewCount, setInterviewCount] = useState(0)
  const [averageScore, setAverageScore] = useState(null)
  const [practiceTime, setPracticeTime] = useState(0)

  useEffect(() => {
    const loadInterviewStats = async () => {
      try {
        const { data } = await interviewApi.getHistory()

        const items = data.data || []

        // Only completed interviews
        const completedInterviews = items.filter(
          (item) => item.status === 'COMPLETED'
        )

        // Interview count
        setInterviewCount(completedInterviews.length)

        // Average score
        const validScores = completedInterviews
          .map((item) => Number(item.overallScore))
          .filter((score) => !Number.isNaN(score))

        if (validScores.length > 0) {
          const totalScore = validScores.reduce(
            (sum, score) => sum + score,
            0
          )

          setAverageScore(totalScore / validScores.length)
        } else {
          setAverageScore(null)
        }

        // Total practice time
        const totalSeconds = completedInterviews.reduce(
          (total, item) =>
            total + (Number(item.durationSeconds) || 0),
          0
        )

        setPracticeTime(totalSeconds)
      } catch (error) {
        console.error('Failed to load interview stats:', error)
      }
    }

    loadInterviewStats()
  }, [])

  const handleLogout = async () => {
    await logout()
    navigate('/login', { replace: true })
  }

  const firstName = user?.fullName
    ? user.fullName.split(' ')[0]
    : 'Candidate'

  const modules = [
    {
      title: 'Mock Interview',
      description:
        'Practice realistic AI-powered HR and technical interviews.',
      icon: Mic,
      action: 'Start Interview',
      path: '/dashboard/interviews',
    },
    {
      title: 'Resume Analyzer',
      description:
        'Analyze your resume and identify areas to improve.',
      icon: FileText,
      action: 'Analyze Resume',
      path: '/dashboard/resume',
    },
    {
      title: 'Coding Interview',
      description:
        'Solve coding problems and practice explaining your approach.',
      icon: Code2,
      action: 'Practice Coding',
      path: '/dashboard/coding',
    },
    {
      title: 'AI Mentor',
      description:
        'Get personalized guidance based on your preparation.',
      icon: Brain,
      action: 'Open AI Mentor',
      path: '/dashboard/ai-mentor',
    },
  ]

  return (
    <div className="min-h-screen bg-[var(--dash-bg)] transition-colors duration-300">

      {/* Header */}
      <header className="border-b border-[var(--dash-border)] bg-[var(--dash-surface)]">
        <div className="container-page flex h-18 items-center justify-between py-4">

          <div className="flex items-center gap-2">
            <span className="flex h-9 w-9 items-center justify-center rounded-lg border border-signal-amber/30 bg-signal-amber/10 text-signal-amber">
              <Mic size={18} strokeWidth={2} />
            </span>

            <span className="font-display text-lg font-semibold tracking-tight text-[var(--dash-text)]">
              PrepWise <span className="text-signal-amber">AI</span>
            </span>
          </div>

          <button
            type="button"
            onClick={handleLogout}
            className="inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm font-medium text-[var(--dash-text)] transition-colors hover:border-signal-amber/50 hover:text-signal-amber"
          >
            <LogOut size={16} />
            Log out
          </button>

        </div>
      </header>

      {/* Main */}
      <main className="container-page py-10">

        {/* Welcome */}
        <section className="mb-10">

          <span className="font-mono text-xs tracking-wide text-signal-amber">
            DASHBOARD
          </span>

          <h1 className="mt-3 font-display text-3xl font-semibold tracking-tight text-[var(--dash-text)]">
            Welcome back, {firstName}
          </h1>

          <p className="mt-2 max-w-xl text-[var(--dash-muted)]">
            Keep practicing, track your progress, and get closer to your dream job.
          </p>

        </section>

        {/* Statistics */}
        <section className="mb-10 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">

          {/* Interviews */}
          <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">

            <div className="flex items-center justify-between">
              <span className="text-sm text-[var(--dash-muted)]">
                Interviews
              </span>

              <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-signal-amber/10 text-signal-amber">
                <Mic size={18} />
              </span>
            </div>

            <p className="mt-3 text-2xl font-semibold text-[var(--dash-text)]">
              {interviewCount}
            </p>

            <p className="mt-1 text-xs text-[var(--dash-muted)]">
              Completed sessions
            </p>

          </div>

          {/* Average Score */}
          <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">

            <div className="flex items-center justify-between">
              <span className="text-sm text-[var(--dash-muted)]">
                Average Score
              </span>

              <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-signal-amber/10 text-signal-amber">
                <TrendingUp size={18} />
              </span>
            </div>

            <p className="mt-3 text-2xl font-semibold text-[var(--dash-text)]">
              {averageScore !== null
                ? Math.round(averageScore)
                : '—'}
            </p>

            <p className="mt-1 text-xs text-[var(--dash-muted)]">
              {averageScore !== null
                ? 'Based on completed interviews'
                : 'Start an interview to get your score'}
            </p>

          </div>

          {/* Practice Time */}
          <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">

            <div className="flex items-center justify-between">
              <span className="text-sm text-[var(--dash-muted)]">
                Practice Time
              </span>

              <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-signal-amber/10 text-signal-amber">
                <Clock3 size={18} />
              </span>
            </div>

            <p className="mt-3 text-2xl font-semibold text-[var(--dash-text)]">
              {Math.floor(practiceTime / 3600)}h{' '}
              {Math.floor((practiceTime % 3600) / 60)}m
            </p>

            <p className="mt-1 text-xs text-[var(--dash-muted)]">
              Total practice time
            </p>

          </div>

          {/* Progress */}
          <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">

            <div className="flex items-center justify-between">
              <span className="text-sm text-[var(--dash-muted)]">
                Progress
              </span>

              <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-signal-amber/10 text-signal-amber">
                <Target size={18} />
              </span>
            </div>

            <p className="mt-3 text-2xl font-semibold text-[var(--dash-text)]">
              {interviewCount > 0 ? 'Active' : 'New'}
            </p>

            <p className="mt-1 text-xs text-[var(--dash-muted)]">
              {interviewCount > 0
                ? 'Keep practicing to improve'
                : 'Complete your first session'}
            </p>

          </div>

        </section>

        {/* Practice Modules */}
        <section>

          <div className="mb-5 flex items-center justify-between">

            <div>
              <span className="font-mono text-xs tracking-wide text-signal-amber">
                PRACTICE
              </span>

              <h2 className="mt-2 font-display text-xl font-semibold text-[var(--dash-text)]">
                Continue your preparation
              </h2>
            </div>

            <BookOpen
              size={20}
              className="text-[var(--dash-muted)]"
            />

          </div>

          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">

            {modules.map((module) => {
              const Icon = module.icon

              return (
                <div
                  key={module.title}
                  className="group rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 transition-all duration-200 hover:-translate-y-0.5 hover:border-signal-amber/40"
                >

                  <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-signal-amber/10 text-signal-amber">
                    <Icon size={20} />
                  </div>

                  <h3 className="mt-4 font-display text-base font-semibold text-[var(--dash-text)]">
                    {module.title}
                  </h3>

                  <p className="mt-2 min-h-[48px] text-sm leading-6 text-[var(--dash-muted)]">
                    {module.description}
                  </p>

                  <button
                    type="button"
                    onClick={() => navigate(module.path)}
                    className="mt-5 inline-flex items-center gap-2 text-sm font-medium text-signal-amber transition-all group-hover:gap-3"
                  >
                    {module.action}
                    <ArrowRight size={15} />
                  </button>

                </div>
              )
            })}

          </div>

        </section>

      </main>

    </div>
  )
}

export default Dashboard