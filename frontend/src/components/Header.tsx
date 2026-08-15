
import { useAuth0 } from '@auth0/auth0-react';
import { LogOut, User } from 'lucide-react';

export const Header = () => {
  const { user, logout } = useAuth0();

  return (
    <header className="topbar">
      <div></div> {/* Placeholder for left side elements like breadcrumbs */}
      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2 text-sm font-medium">
          <User size={16} className="text-muted" />
          {user?.name || user?.email}
        </div>
        <button 
          onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
          className="btn btn-secondary text-sm px-3 py-1"
          title="Logout"
        >
          <LogOut size={16} />
          Logout
        </button>
      </div>
    </header>
  );
};
