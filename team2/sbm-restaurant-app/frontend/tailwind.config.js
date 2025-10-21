/** @type {import('tailwindcss').Config} */
const config = {
  content: [
    "./src/app/**/*.{js,ts,jsx,tsx}",
    "./src/pages/**/*.{js,ts,jsx,tsx}",
    "./src/components/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // Map our CSS variables to Tailwind colors
        gold: {
          primary: 'var(--gold-primary)',
          light: 'var(--gold-light)',
          dark: 'var(--gold-dark)',
        },
        charcoal: {
          dark: 'var(--charcoal-dark)',
          medium: 'var(--charcoal-medium)',
          light: 'var(--charcoal-light)',
        },
        cream: {
          DEFAULT: 'var(--cream)',
          dark: 'var(--cream-dark)',
        },
        status: {
          placed: 'var(--status-placed)',
          preparing: 'var(--status-preparing)',
          ready: 'var(--status-ready)',
          'out-for-delivery': 'var(--status-out-for-delivery)',
          delivered: 'var(--status-delivered)',
          pending: 'var(--status-pending)',
        },
      },
      backgroundColor: {
        primary: 'var(--gold-primary)',
        secondary: 'var(--gold-light)',
        surface: 'var(--charcoal-medium)',
        background: 'var(--charcoal-dark)',
      },
      textColor: {
        primary: 'var(--cream)',
        secondary: 'var(--cream-dark)',
        accent: 'var(--gold-primary)',
      },
      borderColor: {
        primary: 'var(--gold-primary)',
        secondary: 'var(--charcoal-light)',
      },
    },
  },
  plugins: [],
};

export default config;