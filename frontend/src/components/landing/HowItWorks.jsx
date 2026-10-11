const STEPS = [
  {
    index: '01',
    title: 'Register',
    description: 'Create your account in under a minute — no resume required to start.',
  },
  {
    index: '02',
    title: 'Upload Resume',
    description: 'Drop in your resume so interviews are built around your real experience.',
  },
  {
    index: '03',
    title: 'Practice Interviews',
    description: 'Run live mock interviews by voice and video across coding, HR, and company rounds.',
  },
  {
    index: '04',
    title: 'Get AI Feedback',
    description: 'Receive a scored transcript with what worked and what to change.',
  },
  {
    index: '05',
    title: 'Improve Skills',
    description: 'Follow a roadmap built from your weakest answers, session over session.',
  },
  {
    index: '06',
    title: 'Crack Your Dream Job',
    description: 'Walk into the real interview having already answered its hardest questions.',
  },
]

function HowItWorks() {
  return (
    <section id="how-it-works" className="bg-ink-soft py-24 lg:py-32 border-y border-ink-line">
      <div className="container-page">
        <div className="max-w-2xl">
          <span className="font-mono text-xs tracking-wide text-signal-amber">HOW IT WORKS</span>
          <h2 className="mt-3 font-display text-3xl sm:text-4xl font-semibold tracking-tight text-paper">
            Six steps, in order
          </h2>
        </div>

        <div className="mt-14 grid md:grid-cols-2 lg:grid-cols-3 gap-x-8 gap-y-10">
          {STEPS.map((step) => (
            <div key={step.index} className="flex gap-4">
              <span className="font-mono text-sm text-signal-amber/70 pt-0.5 shrink-0">
                {step.index}
              </span>
              <div>
                <h3 className="font-display text-base font-semibold text-paper">{step.title}</h3>
                <p className="mt-1.5 text-sm text-muted leading-relaxed">{step.description}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

export default HowItWorks
