import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Loader2, UserCircle, Camera } from 'lucide-react'
import profileApi from '../api/profileApi.js'
import DashInput from '../components/common/DashInput.jsx'
import TagInput from '../components/profile/TagInput.jsx'
import { useAuth } from '../context/AuthContext.jsx'
import { validateEmail, validateFullName } from '../utils/validators.js'

const EMPTY_FORM = {
  fullName: '',
  email: '',
  phone: '',
  college: '',
  degree: '',
  branch: '',
  graduationYear: '',
  cgpa: '',
  bio: '',
  linkedinUrl: '',
  githubUrl: '',
  portfolioUrl: '',
  location: '',
}

function EditProfile() {
  const navigate = useNavigate()
  const { refreshUser } = useAuth()
  const fileInputRef = useRef(null)

  const [formData, setFormData] = useState(EMPTY_FORM)
  const [skills, setSkills] = useState([])
  const [certifications, setCertifications] = useState([])
  const [profileImage, setProfileImage] = useState('')
  const [fieldErrors, setFieldErrors] = useState({})
  const [serverError, setServerError] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [isSaving, setIsSaving] = useState(false)
  const [isUploadingAvatar, setIsUploadingAvatar] = useState(false)

  useEffect(() => {
    const fetchProfile = async () => {
      setIsLoading(true)
      try {
        const { data } = await profileApi.getProfile()
        const profile = data.data
        setFormData({
          fullName: profile.fullName || '',
          email: profile.email || '',
          phone: profile.phone || '',
          college: profile.college || '',
          degree: profile.degree || '',
          branch: profile.branch || '',
          graduationYear: profile.graduationYear ?? '',
          cgpa: profile.cgpa ?? '',
          bio: profile.bio || '',
          linkedinUrl: profile.linkedinUrl || '',
          githubUrl: profile.githubUrl || '',
          portfolioUrl: profile.portfolioUrl || '',
          location: profile.location || '',
        })
        setSkills(profile.skills || [])
        setCertifications(profile.certifications || [])
        setProfileImage(profile.profileImage || '')
      } catch {
        setServerError('Could not load your profile. Please try again.')
      } finally {
        setIsLoading(false)
      }
    }
    fetchProfile()
  }, [])

  const handleChange = (event) => {
    const { name, value } = event.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    setFieldErrors((prev) => ({ ...prev, [name]: '' }))
    setServerError('')
  }

  const handleAvatarSelect = async (event) => {
    const file = event.target.files?.[0]
    if (!file) return

    setIsUploadingAvatar(true)
    setServerError('')
    try {
      const { data } = await profileApi.uploadAvatar(file)
      setProfileImage(data.data.profileImage)
      await refreshUser()
    } catch (error) {
      setServerError(error.response?.data?.message || 'Failed to upload image. Please try again.')
    } finally {
      setIsUploadingAvatar(false)
    }
  }

  const validate = () => {
    const errors = {
      fullName: validateFullName(formData.fullName),
      email: validateEmail(formData.email),
    }
    setFieldErrors(errors)
    return Object.values(errors).every((error) => !error)
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    if (!validate()) return

    setIsSaving(true)
    setServerError('')

    try {
      const payload = {
        ...formData,
        graduationYear: formData.graduationYear ? Number(formData.graduationYear) : null,
        cgpa: formData.cgpa ? Number(formData.cgpa) : null,
        skills,
        certifications,
      }
      await profileApi.updateProfile(payload)
      await refreshUser()
      navigate('/dashboard/profile', { replace: true })
    } catch (error) {
      setServerError(error.response?.data?.message || 'Failed to save profile. Please try again.')
    } finally {
      setIsSaving(false)
    }
  }

  if (isLoading) {
    return <div className="h-96 rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] animate-pulse" />
  }

  return (
    <div className="max-w-3xl">
      <h1 className="font-display text-xl font-semibold text-[var(--dash-text)] mb-5">Edit Profile</h1>

      {serverError && (
        <div className="mb-5 rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
          {serverError}
        </div>
      )}

      <form onSubmit={handleSubmit} noValidate className="space-y-6">
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">
          <div className="flex items-center gap-5">
            <div className="relative">
              {profileImage ? (
                <img
                  src={profileImage}
                  alt="Profile"
                  className="h-20 w-20 rounded-full object-cover border-2 border-signal-amber/30"
                />
              ) : (
                <span className="flex h-20 w-20 items-center justify-center rounded-full bg-signal-amber/10 border-2 border-signal-amber/30 text-signal-amber">
                  <UserCircle size={40} />
                </span>
              )}
              <button
                type="button"
                onClick={() => fileInputRef.current?.click()}
                disabled={isUploadingAvatar}
                className="absolute -bottom-1 -right-1 flex h-7 w-7 items-center justify-center rounded-full bg-signal-amber text-ink hover:bg-signal-amber/90 transition-colors disabled:opacity-60"
                aria-label="Change profile picture"
              >
                {isUploadingAvatar ? <Loader2 size={13} className="animate-spin" /> : <Camera size={13} />}
              </button>
              <input
                ref={fileInputRef}
                type="file"
                accept="image/png,image/jpeg,image/webp"
                className="hidden"
                onChange={handleAvatarSelect}
              />
            </div>
            <div>
              <p className="text-sm font-medium text-[var(--dash-text)]">Profile picture</p>
              <p className="text-xs text-[var(--dash-muted)] mt-0.5">PNG, JPG or WEBP. Max 5MB.</p>
            </div>
          </div>
        </div>

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 space-y-5">
          <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Basic Information</h2>
          <div className="grid sm:grid-cols-2 gap-5">
            <DashInput
              id="fullName"
              name="fullName"
              label="Full Name"
              value={formData.fullName}
              onChange={handleChange}
              error={fieldErrors.fullName}
            />
            <DashInput
              id="email"
              name="email"
              type="email"
              label="Email"
              value={formData.email}
              onChange={handleChange}
              error={fieldErrors.email}
            />
            <DashInput
              id="phone"
              name="phone"
              label="Phone Number"
              value={formData.phone}
              onChange={handleChange}
              placeholder="+91 98765 43210"
            />
            <DashInput
              id="location"
              name="location"
              label="Location"
              value={formData.location}
              onChange={handleChange}
              placeholder="City, Country"
            />
          </div>
          <div>
            <label htmlFor="bio" className="block text-sm font-medium text-[var(--dash-text)] mb-1.5">
              Bio
            </label>
            <textarea
              id="bio"
              name="bio"
              rows={4}
              value={formData.bio}
              onChange={handleChange}
              placeholder="Tell us a bit about yourself…"
              className="w-full rounded-lg border border-[var(--dash-border)] bg-[var(--dash-bg)] px-4 py-2.5 text-sm text-[var(--dash-text)] placeholder:text-[var(--dash-muted)]/70 focus:outline-none focus:ring-1 focus:ring-signal-amber/30 focus:border-signal-amber/60 transition-colors resize-none"
            />
          </div>
        </div>

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 space-y-5">
          <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Education</h2>
          <div className="grid sm:grid-cols-2 gap-5">
            <DashInput
              id="college"
              name="college"
              label="College"
              value={formData.college}
              onChange={handleChange}
            />
            <DashInput
              id="degree"
              name="degree"
              label="Degree"
              value={formData.degree}
              onChange={handleChange}
              placeholder="B.Tech, B.E., M.Sc…"
            />
            <DashInput
              id="branch"
              name="branch"
              label="Branch"
              value={formData.branch}
              onChange={handleChange}
              placeholder="Computer Science"
            />
            <DashInput
              id="graduationYear"
              name="graduationYear"
              type="number"
              label="Graduation Year"
              value={formData.graduationYear}
              onChange={handleChange}
              min="1950"
              max="2100"
            />
            <DashInput
              id="cgpa"
              name="cgpa"
              type="number"
              step="0.01"
              label="Current CGPA"
              value={formData.cgpa}
              onChange={handleChange}
              min="0"
              max="10"
            />
          </div>
        </div>

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 space-y-5">
          <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Skills &amp; Certifications</h2>
          <TagInput
            label="Skills"
            tags={skills}
            onChange={setSkills}
            placeholder="Add a skill…"
          />
          <TagInput
            label="Certifications"
            tags={certifications}
            onChange={setCertifications}
            placeholder="Add a certification…"
          />
        </div>

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 space-y-5">
          <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Links</h2>
          <div className="grid sm:grid-cols-2 gap-5">
            <DashInput
              id="linkedinUrl"
              name="linkedinUrl"
              label="LinkedIn URL"
              value={formData.linkedinUrl}
              onChange={handleChange}
              placeholder="https://linkedin.com/in/…"
            />
            <DashInput
              id="githubUrl"
              name="githubUrl"
              label="GitHub URL"
              value={formData.githubUrl}
              onChange={handleChange}
              placeholder="https://github.com/…"
            />
            <DashInput
              id="portfolioUrl"
              name="portfolioUrl"
              label="Portfolio URL"
              value={formData.portfolioUrl}
              onChange={handleChange}
              placeholder="https://yourdomain.com"
              className="sm:col-span-2"
            />
          </div>
        </div>

        <div className="flex items-center justify-end gap-3">
          <button
            type="button"
            onClick={() => navigate('/dashboard/profile')}
            className="rounded-lg border border-[var(--dash-border)] px-5 py-2.5 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={isSaving}
            className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-6 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 disabled:opacity-60 transition-colors"
          >
            {isSaving && <Loader2 size={15} className="animate-spin" />}
            {isSaving ? 'Saving…' : 'Save Changes'}
          </button>
        </div>
      </form>
    </div>
  )
}

export default EditProfile
