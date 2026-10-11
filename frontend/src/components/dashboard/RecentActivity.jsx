import { FileText, MessageSquareText, Code2, Inbox } from 'lucide-react'

const ICONS_BY_TYPE = {
  INTERVIEW: MessageSquareText,
  RESUME_UPLOAD: FileText,
  CODING_SUBMISSION: Code2,
}

function formatRelativeTime(dateString) {
  const date = new Date(dateString)
  const diffMs = Date.now() - date.getTime()
  const diffMins = Math.floor(diffMs / 60000)

  if (diffMins < 1) return 'Just now'
  if (diffMins < 60) return `${diffMins}m ago`
  const diffHours = Math.floor(diffMins / 60)
  if (diffHours < 24) return `${diffHours}h ago`
  const diffDays = Math.floor(diffHours / 24)
  return `${diffDays}d ago`
}

function RecentActivity({ activities, isLoading }) {
  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
      <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Recent Activity</h2>

      {isLoading ? (
        <div className="mt-4 space-y-3">
          {[1, 2, 3].map((i) => (
            <div key={i} className="h-12 rounded-lg bg-[var(--dash-surface-hover)] animate-pulse" />
          ))}
        </div>
      ) : activities.length === 0 ? (
        <div className="mt-6 flex flex-col items-center text-center gap-2 py-6">
          <Inbox size={26} className="text-[var(--dash-muted)]" />
          <p className="text-sm text-[var(--dash-muted)]">
            No activity yet. Start your first interview to see it here.
          </p>
        </div>
      ) : (
        <ul className="mt-4 space-y-1">
          {activities.map((activity) => {
            const Icon = ICONS_BY_TYPE[activity.activityType] || Inbox
            return (
              <li
                key={activity.id}
                className="flex items-center gap-3 rounded-lg px-2 py-2.5 hover:bg-[var(--dash-surface-hover)] transition-colors"
              >
                <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-signal-amber/10 text-signal-amber border border-signal-amber/20">
                  <Icon size={16} strokeWidth={1.8} />
                </span>
                <div className="min-w-0 flex-1">
                  <p className="text-sm font-medium text-[var(--dash-text)] truncate">{activity.title}</p>
                  {activity.description && (
                    <p className="text-xs text-[var(--dash-muted)] truncate">{activity.description}</p>
                  )}
                </div>
                <div className="text-right shrink-0">
                  {activity.score != null && (
                    <p className="font-mono text-xs text-signal-mint">{activity.score}</p>
                  )}
                  <p className="text-[11px] text-[var(--dash-muted)]">
                    {formatRelativeTime(activity.createdAt)}
                  </p>
                </div>
              </li>
            )
          })}
        </ul>
      )}
    </div>
  )
}

export default RecentActivity
