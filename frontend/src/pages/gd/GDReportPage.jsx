import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { RadialBar, RadialBarChart, PolarAngleAxis } from 'recharts'
import {
  ArrowLeft,
  MessageCircle,
  ThumbsUp,
  Mic,
  SpellCheck,
  Crown,
  Users,
  Brain,
  Target,
  BookOpen,
  Lightbulb,
} from 'lucide-react'
import gdApi from '../../api/gdApi.js'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import { CATEGORY_LABELS } from '../../utils/gdConstants.js'

function getScoreColor(score) {
  if (score >= 75) return '#4ADE80'
  if (score >= 50) return '#FFB020'
  return '#F87171'
}

function DimensionBar({ icon: Icon, label, value }) {
  return (
    <div>
      <div className="flex items-center justify-between mb-1">
        <span className="flex items-center gap-2 text-sm text-[var(--dash-text)]">
          <Icon size={14} className="text-signal-amber" />
          {label}
        </span>
        <span className="font-mono text-xs text-[var(--dash-muted)]">{Math.round(value)}</span>
      </div>
      <div className="h-1.5 w-full rounded-full bg-[var(--dash-border)] overflow-hidden">
        <div
          className="h-full rounded-full bg-signal-amber transition-all duration-700"
          style={{ width: `${Math.min(100, value)}%` }}
        />
      </div>
    </div>
  )
}

function formatDuration(seconds) {
  if (!seconds) return '—'
  const minutes = Math.floor(seconds / 60)
  const remaining = seconds % 60
  return `${minutes}m ${remaining}s`
}

function GDReportPage() {
  const { sessionId } = useParams()
  const navigate = useNavigate()
  const [report, setReport] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [errorMessage, setErrorMessage] = useState('')

  useEffect(() => {
    const fetchReport = async () => {
      setIsLoading(true)
      setErrorMessage('')
      try {
        const { data } = await gdApi.getReport(sessionId)
        setReport(data.data)
      } catch (error) {
        setErrorMessage(error.response?.data?.message || 'Could not load this report.')
      } finally {
        setIsLoading(false)
      }
    }
    fetchReport()
  }, [sessionId])

  if (isLoading) {
    return (
      <div className="space-y-5 max-w-4xl mx-auto">
        <LoadingSkeleton className="h-10 w-64" />
        <LoadingSkeleton className="h-64 w-full" />
      </div>
    )
  }

  if (errorMessage || !report) {
    return (
      <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400 max-w-4xl mx-auto">
        {errorMessage || 'Report not found.'}
      </div>
    )
  }

  const overallScore = Math.round(report.overallScore)

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <button
        type="button"
        onClick={() => navigate('/dashboard/group-discussion')}
        className="inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
      >
        <ArrowLeft size={15} />
        Back to Group Discussion
      </button>

      <div>
        <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">{report.topic}</h1>
        <p className="text-sm text-[var(--dash-muted)] mt-1">
          {CATEGORY_LABELS[report.category] || report.category} · {report.difficulty} ·{' '}
          {formatDuration(report.durationSeconds)}
        </p>
      </div>

      <div className="grid lg:grid-cols-3 gap-5">
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 flex flex-col items-center justify-center">
          <div className="relative h-36 w-36">
            <RadialBarChart
              width={144}
              height={144}
              cx={72}
              cy={72}
              innerRadius={54}
              outerRadius={70}
              barSize={12}
              data={[{ value: overallScore, fill: getScoreColor(overallScore) }]}
              startAngle={90}
              endAngle={-270}
            >
              <PolarAngleAxis type="number" domain={[0, 100]} angleAxisId={0} tick={false} />
              <RadialBar background={{ fill: 'var(--dash-border)' }} dataKey="value" cornerRadius={10} />
            </RadialBarChart>
            <div className="absolute inset-0 flex flex-col items-center justify-center">
              <span className="font-mono text-3xl font-semibold text-[var(--dash-text)]">{overallScore}</span>
              <span className="text-xs text-[var(--dash-muted)]">Overall Score</span>
            </div>
          </div>
        </div>

        <div className="lg:col-span-2 rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 grid sm:grid-cols-2 gap-4">
          <DimensionBar icon={MessageCircle} label="Communication" value={report.communicationScore} />
          <DimensionBar icon={ThumbsUp} label="Confidence" value={report.confidenceScore} />
          <DimensionBar icon={Mic} label="Fluency" value={report.fluencyScore} />
          <DimensionBar icon={SpellCheck} label="Grammar" value={report.grammarScore} />
          <DimensionBar icon={Crown} label="Leadership" value={report.leadershipScore} />
          <DimensionBar icon={Users} label="Teamwork" value={report.teamworkScore} />
          <DimensionBar icon={Brain} label="Critical Thinking" value={report.criticalThinkingScore} />
          <DimensionBar icon={Target} label="Relevance" value={report.relevanceScore} />
          <DimensionBar icon={BookOpen} label="Vocabulary" value={report.vocabularyScore} />
        </div>
      </div>

      {report.summary && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-2">Overall Summary</h2>
          <p className="text-sm text-[var(--dash-text)] leading-relaxed">{report.summary}</p>
        </div>
      )}

      <div className="grid lg:grid-cols-2 gap-5">
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-2">Strengths</p>
          <ul className="space-y-1.5">
            {report.strengths?.map((item, index) => (
              <li key={index} className="text-sm text-[var(--dash-text)] flex gap-2">
                <span className="text-signal-mint">•</span>
                {item}
              </li>
            ))}
          </ul>
        </div>
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-2">Weaknesses</p>
          <ul className="space-y-1.5">
            {report.weaknesses?.map((item, index) => (
              <li key={index} className="text-sm text-[var(--dash-text)] flex gap-2">
                <span className="text-red-400">•</span>
                {item}
              </li>
            ))}
          </ul>
        </div>
      </div>

      {report.improvementTips?.length > 0 && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <div className="flex items-center gap-2 mb-2">
            <Lightbulb size={14} className="text-signal-amber" />
            <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase">Improvement Tips</p>
          </div>
          <ul className="space-y-1.5">
            {report.improvementTips.map((item, index) => (
              <li key={index} className="text-sm text-[var(--dash-text)] flex gap-2">
                <span className="text-signal-amber">•</span>
                {item}
              </li>
            ))}
          </ul>
        </div>
      )}

      {report.suggestedResponses?.length > 0 && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-2">
            Suggested Better Responses
          </p>
          <ul className="space-y-2">
            {report.suggestedResponses.map((item, index) => (
              <li key={index} className="rounded-lg bg-[var(--dash-bg)] p-3 text-sm text-[var(--dash-text)]">
                {item}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  )
}

export default GDReportPage
