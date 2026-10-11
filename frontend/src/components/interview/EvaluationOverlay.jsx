import { ThumbsUp, ThumbsDown, Lightbulb, ArrowRight, Loader2 } from 'lucide-react'

function getScoreColor(score) {
  if (score >= 75) return 'text-signal-mint'
  if (score >= 50) return 'text-signal-amber'
  return 'text-red-400'
}

function EvaluationOverlay({ evaluation, isLastQuestion, onContinue, isLoadingNext }) {
  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 space-y-5">
      <div className="flex items-center justify-between">
        <h3 className="font-display text-base font-semibold text-[var(--dash-text)]">Answer Feedback</h3>
        <span className={`font-mono text-2xl font-semibold ${getScoreColor(evaluation.score)}`}>
          {Math.round(evaluation.score)}
        </span>
      </div>

      {evaluation.strengths?.length > 0 && (
        <div>
          <div className="flex items-center gap-2 mb-2">
            <ThumbsUp size={14} className="text-signal-mint" />
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">Strengths</p>
          </div>
          <ul className="space-y-1.5">
            {evaluation.strengths.map((item, index) => (
              <li key={index} className="text-sm text-[var(--dash-text)] flex gap-2">
                <span className="text-signal-mint">•</span>
                {item}
              </li>
            ))}
          </ul>
        </div>
      )}

      {evaluation.mistakes?.length > 0 && (
        <div>
          <div className="flex items-center gap-2 mb-2">
            <ThumbsDown size={14} className="text-red-400" />
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">
              Areas to Improve
            </p>
          </div>
          <ul className="space-y-1.5">
            {evaluation.mistakes.map((item, index) => (
              <li key={index} className="text-sm text-[var(--dash-text)] flex gap-2">
                <span className="text-red-400">•</span>
                {item}
              </li>
            ))}
          </ul>
        </div>
      )}

      {evaluation.idealAnswer && (
        <div>
          <div className="flex items-center gap-2 mb-2">
            <Lightbulb size={14} className="text-signal-amber" />
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">Ideal Answer</p>
          </div>
          <p className="text-sm text-[var(--dash-text)] leading-relaxed">{evaluation.idealAnswer}</p>
        </div>
      )}

      <button
        type="button"
        onClick={onContinue}
        disabled={isLoadingNext}
        className="w-full inline-flex items-center justify-center gap-2 rounded-lg bg-signal-amber px-5 py-3 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 transition-colors"
      >
        {isLoadingNext ? (
          <Loader2 size={16} className="animate-spin" />
        ) : (
          <>
            {isLastQuestion ? 'View Final Report' : 'Next Question'}
            <ArrowRight size={16} />
          </>
        )}
      </button>
    </div>
  )
}

export default EvaluationOverlay
