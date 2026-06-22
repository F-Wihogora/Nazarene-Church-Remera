import React from 'react';
import { Link } from 'react-router-dom';
import { ShieldAlert } from 'lucide-react';

const Forbidden = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-church-soft p-4">
      <div className="max-w-md w-full text-center bg-white p-8 rounded-lg shadow-md border border-red-100">
        <div className="flex justify-center mb-4">
          <ShieldAlert size={64} className="text-red-600 animate-pulse" />
        </div>
        <h1 className="text-2xl font-bold text-church-navy mb-2">403 - Access Denied</h1>
        <p className="text-gray-600 text-sm mb-6">
          You do not have the required role privileges to access this administrative module. Please contact the system administrator.
        </p>
        <Link 
          to="/" 
          className="inline-block bg-church-navy text-white px-6 py-2 rounded-md text-sm font-semibold hover:bg-blue-900 transition-colors"
        >
          Return to Homepage
        </Link>
      </div>
    </div>
  );
};

export default Forbidden;
