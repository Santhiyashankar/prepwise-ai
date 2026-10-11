import { Mic, MicOff } from 'lucide-react'
import { useSpeechRecognition } from '../../utils/useSpeechRecognition.js'

function VoiceAnswerButton({ baseText, onTranscriptChange }) {
  const { isSupported, isListening, error, start, stop } = useSpeechRecognition({
    onResult: (transcript) => {
      const separator = baseText && !baseText.endsWith(' ') ? ' ' : ''
      onTranscriptChange(`${baseText}${separator}${transcript}`)
    },
  })

  if (!isSupported) {
    return (
      <span className="text-xs text-[var(--dash-muted)]">
        Voice input isn't supported in this browser.
      </span>
    )
  }

  return (
    <div className="flex items-center gap-2">
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
        {isListening ? 'Stop Recording' : 'Voice Answer'}
      </button>
      {isListening && (
        <span className="flex items-center gap-1.5 text-xs text-red-400">
          <span className="h-1.5 w-1.5 rounded-full bg-red-400 animate-pulse" />
          Listening…
        </span>
      )}
      {error && <span className="text-xs text-red-400">{error}</span>}
    </div>
  )
}

export default VoiceAnswerButton
