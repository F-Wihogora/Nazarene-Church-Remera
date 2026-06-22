import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useLanguage } from '../context/LanguageContext';
import { Heart, CheckCircle, Clock, PlusCircle } from 'lucide-react';

const PrayerRequests = () => {
  const { t } = useLanguage();
  const [prayers, setPrayers] = useState([]);
  const [members, setMembers] = useState([]);
  
  // Create states
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [anonymous, setAnonymous] = useState(false);
  const [selectedMember, setSelectedMember] = useState('');

  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState('');
  const [err, setErr] = useState('');

  const fetchPrayersAndMembers = async () => {
    try {
      const prRes = await api.get('/api/admin/prayers');
      setPrayers(prRes.data);

      const memRes = await api.get('/api/members');
      setMembers(memRes.data);
    } catch (e) {
      console.error("Failed to load prayers and members", e);
    }
  };

  useEffect(() => {
    fetchPrayersAndMembers();
  }, []);

  const handleStatusChange = async (id, status) => {
    try {
      await api.put(`/api/admin/prayers/${id}/status?status=${status}`);
      setMsg(`Prayer request status updated to ${status}.`);
      fetchPrayersAndMembers();
    } catch (e) {
      setErr("Failed to update prayer request status.");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErr('');
    setMsg('');

    try {
      const payload = {
        title,
        description,
        anonymous,
      };
      if (selectedMember) {
        payload.member = { id: parseInt(selectedMember) };
      }
      await api.post('/api/admin/prayers', payload);
      setMsg("Prayer request recorded successfully.");
      setTitle('');
      setDescription('');
      setSelectedMember('');
      setAnonymous(false);
      fetchPrayersAndMembers();
    } catch (e) {
      setErr("Failed to create prayer request.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-8">
      <div className="border-b pb-4">
        <h2 className="text-xl font-bold text-church-navy">Prayer Request Module</h2>
        <p className="text-xs text-gray-500">Record, organize, and monitor prayer requests. Update request statuses as answers are received.</p>
      </div>

      {msg && (
        <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-xs font-semibold rounded-lg text-center">
          {msg}
        </div>
      )}

      {err && (
        <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-xs font-semibold rounded-lg text-center">
          {err}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Record Prayer Request */}
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm space-y-4">
          <h3 className="text-sm font-bold text-church-navy border-b pb-2 uppercase tracking-wider flex items-center gap-2">
            <PlusCircle size={16} />
            Log Prayer Request
          </h3>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Prayer Title
              </label>
              <input
                type="text"
                required
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="e.g. Healing for Sister Martha"
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs"
              />
            </div>

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Associated Member (Optional)
              </label>
              <select
                value={selectedMember}
                onChange={(e) => setSelectedMember(e.target.value)}
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs bg-white"
              >
                <option value="">-- Anonymous / General --</option>
                {members.map(m => (
                  <option key={m.id} value={m.id}>
                    {m.firstName} {m.lastName}
                  </option>
                ))}
              </select>
            </div>

            <div className="flex items-center gap-2 py-1">
              <input
                type="checkbox"
                id="anon"
                checked={anonymous}
                onChange={(e) => setAnonymous(e.target.checked)}
                className="w-4 h-4 text-church-blue border-church-gray rounded focus:ring-church-blue"
              />
              <label htmlFor="anon" className="text-xs font-semibold text-church-navy select-none">
                Submit as Anonymous
              </label>
            </div>

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Details / Description
              </label>
              <textarea
                required
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Describe the prayer request details..."
                className="w-full p-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs h-28 resize-none"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full h-10 bg-church-navy text-white rounded font-bold text-xs uppercase tracking-wide hover:bg-blue-900 transition-colors flex items-center justify-center disabled:opacity-50"
            >
              {loading ? 'Submitting...' : 'Record Request'}
            </button>
          </form>
        </div>

        {/* Requests List */}
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm lg:col-span-2 space-y-4">
          <h3 className="text-sm font-bold text-church-navy border-b pb-2 uppercase tracking-wider flex items-center gap-2">
            <Heart size={16} />
            Prayer Board & Intercession
          </h3>

          <div className="space-y-4">
            {prayers.length === 0 ? (
              <div className="p-8 text-center text-gray-500 font-medium italic">
                No active prayer requests found. Log a request in the panel.
              </div>
            ) : (
              prayers.map((pr) => (
                <div key={pr.id} className="p-4 border border-church-gray rounded-xl space-y-3 hover:border-church-blue transition-colors">
                  <div className="flex items-start justify-between">
                    <div>
                      <h4 className="font-bold text-sm text-church-navy">{pr.title}</h4>
                      <span className="text-[10px] font-semibold text-gray-400">
                        {pr.anonymous ? 'Anonymous' : pr.member ? `Requested by ${pr.member.firstName} ${pr.member.lastName}` : 'General Request'}
                        {pr.createdAt && ` | Received on ${pr.createdAt}`}
                      </span>
                    </div>

                    <span className={`text-[9px] font-bold uppercase px-2 py-0.5 rounded border ${
                      pr.status === 'ANSWERED'
                        ? 'bg-green-50 border-green-200 text-green-700'
                        : pr.status === 'UNDER_PRAYER'
                        ? 'bg-blue-50 border-blue-200 text-blue-700'
                        : 'bg-yellow-50 border-yellow-200 text-yellow-700'
                    }`}>
                      {pr.status}
                    </span>
                  </div>

                  <p className="text-xs text-gray-600 leading-relaxed bg-slate-50 p-3 rounded-lg border border-slate-100 font-medium">
                    {pr.description}
                  </p>

                  <div className="flex items-center gap-2 pt-1 border-t border-slate-50 text-[10px]">
                    <span className="font-bold text-church-navy uppercase tracking-wider">Update Status:</span>
                    <button
                      onClick={() => handleStatusChange(pr.id, 'PENDING')}
                      className="bg-yellow-50 text-yellow-700 border border-yellow-250 px-2 py-0.5 rounded uppercase font-bold hover:bg-yellow-100 transition-colors"
                    >
                      Pending
                    </button>
                    <button
                      onClick={() => handleStatusChange(pr.id, 'UNDER_PRAYER')}
                      className="bg-blue-50 text-blue-700 border border-blue-250 px-2 py-0.5 rounded uppercase font-bold hover:bg-blue-100 transition-colors flex items-center gap-0.5"
                    >
                      <Clock size={10} />
                      Under Prayer
                    </button>
                    <button
                      onClick={() => handleStatusChange(pr.id, 'ANSWERED')}
                      className="bg-green-600 text-white px-2 py-0.5 rounded uppercase font-bold hover:bg-green-700 transition-colors flex items-center gap-0.5"
                    >
                      <CheckCircle size={10} />
                      Answered!
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PrayerRequests;
