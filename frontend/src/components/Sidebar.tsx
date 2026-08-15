
import { NavLink } from 'react-router-dom';
import { LayoutDashboard, FileText, PieChart } from 'lucide-react';

export const Sidebar = () => {
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
          to="/reporting" 
          className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
        >
          <PieChart size={20} />
          Reporting
        </NavLink>
      </nav>
    </aside>
  );
};
