import { useEffect, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { Loader2, MailWarning, ShieldCheck } from 'lucide-react'
import AuthLayout from '../components/layout/AuthLayout.jsx'

function VerifyEmail() {
  const [searchParams] = useSearchParams()
  const token = searchParams.get('token')
  const [status, setStatus] = useState(token ? 'verifying' : 'missing')

  useEffect(() => {
    if (!token) return
    const timer = setTimeout(() => setStatus('success'), 1600)
    return () => clearTimeout(timer)
  }, [token])

  if (status === 'missing') {
    return (
      <AuthLayout eyebrow="EMAIL VERIFICATION" title="Verification link missing">
        <div className="flex flex-col items-center text-center gap-4 py-4">
          <span className="flex h-12 w-12 items-center justify-center rounded-full bg-red-500/10 border border-red-500/30 text-red-400">
            <MailWarning size={22} />
          </span>
          <p className="text-sm text-muted leading-relaxed">
            This link is missing a verification token. Check your inbox for the original email.
          </p>
          <Link to="/login" className="text-sm font-medium text-signal-amber hover:underline">
            Back to log in
          </Link>
        </div>
      </AuthLayout>
    )
  }

  if (status === 'verifying') {
    return (
      <AuthLayout eyebrow="EMAIL VERIFICATION" title="Verifying your email">
        <div className="flex flex-col items-center text-center gap-4 py-6">
          <Loader2 size={28} className="text-signal-amber animate-spin" />
          <p className="text-sm text-muted leading-relaxed">This will only take a moment.</p>
        </div>
      </AuthLayout>
    )
  }

  return (
    <AuthLayout eyebrow="EMAIL VERIFICATION" title="Email verified">
      <div className="flex flex-col items-center text-center gap-4 py-4">
        <span className="flex h-12 w-12 items-center justify-center rounded-full bg-signal-mint/10 border border-signal-mint/30 text-signal-mint">
          <ShieldCheck size={22} />
        </span>
        <p className="text-sm text-muted leading-relaxed">
          Your email address has been verified. You're all set to continue.
        </p>
        <Link
          to="/login"
          className="mt-1 inline-flex items-center justify-center rounded-lg bg-signal-amber px-6 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
        >
          Continue to log in
        </Link>
      </div>
    </AuthLayout>
  )
}

export default VerifyEmail