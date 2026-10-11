import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { ArrowLeft, Loader2, Play, RotateCcw, Send } from 'lucide-react'
import codingApi from '../../api/codingApi.js'
import CodeEditor from '../../components/coding/CodeEditor.jsx'
import TestCaseResultsList from '../../components/coding/TestCaseResultsList.jsx'
import CodeReviewPanel from '../../components/coding/CodeReviewPanel.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import { DIFFICULTY_COLORS, LANGUAGE_OPTIONS, TOPIC_LABELS } from '../../utils/codingConstants.js'

function useAssessmentTimer() {
  const [remainingMs, setRemainingMs] = useState(null)

  useEffect(() => {
    const endTime = sessionStorage.getItem('prepwise_timed_assessment_end')
    if (!endTime) return undefined

    const tick = () => {
      const remaining = Number(endTime) - Date.now()
      setRemainingMs(remaining > 0 ? remaining : 0)
    }
    tick()
    const interval = setInterval(tick, 1000)
    return () => clearInterval(interval)
  }, [])

  return remainingMs
}

function formatMs(ms) {
  const totalSeconds = Math.floor(ms / 1000)
  const minutes = Math.floor(totalSeconds / 60)
  const seconds = totalSeconds % 60
  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`
}

function ProblemSolvePage() {
  const { slug } = useParams()
  const navigate = useNavigate()
  const assessmentRemainingMs = useAssessmentTimer()

  const [problem, setProblem] = useState(null)
  const [language, setLanguage] = useState('JAVA')
  const [code, setCode] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [isRunning, setIsRunning] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [runResult, setRunResult] = useState(null)
  const [submitResult, setSubmitResult] = useState(null)
  const [errorMessage, setErrorMessage] = useState('')
  const [activeResultTab, setActiveResultTab] = useState('run')
  const [loadAttempt, setLoadAttempt] = useState(0)

  useEffect(() => {
    const loadProblem = async () => {
      setIsLoading(true)
      setErrorMessage('')
      try {
        const { data } = await codingApi.getProblem(slug)
        setProblem(data.data)
        setCode(data.data.starterCode[language] || '')
      } catch (error) {
        setErrorMessage(error.response?.data?.message || 'Could not load this problem.')
      } finally {
        setIsLoading(false)
      }
    }
    loadProblem()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [slug, loadAttempt])

  const handleLanguageChange = (nextLanguage) => {
    setLanguage(nextLanguage)
    if (problem) {
      setCode(problem.starterCode[nextLanguage] || '')
    }
    setRunResult(null)
    setSubmitResult(null)
  }

  const handleReset = () => {
    if (problem) setCode(problem.starterCode[language] || '')
    setRunResult(null)
    setSubmitResult(null)
  }

  const handleRun = async () => {
    setIsRunning(true)
    setErrorMessage('')
    setSubmitResult(null)
    try {
      const { data } = await codingApi.runCode({ problemId: problem.id, language, sourceCode: code })
      setRunResult(data.data)
      setActiveResultTab('run')
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to run code.')
    } finally {
      setIsRunning(false)
    }
  }

  const handleSubmit = async () => {
    setIsSubmitting(true)
    setErrorMessage('')
    setRunResult(null)
    try {
      const { data } = await codingApi.submitCode({ problemId: problem.id, language, sourceCode: code })
      setSubmitResult(data.data)
      setActiveResultTab('submit')
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to submit solution.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const charCount = useMemo(() => code.length, [code])

  if (isLoading) {
    return (
      <div className="space-y-5">
        <LoadingSkeleton className="h-8 w-64" />
        <LoadingSkeleton className="h-96 w-full" />
      </div>
    )
  }

  if (errorMessage && !problem) {
    return (
      <div className="max-w-md mx-auto space-y-4 text-center py-16">
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
        <div className="flex items-center justify-center gap-3">
          <button
            type="button"
            onClick={() => navigate('/dashboard/coding/problems')}
            className="rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
          >
            Back to Problems
          </button>
          <button
            type="button"
            onClick={() => setLoadAttempt((prev) => prev + 1)}
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
      <div className="flex items-center justify-between flex-wrap gap-3">
        <button
          type="button"
          onClick={() => navigate('/dashboard/coding/problems')}
          className="inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
        >
          <ArrowLeft size={15} />
          Back to Problems
        </button>

        {assessmentRemainingMs != null && (
          <span className="rounded-full border border-red-500/30 bg-red-500/10 px-4 py-1.5 font-mono text-sm text-red-400">
            Assessment time left: {formatMs(assessmentRemainingMs)}
          </span>
        )}
      </div>

      <div className="grid lg:grid-cols-2 gap-5">
        <div className="space-y-4">
          <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
            <div className="flex items-center justify-between flex-wrap gap-2">
              <h1 className="font-display text-lg font-semibold text-[var(--dash-text)]">{problem.title}</h1>
              <div className="flex items-center gap-2">
                <span className={`rounded-full border px-2.5 py-0.5 text-xs font-medium ${DIFFICULTY_COLORS[problem.difficulty]}`}>
                  {problem.difficulty}
                </span>
                <span className="text-xs text-[var(--dash-muted)]">{TOPIC_LABELS[problem.topic] || problem.topic}</span>
              </div>
            </div>
            {problem.lastScore != null && (
              <p className="mt-2 text-xs text-[var(--dash-muted)]">
                Last score: <span className="font-mono text-signal-mint">{Math.round(problem.lastScore)}</span>
              </p>
            )}
            <pre className="mt-4 text-sm text-[var(--dash-text)] leading-relaxed whitespace-pre-wrap font-sans">
              {problem.description}
            </pre>
          </div>

          {(runResult || submitResult) && (
            <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
              <div className="flex gap-1 border-b border-[var(--dash-border)] mb-4">
                {runResult && (
                  <button
                    type="button"
                    onClick={() => setActiveResultTab('run')}
                    className={`px-3 py-2 text-sm font-medium border-b-2 transition-colors ${
                      activeResultTab === 'run'
                        ? 'border-signal-amber text-signal-amber'
                        : 'border-transparent text-[var(--dash-muted)]'
                    }`}
                  >
                    Run Output
                  </button>
                )}
                {submitResult && (
                  <button
                    type="button"
                    onClick={() => setActiveResultTab('submit')}
                    className={`px-3 py-2 text-sm font-medium border-b-2 transition-colors ${
                      activeResultTab === 'submit'
                        ? 'border-signal-amber text-signal-amber'
                        : 'border-transparent text-[var(--dash-muted)]'
                    }`}
                  >
                    Submission Report
                  </button>
                )}
              </div>

              {activeResultTab === 'run' && runResult && (
                <TestCaseResultsList results={runResult.results} compileError={runResult.compileErrorOutput} />
              )}

              {activeResultTab === 'submit' && submitResult && (
                <div className="space-y-4">
                  <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
                    <div className="rounded-lg bg-[var(--dash-bg)] p-3 text-center">
                      <p className="font-mono text-lg text-signal-amber">{Math.round(submitResult.score)}</p>
                      <p className="text-[11px] text-[var(--dash-muted)] mt-0.5">Score</p>
                    </div>
                    <div className="rounded-lg bg-[var(--dash-bg)] p-3 text-center">
                      <p className="font-mono text-lg text-[var(--dash-text)]">
                        {submitResult.passedTestCases}/{submitResult.totalTestCases}
                      </p>
                      <p className="text-[11px] text-[var(--dash-muted)] mt-0.5">Test Cases</p>
                    </div>
                    <div className="rounded-lg bg-[var(--dash-bg)] p-3 text-center">
                      <p className="font-mono text-lg text-[var(--dash-text)]">
                        {submitResult.executionTimeMs ?? '—'}ms
                      </p>
                      <p className="text-[11px] text-[var(--dash-muted)] mt-0.5">Execution Time</p>
                    </div>
                    <div className="rounded-lg bg-[var(--dash-bg)] p-3 text-center">
                      <p className="font-mono text-lg text-[var(--dash-text)]">
                        {submitResult.memoryUsageKb ? `${Math.round(submitResult.memoryUsageKb / 1024)}MB` : 'N/A'}
                      </p>
                      <p className="text-[11px] text-[var(--dash-muted)] mt-0.5">Memory Usage</p>
                    </div>
                  </div>
                  <TestCaseResultsList
                    results={submitResult.testCaseResults}
                    compileError={submitResult.compileErrorOutput}
                  />
                </div>
              )}
            </div>
          )}

          {submitResult?.report && <CodeReviewPanel report={submitResult.report} language={language} />}
        </div>

        <div className="space-y-4">
          <div className="flex items-center justify-between flex-wrap gap-2">
            <div className="flex gap-1.5">
              {LANGUAGE_OPTIONS.map((option) => (
                <button
                  key={option.value}
                  type="button"
                  onClick={() => handleLanguageChange(option.value)}
                  className={`rounded-lg border px-3 py-1.5 text-xs font-medium transition-colors ${
                    language === option.value
                      ? 'border-signal-amber/50 bg-signal-amber/15 text-signal-amber'
                      : 'border-[var(--dash-border)] text-[var(--dash-muted)] hover:text-[var(--dash-text)]'
                  }`}
                >
                  {option.label}
                </button>
              ))}
            </div>
            <span className="font-mono text-xs text-[var(--dash-muted)]">{charCount} chars</span>
          </div>

          <CodeEditor code={code} language={language} onChange={setCode} />

          {errorMessage && (
            <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
              {errorMessage}
            </div>
          )}

          <div className="flex items-center gap-3 flex-wrap">
            <button
              type="button"
              onClick={handleReset}
              disabled={isRunning || isSubmitting}
              className="inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-4 py-2.5 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 disabled:opacity-60 transition-colors"
            >
              <RotateCcw size={15} />
              Reset Code
            </button>
            <button
              type="button"
              onClick={handleRun}
              disabled={isRunning || isSubmitting}
              className="inline-flex items-center gap-2 rounded-lg border border-signal-mint/30 bg-signal-mint/10 px-4 py-2.5 text-sm font-medium text-signal-mint hover:bg-signal-mint/15 disabled:opacity-60 transition-colors"
            >
              {isRunning ? <Loader2 size={15} className="animate-spin" /> : <Play size={15} />}
              {isRunning ? 'Running…' : 'Run Code'}
            </button>
            <button
              type="button"
              onClick={handleSubmit}
              disabled={isSubmitting || isRunning}
              className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-5 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 transition-colors ml-auto"
            >
              {isSubmitting ? <Loader2 size={15} className="animate-spin" /> : <Send size={15} />}
              {isSubmitting ? 'Submitting…' : 'Submit Solution'}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ProblemSolvePage
