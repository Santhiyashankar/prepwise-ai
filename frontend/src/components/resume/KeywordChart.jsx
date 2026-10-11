import { Cell, Pie, PieChart, ResponsiveContainer, Tooltip } from 'recharts'

function KeywordChart({ matched = [], weak = [], missing = [] }) {
  const data = [
    { name: 'Matched', value: matched.length, color: '#4ADE80' },
    { name: 'Weak', value: weak.length, color: '#FFB020' },
    { name: 'Missing', value: missing.length, color: '#3A4459' },
  ].filter((item) => item.value > 0)

  if (data.length === 0) {
    return (
      <div className="flex h-48 items-center justify-center text-sm text-[var(--dash-muted)]">
        No keyword data available yet.
      </div>
    )
  }

  return (
    <div className="h-48">
      <ResponsiveContainer width="100%" height="100%">
        <PieChart>
          <Pie
            data={data}
            dataKey="value"
            nameKey="name"
            innerRadius={50}
            outerRadius={72}
            paddingAngle={2}
          >
            {data.map((entry) => (
              <Cell key={entry.name} fill={entry.color} />
            ))}
          </Pie>
          <Tooltip
            contentStyle={{
              background: 'var(--dash-surface)',
              border: '1px solid var(--dash-border)',
              borderRadius: '8px',
              fontSize: '12px',
            }}
          />
        </PieChart>
      </ResponsiveContainer>
      <div className="flex items-center justify-center gap-4 -mt-2">
        {data.map((entry) => (
          <div key={entry.name} className="flex items-center gap-1.5">
            <span className="h-2 w-2 rounded-full" style={{ backgroundColor: entry.color }} />
            <span className="text-xs text-[var(--dash-muted)]">
              {entry.name} ({entry.value})
            </span>
          </div>
        ))}
      </div>
    </div>
  )
}

export default KeywordChart
