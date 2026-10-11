import { Code2, Users, MessagesSquare, Terminal, Network, Trophy } from 'lucide-react'

export const INTERVIEW_CARDS = [
  {
    style: 'TECHNICAL',
    title: 'Technical Interview',
    description: 'Core CS fundamentals, language internals, and hands-on problem solving.',
    duration: '25–35 min',
    difficulty: 'Medium',
    icon: Code2,
    gradient: 'from-signal-amber/20 via-signal-amber/5 to-transparent',
  },
  {
    style: 'HR',
    title: 'HR Interview',
    description: 'Culture fit, career goals, and expectation-setting conversations.',
    duration: '15–20 min',
    difficulty: 'Easy',
    icon: Users,
    gradient: 'from-signal-mint/20 via-signal-mint/5 to-transparent',
  },
  {
    style: 'BEHAVIORAL',
    title: 'Behavioral Interview',
    description: 'STAR-format questions about teamwork, conflict, and ownership.',
    duration: '20–25 min',
    difficulty: 'Medium',
    icon: MessagesSquare,
    gradient: 'from-blue-500/20 via-blue-500/5 to-transparent',
  },
  {
    style: 'CODING',
    title: 'Coding Interview',
    description: 'Live coding-style questions with reasoning about complexity and edge cases.',
    duration: '30–40 min',
    difficulty: 'Hard',
    icon: Terminal,
    gradient: 'from-purple-500/20 via-purple-500/5 to-transparent',
  },
  {
    style: 'SYSTEM_DESIGN',
    title: 'System Design',
    description: 'Architecture trade-offs, scalability, and high-level design thinking.',
    duration: '35–45 min',
    difficulty: 'Hard',
    icon: Network,
    gradient: 'from-pink-500/20 via-pink-500/5 to-transparent',
  },
  {
    style: 'MOCK_FINAL',
    title: 'Mock Final Round',
    description: 'A mixed, end-to-end simulation of a real final-round interview loop.',
    duration: '40–50 min',
    difficulty: 'Hard',
    icon: Trophy,
    gradient: 'from-signal-amber/20 via-signal-mint/10 to-transparent',
  },
]

export const ROLE_OPTIONS = [
  { value: 'JAVA_FULL_STACK', label: 'Java Full Stack' },
  { value: 'BACKEND', label: 'Backend' },
  { value: 'FRONTEND', label: 'Frontend' },
  { value: 'CLOUD_ENGINEER', label: 'Cloud Engineer' },
  { value: 'PYTHON_DEVELOPER', label: 'Python Developer' },
  { value: 'DATA_ANALYST', label: 'Data Analyst' },
  { value: 'SOFTWARE_ENGINEER', label: 'Software Engineer' },
]

export const DIFFICULTY_OPTIONS = [
  { value: 'EASY', label: 'Easy' },
  { value: 'MEDIUM', label: 'Medium' },
  { value: 'HARD', label: 'Hard' },
]

export const EXPERIENCE_OPTIONS = [
  { value: 'FRESHER', label: 'Fresher' },
  { value: 'ONE_YEAR', label: '1 Year' },
  { value: 'TWO_YEARS', label: '2 Years' },
]

export const QUESTION_COUNT_OPTIONS = [5, 10, 15]

export const STYLE_OPTIONS = [
  { value: 'TECHNICAL', label: 'Technical' },
  { value: 'HR', label: 'HR' },
  { value: 'MIXED', label: 'Mixed' },
  { value: 'BEHAVIORAL', label: 'Behavioral' },
]

export const STYLE_LABELS = {
  TECHNICAL: 'Technical',
  HR: 'HR',
  BEHAVIORAL: 'Behavioral',
  MIXED: 'Mixed',
  CODING: 'Coding',
  SYSTEM_DESIGN: 'System Design',
  MOCK_FINAL: 'Mock Final Round',
}

export const ROLE_LABELS = ROLE_OPTIONS.reduce((acc, option) => {
  acc[option.value] = option.label
  return acc
}, {})

export const DIFFICULTY_LABELS = {
  EASY: 'Easy',
  MEDIUM: 'Medium',
  HARD: 'Hard',
}
