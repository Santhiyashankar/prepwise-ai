import { createContext, useContext, useEffect, useMemo, useState } from 'react'

const THEME_KEY = 'prepwise_theme'

const ThemeContext = createContext(null)

export function ThemeProvider({ children }) {
  const [theme, setTheme] = useState(() => {
    return localStorage.getItem(THEME_KEY) || 'system'
  })

  useEffect(() => {
    localStorage.setItem(THEME_KEY, theme)

    const root = document.documentElement

    // Remove previous theme classes
    root.classList.remove('light', 'dark')

    if (theme === 'system') {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      root.classList.add(prefersDark ? 'dark' : 'light')
    } else {
      root.classList.add(theme)
    }
  }, [theme])

  useEffect(() => {
    if (theme !== 'system') return

    const media = window.matchMedia('(prefers-color-scheme: dark)')

    const handler = (e) => {
      const root = document.documentElement
      root.classList.remove('light', 'dark')
      root.classList.add(e.matches ? 'dark' : 'light')
    }

    media.addEventListener('change', handler)

    return () => media.removeEventListener('change', handler)
  }, [theme])

  const toggleTheme = () => {
    setTheme((prev) => {
      if (prev === 'light') return 'dark'
      if (prev === 'dark') return 'light'
      return 'dark'
    })
  }

  const value = useMemo(
    () => ({
      theme,
      setTheme,
      toggleTheme,
      isLight: theme === 'light',
    }),
    [theme]
  )

  return (
    <ThemeContext.Provider value={value}>
      {children}
    </ThemeContext.Provider>
  )
}

export function useTheme() {
  const context = useContext(ThemeContext)

  if (!context) {
    throw new Error('useTheme must be used within a ThemeProvider')
  }

  return context
}

export default ThemeContext