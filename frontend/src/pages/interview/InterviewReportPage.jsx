import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { RadialBar, RadialBarChart, PolarAngleAxis } from 'recharts'
import {
  ArrowLeft,
  Award,
  Brain,
  Clock,
  MessageCircle,
  Puzzle,
  Sparkles,
  ThumbsUp,
  Trophy,
} from 'lucide-react'
import interviewApi from '../../api/interviewApi.js'
import Confetti from '../../components/interview/Confetti.jsx'
import LoadingSkeleton from '../../components/common/LoadingSkeleton.jsx'
import { DIFFICULTY_LABELS, ROLE_LABELS, STYLE_LABELS } from '../../utils/interviewConstants.js'

function getScoreColor(score) {
  if (score >= 75) return '#4ADE80'
  if (score >= 50) return '#FFB020'
  return '#F87171'
}

function DimensionBar({ icon: Icon, label, value }) {
  return (
    <div>
      <div className="flex items-center justify-between mb-1.5">
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

function InterviewReportPage() {
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
        const { data } = await interviewApi.getReport(sessionId)
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
  const showConfetti = overallScore > 90

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {showConfetti && <Confetti />}

      <button
        type="button"
        onClick={() => navigate('/dashboard/interviews')}
        className="inline-flex items-center gap-2 text-sm text-[var(--dash-muted)] hover:text-signal-amber transition-colors"
      >
        <ArrowLeft size={15} />
        Back to Interviews
      </button>

      <div>
        <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">
          {ROLE_LABELS[report.role] || report.role} · {STYLE_LABELS[report.style] || report.style} Interview
        </h1>
        <p className="text-sm text-[var(--dash-muted)] mt-1">
          {DIFFICULTY_LABELS[report.difficulty] || report.difficulty} difficulty ·{' '}
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

        <div className="lg:col-span-2 rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6 space-y-4">
          <DimensionBar icon={Brain} label="Technical Knowledge" value={report.technicalKnowledge} />
          <DimensionBar icon={MessageCircle} label="Communication" value={report.communication} />
          <DimensionBar icon={ThumbsUp} label="Confidence" value={report.confidence} />
          <DimensionBar icon={Puzzle} label="Problem Solving" value={report.problemSolving} />
          <DimensionBar icon={Clock} label="Time Management" value={report.timeManagement} />
        </div>
      </div>

      {report.badges?.length > 0 && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <div className="flex items-center gap-2 mb-3">
            <Trophy size={16} className="text-signal-amber" />
            <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">Badges Unlocked</h2>
          </div>
          <div className="flex flex-wrap gap-2">
            {report.badges.map((badge) => (
              <span
                key={badge}
                className="inline-flex items-center gap-1.5 rounded-full border border-signal-amber/30 bg-signal-amber/10 px-3 py-1.5 text-xs font-medium text-signal-amber"
              >
                <Award size={12} />
                {badge}
              </span>
            ))}
          </div>
        </div>
      )}

      {report.summary && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-2">Overall Summary</h2>
          <p className="text-sm text-[var(--dash-text)] leading-relaxed">{report.summary}</p>
        </div>
      )}

      <div>
        <h2 className="font-display text-base font-semibold text-[var(--dash-text)] mb-3">
          Question-wise Feedback
        </h2>
        <div className="space-y-3">
          {report.questionFeedback?.map((item) => (
            <div key={item.questionNumber} className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-mono text-[var(--dash-muted)]">Q{item.questionNumber}</span>
                <span className="font-mono text-sm text-signal-amber">{Math.round(item.score)}</span>
              </div>
              <p className="text-sm font-medium text-[var(--dash-text)]">{item.questionText}</p>

              {item.skipped ? (
                <p className="mt-2 text-xs text-[var(--dash-muted)] italic">Skipped</p>
              ) : (
                item.answerText && (
                  <p className="mt-2 text-sm text-[var(--dash-muted)] leading-relaxed">{item.answerText}</p>
                )
              )}

              {item.mistakes?.length > 0 && (
                <div className="mt-3">
                  <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-1">
                    Improvement Tips
                  </p>
                  <ul className="space-y-1">
                    {item.mistakes.map((mistake, index) => (
                      <li key={index} className="text-xs text-[var(--dash-text)] flex gap-1.5">
                        <span className="text-red-400">•</span>
                        {mistake}
                      </li>
                    ))}
                  </ul>
                </div>
              )}

              {item.idealAnswer && (
                <div className="mt-3 rounded-lg bg-[var(--dash-bg)] p-3">
                  <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-1">
                    Ideal Answer
                  </p>
                  <p className="text-xs text-[var(--dash-text)] leading-relaxed">{item.idealAnswer}</p>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {report.improvementPlan && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">
          <div className="flex items-center gap-2 mb-4">
            <Sparkles size={16} className="text-signal-amber" />
            <h2 className="font-display text-base font-semibold text-[var(--dash-text)]">
              AI Coach · 7-Day Improvement Plan
            </h2>
          </div>

          <div className="grid sm:grid-cols-2 gap-3">
            {report.improvementPlan.dailyPlan?.map((day) => (
              <div key={day.day} className="rounded-xl border border-[var(--dash-border)] p-4">
                <p className="font-mono text-xs text-signal-amber">DAY {day.day}</p>
                <p className="text-sm font-medium text-[var(--dash-text)] mt-1">{day.focusArea}</p>
                <ul className="mt-2 space-y-1">
                  {day.tasks?.map((task, index) => (
                    <li key={index} className="text-xs text-[var(--dash-muted)] flex gap-1.5">
                      <span className="text-signal-amber">•</span>
                      {task}
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>

          <div className="mt-5 grid sm:grid-cols-3 gap-4">
            {report.improvementPlan.recommendedTopics?.length > 0 && (
              <div>
                <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-2">
                  Recommended Topics
                </p>
                <ul className="space-y-1">
                  {report.improvementPlan.recommendedTopics.map((topic, index) => (
                    <li key={index} className="text-xs text-[var(--dash-text)]">
                      • {topic}
                    </li>
                  ))}
                </ul>
              </div>
            )}
            {report.improvementPlan.projectsToRevise?.length > 0 && (
              <div>
                <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-2">
                  Projects to Revise
                </p>
                <ul className="space-y-1">
                  {report.improvementPlan.projectsToRevise.map((project, index) => (
                    <li key={index} className="text-xs text-[var(--dash-text)]">
                      • {project}
                    </li>
                  ))}
                </ul>
              </div>
            )}
            {report.improvementPlan.resumeImprovements?.length > 0 && (
              <div>
                <p className="text-xs font-mono tracking-wide text-[var(--dash-muted)] uppercase mb-2">
                  Resume Improvements
                </p>
                <ul className="space-y-1">
                  {report.improvementPlan.resumeImprovements.map((item, index) => (
                    <li key={index} className="text-xs text-[var(--dash-text)]">
                      • {item}
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

export default InterviewReportPage
