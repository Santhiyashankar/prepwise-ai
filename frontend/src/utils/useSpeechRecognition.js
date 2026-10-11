import { useCallback, useEffect, useRef, useState } from 'react'

function getRecognitionConstructor() {
  return window.SpeechRecognition || window.webkitSpeechRecognition || null
}

export function useSpeechRecognition({ onResult } = {}) {
  const [isSupported] = useState(() => Boolean(getRecognitionConstructor()))
  const [isListening, setIsListening] = useState(false)
  const [error, setError] = useState('')
  const recognitionRef = useRef(null)

  useEffect(() => {
    if (!isSupported) return undefined

    const RecognitionConstructor = getRecognitionConstructor()
    const recognition = new RecognitionConstructor()
    recognition.continuous = true
    recognition.interimResults = true
    recognition.lang = 'en-US'

    recognition.onresult = (event) => {
      let transcript = ''
      for (let i = event.resultIndex; i < event.results.length; i++) {
        transcript += event.results[i][0].transcript
      }
      onResult?.(transcript)
    }

    recognition.onerror = (event) => {
      setError(event.error === 'not-allowed' ? 'Microphone access was denied.' : 'Voice recognition error.')
      setIsListening(false)
    }

    recognition.onend = () => setIsListening(false)

    recognitionRef.current = recognition

    return () => {
      recognition.onresult = null
      recognition.onerror = null
      recognition.onend = null
      recognition.abort()
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isSupported])

  const start = useCallback(() => {
    if (!recognitionRef.current) return
    setError('')
    try {
      recognitionRef.current.start()
      setIsListening(true)
    } catch {
      // Recognition may already be active; ignore duplicate start calls.
    }
  }, [])

  const stop = useCallback(() => {
    if (!recognitionRef.current) return
    recognitionRef.current.stop()
    setIsListening(false)
  }, [])

  return { isSupported, isListening, error, start, stop }
}

export default useSpeechRecognition
