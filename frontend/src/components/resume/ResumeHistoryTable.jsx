import { Eye, RefreshCw, Download, Trash2 } from 'lucide-react'

function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString(undefined, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  })
}

function ResumeHistoryTable({
  items = [],
  selectedIds = [],
  onToggleSelect,
  onView,
  onReanalyze,
  onDownload,
  onDelete,
  busyId,
}) {
  if (items.length === 0) {
    return (
      <p className="text-sm text-[var(--dash-muted)] py-6 text-center">
        No resumes uploaded yet. Upload one to see it here.
      </p>
    )
  }

  return (
    <div className="overflow-x-auto">
      <table className="w-full text-sm">
        <thead>
          <tr className="text-left text-xs text-[var(--dash-muted)] border-b border-[var(--dash-border)]">
            <th className="pb-2 pr-3 font-medium">Compare</th>
            <th className="pb-2 pr-4 font-medium">File</th>
            <th className="pb-2 pr-4 font-medium">ATS Score</th>
            <th className="pb-2 pr-4 font-medium">AI Score</th>
            <th className="pb-2 pr-4 font-medium">Keyword Match</th>
            <th className="pb-2 pr-4 font-medium">Date</th>
            <th className="pb-2 font-medium text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => (
            <tr key={item.id} className="border-b border-[var(--dash-border)]/60 last:border-0">
              <td className="py-3 pr-3">
                <input
                  type="checkbox"
                  checked={selectedIds.includes(item.resumeId)}
                  onChange={() => onToggleSelect(item.resumeId)}
                  className="h-4 w-4 rounded accent-signal-amber"
                />
              </td>
              <td className="py-3 pr-4 text-[var(--dash-text)] max-w-[200px] truncate">{item.fileName}</td>
              <td className="py-3 pr-4 font-mono text-[var(--dash-text)]">{Math.round(item.atsScore)}</td>
              <td className="py-3 pr-4 font-mono text-[var(--dash-text)]">{Math.round(item.aiScore)}</td>
              <td className="py-3 pr-4 font-mono text-[var(--dash-text)]">
                {Math.round(item.keywordMatchPercentage)}%
              </td>
              <td className="py-3 pr-4 text-[var(--dash-muted)]">{formatDate(item.recordedAt)}</td>
              <td className="py-3">
                <div className="flex items-center justify-end gap-1.5">
                  <button
                    type="button"
                    onClick={() => onView(item.resumeId)}
                    className="flex h-7 w-7 items-center justify-center rounded-md text-[var(--dash-muted)] hover:text-signal-amber hover:bg-[var(--dash-surface-hover)] transition-colors"
                    aria-label="View analysis"
                  >
                    <Eye size={15} />
                  </button>
                  <button
                    type="button"
                    onClick={() => onReanalyze(item.resumeId)}
                    disabled={busyId === item.resumeId}
                    className="flex h-7 w-7 items-center justify-center rounded-md text-[var(--dash-muted)] hover:text-signal-amber hover:bg-[var(--dash-surface-hover)] transition-colors disabled:opacity-50"
                    aria-label="Reanalyze"
                  >
                    <RefreshCw size={15} className={busyId === item.resumeId ? 'animate-spin' : ''} />
                  </button>
                  <button
                    type="button"
                    onClick={() => onDownload(item.resumeId)}
                    className="flex h-7 w-7 items-center justify-center rounded-md text-[var(--dash-muted)] hover:text-signal-amber hover:bg-[var(--dash-surface-hover)] transition-colors"
                    aria-label="Download"
                  >
                    <Download size={15} />
                  </button>
                  <button
                    type="button"
                    onClick={() => onDelete(item.resumeId)}
                    className="flex h-7 w-7 items-center justify-center rounded-md text-[var(--dash-muted)] hover:text-red-400 hover:bg-red-500/10 transition-colors"
                    aria-label="Delete"
                  >
                    <Trash2 size={15} />
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default ResumeHistoryTable
