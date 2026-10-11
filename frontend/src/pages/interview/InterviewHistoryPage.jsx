
import { useCallback, useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  ArrowLeft,
  Eye,
  RefreshCw,
  Trash2,
  Search,
  History,
  Trophy,
  Target,
  CalendarDays,
  Clock3,
  Filter,
  RotateCcw,
  AlertCircle,
  CheckCircle2,
  ChevronDown,
  Sparkles,
} from 'lucide-react'

import interviewApi from '../../api/interviewApi.js'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import {
  DIFFICULTY_LABELS,
  ROLE_LABELS,
  STYLE_LABELS,
} from '../../utils/interviewConstants.js'

function formatDuration(seconds) {
  if (seconds == null || seconds === '' || !Number.isFinite(Number(seconds))) {
    return '—'
  }

  const minutes = Math.round(Number(seconds) / 60)
  return `${minutes} min`
}

function formatDate(dateString) {
  if (!dateString) return '—'

  const date = new Date(dateString)

  if (Number.isNaN(date.getTime())) return '—'

  return date.toLocaleDateString('en-IN', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  })
}

function formatScore(score) {
  if (score == null || score === '' || !Number.isFinite(Number(score))) {
    return '—'
  }

  return Math.round(Number(score))
}

function getScoreColor(score) {
  if (score == null || score === '' || !Number.isFinite(Number(score))) {
    return 'text-[var(--dash-muted)]'
  }

  const value = Number(score)

  if (value >= 75) return 'text-emerald-400'
  if (value >= 50) return 'text-amber-400'
  return 'text-rose-400'
}

function InterviewHistoryPage() {
  const navigate = useNavigate()

  const [items, setItems] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [isRefreshing, setIsRefreshing] = useState(false)
  const [busyId, setBusyId] = useState(null)
  const [errorMessage, setErrorMessage] = useState('')
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedType, setSelectedType] = useState('all')
  const [selectedDifficulty, setSelectedDifficulty] = useState('all')
  const [selectedStatus, setSelectedStatus] = useState('all')
  const [sortOrder, setSortOrder] = useState('newest')
  const [deleteTarget, setDeleteTarget] = useState(null)

  const loadHistory = useCallback(async (showRefresh = false) => {
    if (showRefresh) {
      setIsRefreshing(true)
    } else {
      setIsLoading(true)
    }

    setErrorMessage('')

    try {
      const { data } = await interviewApi.getHistory()
      const history = data?.data

      setItems(Array.isArray(history) ? history : [])
    } catch (error) {
      setErrorMessage(
        error.response?.data?.message ||
          'Could not load interview history. Please try again.'
      )
    } finally {
      setIsLoading(false)
      setIsRefreshing(false)
    }
  }, [])

  useEffect(() => {
    loadHistory()
  }, [loadHistory])

  const stats = useMemo(() => {
    const scoredItems = items.filter(
      (item) =>
        item.overallScore != null &&
        item.overallScore !== '' &&
        Number.isFinite(Number(item.overallScore))
    )

    const averageScore = scoredItems.length
      ? Math.round(
          scoredItems.reduce(
            (total, item) => total + Number(item.overallScore),
            0
          ) / scoredItems.length
        )
      : null

    const bestScore = scoredItems.length
      ? Math.max(...scoredItems.map((item) => Number(item.overallScore)))
      : null

    const completedCount = items.filter(
      (item) => item.status === 'COMPLETED'
    ).length

    return {
      total: items.length,
      completed: completedCount,
      averageScore,
      bestScore,
    }
  }, [items])

  const filteredItems = useMemo(() => {
    const search = searchTerm.trim().toLowerCase()

    return [...items]
      .filter((item) => {
        const role = ROLE_LABELS[item.role] || item.role || ''
        const type = STYLE_LABELS[item.style] || item.style || ''
        const difficulty =
          DIFFICULTY_LABELS[item.difficulty] || item.difficulty || ''

        const matchesSearch =
          !search ||
          role.toLowerCase().includes(search) ||
          type.toLowerCase().includes(search) ||
          difficulty.toLowerCase().includes(search) ||
          String(item.status || '').toLowerCase().includes(search)

        const matchesType =
          selectedType === 'all' || item.style === selectedType

        const matchesDifficulty =
          selectedDifficulty === 'all' ||
          item.difficulty === selectedDifficulty

        const matchesStatus =
          selectedStatus === 'all' || item.status === selectedStatus

        return (
          matchesSearch &&
          matchesType &&
          matchesDifficulty &&
          matchesStatus
        )
      })
      .sort((a, b) => {
        const dateA = new Date(a.startedAt || 0).getTime() || 0
        const dateB = new Date(b.startedAt || 0).getTime() || 0

        return sortOrder === 'newest' ? dateB - dateA : dateA - dateB
      })
  }, [
    items,
    searchTerm,
    selectedType,
    selectedDifficulty,
    selectedStatus,
    sortOrder,
  ])

  const clearFilters = () => {
    setSearchTerm('')
    setSelectedType('all')
    setSelectedDifficulty('all')
    setSelectedStatus('all')
    setSortOrder('newest')
  }

  const handleRetry = async (sessionId) => {
    setBusyId(sessionId)
    setErrorMessage('')

    try {
      const { data } = await interviewApi.retry(sessionId)
      const newSessionId = data?.data?.id

      if (!newSessionId) {
        throw new Error('The new interview session ID was not returned.')
      }

      navigate(`/dashboard/interviews/session/${newSessionId}`)
    } catch (error) {
      setErrorMessage(
        error.response?.data?.message ||
          error.message ||
          'Failed to restart interview. Please try again.'
      )
      setBusyId(null)
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return

    const sessionId = deleteTarget.sessionId

    setBusyId(sessionId)
    setErrorMessage('')

    try {
      await interviewApi.delete(sessionId)

      setItems((current) =>
        current.filter((item) => item.sessionId !== sessionId)
      )

      setDeleteTarget(null)
    } catch (error) {
      setErrorMessage(
        error.response?.data?.message ||
          'Failed to delete interview. Please try again.'
      )
    } finally {
      setBusyId(null)
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-5">
        <div>
          <div className="h-7 w-52 animate-pulse rounded-lg bg-[var(--dash-surface-hover)]" />
          <div className="mt-2 h-4 w-80 max-w-full animate-pulse rounded bg-[var(--dash-surface-hover)]" />
        </div>
        <LoadingSkeleton className="h-32 w-full" />
        <LoadingSkeleton className="h-96 w-full" />
      </div>
    )
  }

  return (
    <div className="space-y-6 pb-8">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <button
            type="button"
            onClick={() => navigate('/dashboard/interviews')}
            className="mb-4 inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] transition-colors hover:text-signal-amber"
          >
            <ArrowLeft size={15} />
            Back to Interviews
          </button>

          <div className="flex items-center gap-3">
            <div className="rounded-xl border border-amber-400/20 bg-amber-400/10 p-3">
              <History className="h-6 w-6 text-amber-400" />
            </div>

            <div>
              <h1 className="font-display text-2xl font-semibold text-[var(--dash-text)]">
                Interview History
              </h1>
              <p className="mt-1 text-sm text-[var(--dash-muted)]">
                Track your performance and revisit previous interviews.
              </p>
            </div>
          </div>
        </div>

        <button
          type="button"
          onClick={() => loadHistory(true)}
          disabled={isRefreshing}
          className="inline-flex items-center justify-center gap-2 self-start rounded-lg border border-[var(--dash-border)] bg-[var(--dash-surface)] px-4 py-2.5 text-sm font-medium text-[var(--dash-text)] transition hover:border-amber-400/40 disabled:cursor-not-allowed disabled:opacity-50 sm:self-auto"
        >
          <RefreshCw
            size={15}
            className={isRefreshing ? 'animate-spin' : ''}
          />
          {isRefreshing ? 'Refreshing...' : 'Refresh History'}
        </button>
      </div>

      {/* Error Message */}
      {errorMessage && (
        <div
          role="alert"
          className="flex items-start gap-3 rounded-xl border border-red-500/30 bg-red-500/10 p-4 text-sm text-red-400"
        >
          <AlertCircle size={18} className="mt-0.5 shrink-0" />
          <div className="flex-1">{errorMessage}</div>
          <button
            type="button"
            onClick={() => loadHistory(true)}
            className="shrink-0 font-semibold underline underline-offset-4"
          >
            Retry
          </button>
        </div>
      )}

      {/* Statistics */}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard
          label="Total Interviews"
          value={stats.total}
          caption="Recorded sessions"
          icon={History}
          iconColor="text-amber-400"
        />

        <StatCard
          label="Completed"
          value={stats.completed}
          caption="Finished sessions"
          icon={CheckCircle2}
          iconColor="text-emerald-400"
        />

        <StatCard
          label="Average Score"
          value={stats.averageScore == null ? '—' : `${stats.averageScore}%`}
          caption="Sessions with scores"
          icon={Target}
          iconColor="text-sky-400"
        />

        <StatCard
          label="Best Score"
          value={stats.bestScore == null ? '—' : `${stats.bestScore}%`}
          caption="Highest recorded score"
          icon={Trophy}
          iconColor="text-violet-400"
        />
      </div>

      {/* Filters */}
      <section className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4 sm:p-5">
        <div className="mb-4 flex items-center gap-2">
          <Filter size={16} className="text-amber-400" />
          <h2 className="text-sm font-semibold text-[var(--dash-text)]">
            Search & Filters
          </h2>
        </div>

        <div className="grid grid-cols-1 gap-3 sm:grid-cols-2 xl:grid-cols-5">
          <div className="relative sm:col-span-2 xl:col-span-2">
            <Search
              size={16}
              className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-[var(--dash-muted)]"
            />
            <input
              type="search"
              value={searchTerm}
              onChange={(event) => setSearchTerm(event.target.value)}
              placeholder="Search role, type or status..."
              aria-label="Search interview history"
              className="w-full rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] py-2.5 pl-9 pr-3 text-sm text-[var(--dash-text)] outline-none transition placeholder:text-[var(--dash-muted)] focus:border-amber-400/50"
            />
          </div>

          <FilterSelect
            label="Interview type"
            value={selectedType}
            onChange={setSelectedType}
            options={[
              { value: 'all', label: 'All Types' },
              { value: 'TECHNICAL', label: 'Technical' },
              { value: 'HR', label: 'HR' },
              { value: 'MIXED', label: 'Mixed' },
              { value: 'BEHAVIORAL', label: 'Behavioral' },
            ]}
          />

          <FilterSelect
            label="Difficulty"
            value={selectedDifficulty}
            onChange={setSelectedDifficulty}
            options={[
              { value: 'all', label: 'All Difficulties' },
              { value: 'EASY', label: 'Easy' },
              { value: 'MEDIUM', label: 'Medium' },
              { value: 'HARD', label: 'Hard' },
            ]}
          />

          <FilterSelect
            label="Status"
            value={selectedStatus}
            onChange={setSelectedStatus}
            options={[
              { value: 'all', label: 'All Statuses' },
              { value: 'COMPLETED', label: 'Completed' },
              { value: 'IN_PROGRESS', label: 'In Progress' },
            ]}
          />
        </div>

        <div className="mt-4 flex flex-wrap items-center justify-between gap-3">
          <p className="text-xs text-[var(--dash-muted)]">
            Showing {filteredItems.length} of {items.length} sessions
          </p>

          <div className="flex items-center gap-2">
            <label
              htmlFor="history-sort"
              className="text-xs text-[var(--dash-muted)]"
            >
              Sort:
            </label>
            <div className="relative">
              <select
                id="history-sort"
                value={sortOrder}
                onChange={(event) => setSortOrder(event.target.value)}
                className="appearance-none rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] py-2 pl-3 pr-8 text-xs text-[var(--dash-text)] outline-none focus:border-amber-400/50"
              >
                <option value="newest">Newest first</option>
                <option value="oldest">Oldest first</option>
              </select>
              <ChevronDown
                size={13}
                className="pointer-events-none absolute right-2.5 top-1/2 -translate-y-1/2 text-[var(--dash-muted)]"
              />
            </div>

            <button
              type="button"
              onClick={clearFilters}
              className="inline-flex items-center gap-1.5 rounded-lg px-2 py-2 text-xs text-[var(--dash-muted)] transition hover:text-signal-amber"
            >
              <RotateCcw size={13} />
              Clear
            </button>
          </div>
        </div>
      </section>

      {/* History Table */}
      <section className="overflow-hidden rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)]">
        <div className="flex flex-wrap items-center justify-between gap-2 border-b border-[var(--dash-border)] p-4 sm:p-5">
          <div>
            <h2 className="font-semibold text-[var(--dash-text)]">
              Past Sessions
            </h2>
            <p className="mt-1 text-xs text-[var(--dash-muted)]">
              View reports, retry sessions or remove old records.
            </p>
          </div>

          <span className="rounded-full border border-[var(--dash-border)] bg-[var(--dash-bg)] px-3 py-1 text-xs text-[var(--dash-muted)]">
            {filteredItems.length} results
          </span>
        </div>

        {filteredItems.length === 0 ? (
          <div className="px-6 py-14 text-center">
            <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-bg)]">
              {items.length === 0 ? (
                <Sparkles size={25} className="text-amber-400" />
              ) : (
                <Search size={25} className="text-amber-400" />
              )}
            </div>

            <h3 className="font-semibold text-[var(--dash-text)]">
              {items.length === 0
                ? 'No interviews yet'
                : 'No matching interviews'}
            </h3>

            <p className="mx-auto mt-2 max-w-sm text-sm leading-6 text-[var(--dash-muted)]">
              {items.length === 0
                ? 'Complete a mock interview and your saved sessions will appear here.'
                : 'Try adjusting your search or filters to find a session.'}
            </p>

            {items.length === 0 ? (
              <button
                type="button"
                onClick={() => navigate('/dashboard/interviews')}
                className="mt-5 inline-flex items-center gap-2 rounded-lg bg-amber-500 px-4 py-2.5 text-sm font-semibold text-black transition hover:bg-amber-400"
              >
                Start an Interview
                <ArrowLeft size={15} className="rotate-180" />
              </button>
            ) : (
              <button
                type="button"
                onClick={clearFilters}
                className="mt-5 inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm text-[var(--dash-text)] transition hover:border-amber-400/40"
              >
                <RotateCcw size={14} />
                Clear Filters
              </button>
            )}
          </div>
        ) : (
          <>
            {/* Desktop Table */}
            <div className="hidden overflow-x-auto md:block">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-[var(--dash-border)] text-left text-xs text-[var(--dash-muted)]">
                    <th className="px-5 py-3 font-medium">Role</th>
                    <th className="px-4 py-3 font-medium">Type</th>
                    <th className="px-4 py-3 font-medium">Difficulty</th>
                    <th className="px-4 py-3 font-medium">Score</th>
                    <th className="px-4 py-3 font-medium">Duration</th>
                    <th className="px-4 py-3 font-medium">Date</th>
                    <th className="px-5 py-3 text-right font-medium">Actions</th>
                  </tr>
                </thead>

                <tbody>
                  {filteredItems.map((item) => (
                    <tr
                      key={item.sessionId}
                      className="border-b border-[var(--dash-border)]/60 transition-colors last:border-0 hover:bg-[var(--dash-surface-hover)]/50"
                    >
                      <td className="px-5 py-4 font-medium text-[var(--dash-text)]">
                        {ROLE_LABELS[item.role] || item.role || '—'}
                      </td>

                      <td className="px-4 py-4 text-[var(--dash-text)]">
                        {STYLE_LABELS[item.style] || item.style || '—'}
                      </td>

                      <td className="px-4 py-4 text-[var(--dash-muted)]">
                        {DIFFICULTY_LABELS[item.difficulty] ||
                          item.difficulty ||
                          '—'}
                      </td>

                      <td
                        className={`px-4 py-4 font-mono font-semibold ${getScoreColor(item.overallScore)}`}
                      >
                        {formatScore(item.overallScore)}
                        {item.overallScore != null ? '%' : ''}
                      </td>

                      <td className="px-4 py-4 text-[var(--dash-muted)]">
                        {formatDuration(item.durationSeconds)}
                      </td>

                      <td className="whitespace-nowrap px-4 py-4 text-[var(--dash-muted)]">
                        {formatDate(item.startedAt)}
                      </td>

                      <td className="px-5 py-4">
                        <ActionButtons
                          item={item}
                          busy={busyId === item.sessionId}
                          onView={() =>
                            navigate(
                              `/dashboard/interviews/report/${item.sessionId}`
                            )
                          }
                          onRetry={() => handleRetry(item.sessionId)}
                          onDelete={() => setDeleteTarget(item)}
                        />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Mobile Cards */}
            <div className="space-y-3 p-3 md:hidden">
              {filteredItems.map((item) => (
                <div
                  key={item.sessionId}
                  className="rounded-xl border border-[var(--dash-border)] bg-[var(--dash-bg)] p-4"
                >
                  <div className="flex items-start justify-between gap-3">
                    <div className="min-w-0">
                      <h3 className="font-semibold text-[var(--dash-text)]">
                        {ROLE_LABELS[item.role] || item.role || 'Interview'}
                      </h3>
                      <p className="mt-1 text-sm text-[var(--dash-muted)]">
                        {STYLE_LABELS[item.style] || item.style || '—'}
                        {' · '}
                        {DIFFICULTY_LABELS[item.difficulty] ||
                          item.difficulty ||
                          '—'}
                      </p>
                    </div>

                    <div className="shrink-0 text-right">
                      <p className="text-xs text-[var(--dash-muted)]">Score</p>
                      <p
                        className={`mt-1 text-xl font-bold ${getScoreColor(item.overallScore)}`}
                      >
                        {formatScore(item.overallScore)}
                        {item.overallScore != null ? '%' : ''}
                      </p>
                    </div>
                  </div>

                  <div className="mt-4 flex flex-wrap gap-x-4 gap-y-2 text-xs text-[var(--dash-muted)]">
                    <span className="inline-flex items-center gap-1.5">
                      <CalendarDays size={13} />
                      {formatDate(item.startedAt)}
                    </span>
                    <span className="inline-flex items-center gap-1.5">
                      <Clock3 size={13} />
                      {formatDuration(item.durationSeconds)}
                    </span>
                  </div>

                  <div className="mt-4 border-t border-[var(--dash-border)] pt-3">
                    <ActionButtons
                      item={item}
                      busy={busyId === item.sessionId}
                      onView={() =>
                        navigate(
                          `/dashboard/interviews/report/${item.sessionId}`
                        )
                      }
                      onRetry={() => handleRetry(item.sessionId)}
                      onDelete={() => setDeleteTarget(item)}
                    />
                  </div>
                </div>
              ))}
            </div>
          </>
        )}
      </section>

      {/* Delete Confirmation */}
      {deleteTarget && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-4 backdrop-blur-sm"
          onMouseDown={(event) => {
            if (
              event.target === event.currentTarget &&
              busyId !== deleteTarget.sessionId
            ) {
              setDeleteTarget(null)
            }
          }}
        >
          <div
            role="dialog"
            aria-modal="true"
            aria-labelledby="delete-dialog-title"
            className="w-full max-w-md rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 shadow-2xl"
          >
            <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-red-500/10">
              <Trash2 size={22} className="text-red-400" />
            </div>

            <h2
              id="delete-dialog-title"
              className="text-lg font-semibold text-[var(--dash-text)]"
            >
              Delete interview?
            </h2>

            <p className="mt-2 text-sm leading-6 text-[var(--dash-muted)]">
              This will delete the selected interview record. Make sure you
              no longer need it before continuing.
            </p>

            <div className="mt-6 flex justify-end gap-3">
              <button
                type="button"
                disabled={busyId === deleteTarget.sessionId}
                onClick={() => setDeleteTarget(null)}
                className="rounded-lg border border-[var(--dash-border)] px-4 py-2.5 text-sm text-[var(--dash-text)] transition hover:bg-[var(--dash-surface-hover)] disabled:opacity-50"
              >
                Cancel
              </button>

              <button
                type="button"
                disabled={busyId === deleteTarget.sessionId}
                onClick={handleDelete}
                className="inline-flex items-center gap-2 rounded-lg bg-red-500 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-red-400 disabled:cursor-not-allowed disabled:opacity-50"
              >
                {busyId === deleteTarget.sessionId && (
                  <RefreshCw size={14} className="animate-spin" />
                )}
                Delete Interview
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

function StatCard({ label, value, caption, icon: Icon, iconColor }) {
  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 transition-colors hover:border-amber-400/20">
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-sm text-[var(--dash-muted)]">{label}</p>
          <p className="mt-3 text-3xl font-semibold tracking-tight text-[var(--dash-text)]">
            {value}
          </p>
          <p className="mt-1 text-xs text-[var(--dash-muted)]">{caption}</p>
        </div>

        <div className="rounded-xl border border-[var(--dash-border)] bg-[var(--dash-bg)] p-3">
          <Icon size={21} className={iconColor} />
        </div>
      </div>
    </div>
  )
}

function FilterSelect({ label, value, onChange, options }) {
  return (
    <div className="relative">
      <select
        aria-label={label}
        value={value}
        onChange={(event) => onChange(event.target.value)}
        className="w-full appearance-none rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] px-3 py-2.5 pr-8 text-sm text-[var(--dash-text)] outline-none transition focus:border-amber-400/50"
      >
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>

      <ChevronDown
        size={14}
        className="pointer-events-none absolute right-3 top-1/2 -translate-y-1/2 text-[var(--dash-muted)]"
      />
    </div>
  )
}

function ActionButtons({ item, busy, onView, onRetry, onDelete }) {
  const actionClass =
    'flex h-9 w-9 items-center justify-center rounded-lg border border-[var(--dash-border)] text-[var(--dash-muted)] transition hover:bg-[var(--dash-surface-hover)] disabled:cursor-not-allowed disabled:opacity-40'

  return (
    <div className="flex items-center justify-end gap-2">
      {item.status === 'COMPLETED' && (
        <button
          type="button"
          onClick={onView}
          className={`${actionClass} hover:border-amber-400/30 hover:text-amber-400`}
          aria-label="View interview report"
          title="View report"
        >
          <Eye size={16} />
        </button>
      )}

      <button
        type="button"
        onClick={onRetry}
        disabled={busy}
        className={`${actionClass} hover:border-amber-400/30 hover:text-amber-400`}
        aria-label="Retry interview"
        title="Retry interview"
      >
        <RefreshCw size={15} className={busy ? 'animate-spin' : ''} />
      </button>

      <button
        type="button"
        onClick={onDelete}
        disabled={busy}
        className={`${actionClass} hover:border-red-500/30 hover:text-red-400`}
        aria-label="Delete interview"
        title="Delete interview"
      >
        <Trash2 size={15} />
      </button>
    </div>
  )
}

export default InterviewHistoryPage