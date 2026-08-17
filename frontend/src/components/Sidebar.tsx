
import { NavLink } from 'react-router-dom';
import { LayoutDashboard, FileText, PieChart, Users, Shield } from 'lucide-react';
import { useRole } from '../auth/useRole';

export const Sidebar = () => {
  const { isAdmin } = useRole();

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <span className="text-secondary">Anverra</span> Global
      </div>
      <nav className="sidebar-nav flex-col">
        <NavLink
          to="/"
          end
          className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
        >
          <LayoutDashboard size={20} />
          Dashboard
        </NavLink>
        <NavLink
          to="/policies"
          className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
        >
          <FileText size={20} />
          Policies
        </NavLink>
        <NavLink
          to="/customers"
          className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
        >
          <Users size={20} />
          Customers
        </NavLink>
        <NavLink
          to="/reporting"
          className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
        >
          <PieChart size={20} />
          Reporting
        </NavLink>
        {isAdmin && (
          <>
            <NavLink
              to="/products"
              className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
            >
              <FileText size={20} />
              Products
            </NavLink>
            <NavLink
              to="/insurers"
              className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
            >
              <Shield size={20} />
              Insurers
            </NavLink>
          </>
        )}
      </nav>
    </aside>
  );
};
