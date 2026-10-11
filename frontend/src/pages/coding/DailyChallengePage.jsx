import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import codingApi from '../../api/codingApi.js'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'

function DailyChallengePage() {
  const navigate = useNavigate()
  const [errorMessage, setErrorMessage] = useState('')
  const [retryToken, setRetryToken] = useState(0)

  useEffect(() => {
    let isCancelled = false

    const loadDailyChallenge = async () => {
      setErrorMessage('')
      try {
        const { data } = await codingApi.getDailyChallenge()
        if (!isCancelled) {
          navigate(`/dashboard/coding/problems/${data.data.slug}`, { replace: true })
        }
      } catch (error) {
        if (!isCancelled) {
          setErrorMessage(error.response?.data?.message || "Could not load today's challenge.")
        }
      }
    }
    loadDailyChallenge()

    return () => {
      isCancelled = true
    }
  }, [navigate, retryToken])

  if (errorMessage) {
    return (
      <div className="max-w-md mx-auto space-y-4 text-center py-16">
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
        <div className="flex items-center justify-center gap-3">
          <button
            type="button"
            onClick={() => navigate('/dashboard/coding')}
            className="rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
          >
            Back to Coding
          </button>
          <button
            type="button"
            onClick={() => setRetryToken((prev) => prev + 1)}
            className="rounded-lg bg-signal-amber px-4 py-2 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
          >
            Try Again
          </button>
        </div>
      </div>
    )
  }

  return <LoadingSkeleton className="h-64 w-full" />
}

export default DailyChallengePage
