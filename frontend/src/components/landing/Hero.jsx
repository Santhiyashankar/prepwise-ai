import { Play, ArrowRight } from 'lucide-react'

function TranscriptPanel() {
  return (
    <div className="relative rounded-2xl border border-ink-line bg-ink-soft shadow-2xl shadow-black/40 animate-rise">
      <div className="flex items-center justify-between border-b border-ink-line px-5 py-3">
        <div className="flex items-center gap-2">
          <span className="h-2 w-2 rounded-full bg-signal-amber animate-blink" />
          <span className="font-mono text-xs tracking-wide text-signal-amber">REC · SESSION 04:12</span>
        </div>
        <span className="font-mono text-xs text-muted">SDE-2 · Backend Round</span>
      </div>

      <div className="px-5 py-5 space-y-4">
        <div className="flex gap-3">
          <span className="font-mono text-xs text-muted pt-1 shrink-0">AI</span>
          <p className="text-sm text-paper/90 leading-relaxed">
            Walk me through how you'd design a rate limiter for a public API handling 10k requests
            per second.
          </p>
        </div>

        <div className="flex gap-3">
          <span className="font-mono text-xs text-signal-mint pt-1 shrink-0">YOU</span>
          <div className="flex items-end gap-1 h-6">
            {[1, 2, 3, 4, 5].map((bar) => (
              <span
                key={bar}
                className={`w-1 rounded-full bg-signal-mint/70 animate-wave${bar} h-full origin-bottom`}
              />
            ))}
            <span className="ml-2 text-sm text-muted italic">listening…</span>
          </div>
        </div>

        <div className="flex gap-3 opacity-60">
          <span className="font-mono text-xs text-muted pt-1 shrink-0">AI</span>
          <p className="text-sm text-paper/70 leading-relaxed">
            Good start — now, what happens when the token bucket resets mid-burst?
          </p>
        </div>
      </div>

      <div className="border-t border-ink-line px-5 py-3 flex items-center justify-between">
        <span className="font-mono text-xs text-muted">Confidence score</span>
        <span className="font-mono text-xs text-signal-mint">82 / 100</span>
      </div>
    </div>
  )
}

function Hero() {
  return (
    <section id="home" className="relative overflow-hidden bg-ink pt-32 pb-24 lg:pt-44 lg:pb-32">
      <div
        className="pointer-events-none absolute inset-0 opacity-40"
        style={{
          background:
            'radial-gradient(600px circle at 15% 20%, rgba(255,176,32,0.12), transparent 60%), radial-gradient(500px circle at 85% 60%, rgba(74,222,128,0.08), transparent 60%)',
        }}
      />

      <div className="container-page relative grid lg:grid-cols-2 gap-16 items-center">
        <div>
          <span className="inline-flex items-center gap-2 rounded-full border border-ink-line bg-ink-soft px-3 py-1 font-mono text-xs text-muted">
            <span className="h-1.5 w-1.5 rounded-full bg-signal-mint" />
            Now practicing 12,400 mock interviews / week
          </span>

          <h1 className="mt-6 font-display text-4xl sm:text-5xl lg:text-6xl font-semibold leading-[1.05] tracking-tight text-paper">
            An AI interviewer that actually
            <span className="text-signal-amber"> talks back.</span>
          </h1>

          <p className="mt-6 max-w-xl text-lg text-muted leading-relaxed">
            PrepWise AI runs full mock interviews over voice and video — resume screens, coding
            rounds, HR rounds — then hands you a transcript, a score, and exactly what to fix
            before the real one.
          </p>

          <div className="mt-9 flex flex-col sm:flex-row gap-4">
            <a
              href="/register"
              className="inline-flex items-center justify-center gap-2 rounded-lg bg-signal-amber px-6 py-3.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
            >
              Start Preparing
              <ArrowRight size={16} />
            </a>
            <a
              href="#how-it-works"
              className="inline-flex items-center justify-center gap-2 rounded-lg border border-ink-line px-6 py-3.5 text-sm font-semibold text-paper hover:border-signal-amber/50 hover:text-signal-amber transition-colors"
            >
              <Play size={16} />
              Watch Demo
            </a>
          </div>

          <div className="mt-10 flex items-center gap-8 font-mono text-xs text-muted">
            <span>NO CREDIT CARD</span>
            <span className="h-1 w-1 rounded-full bg-ink-line" />
            <span>SETUP IN 2 MIN</span>
          </div>
        </div>

        <TranscriptPanel />
      </div>
    </section>
  )
}

export default Hero
