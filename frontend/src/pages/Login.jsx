import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import Logo from '../components/Logo';

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

  const selectAccount = (userVal) => {
    setUsername(userVal);
    setPassword('password');
  };

  const testAccounts = [
    { name: "Rev. Jean Kabera (Pastor)", user: "jean_kabera" },
    { name: "Pastor Alice Mutoni (Secretary)", user: "alice_mutoni" },
    { name: "Pastor Floribert Wihogora (Admin/Pastor)", user: "floribert_wihogora" },
    { name: "Sister Mutoni (Youth/Choir)", user: "sister_mutoni" }
  ];

  return (
    <div className="min-h-[85vh] flex flex-col md:flex-row items-center justify-center p-4 gap-8 max-w-5xl mx-auto">
      {/* Login Card */}
      <div className="max-w-md w-full bg-white rounded-xl shadow-lg border border-church-gray overflow-hidden">
        {/* Top brand header */}
        <div className="bg-church-navy p-6 text-white text-center flex flex-col items-center">
          <Logo className="w-16 h-16 text-white mb-2" />
          <h2 className="text-base font-bold tracking-tight uppercase">Nazarene Church Remera</h2>
          <p className="text-[10px] text-gray-300 italic mt-0.5">{t('motto')}</p>
        </div>

        {/* Form body */}
        <form onSubmit={handleSubmit} className="p-8 space-y-6">
          <h3 className="text-sm font-bold text-church-navy text-center border-b pb-2 uppercase tracking-wider">
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
              className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs"
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
              className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs"
              placeholder="••••••••"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full h-11 bg-church-navy text-white rounded font-bold text-xs uppercase tracking-wide hover:bg-blue-900 transition-colors flex items-center justify-center disabled:opacity-50"
          >
            {loading ? t('loading') : t('login')}
          </button>
        </form>
      </div>

      {/* Test Accounts Directory helper */}
      <div className="w-full max-w-sm bg-white rounded-xl shadow border border-church-gray p-6 space-y-4">
        <h3 className="text-sm font-bold text-church-navy border-b pb-2 uppercase tracking-wider">
          Pastors & Leaders directory
        </h3>
        <p className="text-[11px] text-gray-500">
          Click any leader profile to automatically populate credentials and test their specific authorization views:
        </p>
        <div className="space-y-2">
          {testAccounts.map((acc) => (
            <button
              key={acc.user}
              onClick={() => selectAccount(acc.user)}
              className="w-full text-left p-3 rounded border border-church-gray hover:border-church-blue hover:bg-blue-50 hover:bg-opacity-30 transition-all text-xs font-medium text-church-navy flex items-center justify-between"
            >
              <span>{acc.name}</span>
              <span className="text-[10px] text-gray-400 font-mono">{acc.user}</span>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Login;
