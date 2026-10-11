import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Loader2, Download, TriangleAlert } from 'lucide-react'
import profileApi from '../../api/profileApi.js'
import DashInput from '../common/DashInput.jsx'
import { useAuth } from '../../context/AuthContext.jsx'

function PrivacySection() {
  const { logout } = useAuth()
  const navigate = useNavigate()

  const [isExporting, setIsExporting] = useState(false)
  const [isDeleting, setIsDeleting] = useState(false)
  const [isConfirmOpen, setIsConfirmOpen] = useState(false)
  const [confirmText, setConfirmText] = useState('')
  const [errorMessage, setErrorMessage] = useState('')

  const handleExport = async () => {
    setIsExporting(true)
    setErrorMessage('')
    try {
      const { data } = await profileApi.exportAccountData()
      const blob = new Blob([JSON.stringify(data.data, null, 2)], { type: 'application/json' })
      const url = URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = 'prepwise-account-data.json'
      document.body.appendChild(link)
      link.click()
      link.remove()
      URL.revokeObjectURL(url)
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to export account data.')
    } finally {
      setIsExporting(false)
    }
  }

  const handleDeleteAccount = async () => {
    setIsDeleting(true)
    setErrorMessage('')
    try {
      await profileApi.deleteAccount()
      await logout()
      navigate('/', { replace: true })
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to delete account.')
      setIsDeleting(false)
    }
  }

  return (
    <div className="space-y-8">
      {errorMessage && (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      )}

      <div>
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Export Account Data</h2>
        <p className="mt-1.5 text-sm text-[var(--dash-muted)] max-w-md">
          Download a copy of your profile, notification preferences, and recent activity as a JSON file.
        </p>
        <button
          type="button"
          onClick={handleExport}
          disabled={isExporting}
          className="mt-4 inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-5 py-2.5 text-sm font-semibold text-[var(--dash-text)] hover:border-signal-amber/40 disabled:opacity-60 transition-colors"
        >
          {isExporting ? <Loader2 size={15} className="animate-spin" /> : <Download size={15} />}
          {isExporting ? 'Preparing export…' : 'Export my data'}
        </button>
      </div>

      <div className="pt-6 border-t border-[var(--dash-border)]">
        <h2 className="font-display text-base font-semibold text-red-400 flex items-center gap-2">
          <TriangleAlert size={16} />
          Delete Account
        </h2>
        <p className="mt-1.5 text-sm text-[var(--dash-muted)] max-w-md">
          This permanently deletes your account and all associated data. This action cannot be undone.
        </p>

        {!isConfirmOpen ? (
          <button
            type="button"
            onClick={() => setIsConfirmOpen(true)}
            className="mt-4 inline-flex items-center gap-2 rounded-lg border border-red-500/30 px-5 py-2.5 text-sm font-semibold text-red-400 hover:bg-red-500/10 transition-colors"
          >
            Delete my account
          </button>
        ) : (
          <div className="mt-4 max-w-md rounded-xl border border-red-500/30 bg-red-500/5 p-4 space-y-4">
            <DashInput
              id="deleteConfirm"
              label='Type "DELETE" to confirm'
              value={confirmText}
              onChange={(event) => setConfirmText(event.target.value)}
              placeholder="DELETE"
            />
            <div className="flex gap-3">
              <button
                type="button"
                onClick={() => {
                  setIsConfirmOpen(false)
                  setConfirmText('')
                }}
                className="rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
              >
                Cancel
              </button>
              <button
                type="button"
                onClick={handleDeleteAccount}
                disabled={confirmText !== 'DELETE' || isDeleting}
                className="inline-flex items-center gap-2 rounded-lg bg-red-500 px-4 py-2 text-sm font-semibold text-white hover:bg-red-600 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
              >
                {isDeleting && <Loader2 size={14} className="animate-spin" />}
                {isDeleting ? 'Deleting…' : 'Permanently delete'}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default PrivacySection
