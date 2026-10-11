import { useEffect, useRef, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Clock, Loader2, Mic, MicOff, Send, Square } from 'lucide-react'
import gdApi from '../../api/gdApi.js'
import GDMessageBubble from '../../components/gd/GDMessageBubble.jsx'
import GDParticipantsList from '../../components/gd/GDParticipantsList.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import { useSpeechRecognition } from '../../utils/useSpeechRecognition.js'

function formatTimer(seconds) {
  const minutes = Math.floor(seconds / 60)
  const remaining = seconds % 60
  return `${String(minutes).padStart(2, '0')}:${String(remaining).padStart(2, '0')}`
}

function GDSessionPage() {
  const { sessionId } = useParams()
  const navigate = useNavigate()

  const [session, setSession] = useState(null)
  const [messages, setMessages] = useState([])
  const [inputText, setInputText] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [isSending, setIsSending] = useState(false)
  const [isFinishing, setIsFinishing] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const [remainingSeconds, setRemainingSeconds] = useState(null)

  const chatEndRef = useRef(null)
  const timerRef = useRef(null)

  const { isSupported, isListening, start, stop } = useSpeechRecognition({
    onResult: (transcript) => {
      setInputText((prev) => {
        const separator = prev && !prev.endsWith(' ') ? ' ' : ''
        return `${prev}${separator}${transcript}`
      })
    },
  })

  useEffect(() => {
    const loadSession = async () => {
      setIsLoading(true)
      setErrorMessage('')
      try {
        const { data } = await gdApi.getSession(sessionId)
        setSession(data.data)
        setMessages(data.data.messages)
        if (data.data.status === 'IN_PROGRESS') {
          setRemainingSeconds(data.data.durationMinutes * 60)
        }
      } catch (error) {
        setErrorMessage(error.response?.data?.message || 'Could not load this discussion.')
      } finally {
        setIsLoading(false)
      }
    }
    loadSession()
  }, [sessionId])

  useEffect(() => {
    if (remainingSeconds == null) return undefined
    timerRef.current = setInterval(() => {
      setRemainingSeconds((prev) => (prev != null && prev > 0 ? prev - 1 : 0))
    }, 1000)
    return () => clearInterval(timerRef.current)
  }, [remainingSeconds != null])

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const handleSend = async () => {
    if (!inputText.trim() || isSending) return

    const content = inputText.trim()
    setInputText('')
    setIsSending(true)
    setErrorMessage('')

    try {
      const { data } = await gdApi.sendMessage(sessionId, content)
      setMessages((prev) => [...prev, data.data.userMessage, ...data.data.aiMessages])
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to send message.')
      setInputText(content)
    } finally {
      setIsSending(false)
    }
  }

  const handleFinish = async () => {
    setIsFinishing(true)
    setErrorMessage('')
    try {
      await gdApi.finish(sessionId)
      navigate(`/dashboard/group-discussion/report/${sessionId}`)
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to finish discussion.')
      setIsFinishing(false)
    }
  }

  const findPersonality = (participantId) =>
    session?.participants.find((participant) => participant.id === participantId)?.personality

  if (isLoading) {
    return (
      <div className="space-y-5">
        <LoadingSkeleton className="h-10 w-64" />
        <LoadingSkeleton className="h-96 w-full" />
      </div>
    )
  }

  if (errorMessage && !session) {
    return (
      <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
        {errorMessage}
      </div>
    )
  }

  const isCompleted = session.status !== 'IN_PROGRESS'

  return (
    <div className="grid lg:grid-cols-[280px_1fr] gap-5">
      <div className="hidden lg:block">
        <GDParticipantsList participants={session.participants} />
      </div>

      <div className="space-y-4">
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <div className="flex items-center justify-between flex-wrap gap-2">
            <h1 className="font-display text-base font-semibold text-[var(--dash-text)] max-w-lg">
              {session.topic}
            </h1>
            {remainingSeconds != null && (
              <span
                className={`inline-flex items-center gap-1.5 rounded-full border px-3 py-1 font-mono text-sm ${
                  remainingSeconds <= 30
                    ? 'border-red-500/30 bg-red-500/10 text-red-400'
                    : 'border-signal-amber/30 bg-signal-amber/10 text-signal-amber'
                }`}
              >
                <Clock size={13} />
                {formatTimer(remainingSeconds)}
              </span>
            )}
          </div>
        </div>

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 h-[420px] overflow-y-auto space-y-4">
          {messages.map((message) => (
            <GDMessageBubble key={message.id} message={message} personality={findPersonality(message.participantId)} />
          ))}
          <div ref={chatEndRef} />
        </div>

        {errorMessage && (
          <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
            {errorMessage}
          </div>
        )}

        {!isCompleted ? (
          <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4 space-y-3">
            <textarea
              value={inputText}
              onChange={(event) => setInputText(event.target.value)}
              onKeyDown={(event) => {
                if (event.key === 'Enter' && !event.shiftKey) {
                  event.preventDefault()
                  handleSend()
                }
              }}
              rows={2}
              placeholder="Share your point with the group…"
              className="w-full rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] px-4 py-2.5 text-sm text-[var(--dash-text)] placeholder:text-[var(--dash-muted)]/70 focus:outline-none focus:ring-1 focus:ring-signal-amber/30 focus:border-signal-amber/60 transition-colors resize-none"
            />
            <div className="flex items-center justify-between gap-2 flex-wrap">
              <div className="flex items-center gap-2">
                {isSupported && (
                  <button
                    type="button"
                    onClick={() => (isListening ? stop() : start())}
                    className={`inline-flex items-center gap-2 rounded-lg border px-3.5 py-2 text-sm font-medium transition-colors ${
                      isListening
                        ? 'border-red-500/40 bg-red-500/10 text-red-400'
                        : 'border-[var(--dash-border)] text-[var(--dash-text)] hover:border-signal-amber/40'
                    }`}
                  >
                    {isListening ? <MicOff size={15} /> : <Mic size={15} />}
                    {isListening ? 'Stop' : 'Voice Input'}
                  </button>
                )}
                <button
                  type="button"
                  onClick={handleFinish}
                  disabled={isFinishing}
                  className="inline-flex items-center gap-2 rounded-lg border border-red-500/30 px-3.5 py-2 text-sm font-medium text-red-400 hover:bg-red-500/10 disabled:opacity-60 transition-colors"
                >
                  {isFinishing ? <Loader2 size={15} className="animate-spin" /> : <Square size={15} />}
                  Finish Discussion
                </button>
              </div>
              <button
                type="button"
                onClick={handleSend}
                disabled={isSending || !inputText.trim()}
                className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-5 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 disabled:cursor-not-allowed transition-colors"
              >
                {isSending ? <Loader2 size={15} className="animate-spin" /> : <Send size={15} />}
                {isSending ? 'Sending…' : 'Send'}
              </button>
            </div>
          </div>
        ) : (
          <div className="text-center py-4">
            <button
              type="button"
              onClick={() => navigate(`/dashboard/group-discussion/report/${sessionId}`)}
              className="rounded-lg bg-signal-amber px-5 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
            >
              View Report
            </button>
          </div>
        )}
      </div>
    </div>
  )
}

export default GDSessionPage
