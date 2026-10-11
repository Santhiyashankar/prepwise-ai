import { CheckCircle2, XCircle } from 'lucide-react'

function TestCaseResultsList({ results = [], compileError }) {
  if (compileError) {
    return (
      <div className="rounded-lg border border-red-500/30 bg-red-500/10 p-4">
        <p className="text-xs font-mono tracking-wide text-red-400 uppercase mb-2">Compile Error</p>
        <pre className="text-xs text-red-300 whitespace-pre-wrap font-mono">{compileError}</pre>
      </div>
    )
  }

  if (results.length === 0) {
    return <p className="text-sm text-[var(--dash-muted)]">No test case results available.</p>
  }

  return (
    <div className="space-y-2">
      {results.map((result, index) => (
        <div
          key={index}
          className={`rounded-lg border p-3 ${
            result.passed ? 'border-signal-mint/25 bg-signal-mint/5' : 'border-red-500/25 bg-red-500/5'
          }`}
        >
          <div className="flex items-center gap-2 mb-2">
            {result.passed ? (
              <CheckCircle2 size={15} className="text-signal-mint" />
            ) : (
              <XCircle size={15} className="text-red-400" />
            )}
            <span className="text-xs font-medium text-[var(--dash-text)]">Test Case {index + 1}</span>
            {result.executionTimeMs != null && (
              <span className="ml-auto font-mono text-[11px] text-[var(--dash-muted)]">
                {result.executionTimeMs}ms
              </span>
            )}
          </div>
          <div className="grid sm:grid-cols-3 gap-2 text-xs font-mono">
            <div>
              <p className="text-[var(--dash-muted)] mb-0.5">Input</p>
              <pre className="text-[var(--dash-text)] whitespace-pre-wrap break-words">{result.input || '—'}</pre>
            </div>
            <div>
              <p className="text-[var(--dash-muted)] mb-0.5">Expected</p>
              <pre className="text-[var(--dash-text)] whitespace-pre-wrap break-words">{result.expectedOutput}</pre>
            </div>
            <div>
              <p className="text-[var(--dash-muted)] mb-0.5">Actual</p>
              <pre className="text-[var(--dash-text)] whitespace-pre-wrap break-words">{result.actualOutput}</pre>
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}

export default TestCaseResultsList
