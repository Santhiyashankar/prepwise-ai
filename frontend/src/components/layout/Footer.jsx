import { Mic, Github, Linkedin, Twitter } from 'lucide-react'

const FOOTER_LINKS = {
  Company: [
    { label: 'About', href: '/about' },
    { label: 'Contact', href: '#contact' },
  ],
  Legal: [
    { label: 'Privacy Policy', href: '/privacy' },
    { label: 'Terms & Conditions', href: '/terms' },
  ],
}

function Footer() {
  return (
    <footer id="contact" className="bg-ink pt-16 pb-8">
      <div className="container-page">
        <div className="grid gap-10 md:grid-cols-[2fr_1fr_1fr]">
          <div>
            <a href="#home" className="flex items-center gap-2">
              <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-signal-amber/10 border border-signal-amber/30 text-signal-amber">
                <Mic size={18} strokeWidth={2} />
              </span>
              <span className="font-display text-lg font-semibold tracking-tight text-paper">
                PrepWise <span className="text-signal-amber">AI</span>
              </span>
            </a>
            <p className="mt-4 max-w-sm text-sm text-muted leading-relaxed">
              Live mock interviews with an AI panel — voice, video, and a transcript for every
              round.
            </p>
            <div className="mt-6 flex items-center gap-4">
              <a
                href="https://github.com"
                aria-label="GitHub"
                className="text-muted hover:text-signal-amber transition-colors"
              >
                <Github size={18} />
              </a>
              <a
                href="https://linkedin.com"
                aria-label="LinkedIn"
                className="text-muted hover:text-signal-amber transition-colors"
              >
                <Linkedin size={18} />
              </a>
              <a
                href="https://twitter.com"
                aria-label="Twitter"
                className="text-muted hover:text-signal-amber transition-colors"
              >
                <Twitter size={18} />
              </a>
            </div>
          </div>

          {Object.entries(FOOTER_LINKS).map(([section, links]) => (
            <div key={section}>
              <p className="font-mono text-xs tracking-wide text-muted">{section.toUpperCase()}</p>
              <ul className="mt-4 space-y-3">
                {links.map((link) => (
                  <li key={link.label}>
                    <a
                      href={link.href}
                      className="text-sm text-paper/80 hover:text-signal-amber transition-colors"
                    >
                      {link.label}
                    </a>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>

        <div className="mt-14 pt-6 border-t border-ink-line flex flex-col sm:flex-row items-center justify-between gap-3">
          <p className="text-xs text-muted">© {new Date().getFullYear()} PrepWise AI. All rights reserved.</p>
          <p className="font-mono text-xs text-muted">BUILT FOR CANDIDATES, NOT RECRUITERS</p>
        </div>
      </div>
    </footer>
  )
}

export default Footer
