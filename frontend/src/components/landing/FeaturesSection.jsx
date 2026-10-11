import {
  FileText,
  Building2,
  Code2,
  Users,
  Compass,
  ScanSearch,
  CalendarClock,
  TrendingUp,
  MessageSquareText,
  Map,
} from 'lucide-react'
import FeatureCard from './FeatureCard.jsx'

const FEATURES = [
  {
    icon: FileText,
    title: 'AI Resume Interview',
    description: 'Upload your resume and get interviewed on the exact projects and skills listed in it.',
  },
  {
    icon: Building2,
    title: 'Company Specific Interviews',
    description: 'Practice interview patterns modeled on real hiring bars from specific companies.',
    tag: 'POPULAR',
  },
  {
    icon: Code2,
    title: 'Coding Interview',
    description: 'Solve live coding problems while the AI probes your approach and complexity trade-offs.',
  },
  {
    icon: Users,
    title: 'HR Interview',
    description: 'Rehearse behavioral and HR rounds with follow-up questions that adapt to your answers.',
  },
  {
    icon: Compass,
    title: 'AI Mentor',
    description: 'Get a standing AI mentor that tracks your weak areas across every session.',
  },
  {
    icon: ScanSearch,
    title: 'ATS Resume Analyzer',
    description: 'Score your resume against applicant tracking systems before you ever hit submit.',
  },
  {
    icon: CalendarClock,
    title: 'Study Planner',
    description: 'A day-by-day prep plan generated from your target role and interview date.',
  },
  {
    icon: TrendingUp,
    title: 'Placement Predictor',
    description: 'A live estimate of your readiness, updated after every practice interview.',
  },
  {
    icon: MessageSquareText,
    title: 'AI Feedback',
    description: 'Line-by-line feedback on clarity, correctness, and confidence for every answer.',
  },
  {
    icon: Map,
    title: 'Career Roadmap',
    description: 'A structured skill roadmap from where you are now to your target job title.',
  },
]

function FeaturesSection() {
  return (
    <section id="features" className="bg-ink py-24 lg:py-32">
      <div className="container-page">
        <div className="max-w-2xl">
          <span className="font-mono text-xs tracking-wide text-signal-amber">FEATURES</span>
          <h2 className="mt-3 font-display text-3xl sm:text-4xl font-semibold tracking-tight text-paper">
            Everything between you and the offer letter
          </h2>
          <p className="mt-4 text-muted leading-relaxed">
            Ten tools, one panel. Built to cover resume screens, technical rounds, and HR rounds
            without switching apps.
          </p>
        </div>

        <div className="mt-14 grid sm:grid-cols-2 lg:grid-cols-3 gap-5">
          {FEATURES.map((feature) => (
            <FeatureCard key={feature.title} {...feature} />
          ))}
        </div>
      </div>
    </section>
  )
}

export default FeaturesSection
