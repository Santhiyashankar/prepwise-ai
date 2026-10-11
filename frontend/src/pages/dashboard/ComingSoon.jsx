import { useLocation } from 'react-router-dom'
import { Hammer } from 'lucide-react'

function formatTitle(pathname) {
  const segment = pathname.split('/').filter(Boolean).pop() || 'module'
  return segment
    .split('-')
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ')
}

function ComingSoon() {
  const location = useLocation()
  const title = formatTitle(location.pathname)

  return (
    <div className="flex flex-col items-center justify-center text-center gap-4 rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] py-24 px-6">
      <span className="flex h-12 w-12 items-center justify-center rounded-full bg-signal-amber/10 border border-signal-amber/30 text-signal-amber">
        <Hammer size={22} />
      </span>
      <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">{title}</h1>
      <p className="text-sm text-[var(--dash-muted)] max-w-sm">
        This module is being built and will land in an upcoming release.
      </p>
    </div>
  )
}

export default ComingSoon
