import { useEffect, useState } from 'react'
import { BookOpen, Briefcase, FileText, Sparkles, CheckCircle2 } from 'lucide-react'
import interviewApi from '../../../api/interviewApi.js'
import LoadingSkeleton from '../../../components/common/LoadingSkeleton.jsx'

function StudyPlanPage() {
  const [report, setReport] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [errorMessage, setErrorMessage] = useState('')

  useEffect(() => {
    const loadLatestPlan = async () => {
      try {
        const { data } = await interviewApi.getHistory()

        const completed = (data.data || []).find(
          (item) => item.status === 'COMPLETED'
        )

        if (!completed) {
          setReport(null)
          return
        }

        const reportResponse = await interviewApi.getReport(completed.sessionId)
        setReport(reportResponse.data.data)
      } catch (error) {
        setErrorMessage(
          error.response?.data?.message || 'Could not load your study plan.'
        )
      } finally {
        setIsLoading(false)
      }
    }

    loadLatestPlan()
  }, [])

  if (isLoading) {
    return (
      <div className="space-y-5">
        <LoadingSkeleton className="h-10 w-72" />
        <LoadingSkeleton className="h-32 w-full" />
        <LoadingSkeleton className="h-96 w-full" />
      </div>
    )
  }

  if (errorMessage) {
    return (
      <div className="rounded-lg border border-red-500/30 bg-red-500/10 px-4 py-3 text-sm text-red-400">
        {errorMessage}
      </div>
    )
  }

  if (!report?.improvementPlan) {
    return (
      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] py-20 px-6 text-center">
        <Sparkles
          size={32}
          className="mx-auto text-signal-amber mb-4"
        />

        <h1 className="font-display text-xl font-semibold text-[var(--dash-text)]">
          AI Study Plan
        </h1>

        <p className="mt-2 text-sm text-[var(--dash-muted)]">
          Complete an interview to generate your personalized 7-day improvement plan.
        </p>
      </div>
    )
  }

  const plan = report.improvementPlan

  const topicCount = plan.recommendedTopics?.length || 0
  const projectCount = plan.projectsToRevise?.length || 0
  const resumeCount = plan.resumeImprovements?.length || 0

  return (
    <div className="space-y-6">

      {/* Header */}
      <div>
        <h1 className="font-display text-2xl font-semibold text-[var(--dash-text)]">
          AI Study Plan
        </h1>

        <p className="mt-1 text-sm text-[var(--dash-muted)]">
          Your personalized 7-day interview improvement plan
        </p>
      </div>

      {/* Latest Interview */}
      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
        <div className="flex items-center justify-between">
          <div>
            <p className="text-xs uppercase tracking-wide text-[var(--dash-muted)]">
              Latest Interview
            </p>

            <h2 className="mt-1 text-base font-semibold text-[var(--dash-text)]">
              Performance
            </h2>
          </div>

          <div className="text-right">
            <p className="font-mono text-3xl font-semibold text-signal-amber">
              {Math.round(report.overallScore || 0)}
              <span className="text-sm text-[var(--dash-muted)]"> / 100</span>
            </p>
          </div>
        </div>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <BookOpen size={20} className="text-signal-amber mb-3" />

          <p className="font-mono text-2xl font-semibold text-[var(--dash-text)]">
            {topicCount}
          </p>

          <p className="text-xs text-[var(--dash-muted)]">
            Topics
          </p>
        </div>

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <Briefcase size={20} className="text-signal-amber mb-3" />

          <p className="font-mono text-2xl font-semibold text-[var(--dash-text)]">
            {projectCount}
          </p>

          <p className="text-xs text-[var(--dash-muted)]">
            Projects
          </p>
        </div>

        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-5">
          <FileText size={20} className="text-signal-amber mb-3" />

          <p className="font-mono text-2xl font-semibold text-[var(--dash-text)]">
            {resumeCount}
          </p>

          <p className="text-xs text-[var(--dash-muted)]">
            Resume Tips
          </p>
        </div>

      </div>

      {/* 7 Day Plan */}
      <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">

        <div className="flex items-center gap-2 mb-2">
          <Sparkles size={18} className="text-signal-amber" />

          <h2 className="font-display text-lg font-semibold text-[var(--dash-text)]">
            7-Day Improvement Plan
          </h2>
        </div>

        <p className="text-sm text-[var(--dash-muted)] mb-6">
          AI has analyzed your latest interview and identified the areas
          that need the most attention. Follow the daily plan below to
          improve your interview performance.
        </p>

        <div className="space-y-4">

          {plan.dailyPlan?.map((day) => (
            <div
              key={day.day}
              className="rounded-xl border border-[var(--dash-border)] p-5"
            >

              <div className="flex gap-4">

                <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-signal-amber/10 border border-signal-amber/30">
                  <span className="font-mono text-sm font-semibold text-signal-amber">
                    {day.day}
                  </span>
                </div>

                <div className="flex-1">

                  <p className="font-mono text-xs text-signal-amber">
                    DAY {day.day}
                  </p>

                  <h3 className="mt-1 text-base font-semibold text-[var(--dash-text)]">
                    {day.focusArea}
                  </h3>

                  <div className="mt-3 space-y-2">

                    {day.tasks?.map((task, index) => (
                      <div
                        key={index}
                        className="flex gap-2 text-sm text-[var(--dash-muted)]"
                      >
                        <CheckCircle2
                          size={15}
                          className="mt-0.5 shrink-0 text-signal-amber"
                        />

                        <span>{task}</span>
                      </div>
                    ))}

                  </div>

                </div>

              </div>

            </div>
          ))}

        </div>
      </div>

      {/* Recommended Topics */}
      {plan.recommendedTopics?.length > 0 && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">

          <h2 className="font-display text-lg font-semibold text-[var(--dash-text)] mb-4">
            Recommended Topics
          </h2>

          <div className="grid sm:grid-cols-2 gap-3">

            {plan.recommendedTopics.map((topic, index) => (
              <div
                key={index}
                className="rounded-xl border border-[var(--dash-border)] p-4 text-sm text-[var(--dash-text)]"
              >
                • {topic}
              </div>
            ))}

          </div>

        </div>
      )}

      {/* Projects */}
      {plan.projectsToRevise?.length > 0 && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">

          <h2 className="font-display text-lg font-semibold text-[var(--dash-text)] mb-4">
            Projects to Revise
          </h2>

          <div className="space-y-2">

            {plan.projectsToRevise.map((project, index) => (
              <div
                key={index}
                className="flex gap-2 text-sm text-[var(--dash-text)]"
              >
                <Briefcase
                  size={15}
                  className="mt-0.5 text-signal-amber shrink-0"
                />

                {project}
              </div>
            ))}

          </div>

        </div>
      )}

      {/* Resume */}
      {plan.resumeImprovements?.length > 0 && (
        <div className="rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-6">

          <h2 className="font-display text-lg font-semibold text-[var(--dash-text)] mb-4">
            Resume Improvements
          </h2>

          <div className="space-y-2">

            {plan.resumeImprovements.map((item, index) => (
              <div
                key={index}
                className="flex gap-2 text-sm text-[var(--dash-text)]"
              >
                <FileText
                  size={15}
                  className="mt-0.5 text-signal-amber shrink-0"
                />

                {item}
              </div>
            ))}

          </div>

        </div>
      )}

    </div>
  )
}

export default StudyPlanPage