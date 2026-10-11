const COLORS = ['#FFB020', '#4ADE80', '#60A5FA', '#F472B6', '#F5F3EE']

function Confetti({ pieceCount = 60 }) {
  const pieces = Array.from({ length: pieceCount }, (_, index) => {
    const left = Math.random() * 100
    const delay = Math.random() * 0.6
    const duration = 2.4 + Math.random() * 1.4
    const color = COLORS[index % COLORS.length]
    const rotate = Math.random() * 360
    const size = 6 + Math.random() * 6

    return (
      <span
        key={index}
        style={{
          position: 'absolute',
          top: '-10px',
          left: `${left}%`,
          width: `${size}px`,
          height: `${size * 0.4}px`,
          backgroundColor: color,
          transform: `rotate(${rotate}deg)`,
          animation: `confetti-fall ${duration}s ease-in ${delay}s forwards`,
          borderRadius: '2px',
        }}
      />
    )
  })

  return (
    <div className="pointer-events-none fixed inset-0 z-[100] overflow-hidden">
      <style>{`
        @keyframes confetti-fall {
          0% { transform: translateY(0) rotate(0deg); opacity: 1; }
          100% { transform: translateY(100vh) rotate(540deg); opacity: 0; }
        }
      `}</style>
      {pieces}
    </div>
  )
}

export default Confetti
