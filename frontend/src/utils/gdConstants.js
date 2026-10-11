import { Briefcase, Cpu, Newspaper, LineChart, Brain, Shuffle } from 'lucide-react'

export const GD_DASHBOARD_CARDS = [
  {
    category: 'PLACEMENT',
    title: 'Placement GD',
    description: 'Classic placement-style topics on careers, education, and society.',
    difficulty: 'Medium',
    duration: '10 min',
    participants: '4-6',
    icon: Briefcase,
    gradient: 'from-signal-amber/20 via-signal-amber/5 to-transparent',
  },
  {
    category: 'TECHNICAL',
    title: 'Technical GD',
    description: 'Technology trends, tools, and engineering trade-off debates.',
    difficulty: 'Hard',
    duration: '10 min',
    participants: '3-5',
    icon: Cpu,
    gradient: 'from-blue-500/20 via-blue-500/5 to-transparent',
  },
  {
    category: 'CURRENT_AFFAIRS',
    title: 'Current Affairs GD',
    description: 'Recent events, policy, and public discourse topics.',
    difficulty: 'Medium',
    duration: '10 min',
    participants: '4-6',
    icon: Newspaper,
    gradient: 'from-signal-mint/20 via-signal-mint/5 to-transparent',
  },
  {
    category: 'BUSINESS_CASE',
    title: 'Business Case GD',
    description: 'Case-style business scenarios requiring structured reasoning.',
    difficulty: 'Hard',
    duration: '15 min',
    participants: '4-6',
    icon: LineChart,
    gradient: 'from-purple-500/20 via-purple-500/5 to-transparent',
  },
  {
    category: 'ABSTRACT',
    title: 'Abstract Topic GD',
    description: 'Open-ended, interpretive topics that reward creative framing.',
    difficulty: 'Medium',
    duration: '10 min',
    participants: '3-5',
    icon: Brain,
    gradient: 'from-pink-500/20 via-pink-500/5 to-transparent',
  },
  {
    category: 'RANDOM',
    title: 'Random Topic GD',
    description: 'A surprise topic, picked fresh for each session.',
    difficulty: 'Easy',
    duration: '5 min',
    participants: '3-4',
    icon: Shuffle,
    gradient: 'from-signal-amber/20 via-signal-mint/10 to-transparent',
  },
]

export const DIFFICULTY_OPTIONS = ['EASY', 'MEDIUM', 'HARD']
export const PARTICIPANT_COUNT_OPTIONS = [3, 4, 5, 6]
export const DURATION_OPTIONS = [5, 10, 15]

export const CATEGORY_LABELS = {
  PLACEMENT: 'Placement GD',
  TECHNICAL: 'Technical GD',
  CURRENT_AFFAIRS: 'Current Affairs GD',
  BUSINESS_CASE: 'Business Case GD',
  ABSTRACT: 'Abstract Topic GD',
  RANDOM: 'Random Topic GD',
}

export const PERSONALITY_LABELS = {
  TEAM_LEADER: 'Team Leader',
  LOGICAL_THINKER: 'Logical Thinker',
  AGGRESSIVE_SPEAKER: 'Aggressive Speaker',
  CALM_SPEAKER: 'Calm Speaker',
  CREATIVE_THINKER: 'Creative Thinker',
  QUESTIONER: 'Questioner',
}

export const PARTICIPANT_COLORS = [
  { text: 'text-signal-amber', bg: 'bg-signal-amber/10', border: 'border-signal-amber/25' },
  { text: 'text-signal-mint', bg: 'bg-signal-mint/10', border: 'border-signal-mint/25' },
  { text: 'text-blue-400', bg: 'bg-blue-500/10', border: 'border-blue-500/25' },
  { text: 'text-purple-400', bg: 'bg-purple-500/10', border: 'border-purple-500/25' },
  { text: 'text-pink-400', bg: 'bg-pink-500/10', border: 'border-pink-500/25' },
  { text: 'text-orange-400', bg: 'bg-orange-500/10', border: 'border-orange-500/25' },
]
