import { useEffect, useState } from 'react'
import { X, Loader2 } from 'lucide-react'
import {
  DIFFICULTY_OPTIONS,
  EXPERIENCE_OPTIONS,
  QUESTION_COUNT_OPTIONS,
  ROLE_OPTIONS,
  STYLE_OPTIONS,
} from '../../utils/interviewConstants.js'

function OptionGroup({ label, options, value, onChange }) {
  return (
    <div>
      <p className="text-sm font-medium text-[var(--dash-text)] mb-2">{label}</p>
      <div className="flex flex-wrap gap-2">
        {options.map((option) => {
          const optionValue = typeof option === 'object' ? option.value : option
          const optionLabel = typeof option === 'object' ? option.label : option
          const isActive = value === optionValue
          return (
            <button
              key={optionValue}
              type="button"
              onClick={() => onChange(optionValue)}
              className={`rounded-lg border px-3.5 py-2 text-sm font-medium transition-colors ${
                isActive
                  ? 'border-signal-amber/50 bg-signal-amber/15 text-signal-amber'
                  : 'border-[var(--dash-border)] text-[var(--dash-muted)] hover:border-signal-amber/30 hover:text-[var(--dash-text)]'
              }`}
            >
              {optionLabel}
            </button>
          )
        })}
      </div>
    </div>
  )
}

function InterviewSetupModal({ initialStyle, onClose, onStart, isStarting }) {
  const [role, setRole] = useState(ROLE_OPTIONS[0].value)
  const [difficulty, setDifficulty] = useState('MEDIUM')
  const [experience, setExperience] = useState('FRESHER')
  const [questionCount, setQuestionCount] = useState(5)
  const [style, setStyle] = useState('TECHNICAL')

  useEffect(() => {
    if (initialStyle && STYLE_OPTIONS.some((option) => option.value === initialStyle)) {
      setStyle(initialStyle)
    }
  }, [initialStyle])

  const handleSubmit = () => {
    onStart({ role, difficulty, experience, style, questionCount })
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 px-4">
      <div className="w-full max-w-lg rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between mb-5">
          <h2 className="font-display text-lg font-semibold text-[var(--dash-text)]">Set Up Your Interview</h2>
          <button
            type="button"
            onClick={onClose}
            className="text-[var(--dash-muted)] hover:text-[var(--dash-text)] transition-colors"
            aria-label="Close"
          >
            <X size={20} />
          </button>
        </div>

        <div className="space-y-5">
          <div>
            <p className="text-sm font-medium text-[var(--dash-text)] mb-2">Role</p>
            <select
              value={role}
              onChange={(event) => setRole(event.target.value)}
              className="w-full rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] px-4 py-2.5 text-sm text-[var(--dash-text)] focus:outline-none focus:ring-1 focus:ring-signal-amber/30 focus:border-signal-amber/60"
            >
              {ROLE_OPTIONS.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          <OptionGroup label="Difficulty" options={DIFFICULTY_OPTIONS} value={difficulty} onChange={setDifficulty} />
          <OptionGroup label="Experience" options={EXPERIENCE_OPTIONS} value={experience} onChange={setExperience} />
          <OptionGroup
            label="Question Count"
            options={QUESTION_COUNT_OPTIONS}
            value={questionCount}
            onChange={setQuestionCount}
          />
          <OptionGroup label="Interview Style" options={STYLE_OPTIONS} value={style} onChange={setStyle} />
        </div>

        <button
          type="button"
          onClick={handleSubmit}
          disabled={isStarting}
          className="mt-6 w-full inline-flex items-center justify-center gap-2 rounded-lg bg-signal-amber px-5 py-3 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 transition-colors"
        >
          {isStarting && <Loader2 size={16} className="animate-spin" />}
          {isStarting ? 'Preparing your interview…' : 'Start Interview'}
        </button>
      </div>
    </div>
  )
}

export default InterviewSetupModal
