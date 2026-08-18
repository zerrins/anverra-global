import { describe, it, expect, jest } from '@jest/globals';
import React from 'react';
import renderer from 'react-test-renderer';
import { PolicyDetailsScreen } from '../PolicyDetailsScreen';
import * as api from '../../api/generated/endpoints';

jest.mock('../../api/generated/endpoints', () => ({
  useGetPolicy: jest.fn(),
}));

jest.mock('@react-navigation/native', () => ({
  useRoute: () => ({
    params: { policyId: '1' }
  }),
}));

describe('PolicyDetailsScreen', () => {
  it('renders loading state', async () => {
    (api.useGetPolicy as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: true,
      isError: false,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<PolicyDetailsScreen />);
    });
    expect(component.toJSON()).toBeTruthy();
  });

  it('renders error state', async () => {
    (api.useGetPolicy as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: false,
      isError: true,
      refetch: jest.fn(),
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<PolicyDetailsScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'Failed to load policy details.' })).toBeTruthy();
  });

  it('renders 403 access denied state', async () => {
    (api.useGetPolicy as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: false,
      isError: true,
      error: { status: 403 }
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<PolicyDetailsScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'Access Denied' })).toBeTruthy();
  });

  it('renders 404 not found state', async () => {
    (api.useGetPolicy as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: false,
      isError: true,
      error: { status: 404 }
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<PolicyDetailsScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'Policy Not Found' })).toBeTruthy();
  });

  it('renders policy details', async () => {
    (api.useGetPolicy as jest.Mock<any>).mockReturnValue({
      data: { data: { policyId: '1', policyNumber: 'POL123', status: 'ACTIVE', premium: 100, sumAssured: 1000 } },
      isLoading: false,
      isError: false,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<PolicyDetailsScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'POL123' })).toBeTruthy();
    expect(root.findByProps({ children: 'ACTIVE' })).toBeTruthy();
  });
});
