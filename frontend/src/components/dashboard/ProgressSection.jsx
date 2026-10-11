import { Bar, BarChart, ResponsiveContainer, Tooltip, XAxis } from 'recharts'

function ProgressBar({ label, percentage }) {
  return (
    <div>
      <div className="flex items-center justify-between mb-1.5">
        <span className="text-xs font-medium text-[var(--dash-text)]">{label}</span>
        <span className="font-mono text-xs text-[var(--dash-muted)]">{percentage}%</span>
      </div>
      <div className="h-1.5 w-full rounded-full bg-[var(--dash-border)] overflow-hidden">
        <div
          className="h-full rounded-full bg-signal-amber transition-all duration-700"
          style={{ width: `${Math.min(100, Math.max(0, percentage))}%` }}
        />
      </div>
    </div>
  )
}

function CustomTooltip({ active, payload, label }) {
  if (!active || !payload?.length) return null
  return (
    <div className="rounded-lg border border-[var(--dash-border)] bg-[var(--dash-surface)] px-3 py-2 shadow-lg">
      <p className="font-mono text-xs text-[var(--dash-muted)]">{label}</p>
      <p className="font-mono text-sm text-signal-amber">{payload[0].value} interviews</p>
    </div>
  )
}

function ProgressSection({ progress, isLoading }) {
  if (isLoading) {
    return (
      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <div className="h-64 rounded-lg bg-[var(--dash-surface-hover)] animate-pulse" />
      </div>
    )
  }

  const weeklyActivity = progress?.weeklyActivity || []
  const skillsProgress = progress?.skillsProgress || []
  const companyReadiness = progress?.companyReadiness || []
  const placementReadiness = progress?.placementReadiness ?? 0

  return (
    <div className="grid lg:grid-cols-2 gap-5">
      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">
          Weekly Interview Activity
        </h2>
        <div className="mt-4 h-56">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={weeklyActivity}>
              <XAxis
                dataKey="day"
                stroke="var(--dash-muted)"
                fontSize={11}
                tickLine={false}
                axisLine={false}
              />
              <Tooltip content={<CustomTooltip />} cursor={{ fill: 'var(--dash-surface-hover)' }} />
              <Bar dataKey="interviewCount" fill="#FFB020" radius={[4, 4, 0, 0]} maxBarSize={28} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 space-y-5">
        <div>
          <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">
            Placement Readiness
          </h2>
          <div className="mt-3">
            <ProgressBar label="Overall readiness" percentage={placementReadiness} />
          </div>
        </div>

        {skillsProgress.length > 0 && (
          <div>
            <h3 className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-3">
              Skills Progress
            </h3>
            <div className="space-y-3">
              {skillsProgress.map((skill) => (
                <ProgressBar
                  key={skill.skillName}
                  label={skill.skillName}
                  percentage={skill.progressPercentage}
                />
              ))}
            </div>
          </div>
        )}

        {companyReadiness.length > 0 && (
          <div>
            <h3 className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-3">
              Company Readiness
            </h3>
            <div className="space-y-3">
              {companyReadiness.map((company) => (
                <ProgressBar
                  key={company.companyName}
                  label={company.companyName}
                  percentage={company.readinessPercentage}
                />
              ))}
            </div>
          </div>
        )}

        {skillsProgress.length === 0 && companyReadiness.length === 0 && (
          <p className="text-sm text-[var(--dash-muted)]">
            Complete a few practice sessions to unlock skill and company breakdowns.
          </p>
        )}
      </div>
    </div>
  )
}

export default ProgressSection
