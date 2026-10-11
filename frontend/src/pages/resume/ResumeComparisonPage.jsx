import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'
import resumeApi from '../../api/resumeApi.js'
import ResumeComparison from '../../components/resume/ResumeComparison.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'

function ResumeComparisonPage() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const firstId = searchParams.get('a')
  const secondId = searchParams.get('b')

  const [comparison, setComparison] = useState(null)
  const [isLoading, setIsLoading] = useState(Boolean(firstId && secondId))
  const [errorMessage, setErrorMessage] = useState('')

  useEffect(() => {
    if (!firstId || !secondId) return
    const fetchComparison = async () => {
      setIsLoading(true)
      setErrorMessage('')
      try {
        const { data } = await resumeApi.compare(firstId, secondId)
        setComparison(data.data)
      } catch (error) {
        setErrorMessage(error.response?.data?.message || 'Could not load comparison.')
      } finally {
        setIsLoading(false)
      }
    }
    fetchComparison()
  }, [firstId, secondId])

  return (
    <div className="space-y-5">
      <button
        type="button"
        onClick={() => navigate('/dashboard/resume/history')}
        className="inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
      >
        <ArrowLeft size={15} />
        Back to History
      </button>

      <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">Compare Resumes</h1>

      {!firstId || !secondId ? (
        <p className="text-sm text-[var(--dash-muted)]">
          Select two resumes from your{' '}
          <button
            type="button"
            onClick={() => navigate('/dashboard/resume/history')}
            className="text-signal-amber hover:underline"
          >
            history
          </button>{' '}
          to compare them here.
        </p>
      ) : isLoading ? (
        <LoadingSkeleton className="h-96 w-full" />
      ) : errorMessage ? (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      ) : (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <ResumeComparison first={comparison?.first} second={comparison?.second} />
        </div>
      )}
    </div>
  )
}

export default ResumeComparisonPage
