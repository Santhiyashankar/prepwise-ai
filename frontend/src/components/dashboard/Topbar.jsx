import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Bell, Menu, Moon, Search, Sun, ChevronDown, UserCircle, Settings, LogOut } from 'lucide-react'
import { useAuth } from '../../context/AuthContext.jsx'
import { useTheme } from '../../context/ThemeContext.jsx'

function Topbar({ onOpenMobileSidebar }) {
  const { user, logout } = useAuth()
  const { theme, toggleTheme } = useTheme()
  const navigate = useNavigate()
  const [isDropdownOpen, setIsDropdownOpen] = useState(false)
  const dropdownRef = useRef(null)

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsDropdownOpen(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  const handleLogout = async () => {
    await logout()
    navigate('/login', { replace: true })
  }

  const initials = user?.fullName
    ? user.fullName
        .split(' ')
        .map((part) => part[0])
        .slice(0, 2)
        .join('')
        .toUpperCase()
    : 'U'

  return (
    <header className="sticky top-0 z-30 h-16 shrink-0 border-b border-[var(--dash-border)] bg-[var(--dash-surface)]/95 backdrop-blur">
      <div className="flex h-full items-center justify-between gap-4 px-4 sm:px-6">
        <div className="flex items-center gap-3 min-w-0">
          <button
            type="button"
            onClick={onOpenMobileSidebar}
            className="lg:hidden text-[var(--dash-text)] p-1.5 -ml-1.5"
            aria-label="Open sidebar"
          >
            <Menu size={22} />
          </button>
          <div className="min-w-0 hidden sm:block">
            <p className="text-sm font-medium text-[var(--dash-text)] truncate">
              Welcome back{user?.fullName ? `, ${user.fullName.split(' ')[0]}` : ''}
            </p>
            <p className="text-xs text-[var(--dash-muted)]">Let's get you interview-ready.</p>
          </div>
        </div>

        <div className="hidden md:flex flex-1 max-w-md">
          <div className="relative w-full">
            <Search
              size={16}
              className="absolute left-3 top-1/2 -translate-y-1/2 text-[var(--dash-muted)]"
            />
            <input
              type="text"
              placeholder="Search interviews, topics, companies…"
              className="w-full rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] py-2 pl-9 pr-3 text-sm text-[var(--dash-text)] placeholder:text-[var(--dash-muted)]/70 focus:outline-none focus:ring-1 focus:ring-signal-amber/40 focus:border-signal-amber/50 transition-colors"
            />
          </div>
        </div>

        <div className="flex items-center gap-2 sm:gap-3 shrink-0">
          <button
            type="button"
            onClick={toggleTheme}
            className="flex h-9 w-9 items-center justify-center rounded-lg border border-[var(--dash-border)] text-[var(--dash-muted)] hover:text-signal-amber hover:border-signal-amber/40 transition-colors"
            aria-label="Toggle theme"
          >
            {theme === 'dark' ? <Sun size={17} /> : <Moon size={17} />}
          </button>

          <button
            type="button"
            className="relative flex h-9 w-9 items-center justify-center rounded-lg border border-[var(--dash-border)] text-[var(--dash-muted)] hover:text-signal-amber hover:border-signal-amber/40 transition-colors"
            aria-label="Notifications"
          >
            <Bell size={17} />
            <span className="absolute top-1.5 right-1.5 h-1.5 w-1.5 rounded-full bg-signal-amber" />
          </button>

          <div className="relative" ref={dropdownRef}>
            <button
              type="button"
              onClick={() => setIsDropdownOpen((prev) => !prev)}
              className="flex items-center gap-2 rounded-lg border border-[var(--dash-border)] pl-1.5 pr-2.5 py-1.5 hover:border-signal-amber/40 transition-colors"
              aria-expanded={isDropdownOpen}
            >
              <span className="flex h-7 w-7 items-center justify-center rounded-full bg-signal-amber/15 text-signal-amber text-xs font-semibold">
                {initials}
              </span>
              <ChevronDown size={14} className="text-[var(--dash-muted)] hidden sm:block" />
            </button>

            {isDropdownOpen && (
              <div className="absolute right-0 mt-2 w-52 rounded-lg border border-[var(--dash-border)] bg-[var(--dash-surface)] shadow-xl py-1.5">
                <div className="px-3 py-2 border-b border-[var(--dash-border)]">
                  <p className="text-sm font-medium text-[var(--dash-text)] truncate">{user?.fullName}</p>
                  <p className="text-xs text-[var(--dash-muted)] truncate">{user?.email}</p>
                </div>
                <button
                  type="button"
                  onClick={() => {
                    setIsDropdownOpen(false)
                    navigate('/dashboard/profile')
                  }}
                  className="flex w-full items-center gap-2 px-3 py-2 text-sm text-[var(--dash-text)] hover:bg-[var(--dash-surface-hover)] transition-colors"
                >
                  <UserCircle size={16} />
                  Profile
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setIsDropdownOpen(false)
                    navigate('/dashboard/settings')
                  }}
                  className="flex w-full items-center gap-2 px-3 py-2 text-sm text-[var(--dash-text)] hover:bg-[var(--dash-surface-hover)] transition-colors"
                >
                  <Settings size={16} />
                  Settings
                </button>
                <button
                  type="button"
                  onClick={handleLogout}
                  className="flex w-full items-center gap-2 px-3 py-2 text-sm text-red-400 hover:bg-red-500/10 transition-colors"
                >
                  <LogOut size={16} />
                  Logout
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  )
}

export default Topbar
