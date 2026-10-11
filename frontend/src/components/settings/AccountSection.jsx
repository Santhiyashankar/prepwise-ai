import { useEffect, useState } from 'react'
import { Loader2 } from 'lucide-react'
import profileApi from '../../api/profileApi.js'
import DashInput from '../common/DashInput.jsx'
import { useAuth } from '../../context/AuthContext.jsx'
import { validateEmail, validateFullName } from '../../utils/validators.js'

function AccountSection() {
  const { refreshUser } = useAuth()
  const [fullProfile, setFullProfile] = useState(null)
  const [fullName, setFullName] = useState('')
  const [email, setEmail] = useState('')
  const [errors, setErrors] = useState({})
  const [isLoading, setIsLoading] = useState(true)
  const [isSaving, setIsSaving] = useState(false)
  const [statusMessage, setStatusMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('')

  useEffect(() => {
    const fetchProfile = async () => {
      setIsLoading(true)
      try {
        const { data } = await profileApi.getProfile()
        setFullProfile(data.data)
        setFullName(data.data.fullName || '')
        setEmail(data.data.email || '')
      } catch {
        setErrorMessage('Could not load account details.')
      } finally {
        setIsLoading(false)
      }
    }
    fetchProfile()
  }, [])

  const handleSave = async (event) => {
    event.preventDefault()
    const nextErrors = {
      fullName: validateFullName(fullName),
      email: validateEmail(email),
    }
    setErrors(nextErrors)
    if (Object.values(nextErrors).some(Boolean)) return

    setIsSaving(true)
    setStatusMessage('')
    setErrorMessage('')

    try {
      await profileApi.updateProfile({
        ...fullProfile,
        fullName,
        email,
      })
      await refreshUser()
      setStatusMessage('Account details updated successfully.')
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to update account details.')
    } finally {
      setIsSaving(false)
    }
  }

  if (isLoading) {
    return <div className="h-48 rounded-xl bg-[var(--dash-surface-hover)] animate-pulse" />
  }

  return (
    <div className="space-y-5">
      <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Account</h2>

      {statusMessage && (
        <div className="rounded-lg border border-signal-mint/30 bg-signal-mint/10 px-4 py-3 text-sm text-signal-mint">
          {statusMessage}
        </div>
      )}
      {errorMessage && (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      )}

      <form onSubmit={handleSave} noValidate className="space-y-5 max-w-md">
        <DashInput
          id="settings-fullName"
          label="Full Name"
          value={fullName}
          onChange={(event) => setFullName(event.target.value)}
          error={errors.fullName}
        />
        <DashInput
          id="settings-email"
          type="email"
          label="Email Address"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          error={errors.email}
        />
        <button
          type="submit"
          disabled={isSaving}
          className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-5 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 transition-colors"
        >
          {isSaving && <Loader2 size={15} className="animate-spin" />}
          {isSaving ? 'Saving…' : 'Save Changes'}
        </button>
      </form>
    </div>
  )
}

export default AccountSection
