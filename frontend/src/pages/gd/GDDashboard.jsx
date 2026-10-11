import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { History, Users, Clock } from 'lucide-react'
import gdApi from '../../api/gdApi.js'
import GDSetupModal from '../../components/gd/GDSetupModal.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import { GD_DASHBOARD_CARDS } from '../../utils/gdConstants.js'

function GDCard({ card, onStart }) {
  return (
    <div className="group relative overflow-hidden rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 transition-all hover:-translate-y-1 hover:border-signal-amber/40 hover:shadow-xl hover:shadow-black/10">
      <div className={`pointer-events-none absolute inset-0 bg-gradient-to-br ${card.gradient}`} />
      <div className="relative">
        <span className="flex h-11 w-11 items-center justify-center rounded-xl bg-signal-amber/10 text-signal-amber border border-signal-amber/25 group-hover:scale-105 transition-transform">
          <card.icon size={20} strokeWidth={1.8} />
        </span>
        <h3 className="mt-4 font-display text-base font-semibold text-[var(--dash-text)]">{card.title}</h3>
        <p className="mt-1.5 text-xs text-[var(--dash-muted)] leading-relaxed min-h-[32px]">{card.description}</p>

        <div className="mt-4 flex items-center gap-3 text-xs text-[var(--dash-muted)]">
          <span>{card.difficulty}</span>
          <span className="h-1 w-1 rounded-full bg-[var(--dash-border)]" />
          <span className="inline-flex items-center gap-1">
            <Clock size={11} />
            {card.duration}
          </span>
          <span className="h-1 w-1 rounded-full bg-[var(--dash-border)]" />
          <span className="inline-flex items-center gap-1">
            <Users size={11} />
            {card.participants}
          </span>
        </div>

        <button
          type="button"
          onClick={() => onStart(card.category)}
          className="mt-4 w-full inline-flex items-center justify-center gap-2 rounded-lg bg-signal-amber px-4 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
        >
          Start Discussion
        </button>
      </div>
    </div>
  )
}

function GDDashboard() {
  const navigate = useNavigate()
  const [history, setHistory] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [modalCategory, setModalCategory] = useState(null)
  const [isStarting, setIsStarting] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  useEffect(() => {
    const loadHistory = async () => {
      setIsLoading(true)
      try {
        const { data } = await gdApi.getHistory()
        setHistory(data.data)
      } catch {
        setHistory([])
      } finally {
        setIsLoading(false)
      }
    }
    loadHistory()
  }, [])

  const handleStart = async (payload) => {
    setIsStarting(true)
    setErrorMessage('')
    try {
      const { data } = await gdApi.start(payload)
      navigate(`/dashboard/group-discussion/session/${data.data.id}`)
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to start discussion. Please try again.')
      setIsStarting(false)
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div>
          <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">Group Discussion Simulator</h1>
          <p className="text-sm text-[var(--dash-muted)] mt-1">
            Practice with AI participants who each bring a different personality to the table.
          </p>
        </div>
        <button
          type="button"
          onClick={() => navigate('/dashboard/group-discussion/history')}
          className="inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
        >
          <History size={15} />
          History
        </button>
      </div>

      {errorMessage && (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      )}

      <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {GD_DASHBOARD_CARDS.map((card) => (
          <GDCard key={card.category} card={card} onStart={setModalCategory} />
        ))}
      </div>

      <div>
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-3">Recent Discussions</h2>
        {isLoading ? (
          <LoadingSkeleton className="h-24 w-full" />
        ) : history.length === 0 ? (
          <p className="text-sm text-[var(--dash-muted)]">No discussions yet. Pick a topic above to begin.</p>
        ) : (
          <div className="space-y-2">
            {history.slice(0, 5).map((item) => (
              <div
                key={item.sessionId}
                className="rounded-xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4 flex items-center justify-between gap-3"
              >
                <p className="text-sm text-[var(--dash-text)] truncate flex-1">{item.topic}</p>
                <div className="flex items-center gap-3 shrink-0">
                  {item.overallScore != null && (
                    <span className="font-mono text-sm text-signal-mint">{Math.round(item.overallScore)}</span>
                  )}
                  <button
                    type="button"
                    onClick={() =>
                      navigate(
                        item.status === 'COMPLETED'
                          ? `/dashboard/group-discussion/report/${item.sessionId}`
                          : `/dashboard/group-discussion/session/${item.sessionId}`
                      )
                    }
                    className="text-xs font-medium text-signal-amber hover:underline"
                  >
                    {item.status === 'COMPLETED' ? 'View Report' : 'Resume'}
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {modalCategory && (
        <GDSetupModal
          initialCategory={modalCategory}
          onClose={() => setModalCategory(null)}
          onStart={handleStart}
          isStarting={isStarting}
        />
      )}
    </div>
  )
}

export default GDDashboard
