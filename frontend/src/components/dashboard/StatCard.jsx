function StatCard({ icon: Icon, label, value, suffix = '', accent = 'amber' }) {
  const accentClasses = {
    amber: 'bg-signal-amber/10 text-signal-amber border-signal-amber/25',
    mint: 'bg-signal-mint/10 text-signal-mint border-signal-mint/25',
  }

  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 hover:border-signal-amber/30 transition-colors">
      <div className="flex items-center justify-between">
        <span
          className={`flex h-10 w-10 items-center justify-center rounded-xl border ${accentClasses[accent]}`}
        >
          <Icon size={18} strokeWidth={1.8} />
        </span>
      </div>
      <p className="mt-4 font-mono text-2xl font-medium text-[var(--dash-text)]">
        {value}
        {suffix}
      </p>
      <p className="mt-1 text-xs text-[var(--dash-muted)]">{label}</p>
    </div>
  )
}

export default StatCard
