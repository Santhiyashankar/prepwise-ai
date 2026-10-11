import { RadialBar, RadialBarChart, PolarAngleAxis } from 'recharts'

function getScoreColor(score) {
  if (score >= 75) return '#4ADE80'
  if (score >= 50) return '#FFB020'
  return '#F87171'
}

function ATSScoreCard({ label, score, description }) {
  const value = Math.round(score || 0)
  const color = getScoreColor(value)
  const data = [{ name: label, value, fill: color }]

  return (
    <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5 flex items-center gap-5">
      <div className="relative h-24 w-24 shrink-0">
        <RadialBarChart
          width={96}
          height={96}
          cx={48}
          cy={48}
          innerRadius={34}
          outerRadius={46}
          barSize={8}
          data={data}
          startAngle={90}
          endAngle={-270}
        >
          <PolarAngleAxis type="number" domain={[0, 100]} angleAxisId={0} tick={false} />
          <RadialBar background={{ fill: 'var(--dash-border)' }} dataKey="value" cornerRadius={8} />
        </RadialBarChart>
        <div className="absolute inset-0 flex items-center justify-center">
          <span className="font-mono text-lg font-semibold text-[var(--dash-text)]">{value}</span>
        </div>
      </div>
      <div>
        <p className="text-sm font-medium text-[var(--dash-text)]">{label}</p>
        {description && <p className="mt-1 text-xs text-[var(--dash-muted)] max-w-[180px]">{description}</p>}
      </div>
    </div>
  )
}

export default ATSScoreCard
