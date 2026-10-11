import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { Loader2 } from 'lucide-react'
import AuthLayout from '../components/layout/AuthLayout.jsx'
import FormInput from '../components/common/FormInput.jsx'
import { useAuth } from '../context/AuthContext.jsx'
import { validateEmail } from '../utils/validators.js'

function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const [formData, setFormData] = useState({ email: '', password: '' })
  const [fieldErrors, setFieldErrors] = useState({})
  const [serverError, setServerError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleChange = (event) => {
    const { name, value } = event.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    setFieldErrors((prev) => ({ ...prev, [name]: '' }))
    setServerError('')
  }

  const validate = () => {
    const errors = {
      email: validateEmail(formData.email),
      password: formData.password ? '' : 'Password is required',
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
      await login(formData)
      const redirectTo = location.state?.from?.pathname || '/dashboard'
      navigate(redirectTo, { replace: true })
    } catch (error) {
      setServerError(error.response?.data?.message || 'Invalid email or password.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <AuthLayout
      eyebrow="WELCOME BACK"
      title="Log in to your account"
      subtitle="Pick up your prep exactly where you left off."
      footer={
        <>
          Don't have an account?{' '}
          <Link to="/register" className="text-signal-amber font-medium hover:underline">
            Register
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
          value={formData.email}
          onChange={handleChange}
          error={fieldErrors.email}
          autoComplete="email"
        />

        <FormInput
          id="password"
          name="password"
          type="password"
          label="Password"
          placeholder="••••••••"
          value={formData.password}
          onChange={handleChange}
          error={fieldErrors.password}
          autoComplete="current-password"
        />

        <div className="flex justify-end -mt-2">
          <Link to="/forgot-password" className="text-xs text-muted hover:text-signal-amber transition-colors">
            Forgot password?
          </Link>
        </div>

        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full inline-flex items-center justify-center gap-2 rounded-lg bg-signal-amber px-6 py-3 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 disabled:cursor-not-allowed transition-colors"
        >
          {isSubmitting && <Loader2 size={16} className="animate-spin" />}
          {isSubmitting ? 'Logging in…' : 'Log in'}
        </button>
      </form>
    </AuthLayout>
  )
}

export default Login