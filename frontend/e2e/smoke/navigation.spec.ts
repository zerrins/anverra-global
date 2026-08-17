import { test, expect } from '@playwright/test';

test.describe('Smoke Test', () => {
  test('Application boots and renders without crashing', async ({ page }) => {
    const errors: string[] = [];
    page.on('pageerror', (err) => {
      errors.push(err.message);
    });

    await page.goto('/', { waitUntil: 'load' });

    // Verify no obvious uncaught exceptions
    expect(errors).toHaveLength(0);
  });
});
