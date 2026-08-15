import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  test: {
    env: {
      VITE_API_BASE_URL: 'http://localhost:8080',
      VITE_AUTH0_DOMAIN: 'test-domain.auth0.com',
      VITE_AUTH0_CLIENT_ID: 'test-client-id',
      VITE_AUTH0_AUDIENCE: 'test-audience',
    },
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.ts'],
    globals: true,
  },
});
