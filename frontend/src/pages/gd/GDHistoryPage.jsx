import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { ArrowLeft, Eye, RefreshCw, Trash2 } from 'lucide-react'
import gdApi from '../../api/gdApi.js'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import { CATEGORY_LABELS } from '../../utils/gdConstants.js'

function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString(undefined, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  })
}

function formatDuration(seconds) {
  if (!seconds) return '—'
  return `${Math.round(seconds / 60)} min`
}

function GDHistoryPage() {
  const navigate = useNavigate()
  const [items, setItems] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [busyId, setBusyId] = useState(null)
  const [errorMessage, setErrorMessage] = useState('')

  const loadHistory = async () => {
    setIsLoading(true)
    try {
      const { data } = await gdApi.getHistory()
      setItems(data.data)
    } catch {
      setErrorMessage('Could not load discussion history.')
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    loadHistory()
  }, [])

  const handleRetry = async (sessionId) => {
    setBusyId(sessionId)
    setErrorMessage('')
    try {
      const { data } = await gdApi.retry(sessionId)
      navigate(`/dashboard/group-discussion/session/${data.data.id}`)
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to restart discussion.')
      setBusyId(null)
    }
  }

  const handleDelete = async (sessionId) => {
    setBusyId(sessionId)
    setErrorMessage('')
    try {
      await gdApi.delete(sessionId)
      await loadHistory()
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to delete discussion.')
    } finally {
      setBusyId(null)
    }
  }

  if (isLoading) {
    return <LoadingSkeleton className="h-96 w-full" />
  }

  return (
    <div className="space-y-5">
      <button
        type="button"
        onClick={() => navigate('/dashboard/group-discussion')}
        className="inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
      >
        <ArrowLeft size={15} />
        Back to Group Discussion
      </button>

      <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">Discussion History</h1>

      {errorMessage && (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      )}

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        {items.length === 0 ? (
          <p className="text-sm text-[var(--dash-muted)] text-center py-6">No discussions yet.</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="text-left text-xs text-[var(--dash-muted)] border-b border-[var(--dash-border)]">
                  <th className="pb-2 pr-4 font-medium">Topic</th>
                  <th className="pb-2 pr-4 font-medium">Category</th>
                  <th className="pb-2 pr-4 font-medium">Duration</th>
                  <th className="pb-2 pr-4 font-medium">Score</th>
                  <th className="pb-2 pr-4 font-medium">Date</th>
                  <th className="pb-2 font-medium text-right">Actions</th>
                </tr>
              </thead>
              <tbody>
                {items.map((item) => (
                  <tr key={item.sessionId} className="border-b border-[var(--dash-border)]/60 last:border-0">
                    <td className="py-3 pr-4 text-[var(--dash-text)] max-w-[240px] truncate">{item.topic}</td>
                    <td className="py-3 pr-4 text-[var(--dash-muted)]">
                      {CATEGORY_LABELS[item.category] || item.category}
                    </td>
                    <td className="py-3 pr-4 text-[var(--dash-muted)]">{formatDuration(item.durationSeconds)}</td>
                    <td className="py-3 pr-4 font-mono text-[var(--dash-text)]">
                      {item.overallScore != null ? Math.round(item.overallScore) : '—'}
                    </td>
                    <td className="py-3 pr-4 text-[var(--dash-muted)]">{formatDate(item.startedAt)}</td>
                    <td className="py-3">
                      <div className="flex items-center justify-end gap-1.5">
                        {item.status === 'COMPLETED' && (
                          <button
                            type="button"
                            onClick={() => navigate(`/dashboard/group-discussion/report/${item.sessionId}`)}
                            className="flex h-7 w-7 items-center justify-center rounded-md text-[var(--dash-muted)] hover:text-signal-amber hover:bg-[var(--dash-surface-hover)] transition-colors"
                            aria-label="View report"
                          >
                            <Eye size={15} />
                          </button>
                        )}
                        <button
                          type="button"
                          onClick={() => handleRetry(item.sessionId)}
                          disabled={busyId === item.sessionId}
                          className="flex h-7 w-7 items-center justify-center rounded-md text-[var(--dash-muted)] hover:text-signal-amber hover:bg-[var(--dash-surface-hover)] transition-colors disabled:opacity-50"
                          aria-label="Retry"
                        >
                          <RefreshCw size={15} className={busyId === item.sessionId ? 'animate-spin' : ''} />
                        </button>
                        <button
                          type="button"
                          onClick={() => handleDelete(item.sessionId)}
                          disabled={busyId === item.sessionId}
                          className="flex h-7 w-7 items-center justify-center rounded-md text-[var(--dash-muted)] hover:text-red-400 hover:bg-red-500/10 transition-colors disabled:opacity-50"
                          aria-label="Delete"
                        >
                          <Trash2 size={15} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}

export default GDHistoryPage
