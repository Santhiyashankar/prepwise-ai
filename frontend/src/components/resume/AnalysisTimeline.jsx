import { CartesianGrid, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'

function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString(undefined, { month: 'short', day: 'numeric' })
}

function CustomTooltip({ active, payload, label }) {
  if (!active || !payload?.length) return null
  return (
    <div className="rounded-lg border border-[var(--dash-border)] bg-[var(--dash-surface)] px-3 py-2 shadow-lg">
      <p className="font-mono text-xs text-[var(--dash-muted)]">{formatDate(label)}</p>
      {payload.map((entry) => (
        <p key={entry.dataKey} className="font-mono text-xs" style={{ color: entry.color }}>
          {entry.name}: {entry.value}
        </p>
      ))}
    </div>
  )
}

function AnalysisTimeline({ history = [] }) {
  if (history.length === 0) {
    return (
      <div className="flex h-56 items-center justify-center text-sm text-[var(--dash-muted)]">
        Analyze a resume to start tracking your score trend.
      </div>
    )
  }

  const data = history.map((item) => ({
    recordedAt: item.recordedAt,
    atsScore: Math.round(item.atsScore),
    aiScore: Math.round(item.aiScore),
  }))

  return (
    <div className="h-56">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="var(--dash-border)" />
          <XAxis
            dataKey="recordedAt"
            tickFormatter={formatDate}
            stroke="var(--dash-muted)"
            fontSize={11}
            tickLine={false}
            axisLine={false}
          />
          <YAxis domain={[0, 100]} stroke="var(--dash-muted)" fontSize={11} tickLine={false} axisLine={false} />
          <Tooltip content={<CustomTooltip />} />
          <Line
            type="monotone"
            dataKey="atsScore"
            name="ATS Score"
            stroke="#FFB020"
            strokeWidth={2}
            dot={{ r: 3 }}
          />
          <Line
            type="monotone"
            dataKey="aiScore"
            name="AI Score"
            stroke="#4ADE80"
            strokeWidth={2}
            dot={{ r: 3 }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  )
}

export default AnalysisTimeline
