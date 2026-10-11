function FeatureCard({ icon: Icon, title, description, tag }) {
  return (
    <div className="group relative rounded-2xl border border-ink-line bg-ink-soft p-6 hover:border-signal-amber/40 transition-colors">
      <div className="flex items-start justify-between">
        <span className="flex h-11 w-11 items-center justify-center rounded-xl bg-signal-amber/10 text-signal-amber border border-signal-amber/20 group-hover:bg-signal-amber/20 transition-colors">
          <Icon size={20} strokeWidth={1.8} />
        </span>
        {tag && (
          <span className="font-mono text-[10px] tracking-wide text-signal-mint border border-signal-mint/30 rounded-full px-2 py-0.5">
            {tag}
          </span>
        )}
      </div>
      <h3 className="mt-5 font-display text-lg font-semibold text-paper">{title}</h3>
      <p className="mt-2 text-sm text-muted leading-relaxed">{description}</p>
    </div>
  )
}

export default FeatureCard
