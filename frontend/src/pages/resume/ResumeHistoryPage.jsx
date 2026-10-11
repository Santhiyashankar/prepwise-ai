import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { GitCompare } from 'lucide-react'
import resumeApi from '../../api/resumeApi.js'
import ResumeHistoryTable from '../../components/resume/ResumeHistoryTable.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'

function ResumeHistoryPage() {
  const navigate = useNavigate()
  const [items, setItems] = useState([])
  const [selectedIds, setSelectedIds] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [busyId, setBusyId] = useState(null)
  const [errorMessage, setErrorMessage] = useState('')

  const loadHistory = async () => {
    setIsLoading(true)
    try {
      const { data } = await resumeApi.getHistory()
      setItems([...data.data].reverse())
    } catch {
      setErrorMessage('Could not load resume history.')
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    loadHistory()
  }, [])

  const handleToggleSelect = (resumeId) => {
    setSelectedIds((prev) => {
      if (prev.includes(resumeId)) return prev.filter((id) => id !== resumeId)
      if (prev.length >= 2) return [prev[1], resumeId]
      return [...prev, resumeId]
    })
  }

  const handleView = (resumeId) => navigate(`/dashboard/resume/analysis/${resumeId}`)

  const handleReanalyze = async (resumeId) => {
    setBusyId(resumeId)
    setErrorMessage('')
    try {
      await resumeApi.reanalyze(resumeId)
      await loadHistory()
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to reanalyze resume.')
    } finally {
      setBusyId(null)
    }
  }

  const handleDownload = async (resumeId) => {
    try {
      const { data } = await resumeApi.download(resumeId)
      const url = URL.createObjectURL(data)
      const link = document.createElement('a')
      link.href = url
      link.download = 'resume'
      document.body.appendChild(link)
      link.click()
      link.remove()
      URL.revokeObjectURL(url)
    } catch {
      setErrorMessage('Failed to download resume.')
    }
  }

  const handleDelete = async (resumeId) => {
    setBusyId(resumeId)
    setErrorMessage('')
    try {
      await resumeApi.delete(resumeId)
      setSelectedIds((prev) => prev.filter((id) => id !== resumeId))
      await loadHistory()
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to delete resume.')
    } finally {
      setBusyId(null)
    }
  }

  const handleCompare = () => {
    if (selectedIds.length !== 2) return
    navigate(`/dashboard/resume/compare?a=${selectedIds[0]}&b=${selectedIds[1]}`)
  }

  if (isLoading) {
    return <LoadingSkeleton className="h-96 w-full" />
  }

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div>
          <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">Resume History</h1>
          <p className="text-sm text-[var(--dash-muted)] mt-1">
            Every version you've uploaded and analyzed, with scores over time.
          </p>
        </div>
        <button
          type="button"
          onClick={handleCompare}
          disabled={selectedIds.length !== 2}
          className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-4 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
        >
          <GitCompare size={15} />
          Compare Selected
        </button>
      </div>

      {errorMessage && (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      )}

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <ResumeHistoryTable
          items={items}
          selectedIds={selectedIds}
          onToggleSelect={handleToggleSelect}
          onView={handleView}
          onReanalyze={handleReanalyze}
          onDownload={handleDownload}
          onDelete={handleDelete}
          busyId={busyId}
        />
      </div>
    </div>
  )
}

export default ResumeHistoryPage
