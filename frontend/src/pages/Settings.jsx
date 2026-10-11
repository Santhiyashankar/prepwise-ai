import { useState } from 'react'
import SettingsNav from '../components/settings/SettingsNav.jsx'
import AccountSection from '../components/settings/AccountSection.jsx'
import SecuritySection from '../components/settings/SecuritySection.jsx'
import NotificationsSection from '../components/settings/NotificationsSection.jsx'
import AppearanceSection from '../components/settings/AppearanceSection.jsx'
import PrivacySection from '../components/settings/PrivacySection.jsx'

const SECTION_COMPONENTS = {
  account: AccountSection,
  security: SecuritySection,
  notifications: NotificationsSection,
  appearance: AppearanceSection,
  privacy: PrivacySection,
}

function Settings() {
  const [activeSection, setActiveSection] = useState('account')
  const ActiveComponent = SECTION_COMPONENTS[activeSection]

  return (
    <div>
      <h1 className="font-display text-xl font-semibold text-[var(--dash-text)] mb-5">Settings</h1>

      <div className="flex flex-col lg:flex-row gap-6">
        <SettingsNav activeSection={activeSection} onSelect={setActiveSection} />

        <div className="flex-1 min-w-0 rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">
          <ActiveComponent />
        </div>
      </div>
    </div>
  )
}

export default Settings
