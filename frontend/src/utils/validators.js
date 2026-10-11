export function validateEmail(email) {
  if (!email.trim()) return 'Email is required'
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(email)) return 'Enter a valid email address'
  return ''
}

export function validatePassword(password) {
  if (!password) return 'Password is required'
  if (password.length < 8) return 'Password must be at least 8 characters'
  return ''
}

export function validateFullName(fullName) {
  if (!fullName.trim()) return 'Full name is required'
  if (fullName.trim().length < 2) return 'Full name must be at least 2 characters'
  return ''
}

export function validateConfirmPassword(password, confirmPassword) {
  if (!confirmPassword) return 'Please confirm your password'
  if (password !== confirmPassword) return 'Passwords do not match'
  return ''
}