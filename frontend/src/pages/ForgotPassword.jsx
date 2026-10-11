import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Loader2, MailCheck } from 'lucide-react'
import AuthLayout from '../components/layout/AuthLayout.jsx'
import FormInput from '../components/common/FormInput.jsx'
import authApi from '../api/authApi.js'
import { validateEmail } from '../utils/validators.js'

function ForgotPassword() {
  const [email, setEmail] = useState('')
  const [fieldError, setFieldError] = useState('')
  const [serverError, setServerError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [isSubmitted, setIsSubmitted] = useState(false)

  const handleChange = (event) => {
    setEmail(event.target.value)
    setFieldError('')
    setServerError('')
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    const error = validateEmail(email)
    setFieldError(error)
    if (error) return

    setIsSubmitting(true)
    setServerError('')

    try {
      await authApi.forgotPassword(email)
      setIsSubmitted(true)
    } catch (error_) {
      setServerError(error_.response?.data?.message || 'Something went wrong. Please try again.')
    } finally {
      setIsSubmitting(false)
    }
  }

  if (isSubmitted) {
    return (
      <AuthLayout
        eyebrow="CHECK YOUR INBOX"
        title="Reset link sent"
        subtitle="If an account exists for that email, a reset link is on its way."
      >
        <div className="flex flex-col items-center text-center gap-4 py-4">
          <span className="flex h-12 w-12 items-center justify-center rounded-full bg-signal-mint/10 border border-signal-mint/30 text-signal-mint">
            <MailCheck size={22} />
          </span>
          <p className="text-sm text-muted leading-relaxed">
            The link expires in 30 minutes. Check your spam folder if it doesn't arrive shortly.
          </p>
          <Link
            to="/login"
            className="mt-2 text-sm font-medium text-signal-amber hover:underline"
          >
            Back to log in
          </Link>
        </div>
      </AuthLayout>
    )
  }

  return (
    <AuthLayout
      eyebrow="RESET PASSWORD"
      title="Forgot your password?"
      subtitle="Enter your email and we'll send you a reset link."
      footer={
        <>
          Remembered it?{' '}
          <Link to="/login" className="text-signal-amber font-medium hover:underline">
            Log in
          </Link>
        </>
      }
    >
      <form onSubmit={handleSubmit} noValidate className="space-y-5">
        {serverError && (
          <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
            {serverError}
          </div>
        )}

        <FormInput
          id="email"
          name="email"
          type="email"
          label="Email address"
          placeholder="you@example.com"
          value={email}
          onChange={handleChange}
          error={fieldError}
          autoComplete="email"
        />

        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full inline-flex items-center justify-center gap-2 rounded-lg bg-signal-amber px-6 py-3 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 disabled:cursor-not-allowed transition-colors"
        >
          {isSubmitting && <Loader2 size={16} className="animate-spin" />}
          {isSubmitting ? 'Sending link…' : 'Send reset link'}
        </button>
      </form>
    </AuthLayout>
  )
}

export default ForgotPassword