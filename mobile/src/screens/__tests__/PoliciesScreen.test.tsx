import { describe, it, expect, jest } from '@jest/globals';
import React from 'react';
import renderer from 'react-test-renderer';
import { PoliciesScreen } from '../PoliciesScreen';
import * as api from '../../api/generated/endpoints';

jest.mock('../../api/generated/endpoints', () => ({
  useListPolicies: jest.fn(),
}));

jest.mock('@react-navigation/native', () => ({
  useNavigation: () => ({
    navigate: jest.fn(),
  }),
}));

describe('PoliciesScreen', () => {
  it('renders loading state', async () => {
    (api.useListPolicies as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: true,
      isError: false,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<PoliciesScreen />);
    });
    expect(component.toJSON()).toBeTruthy();
  });

  it('renders error state', async () => {
    (api.useListPolicies as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: false,
      isError: true,
      refetch: jest.fn(),
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<PoliciesScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'Failed to load policies. Please try again.' })).toBeTruthy();
  });

  it('renders empty list state', async () => {
    (api.useListPolicies as jest.Mock<any>).mockReturnValue({
      data: { data: { content: [] } },
      isLoading: false,
      isError: false,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<PoliciesScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'No policies found.' })).toBeTruthy();
  });
});
