import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import authApi from '../api/authApi.js'
import tokenStorage from '../utils/tokenStorage.js'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => tokenStorage.getUser())
  const [isLoading, setIsLoading] = useState(true)
  const [authError, setAuthError] = useState(null)

  useEffect(() => {
    const bootstrapSession = async () => {
      const accessToken = tokenStorage.getAccessToken()

      if (!accessToken) {
        setIsLoading(false)
        return
      }

      try {
        const { data } = await authApi.getCurrentUser()
        tokenStorage.setUser(data.data)
        setUser(data.data)
      } catch {
        tokenStorage.clear()
        setUser(null)
      } finally {
        setIsLoading(false)
      }
    }

    bootstrapSession()
  }, [])

  // Refresh logged-in user after profile update
  const refreshUser = useCallback(async () => {
    try {
      const { data } = await authApi.getCurrentUser()
      tokenStorage.setUser(data.data)
      setUser(data.data)
      return data.data
    } catch (error) {
      tokenStorage.clear()
      setUser(null)
      throw error
    }
  }, [])

  const register = useCallback(async (payload) => {
    setAuthError(null)

    try {
      const { data } = await authApi.register(payload)
      const authData = data.data

      tokenStorage.setSession({
        accessToken: authData.accessToken,
        refreshToken: authData.refreshToken,
        user: authData.user,
      })

      setUser(authData.user)
      return authData
    } catch (error) {
      const message =
        error.response?.data?.message ||
        'Registration failed. Please try again.'

      setAuthError(message)
      throw error
    }
  }, [])

  const login = useCallback(async (payload) => {
    setAuthError(null)

    try {
      const { data } = await authApi.login(payload)
      const authData = data.data

      tokenStorage.setSession({
        accessToken: authData.accessToken,
        refreshToken: authData.refreshToken,
        user: authData.user,
      })

      setUser(authData.user)
      return authData
    } catch (error) {
      const message =
        error.response?.data?.message ||
        'Invalid email or password.'

      setAuthError(message)
      throw error
    }
  }, [])

  const logout = useCallback(async () => {
    const refreshToken = tokenStorage.getRefreshToken()

    try {
      if (refreshToken) {
        await authApi.logout(refreshToken)
      }
    } catch {
      // Ignore network errors
    } finally {
      tokenStorage.clear()
      setUser(null)
    }
  }, [])

  const clearAuthError = useCallback(() => {
    setAuthError(null)
  }, [])

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: Boolean(user),
      isLoading,
      authError,
      register,
      login,
      logout,
      refreshUser,
      clearAuthError,
    }),
    [
      user,
      isLoading,
      authError,
      register,
      login,
      logout,
      refreshUser,
      clearAuthError,
    ]
  )

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)

  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }

  return context
}

export default AuthContext