
import { useEffect, useState } from 'react'
import {
  Map,
  CheckCircle2,
  Circle,
  Clock3,
  Sparkles,
  ArrowRight,
  BookOpen,
  Target,
  RotateCcw,
} from 'lucide-react'

const roles = [
  'Java Full Stack Developer',
  'Backend Developer',
  'Frontend Developer',
  'Software Engineer',
  'Cloud Engineer',
]

const roadmapData = {
  'Java Full Stack Developer': [
    {
      title: 'Java Fundamentals',
      description: 'Build a strong foundation in Java programming.',
      topics: ['OOP', 'Collections', 'Exception Handling', 'Multithreading'],
    },
    {
      title: 'Frontend Development',
      description: 'Build responsive and interactive user interfaces.',
      topics: ['HTML', 'CSS', 'JavaScript', 'React.js'],
    },
    {
      title: 'Backend Development',
      description: 'Build backend applications using Java and Spring Boot.',
      topics: ['Spring Boot', 'REST APIs', 'Spring Security', 'JWT'],
    },
    {
      title: 'Database Management',
      description: 'Store and retrieve application data efficiently.',
      topics: ['MySQL', 'SQL Queries', 'Joins', 'JPA & Hibernate'],
    },
    {
      title: 'Full Stack Projects',
      description: 'Connect frontend, backend and database components.',
      topics: ['API Integration', 'Authentication', 'Git & GitHub', 'Deployment'],
    },
    {
      title: 'Placement Preparation',
      description: 'Prepare for coding rounds and technical interviews.',
      topics: ['DSA', 'Java Interview Questions', 'HR Interview', 'Mock Interviews'],
    },
  ],

  'Backend Developer': [
    {
      title: 'Java Programming',
      description: 'Master core Java and object-oriented programming.',
      topics: ['OOP', 'Collections', 'Exception Handling', 'Streams'],
    },
    {
      title: 'Advanced Java',
      description: 'Understand Java features used in backend systems.',
      topics: ['Multithreading', 'JDBC', 'Generics', 'JVM Basics'],
    },
    {
      title: 'Spring Boot & APIs',
      description: 'Create maintainable backend services.',
      topics: ['Spring Boot', 'REST APIs', 'Dependency Injection', 'Validation'],
    },
    {
      title: 'Database & Security',
      description: 'Manage data and protect backend endpoints.',
      topics: ['MySQL', 'SQL Joins', 'JPA & Hibernate', 'Spring Security'],
    },
    {
      title: 'Backend Projects',
      description: 'Build and test production-style backend applications.',
      topics: ['JWT', 'Exception Handling', 'JUnit', 'Postman'],
    },
    {
      title: 'Technical Preparation',
      description: 'Practise common backend interview topics.',
      topics: ['DSA', 'Java Questions', 'API Design', 'Mock Interviews'],
    },
  ],

  'Frontend Developer': [
    {
      title: 'Web Fundamentals',
      description: 'Learn the building blocks of web interfaces.',
      topics: ['HTML', 'CSS', 'JavaScript', 'Responsive Design'],
    },
    {
      title: 'JavaScript',
      description: 'Understand modern JavaScript and asynchronous code.',
      topics: ['ES6+', 'Promises', 'Async/Await', 'Fetch API'],
    },
    {
      title: 'React Development',
      description: 'Build reusable components and interactive applications.',
      topics: ['Components', 'Props & State', 'Hooks', 'React Router'],
    },
    {
      title: 'UI & API Integration',
      description: 'Connect interfaces to backend services.',
      topics: ['Tailwind CSS', 'REST APIs', 'Forms', 'Error Handling'],
    },
    {
      title: 'Frontend Projects',
      description: 'Build and deploy responsive web applications.',
      topics: ['Portfolio', 'Git & GitHub', 'Accessibility', 'Deployment'],
    },
    {
      title: 'Interview Preparation',
      description: 'Prepare for frontend coding and technical rounds.',
      topics: ['JavaScript Questions', 'React Questions', 'UI Challenges', 'Mock Interviews'],
    },
  ],

  'Software Engineer': [
    {
      title: 'Programming Fundamentals',
      description: 'Develop strong problem-solving and coding skills.',
      topics: ['Java', 'OOP', 'Data Types', 'Exception Handling'],
    },
    {
      title: 'Data Structures & Algorithms',
      description: 'Practise common coding assessment patterns.',
      topics: ['Arrays', 'Strings', 'Linked Lists', 'Trees & Graphs'],
    },
    {
      title: 'Database Concepts',
      description: 'Understand relational databases and data modelling.',
      topics: ['SQL', 'Joins', 'Indexes', 'Normalization'],
    },
    {
      title: 'Software Development',
      description: 'Learn to build maintainable applications.',
      topics: ['REST APIs', 'Git', 'Testing', 'Debugging'],
    },
    {
      title: 'Projects & Design',
      description: 'Apply your skills to practical software projects.',
      topics: ['Problem Solving', 'Design Patterns', 'Clean Code', 'System Design Basics'],
    },
    {
      title: 'Placement Preparation',
      description: 'Prepare for software engineering recruitment.',
      topics: ['Coding Practice', 'Core CS', 'Technical Interviews', 'HR Interviews'],
    },
  ],

  'Cloud Engineer': [
    {
      title: 'Linux & Networking',
      description: 'Learn the fundamentals of operating systems and networks.',
      topics: ['Linux Commands', 'TCP/IP', 'DNS', 'HTTP/HTTPS'],
    },
    {
      title: 'Cloud Fundamentals',
      description: 'Understand cloud infrastructure and service models.',
      topics: ['IaaS & PaaS', 'AWS Basics', 'Azure Basics', 'Regions & Availability Zones'],
    },
    {
      title: 'Cloud Identity & Security',
      description: 'Learn access control and cloud security essentials.',
      topics: ['IAM', 'Roles & Policies', 'Security Groups', 'Encryption Basics'],
    },
    {
      title: 'Deployment & Automation',
      description: 'Deploy applications and automate common tasks.',
      topics: ['EC2', 'Docker', 'CI/CD', 'Shell Scripting'],
    },
    {
      title: 'Monitoring & Projects',
      description: 'Practise deployment, monitoring and troubleshooting.',
      topics: ['CloudWatch', 'Logging', 'S3', 'Cloud Deployment Project'],
    },
    {
      title: 'Cloud Interview Preparation',
      description: 'Prepare for cloud support and engineering interviews.',
      topics: ['Cloud Questions', 'Networking', 'Troubleshooting', 'Mock Interviews'],
    },
  ],
}

const createRoadmap = (role) =>
  roadmapData[role].map((step, index) => ({
    ...step,
    status:
      role === 'Java Full Stack Developer' && index < 2
        ? 'completed'
        : role === 'Java Full Stack Developer' && index === 2
          ? 'in-progress'
          : 'upcoming',
  }))

const validStatuses = ['completed', 'in-progress', 'upcoming']

function loadSavedProgress(role) {
  try {
    const saved = localStorage.getItem(`careerRoadmapProgress:${role}`)

    if (saved) {
      const parsed = JSON.parse(saved)
      const template = roadmapData[role]

      const valid =
        Array.isArray(parsed) &&
        parsed.length === template.length &&
        parsed.every(
          (step, index) =>
            step.title === template[index].title &&
            validStatuses.includes(step.status)
        )

      if (valid) return parsed
    }
  } catch {
    // Use the default roadmap when saved data is unavailable.
  }

  return createRoadmap(role)
}

function CareerRoadmapPage() {
  const [selectedRole, setSelectedRole] = useState(() => {
    try {
      const savedRole = localStorage.getItem('careerRoadmapRole')
      return roles.includes(savedRole) ? savedRole : roles[0]
    } catch {
      return roles[0]
    }
  })

  const [roadmaps, setRoadmaps] = useState(() => {
    const result = {}

    roles.forEach((role) => {
      result[role] = loadSavedProgress(role)
    })

    return result
  })

  const roadmap = roadmaps[selectedRole]
  const completedCount = roadmap.filter(
    (step) => step.status === 'completed'
  ).length

  const progress = Math.round(
    (completedCount / roadmap.length) * 100
  )

  useEffect(() => {
    try {
      localStorage.setItem('careerRoadmapRole', selectedRole)
    } catch {
      // The page remains usable without browser storage.
    }
  }, [selectedRole])

  useEffect(() => {
    try {
      roles.forEach((role) => {
        localStorage.setItem(
          `careerRoadmapProgress:${role}`,
          JSON.stringify(roadmaps[role])
        )
      })
    } catch {
      // Progress remains available in the current page session.
    }
  }, [roadmaps])

  const updateStage = (index) => {
    setRoadmaps((current) => ({
      ...current,
      [selectedRole]: current[selectedRole].map((step, i) => {
        if (i !== index) return step

        const nextStatus =
          step.status === 'upcoming'
            ? 'in-progress'
            : step.status === 'in-progress'
              ? 'completed'
              : 'upcoming'

        return { ...step, status: nextStatus }
      }),
    }))
  }

  const resetProgress = () => {
    setRoadmaps((current) => ({
      ...current,
      [selectedRole]: createRoadmap(selectedRole),
    }))
  }

  const getStatus = (status) => {
    if (status === 'completed') {
      return {
        label: 'Completed',
        color: 'text-green-400',
        bg: 'bg-green-400/10',
      }
    }

    if (status === 'in-progress') {
      return {
        label: 'In Progress',
        color: 'text-amber-400',
        bg: 'bg-amber-400/10',
      }
    }

    return {
      label: 'Upcoming',
      color: 'text-gray-400',
      bg: 'bg-white/5',
    }
  }

  return (
    <div className="min-h-screen bg-[#07111f] p-6 text-white">
      <div className="mx-auto max-w-6xl">
        <div className="mb-8 flex items-center gap-3">
          <div className="rounded-xl bg-amber-500/10 p-3">
            <Map className="h-7 w-7 text-amber-400" />
          </div>
          <div>
            <h1 className="text-3xl font-bold">Career Roadmap</h1>
            <p className="mt-1 text-gray-400">
              Follow a structured path to your target software role.
            </p>
          </div>
        </div>

        <div className="mb-8 grid gap-6 lg:grid-cols-3">
          <div className="rounded-2xl border border-white/10 bg-[#0d1a2b] p-6 lg:col-span-2">
            <div className="mb-4 flex items-center gap-2">
              <Target className="h-5 w-5 text-amber-400" />
              <h2 className="text-xl font-semibold">Target Role</h2>
            </div>

            <div className="flex flex-wrap gap-3">
              {roles.map((role) => (
                <button
                  key={role}
                  type="button"
                  onClick={() => setSelectedRole(role)}
                  className={`rounded-lg px-4 py-2 text-sm font-medium transition ${
                    selectedRole === role
                      ? 'bg-amber-500 text-black'
                      : 'bg-white/5 text-gray-300 hover:bg-white/10'
                  }`}
                >
                  {role}
                </button>
              ))}
            </div>

            <p className="mt-5 flex flex-wrap items-center gap-2 text-sm text-gray-400">
              <Sparkles className="h-4 w-4 text-amber-400" />
              Preparing for:
              <span className="font-semibold text-amber-400">
                {selectedRole}
              </span>
            </p>
          </div>

          <div className="rounded-2xl border border-white/10 bg-[#0d1a2b] p-6">
            <div className="mb-4 flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-400">Roadmap Progress</p>
                <p className="mt-1 text-3xl font-bold">{progress}%</p>
              </div>
              <div className="rounded-xl bg-amber-500/10 p-3">
                <CheckCircle2 className="h-6 w-6 text-amber-400" />
              </div>
            </div>

            <div className="h-2 overflow-hidden rounded-full bg-white/10">
              <div
                className="h-full rounded-full bg-amber-400 transition-all duration-500"
                style={{ width: `${progress}%` }}
              />
            </div>

            <p className="mt-3 text-xs text-gray-500">
              {completedCount} of {roadmap.length} stages completed
            </p>
          </div>
        </div>

        <div className="mb-5 flex flex-wrap items-center justify-between gap-3">
          <div>
            <h2 className="text-2xl font-bold">Your Learning Path</h2>
            <p className="mt-1 text-sm text-gray-400">
              Update each stage as you learn.
            </p>
          </div>

          <button
            type="button"
            onClick={resetProgress}
            className="flex items-center gap-2 rounded-lg border border-white/10 px-4 py-2 text-sm text-gray-300 transition hover:bg-white/5"
          >
            <RotateCcw className="h-4 w-4" />
            Reset This Role
          </button>
        </div>

        <div className="space-y-5">
          {roadmap.map((step, index) => {
            const status = getStatus(step.status)

            return (
              <div
                key={step.title}
                className="flex gap-4 rounded-2xl border border-white/10 bg-[#0d1a2b] p-5 transition hover:border-white/20"
              >
                <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-[#07111f]">
                  {step.status === 'completed' ? (
                    <CheckCircle2 className="h-8 w-8 text-green-400" />
                  ) : step.status === 'in-progress' ? (
                    <Clock3 className="h-8 w-8 text-amber-400" />
                  ) : (
                    <Circle className="h-8 w-8 text-gray-500" />
                  )}
                </div>

                <div className="min-w-0 flex-1">
                  <div className="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                    <div>
                      <div className="flex flex-wrap items-center gap-2">
                        <span className="text-sm text-gray-500">
                          STEP {index + 1}
                        </span>
                        <span
                          className={`rounded-full px-2.5 py-1 text-xs font-medium ${status.bg} ${status.color}`}
                        >
                          {status.label}
                        </span>
                      </div>

                      <h3 className="mt-2 text-lg font-semibold">
                        {step.title}
                      </h3>
                      <p className="mt-1 text-sm text-gray-400">
                        {step.description}
                      </p>
                    </div>

                    <button
                      type="button"
                      onClick={() => updateStage(index)}
                      className={`flex shrink-0 items-center justify-center gap-2 rounded-lg px-4 py-2 text-sm font-medium transition ${
                        step.status === 'completed'
                          ? 'bg-white/5 text-gray-300 hover:bg-white/10'
                          : 'bg-amber-500 text-black hover:bg-amber-400'
                      }`}
                    >
                      {step.status === 'upcoming' ? (
                        <>
                          <BookOpen className="h-4 w-4" />
                          Start Learning
                          <ArrowRight className="h-4 w-4" />
                        </>
                      ) : step.status === 'in-progress' ? (
                        <>
                          <CheckCircle2 className="h-4 w-4" />
                          Mark Completed
                        </>
                      ) : (
                        <>
                          <RotateCcw className="h-4 w-4" />
                          Reopen Stage
                        </>
                      )}
                    </button>
                  </div>

                  <div className="mt-4 flex flex-wrap gap-2">
                    {step.topics.map((topic) => (
                      <a
                        key={topic}
                        href={`https://www.google.com/search?q=${encodeURIComponent(
                          `${topic} tutorial for beginners`
                        )}`}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="inline-flex items-center gap-1 rounded-md border border-white/5 bg-white/5 px-3 py-2 text-xs text-gray-300 transition hover:border-amber-400/30 hover:bg-amber-400/10 hover:text-amber-300"
                        title={`Learn ${topic}`}
                      >
                        {topic}
                        <ArrowRight className="h-3 w-3" />
                      </a>
                    ))}
                  </div>
                </div>
              </div>
            )
          })}
        </div>

        <div className="mt-8 rounded-2xl border border-amber-400/10 bg-amber-400/5 p-5">
          <div className="flex gap-3">
            <Sparkles className="mt-0.5 h-5 w-5 shrink-0 text-amber-400" />
            <div>
              <h3 className="font-semibold text-amber-300">PrepWise Tip</h3>
              <p className="mt-1 text-sm leading-6 text-gray-400">
                Focus on one stage at a time. Build projects while learning
                and use the Interview and Coding sections to test your
                preparation.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CareerRoadmapPage