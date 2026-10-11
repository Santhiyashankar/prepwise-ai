const STATUS_STYLES = {
  MATCHED: 'bg-signal-mint/10 text-signal-mint border-signal-mint/25',
  WEAK: 'bg-signal-amber/10 text-signal-amber border-signal-amber/25',
  MISSING: 'bg-[var(--dash-border)] text-[var(--dash-muted)] border-[var(--dash-border)]',
  RECOMMENDED: 'bg-blue-500/10 text-blue-400 border-blue-500/25',
}

const STATUS_LABELS = {
  MATCHED: 'Matched',
  WEAK: 'Weak',
  MISSING: 'Missing',
  RECOMMENDED: 'Recommended',
}

function KeywordTable({ keywords = [] }) {
  if (keywords.length === 0) {
    return <p className="text-sm text-[var(--dash-muted)]">No keyword data available yet.</p>
  }

  return (
    <div className="overflow-x-auto">
      <table className="w-full text-sm">
        <thead>
          <tr className="text-left text-xs text-[var(--dash-muted)] border-b border-[var(--dash-border)]">
            <th className="pb-2 pr-4 font-medium">Keyword</th>
            <th className="pb-2 font-medium">Status</th>
          </tr>
        </thead>
        <tbody>
          {keywords.map((item) => (
            <tr key={item.keyword} className="border-b border-[var(--dash-border)]/60 last:border-0">
              <td className="py-2.5 pr-4 text-[var(--dash-text)]">{item.keyword}</td>
              <td className="py-2.5">
                <span
                  className={`inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-medium ${STATUS_STYLES[item.status]}`}
                >
                  {STATUS_LABELS[item.status]}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default KeywordTable
