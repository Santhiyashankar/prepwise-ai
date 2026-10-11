import { Link } from 'react-router-dom'
import { Mic } from 'lucide-react'

function AuthLayout({ eyebrow, title, subtitle, children, footer }) {
  return (
    <div className="min-h-screen bg-ink flex flex-col">
      <header className="container-page py-6">
        <Link to="/" className="inline-flex items-center gap-2 group w-fit">
          <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-signal-amber/10 border border-signal-amber/30 text-signal-amber group-hover:bg-signal-amber/20 transition-colors">
            <Mic size={18} strokeWidth={2} />
          </span>
          <span className="font-display text-lg font-semibold tracking-tight text-paper">
            PrepWise <span className="text-signal-amber">AI</span>
          </span>
        </Link>
      </header>

      <main className="flex-1 flex items-center justify-center px-6 py-10">
        <div className="w-full max-w-md">
          <div className="text-center mb-8">
            {eyebrow && (
              <span className="font-mono text-xs tracking-wide text-signal-amber">{eyebrow}</span>
            )}
            <h1 className="mt-3 font-display text-2xl sm:text-3xl font-semibold tracking-tight text-paper">
              {title}
            </h1>
            {subtitle && <p className="mt-2 text-sm text-muted leading-relaxed">{subtitle}</p>}
          </div>

          <div className="rounded-2xl border border-ink-line bg-ink-soft p-6 sm:p-8 shadow-2xl shadow-black/30">
            {children}
          </div>

          {footer && <div className="mt-6 text-center text-sm text-muted">{footer}</div>}
        </div>
      </main>
    </div>
  )
}

export default AuthLayout