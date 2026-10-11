
import { useEffect, useState } from 'react'
import {
  Trophy,
  Medal,
  Crown,
  Target,
  CheckCircle2,
  RefreshCw,
  Users,
  AlertCircle,
  Award,
} from 'lucide-react'
import leaderboardApi from '../../api/leaderboardApi.js'

function getInitials(name) {
  return (name || 'User')
    .trim()
    .split(/\s+/)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase() || '')
    .join('')
}

function getRankStyle(rank) {
  if (rank === 1) {
    return {
      label: '1st',
      icon: Crown,
      color: 'text-amber-400',
      background: 'bg-amber-400/10 border-amber-400/30',
    }
  }

  if (rank === 2) {
    return {
      label: '2nd',
      icon: Medal,
      color: 'text-slate-300',
      background: 'bg-slate-400/10 border-slate-400/30',
    }
  }

  if (rank === 3) {
    return {
      label: '3rd',
      icon: Medal,
      color: 'text-orange-400',
      background: 'bg-orange-400/10 border-orange-400/30',
    }
  }

  return {
    label: `#${rank}`,
    icon: Award,
    color: 'text-[var(--dash-muted)]',
    background: 'bg-[var(--dash-surface)] border-[var(--dash-border)]',
  }
}

function LeaderboardPage() {
  const [entries, setEntries] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  async function fetchLeaderboard() {
    setLoading(true)
    setError('')

    try {
      const response = await leaderboardApi.getLeaderboard()

      // Supports either a direct array or the standard { data: [...] } response.
      const payload = response.data
      const leaderboard = Array.isArray(payload)
        ? payload
        : Array.isArray(payload?.data)
          ? payload.data
          : []

      setEntries(leaderboard)
    } catch (err) {
      console.error('Failed to load leaderboard:', err)
      setError(
        err.response?.status === 403
          ? 'You do not have permission to view the leaderboard.'
          : 'Unable to load the leaderboard. Please try again.',
      )
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchLeaderboard()
  }, [])

  const topEntries = entries.slice(0, 3)

  return (
    <div className="min-h-screen space-y-6 bg-[var(--dash-bg)] p-4 text-[var(--dash-text)] sm:p-6 lg:p-8">
      {/* Header */}
      <section className="relative overflow-hidden rounded-3xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 sm:p-8">
        <div className="pointer-events-none absolute -right-12 -top-16 h-52 w-52 rounded-full bg-amber-400/10 blur-3xl" />

        <div className="relative flex flex-col justify-between gap-5 sm:flex-row sm:items-center">
          <div>
            <div className="mb-3 inline-flex items-center gap-2 rounded-full border border-amber-400/20 bg-amber-400/10 px-3 py-1.5 text-xs font-semibold text-amber-400">
              <Trophy size={14} />
              COMMUNITY RANKINGS
            </div>

            <h1 className="text-3xl font-bold tracking-tight sm:text-4xl">
              Leaderboard
            </h1>

            <p className="mt-2 max-w-xl text-sm leading-6 text-[var(--dash-muted)] sm:text-base">
              Every interview is an opportunity to improve. Track your
              performance and see how you rank among other learners.
            </p>
          </div>

          <button
            type="button"
            onClick={fetchLeaderboard}
            disabled={loading}
            className="inline-flex shrink-0 items-center justify-center gap-2 rounded-xl border border-[var(--dash-border)] bg-[var(--dash-bg)] px-4 py-3 text-sm font-semibold transition hover:border-amber-400/50 hover:text-amber-400 disabled:cursor-not-allowed disabled:opacity-50"
          >
            <RefreshCw size={16} className={loading ? 'animate-spin' : ''} />
            Refresh
          </button>
        </div>
      </section>

      {/* Summary cards */}
      <section className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <SummaryCard
          icon={Users}
          label="Ranked learners"
          value={loading ? '—' : entries.length.toLocaleString()}
          description="Learners with scored interviews"
        />
        <SummaryCard
          icon={Trophy}
          label="Top score"
          value={
            loading
              ? '—'
              : entries.length
                ? `${Number(entries[0].averageScore || 0).toFixed(1)}%`
                : '—'
          }
          description="Highest average interview score"
        />
        <SummaryCard
          icon={CheckCircle2}
          label="Scored interviews"
          value={
            loading
              ? '—'
              : entries
                  .reduce(
                    (total, entry) =>
                      total + Number(entry.completedInterviews || 0),
                    0,
                  )
                  .toLocaleString()
          }
          description="Across ranked learners"
        />
      </section>

      {/* Leaderboard table */}
      <section className="overflow-hidden rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)]">
        <div className="flex items-center justify-between gap-3 border-b border-[var(--dash-border)] p-5 sm:p-6">
          <div>
            <h2 className="text-lg font-bold">Interview rankings</h2>
            <p className="mt-1 text-sm text-[var(--dash-muted)]">
              Ranked by average score, with interview count as the tiebreaker.
            </p>
          </div>

          <Trophy className="shrink-0 text-amber-400" size={24} />
        </div>

        {loading ? (
          <div className="flex flex-col items-center justify-center gap-3 px-5 py-16">
            <RefreshCw className="animate-spin text-amber-400" size={28} />
            <p className="text-sm text-[var(--dash-muted)]">
              Loading real leaderboard data...
            </p>
          </div>
        ) : error ? (
          <div className="flex flex-col items-center px-5 py-14 text-center">
            <AlertCircle className="mb-3 text-red-400" size={32} />
            <p className="font-semibold">Something went wrong</p>
            <p className="mt-2 max-w-md text-sm text-[var(--dash-muted)]">
              {error}
            </p>
            <button
              type="button"
              onClick={fetchLeaderboard}
              className="mt-5 rounded-xl bg-amber-400 px-4 py-2.5 text-sm font-bold text-slate-950 transition hover:bg-amber-300"
            >
              Try again
            </button>
          </div>
        ) : entries.length === 0 ? (
          <div className="flex flex-col items-center px-5 py-16 text-center">
            <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-2xl bg-amber-400/10">
              <Trophy className="text-amber-400" size={30} />
            </div>
            <h3 className="text-lg font-bold">No rankings yet</h3>
            <p className="mt-2 max-w-md text-sm leading-6 text-[var(--dash-muted)]">
              The leaderboard will appear when learners complete interviews
              that have a recorded score. Complete an interview to get started!
            </p>
          </div>
        ) : (
          <>
            {/* Top three */}
            <div className="grid grid-cols-1 gap-3 border-b border-[var(--dash-border)] p-4 sm:grid-cols-3 sm:p-6">
              {topEntries.map((entry) => {
                const rank = getRankStyle(Number(entry.rank))
                const RankIcon = rank.icon

                return (
                  <div
                    key={entry.userId}
                    className={`relative overflow-hidden rounded-2xl border p-5 ${rank.background}`}
                  >
                    {Number(entry.rank) === 1 && (
                      <div className="absolute -right-5 -top-5 h-20 w-20 rounded-full bg-amber-400/10 blur-xl" />
                    )}

                    <div className="relative flex items-center justify-between">
                      <span className={`text-sm font-bold ${rank.color}`}>
                        {rank.label} Place
                      </span>
                      <RankIcon size={21} className={rank.color} />
                    </div>

                    <div className="relative mt-5 flex items-center gap-3">
                      <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full border border-[var(--dash-border)] bg-[var(--dash-bg)] text-sm font-bold">
                        {getInitials(entry.fullName)}
                      </div>
                      <div className="min-w-0">
                        <p className="truncate font-semibold">
                          {entry.fullName || 'Learner'}
                        </p>
                        <p className="mt-1 text-xs text-[var(--dash-muted)]">
                          {Number(entry.completedInterviews || 0)} scored
                          interviews
                        </p>
                      </div>
                    </div>

                    <div className="relative mt-5">
                      <p className={`text-3xl font-bold ${rank.color}`}>
                        {Number(entry.averageScore || 0).toFixed(1)}
                        <span className="ml-1 text-base">%</span>
                      </p>
                      <p className="mt-1 text-xs text-[var(--dash-muted)]">
                        Average score
                      </p>
                    </div>
                  </div>
                )
              })}
            </div>

            {/* Full rankings */}
            <div className="overflow-x-auto">
              <table className="w-full min-w-[620px] text-left">
                <thead>
                  <tr className="border-b border-[var(--dash-border)] text-xs uppercase tracking-wider text-[var(--dash-muted)]">
                    <th className="px-5 py-4 font-semibold sm:px-6">Rank</th>
                    <th className="px-5 py-4 font-semibold">Learner</th>
                    <th className="px-5 py-4 font-semibold">Interviews</th>
                    <th className="px-5 py-4 font-semibold">Average score</th>
                  </tr>
                </thead>

                <tbody>
                  {entries.map((entry) => {
                    const rank = getRankStyle(Number(entry.rank))

                    return (
                      <tr
                        key={entry.userId}
                        className="border-b border-[var(--dash-border)] last:border-b-0 transition hover:bg-[var(--dash-surface-hover)]"
                      >
                        <td className="px-5 py-4 sm:px-6">
                          <span className={`font-bold ${rank.color}`}>
                            #{entry.rank}
                          </span>
                        </td>

                        <td className="px-5 py-4">
                          <div className="flex items-center gap-3">
                            <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full border border-[var(--dash-border)] bg-[var(--dash-bg)] text-xs font-bold">
                              {getInitials(entry.fullName)}
                            </div>
                            <span className="font-medium">
                              {entry.fullName || 'Learner'}
                            </span>
                          </div>
                        </td>

                        <td className="px-5 py-4 text-sm text-[var(--dash-muted)]">
                          {Number(entry.completedInterviews || 0)}
                        </td>

                        <td className="px-5 py-4">
                          <div className="flex items-center gap-3">
                            <div className="h-2 w-24 overflow-hidden rounded-full bg-[var(--dash-bg)]">
                              <div
                                className="h-full rounded-full bg-amber-400"
                                style={{
                                  width: `${Math.min(
                                    100,
                                    Math.max(
                                      0,
                                      Number(entry.averageScore || 0),
                                    ),
                                  )}%`,
                                }}
                              />
                            </div>
                            <span className="whitespace-nowrap text-sm font-bold">
                              {Number(entry.averageScore || 0).toFixed(1)}%
                            </span>
                          </div>
                        </td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            </div>
          </>
        )}
      </section>

      <p className="px-1 text-xs leading-5 text-[var(--dash-muted)]">
        Rankings are calculated from completed interviews with recorded scores.
        Your results update when you refresh the leaderboard.
      </p>
    </div>
  )
}

function SummaryCard({ icon: Icon, label, value, description }) {
  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-sm text-[var(--dash-muted)]">{label}</p>
          <p className="mt-2 text-2xl font-bold">{value}</p>
        </div>
        <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-amber-400/10">
          <Icon className="text-amber-400" size={21} />
        </div>
      </div>
      <p className="mt-3 text-xs text-[var(--dash-muted)]">{description}</p>
    </div>
  )
}

export default LeaderboardPage

