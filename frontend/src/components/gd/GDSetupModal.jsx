import { useEffect, useState } from 'react'
import { X, Loader2 } from 'lucide-react'
import {
  CATEGORY_LABELS,
  DIFFICULTY_OPTIONS,
  DURATION_OPTIONS,
  PARTICIPANT_COUNT_OPTIONS,
} from '../../utils/gdConstants.js'

function OptionGroup({ label, options, value, onChange, formatLabel }) {
  return (
    <div>
      <p className="text-sm font-medium text-[var(--dash-text)] mb-2">{label}</p>
      <div className="flex flex-wrap gap-2">
        {options.map((option) => (
          <button
            key={option}
            type="button"
            onClick={() => onChange(option)}
            className={`rounded-lg border px-3.5 py-2 text-sm font-medium transition-colors ${
              value === option
                ? 'border-signal-amber/50 bg-signal-amber/15 text-signal-amber'
                : 'border-[var(--dash-border)] text-[var(--dash-muted)] hover:border-signal-amber/30 hover:text-[var(--dash-text)]'
            }`}
          >
            {formatLabel ? formatLabel(option) : option}
          </button>
        ))}
      </div>
    </div>
  )
}

function GDSetupModal({ initialCategory, onClose, onStart, isStarting }) {
  const [category, setCategory] = useState(initialCategory || 'PLACEMENT')
  const [difficulty, setDifficulty] = useState('MEDIUM')
  const [participantCount, setParticipantCount] = useState(4)
  const [durationMinutes, setDurationMinutes] = useState(10)

  useEffect(() => {
    if (initialCategory) setCategory(initialCategory)
  }, [initialCategory])

  const handleSubmit = () => {
    onStart({ category, difficulty, participantCount, durationMinutes })
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 px-4">
      <div className="w-full max-w-lg rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between mb-5">
          <h2 className="font-display text-lg font-semibold text-[var(--dash-text)]">Set Up Group Discussion</h2>
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
          <OptionGroup
            label="Topic Category"
            options={Object.keys(CATEGORY_LABELS)}
            value={category}
            onChange={setCategory}
            formatLabel={(value) => CATEGORY_LABELS[value]}
          />
          <OptionGroup label="Difficulty" options={DIFFICULTY_OPTIONS} value={difficulty} onChange={setDifficulty} />
          <OptionGroup
            label="Number of AI Participants"
            options={PARTICIPANT_COUNT_OPTIONS}
            value={participantCount}
            onChange={setParticipantCount}
          />
          <OptionGroup
            label="Duration"
            options={DURATION_OPTIONS}
            value={durationMinutes}
            onChange={setDurationMinutes}
            formatLabel={(value) => `${value} min`}
          />
          <div>
            <p className="text-sm font-medium text-[var(--dash-text)] mb-2">Language</p>
            <span className="inline-block rounded-lg border border-[var(--dash-border)] px-3.5 py-2 text-sm text-[var(--dash-muted)]">
              English
            </span>
          </div>
        </div>

        <button
          type="button"
          onClick={handleSubmit}
          disabled={isStarting}
          className="mt-6 w-full inline-flex items-center justify-center gap-2 rounded-lg bg-signal-amber px-5 py-3 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 transition-colors"
        >
          {isStarting && <Loader2 size={16} className="animate-spin" />}
          {isStarting ? 'Assembling the group…' : 'Start Discussion'}
        </button>
      </div>
    </div>
  )
}

export default GDSetupModal
