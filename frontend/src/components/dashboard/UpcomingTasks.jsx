import { CalendarClock, Target, TrendingUp, CheckCircle2 } from 'lucide-react'

function formatDueDate(dateString) {
  if (!dateString) return 'No due date'
  const date = new Date(dateString)
  return date.toLocaleDateString(undefined, { month: 'short', day: 'numeric', hour: 'numeric', minute: '2-digit' })
}

function TaskGroup({ title, icon: Icon, tasks }) {
  return (
    <div>
      <div className="flex items-center gap-2 mb-3">
        <Icon size={15} className="text-signal-amber" />
        <h3 className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">{title}</h3>
      </div>

      {tasks.length === 0 ? (
        <p className="text-sm text-[var(--dash-muted)] pl-1">Nothing scheduled.</p>
      ) : (
        <ul className="space-y-2">
          {tasks.map((task) => (
            <li
              key={task.id}
              className="flex items-center justify-between gap-3 rounded-lg border border-[var(--dash-border)] px-3 py-2.5"
            >
              <span className="text-sm text-[var(--dash-text)] truncate">{task.title}</span>
              <span className="font-mono text-xs text-[var(--dash-muted)] shrink-0">
                {formatDueDate(task.dueDate)}
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

function UpcomingTasks({ tasks, isLoading }) {
  if (isLoading) {
    return (
      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <div className="h-48 rounded-lg bg-[var(--dash-surface-hover)] animate-pulse" />
      </div>
    )
  }

  const scheduledInterviews = tasks?.scheduledInterviews || []
  const dailyGoals = tasks?.dailyGoals || []
  const weeklyTargets = tasks?.weeklyTargets || []
  const isEmpty = scheduledInterviews.length === 0 && dailyGoals.length === 0 && weeklyTargets.length === 0

  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
      <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Upcoming Tasks</h2>

      {isEmpty ? (
        <div className="mt-6 flex flex-col items-center text-center gap-2 py-6">
          <CheckCircle2 size={26} className="text-[var(--dash-muted)]" />
          <p className="text-sm text-[var(--dash-muted)]">You're all caught up. No pending tasks.</p>
        </div>
      ) : (
        <div className="mt-4 grid sm:grid-cols-3 gap-6">
          <TaskGroup title="Scheduled Interviews" icon={CalendarClock} tasks={scheduledInterviews} />
          <TaskGroup title="Daily Goals" icon={Target} tasks={dailyGoals} />
          <TaskGroup title="Weekly Targets" icon={TrendingUp} tasks={weeklyTargets} />
        </div>
      )}
    </div>
  )
}

export default UpcomingTasks
