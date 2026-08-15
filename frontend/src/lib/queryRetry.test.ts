import { describe, it, expect } from 'vitest';
import { shouldRetry } from './queryRetry';

describe('shouldRetry', () => {
  describe('HTTP 401 — never retry (JWT contract §9)', () => {
    it('returns false on first failure', () => {
      expect(shouldRetry(0, { status: 401, data: {} })).toBe(false);
    });

    it('returns false even at failureCount > 0', () => {
      expect(shouldRetry(1, { status: 401, data: {} })).toBe(false);
    });
  });

  describe('HTTP 403 — never retry (JWT contract §9)', () => {
    it('returns false on first failure', () => {
      expect(shouldRetry(0, { status: 403, data: {} })).toBe(false);
    });

    it('returns false even at failureCount > 0', () => {
      expect(shouldRetry(1, { status: 403, data: {} })).toBe(false);
    });
  });

  describe('HTTP 500 — allow exactly one retry', () => {
    it('returns true on first failure (failureCount 0)', () => {
      expect(shouldRetry(0, { status: 500, data: {} })).toBe(true);
    });

    it('returns false after one retry (failureCount 1)', () => {
      expect(shouldRetry(1, { status: 500, data: {} })).toBe(false);
    });
  });

  describe('Other network/unknown errors', () => {
    it('retries once for null error', () => {
      expect(shouldRetry(0, null)).toBe(true);
      expect(shouldRetry(1, null)).toBe(false);
    });

    it('retries once for undefined error', () => {
      expect(shouldRetry(0, undefined)).toBe(true);
      expect(shouldRetry(1, undefined)).toBe(false);
    });

    it('retries once for errors with no status property', () => {
      expect(shouldRetry(0, new Error('Network error'))).toBe(true);
      expect(shouldRetry(1, new Error('Network error'))).toBe(false);
    });

    it('retries once for status 422 (validation — not an auth error)', () => {
      expect(shouldRetry(0, { status: 422, data: {} })).toBe(true);
      expect(shouldRetry(1, { status: 422, data: {} })).toBe(false);
    });

    it('retries once for status 404', () => {
      expect(shouldRetry(0, { status: 404, data: {} })).toBe(true);
      expect(shouldRetry(1, { status: 404, data: {} })).toBe(false);
    });
  });
});
