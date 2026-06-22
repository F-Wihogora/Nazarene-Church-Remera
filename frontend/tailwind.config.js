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
          dark: '#00122B',      // Solid deep black-blue
          navy: '#002855',      // Official Nazarene deep navy
          blue: '#005A9C',      // Official Nazarene accent blue
          soft: '#F8FAFC',      // Soft clean background
          charcoal: '#0F172A',  // Slate black text
          gray: '#E2E8F0',      // Light border gray
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', '-apple-system', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
