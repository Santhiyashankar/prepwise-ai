import { ArrowRight } from 'lucide-react'

function CTASection() {
  return (
    <section id="pricing" className="relative overflow-hidden bg-ink py-24 lg:py-32">
      <div
        className="pointer-events-none absolute inset-0"
        style={{
          background:
            'radial-gradient(700px circle at 50% 0%, rgba(255,176,32,0.10), transparent 60%)',
        }}
      />
      <div className="container-page relative text-center">
        <h2 className="font-display text-3xl sm:text-4xl lg:text-5xl font-semibold tracking-tight text-paper max-w-3xl mx-auto">
          Start Your AI Interview Journey Today
        </h2>
        <p className="mt-5 text-muted max-w-xl mx-auto">
          Your first mock interview is on us. See your score before you decide on a plan.
        </p>
        <div className="mt-9 flex flex-col sm:flex-row items-center justify-center gap-4">
          <a
            href="/register"
            className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-7 py-3.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
          >
            Start Preparing
            <ArrowRight size={16} />
          </a>
          <a
            href="#contact"
            className="inline-flex items-center gap-2 rounded-lg border border-ink-line px-7 py-3.5 text-sm font-semibold text-paper hover:border-signal-amber/50 hover:text-signal-amber transition-colors"
          >
            Talk to Us
          </a>
        </div>
      </div>
    </section>
  )
}

export default CTASection
