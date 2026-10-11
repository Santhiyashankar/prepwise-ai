import { useState } from 'react'
import { Outlet } from 'react-router-dom'
import Sidebar from './Sidebar.jsx'
import Topbar from './Topbar.jsx'
import { ThemeProvider, useTheme } from '../../context/ThemeContext.jsx'

function DashboardShell() {
  const [isCollapsed, setIsCollapsed] = useState(false)
  const [isMobileOpen, setIsMobileOpen] = useState(false)
  const { isLight } = useTheme()

  return (
    <div className={`dashboard-root ${isLight ? 'light' : ''} min-h-screen flex bg-[var(--dash-bg)]`}>
      <Sidebar
        isCollapsed={isCollapsed}
        onToggle={() => setIsCollapsed((prev) => !prev)}
        isMobileOpen={isMobileOpen}
        onCloseMobile={() => setIsMobileOpen(false)}
      />

      <div className="flex-1 min-w-0 flex flex-col">
        <Topbar onOpenMobileSidebar={() => setIsMobileOpen(true)} />
        <main className="flex-1 px-4 sm:px-6 py-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}

function DashboardLayout() {
  return (
    <ThemeProvider>
      <DashboardShell />
    </ThemeProvider>
  )
}

export default DashboardLayout
