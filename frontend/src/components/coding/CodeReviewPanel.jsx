import { ThumbsUp, ThumbsDown, Lightbulb, Gauge } from 'lucide-react'
import CodeEditor from './CodeEditor.jsx'

function ScoreBar({ label, value }) {
  return (
    <div>
      <div className="flex items-center justify-between mb-1">
        <span className="text-xs text-[var(--dash-text)]">{label}</span>
        <span className="font-mono text-xs text-[var(--dash-muted)]">{Math.round(value)}</span>
      </div>
      <div className="h-1.5 w-full rounded-full bg-[var(--dash-border)] overflow-hidden">
        <div
          className="h-full rounded-full bg-signal-amber transition-all duration-700"
          style={{ width: `${Math.min(100, value)}%` }}
        />
      </div>
    </div>
  )
}

function CodeReviewPanel({ report, language }) {
  if (!report) return null

  return (
    <div className="space-y-5">
      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <div className="flex items-center justify-between mb-4">
          <h3 className="font-display text-base font-semibold text-[var(--dash-text)] flex items-center gap-2">
            <Gauge size={16} className="text-signal-amber" />
            AI Code Review
          </h3>
          <span className="font-mono text-2xl font-semibold text-signal-amber">
            {Math.round(report.overallScore)}
          </span>
        </div>

        <div className="grid sm:grid-cols-2 gap-4 mb-4">
          <ScoreBar label="Correctness" value={report.correctnessScore} />
          <ScoreBar label="Readability" value={report.readabilityScore} />
          <ScoreBar label="Best Practices" value={report.bestPracticesScore} />
          <ScoreBar label="Edge Case Handling" value={report.edgeCaseScore} />
        </div>

        <div className="grid sm:grid-cols-2 gap-4 text-sm">
          <div className="rounded-lg bg-[var(--dash-bg)] p-3">
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">Time Complexity</p>
            <p className="mt-1 font-mono text-signal-mint">{report.timeComplexity || 'N/A'}</p>
          </div>
          <div className="rounded-lg bg-[var(--dash-bg)] p-3">
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">Space Complexity</p>
            <p className="mt-1 font-mono text-signal-mint">{report.spaceComplexity || 'N/A'}</p>
          </div>
        </div>
      </div>

      <div className="grid lg:grid-cols-2 gap-5">
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <div className="flex items-center gap-2 mb-2">
            <ThumbsUp size={14} className="text-signal-mint" />
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">Strengths</p>
          </div>
          <ul className="space-y-1.5">
            {report.strengths?.map((item, index) => (
              <li key={index} className="text-sm text-[var(--dash-text)] flex gap-2">
                <span className="text-signal-mint">•</span>
                {item}
              </li>
            ))}
          </ul>
        </div>

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <div className="flex items-center gap-2 mb-2">
            <ThumbsDown size={14} className="text-red-400" />
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">Weaknesses</p>
          </div>
          <ul className="space-y-1.5">
            {report.weaknesses?.map((item, index) => (
              <li key={index} className="text-sm text-[var(--dash-text)] flex gap-2">
                <span className="text-red-400">•</span>
                {item}
              </li>
            ))}
          </ul>
        </div>
      </div>

      {report.optimizationSuggestions?.length > 0 && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <div className="flex items-center gap-2 mb-2">
            <Lightbulb size={14} className="text-signal-amber" />
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">
              Optimization Suggestions
            </p>
          </div>
          <ul className="space-y-1.5">
            {report.optimizationSuggestions.map((item, index) => (
              <li key={index} className="text-sm text-[var(--dash-text)] flex gap-2">
                <span className="text-signal-amber">•</span>
                {item}
              </li>
            ))}
          </ul>
        </div>
      )}

      {report.optimizedSolution && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-3">
            Optimized Solution
          </p>
          <CodeEditor code={report.optimizedSolution} language={language} onChange={() => {}} readOnly />
        </div>
      )}
    </div>
  )
}

export default CodeReviewPanel
