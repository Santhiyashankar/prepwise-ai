import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { History } from 'lucide-react'
import interviewApi from '../../api/interviewApi.js'
import InterviewCard from '../../components/interview/InterviewCard.jsx'
import InterviewSetupModal from '../../components/interview/InterviewSetupModal.jsx'
import RecentInterviewCard from '../../components/interview/RecentInterviewCard.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import { INTERVIEW_CARDS } from '../../utils/interviewConstants.js'

function InterviewDashboard() {
  const navigate = useNavigate()
  const [history, setHistory] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [modalStyle, setModalStyle] = useState(null)
  const [isStarting, setIsStarting] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  const loadHistory = async () => {
    setIsLoading(true)
    try {
      const { data } = await interviewApi.getHistory()
      setHistory(data.data)
    } catch {
      setErrorMessage('Could not load your interview history.')
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    loadHistory()
  }, [])

  const lastScoreByStyle = INTERVIEW_CARDS.reduce((acc, card) => {
    const match = history.find((item) => item.style === card.style && item.overallScore != null)
    acc[card.style] = match?.overallScore ?? null
    return acc
  }, {})

  const handleStart = async (payload) => {
    setIsStarting(true)
    setErrorMessage('')
    try {
      const { data } = await interviewApi.start(payload)
      navigate(`/dashboard/interviews/session/${data.data.id}`)
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to start interview. Please try again.')
      setIsStarting(false)
    }
  }

  const handleRetry = async (sessionId) => {
    setErrorMessage('')
    try {
      const { data } = await interviewApi.retry(sessionId)
      navigate(`/dashboard/interviews/session/${data.data.id}`)
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to restart interview.')
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div>
          <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">AI Interview Simulator</h1>
          <p className="text-sm text-[var(--dash-muted)] mt-1">
            Practice live, one question at a time, with feedback that adapts to your answers.
          </p>
        </div>
        <button
          type="button"
          onClick={() => navigate('/dashboard/interviews/history')}
          className="inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
        >
          <History size={15} />
          Full History
        </button>
      </div>

      {errorMessage && (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      )}

      <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {INTERVIEW_CARDS.map((card) => (
          <InterviewCard
            key={card.style}
            card={card}
            lastScore={lastScoreByStyle[card.style]}
            onStart={setModalStyle}
          />
        ))}
      </div>

      <div>
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-3">Recent Interviews</h2>
        {isLoading ? (
          <div className="space-y-3">
            <LoadingSkeleton className="h-16 w-full" />
            <LoadingSkeleton className="h-16 w-full" />
          </div>
        ) : history.length === 0 ? (
          <p className="text-sm text-[var(--dash-muted)]">
            No interviews yet. Pick a card above to start your first one.
          </p>
        ) : (
          <div className="space-y-3">
            {history.slice(0, 5).map((item) => (
              <RecentInterviewCard
                key={item.sessionId}
                item={item}
                onViewReport={(sessionId) => navigate(`/dashboard/interviews/report/${sessionId}`)}
                onRetry={handleRetry}
              />
            ))}
          </div>
        )}
      </div>

      {modalStyle && (
        <InterviewSetupModal
          initialStyle={modalStyle}
          onClose={() => setModalStyle(null)}
          onStart={handleStart}
          isStarting={isStarting}
        />
      )}
    </div>
  )
}

export default InterviewDashboard
