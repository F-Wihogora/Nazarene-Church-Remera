import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { LanguageProvider } from './context/LanguageContext';
import ProtectedRoute from './components/ProtectedRoute';
import PublicLayout from './components/PublicLayout';
import AdminLayout from './components/AdminLayout';
import api from './services/api';


// Pages
import Home from './pages/Home';
import Login from './pages/Login';
import Forbidden from './pages/Forbidden';
import MemberManagement from './pages/MemberManagement';
import WorshipPlanner from './pages/WorshipPlanner';
import AdminEvents from './pages/AdminEvents';
import AdminFinances from './pages/AdminFinances';
import VisitorManagement from './pages/VisitorManagement';
import PrayerRequests from './pages/PrayerRequests';

// Stub pages to prevent routing errors
const About = () => (
  <div className="max-w-5xl mx-auto px-4 py-16 space-y-6">
    <h1 className="text-3xl font-extrabold text-church-navy border-b pb-2">About Our Church</h1>
    <p className="text-sm text-gray-600 leading-relaxed">
      The Church of the Nazarene is a global community of faith commissioned to take the Good News of life in Christ to people everywhere. Our local branch at Remera, Kigali, focuses on teaching biblical holiness, fostering family unity, and reaching out to the vulnerable through compassion.
    </p>
  </div>
);

const Services = () => (
  <div className="max-w-5xl mx-auto px-4 py-16 space-y-6">
    <h1 className="text-3xl font-extrabold text-church-navy border-b pb-2">Weekly Services</h1>
    <p className="text-sm text-gray-600">Join us in praise, prayers, and study sessions:</p>
    <ul className="space-y-4">
      <li className="bg-white p-4 rounded border border-church-gray">
        <span className="font-bold text-church-navy block text-base">Sunday Celebration Service</span>
        <span className="text-xs text-gray-500">Every Sunday, 09:00 AM - 11:30 AM | Sanctuary & Online</span>
      </li>
      <li className="bg-white p-4 rounded border border-church-gray">
        <span className="font-bold text-church-navy block text-base">Youth Fellowship (NYI)</span>
        <span className="text-xs text-gray-500">Every Saturday, 04:00 PM - 06:00 PM | Fellowship Hall</span>
      </li>
      <li className="bg-white p-4 rounded border border-church-gray">
        <span className="font-bold text-church-navy block text-base">Overnight Prayer Service</span>
        <span className="text-xs text-gray-500">Every Friday, 10:00 PM - 04:00 AM | Sanctuary</span>
      </li>
    </ul>
  </div>
);

const Sermons = () => (
  <div className="max-w-5xl mx-auto px-4 py-16 space-y-6">
    <h1 className="text-3xl font-extrabold text-church-navy border-b pb-2">Sermons Archive</h1>
    <p className="text-sm text-gray-600">Watch or listen to our recent gospel teachings:</p>
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div className="bg-white rounded border border-church-gray overflow-hidden">
        <div className="aspect-video bg-black flex items-center justify-center text-white text-xs">Video Player Mockup</div>
        <div className="p-4">
          <span className="text-xs font-semibold text-church-blue block">Sermon - June 2026</span>
          <h2 className="font-bold text-church-navy text-base mt-1">Living in Christian Holiness</h2>
          <p className="text-xs text-gray-500 mt-1">Preacher: Rev. Jean Kabera | Scripture: Romans 12:1-2</p>
        </div>
      </div>
    </div>
  </div>
);

const Events = () => (
  <div className="max-w-5xl mx-auto px-4 py-16 space-y-6">
    <h1 className="text-3xl font-extrabold text-church-navy border-b pb-2">Upcoming Events</h1>
    <p className="text-sm text-gray-600">Stay up to date with church programs and department activities.</p>
  </div>
);

const JoinUs = () => (
  <div className="max-w-5xl mx-auto px-4 py-16 space-y-6">
    <h1 className="text-3xl font-extrabold text-church-navy border-b pb-2">Join Our Family</h1>
    <p className="text-sm text-gray-600">Are you a visitor or newcomer? Fill out this card so we can welcome you formally.</p>
    <form className="max-w-md bg-white p-6 rounded border border-church-gray space-y-4">
      <div>
        <label className="block text-xs font-bold uppercase text-church-navy mb-1">First Name</label>
        <input type="text" required className="w-full h-10 px-3 border rounded text-sm focus:outline-none" />
      </div>
      <div>
        <label className="block text-xs font-bold uppercase text-church-navy mb-1">Last Name</label>
        <input type="text" required className="w-full h-10 px-3 border rounded text-sm focus:outline-none" />
      </div>
      <div>
        <label className="block text-xs font-bold uppercase text-church-navy mb-1">Email</label>
        <input type="email" required className="w-full h-10 px-3 border rounded text-sm focus:outline-none" />
      </div>
      <div>
        <label className="block text-xs font-bold uppercase text-church-navy mb-1">Phone</label>
        <input type="text" required className="w-full h-10 px-3 border rounded text-sm focus:outline-none" />
      </div>
      <button type="submit" className="w-full h-10 bg-church-navy text-white font-semibold text-sm rounded hover:bg-blue-900">
        Submit Visitor Card
      </button>
    </form>
  </div>
);

// Admin Pages
const AdminDashboard = () => {
  const [stats, setStats] = React.useState({ members: 0, prayers: 0, visitors: 0, offerings: 0 });
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const fetchStats = async () => {
      try {
        const [mRes, pRes, vRes, fRes] = await Promise.all([
          api.get('/api/members'),
          api.get('/api/admin/prayers'),
          api.get('/api/admin/visitors'),
          api.get('/api/admin/finances')
        ]);
        
        const pendingPrayers = pRes.data.filter(p => p.status === 'PENDING').length;
        const totalOfferings = fRes.data
          .filter(f => f.recordType === 'INCOME' && f.category === 'OFFERING')
          .reduce((sum, f) => sum + f.amount, 0);

        setStats({
          members: mRes.data.length,
          prayers: pendingPrayers,
          visitors: vRes.data.length,
          offerings: totalOfferings
        });
      } catch (e) {
        console.error("Failed to load dashboard stats", e);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  return (
    <div className="space-y-6">
      <h2 className="text-xl font-bold text-church-navy border-b pb-2 font-sans tracking-wide">Dashboard Incamake</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm flex flex-col justify-between">
          <span className="text-xs font-bold text-gray-500 uppercase tracking-wider">Total Members</span>
          <span className="text-2xl font-black text-church-navy mt-2">{loading ? '...' : stats.members}</span>
        </div>
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm flex flex-col justify-between">
          <span className="text-xs font-bold text-gray-500 uppercase tracking-wider">Pending Prayers</span>
          <span className="text-2xl font-black text-amber-600 mt-2">{loading ? '...' : `${stats.prayers} Request(s)`}</span>
        </div>
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm flex flex-col justify-between">
          <span className="text-xs font-bold text-gray-500 uppercase tracking-wider">Registered Visitors</span>
          <span className="text-2xl font-black text-church-blue mt-2">{loading ? '...' : stats.visitors}</span>
        </div>
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm flex flex-col justify-between">
          <span className="text-xs font-bold text-gray-500 uppercase tracking-wider">Total Offerings</span>
          <span className="text-2xl font-black text-green-600 mt-2">{loading ? '...' : `${stats.offerings.toLocaleString()} RWF`}</span>
        </div>
      </div>
    </div>
  );
};



function App() {
  return (
    <Router>
      <LanguageProvider>
        <AuthProvider>
          <Routes>
            {/* Public Outreach Website Layout */}
            <Route path="/" element={<PublicLayout />}>
              <Route index element={<Home />} />
              <Route path="about" element={<About />} />
              <Route path="services" element={<Services />} />
              <Route path="sermons" element={<Sermons />} />
              <Route path="events" element={<Events />} />
              <Route path="join" element={<JoinUs />} />
              <Route path="login" element={<Login />} />
            </Route>

            {/* Secure Admin Portal Layout */}
            <Route path="/admin" element={
              <ProtectedRoute>
                <AdminLayout />
              </ProtectedRoute>
            }>
              <Route index element={<AdminDashboard />} />
              <Route path="members" element={<MemberManagement />} />
              <Route path="worship" element={<WorshipPlanner />} />
              <Route path="events" element={<AdminEvents />} />
              <Route path="visitors" element={<VisitorManagement />} />
              <Route path="prayers" element={<PrayerRequests />} />
              <Route path="finances" element={<AdminFinances />} />
            </Route>

            {/* Error Pages */}
            <Route path="/forbidden" element={<Forbidden />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </AuthProvider>
      </LanguageProvider>
    </Router>
  );
}

export default App;
