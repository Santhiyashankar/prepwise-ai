import { useState } from 'react'
import { Send, Sparkles, Bot, User } from 'lucide-react'
import ReactMarkdown from 'react-markdown'
import mentorApi from '../../../api/mentorApi.js'

function AIMentorPage() {
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(false)

  const [messages, setMessages] = useState([
    {
      role: 'ai',
      text: "Hi! I'm your AI Mentor. I can help you with interview preparation, Java, React, DSA, resume questions, and career preparation. What would you like to practice today?",
    },
  ])

  const handleSend = async () => {
    const text = message.trim()

    if (!text || loading) return

    setLoading(true)

    setMessages((prev) => [
      ...prev,
      {
        role: 'user',
        text,
      },
    ])

    setMessage('')

    try {
      const response = await mentorApi.chat(text)

      setMessages((prev) => [
        ...prev,
        {
          role: 'ai',
          text: response.data.data,
        },
      ])
    } catch (error) {
      console.error('AI Mentor error:', error)

      setMessages((prev) => [
        ...prev,
        {
          role: 'ai',
          text: 'Sorry, I could not connect to the AI Mentor. Please try again.',
        },
      ])
    } finally {
      setLoading(false)
    }
  }

  const handleKeyDown = (event) => {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault()
      handleSend()
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <div className="flex items-center gap-2">
          <Sparkles size={22} className="text-signal-amber" />

          <h1 className="font-display text-2xl font-semibold text-[var(--dash-text)]">
            AI Mentor
          </h1>
        </div>

        <p className="mt-1 text-sm text-[var(--dash-muted)]">
          Your personal AI mentor for interview and career preparation
        </p>
      </div>

      {/* Chat Container */}
      <div className="overflow-hidden rounded-2xl border border-[var(--dash-border)] bg-[var(--dash-surface)]">
        {/* Chat Header */}
        <div className="flex items-center gap-3 border-b border-[var(--dash-border)] px-5 py-4">
          <div className="flex h-10 w-10 items-center justify-center rounded-full border border-signal-amber/30 bg-signal-amber/10">
            <Bot size={20} className="text-signal-amber" />
          </div>

          <div>
            <h2 className="text-sm font-semibold text-[var(--dash-text)]">
              PrepWise AI Mentor
            </h2>

            <p className="text-xs text-[var(--dash-muted)]">
              Interview preparation assistant
            </p>
          </div>
        </div>

        {/* Messages */}
        <div className="min-h-[420px] max-h-[520px] space-y-4 overflow-y-auto p-5">
          {messages.map((item, index) => (
            <div
              key={index}
              className={`flex gap-3 ${
                item.role === 'user'
                  ? 'justify-end'
                  : 'justify-start'
              }`}
            >
              {/* AI Icon */}
              {item.role === 'ai' && (
                <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-full border border-signal-amber/30 bg-signal-amber/10">
                  <Bot size={16} className="text-signal-amber" />
                </div>
              )}

              {/* Message */}
              <div
                className={`max-w-[75%] rounded-xl px-4 py-3 text-sm ${
                  item.role === 'user'
                    ? 'bg-signal-amber text-black'
                    : 'border border-[var(--dash-border)] bg-[var(--dash-bg)] text-[var(--dash-text)]'
                }`}
              >
                {item.role === 'ai' ? (
                  <ReactMarkdown
                    components={{
                      h1: ({ children }) => (
                        <h1 className="mb-3 text-lg font-bold">
                          {children}
                        </h1>
                      ),

                      h2: ({ children }) => (
                        <h2 className="mb-2 mt-4 text-base font-bold">
                          {children}
                        </h2>
                      ),

                      h3: ({ children }) => (
                        <h3 className="mb-2 mt-3 text-sm font-bold">
                          {children}
                        </h3>
                      ),

                      p: ({ children }) => (
                        <p className="mb-2 last:mb-0">
                          {children}
                        </p>
                      ),

                      ul: ({ children }) => (
                        <ul className="mb-2 ml-5 list-disc space-y-1">
                          {children}
                        </ul>
                      ),

                      ol: ({ children }) => (
                        <ol className="mb-2 ml-5 list-decimal space-y-1">
                          {children}
                        </ol>
                      ),

                      li: ({ children }) => (
                        <li>{children}</li>
                      ),

                      strong: ({ children }) => (
                        <strong className="font-semibold">
                          {children}
                        </strong>
                      ),

                      em: ({ children }) => (
                        <em>{children}</em>
                      ),

                      blockquote: ({ children }) => (
                        <blockquote className="my-3 border-l-2 border-signal-amber pl-3 italic">
                          {children}
                        </blockquote>
                      ),

                      code: ({ children, className }) => {
                        const isBlock = className?.includes('language-')

                        if (isBlock) {
                          return (
                            <pre className="my-3 overflow-x-auto rounded-lg border border-[var(--dash-border)] bg-black/30 p-4">
                              <code className="text-xs leading-5">
                                {children}
                              </code>
                            </pre>
                          )
                        }

                        return (
                          <code className="rounded bg-black/20 px-1.5 py-0.5 text-xs">
                            {children}
                          </code>
                        )
                      },

                      pre: ({ children }) => (
                        <div className="my-3 overflow-x-auto">
                          {children}
                        </div>
                      ),

                      a: ({ children, href }) => (
                        <a
                          href={href}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="text-signal-amber underline"
                        >
                          {children}
                        </a>
                      ),
                    }}
                  >
                    {item.text}
                  </ReactMarkdown>
                ) : (
                  item.text
                )}
              </div>

              {/* User Icon */}
              {item.role === 'user' && (
                <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-full border border-[var(--dash-border)]">
                  <User
                    size={16}
                    className="text-[var(--dash-muted)]"
                  />
                </div>
              )}
            </div>
          ))}

          {/* Loading */}
          {loading && (
            <div className="flex justify-start gap-3">
              <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-full border border-signal-amber/30 bg-signal-amber/10">
                <Bot size={16} className="text-signal-amber" />
              </div>

              <div className="rounded-xl border border-[var(--dash-border)] bg-[var(--dash-bg)] px-4 py-3 text-sm text-[var(--dash-muted)]">
                Thinking...
              </div>
            </div>
          )}
        </div>

        {/* Input */}
        <div className="border-t border-[var(--dash-border)] p-4">
          <div className="flex items-end gap-3">
            <textarea
              value={message}
              onChange={(event) => setMessage(event.target.value)}
              onKeyDown={handleKeyDown}
              placeholder="Ask your AI Mentor anything..."
              rows={2}
              disabled={loading}
              className="flex-1 resize-none rounded-xl border border-[var(--dash-border)] bg-[var(--dash-bg)] px-4 py-3 text-sm text-[var(--dash-text)] outline-none focus:border-signal-amber disabled:cursor-not-allowed disabled:opacity-60"
            />

            <button
              type="button"
              onClick={handleSend}
              disabled={!message.trim() || loading}
              className="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-signal-amber text-black transition-opacity disabled:cursor-not-allowed disabled:opacity-40"
            >
              <Send size={18} />
            </button>
          </div>

          <p className="mt-2 text-xs text-[var(--dash-muted)]">
            Press Enter to send
          </p>
        </div>
      </div>

      {/* Quick Practice */}
      <div>
        <h2 className="mb-3 text-sm font-semibold text-[var(--dash-text)]">
          Quick Practice
        </h2>

        <div className="grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
          {[
            'Ask me a Java interview question',
            'Help me improve my resume',
            'Give me a DSA problem',
            'Practice HR interview questions',
          ].map((prompt) => (
            <button
              key={prompt}
              type="button"
              onClick={() => setMessage(prompt)}
              disabled={loading}
              className="rounded-xl border border-[var(--dash-border)] bg-[var(--dash-surface)] p-4 text-left text-sm text-[var(--dash-muted)] transition-colors hover:border-signal-amber/50 hover:text-[var(--dash-text)] disabled:cursor-not-allowed disabled:opacity-50"
            >
              {prompt}
            </button>
          ))}
        </div>
      </div>
    </div>
  )
}

export default AIMentorPage