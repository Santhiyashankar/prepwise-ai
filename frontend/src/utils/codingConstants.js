import { Code2, Building2, CalendarClock, Timer, Sparkles, History } from 'lucide-react'

export const CODING_DASHBOARD_CARDS = [
  {
    id: 'dsa-practice',
    title: 'DSA Practice',
    description: 'Browse the full problem set filtered by topic and difficulty.',
    icon: Code2,
    gradient: 'from-signal-amber/20 via-signal-amber/5 to-transparent',
    to: '/dashboard/coding/problems',
  },
  {
    id: 'company-wise',
    title: 'Company-wise Questions',
    description: 'Practice questions tagged to real hiring companies.',
    icon: Building2,
    gradient: 'from-blue-500/20 via-blue-500/5 to-transparent',
    to: '/dashboard/coding/problems?company=AMAZON',
  },
  {
    id: 'daily-challenge',
    title: 'Daily Coding Challenge',
    description: "Today's featured problem, picked fresh every day.",
    icon: CalendarClock,
    gradient: 'from-signal-mint/20 via-signal-mint/5 to-transparent',
    to: '/dashboard/coding/daily',
  },
  {
    id: 'timed-assessment',
    title: 'Timed Assessment',
    description: 'A timed set of problems to simulate real test conditions.',
    icon: Timer,
    gradient: 'from-purple-500/20 via-purple-500/5 to-transparent',
    to: '/dashboard/coding/timed-assessment',
  },
  {
    id: 'ai-code-review',
    title: 'AI Code Review',
    description: 'Revisit past submissions and their Gemini-powered reviews.',
    icon: Sparkles,
    gradient: 'from-pink-500/20 via-pink-500/5 to-transparent',
    to: '/dashboard/coding/history',
  },
  {
    id: 'previous-attempts',
    title: 'Previous Attempts',
    description: 'Every submission you have made, with scores and dates.',
    icon: History,
    gradient: 'from-signal-amber/20 via-signal-mint/10 to-transparent',
    to: '/dashboard/coding/history',
  },
]

export const DIFFICULTY_OPTIONS = ['EASY', 'MEDIUM', 'HARD']

export const TOPIC_OPTIONS = [
  'ARRAYS', 'STRINGS', 'LINKED_LIST', 'TREES', 'GRAPHS', 'DP', 'RECURSION', 'GREEDY', 'BACKTRACKING',
]

export const COMPANY_OPTIONS = [
  'TCS', 'COGNIZANT', 'CAPGEMINI', 'HCL', 'INFOSYS', 'ACCENTURE', 'AMAZON', 'MICROSOFT', 'GOOGLE',
]

export const LANGUAGE_OPTIONS = [
  { value: 'JAVA', label: 'Java' },
  { value: 'PYTHON', label: 'Python' },
  { value: 'CPP', label: 'C++' },
  { value: 'JAVASCRIPT', label: 'JavaScript' },
]

export const TOPIC_LABELS = {
  ARRAYS: 'Arrays',
  STRINGS: 'Strings',
  LINKED_LIST: 'Linked List',
  TREES: 'Trees',
  GRAPHS: 'Graphs',
  DP: 'Dynamic Programming',
  RECURSION: 'Recursion',
  GREEDY: 'Greedy',
  BACKTRACKING: 'Backtracking',
}

export const DIFFICULTY_COLORS = {
  EASY: 'text-signal-mint border-signal-mint/30 bg-signal-mint/10',
  MEDIUM: 'text-signal-amber border-signal-amber/30 bg-signal-amber/10',
  HARD: 'text-red-400 border-red-500/30 bg-red-500/10',
}
