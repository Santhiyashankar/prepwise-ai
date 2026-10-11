import { useEffect, useState } from 'react'
import { Menu, X, Mic } from 'lucide-react'

const NAV_LINKS = [
  { label: 'Home', href: '#home' },
  { label: 'Features', href: '#features' },
  { label: 'How It Works', href: '#how-it-works' },
  { label: 'Pricing', href: '#pricing' },
  { label: 'Contact', href: '#contact' },
]

function Navbar() {
  const [isOpen, setIsOpen] = useState(false)
  const [isScrolled, setIsScrolled] = useState(false)

  useEffect(() => {
    const onScroll = () => setIsScrolled(window.scrollY > 8)
    window.addEventListener('scroll', onScroll)
    return () => window.removeEventListener('scroll', onScroll)
  }, [])

  return (
    <header
      className={`fixed inset-x-0 top-0 z-50 transition-colors duration-300 ${
        isScrolled ? 'bg-ink/90 backdrop-blur-md border-b border-ink-line' : 'bg-transparent'
      }`}
    >
      <nav className="container-page flex h-18 items-center justify-between py-4">
        <a href="#home" className="flex items-center gap-2 group">
          <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-signal-amber/10 border border-signal-amber/30 text-signal-amber group-hover:bg-signal-amber/20 transition-colors">
            <Mic size={18} strokeWidth={2} />
          </span>
          <span className="font-display text-lg font-semibold tracking-tight text-paper">
            PrepWise <span className="text-signal-amber">AI</span>
          </span>
        </a>

        <div className="hidden lg:flex items-center gap-8">
          {NAV_LINKS.map((link) => (
            <a
              key={link.label}
              href={link.href}
              className="text-sm font-medium text-muted hover:text-paper transition-colors"
            >
              {link.label}
            </a>
          ))}
        </div>

        <div className="hidden lg:flex items-center gap-3">
          <a
            href="/login"
            className="text-sm font-medium text-paper hover:text-signal-amber transition-colors px-4 py-2"
          >
            Log in
          </a>
          <a
            href="/register"
            className="text-sm font-semibold text-ink bg-signal-amber hover:bg-signal-amber/90 rounded-lg px-4 py-2 transition-colors"
          >
            Register
          </a>
        </div>

        <button
          type="button"
          onClick={() => setIsOpen((prev) => !prev)}
          className="lg:hidden text-paper p-2 -mr-2"
          aria-label={isOpen ? 'Close menu' : 'Open menu'}
          aria-expanded={isOpen}
        >
          {isOpen ? <X size={24} /> : <Menu size={24} />}
        </button>
      </nav>

      {isOpen && (
        <div className="lg:hidden border-t border-ink-line bg-ink px-6 pb-6 pt-2">
          <div className="flex flex-col gap-1">
            {NAV_LINKS.map((link) => (
              <a
                key={link.label}
                href={link.href}
                onClick={() => setIsOpen(false)}
                className="py-3 text-sm font-medium text-muted hover:text-paper border-b border-ink-line/60"
              >
                {link.label}
              </a>
            ))}
          </div>
          <div className="flex flex-col gap-3 pt-5">
            <a
              href="/login"
              className="text-center text-sm font-medium text-paper border border-ink-line rounded-lg px-4 py-2.5"
            >
              Log in
            </a>
            <a
              href="/register"
              className="text-center text-sm font-semibold text-ink bg-signal-amber rounded-lg px-4 py-2.5"
            >
              Register
            </a>
          </div>
        </div>
      )}
    </header>
  )
}

export default Navbar
