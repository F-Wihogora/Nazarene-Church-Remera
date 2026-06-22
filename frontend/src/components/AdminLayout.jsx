import React, { useState } from 'react';
import { Link, Outlet, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { 
  LayoutDashboard, Users, Music, Calendar, DollarSign, 
  LogOut, Menu, X, Globe, UserCheck 
} from 'lucide-react';
import Logo from './Logo';

const AdminLayout = () => {
  const { user, logout } = useAuth();
  const { lang, setLang, t } = useLanguage();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const navItems = [
    { name: t('dashboard'), path: '/admin', icon: LayoutDashboard },
    { name: t('members'), path: '/admin/members', icon: Users },
    { name: t('worshipPlan'), path: '/admin/worship', icon: Music },
    { name: t('events'), path: '/admin/events', icon: Calendar },
    { name: t('finances'), path: '/admin/finances', icon: DollarSign },
  ];

  const handleLanguageChange = (e) => {
    setLang(e.target.value);
  };

  return (
    <div className="min-h-screen flex bg-church-soft">
      {/* Mobile Sidebar Overlay */}
      {sidebarOpen && (
        <div 
          onClick={() => setSidebarOpen(false)}
          className="fixed inset-0 z-40 bg-black bg-opacity-50 md:hidden"
        />
      )}

      {/* Sidebar Panel */}
      <aside className={`
        fixed inset-y-0 left-0 z-50 w-64 bg-church-navy text-white flex flex-col transform transition-transform duration-300 ease-in-out
        ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'}
        md:relative md:translate-x-0
      `}>
        {/* Brand header */}
        <div className="h-16 flex items-center justify-between px-4 border-b border-church-navy border-opacity-30 bg-church-dark">
          <div className="flex items-center space-x-2">
            <Logo className="w-8 h-8 text-white" />
            <div>
              <span className="font-bold text-sm tracking-tight text-white block">NCR-DE Portal</span>
              <span className="text-[10px] text-gray-300 block">{t('motto')}</span>
            </div>
          </div>
          <button 
            onClick={() => setSidebarOpen(false)}
            className="text-white hover:text-blue-300 md:hidden"
          >
            <X size={20} />
          </button>
        </div>

        {/* Sidebar Nav Links */}
        <nav className="flex-1 px-2 py-4 space-y-1">
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = location.pathname === item.path;
            return (
              <Link
                key={item.name}
                to={item.path}
                onClick={() => setSidebarOpen(false)}
                className={`
                  flex items-center space-x-3 px-3 py-2.5 rounded-md text-xs font-semibold uppercase tracking-wider transition-colors
                  ${isActive 
                    ? 'bg-church-blue text-white shadow-md' 
                    : 'text-gray-300 hover:bg-church-navy hover:text-white'
                  }
                `}
              >
                <Icon size={16} />
                <span>{item.name}</span>
              </Link>
            );
          })}
        </nav>

        {/* User Card & Logout */}
        <div className="p-4 border-t border-church-blue border-opacity-20 bg-church-dark">
          <div className="flex items-center space-x-3 mb-4">
            <div className="w-9 h-9 rounded-full bg-church-blue flex items-center justify-center font-bold text-sm text-white">
              {user?.username?.substring(0, 2).toUpperCase() || 'AD'}
            </div>
            <div className="truncate">
              <span className="font-semibold text-sm text-white block truncate">{user?.username}</span>
              <span className="text-[10px] text-gray-300 block truncate">{user?.roles?.[0]}</span>
            </div>
          </div>
          
          <button
            onClick={handleLogout}
            className="w-full flex items-center justify-center space-x-2 bg-red-900 bg-opacity-35 text-red-200 py-2 rounded-md text-xs font-semibold hover:bg-red-900 transition-colors"
          >
            <LogOut size={14} />
            <span>{t('logout')}</span>
          </button>
        </div>
      </aside>

      {/* Main Panel Content Container */}
      <div className="flex-1 flex flex-col min-w-0 overflow-x-hidden">
        {/* Top Header */}
        <header className="h-16 bg-white border-b border-church-gray flex items-center justify-between px-4 md:px-8 shadow-sm">
          <div className="flex items-center space-x-3">
            <button
              onClick={() => setSidebarOpen(true)}
              className="text-church-navy focus:outline-none md:hidden"
            >
              <Menu size={24} />
            </button>
            <h1 className="text-lg font-bold text-church-navy hidden md:block">
              {t('adminPortal')}
            </h1>
          </div>

          <div className="flex items-center space-x-4">
            {/* Language Selector */}
            <div className="flex items-center space-x-1 text-church-navy">
              <Globe size={16} />
              <select
                value={lang}
                onChange={handleLanguageChange}
                className="bg-transparent text-church-navy text-xs border-none focus:ring-0 cursor-pointer font-medium"
              >
                <option value="en">English</option>
                <option value="rw">Kinyarwanda</option>
                <option value="fr">Français</option>
              </select>
            </div>
            
            <span className="w-[1px] h-6 bg-church-gray" />

            <div className="flex items-center space-x-2 text-xs font-semibold text-church-navy">
              <UserCheck size={14} />
              <span className="hidden sm:inline">Active Session</span>
            </div>
          </div>
        </header>

        {/* Content Outlet */}
        <main className="flex-grow p-4 md:p-8 overflow-y-auto">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default AdminLayout;
