import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { ArrowRight } from 'lucide-react'
import codingApi from '../../api/codingApi.js'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import { CODING_DASHBOARD_CARDS } from '../../utils/codingConstants.js'

function CodingDashboardCard({ card }) {
  const navigate = useNavigate()
  return (
    <div
      className="group relative overflow-hidden rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 transition-all hover:-translate-y-1 hover:border-signal-amber/40 hover:shadow-xl hover:shadow-black/10 cursor-pointer"
      onClick={() => navigate(card.to)}
    >
      <div className={`pointer-events-none absolute inset-0 bg-gradient-to-br ${card.gradient}`} />
      <div className="relative">
        <span className="flex h-11 w-11 items-center justify-center rounded-xl bg-signal-amber/10 text-signal-amber border border-signal-amber/25 group-hover:scale-105 transition-transform">
          <card.icon size={20} strokeWidth={1.8} />
        </span>
        <h3 className="mt-4 font-display text-base font-semibold text-[var(--dash-text)]">{card.title}</h3>
        <p className="mt-1.5 text-xs text-[var(--dash-muted)] leading-relaxed min-h-[32px]">{card.description}</p>
        <span className="mt-4 inline-flex items-center gap-1.5 text-xs font-medium text-signal-amber">
          Explore
          <ArrowRight size={13} />
        </span>
      </div>
    </div>
  )
}

function CodingDashboard() {
  const navigate = useNavigate()
  const [history, setHistory] = useState([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const loadHistory = async () => {
      setIsLoading(true)
      try {
        const { data } = await codingApi.getHistory()
        setHistory(data.data)
      } catch {
        setHistory([])
      } finally {
        setIsLoading(false)
      }
    }
    loadHistory()
  }, [])

  return (
    <div className="space-y-6">
      <div>
        <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">Coding Assessment</h1>
        <p className="text-sm text-[var(--dash-muted)] mt-1">
          Solve real problems and get instant AI-powered code reviews.
        </p>
      </div>

      <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {CODING_DASHBOARD_CARDS.map((card) => (
          <CodingDashboardCard key={card.id} card={card} />
        ))}
      </div>

      <div>
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-3">Recent Submissions</h2>
        {isLoading ? (
          <LoadingSkeleton className="h-32 w-full" />
        ) : history.length === 0 ? (
          <p className="text-sm text-[var(--dash-muted)]">No submissions yet. Pick a problem to get started.</p>
        ) : (
          <div className="space-y-2">
            {history.slice(0, 5).map((item) => (
              <div
                key={item.submissionId}
                className="rounded-xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4 flex items-center justify-between"
              >
                <div>
                  <p className="text-sm font-medium text-[var(--dash-text)]">{item.problemTitle}</p>
                  <p className="text-xs text-[var(--dash-muted)] mt-0.5">{item.language}</p>
                </div>
                <div className="flex items-center gap-3">
                  <span className="font-mono text-sm text-signal-mint">{Math.round(item.score)}</span>
                  <button
                    type="button"
                    onClick={() => navigate(`/dashboard/coding/submissions/${item.submissionId}`)}
                    className="text-xs font-medium text-signal-amber hover:underline"
                  >
                    View
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default CodingDashboard
