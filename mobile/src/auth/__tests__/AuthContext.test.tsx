import { describe, it, expect, jest, beforeEach } from '@jest/globals';
import React from 'react';
import renderer from 'react-test-renderer';
import { Text } from 'react-native';
import { AuthProvider, useAuth } from '../AuthContext';
import { useAuth0 } from 'react-native-auth0';

jest.mock('react-native-auth0', () => ({
  useAuth0: jest.fn(),
}));

const TestComponent = () => {
  const { isAuthenticated, isLoading, login, logout, error } = useAuth();
  return (
    <>
      <Text testID="auth-state">{isAuthenticated ? 'Authenticated' : 'Unauthenticated'}</Text>
      <Text testID="loading-state">{isLoading ? 'Loading' : 'Ready'}</Text>
      <Text testID="error-state">{error ? 'Error' : 'NoError'}</Text>
      <Text testID="login-btn" onPress={login}>Login</Text>
      <Text testID="logout-btn" onPress={logout}>Logout</Text>
    </>
  );
};

describe('AuthContext', () => {
  const mockAuthorize = jest.fn();
  const mockClearSession = jest.fn();
  const mockGetCredentials = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('provides unauthenticated state', async () => {
    (useAuth0 as jest.Mock<any>).mockReturnValue({
      user: null,
      isLoading: false,
      error: null,
      authorize: mockAuthorize,
      clearSession: mockClearSession,
      getCredentials: mockGetCredentials,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      );
    });

    const root = component.root;
    expect(root.findByProps({ testID: 'auth-state' }).props.children).toBe('Unauthenticated');
  });

  it('provides authenticated state', async () => {
    (useAuth0 as jest.Mock<any>).mockReturnValue({
      user: { name: 'Test User' },
      isLoading: false,
      error: null,
      authorize: mockAuthorize,
      clearSession: mockClearSession,
      getCredentials: mockGetCredentials,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      );
    });

    const root = component.root;
    expect(root.findByProps({ testID: 'auth-state' }).props.children).toBe('Authenticated');
  });

  it('invokes login', async () => {
    (useAuth0 as jest.Mock<any>).mockReturnValue({
      user: null,
      isLoading: false,
      error: null,
      authorize: mockAuthorize,
      clearSession: mockClearSession,
      getCredentials: mockGetCredentials,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      );
    });

    await renderer.act(async () => {
      component.root.findByProps({ testID: 'login-btn' }).props.onPress();
    });

    expect(mockAuthorize).toHaveBeenCalled();
  });

  it('invokes logout', async () => {
    (useAuth0 as jest.Mock<any>).mockReturnValue({
      user: { name: 'Test User' },
      isLoading: false,
      error: null,
      authorize: mockAuthorize,
      clearSession: mockClearSession,
      getCredentials: mockGetCredentials,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(
        <AuthProvider>
          <TestComponent />
        </AuthProvider>
      );
    });

    await renderer.act(async () => {
      component.root.findByProps({ testID: 'logout-btn' }).props.onPress();
    });

    expect(mockClearSession).toHaveBeenCalled();
  });
});
