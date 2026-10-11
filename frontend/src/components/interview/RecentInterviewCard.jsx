import { RefreshCw, FileText } from 'lucide-react'
import { DIFFICULTY_LABELS, ROLE_LABELS, STYLE_LABELS } from '../../utils/interviewConstants.js'

function formatDuration(seconds) {
  if (!seconds) return '—'
  const minutes = Math.round(seconds / 60)
  return `${minutes} min`
}

function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString(undefined, { month: 'short', day: 'numeric' })
}

function RecentInterviewCard({ item, onViewReport, onRetry }) {
  const isCompleted = item.status === 'COMPLETED'

  return (
    <div className="rounded-xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4 flex items-center justify-between gap-4">
      <div className="min-w-0">
        <p className="text-sm font-medium text-[var(--dash-text)] truncate">
          {ROLE_LABELS[item.role] || item.role} · {STYLE_LABELS[item.style] || item.style}
        </p>
        <div className="mt-1 flex items-center gap-2 text-xs text-[var(--dash-muted)]">
          <span>{DIFFICULTY_LABELS[item.difficulty] || item.difficulty}</span>
          <span className="h-1 w-1 rounded-full bg-[var(--dash-border)]" />
          <span>{formatDuration(item.durationSeconds)}</span>
          <span className="h-1 w-1 rounded-full bg-[var(--dash-border)]" />
          <span>{formatDate(item.startedAt)}</span>
        </div>
      </div>

      <div className="flex items-center gap-3 shrink-0">
        {isCompleted && item.overallScore != null && (
          <span className="font-mono text-sm text-signal-mint">{Math.round(item.overallScore)}</span>
        )}
        {isCompleted ? (
          <button
            type="button"
            onClick={() => onViewReport(item.sessionId)}
            className="inline-flex items-center gap-1.5 rounded-lg border border-[var(--dash-border)] px-3 py-1.5 text-xs font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
          >
            <FileText size={13} />
            Report
          </button>
        ) : (
          <span className="text-xs text-[var(--dash-muted)]">In progress</span>
        )}
        <button
          type="button"
          onClick={() => onRetry(item.sessionId)}
          className="inline-flex items-center gap-1.5 rounded-lg border border-[var(--dash-border)] px-3 py-1.5 text-xs font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
        >
          <RefreshCw size={13} />
          Retry
        </button>
      </div>
    </div>
  )
}

export default RecentInterviewCard
