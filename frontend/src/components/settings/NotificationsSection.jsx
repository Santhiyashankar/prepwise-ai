import { useEffect, useState } from 'react'
import profileApi from '../../api/profileApi.js'
import ToggleSwitch from '../common/ToggleSwitch.jsx'

const TOGGLE_CONFIG = [
  {
    key: 'emailNotifications',
    label: 'Email Notifications',
    description: 'Receive general updates and announcements by email.',
  },
  {
    key: 'interviewReminders',
    label: 'Interview Reminders',
    description: 'Get reminded before your scheduled mock interviews.',
  },
  {
    key: 'weeklyProgressReports',
    label: 'Weekly Progress Reports',
    description: 'A summary of your practice activity, sent every week.',
  },
  {
    key: 'placementAlerts',
    label: 'Placement Alerts',
    description: 'Alerts about new company drives matching your profile.',
  },
]

function NotificationsSection() {
  const [preferences, setPreferences] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [savingKey, setSavingKey] = useState('')
  const [errorMessage, setErrorMessage] = useState('')

  useEffect(() => {
    const fetchPreferences = async () => {
      setIsLoading(true)
      try {
        const { data } = await profileApi.getNotificationPreferences()
        setPreferences(data.data)
      } catch {
        setErrorMessage('Could not load notification preferences.')
      } finally {
        setIsLoading(false)
      }
    }
    fetchPreferences()
  }, [])

  const handleToggle = async (key, value) => {
    const previous = preferences
    const updated = { ...preferences, [key]: value }
    setPreferences(updated)
    setSavingKey(key)
    setErrorMessage('')

    try {
      await profileApi.updateNotificationPreferences(updated)
    } catch (error) {
      setPreferences(previous)
      setErrorMessage(error.response?.data?.message || 'Failed to update preference.')
    } finally {
      setSavingKey('')
    }
  }

  if (isLoading) {
    return <div className="h-56 rounded-xl bg-[var(--dash-surface-hover)] animate-pulse" />
  }

  return (
    <div className="space-y-3">
      <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Notifications</h2>

      {errorMessage && (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {errorMessage}
        </div>
      )}

      <div className="divide-y divide-[var(--dash-border)]">
        {TOGGLE_CONFIG.map((item) => (
          <ToggleSwitch
            key={item.key}
            label={item.label}
            description={item.description}
            checked={Boolean(preferences?.[item.key])}
            disabled={savingKey === item.key}
            onChange={(value) => handleToggle(item.key, value)}
          />
        ))}
      </div>
    </div>
  )
}

export default NotificationsSection
