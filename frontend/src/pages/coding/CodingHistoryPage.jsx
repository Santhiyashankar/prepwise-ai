import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { ArrowLeft, Eye, RefreshCw } from 'lucide-react'
import codingApi from '../../api/codingApi.js'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'

function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString(undefined, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  })
}

const STATUS_STYLES = {
  COMPLETED: 'text-signal-mint',
  COMPILE_ERROR: 'text-red-400',
  RUNTIME_ERROR: 'text-red-400',
  TIMEOUT: 'text-signal-amber',
  PENDING: 'text-[var(--dash-muted)]',
  RUNNING: 'text-[var(--dash-muted)]',
}

function CodingHistoryPage() {
  const navigate = useNavigate()
  const [items, setItems] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [errorMessage, setErrorMessage] = useState('')

  useEffect(() => {
    const loadHistory = async () => {
      setIsLoading(true)
      try {
        const { data } = await codingApi.getHistory()
        setItems(data.data)
      } catch {
        setErrorMessage('Could not load coding history.')
      } finally {
        setIsLoading(false)
      }
    }
    loadHistory()
  }, [])

  if (isLoading) {
    return <LoadingSkeleton className="h-96 w-full" />
  }

  return (
    <div className="space-y-5">
      <button
        type="button"
        onClick={() => navigate('/dashboard/coding')}
        className="inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
      >
        <ArrowLeft size={15} />
        Back to Coding
      </button>

      <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">Coding History</h1>

      {errorMessage && (
        <div className="flex items-center justify-between gap-3 rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          <span>{errorMessage}</span>
          <button
            type="button"
            onClick={loadHistory}
            className="shrink-0 rounded-md border border-red-500/30 px-3 py-1 text-xs font-medium hover:bg-red-500/10 transition-colors"
          >
            Retry
          </button>
        </div>
      )}

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        {items.length === 0 ? (
          <p className="text-sm text-[var(--dash-muted)] text-center py-6">No submissions yet.</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="text-left text-xs text-[var(--dash-muted)] border-b border-[var(--dash-border)]">
                  <th className="pb-2 pr-4 font-medium">Problem</th>
                  <th className="pb-2 pr-4 font-medium">Language</th>
                  <th className="pb-2 pr-4 font-medium">Score</th>
                  <th className="pb-2 pr-4 font-medium">Status</th>
                  <th className="pb-2 pr-4 font-medium">Date</th>
                  <th className="pb-2 font-medium text-right">Actions</th>
                </tr>
              </thead>
              <tbody>
                {items.map((item) => (
                  <tr key={item.submissionId} className="border-b border-[var(--dash-border)]/60 last:border-0">
                    <td className="py-3 pr-4 text-[var(--dash-text)]">{item.problemTitle}</td>
                    <td className="py-3 pr-4 text-[var(--dash-muted)]">{item.language}</td>
                    <td className="py-3 pr-4 font-mono text-[var(--dash-text)]">{Math.round(item.score)}</td>
                    <td className={`py-3 pr-4 text-xs font-medium ${STATUS_STYLES[item.status] || ''}`}>
                      {item.status.replace('_', ' ')}
                    </td>
                    <td className="py-3 pr-4 text-[var(--dash-muted)]">{formatDate(item.submittedAt)}</td>
                    <td className="py-3">
                      <div className="flex items-center justify-end gap-1.5">
                        <button
                          type="button"
                          onClick={() => navigate(`/dashboard/coding/submissions/${item.submissionId}`)}
                          className="flex h-7 w-7 items-center justify-center rounded-md text-[var(--dash-muted)] hover:text-signal-amber hover:bg-[var(--dash-surface-hover)] transition-colors"
                          aria-label="View solution"
                        >
                          <Eye size={15} />
                        </button>
                        <button
                          type="button"
                          onClick={() => navigate(`/dashboard/coding/problems/${item.problemSlug}`)}
                          className="flex h-7 w-7 items-center justify-center rounded-md text-[var(--dash-muted)] hover:text-signal-amber hover:bg-[var(--dash-surface-hover)] transition-colors"
                          aria-label="Retry"
                        >
                          <RefreshCw size={15} />
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

export default CodingHistoryPage
