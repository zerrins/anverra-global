import { describe, it, expect, jest } from '@jest/globals';
import React from 'react';
import renderer from 'react-test-renderer';
import { CustomersScreen } from '../CustomersScreen';
import * as hooks from '../../api/hooks/useMobileListCustomers';

jest.mock('../../api/hooks/useMobileListCustomers', () => ({
  useMobileListCustomers: jest.fn(),
}));

jest.mock('@react-navigation/native', () => ({
  useNavigation: () => ({
    navigate: jest.fn(),
  }),
}));

describe('CustomersScreen', () => {
  it('renders loading state', async () => {
    (hooks.useMobileListCustomers as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: true,
      isError: false,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomersScreen />);
    });
    expect(component.toJSON()).toBeTruthy();
  });

  it('renders error state', async () => {
    (hooks.useMobileListCustomers as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: false,
      isError: true,
      refetch: jest.fn(),
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomersScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'Failed to load customers. Please try again.' })).toBeTruthy();
  });

  it('renders empty list state', async () => {
    (hooks.useMobileListCustomers as jest.Mock<any>).mockReturnValue({
      data: { data: { content: [] } },
      isLoading: false,
      isError: false,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomersScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'No customers found.' })).toBeTruthy();
  });

  it('renders 403 access denied state', async () => {
    (hooks.useMobileListCustomers as jest.Mock<any>).mockReturnValue({
      data: null,
      isLoading: false,
      isError: true,
      error: { status: 403 }
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomersScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'Access Denied' })).toBeTruthy();
  });

  it('renders customers with pagination and search', async () => {
    (hooks.useMobileListCustomers as jest.Mock<any>).mockReturnValue({
      data: { data: { content: [{ id: '1', name: 'John Doe', status: 'ACTIVE', customerType: 'INDIVIDUAL' }], totalPages: 2, first: true, last: false } },
      isLoading: false,
      isError: false,
    });

    let component: any;
    await renderer.act(async () => {
      component = renderer.create(<CustomersScreen />);
    });
    const root = component.root;
    expect(root.findByProps({ children: 'John Doe' })).toBeTruthy();
    expect(root.findByProps({ children: 'Next' })).toBeTruthy();
    expect(root.findByProps({ placeholder: 'Search by name...' })).toBeTruthy();
  });
});
