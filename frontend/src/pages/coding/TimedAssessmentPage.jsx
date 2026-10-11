import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { ArrowLeft, Loader2, Timer } from 'lucide-react'
import codingApi from '../../api/codingApi.js'
import ProblemCard from '../../components/coding/ProblemCard.jsx'
import { DIFFICULTY_OPTIONS } from '../../utils/codingConstants.js'

function TimedAssessmentPage() {
  const navigate = useNavigate()
  const [difficulty, setDifficulty] = useState('MEDIUM')
  const [problemCount, setProblemCount] = useState(3)
  const [assessment, setAssessment] = useState(null)
  const [isGenerating, setIsGenerating] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  const handleGenerate = async () => {
    setIsGenerating(true)
    setErrorMessage('')
    try {
      const { data } = await codingApi.generateTimedAssessment({ difficulty, problemCount })
      setAssessment(data.data)
      const endTime = Date.now() + data.data.totalTimeLimitMs
      sessionStorage.setItem('prepwise_timed_assessment_end', String(endTime))
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to generate assessment.')
    } finally {
      setIsGenerating(false)
    }
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

      <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">Timed Assessment</h1>

      {errorMessage && (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      )}

      {!assessment ? (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 space-y-5 max-w-lg">
          <div>
            <p className="text-sm font-medium text-[var(--dash-text)] mb-2">Difficulty</p>
            <div className="flex gap-2">
              {DIFFICULTY_OPTIONS.map((option) => (
                <button
                  key={option}
                  type="button"
                  onClick={() => setDifficulty(option)}
                  className={`rounded-lg border px-3.5 py-2 text-sm font-medium transition-colors ${
                    difficulty === option
                      ? 'border-signal-amber/50 bg-signal-amber/15 text-signal-amber'
                      : 'border-[var(--dash-border)] text-[var(--dash-muted)]'
                  }`}
                >
                  {option.charAt(0) + option.slice(1).toLowerCase()}
                </button>
              ))}
            </div>
          </div>

          <div>
            <p className="text-sm font-medium text-[var(--dash-text)] mb-2">Number of Problems</p>
            <div className="flex gap-2">
              {[2, 3, 5].map((count) => (
                <button
                  key={count}
                  type="button"
                  onClick={() => setProblemCount(count)}
                  className={`rounded-lg border px-3.5 py-2 text-sm font-medium transition-colors ${
                    problemCount === count
                      ? 'border-signal-amber/50 bg-signal-amber/15 text-signal-amber'
                      : 'border-[var(--dash-border)] text-[var(--dash-muted)]'
                  }`}
                >
                  {count}
                </button>
              ))}
            </div>
          </div>

          <button
            type="button"
            onClick={handleGenerate}
            disabled={isGenerating}
            className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-5 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 transition-colors"
          >
            {isGenerating ? <Loader2 size={16} className="animate-spin" /> : <Timer size={16} />}
            {isGenerating ? 'Preparing…' : 'Start Assessment'}
          </button>
        </div>
      ) : (
        <div className="space-y-3">
          <p className="text-sm text-[var(--dash-muted)]">
            Total time budget: {Math.round(assessment.totalTimeLimitMs / 60000)} minutes. Solve each problem in turn.
          </p>
          {assessment.problems.map((problem) => (
            <ProblemCard key={problem.id} problem={problem} />
          ))}
        </div>
      )}
    </div>
  )
}

export default TimedAssessmentPage
