import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { History, GitCompare, ThumbsUp, ThumbsDown, ArrowRight } from 'lucide-react'
import resumeApi from '../../api/resumeApi.js'
import ResumeUploader from '../../components/resume/ResumeUploader.jsx'
import ATSScoreCard from '../../components/resume/ATSScoreCard.jsx'
import KeywordChart from '../../components/resume/KeywordChart.jsx'
import AnalysisTimeline from '../../components/resume/AnalysisTimeline.jsx'
import ImprovementCard from '../../components/resume/ImprovementCard.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'

const SECTION_KEYS = ['skills', 'education', 'experience', 'projects', 'certifications', 'languages', 'achievements']

function ResumeDashboard() {
  const navigate = useNavigate()
  const [latest, setLatest] = useState(null)
  const [history, setHistory] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [hasResume, setHasResume] = useState(true)

  const loadData = async () => {
    setIsLoading(true)
    try {
      const [latestRes, historyRes] = await Promise.allSettled([
        resumeApi.getLatest(),
        resumeApi.getHistory(),
      ])

      if (latestRes.status === 'fulfilled') {
        setLatest(latestRes.value.data.data)
        setHasResume(true)
      } else {
        setHasResume(false)
      }

      if (historyRes.status === 'fulfilled') {
        setHistory(historyRes.value.data.data)
      }
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  const handleUploadComplete = (fullResponse) => {
    setLatest(fullResponse)
    setHasResume(true)
    loadData()
  }

  if (isLoading) {
    return (
      <div className="space-y-5">
        <LoadingSkeleton className="h-40 w-full" />
        <div className="grid sm:grid-cols-3 gap-4">
          <LoadingSkeleton className="h-28" />
          <LoadingSkeleton className="h-28" />
          <LoadingSkeleton className="h-28" />
        </div>
        <LoadingSkeleton className="h-64 w-full" />
      </div>
    )
  }

  const analysis = latest?.analysis
  const resume = latest?.resume
  const keywordAnalysis = latest?.keywordAnalysis

  const completedSections = resume
    ? SECTION_KEYS.filter((key) => resume[key] && resume[key].length > 0).length
    : 0
  const completenessPercentage = Math.round((completedSections / SECTION_KEYS.length) * 100)

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between flex-wrap gap-3">
        <div>
          <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">Resume Analyzer</h1>
          <p className="text-sm text-[var(--dash-muted)] mt-1">
            AI-powered ATS scoring, keyword matching, and resume feedback.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <button
            type="button"
            onClick={() => navigate('/dashboard/resume/history')}
            className="inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
          >
            <History size={15} />
            History
          </button>
          <button
            type="button"
            onClick={() => navigate('/dashboard/resume/compare')}
            className="inline-flex items-center gap-2 rounded-lg border border-[var(--dash-border)] px-4 py-2 text-sm font-medium text-[var(--dash-text)] hover:border-signal-amber/40 transition-colors"
          >
            <GitCompare size={15} />
            Compare
          </button>
        </div>
      </div>

      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-4">
          {hasResume ? 'Upload a New Version' : 'Upload Your Resume'}
        </h2>
        <ResumeUploader onComplete={handleUploadComplete} />
      </div>

      {!hasResume && (
        <p className="text-sm text-[var(--dash-muted)] text-center">
          Upload a resume above to see your ATS score, keyword match, and AI feedback.
        </p>
      )}

      {hasResume && analysis && (
        <>
          <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
            <ATSScoreCard label="ATS Score" score={analysis.atsScore} description="Applicant tracking system match" />
            <ATSScoreCard label="AI Score" score={analysis.aiScore} description="Gemini's overall quality rating" />
            <ATSScoreCard
              label="Keyword Match"
              score={analysis.keywordMatchPercentage}
              description="Coverage of key industry terms"
            />
            <ATSScoreCard
              label="Completeness"
              score={completenessPercentage}
              description={`${completedSections} of ${SECTION_KEYS.length} sections found`}
            />
          </div>

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
            </div>
            <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
              <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-2">Score Trend</h2>
              <AnalysisTimeline history={history} />
            </div>
          </div>

          <div className="grid lg:grid-cols-2 gap-5">
            <ImprovementCard
              title="Top Strengths"
              icon={ThumbsUp}
              tone="positive"
              items={analysis.strengths?.slice(0, 5)}
            />
            <ImprovementCard
              title="Top Weaknesses"
              icon={ThumbsDown}
              tone="negative"
              items={analysis.weaknesses?.slice(0, 5)}
            />
          </div>

          <button
            type="button"
            onClick={() => navigate(`/dashboard/resume/analysis/${resume.id}`)}
            className="inline-flex items-center gap-2 rounded-lg bg-signal-amber px-5 py-2.5 text-sm font-semibold text-ink hover:bg-signal-amber/90 transition-colors"
          >
            View Full Analysis
            <ArrowRight size={15} />
          </button>
        </>
      )}

      {hasResume && !analysis && (
        <p className="text-sm text-[var(--dash-muted)] text-center">
          Your resume was uploaded but hasn't been analyzed yet.
        </p>
      )}
    </div>
  )
}

export default ResumeDashboard
