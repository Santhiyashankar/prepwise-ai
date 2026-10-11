import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'
import codingApi from '../../api/codingApi.js'
import CodeEditor from '../../components/coding/CodeEditor.jsx'
import TestCaseResultsList from '../../components/coding/TestCaseResultsList.jsx'
import CodeReviewPanel from '../../components/coding/CodeReviewPanel.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'

function SubmissionDetailPage() {
  const { submissionId } = useParams()
  const navigate = useNavigate()
  const [submission, setSubmission] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [errorMessage, setErrorMessage] = useState('')
  const [retryToken, setRetryToken] = useState(0)

  useEffect(() => {
    const loadSubmission = async () => {
      setIsLoading(true)
      setErrorMessage('')
      try {
        const { data } = await codingApi.getSubmission(submissionId)
        setSubmission(data.data)
      } catch (error) {
        setErrorMessage(error.response?.data?.message || 'Could not load this submission.')
      } finally {
        setIsLoading(false)
      }
    }
    loadSubmission()
  }, [submissionId, retryToken])

  if (isLoading) {
    return (
      <div className="space-y-5">
        <LoadingSkeleton className="h-8 w-64" />
        <LoadingSkeleton className="h-96 w-full" />
      </div>
    )
  }

  if (errorMessage || !submission) {
    return (
      <div className="max-w-md mx-auto space-y-4 text-center py-16">
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage || 'Submission not found.'}
        </div>
        <div className="flex items-center justify-center gap-3">
          <button
            type="button"
            onClick={() => navigate('/dashboard/coding/history')}
            className="rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
          >
            Back to History
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

  return (
    <div className="space-y-5">
      <button
        type="button"
        onClick={() => navigate('/dashboard/coding/history')}
        className="inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
      >
        <ArrowLeft size={15} />
        Back to History
      </button>

      <div className="flex items-center justify-between flex-wrap gap-3">
        <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">{submission.problemTitle}</h1>
        <span className="font-mono text-2xl font-semibold text-signal-amber">{Math.round(submission.score)}</span>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
        <div className="rounded-lg border border-[var(--dash-border)] bg-[var(--dash-surface)] p-3 text-center">
          <p className="font-mono text-lg text-[var(--dash-text)]">
            {submission.passedTestCases}/{submission.totalTestCases}
          </p>
          <p className="text-[11px] text-[var(--dash-muted)] mt-0.5">Test Cases</p>
        </div>
        <div className="rounded-lg border border-[var(--dash-border)] bg-[var(--dash-surface)] p-3 text-center">
          <p className="font-mono text-lg text-[var(--dash-text)]">{submission.executionTimeMs ?? '—'}ms</p>
          <p className="text-[11px] text-[var(--dash-muted)] mt-0.5">Execution Time</p>
        </div>
        <div className="rounded-lg border border-[var(--dash-border)] bg-[var(--dash-surface)] p-3 text-center">
          <p className="font-mono text-lg text-[var(--dash-text)]">
            {submission.memoryUsageKb ? `${Math.round(submission.memoryUsageKb / 1024)}MB` : 'N/A'}
          </p>
          <p className="text-[11px] text-[var(--dash-muted)] mt-0.5">Memory</p>
        </div>
        <div className="rounded-lg border border-[var(--dash-border)] bg-[var(--dash-surface)] p-3 text-center">
          <p className="font-mono text-lg text-[var(--dash-text)]">{submission.language}</p>
          <p className="text-[11px] text-[var(--dash-muted)] mt-0.5">Language</p>
        </div>
      </div>

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-3">Submitted Code</p>
        <CodeEditor code={submission.sourceCode} language={submission.language} onChange={() => {}} readOnly />
      </div>

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-3">Test Case Results</p>
        <TestCaseResultsList results={submission.testCaseResults} compileError={submission.compileErrorOutput} />
      </div>

      {submission.report && <CodeReviewPanel report={submission.report} language={submission.language} />}
    </div>
  )
}

export default SubmissionDetailPage
