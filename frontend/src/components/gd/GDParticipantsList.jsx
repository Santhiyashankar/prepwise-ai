import { PARTICIPANT_COLORS, PERSONALITY_LABELS } from '../../utils/gdConstants.js'

function getInitials(name) {
  return name
    .split(' ')
    .map((part) => part[0])
    .slice(0, 2)
    .join('')
    .toUpperCase()
}

function GDParticipantsList({ participants = [] }) {
  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4">
      <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-3">
        Participants ({participants.length + 1})
      </p>
      <div className="space-y-2.5">
        <div className="flex items-center gap-2.5">
          <span className="flex h-8 w-8 items-center justify-center rounded-full bg-signal-amber/15 text-signal-amber border border-signal-amber/30 text-xs font-semibold">
            You
          </span>
          <div>
            <p className="text-sm font-medium text-[var(--dash-text)]">You</p>
            <p className="text-[11px] text-[var(--dash-muted)]">Candidate</p>
          </div>
        </div>

        {participants.map((participant) => {
          const color = PARTICIPANT_COLORS[participant.colorIndex % PARTICIPANT_COLORS.length]
          return (
            <div key={participant.id} className="flex items-center gap-2.5">
              <span
                className={`flex h-8 w-8 items-center justify-center rounded-full text-xs font-semibold border ${color.bg} ${color.text} ${color.border}`}
              >
                {getInitials(participant.name)}
              </span>
              <div>
                <p className="text-sm font-medium text-[var(--dash-text)]">{participant.name}</p>
                <p className="text-[11px] text-[var(--dash-muted)]">
                  {PERSONALITY_LABELS[participant.personality] || participant.personality}
                </p>
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}

export default GDParticipantsList
