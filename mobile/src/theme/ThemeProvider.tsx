import React, { createContext, useContext, ReactNode } from 'react';

export const theme = {
  colors: {
    primary: '#0F62FE',
    secondary: '#393939',
    background: '#FFFFFF',
    surface: '#F4F4F4',
    error: '#DA1E28',
    text: '#161616',
    textSecondary: '#525252',
    border: '#E0E0E0',
  },
  spacing: {
    xs: 4,
    sm: 8,
    md: 16,
    lg: 24,
    xl: 32,
  },
  typography: {
    h1: { fontSize: 24, fontWeight: '700' as const },
    h2: { fontSize: 20, fontWeight: '600' as const },
    body: { fontSize: 16, fontWeight: '400' as const },
    caption: { fontSize: 12, fontWeight: '400' as const },
  },
  borderRadius: {
    sm: 4,
    md: 8,
    lg: 16,
  },
};

export type Theme = typeof theme;

const ThemeContext = createContext<Theme>(theme);

export const ThemeProvider = ({ children }: { children: ReactNode }) => {
  return (
    <ThemeContext.Provider value={theme}>
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = () => useContext(ThemeContext);
