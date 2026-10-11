const STATS = [
  { value: '2.4M+', label: 'Interviews Conducted' },
  { value: '180+', label: 'Companies Supported' },
  { value: '58K+', label: 'Practice Questions' },
  { value: '31K+', label: 'Success Stories' },
]

function Statistics() {
  return (
    <section className="bg-ink py-20 border-b border-ink-line">
      <div className="container-page">
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-8">
          {STATS.map((stat) => (
            <div key={stat.label} className="text-center lg:text-left">
              <p className="font-mono text-3xl sm:text-4xl font-medium text-signal-amber">
                {stat.value}
              </p>
              <p className="mt-2 text-sm text-muted">{stat.label}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

export default Statistics
