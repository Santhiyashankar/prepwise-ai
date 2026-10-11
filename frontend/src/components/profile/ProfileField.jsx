function ProfileField({ label, value, icon: Icon }) {
  return (
    <div className="flex items-start gap-3">
      {Icon && (
        <span className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-signal-amber/10 text-signal-amber border border-signal-amber/20">
          <Icon size={14} strokeWidth={1.8} />
        </span>
      )}
      <div className="min-w-0">
        <p className="text-xs text-[var(--dash-muted)]">{label}</p>
        <p className="text-sm font-medium text-[var(--dash-text)] break-words">
          {value || <span className="text-[var(--dash-muted)] font-normal">Not added yet</span>}
        </p>
      </div>
    </div>
  )
}

export default ProfileField
