import { Quote } from 'lucide-react'

const TESTIMONIALS = [
  {
    name: 'Aditi Rao',
    role: 'SDE-1, joined via campus placement',
    quote:
      'The coding round practice caught two blind spots in how I explained complexity trade-offs. Fixed both before my actual interview.',
  },
  {
    name: 'Rohan Mehta',
    role: 'Backend Engineer, career switcher',
    quote:
      'Company-specific mock rounds meant I walked in already knowing the format. The HR round practice removed most of my nerves.',
  },
  {
    name: 'Sneha Iyer',
    role: 'New graduate, first job search',
    quote:
      'The ATS analyzer rewrote my resume summary and I started getting callbacks within the same week.',
  },
]

function Testimonials() {
  return (
    <section className="bg-ink-soft py-24 lg:py-32 border-b border-ink-line">
      <div className="container-page">
        <div className="max-w-2xl">
          <span className="font-mono text-xs tracking-wide text-signal-amber">TESTIMONIALS</span>
          <h2 className="mt-3 font-display text-3xl sm:text-4xl font-semibold tracking-tight text-paper">
            Candidates, not case studies
          </h2>
        </div>

        <div className="mt-14 grid md:grid-cols-3 gap-6">
          {TESTIMONIALS.map((item) => (
            <div
              key={item.name}
              className="rounded-2xl border border-ink-line bg-ink p-6 flex flex-col justify-between"
            >
              <Quote className="text-signal-amber/40" size={28} />
              <p className="mt-4 text-sm text-paper/90 leading-relaxed flex-1">"{item.quote}"</p>
              <div className="mt-6 pt-4 border-t border-ink-line">
                <p className="text-sm font-semibold text-paper">{item.name}</p>
                <p className="text-xs text-muted mt-0.5">{item.role}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

export default Testimonials
