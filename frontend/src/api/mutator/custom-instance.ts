import { getToken } from '../../auth/token';

const baseURL = import.meta.env.VITE_API_BASE_URL || '';

export const customInstance = async <T>(
  url: string,
  config: RequestInit
): Promise<T> => {
  const token = await getToken();
  
  const headers = new Headers(config.headers);
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }
  if (!headers.has('Content-Type') && config.method !== 'GET') {
    headers.set('Content-Type', 'application/json');
  }

  const response = await fetch(`${baseURL}${url}`, {
    ...config,
    headers,
  });

  if (!response.ok) {
    let errorData;
    try {
      errorData = await response.json();
    } catch {
      errorData = { detail: response.statusText };
    }
    throw { status: response.status, data: errorData };
  }

  let data: any;
  const contentType = response.headers.get('content-type');
  if (contentType && contentType.includes('application/json')) {
    data = await response.json();
  } else {
    data = await response.text();
  }

  return {
    data,
    status: response.status,
    headers: response.headers,
  } as unknown as T;
};
