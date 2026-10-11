import { Megaphone } from 'lucide-react'
import { PARTICIPANT_COLORS, PERSONALITY_LABELS } from '../../utils/gdConstants.js'

function getInitials(name) {
  return name
    .split(' ')
    .map((part) => part[0])
    .slice(0, 2)
    .join('')
    .toUpperCase()
}

function GDMessageBubble({ message, personality }) {
  if (message.senderType === 'SYSTEM') {
    return (
      <div className="flex items-center justify-center gap-2 py-2">
        <Megaphone size={13} className="text-[var(--dash-muted)]" />
        <p className="text-xs text-[var(--dash-muted)] italic text-center max-w-md">{message.content}</p>
      </div>
    )
  }

  const isUser = message.senderType === 'USER'
  const color = PARTICIPANT_COLORS[message.colorIndex >= 0 ? message.colorIndex % PARTICIPANT_COLORS.length : 0]

  return (
    <div className={`flex gap-3 ${isUser ? 'flex-row-reverse' : ''}`}>
      <span
        className={`flex h-8 w-8 shrink-0 items-center justify-center rounded-full text-xs font-semibold ${
          isUser
            ? 'bg-signal-amber/15 text-signal-amber border border-signal-amber/30'
            : `${color.bg} ${color.text} border ${color.border}`
        }`}
      >
        {isUser ? 'You' : getInitials(message.senderName)}
      </span>

      <div className={`max-w-[75%] ${isUser ? 'items-end' : 'items-start'} flex flex-col`}>
        {!isUser && (
          <p className="text-[11px] text-[var(--dash-muted)] mb-1 px-1">
            {message.senderName}
            {personality && <span className="ml-1.5 opacity-70">· {PERSONALITY_LABELS[personality]}</span>}
          </p>
        )}
        <div
          className={`rounded-2xl px-4 py-2.5 text-sm leading-relaxed ${
            isUser
              ? 'bg-signal-amber text-ink rounded-tr-sm'
              : 'bg-[var(--dash-surface)] border border-[var(--dash-border)] text-[var(--dash-text)] rounded-tl-sm'
          }`}
        >
          {message.content}
        </div>
      </div>
    </div>
  )
}

export default GDMessageBubble
