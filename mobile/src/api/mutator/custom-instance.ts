import { getAccessToken } from '../../auth/getAccessToken';

const BASE_URL = process.env.EXPO_PUBLIC_API_URL || 'http://localhost:8080';

export interface ErrorType<ErrorData> {
  status: number;
  data?: ErrorData;
}

export const customInstance = async <T>(
  url: string,
  options?: RequestInit & { params?: any; data?: any }
): Promise<T> => {
  const config = options || {};
  const token = await getAccessToken();

  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...config.headers,
  };

  const queryParams = config.params
    ? '?' + new URLSearchParams(config.params as any).toString()
    : '';

  const response = await fetch(`${BASE_URL}${url}${queryParams}`, {
    method: config.method,
    headers,
    body: config.data ? JSON.stringify(config.data) : undefined,
    signal: config.signal,
  });

  if (!response.ok) {
    let errorData = null;
    try {
      errorData = await response.json();
    } catch (_e) {
      // Not JSON
    }

    const error: ErrorType<any> = {
      status: response.status,
      data: errorData,
    };
    throw error;
  }

  if (response.status === 204) {
    return {} as T;
  }

  const text = await response.text();
  if (!text) {
    return {} as T;
  }
  
  return JSON.parse(text);
};
