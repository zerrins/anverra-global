import { Component } from 'react';
import type { ErrorInfo, ReactNode } from 'react';
import { AlertTriangle } from 'lucide-react';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error?: Error;
}

export class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false
  };

  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Uncaught error:', error, errorInfo);
  }

  public render() {
    if (this.state.hasError) {
      return (
        <div className="flex flex-col items-center justify-center min-h-screen p-4 bg-main">
          <AlertTriangle className="text-danger mb-4" size={64} />
          <h1 className="text-2xl font-bold mb-2">Something went wrong</h1>
          <p className="text-muted mb-6 max-w-md text-center">
            An unexpected error occurred. Please try refreshing the page or contact support if the problem persists.
          </p>
          <div className="bg-white p-4 rounded-md border border-border-light overflow-auto max-w-2xl w-full text-left text-sm text-danger font-mono">
            {this.state.error?.message}
          </div>
          <button
            className="btn btn-primary mt-6"
            onClick={() => window.location.reload()}
          >
            Refresh Page
          </button>
        </div>
      );
    }

    return this.props.children;
  }
}
