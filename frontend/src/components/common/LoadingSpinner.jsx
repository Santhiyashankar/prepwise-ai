function LoadingSpinner({ label = 'Loading' }) {
  return (
    <div className="flex min-h-screen items-center justify-center bg-ink">
      <div className="flex flex-col items-center gap-4">
        <div className="h-10 w-10 rounded-full border-2 border-ink-line border-t-signal-amber animate-spin" />
        <span className="font-mono text-xs tracking-wide text-muted">{label}…</span>
      </div>
    </div>
  )
}

export default LoadingSpinner