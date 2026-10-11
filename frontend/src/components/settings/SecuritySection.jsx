import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Loader2, LogOut } from 'lucide-react'
import profileApi from '../../api/profileApi.js'
import DashInput from '../common/DashInput.jsx'
import { useAuth } from '../../context/AuthContext.jsx'
import { validateConfirmPassword, validatePassword } from '../../utils/validators.js'

function SecuritySection() {
  const { logout } = useAuth()
  const navigate = useNavigate()

  const [formData, setFormData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  })
  const [errors, setErrors] = useState({})
  const [isSaving, setIsSaving] = useState(false)
  const [isLoggingOutAll, setIsLoggingOutAll] = useState(false)
  const [statusMessage, setStatusMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('')

  const handleChange = (event) => {
    const { name, value } = event.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    setErrors((prev) => ({ ...prev, [name]: '' }))
    setStatusMessage('')
    setErrorMessage('')
  }

  const handleChangePassword = async (event) => {
    event.preventDefault()
    const nextErrors = {
      currentPassword: formData.currentPassword ? '' : 'Current password is required',
      newPassword: validatePassword(formData.newPassword),
      confirmPassword: validateConfirmPassword(formData.newPassword, formData.confirmPassword),
    }
    setErrors(nextErrors)
    if (Object.values(nextErrors).some(Boolean)) return

    setIsSaving(true)
    setStatusMessage('')
    setErrorMessage('')

    try {
      await profileApi.changePassword({
        currentPassword: formData.currentPassword,
        newPassword: formData.newPassword,
      })
      setStatusMessage('Password changed successfully.')
      setFormData({ currentPassword: '', newPassword: '', confirmPassword: '' })
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to change password.')
    } finally {
      setIsSaving(false)
    }
  }

  const handleLogoutAllDevices = async () => {
    setIsLoggingOutAll(true)
    setErrorMessage('')
    try {
      await profileApi.logoutAllDevices()
      await logout()
      navigate('/login', { replace: true })
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Failed to logout from all devices.')
      setIsLoggingOutAll(false)
    }
  }

  return (
    <div className="space-y-8">
      <div>
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Change Password</h2>

        {statusMessage && (
          <div className="mt-4 rounded-lg border border-signal-mint/30 bg-signal-mint/10 px-4 py-3 text-sm text-signal-mint">
            {statusMessage}
          </div>
        )}
        {errorMessage && (
          <div className="mt-4 rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
            {errorMessage}
          </div>
        )}

        <form onSubmit={handleChangePassword} noValidate className="mt-4 space-y-5 max-w-md">
          <DashInput
            id="currentPassword"
            name="currentPassword"
            type="password"
            label="Current Password"
            value={formData.currentPassword}
            onChange={handleChange}
            error={errors.currentPassword}
            autoComplete="current-password"
          />
          <DashInput
            id="newPassword"
            name="newPassword"
            type="password"
            label="New Password"
            value={formData.newPassword}
            onChange={handleChange}
            error={errors.newPassword}
            autoComplete="new-password"
          />
          <DashInput
            id="confirmPassword"
            name="confirmPassword"
            type="password"
            label="Confirm New Password"
            value={formData.confirmPassword}
            onChange={handleChange}
            error={errors.confirmPassword}
            autoComplete="new-password"
          />
          <button
            type="submit"
            disabled={isSaving}
            className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-5 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 transition-colors"
          >
            {isSaving && <Loader2 size={15} className="animate-spin" />}
            {isSaving ? 'Updating…' : 'Update Password'}
          </button>
        </form>
      </div>

      <div className="pt-6 border-t border-[var(--dash-border)]">
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Active Sessions</h2>
        <p className="mt-1.5 text-sm text-[var(--dash-muted)] max-w-md">
          Sign out of PrepWise AI on every device you're currently logged into, including this one.
        </p>
        <button
          type="button"
          onClick={handleLogoutAllDevices}
          disabled={isLoggingOutAll}
          className="mt-4 inline-flex items-center gap-2 rounded-lg border border-red-500/30 px-5 py-2.5 text-sm font-semibold text-red-400 hover:bg-red-500/10 disabled:opacity-60 transition-colors"
        >
          {isLoggingOutAll ? <Loader2 size={15} className="animate-spin" /> : <LogOut size={15} />}
          {isLoggingOutAll ? 'Logging out…' : 'Logout from all devices'}
        </button>
      </div>
    </div>
  )
}

export default SecuritySection
