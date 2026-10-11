import { useState } from 'react'
import { X } from 'lucide-react'

function TagInput({ label, tags, onChange, placeholder }) {
  const [draft, setDraft] = useState('')

  const addTag = () => {
    const value = draft.trim()
    if (!value || tags.includes(value)) {
      setDraft('')
      return
    }
    onChange([...tags, value])
    setDraft('')
  }

  const removeTag = (tagToRemove) => {
    onChange(tags.filter((tag) => tag !== tagToRemove))
  }

  const handleKeyDown = (event) => {
    if (event.key === 'Enter' || event.key === ',') {
      event.preventDefault()
      addTag()
    }
  }

  return (
    <div>
      <label className="block text-sm font-medium text-[var(--dash-text)] mb-1.5">{label}</label>
      <div className="flex flex-wrap gap-2 rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] px-3 py-2.5 focus-within:border-signal-amber/60 focus-within:ring-1 focus-within:ring-signal-amber/30 transition-colors">
        {tags.map((tag) => (
          <span
            key={tag}
            className="inline-flex items-center gap-1.5 rounded-md bg-signal-amber/10 border border-signal-amber/25 px-2.5 py-1 text-xs font-medium text-signal-amber"
          >
            {tag}
            <button
              type="button"
              onClick={() => removeTag(tag)}
              className="hover:text-red-400 transition-colors"
              aria-label={`Remove ${tag}`}
            >
              <X size={12} />
            </button>
          </span>
        ))}
        <input
          type="text"
          value={draft}
          onChange={(event) => setDraft(event.target.value)}
          onKeyDown={handleKeyDown}
          onBlur={addTag}
          placeholder={placeholder}
          className="flex-1 min-w-[120px] bg-transparent text-sm text-[var(--dash-text)] placeholder:text-[var(--dash-muted)]/70 focus:outline-none py-1"
        />
      </div>
      <p className="mt-1.5 text-xs text-[var(--dash-muted)]">Press Enter or comma to add</p>
    </div>
  )
}

export default TagInput
