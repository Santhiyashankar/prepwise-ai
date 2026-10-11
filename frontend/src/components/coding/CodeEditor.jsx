import { useMemo } from 'react'
import Editor from 'react-simple-code-editor'
import Prism from 'prismjs'
import 'prismjs/components/prism-clike'
import 'prismjs/components/prism-java'
import 'prismjs/components/prism-python'
import 'prismjs/components/prism-c'
import 'prismjs/components/prism-cpp'
import 'prismjs/components/prism-javascript'

const LANGUAGE_MAP = {
  JAVA: 'java',
  PYTHON: 'python',
  CPP: 'cpp',
  JAVASCRIPT: 'javascript',
}

function CodeEditor({ code, language, onChange, readOnly = false }) {
  const prismLanguage = LANGUAGE_MAP[language] || 'clike'

  const lineCount = useMemo(() => code.split('\n').length, [code])

  const highlight = (source) => {
    const grammar = Prism.languages[prismLanguage] || Prism.languages.clike
    return Prism.highlight(source, grammar, prismLanguage)
  }

  return (
    <div className="flex rounded-xl border border-[var(--dash-border)] bg-[#0d1117] overflow-hidden">
      <div
        className="select-none px-3 py-4 text-right font-mono text-xs leading-[1.6] text-[#4b5563] bg-[#0a0e14] border-r border-[var(--dash-border)]"
        aria-hidden="true"
      >
        {Array.from({ length: lineCount }, (_, i) => (
          <div key={i}>{i + 1}</div>
        ))}
      </div>
      <div className="flex-1 overflow-x-auto">
        <Editor
          value={code}
          onValueChange={onChange}
          highlight={highlight}
          padding={16}
          disabled={readOnly}
          tabSize={2}
          insertSpaces
          textareaClassName="code-editor-textarea"
          preClassName="code-editor-pre"
          style={{
            fontFamily: '"IBM Plex Mono", monospace',
            fontSize: 13,
            lineHeight: '1.6',
            minHeight: '360px',
            color: '#e6edf3',
          }}
        />
      </div>
    </div>
  )
}

export default CodeEditor
