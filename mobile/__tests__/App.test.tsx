import { describe, it, expect, jest } from '@jest/globals';
import React from 'react';
import renderer from 'react-test-renderer';
import App from '../App';
import { ErrorBoundary } from '../src/components/ErrorBoundary';

jest.mock('react-native-auth0', () => ({
  Auth0Provider: ({ children }: any) => children,
  useAuth0: () => ({
    authorize: jest.fn(),
    clearSession: jest.fn(),
    getCredentials: jest.fn(),
    user: null,
    isLoading: false,
    error: null,
  }),
}));

describe('Application Root', () => {
  it('renders without crashing', async () => {
    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<App />);
    });
    expect(component.toJSON()).toBeTruthy();
  });
});

describe('ErrorBoundary', () => {
  it('renders fallback UI when child throws', async () => {
    const ThrowError = () => {
      throw new Error('Test Error');
    };

    const consoleError = jest.spyOn(console, 'error').mockImplementation(() => {});

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(
        <ErrorBoundary>
          <ThrowError />
        </ErrorBoundary>
      );
    });

    const root = component.root;
    expect(root.findByProps({ children: 'Something went wrong' })).toBeTruthy();
    consoleError.mockRestore();
  });
});
