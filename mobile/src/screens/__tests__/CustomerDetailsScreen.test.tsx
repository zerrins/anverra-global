import { describe, it, expect, jest } from '@jest/globals';
import React from 'react';
import renderer from 'react-test-renderer';
import { CustomerDetailsScreen } from '../CustomerDetailsScreen';
import * as api from '../../api/generated/endpoints';

jest.mock('../../api/generated/endpoints', () => ({
  useGetCustomer: jest.fn(),
}));

jest.mock('@react-navigation/native', () => ({
  useRoute: () => ({
    params: { customerId: '1' }
  }),
}));

describe('CustomerDetailsScreen', () => {
  it('renders loading state', async () => {
    (api.useGetCustomer as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: true,
      isError: false,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomerDetailsScreen />);
    });
    expect(component.toJSON()).toBeTruthy();
  });

  it('renders error state', async () => {
    (api.useGetCustomer as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: false,
      isError: true,
      refetch: jest.fn(),
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomerDetailsScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'Failed to load customer details.' })).toBeTruthy();
  });

  it('renders 403 access denied state', async () => {
    (api.useGetCustomer as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: false,
      isError: true,
      error: { status: 403 }
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomerDetailsScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'Access Denied' })).toBeTruthy();
  });

  it('renders 404 not found state', async () => {
    (api.useGetCustomer as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: false,
      isError: true,
      error: { status: 404 }
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomerDetailsScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'Customer Not Found' })).toBeTruthy();
  });

  it('renders customer details', async () => {
    (api.useGetCustomer as jest.Mock<any>).mockReturnValue({
      data: { data: { id: '1', name: 'John Doe', status: 'ACTIVE', customerType: 'INDIVIDUAL', individualInfo: 'DOB 1990' } },
      isLoading: false,
      isError: false,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomerDetailsScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'John Doe' })).toBeTruthy();
    expect(root.findByProps({ children: 'ACTIVE' })).toBeTruthy();
    expect(root.findByProps({ children: 'DOB 1990' })).toBeTruthy();
  });
});
