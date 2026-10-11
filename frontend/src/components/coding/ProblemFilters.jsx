import { COMPANY_OPTIONS, DIFFICULTY_OPTIONS, TOPIC_LABELS, TOPIC_OPTIONS } from '../../utils/codingConstants.js'

function FilterSelect({ label, value, onChange, options, labels }) {
  return (
    <div>
      <label className="block text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-1.5">
        {label}
      </label>
      <select
        value={value || ''}
        onChange={(event) => onChange(event.target.value || null)}
        className="w-full rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] px-3 py-2 text-sm text-[var(--dash-text)] focus:outline-none focus:ring-1 focus:ring-signal-amber/30 focus:border-signal-amber/60"
      >
        <option value="">All</option>
        {options.map((option) => (
          <option key={option} value={option}>
            {labels ? labels[option] : option.charAt(0) + option.slice(1).toLowerCase()}
          </option>
        ))}
      </select>
    </div>
  )
}

function ProblemFilters({ difficulty, topic, company, onChange }) {
  return (
    <div className="grid sm:grid-cols-3 gap-4">
      <FilterSelect
        label="Difficulty"
        value={difficulty}
        onChange={(value) => onChange({ difficulty: value, topic, company })}
        options={DIFFICULTY_OPTIONS}
      />
      <FilterSelect
        label="Topic"
        value={topic}
        onChange={(value) => onChange({ difficulty, topic: value, company })}
        options={TOPIC_OPTIONS}
        labels={TOPIC_LABELS}
      />
      <FilterSelect
        label="Company"
        value={company}
        onChange={(value) => onChange({ difficulty, topic, company: value })}
        options={COMPANY_OPTIONS}
      />
    </div>
  )
}

export default ProblemFilters
