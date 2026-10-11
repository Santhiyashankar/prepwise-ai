import { useNavigate } from 'react-router-dom'
import { Clock, ArrowRight } from 'lucide-react'
import { DIFFICULTY_COLORS, TOPIC_LABELS } from '../../utils/codingConstants.js'

function ProblemCard({ problem }) {
  const navigate = useNavigate()

  return (
    <div className="rounded-xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4 flex items-center justify-between gap-4 hover:border-signal-amber/30 transition-colors">
      <div className="min-w-0">
        <p className="text-sm font-medium text-[var(--dash-text)] truncate">{problem.title}</p>
        <div className="mt-1.5 flex items-center gap-2 flex-wrap text-xs">
          <span className={`rounded-full border px-2 py-0.5 font-medium ${DIFFICULTY_COLORS[problem.difficulty]}`}>
            {problem.difficulty}
          </span>
          <span className="text-[var(--dash-muted)]">{TOPIC_LABELS[problem.topic] || problem.topic}</span>
          {problem.company && (
            <>
              <span className="h-1 w-1 rounded-full bg-[var(--dash-border)]" />
              <span className="text-[var(--dash-muted)]">{problem.company}</span>
            </>
          )}
          <span className="h-1 w-1 rounded-full bg-[var(--dash-border)]" />
          <span className="inline-flex items-center gap-1 text-[var(--dash-muted)]">
            <Clock size={11} />
            {Math.round(problem.timeLimitMs / 1000)}s
          </span>
          <span className="h-1 w-1 rounded-full bg-[var(--dash-border)]" />
          <span className="font-mono text-signal-mint">{problem.acceptanceRate}% acceptance</span>
        </div>
      </div>

      <button
        type="button"
        onClick={() => navigate(`/dashboard/coding/problems/${problem.slug}`)}
        className="inline-flex items-center gap-1.5 rounded-lg bg-signal-amber px-4 py-2 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors shrink-0"
      >
        Start Coding
        <ArrowRight size={14} />
      </button>
    </div>
  )
}

export default ProblemCard
