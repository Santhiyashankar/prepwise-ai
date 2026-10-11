import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'
import codingApi from '../../api/codingApi.js'
import ProblemFilters from '../../components/coding/ProblemFilters.jsx'
import ProblemCard from '../../components/coding/ProblemCard.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'

function ProblemListPage() {
  const navigate = useNavigate()
  const [searchParams, setSearchParams] = useSearchParams()

  const difficulty = searchParams.get('difficulty')
  const topic = searchParams.get('topic')
  const company = searchParams.get('company')

  const [problems, setProblems] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [errorMessage, setErrorMessage] = useState('')
  const [retryToken, setRetryToken] = useState(0)

  useEffect(() => {
    const loadProblems = async () => {
      setIsLoading(true)
      setErrorMessage('')
      try {
        const { data } = await codingApi.listProblems({ difficulty, topic, company })
        setProblems(data.data)
      } catch {
        setErrorMessage('Could not load problems.')
      } finally {
        setIsLoading(false)
      }
    }
    loadProblems()
  }, [difficulty, topic, company, retryToken])

  const handleFilterChange = (next) => {
    const params = {}
    if (next.difficulty) params.difficulty = next.difficulty
    if (next.topic) params.topic = next.topic
    if (next.company) params.company = next.company
    setSearchParams(params)
  }

  return (
    <div className="space-y-5">
      <button
        type="button"
        onClick={() => navigate('/dashboard/coding')}
        className="inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
      >
        <ArrowLeft size={15} />
        Back to Coding
      </button>

      <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">Problem Set</h1>

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <ProblemFilters difficulty={difficulty} topic={topic} company={company} onChange={handleFilterChange} />
      </div>

      {errorMessage && (
        <div className="flex items-center justify-between gap-3 rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          <span>{errorMessage}</span>
          <button
            type="button"
            onClick={() => setRetryToken((prev) => prev + 1)}
            className="shrink-0 rounded-md border border-red-500/30 px-3 py-1 text-xs font-medium hover:bg-red-500/10 transition-colors"
          >
            Retry
          </button>
        </div>
      )}

      {isLoading ? (
        <div className="space-y-3">
          <LoadingSkeleton className="h-20 w-full" />
          <LoadingSkeleton className="h-20 w-full" />
          <LoadingSkeleton className="h-20 w-full" />
        </div>
      ) : problems.length === 0 ? (
        <p className="text-sm text-[var(--dash-muted)] text-center py-8">No problems match these filters.</p>
      ) : (
        <div className="space-y-3">
          {problems.map((problem) => (
            <ProblemCard key={problem.id} problem={problem} />
          ))}
        </div>
      )}
    </div>
  )
}

export default ProblemListPage
