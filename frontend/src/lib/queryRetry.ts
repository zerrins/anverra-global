/**
 * Determines whether a TanStack Query failure should be retried.
 *
 * Per REQ-DEC-010 §9 (Frozen JWT Contract):
 *   - HTTP 401 (Unauthenticated): clear state, redirect to login — DO NOT retry.
 *   - HTTP 403 (Unauthorized): render Access Denied — DO NOT retry.
 *   - Other errors: allow at most one retry.
 *
 * The custom fetch mutator (custom-instance.ts) throws plain objects of the
 * shape: { status: number, data: unknown }
 */
export const shouldRetry = (failureCount: number, error: unknown): boolean => {
  // Safely extract status from the custom mutator error shape
  const status = (error as { status?: number } | null)?.status;

  if (status === 401 || status === 403) {
    return false;
  }

  return failureCount < 1;
};
