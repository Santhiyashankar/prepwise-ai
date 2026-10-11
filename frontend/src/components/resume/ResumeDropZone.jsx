import { useRef, useState } from 'react'
import { UploadCloud, FileText } from 'lucide-react'

const ACCEPTED_TYPES = ['.pdf', '.docx']
const MAX_SIZE_BYTES = 10 * 1024 * 1024

function ResumeDropzone({ onFileSelected, disabled }) {
  const inputRef = useRef(null)
  const [isDragging, setIsDragging] = useState(false)
  const [validationError, setValidationError] = useState('')

  const validateAndEmit = (file) => {
    if (!file) return
    setValidationError('')

    const extension = '.' + file.name.split('.').pop().toLowerCase()
    if (!ACCEPTED_TYPES.includes(extension)) {
      setValidationError('Only PDF and DOCX files are supported.')
      return
    }
    if (file.size > MAX_SIZE_BYTES) {
      setValidationError('File must be smaller than 10MB.')
      return
    }
    onFileSelected(file)
  }

  const handleDrop = (event) => {
    event.preventDefault()
    setIsDragging(false)
    if (disabled) return
    const file = event.dataTransfer.files?.[0]
    validateAndEmit(file)
  }

  const handleInputChange = (event) => {
    const file = event.target.files?.[0]
    validateAndEmit(file)
    event.target.value = ''
  }

  return (
    <div>
      <div
        onDragOver={(event) => {
          event.preventDefault()
          if (!disabled) setIsDragging(true)
        }}
        onDragLeave={() => setIsDragging(false)}
        onDrop={handleDrop}
        onClick={() => !disabled && inputRef.current?.click()}
        role="button"
        tabIndex={0}
        className={`flex flex-col items-center justify-center gap-3 rounded-2xl border-2 border-dashed px-6 py-12 text-center transition-colors cursor-pointer ${
          isDragging
            ? 'border-signal-amber bg-signal-amber/10'
            : 'border-[var(--dash-border)] hover:border-signal-amber/40'
        } ${disabled ? 'opacity-60 pointer-events-none' : ''}`}
      >
        <span className="flex h-14 w-14 items-center justify-center rounded-full bg-signal-amber/10 border border-signal-amber/25 text-signal-amber">
          {isDragging ? <FileText size={26} /> : <UploadCloud size={26} />}
        </span>
        <div>
          <p className="text-sm font-medium text-[var(--dash-text)]">
            Drag and drop your resume, or click to browse
          </p>
          <p className="mt-1 text-xs text-[var(--dash-muted)]">PDF or DOCX, up to 10MB</p>
        </div>
        <input
          ref={inputRef}
          type="file"
          accept=".pdf,.docx"
          className="hidden"
          onChange={handleInputChange}
        />
      </div>
      {validationError && <p className="mt-2 text-xs text-red-400">{validationError}</p>}
    </div>
  )
}

export default ResumeDropzone
