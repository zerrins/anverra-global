
import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { AppShell } from '../components/AppShell';
import { ProtectedRoute } from '../components/ProtectedRoute';
import { AccessDenied } from '../components/AccessDenied';
import { ErrorBoundary } from '../components/ErrorBoundary';

// Lazy load pages
const Dashboard = React.lazy(() => import('../pages/Dashboard'));
const PolicyList = React.lazy(() => import('../pages/PolicyList'));
const PolicyDetails = React.lazy(() => import('../pages/PolicyDetails'));
const ReportingDashboard = React.lazy(() => import('../pages/ReportingDashboard'));
const CustomerList = React.lazy(() => import('../pages/CustomerList'));
const CustomerDetails = React.lazy(() => import('../pages/CustomerDetails'));
const InsurerList = React.lazy(() => import('../pages/InsurerList'));
const LoadingFallback = () => (
  <div className="flex items-center justify-center h-full w-full py-12">
    <div className="spinner spinner-md"></div>
  </div>
);

const router = createBrowserRouter([
  {
    path: '/',
    element: <ProtectedRoute />,
    errorElement: (
      <ErrorBoundary>
        <div className="p-4">Error loading route</div>
      </ErrorBoundary>
    ),
    children: [
      {
        element: <AppShell />,
        children: [
          {
            index: true,
            element: (
              <React.Suspense fallback={<LoadingFallback />}>
                <Dashboard />
              </React.Suspense>
            ),
          },
          {
            path: 'policies',
            element: (
              <React.Suspense fallback={<LoadingFallback />}>
                <PolicyList />
              </React.Suspense>
            ),
          },
          {
            path: 'policies/:id',
            element: (
              <React.Suspense fallback={<LoadingFallback />}>
                <PolicyDetails />
              </React.Suspense>
            ),
          },
          {
            path: 'reporting',
            element: (
              <React.Suspense fallback={<LoadingFallback />}>
                <ReportingDashboard />
              </React.Suspense>
            ),
          },
          {
            path: 'customers',
            element: (
              <React.Suspense fallback={<LoadingFallback />}>
                <CustomerList />
              </React.Suspense>
            ),
          },
          {
            path: 'customers/:id',
            element: (
              <React.Suspense fallback={<LoadingFallback />}>
                <CustomerDetails />
              </React.Suspense>
            ),
          },
          {
            path: 'insurers',
            element: (
              <React.Suspense fallback={<LoadingFallback />}>
                <InsurerList />
              </React.Suspense>
            ),
          },
        ],
      },
      {
        path: 'access-denied',
        element: <AccessDenied />,
      },
    ],
  },
]);

export const AppRouter = () => {
  return <RouterProvider router={router} />;
};
