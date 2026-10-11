import { useState } from 'react'
import { Eye, EyeOff } from 'lucide-react'

function FormInput({ label, id, type = 'text', error, className = '', ...props }) {
  const [showPassword, setShowPassword] = useState(false)
  const isPassword = type === 'password'
  const inputType = isPassword ? (showPassword ? 'text' : 'password') : type

  return (
    <div className={className}>
      <label htmlFor={id} className="block text-sm font-medium text-paper mb-1.5">
        {label}
      </label>
      <div className="relative">
        <input
          id={id}
          type={inputType}
          className={`w-full rounded-lg border bg-ink px-4 py-2.5 text-sm text-paper placeholder:text-muted/60 focus:outline-none focus:ring-1 transition-colors ${
            error
              ? 'border-red-500/60 focus:border-red-500 focus:ring-red-500/40'
              : 'border-ink-line focus:border-signal-amber/60 focus:ring-signal-amber/30'
          } ${isPassword ? 'pr-11' : ''}`}
          aria-invalid={Boolean(error)}
          aria-describedby={error ? `${id}-error` : undefined}
          {...props}
        />
        {isPassword && (
          <button
            type="button"
            onClick={() => setShowPassword((prev) => !prev)}
            className="absolute right-3 top-1/2 -translate-y-1/2 text-muted hover:text-paper transition-colors"
            aria-label={showPassword ? 'Hide password' : 'Show password'}
            tabIndex={-1}
          >
            {showPassword ? <EyeOff size={16} /> : <Eye size={16} />}
          </button>
        )}
      </div>
      {error && (
        <p id={`${id}-error`} className="mt-1.5 text-xs text-red-400">
          {error}
        </p>
      )}
    </div>
  )
}

export default FormInput