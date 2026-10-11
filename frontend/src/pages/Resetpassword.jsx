import { useState } from 'react'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'
import { Loader2, ShieldCheck, TriangleAlert } from 'lucide-react'
import AuthLayout from '../components/layout/AuthLayout.jsx'
import FormInput from '../components/common/FormInput.jsx'
import authApi from '../api/authApi.js'
import { validateConfirmPassword, validatePassword } from '../utils/validators.js'

function ResetPassword() {
  const [searchParams] = useSearchParams()
  const navigate = useNavigate()
  const token = searchParams.get('token')

  const [formData, setFormData] = useState({ password: '', confirmPassword: '' })
  const [fieldErrors, setFieldErrors] = useState({})
  const [serverError, setServerError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [isSuccess, setIsSuccess] = useState(false)

  const handleChange = (event) => {
    const { name, value } = event.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    setFieldErrors((prev) => ({ ...prev, [name]: '' }))
    setServerError('')
  }

  const validate = () => {
    const errors = {
      password: validatePassword(formData.password),
      confirmPassword: validateConfirmPassword(formData.password, formData.confirmPassword),
    }
    setFieldErrors(errors)
    return Object.values(errors).every((error) => !error)
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    if (!validate()) return

    setIsSubmitting(true)
    setServerError('')

    try {
      await authApi.resetPassword({ token, newPassword: formData.password })
      setIsSuccess(true)
    } catch (error) {
      setServerError(error.response?.data?.message || 'This reset link is invalid or has expired.')
    } finally {
      setIsSubmitting(false)
    }
  }

  if (!token) {
    return (
      <AuthLayout eyebrow="RESET PASSWORD" title="Invalid reset link">
        <div className="flex flex-col items-center text-center gap-4 py-4">
          <span className="flex h-12 w-12 items-center justify-center rounded-full bg-red-500/10 border border-red-500/30 text-red-400">
            <TriangleAlert size={22} />
          </span>
          <p className="text-sm text-muted leading-relaxed">
            This reset link is missing a token. Request a new one to continue.
          </p>
          <Link to="/forgot-password" className="text-sm font-medium text-signal-amber hover:underline">
            Request new link
          </Link>
        </div>
      </AuthLayout>
    )
  }

  if (isSuccess) {
    return (
      <AuthLayout eyebrow="ALL SET" title="Password updated">
        <div className="flex flex-col items-center text-center gap-4 py-4">
          <span className="flex h-12 w-12 items-center justify-center rounded-full bg-signal-mint/10 border border-signal-mint/30 text-signal-mint">
            <ShieldCheck size={22} />
          </span>
          <p className="text-sm text-muted leading-relaxed">
            Your password has been reset. Log in with your new password.
          </p>
          <button
            type="button"
            onClick={() => navigate('/login', { replace: true })}
            className="mt-1 inline-flex items-center justify-center rounded-lg bg-signal-amber px-6 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
          >
            Go to log in
          </button>
        </div>
      </AuthLayout>
    )
  }

  return (
    <AuthLayout eyebrow="RESET PASSWORD" title="Set a new password" subtitle="Make it something you'll remember.">
      <form onSubmit={handleSubmit} noValidate className="space-y-5">
        {serverError && (
          <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
            {serverError}
          </div>
        )}

        <FormInput
          id="password"
          name="password"
          type="password"
          label="New password"
          placeholder="At least 8 characters"
          value={formData.password}
          onChange={handleChange}
          error={fieldErrors.password}
          autoComplete="new-password"
        />

        <FormInput
          id="confirmPassword"
          name="confirmPassword"
          type="password"
          label="Confirm new password"
          placeholder="Re-enter your new password"
          value={formData.confirmPassword}
          onChange={handleChange}
          error={fieldErrors.confirmPassword}
          autoComplete="new-password"
        />

        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full inline-flex items-center justify-center gap-2 rounded-lg bg-signal-amber px-6 py-3 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 disabled:cursor-not-allowed transition-colors"
        >
          {isSubmitting && <Loader2 size={16} className="animate-spin" />}
          {isSubmitting ? 'Updating…' : 'Update password'}
        </button>
      </form>
    </AuthLayout>
  )
}

export default ResetPassword