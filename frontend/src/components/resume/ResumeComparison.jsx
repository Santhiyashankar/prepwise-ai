function ScoreRow({ label, first, second }) {
  const higher = first === second ? null : first > second ? 'first' : 'second'
  return (
    <div className="grid grid-cols-3 items-center gap-3 py-2.5 border-b border-[var(--dash-border)]/60 last:border-0">
      <span
        className={`font-mono text-sm ${higher === 'first' ? 'text-signal-mint' : 'text-[var(--dash-text)]'}`}
      >
        {Math.round(first)}
      </span>
      <span className="text-xs text-[var(--dash-muted)] text-center">{label}</span>
      <span
        className={`font-mono text-sm text-right ${higher === 'second' ? 'text-signal-mint' : 'text-[var(--dash-text)]'}`}
      >
        {Math.round(second)}
      </span>
    </div>
  )
}

function ResumeComparison({ first, second }) {
  if (!first || !second) {
    return (
      <p className="text-sm text-[var(--dash-muted)] text-center py-10">
        Select two resumes from your history to compare them.
      </p>
    )
  }

  const firstAnalysis = first.analysis
  const secondAnalysis = second.analysis

  return (
    <div className="space-y-5">
      <div className="grid grid-cols-2 gap-5">
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4 text-center">
          <p className="text-sm font-medium text-[var(--dash-text)] truncate">{first.resume.fileName}</p>
          <p className="text-xs text-[var(--dash-muted)] mt-0.5">Version {first.resume.version}</p>
        </div>
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4 text-center">
          <p className="text-sm font-medium text-[var(--dash-text)] truncate">{second.resume.fileName}</p>
          <p className="text-xs text-[var(--dash-muted)] mt-0.5">Version {second.resume.version}</p>
        </div>
      </div>

      {firstAnalysis && secondAnalysis ? (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <ScoreRow label="ATS Score" first={firstAnalysis.atsScore} second={secondAnalysis.atsScore} />
          <ScoreRow label="AI Score" first={firstAnalysis.aiScore} second={secondAnalysis.aiScore} />
          <ScoreRow
            label="Keyword Match %"
            first={firstAnalysis.keywordMatchPercentage}
            second={secondAnalysis.keywordMatchPercentage}
          />
          <ScoreRow
            label="Overall Rating"
            first={firstAnalysis.overallRating}
            second={secondAnalysis.overallRating}
          />
        </div>
      ) : (
        <p className="text-sm text-[var(--dash-muted)] text-center py-6">
          One or both resumes haven't been analyzed yet.
        </p>
      )}

      <div className="grid grid-cols-2 gap-5">
        {[first, second].map((entry) => (
          <div key={entry.resume.id} className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4">
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-2">
              Top Strengths
            </p>
            {entry.analysis?.strengths?.length > 0 ? (
              <ul className="space-y-1.5">
                {entry.analysis.strengths.slice(0, 3).map((item, index) => (
                  <li key={index} className="text-sm text-[var(--dash-text)] flex gap-2">
                    <span className="text-signal-mint">•</span>
                    {item}
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-sm text-[var(--dash-muted)]">No data available.</p>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}

export default ResumeComparison
