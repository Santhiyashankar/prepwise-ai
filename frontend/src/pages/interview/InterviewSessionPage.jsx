import { useEffect, useRef, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Clock, Loader2, SkipForward, Square } from 'lucide-react'
import interviewApi from '../../api/interviewApi.js'
import VoiceAnswerButton from '../../components/interview/VoiceAnswerButton.jsx'
import EvaluationOverlay from '../../components/interview/EvaluationOverlay.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import { DIFFICULTY_LABELS } from '../../utils/interviewConstants.js'

const MAX_ANSWER_LENGTH = 4000

function formatTimer(seconds) {
  const minutes = Math.floor(seconds / 60)
  const remaining = seconds % 60
  return `${String(minutes).padStart(2, '0')}:${String(remaining).padStart(2, '0')}`
}

function InterviewSessionPage() {
  const { sessionId } = useParams()
  const navigate = useNavigate()

  const [session, setSession] = useState(null)
  const [question, setQuestion] = useState(null)
  const [answerText, setAnswerText] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [isFinishing, setIsFinishing] = useState(false)
  const [evaluation, setEvaluation] = useState(null)
  const [interviewComplete, setInterviewComplete] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const [elapsedSeconds, setElapsedSeconds] = useState(0)

  const timerRef = useRef(null)
  const questionStartRef = useRef(Date.now())

  useEffect(() => {
    const fetchSession = async () => {
      setIsLoading(true)
      setErrorMessage('')
      try {
        const { data } = await interviewApi.getSession(sessionId)
        setSession(data.data)
        setQuestion(data.data.firstQuestion)
      } catch (error) {
        setErrorMessage(error.response?.data?.message || 'Could not load this interview session.')
      } finally {
        setIsLoading(false)
      }
    }
    fetchSession()
  }, [sessionId])

  useEffect(() => {
    if (!question || evaluation) return undefined
    questionStartRef.current = Date.now()
    setElapsedSeconds(0)
    timerRef.current = setInterval(() => {
      setElapsedSeconds((prev) => prev + 1)
    }, 1000)
    return () => clearInterval(timerRef.current)
  }, [question, evaluation])

  const submitAnswer = async ({ skipped }) => {
    setIsSubmitting(true)
    setErrorMessage('')
    const timeTakenSeconds = Math.round((Date.now() - questionStartRef.current) / 1000)

    try {
      const { data } = await interviewApi.submitAnswer(sessionId, question.questionNumber, {
        answerText: skipped ? '' : answerText,
        skipped,
        timeTakenSeconds,
      })
      setEvaluation(data.data.evaluation)
      setInterviewComplete(data.data.interviewComplete)
      if (!data.data.interviewComplete) {
        setSession((prev) => ({ ...prev, currentQuestionNumber: data.data.nextQuestion.questionNumber }))
      }
      if (data.data.nextQuestion) {
        setQuestion((prev) => ({ ...prev, _pendingNext: data.data.nextQuestion }))
      }
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to submit your answer. Please try again.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleContinue = () => {
    if (interviewComplete) {
      navigate(`/dashboard/interviews/report/${sessionId}`)
      return
    }
    setQuestion(question._pendingNext)
    setAnswerText('')
    setEvaluation(null)
  }

  const handleFinishInterview = async () => {
    setIsFinishing(true)
    setErrorMessage('')
    try {
      await interviewApi.finish(sessionId)
      navigate(`/dashboard/interviews/report/${sessionId}`)
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to finish the interview.')
      setIsFinishing(false)
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-5 max-w-3xl mx-auto">
        <LoadingSkeleton className="h-8 w-64" />
        <LoadingSkeleton className="h-64 w-full" />
      </div>
    )
  }

  if (errorMessage && !session) {
    return (
      <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400 max-w-3xl mx-auto">
        {errorMessage}
      </div>
    )
  }

  if (!question) {
    return (
      <div className="max-w-3xl mx-auto text-center py-16">
        <p className="text-sm text-[var(--dash-muted)]">This interview has already been completed.</p>
        <button
          type="button"
          onClick={() => navigate(`/dashboard/interviews/report/${sessionId}`)}
          className="mt-4 rounded-lg bg-signal-amber px-5 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
        >
          View Report
        </button>
      </div>
    )
  }

  return (
    <div className="max-w-3xl mx-auto space-y-5">
      <div className="flex items-center justify-between">
        <span className="font-mono text-sm text-[var(--dash-muted)]">
          Question {question.questionNumber} / {question.totalQuestions}
        </span>
        <div className="flex items-center gap-3">
          <span className="inline-flex items-center gap-1.5 font-mono text-sm text-[var(--dash-text)]">
            <Clock size={14} />
            {formatTimer(elapsedSeconds)}
          </span>
          <span className="rounded-full border border-signal-amber/30 bg-signal-amber/10 px-3 py-1 font-mono text-xs text-signal-amber">
            {DIFFICULTY_LABELS[session.difficulty] || session.difficulty}
          </span>
        </div>
      </div>

      <div className="h-1.5 w-full rounded-full bg-[var(--dash-border)] overflow-hidden">
        <div
          className="h-full rounded-full bg-signal-amber transition-all duration-500"
          style={{ width: `${(question.questionNumber / question.totalQuestions) * 100}%` }}
        />
      </div>

      {errorMessage && (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      )}

      {evaluation ? (
        <EvaluationOverlay
          evaluation={evaluation}
          isLastQuestion={interviewComplete}
          onContinue={handleContinue}
          isLoadingNext={false}
        />
      ) : (
        <>
          <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">
            <p className="text-base text-[var(--dash-text)] leading-relaxed">{question.questionText}</p>
          </div>

          <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 space-y-3">
            <textarea
              value={answerText}
              onChange={(event) => setAnswerText(event.target.value.slice(0, MAX_ANSWER_LENGTH))}
              rows={8}
              placeholder="Type your answer here, or use voice input below…"
              className="w-full rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] px-4 py-3 text-sm text-[var(--dash-text)] placeholder:text-[var(--dash-muted)]/70 focus:outline-none focus:ring-1 focus:ring-signal-amber/30 focus:border-signal-amber/60 transition-colors resize-none"
            />
            <div className="flex items-center justify-between flex-wrap gap-2">
              <VoiceAnswerButton baseText={answerText} onTranscriptChange={setAnswerText} />
              <span className="font-mono text-xs text-[var(--dash-muted)]">
                {answerText.length} / {MAX_ANSWER_LENGTH}
              </span>
            </div>
          </div>

          <div className="flex items-center justify-between gap-3 flex-wrap">
            <button
              type="button"
              disabled
              className="inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-4 py-2.5 text-sm font-medium text-[var(--dash-muted)] opacity-50 cursor-not-allowed"
              title="Answers are locked in once submitted in an adaptive interview"
            >
              Previous
            </button>

            <div className="flex items-center gap-3">
              <button
                type="button"
                onClick={() => submitAnswer({ skipped: true })}
                disabled={isSubmitting}
                className="inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-4 py-2.5 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 disabled:opacity-60 transition-colors"
              >
                <SkipForward size={15} />
                Skip
              </button>

              <button
                type="button"
                onClick={handleFinishInterview}
                disabled={isFinishing || isSubmitting}
                className="inline-flex items-center gap-2 rounded-lg border border-red-500/30 px-4 py-2.5 text-sm font-medium text-red-400 hover:bg-red-500/10 disabled:opacity-60 transition-colors"
              >
                {isFinishing ? <Loader2 size={15} className="animate-spin" /> : <Square size={15} />}
                Finish Interview
              </button>

              <button
                type="button"
                onClick={() => submitAnswer({ skipped: false })}
                disabled={isSubmitting || !answerText.trim()}
                className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-5 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 disabled:cursor-not-allowed transition-colors"
              >
                {isSubmitting && <Loader2 size={15} className="animate-spin" />}
                {isSubmitting ? 'Evaluating…' : 'Next'}
              </button>
            </div>
          </div>
        </>
      )}
    </div>
  )
}

export default InterviewSessionPage
