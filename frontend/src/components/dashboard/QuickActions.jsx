import { useNavigate } from 'react-router-dom'
import { PlayCircle, Upload, ScanSearch, Code2, Compass } from 'lucide-react'

const ACTIONS = [
  { label: 'Start Interview', icon: PlayCircle, to: '/dashboard/interviews', accent: true },
  { label: 'Upload Resume', icon: Upload, to: '/dashboard/resume' },
  { label: 'Resume Analyzer', icon: ScanSearch, to: '/dashboard/resume' },
  { label: 'Coding Practice', icon: Code2, to: '/dashboard/coding' },
  { label: 'AI Mentor', icon: Compass, to: '/dashboard/ai-mentor' },
]

function QuickActions() {
  const navigate = useNavigate()

  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
      <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Quick Actions</h2>
      <div className="mt-4 grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3">
        {ACTIONS.map((action) => (
          <button
            key={action.label}
            type="button"
            onClick={() => navigate(action.to)}
            className={`flex flex-col items-center justify-center gap-2 rounded-xl border px-3 py-4 text-center transition-colors ${
              action.accent
                ? 'border-signal-amber/30 bg-signal-amber/10 text-signal-amber hover:bg-signal-amber/15'
                : 'border-[var(--dash-border)] text-[var(--dash-muted)] hover:border-signal-amber/30 hover:text-[var(--dash-text)]'
            }`}
          >
            <action.icon size={20} strokeWidth={1.8} />
            <span className="text-xs font-medium leading-tight">{action.label}</span>
          </button>
        ))}
      </div>
    </div>
  )
}

export default QuickActions
