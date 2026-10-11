import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import {
  ArrowLeft,
  ThumbsUp,
  ThumbsDown,
  FileWarning,
  SpellCheck,
  LayoutTemplate,
  FolderKanban,
  Briefcase,
  Sparkles,
} from 'lucide-react'
import resumeApi from '../../api/resumeApi.js'
import ATSScoreCard from '../../components/resume/ATSScoreCard.jsx'
import ImprovementCard from '../../components/resume/ImprovementCard.jsx'
import KeywordChart from '../../components/resume/KeywordChart.jsx'
import KeywordTable from '../../components/resume/KeywordTable.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'

const TABS = [
  { id: 'overview', label: 'Overview' },
  { id: 'ats', label: 'ATS Report' },
  { id: 'keywords', label: 'Keywords' },
]

function ResumeAnalysisPage() {
  const { resumeId } = useParams()
  const navigate = useNavigate()
  const [data, setData] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [errorMessage, setErrorMessage] = useState('')
  const [activeTab, setActiveTab] = useState('overview')

  useEffect(() => {
    const fetchResume = async () => {
      setIsLoading(true)
      setErrorMessage('')
      try {
        const { data: response } = await resumeApi.getById(resumeId)
        setData(response.data)
      } catch (error) {
        setErrorMessage(error.response?.data?.message || 'Could not load this resume.')
      } finally {
        setIsLoading(false)
      }
    }
    fetchResume()
  }, [resumeId])

  if (isLoading) {
    return (
      <div className="space-y-5">
        <LoadingSkeleton className="h-10 w-48" />
        <LoadingSkeleton className="h-96 w-full" />
      </div>
    )
  }

  if (errorMessage || !data) {
    return (
      <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
        {errorMessage || 'Resume not found.'}
      </div>
    )
  }

  const { resume, analysis, keywordAnalysis } = data

  return (
    <div className="space-y-5">
      <button
        type="button"
        onClick={() => navigate('/dashboard/resume')}
        className="inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
      >
        <ArrowLeft size={15} />
        Back to Resume Analyzer
      </button>

      <div>
        <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">{resume.fileName}</h1>
        <p className="text-sm text-[var(--dash-muted)] mt-1">Version {resume.version}</p>
      </div>

      {!analysis ? (
        <p className="text-sm text-[var(--dash-muted)]">This resume hasn't been analyzed yet.</p>
      ) : (
        <>
          <div className="flex gap-1 border-b border-[var(--dash-border)]">
            {TABS.map((tab) => (
              <button
                key={tab.id}
                type="button"
                onClick={() => setActiveTab(tab.id)}
                className={`px-4 py-2.5 text-sm font-medium border-b-2 transition-colors ${
                  activeTab === tab.id
                    ? 'border-signal-amber text-signal-amber'
                    : 'border-transparent text-[var(--dash-muted)] hover:text-[var(--dash-text)]'
                }`}
              >
                {tab.label}
              </button>
            ))}
          </div>

          {activeTab === 'overview' && (
            <div className="space-y-5">
              <div className="grid sm:grid-cols-3 gap-4">
                <ATSScoreCard label="ATS Score" score={analysis.atsScore} />
                <ATSScoreCard label="AI Score" score={analysis.aiScore} />
                <ATSScoreCard label="Overall Rating" score={analysis.overallRating} />
              </div>

              {analysis.professionalHeadline && (
                <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
                  <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">
                    Professional Headline
                  </p>
                  <p className="mt-1.5 text-sm font-medium text-[var(--dash-text)]">
                    {analysis.professionalHeadline}
                  </p>
                </div>
              )}

              {analysis.professionalSummary && (
                <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
                  <div className="flex items-center gap-2">
                    <Sparkles size={15} className="text-signal-amber" />
                    <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">
                      Professional Summary
                    </p>
                  </div>
                  <p className="mt-2 text-sm text-[var(--dash-text)] leading-relaxed">
                    {analysis.professionalSummary}
                  </p>
                </div>
              )}

              {analysis.careerObjective && (
                <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
                  <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">
                    Suggested Career Objective
                  </p>
                  <p className="mt-1.5 text-sm text-[var(--dash-text)] leading-relaxed">
                    {analysis.careerObjective}
                  </p>
                </div>
              )}

              <div className="grid lg:grid-cols-2 gap-5">
                <ImprovementCard title="Strengths" icon={ThumbsUp} tone="positive" items={analysis.strengths} />
                <ImprovementCard title="Weaknesses" icon={ThumbsDown} tone="negative" items={analysis.weaknesses} />
                <ImprovementCard
                  title="Missing Sections"
                  icon={FileWarning}
                  items={analysis.missingSections}
                />
                <ImprovementCard title="Grammar Suggestions" icon={SpellCheck} items={analysis.grammarSuggestions} />
                <ImprovementCard
                  title="Formatting Suggestions"
                  icon={LayoutTemplate}
                  items={analysis.formattingSuggestions}
                />
                <ImprovementCard
                  title="Project Improvements"
                  icon={FolderKanban}
                  items={analysis.projectImprovements}
                />
                <ImprovementCard
                  title="Experience Improvements"
                  icon={Briefcase}
                  items={analysis.experienceImprovements}
                />
                <ImprovementCard title="Skill Suggestions" icon={Sparkles} items={analysis.skillSuggestions} />
              </div>
            </div>
          )}

          {activeTab === 'ats' && (
            <div className="space-y-5">
              <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
                <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-4">
                  ATS Score Breakdown
                </h2>
                <div className="space-y-4">
                  {Object.entries(analysis.atsBreakdown || {}).map(([criterion, score]) => (
                    <div key={criterion}>
                      <div className="flex items-center justify-between mb-1.5">
                        <span className="text-sm text-[var(--dash-text)]">{criterion}</span>
                        <span className="font-mono text-xs text-[var(--dash-muted)]">{score}</span>
                      </div>
                      <div className="h-1.5 w-full rounded-full bg-[var(--dash-border)] overflow-hidden">
                        <div
                          className="h-full rounded-full bg-signal-amber"
                          style={{ width: `${Math.min(100, (score / 15) * 100)}%` }}
                        />
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {activeTab === 'keywords' && (
            <div className="grid lg:grid-cols-2 gap-5">
              <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
                <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-2">
                  Keyword Coverage
                </h2>
                <KeywordChart
                  matched={keywordAnalysis?.matchedKeywords}
                  weak={keywordAnalysis?.weakKeywords}
                  missing={keywordAnalysis?.missingKeywords}
                />
                <p className="mt-3 text-center font-mono text-sm text-[var(--dash-text)]">
                  {Math.round(keywordAnalysis?.skillCoveragePercentage || 0)}% skill coverage
                </p>
              </div>
              <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
                <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-3">
                  All Keywords
                </h2>
                <KeywordTable keywords={keywordAnalysis?.keywords} />
              </div>
            </div>
          )}
        </>
      )}
    </div>
  )
}

export default ResumeAnalysisPage
