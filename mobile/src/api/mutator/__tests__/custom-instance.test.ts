import { describe, it, expect, jest, beforeEach } from '@jest/globals';
import { customInstance } from '../custom-instance';
import { getAccessToken, handleUnauthorized } from '../../../auth/getAccessToken';

jest.mock('../../../auth/getAccessToken', () => ({
  getAccessToken: jest.fn(),
  handleUnauthorized: jest.fn(),
}));

global.fetch = jest.fn();

describe('customInstance', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('attaches Authorization header when token exists', async () => {
    (getAccessToken as jest.Mock<any>).mockResolvedValue('fake-token');
    (global.fetch as jest.Mock<any>).mockResolvedValue({
      ok: true,
      status: 200,
      text: jest.fn().mockResolvedValue('{"success":true}'),
    });

    const result = await customInstance('/test-url', { method: 'GET' });
    expect(result).toEqual({ success: true });
    expect(global.fetch).toHaveBeenCalledWith(
      expect.stringContaining('/test-url'),
      expect.objectContaining({
        headers: expect.objectContaining({
          Authorization: 'Bearer fake-token',
        }),
      })
    );
  });

  it('omits Authorization header when token is missing', async () => {
    (getAccessToken as jest.Mock<any>).mockResolvedValue(undefined);
    (global.fetch as jest.Mock<any>).mockResolvedValue({
      ok: true,
      status: 200,
      text: jest.fn().mockResolvedValue('{"success":true}'),
    });

    await customInstance('/test-url', { method: 'GET' });
    
    const fetchCall: any = (global.fetch as jest.Mock<any>).mock.calls[0][1];
    expect(fetchCall.headers).not.toHaveProperty('Authorization');
  });

  it('throws mapped error structure and calls handleUnauthorized on 401', async () => {
    (getAccessToken as jest.Mock<any>).mockResolvedValue('fake-token');
    (global.fetch as jest.Mock<any>).mockResolvedValue({
      ok: false,
      status: 401,
      json: jest.fn().mockResolvedValue({ detail: 'Unauthorized' }),
    });

    await expect(customInstance('/test-url', { method: 'GET' })).rejects.toEqual({
      status: 401,
      data: { detail: 'Unauthorized' },
    });
    
    expect(handleUnauthorized).toHaveBeenCalled();
  });

  it('throws mapped error structure on 403', async () => {
    (getAccessToken as jest.Mock<any>).mockResolvedValue('fake-token');
    (global.fetch as jest.Mock<any>).mockResolvedValue({
      ok: false,
      status: 403,
      json: jest.fn().mockResolvedValue({ detail: 'Forbidden' }),
    });

    await expect(customInstance('/test-url', { method: 'GET' })).rejects.toEqual({
      status: 403,
      data: { detail: 'Forbidden' },
    });
  });

  it('throws safely on 500 when JSON parsing fails', async () => {
    (getAccessToken as jest.Mock<any>).mockResolvedValue('fake-token');
    (global.fetch as jest.Mock<any>).mockResolvedValue({
      ok: false,
      status: 500,
      json: jest.fn().mockRejectedValue(new Error('Invalid JSON')),
    });

    await expect(customInstance('/test-url', { method: 'GET' })).rejects.toEqual({
      status: 500,
      data: null,
    });
  });

  it('returns empty object on 204 No Content', async () => {
    (getAccessToken as jest.Mock<any>).mockResolvedValue('fake-token');
    (global.fetch as jest.Mock<any>).mockResolvedValue({
      ok: true,
      status: 204,
    });

    const result = await customInstance('/test-url', { method: 'DELETE' });
    expect(result).toEqual({});
  });
});
