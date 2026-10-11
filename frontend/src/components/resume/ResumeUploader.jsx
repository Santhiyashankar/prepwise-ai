import { useState } from 'react'
import { CheckCircle2, Loader2, Sparkles, TriangleAlert } from 'lucide-react'
import resumeApi from '../../api/resumeApi.js'
import ResumeDropzone from './ResumeDropzone.jsx'

const STAGES = {
  IDLE: 'idle',
  UPLOADING: 'uploading',
  PARSING: 'parsing',
  ANALYZING: 'analyzing',
  DONE: 'done',
  ERROR: 'error',
}

function ResumeUploader({ onComplete }) {
  const [stage, setStage] = useState(STAGES.IDLE)
  const [progress, setProgress] = useState(0)
  const [errorMessage, setErrorMessage] = useState('')

  const handleFileSelected = async (file) => {
    setStage(STAGES.UPLOADING)
    setProgress(0)
    setErrorMessage('')

    try {
      const { data } = await resumeApi.upload(file, (event) => {
        if (event.total) {
          setProgress(Math.round((event.loaded / event.total) * 100))
        }
      })
      const resume = data.data

      setStage(STAGES.PARSING)
      await new Promise((resolve) => setTimeout(resolve, 900))

      setStage(STAGES.ANALYZING)
      const { data: analysisData } = await resumeApi.analyze(resume.id)

      setStage(STAGES.DONE)
      onComplete(analysisData.data)
    } catch (error) {
      setStage(STAGES.ERROR)
      const status = error.response?.status
      if (status === 504) {
        setErrorMessage('The AI service timed out. Please try analyzing again.')
      } else if (status === 400) {
        setErrorMessage(error.response?.data?.message || 'This file could not be processed.')
      } else if (!error.response) {
        setErrorMessage('Network error. Check your connection and try again.')
      } else {
        setErrorMessage(error.response?.data?.message || 'Something went wrong. Please try again.')
      }
    }
  }

  const handleRetry = () => {
    setStage(STAGES.IDLE)
    setErrorMessage('')
  }

  if (stage === STAGES.IDLE || stage === STAGES.DONE) {
    return <ResumeDropzone onFileSelected={handleFileSelected} />
  }

  if (stage === STAGES.ERROR) {
    return (
      <div className="flex flex-col items-center justify-center gap-3 rounded-2xl border border-red-500/30 bg-red-500/5 px-6 py-10 text-center">
        <span className="flex h-12 w-12 items-center justify-center rounded-full bg-red-500/10 border border-red-500/30 text-red-400">
          <TriangleAlert size={22} />
        </span>
        <p className="text-sm text-red-400 max-w-sm">{errorMessage}</p>
        <button
          type="button"
          onClick={handleRetry}
          className="mt-1 rounded-lg border border-red-500/30 px-4 py-2 text-sm font-medium text-red-400 hover:bg-red-500/10 transition-colors"
        >
          Try again
        </button>
      </div>
    )
  }

  const stageConfig = {
    [STAGES.UPLOADING]: {
      icon: <Loader2 size={22} className="animate-spin" />,
      title: 'Uploading your resume…',
      subtitle: `${progress}% complete`,
    },
    [STAGES.PARSING]: {
      icon: <Loader2 size={22} className="animate-spin" />,
      title: 'Reading your resume…',
      subtitle: 'Extracting contact details, skills, and sections',
    },
    [STAGES.ANALYZING]: {
      icon: <Sparkles size={22} className="animate-pulse" />,
      title: 'Gemini is analyzing your resume…',
      subtitle: 'Scoring, matching keywords, and generating feedback',
    },
  }[stage]

  return (
    <div className="flex flex-col items-center justify-center gap-4 rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] px-6 py-12 text-center">
      <span className="flex h-14 w-14 items-center justify-center rounded-full bg-signal-amber/10 border border-signal-amber/25 text-signal-amber">
        {stageConfig.icon}
      </span>
      <div>
        <p className="text-sm font-medium text-[var(--dash-text)]">{stageConfig.title}</p>
        <p className="mt-1 text-xs text-[var(--dash-muted)]">{stageConfig.subtitle}</p>
      </div>
      {stage === STAGES.UPLOADING && (
        <div className="h-1.5 w-48 rounded-full bg-[var(--dash-border)] overflow-hidden">
          <div
            className="h-full rounded-full bg-signal-amber transition-all duration-300"
            style={{ width: `${progress}%` }}
          />
        </div>
      )}
      {stage === STAGES.DONE && <CheckCircle2 className="text-signal-mint" size={20} />}
    </div>
  )
}

export default ResumeUploader
