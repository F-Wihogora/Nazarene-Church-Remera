import React, { useState } from 'react';
import { Link, Outlet, useNavigate } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { useAuth } from '../context/AuthContext';
import { Menu, X, Globe, User } from 'lucide-react';
import Logo from './Logo';

const PublicLayout = () => {
  const { lang, setLang, t } = useLanguage();
  const { user, isAuthenticated, logout } = useAuth();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const navigate = useNavigate();

  const handleLanguageChange = (e) => {
    setLang(e.target.value);
  };

  return (
    <div className="min-h-screen flex flex-col bg-church-soft">
      {/* Header / Navbar */}
      <header className="sticky top-0 z-50 bg-church-navy text-white shadow-md backdrop-blur-md bg-opacity-95 border-b border-church-navy border-opacity-30">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            {/* Brand Logo & Name */}
            <div className="flex items-center space-x-3">
              <Logo className="w-10 h-10 text-white" />
              <div>
                <Link to="/" className="text-sm font-extrabold tracking-tight text-white block uppercase">
                  Nazarene Church Remera
                </Link>
                <span className="text-[10px] text-gray-300 block -mt-1 font-medium italic">
                  {t('motto')}
                </span>
              </div>
            </div>

            {/* Desktop Navigation Links */}
            <nav className="hidden md:flex space-x-6 text-xs font-semibold uppercase tracking-wider">
              <Link to="/" className="hover:text-blue-300 transition-colors">{t('home')}</Link>
              <Link to="/about" className="hover:text-blue-300 transition-colors">{t('about')}</Link>
              <Link to="/services" className="hover:text-blue-300 transition-colors">{t('services')}</Link>
              <Link to="/sermons" className="hover:text-blue-300 transition-colors">{t('sermons')}</Link>
              <Link to="/events" className="hover:text-blue-300 transition-colors">{t('events')}</Link>
              <Link to="/join" className="hover:text-blue-300 transition-colors">{t('joinUs')}</Link>
            </nav>

            {/* Language Switcher & Auth Section */}
            <div className="hidden md:flex items-center space-x-4">
              <div className="flex items-center space-x-1 text-gray-300">
                <Globe size={16} />
                <select
                  value={lang}
                  onChange={handleLanguageChange}
                  className="bg-transparent text-white text-xs border-none focus:ring-0 cursor-pointer"
                >
                  <option value="en" className="text-black">English</option>
                  <option value="rw" className="text-black">Kinyarwanda</option>
                  <option value="fr" className="text-black">Français</option>
                </select>
              </div>

              {isAuthenticated() ? (
                <div className="flex items-center space-x-3">
                  <Link
                    to="/admin"
                    className="flex items-center space-x-1 bg-church-blue text-white px-3 h-9 rounded-md text-xs font-semibold hover:bg-blue-700 transition-colors"
                  >
                    <User size={14} />
                    <span>{t('adminPortal')}</span>
                  </Link>
                  <button
                    onClick={() => { logout(); navigate('/'); }}
                    className="text-xs text-gray-300 hover:text-white transition-colors"
                  >
                    {t('logout')}
                  </button>
                </div>
              ) : (
                <Link
                  to="/login"
                  className="bg-white text-church-navy px-4 h-9 rounded-md text-xs font-semibold hover:bg-gray-100 transition-colors flex items-center justify-center"
                >
                  {t('login')}
                </Link>
              )}
            </div>

            {/* Mobile Menu Button */}
            <div className="md:hidden flex items-center space-x-3">
              <select
                value={lang}
                onChange={handleLanguageChange}
                className="bg-transparent text-white text-xs border border-white border-opacity-30 rounded px-1 py-0.5 focus:ring-0 cursor-pointer"
              >
                <option value="en" className="text-black">EN</option>
                <option value="rw" className="text-black">RW</option>
                <option value="fr" className="text-black">FR</option>
              </select>

              <button
                onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                className="text-white hover:text-blue-300 focus:outline-none"
              >
                {mobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile Navigation Menu */}
        {mobileMenuOpen && (
          <div className="md:hidden bg-church-navy border-t border-church-blue border-opacity-20 px-2 pt-2 pb-4 space-y-1">
            <Link
              to="/"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium hover:bg-church-blue"
            >
              {t('home')}
            </Link>
            <Link
              to="/about"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium hover:bg-church-blue"
            >
              {t('about')}
            </Link>
            <Link
              to="/services"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium hover:bg-church-blue"
            >
              {t('services')}
            </Link>
            <Link
              to="/sermons"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium hover:bg-church-blue"
            >
              {t('sermons')}
            </Link>
            <Link
              to="/events"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium hover:bg-church-blue"
            >
              {t('events')}
            </Link>
            <Link
              to="/join"
              onClick={() => setMobileMenuOpen(false)}
              className="block px-3 py-2 rounded-md text-base font-medium hover:bg-church-blue"
            >
              {t('joinUs')}
            </Link>
            <hr className="border-church-blue border-opacity-30 my-2" />
            {isAuthenticated() ? (
              <>
                <Link
                  to="/admin"
                  onClick={() => setMobileMenuOpen(false)}
                  className="block px-3 py-2 rounded-md text-base font-medium bg-church-blue text-center text-white"
                >
                  {t('adminPortal')}
                </Link>
                <button
                  onClick={() => { logout(); setMobileMenuOpen(false); navigate('/'); }}
                  className="w-full text-center block px-3 py-2 rounded-md text-base font-medium hover:bg-red-800 text-red-300 mt-1"
                >
                  {t('logout')}
                </button>
              </>
            ) : (
              <Link
                to="/login"
                onClick={() => setMobileMenuOpen(false)}
                className="block px-3 py-2 rounded-md text-base font-medium bg-white text-church-navy text-center"
              >
                {t('login')}
              </Link>
            )}
          </div>
        )}
      </header>

      {/* Main Page Content */}
      <main className="flex-grow">
        <Outlet />
      </main>

      {/* Footer */}
      <footer className="bg-church-dark text-white border-t border-church-navy py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="space-y-3">
              <div className="flex items-center space-x-2">
                <Logo className="w-8 h-8 text-white" />
                <h3 className="text-base font-bold">Nazarene Church Remera</h3>
              </div>
              <p className="text-gray-400 text-xs italic">"{t('motto')}"</p>
              <p className="text-gray-400 text-xs mt-3">Kigali, Rwanda</p>
            </div>
            <div>
              <h4 className="text-xs font-semibold text-gray-300 uppercase tracking-wider mb-3">Links</h4>
              <ul className="space-y-2 text-xs text-gray-400">
                <li><Link to="/about" className="hover:text-white transition-colors">{t('about')}</Link></li>
                <li><Link to="/services" className="hover:text-white transition-colors">{t('services')}</Link></li>
                <li><Link to="/sermons" className="hover:text-white transition-colors">{t('sermons')}</Link></li>
                <li><Link to="/events" className="hover:text-white transition-colors">{t('events')}</Link></li>
              </ul>
            </div>
            <div>
              <h4 className="text-xs font-semibold text-gray-300 uppercase tracking-wider mb-3">Office Contacts</h4>
              <p className="text-xs text-gray-400">Email: info@nazareneremera.org</p>
              <p className="text-xs text-gray-400">Phone: +250 788 000 000</p>
              <p className="text-xs text-gray-400 mt-3">&copy; {new Date().getFullYear()} Nazarene Church Remera Digital Ecosystem (NCR-DE).</p>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default PublicLayout;
