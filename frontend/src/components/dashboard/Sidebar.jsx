import { NavLink, useNavigate } from 'react-router-dom'
import {
  LayoutDashboard,
  FileText,
  MessagesSquare,
  Code2,
  Users,
  CalendarClock,
  Compass,
  Map,
  History,
  Trophy,
  UserCircle,
  Settings,
  LogOut,
  ChevronsLeft,
  ChevronsRight,
  Mic,
} from 'lucide-react'
import { useAuth } from '../../context/AuthContext.jsx'

const MENU_ITEMS = [
  { label: 'Dashboard', to: '/dashboard', icon: LayoutDashboard, end: true },
  { label: 'Resume', to: '/dashboard/resume', icon: FileText },
  { label: 'Interviews', to: '/dashboard/interviews', icon: MessagesSquare },
  { label: 'Coding', to: '/dashboard/coding', icon: Code2 },
  { label: 'Group Discussion', to: '/dashboard/group-discussion', icon: Users },
  { label: 'Study Plan', to: '/dashboard/study-plan', icon: CalendarClock },
  { label: 'AI Mentor', to: '/dashboard/ai-mentor', icon: Compass },
  { label: 'Career Roadmap', to: '/dashboard/career-roadmap', icon: Map },
  { label: 'Interview History', to: '/dashboard/history', icon: History },
  { label: 'Leaderboard', to: '/dashboard/leaderboard', icon: Trophy },
  { label: 'Profile', to: '/dashboard/profile', icon: UserCircle },
  { label: 'Settings', to: '/dashboard/settings', icon: Settings },
]

function Sidebar({ isCollapsed, onToggle, isMobileOpen, onCloseMobile }) {
  const { logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = async () => {
    await logout()
    navigate('/login', { replace: true })
  }

  const navLinkClasses = ({ isActive }) =>
    `flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors ${
      isActive
        ? 'bg-signal-amber/15 text-signal-amber'
        : 'text-[var(--dash-muted)] hover:bg-[var(--dash-surface-hover)] hover:text-[var(--dash-text)]'
    }`

  return (
    <>
      {isMobileOpen && (
        <div
          className="fixed inset-0 z-40 bg-black/60 lg:hidden"
          onClick={onCloseMobile}
          aria-hidden="true"
        />
      )}

      <aside
        className={`fixed lg:sticky top-0 left-0 z-50 h-screen shrink-0 border-r border-[var(--dash-border)] bg-[var(--dash-surface)] transition-all duration-300 flex flex-col
          ${isCollapsed ? 'lg:w-20' : 'lg:w-64'}
          ${isMobileOpen ? 'w-64 translate-x-0' : 'w-64 -translate-x-full lg:translate-x-0'}
        `}
      >
        <div className="flex items-center justify-between h-16 px-4 border-b border-[var(--dash-border)] shrink-0">
          <div className="flex items-center gap-2 overflow-hidden">
            <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-signal-amber/10 border border-signal-amber/30 text-signal-amber">
              <Mic size={18} strokeWidth={2} />
            </span>
            {!isCollapsed && (
              <span className="font-display text-base font-semibold tracking-tight text-[var(--dash-text)] whitespace-nowrap">
                PrepWise <span className="text-signal-amber">AI</span>
              </span>
            )}
          </div>
          <button
            type="button"
            onClick={onToggle}
            className="hidden lg:flex items-center justify-center h-7 w-7 rounded-md text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
            aria-label={isCollapsed ? 'Expand sidebar' : 'Collapse sidebar'}
          >
            {isCollapsed ? <ChevronsRight size={16} /> : <ChevronsLeft size={16} />}
          </button>
        </div>

        <nav className="flex-1 overflow-y-auto px-3 py-4 space-y-1">
          {MENU_ITEMS.map((item) => (
            <NavLink
              key={item.label}
              to={item.to}
              end={item.end}
              onClick={onCloseMobile}
              className={navLinkClasses}
              title={isCollapsed ? item.label : undefined}
            >
              <item.icon size={18} strokeWidth={1.8} className="shrink-0" />
              {!isCollapsed && <span className="whitespace-nowrap">{item.label}</span>}
            </NavLink>
          ))}
        </nav>

        <div className="px-3 py-4 border-t border-[var(--dash-border)] shrink-0">
          <button
            type="button"
            onClick={handleLogout}
            className="flex w-full items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium text-[var(--dash-muted)] hover:bg-red-500/10 hover:text-red-400 transition-colors"
            title={isCollapsed ? 'Logout' : undefined}
          >
            <LogOut size={18} strokeWidth={1.8} className="shrink-0" />
            {!isCollapsed && <span>Logout</span>}
          </button>
        </div>
      </aside>
    </>
  )
}

export default Sidebar
