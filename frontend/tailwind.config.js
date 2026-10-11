/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        ink: {
          DEFAULT: '#0B1220',
          soft: '#131C2E',
          line: '#233047',
        },
        paper: {
          DEFAULT: '#EDEFF4',
          dim: '#DCE0EA',
        },
        signal: {
          amber: '#FFB020',
          amberDim: '#8A5A11',
          mint: '#4ADE80',
        },
        muted: '#94A3B8',
      },
      fontFamily: {
        display: ['"Space Grotesk"', 'sans-serif'],
        body: ['"Inter"', 'sans-serif'],
        mono: ['"IBM Plex Mono"', 'monospace'],
      },
      keyframes: {
        blink: {
          '0%, 100%': { opacity: 1 },
          '50%': { opacity: 0.15 },
        },
        wave: {
          '0%, 100%': { transform: 'scaleY(0.3)' },
          '50%': { transform: 'scaleY(1)' },
        },
        rise: {
          '0%': { opacity: 0, transform: 'translateY(16px)' },
          '100%': { opacity: 1, transform: 'translateY(0)' },
        },
      },
      animation: {
        blink: 'blink 1.4s ease-in-out infinite',
        wave1: 'wave 0.9s ease-in-out infinite',
        wave2: 'wave 1.1s ease-in-out infinite',
        wave3: 'wave 0.75s ease-in-out infinite',
        wave4: 'wave 1s ease-in-out infinite',
        wave5: 'wave 0.85s ease-in-out infinite',
        rise: 'rise 0.6s ease-out both',
      },
    },
  },
  plugins: [],
}
