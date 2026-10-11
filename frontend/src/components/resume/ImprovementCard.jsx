function ImprovementCard({ title, icon: Icon, items = [], tone = 'neutral' }) {
  const toneStyles = {
    positive: 'text-signal-mint border-signal-mint/20 bg-signal-mint/10',
    negative: 'text-red-400 border-red-500/20 bg-red-500/10',
    neutral: 'text-signal-amber border-signal-amber/20 bg-signal-amber/10',
  }

  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
      <div className="flex items-center gap-2.5">
        {Icon && (
          <span className={`flex h-8 w-8 items-center justify-center rounded-lg border ${toneStyles[tone]}`}>
            <Icon size={15} strokeWidth={1.8} />
          </span>
        )}
        <h3 className="font-display text-sm font-semibold text-[var(--dash-text)]">{title}</h3>
      </div>

      {items.length === 0 ? (
        <p className="mt-3 text-sm text-[var(--dash-muted)]">Nothing to show here yet.</p>
      ) : (
        <ul className="mt-3 space-y-2">
          {items.map((item, index) => (
            <li key={index} className="flex gap-2 text-sm text-[var(--dash-text)] leading-relaxed">
              <span className="text-[var(--dash-muted)] shrink-0">•</span>
              <span>{item}</span>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

export default ImprovementCard
