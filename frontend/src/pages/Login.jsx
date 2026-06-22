import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';

const Login = () => {
  const { login } = useAuth();
  const { t } = useLanguage();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || '/admin';

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    
    const result = await login(username, password);
    setLoading(false);
    
    if (result.success) {
      navigate(from, { replace: true });
    } else {
      setError(result.message);
    }
  };

  return (
    <div className="min-h-[70vh] flex items-center justify-center p-4">
      <div className="max-w-md w-full bg-white rounded-xl shadow-lg border border-church-gray overflow-hidden">
        {/* Top brand header */}
        <div className="bg-church-navy p-6 text-white text-center">
          <div className="w-16 h-16 bg-white rounded-full mx-auto mb-3 flex items-center justify-center border-2 border-church-navy">
            <span className="text-church-navy font-black text-xl">CN</span>
          </div>
          <h2 className="text-xl font-bold tracking-tight">Nazarene Church Remera</h2>
          <p className="text-xs text-gray-300 italic mt-0.5">{t('motto')}</p>
        </div>

        {/* Form body */}
        <form onSubmit={handleSubmit} className="p-8 space-y-6">
          <h3 className="text-lg font-bold text-church-navy text-center border-b pb-2">
            {t('adminPortal')} {t('login')}
          </h3>

          {error && (
            <div className="p-3 bg-red-50 border border-red-200 rounded text-red-700 text-xs font-semibold text-center">
              {error}
            </div>
          )}

          {location.search.includes('expired') && (
            <div className="p-3 bg-blue-50 border border-blue-200 rounded text-blue-800 text-xs font-semibold text-center">
              Session expired. Please log in again.
            </div>
          )}

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-church-navy mb-1.5">
              {t('username')} / Email
            </label>
            <input
              type="text"
              required
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-sm"
              placeholder="Enter your username or email"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-church-navy mb-1.5">
              {t('password')}
            </label>
            <input
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-sm"
              placeholder="••••••••"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full h-11 bg-church-navy text-white rounded font-bold text-sm tracking-wide hover:bg-blue-900 transition-colors flex items-center justify-center disabled:opacity-50"
          >
            {loading ? t('loading') : t('login')}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;
