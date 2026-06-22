import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useLanguage } from '../context/LanguageContext';
import { Calendar, UserPlus, Clock, Trash2, CheckCircle, ShieldAlert, Award } from 'lucide-react';

const AdminEvents = () => {
  const { t } = useLanguage();
  const [volunteers, setVolunteers] = useState([]);
  const [members, setMembers] = useState([]);
  const [departments, setDepartments] = useState([]);
  
  // Form fields
  const [selectedMember, setSelectedMember] = useState('');
  const [selectedDept, setSelectedDept] = useState('');
  const [roleDesc, setRoleDesc] = useState('');
  const [scheduledDate, setScheduledDate] = useState('');
  const [startTime, setStartTime] = useState('09:00');
  const [endTime, setEndTime] = useState('11:30');

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [err, setErr] = useState('');

  const fetchVolunteersAndMeta = async () => {
    try {
      const volRes = await api.get('/api/admin/volunteers');
      setVolunteers(volRes.data);

      const memRes = await api.get('/api/members');
      setMembers(memRes.data);

      const deptRes = await api.get('/api/admin/departments');
      setDepartments(deptRes.data);
    } catch (e) {
      console.error("Error fetching event metadata", e);
    }
  };

  useEffect(() => {
    fetchVolunteersAndMeta();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErr('');
    setMessage('');

    try {
      await api.post(`/api/admin/volunteers?memberId=${selectedMember}&departmentId=${selectedDept}&roleDescription=${encodeURIComponent(roleDesc)}&date=${scheduledDate}&startTime=${startTime}:00&endTime=${endTime}:00`);
      setMessage('Volunteer assigned successfully!');
      setRoleDesc('');
      setSelectedMember('');
      setSelectedDept('');
      fetchVolunteersAndMeta();
    } catch (e) {
      setErr(e.response?.data?.message || 'Failed to assign volunteer');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to remove this assignment?")) return;
    try {
      await api.delete(`/api/admin/volunteers/${id}`);
      setVolunteers(volunteers.filter(v => v.id !== id));
      setMessage('Assignment removed successfully.');
    } catch (e) {
      setErr('Failed to delete assignment.');
    }
  };

  return (
    <div className="space-y-8">
      <div className="border-b pb-4">
        <h2 className="text-xl font-bold text-church-navy">Volunteer & Ministry Team Schedules</h2>
        <p className="text-xs text-gray-500">Coordinate duty rosters, choir assignments, and tech team rotations.</p>
      </div>

      {message && (
        <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-xs font-semibold rounded-lg text-center">
          {message}
        </div>
      )}

      {err && (
        <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-xs font-semibold rounded-lg text-center">
          {err}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Assignment Form */}
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm space-y-4">
          <h3 className="text-sm font-bold text-church-navy border-b pb-2 uppercase tracking-wider flex items-center gap-2">
            <UserPlus size={16} />
            Schedule Service Duty
          </h3>
          
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Select Member
              </label>
              <select
                required
                value={selectedMember}
                onChange={(e) => setSelectedMember(e.target.value)}
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs bg-white"
              >
                <option value="">-- Choose Member --</option>
                {members.map(m => (
                  <option key={m.id} value={m.id}>
                    {m.firstName} {m.lastName} ({m.phone})
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Select Ministry/Department
              </label>
              <select
                required
                value={selectedDept}
                onChange={(e) => setSelectedDept(e.target.value)}
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs bg-white"
              >
                <option value="">-- Choose Department --</option>
                {departments.map(d => (
                  <option key={d.id} value={d.id}>{d.name}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Role Description (e.g. Ushering Front Row, Lead Keyboard, Sound Mixing)
              </label>
              <input
                type="text"
                required
                value={roleDesc}
                onChange={(e) => setRoleDesc(e.target.value)}
                placeholder="Enter specific assignment details"
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs"
              />
            </div>

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Scheduled Date
              </label>
              <input
                type="date"
                required
                value={scheduledDate}
                onChange={(e) => setScheduledDate(e.target.value)}
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                  Start Time
                </label>
                <input
                  type="time"
                  required
                  value={startTime}
                  onChange={(e) => setStartTime(e.target.value)}
                  className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs"
                />
              </div>
              <div>
                <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                  End Time
                </label>
                <input
                  type="time"
                  required
                  value={endTime}
                  onChange={(e) => setEndTime(e.target.value)}
                  className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs"
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full h-10 bg-church-navy text-white rounded font-bold text-xs uppercase tracking-wide hover:bg-blue-900 transition-colors flex items-center justify-center disabled:opacity-50"
            >
              {loading ? 'Scheduling...' : 'Save Assignment'}
            </button>
          </form>
        </div>

        {/* Rotation Rosters */}
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm lg:col-span-2 space-y-4">
          <h3 className="text-sm font-bold text-church-navy border-b pb-2 uppercase tracking-wider flex items-center gap-2">
            <Calendar size={16} />
            Duty Rotation Board
          </h3>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="bg-slate-50 text-church-navy font-bold uppercase text-[10px] tracking-wider border-b border-church-gray">
                  <th className="p-3">Volunteer</th>
                  <th className="p-3">Ministry Team</th>
                  <th className="p-3">Specific Duty</th>
                  <th className="p-3">Scheduled Date</th>
                  <th className="p-3">Hours</th>
                  <th className="p-3">Status</th>
                  <th className="p-3 text-center">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {volunteers.length === 0 ? (
                  <tr>
                    <td colSpan="7" className="p-4 text-center text-gray-500 font-medium italic">
                      No service rosters scheduled. Set an assignment in the panel.
                    </td>
                  </tr>
                ) : (
                  volunteers.map((vol) => (
                    <tr key={vol.id} className="hover:bg-slate-50">
                      <td className="p-3 font-semibold text-church-navy">
                        {vol.member?.firstName} {vol.member?.lastName}
                      </td>
                      <td className="p-3">
                        <span className="bg-blue-50 text-church-blue border border-blue-100 text-[10px] font-bold px-2 py-0.5 rounded-full">
                          {vol.department?.name}
                        </span>
                      </td>
                      <td className="p-3 text-gray-600 font-medium">{vol.roleDescription}</td>
                      <td className="p-3 font-medium text-church-navy">{vol.scheduledDate}</td>
                      <td className="p-3 text-gray-500 flex items-center gap-1">
                        <Clock size={12} />
                        {vol.startTime?.substring(0,5)} - {vol.endTime?.substring(0,5)}
                      </td>
                      <td className="p-3">
                        <span className="text-[10px] font-bold uppercase text-green-700 bg-green-50 border border-green-150 px-2 py-0.5 rounded">
                          {vol.status}
                        </span>
                      </td>
                      <td className="p-3 text-center">
                        <button
                          onClick={() => handleDelete(vol.id)}
                          className="text-red-500 hover:text-red-700 p-1"
                          title="Remove assignment"
                        >
                          <Trash2 size={15} />
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminEvents;
