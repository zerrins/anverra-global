import '@testing-library/jest-dom';
import { beforeAll, afterEach, afterAll } from 'vitest';
import { setupServer } from 'msw/node';
import { handlers } from './mocks/handlers';
import { resetTokenState, setTokenGetter } from '../auth/token';

export const server = setupServer(...handlers);

beforeAll(() => server.listen());

beforeEach(() => {
  setTokenGetter(async () => undefined);
});

afterEach(() => {
  server.resetHandlers();
  resetTokenState();
});
afterAll(() => server.close());

// Mock window.matchMedia
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: (query: string) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: () => {}, // Deprecated
    removeListener: () => {}, // Deprecated
    addEventListener: () => {},
    removeEventListener: () => {},
    dispatchEvent: () => false,
  }),
});
