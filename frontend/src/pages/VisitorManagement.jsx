import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useLanguage } from '../context/LanguageContext';
import { Users, UserCheck, Heart, Phone, Mail, Award, CheckCircle } from 'lucide-react';

const VisitorManagement = () => {
  const { t } = useLanguage();
  const [visitors, setVisitors] = useState([]);
  const [msg, setMsg] = useState('');
  const [err, setErr] = useState('');
  const [loading, setLoading] = useState(false);

  const fetchVisitors = async () => {
    try {
      const res = await api.get('/api/admin/visitors');
      setVisitors(res.data);
    } catch (e) {
      console.error("Failed to load visitors list", e);
    }
  };

  useEffect(() => {
    fetchVisitors();
  }, []);

  const handleFollowUp = async (id, status) => {
    try {
      await api.put(`/api/admin/visitors/${id}/followup?status=${status}`);
      setMsg(`Visitor follow-up status updated to ${status}.`);
      fetchVisitors();
    } catch (e) {
      setErr("Failed to update status.");
    }
  };

  const handleConvert = async (id) => {
    if (!window.confirm("Convert this visitor into an active church Member?")) return;
    setLoading(true);
    setErr('');
    setMsg('');

    try {
      await api.post(`/api/admin/visitors/${id}/convert`);
      setMsg("Visitor successfully converted to active Member!");
      fetchVisitors();
    } catch (e) {
      setErr("Failed to convert visitor to member.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-8">
      <div className="border-b pb-4">
        <h2 className="text-xl font-bold text-church-navy">Visitor & Newcomer Management</h2>
        <p className="text-xs text-gray-500">Log first-time visitors, follow up on their conversion to members, and view invitations.</p>
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

      <div className="bg-white rounded-xl border border-church-gray shadow-sm overflow-hidden">
        <div className="p-6 border-b border-church-gray bg-slate-50 flex items-center justify-between">
          <h3 className="text-sm font-bold text-church-navy uppercase tracking-wider flex items-center gap-2">
            <Users size={16} />
            Visitors Registry
          </h3>
          <span className="text-[10px] font-bold bg-church-navy text-white px-2.5 py-1 rounded-full uppercase tracking-wider">
            {visitors.length} Registered
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-slate-50 text-church-navy font-bold uppercase text-[10px] tracking-wider border-b border-church-gray">
                <th className="p-4">Visitor Name</th>
                <th className="p-4">Contacts</th>
                <th className="p-4">Visit Date</th>
                <th className="p-4">Invited By</th>
                <th className="p-4">Follow-up Status</th>
                <th className="p-4 text-center">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {visitors.length === 0 ? (
                <tr>
                  <td colSpan="6" className="p-8 text-center text-gray-500 font-medium italic">
                    No visitor records registered yet. Visitors are added when they join via the public page guest card.
                  </td>
                </tr>
              ) : (
                visitors.map((v) => (
                  <tr key={v.id} className="hover:bg-slate-50">
                    <td className="p-4 font-bold text-church-navy">
                      {v.firstName} {v.lastName}
                    </td>
                    <td className="p-4 space-y-1">
                      <div className="flex items-center gap-1.5 text-gray-600 font-medium">
                        <Phone size={12} className="text-gray-400" />
                        {v.phone || 'No Phone'}
                      </div>
                      <div className="flex items-center gap-1.5 text-gray-600">
                        <Mail size={12} className="text-gray-400" />
                        {v.email || 'No Email'}
                      </div>
                    </td>
                    <td className="p-4 font-medium text-gray-600">{v.visitDate}</td>
                    <td className="p-4 font-medium text-church-blue">{v.invitedBy || 'Walk-in'}</td>
                    <td className="p-4">
                      <span className={`text-[10px] font-bold uppercase px-2 py-0.5 rounded border ${
                        v.followUpStatus === 'CONVERTED'
                          ? 'bg-green-50 border-green-200 text-green-700'
                          : v.followUpStatus === 'CONTACTED'
                          ? 'bg-blue-50 border-blue-200 text-blue-700'
                          : 'bg-yellow-50 border-yellow-200 text-yellow-700'
                      }`}>
                        {v.followUpStatus}
                      </span>
                    </td>
                    <td className="p-4 text-center">
                      <div className="flex justify-center items-center gap-2">
                        {v.followUpStatus !== 'CONVERTED' && (
                          <>
                            <button
                              onClick={() => handleFollowUp(v.id, 'CONTACTED')}
                              className="bg-blue-50 text-blue-700 border border-blue-200 px-2 py-1 rounded text-[10px] font-bold uppercase hover:bg-blue-100 transition-colors"
                            >
                              Mark Contacted
                            </button>
                            <button
                              onClick={() => handleConvert(v.id)}
                              disabled={loading}
                              className="bg-green-600 text-white px-2 py-1 rounded text-[10px] font-bold uppercase hover:bg-green-700 transition-colors flex items-center gap-1"
                            >
                              <UserCheck size={12} />
                              Convert to Member
                            </button>
                          </>
                        )}
                        {v.followUpStatus === 'CONVERTED' && (
                          <span className="text-green-700 text-[10px] font-bold uppercase flex items-center gap-1">
                            <CheckCircle size={12} />
                            Full Member
                          </span>
                        )}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default VisitorManagement;
