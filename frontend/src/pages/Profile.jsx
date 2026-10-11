import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Phone,
  GraduationCap,
  Building2,
  BookOpen,
  Award,
  MapPin,
  Link2,
  Github,
  Globe,
  Pencil,
  UserCircle,
} from 'lucide-react'
import profileApi from '../api/profileApi.js'
import ProfileField from '../components/profile/ProfileField.jsx'

function Profile() {
  const navigate = useNavigate()
  const [profile, setProfile] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchProfile = async () => {
      setIsLoading(true)
      setError('')
      try {
        const { data } = await profileApi.getProfile()
        setProfile(data.data)
      } catch {
        setError('Could not load your profile. Please refresh the page.')
      } finally {
        setIsLoading(false)
      }
    }
    fetchProfile()
  }, [])

  if (isLoading) {
    return (
      <div className="space-y-5">
        <div className="h-40 rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] animate-pulse" />
        <div className="h-64 rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] animate-pulse" />
      </div>
    )
  }

  if (error || !profile) {
    return (
      <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
        {error || 'Profile unavailable.'}
      </div>
    )
  }

  return (
    <div className="space-y-5">
      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">
        <div className="flex flex-col sm:flex-row sm:items-center gap-5">
          {profile.profileImage ? (
            <img
              src={profile.profileImage}
              alt={profile.fullName}
              className="h-20 w-20 rounded-full object-cover border-2 border-signal-amber/30"
            />
          ) : (
            <span className="flex h-20 w-20 items-center justify-center rounded-full bg-signal-amber/10 border-2 border-signal-amber/30 text-signal-amber">
              <UserCircle size={40} />
            </span>
          )}

          <div className="flex-1 min-w-0">
            <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">
              {profile.fullName}
            </h1>
            <p className="text-sm text-[var(--dash-muted)]">{profile.email}</p>
            {profile.location && (
              <p className="mt-1 flex items-center gap-1 text-xs text-[var(--dash-muted)]">
                <MapPin size={12} />
                {profile.location}
              </p>
            )}
          </div>

          <button
            type="button"
            onClick={() => navigate('/dashboard/profile/edit')}
            className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-4 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors shrink-0"
          >
            <Pencil size={15} />
            Edit Profile
          </button>
        </div>

        {profile.bio && (
          <p className="mt-5 pt-5 border-t border-[var(--dash-border)] text-sm text-[var(--dash-text)] leading-relaxed">
            {profile.bio}
          </p>
        )}
      </div>

      <div className="grid md:grid-cols-2 gap-5">
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 space-y-5">
          <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">
            Contact &amp; Links
          </h2>
          <ProfileField label="Phone Number" value={profile.phone} icon={Phone} />
          <ProfileField label="LinkedIn" value={profile.linkedinUrl} icon={Link2} />
          <ProfileField label="GitHub" value={profile.githubUrl} icon={Github} />
          <ProfileField label="Portfolio" value={profile.portfolioUrl} icon={Globe} />
        </div>

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 space-y-5">
          <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Education</h2>
          <ProfileField label="College" value={profile.college} icon={Building2} />
          <ProfileField label="Degree" value={profile.degree} icon={GraduationCap} />
          <ProfileField label="Branch" value={profile.branch} icon={BookOpen} />
          <div className="grid grid-cols-2 gap-4">
            <ProfileField label="Graduation Year" value={profile.graduationYear} />
            <ProfileField label="Current CGPA" value={profile.cgpa} />
          </div>
        </div>
      </div>

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)] flex items-center gap-2">
          <Award size={16} className="text-signal-amber" />
          Skills
        </h2>
        {profile.skills?.length > 0 ? (
          <div className="mt-3 flex flex-wrap gap-2">
            {profile.skills.map((skill) => (
              <span
                key={skill}
                className="rounded-md bg-signal-amber/10 border border-signal-amber/25 px-2.5 py-1 text-xs font-medium text-signal-amber"
              >
                {skill}
              </span>
            ))}
          </div>
        ) : (
          <p className="mt-3 text-sm text-[var(--dash-muted)]">No skills added yet.</p>
        )}
      </div>

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)] flex items-center gap-2">
          <Award size={16} className="text-signal-mint" />
          Certifications
        </h2>
        {profile.certifications?.length > 0 ? (
          <ul className="mt-3 space-y-2">
            {profile.certifications.map((cert) => (
              <li
                key={cert}
                className="rounded-lg border border-[var(--dash-border)] px-3 py-2 text-sm text-[var(--dash-text)]"
              >
                {cert}
              </li>
            ))}
          </ul>
        ) : (
          <p className="mt-3 text-sm text-[var(--dash-muted)]">No certifications added yet.</p>
        )}
      </div>
    </div>
  )
}

export default Profile
