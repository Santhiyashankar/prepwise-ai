import { UserCog, ShieldCheck, Bell, Palette, Lock } from 'lucide-react'

export const SETTINGS_SECTIONS = [
  { id: 'account', label: 'Account', icon: UserCog },
  { id: 'security', label: 'Security', icon: ShieldCheck },
  { id: 'notifications', label: 'Notifications', icon: Bell },
  { id: 'appearance', label: 'Appearance', icon: Palette },
  { id: 'privacy', label: 'Privacy', icon: Lock },
]

function SettingsNav({ activeSection, onSelect }) {
  return (
    <nav className="flex lg:flex-col gap-1 overflow-x-auto lg:overflow-visible pb-2 lg:pb-0 lg:w-56 shrink-0">
      {SETTINGS_SECTIONS.map((section) => (
        <button
          key={section.id}
          type="button"
          onClick={() => onSelect(section.id)}
          className={`flex items-center gap-2.5 rounded-lg px-3 py-2.5 text-sm font-medium whitespace-nowrap transition-colors ${
            activeSection === section.id
              ? 'bg-signal-amber/15 text-signal-amber'
              : 'text-[var(--dash-muted)] hover:bg-[var(--dash-surface-hover)] hover:text-[var(--dash-text)]'
          }`}
        >
          <section.icon size={16} strokeWidth={1.8} />
          {section.label}
        </button>
      ))}
    </nav>
  )
}

export default SettingsNav
