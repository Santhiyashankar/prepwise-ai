import { PlayCircle } from 'lucide-react'

function InterviewCard({ card, lastScore, onStart }) {
  return (
    <div
      className={`group relative overflow-hidden rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 transition-all hover:-translate-y-1 hover:border-signal-amber/40 hover:shadow-xl hover:shadow-black/10`}
    >
      <div className={`pointer-events-none absolute inset-0 bg-gradient-to-br ${card.gradient}`} />

      <div className="relative">
        <div className="flex items-start justify-between">
          <span className="flex h-11 w-11 items-center justify-center rounded-xl bg-signal-amber/10 text-signal-amber border border-signal-amber/25 group-hover:scale-105 transition-transform">
            <card.icon size={20} strokeWidth={1.8} />
          </span>
          {lastScore != null && (
            <span className="font-mono text-[10px] tracking-wide text-signal-mint border border-signal-mint/30 rounded-full px-2 py-0.5">
              LAST {Math.round(lastScore)}
            </span>
          )}
        </div>

        <h3 className="mt-4 font-display text-base font-semibold text-[var(--dash-text)]">{card.title}</h3>
        <p className="mt-1.5 text-xs text-[var(--dash-muted)] leading-relaxed min-h-[32px]">{card.description}</p>

        <div className="mt-4 flex items-center gap-3 text-xs text-[var(--dash-muted)]">
          <span>{card.duration}</span>
          <span className="h-1 w-1 rounded-full bg-[var(--dash-border)]" />
          <span>{card.difficulty}</span>
        </div>

        <button
          type="button"
          onClick={() => onStart(card.style)}
          className="mt-4 w-full inline-flex items-center justify-center gap-2 rounded-lg bg-signal-amber px-4 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
        >
          <PlayCircle size={16} />
          Start
        </button>
      </div>
    </div>
  )
}

export default InterviewCard
