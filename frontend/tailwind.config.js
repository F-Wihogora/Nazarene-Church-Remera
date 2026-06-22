/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        church: {
          dark: '#050D1A',      // Near black dark blue
          navy: '#0F2C59',      // Core logo dark blue
          blue: '#1C5BBA',      // Accent medium blue
          soft: '#F4F7FC',      // Creamy white/blue background
          charcoal: '#1A1D20',  // Muted black text
          gray: '#E2E8F0',      // Border gray
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', '-apple-system', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
