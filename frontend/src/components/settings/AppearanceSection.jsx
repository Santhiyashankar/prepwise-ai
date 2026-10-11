import { Sun, Moon, Monitor } from 'lucide-react'
import { useTheme } from '../../context/ThemeContext.jsx'

const THEME_OPTIONS = [
  { value: 'light', label: 'Light', icon: Sun, description: 'Bright surfaces, ideal for daytime.' },
  { value: 'dark', label: 'Dark', icon: Moon, description: 'Low-glare surfaces, easy on the eyes.' },
  { value: 'system', label: 'System', icon: Monitor, description: 'Match your device settings.' },
]

function AppearanceSection() {
  const { theme, setTheme } = useTheme()

  return (
    <div className="space-y-4">
      <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Appearance</h2>
      <p className="text-sm text-[var(--dash-muted)]">Choose how PrepWise AI looks on this device.</p>

      <div className="grid sm:grid-cols-3 gap-4 max-w-2xl">
        {THEME_OPTIONS.map((option) => (
          <button
            key={option.value}
            type="button"
            onClick={() => setTheme(option.value)}
            className={`flex flex-col items-start gap-3 rounded-xl border p-4 text-left transition-colors ${
              theme === option.value
                ? 'border-signal-amber/50 bg-signal-amber/10'
                : 'border-[var(--dash-border)] hover:border-signal-amber/30'
            }`}
          >
            <span
              className={`flex h-10 w-10 items-center justify-center rounded-lg border ${
                theme === option.value
                  ? 'bg-signal-amber/15 text-signal-amber border-signal-amber/30'
                  : 'text-[var(--dash-muted)] border-[var(--dash-border)]'
              }`}
            >
              <option.icon size={18} strokeWidth={1.8} />
            </span>
            <div>
              <p className="text-sm font-medium text-[var(--dash-text)]">{option.label}</p>
              <p className="text-xs text-[var(--dash-muted)] mt-0.5">{option.description}</p>
            </div>
          </button>
        ))}
      </div>
    </div>
  )
}

export default AppearanceSection
