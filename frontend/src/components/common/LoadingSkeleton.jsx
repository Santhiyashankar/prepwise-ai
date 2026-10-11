function LoadingSkeleton({ className = 'h-24 w-full', rounded = 'rounded-xl' }) {
  return <div className={`${className} ${rounded} bg-[var(--dash-surface-hover)] animate-pulse`} />
}

export default LoadingSkeleton
