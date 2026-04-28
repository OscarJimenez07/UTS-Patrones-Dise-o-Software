/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      fontFamily: {
        display: ['Orbitron', 'sans-serif'],
        sans: ['Inter', 'system-ui', 'sans-serif'],
        mono: ['JetBrains Mono', 'Menlo', 'monospace'],
      },
      colors: {
        ink: {
          950: '#0a0b14',
          900: '#0f111c',
          800: '#161928',
          700: '#1f2335',
          600: '#2a2e44',
        },
        neon: {
          violet: '#a855f7',
          fuchsia: '#d946ef',
          emerald: '#10b981',
          cyan: '#22d3ee',
          amber: '#f59e0b',
        },
      },
      boxShadow: {
        'neon-violet': '0 0 20px rgba(168, 85, 247, 0.45)',
        'neon-emerald': '0 0 20px rgba(16, 185, 129, 0.4)',
        'neon-cyan': '0 0 20px rgba(34, 211, 238, 0.4)',
      },
      backgroundImage: {
        'grid-pattern':
          "linear-gradient(rgba(168,85,247,0.06) 1px, transparent 1px), linear-gradient(90deg, rgba(168,85,247,0.06) 1px, transparent 1px)",
      },
    },
  },
  plugins: [],
}
