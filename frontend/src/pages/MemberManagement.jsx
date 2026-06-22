import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { 
  UserPlus, Search, Edit3, Trash2, Link2, 
  Check, X, Users, RefreshCw 
} from 'lucide-react';

const MemberManagement = () => {
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Search & Filter State
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('');
  const [status, setStatus] = useState('');

  // Form State
  const [showForm, setShowForm] = useState(false);
  const [editId, setEditId] = useState(null);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    gender: 'MALE',
    dob: '',
    address: '',
    status: 'ACTIVE',
    category: 'ADULT',
    campusCode: 'REMERA',
    familyId: '',
    familyRole: 'HEAD_OF_HOUSEHOLD'
  });

  // Family linking modal state
  const [linkMemberId, setLinkMemberId] = useState(null);
  const [linkFamilyId, setLinkFamilyId] = useState('');
  const [linkFamilyRole, setLinkFamilyRole] = useState('HEAD_OF_HOUSEHOLD');

  useEffect(() => {
    fetchMembers();
  }, [search, category, status]);

  const fetchMembers = async () => {
    setLoading(true);
    setError('');
    try {
      const params = {};
      if (search) params.search = search;
      if (category) params.category = category;
      if (status) params.status = status;
      
      const response = await api.get('/api/members', { params });
      setMembers(response.data);
    } catch (err) {
      console.error("Failed to load members:", err);
      setError("Failed to fetch members directory. Check backend authorization.");
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleOpenCreate = () => {
    setEditId(null);
    setFormData({
      firstName: '',
      lastName: '',
      email: '',
      phone: '',
      gender: 'MALE',
      dob: '',
      address: '',
      status: 'ACTIVE',
      category: 'ADULT',
      campusCode: 'REMERA',
      familyId: '',
      familyRole: 'HEAD_OF_HOUSEHOLD'
    });
    setShowForm(true);
  };

  const handleOpenEdit = (member) => {
    setEditId(member.id);
    setFormData({
      firstName: member.firstName || '',
      lastName: member.lastName || '',
      email: member.email || '',
      phone: member.phone || '',
      gender: member.gender || 'MALE',
      dob: member.dob || '',
      address: member.address || '',
      status: member.status || 'ACTIVE',
      category: member.category || 'ADULT',
      campusCode: member.campusCode || 'REMERA',
      familyId: member.familyId || '',
      familyRole: member.familyRole || 'HEAD_OF_HOUSEHOLD'
    });
    setShowForm(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    
    // Clean up empty IDs
    const payload = { ...formData };
    if (!payload.familyId) {
      delete payload.familyId;
      delete payload.familyRole;
    }

    try {
      if (editId) {
        await api.put(`/api/members/${editId}`, payload);
        setSuccess("Member updated successfully.");
      } else {
        await api.post('/api/members', payload);
        setSuccess("Member registered successfully.");
      }
      setShowForm(false);
      fetchMembers();
    } catch (err) {
      setError(err.response?.data?.message || "Operation failed. Email may already be in use.");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to soft-delete this member profile?")) return;
    setError('');
    setSuccess('');
    try {
      await api.delete(`/api/members/${id}`);
      setSuccess("Member archived successfully.");
      fetchMembers();
    } catch (err) {
      setError("Failed to archive member. Required: Admin permissions.");
    }
  };

  const handleLinkFamily = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      await api.post(`/api/members/${linkMemberId}/link-family`, null, {
        params: {
          familyId: linkFamilyId || null,
          familyRole: linkFamilyRole
        }
      });
      setSuccess("Family link updated successfully.");
      setLinkMemberId(null);
      fetchMembers();
    } catch (err) {
      setError("Failed to update family link.");
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row items-center justify-between gap-4 border-b border-church-gray pb-4">
        <div>
          <h2 className="text-xl font-bold text-church-navy">Member Directory</h2>
          <p className="text-xs text-gray-500">Manage church members, profiles, and family links.</p>
        </div>
        <button
          onClick={handleOpenCreate}
          className="flex items-center space-x-1 bg-church-navy text-white px-4 py-2 rounded text-xs font-semibold hover:bg-blue-900 transition-colors"
        >
          <UserPlus size={14} />
          <span>Add Member</span>
        </button>
      </div>

      {/* Alerts */}
      {error && <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-xs font-bold rounded">{error}</div>}
      {success && <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-xs font-bold rounded">{success}</div>}

      {/* Search & Filters */}
      <div className="flex flex-wrap gap-3 bg-white p-4 rounded border border-church-gray shadow-sm">
        <div className="flex-grow min-w-[200px] relative">
          <Search className="absolute left-3 top-2.5 text-gray-400" size={16} />
          <input
            type="text"
            placeholder="Search by name or email..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full h-9 pl-9 pr-3 border rounded text-xs focus:outline-none focus:ring-1 focus:ring-church-blue"
          />
        </div>
        <select
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          className="h-9 border rounded text-xs px-2 focus:ring-1 focus:ring-church-blue"
        >
          <option value="">All Categories</option>
          <option value="ADULT">Adults</option>
          <option value="YOUTH">Youths</option>
          <option value="CHILD">Children</option>
        </select>
        <select
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          className="h-9 border rounded text-xs px-2 focus:ring-1 focus:ring-church-blue"
        >
          <option value="">All Statuses</option>
          <option value="ACTIVE">Active</option>
          <option value="INACTIVE">Inactive</option>
        </select>
        <button onClick={fetchMembers} className="h-9 w-9 flex items-center justify-center border rounded text-gray-500 hover:text-church-blue bg-gray-50">
          <RefreshCw size={14} className={loading ? 'animate-spin' : ''} />
        </button>
      </div>

      {/* Member Directory Table */}
      <div className="bg-white rounded border border-church-gray shadow-sm overflow-x-auto">
        <table className="w-full text-left border-collapse text-xs">
          <thead>
            <tr className="bg-church-soft border-b border-church-gray text-church-navy font-bold uppercase tracking-wider">
              <th className="p-3">Name</th>
              <th className="p-3">Contact</th>
              <th className="p-3">Category</th>
              <th className="p-3">Status</th>
              <th className="p-3">Family</th>
              <th className="p-3 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading && members.length === 0 ? (
              <tr>
                <td colSpan="6" className="p-6 text-center text-gray-500">Loading members list...</td>
              </tr>
            ) : members.length === 0 ? (
              <tr>
                <td colSpan="6" className="p-6 text-center text-gray-500">No members found matching parameters.</td>
              </tr>
            ) : (
              members.map(member => (
                <tr key={member.id} className="border-b border-church-gray hover:bg-slate-50 transition-colors">
                  <td className="p-3 font-semibold text-church-navy">
                    {member.firstName} {member.lastName}
                  </td>
                  <td className="p-3">
                    <span className="block">{member.email || 'N/A'}</span>
                    <span className="block text-gray-400">{member.phone || 'N/A'}</span>
                  </td>
                  <td className="p-3">
                    <span className={`px-2 py-0.5 rounded text-[10px] font-bold ${
                      member.category === 'ADULT' ? 'bg-blue-100 text-blue-800' :
                      member.category === 'YOUTH' ? 'bg-orange-100 text-orange-800' :
                      'bg-purple-100 text-purple-800'
                    }`}>{member.category}</span>
                  </td>
                  <td className="p-3">
                    <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${
                      member.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                    }`}>{member.status}</span>
                  </td>
                  <td className="p-3">
                    {member.familyName ? (
                      <span className="block text-church-navy font-medium">
                        {member.familyName} ({member.familyRole})
                      </span>
                    ) : (
                      <span className="text-gray-400">None</span>
                    )}
                  </td>
                  <td className="p-3 text-right space-x-2">
                    <button
                      onClick={() => { setLinkMemberId(member.id); setLinkFamilyId(member.familyId || ''); }}
                      className="text-gray-500 hover:text-church-blue"
                      title="Link Family"
                    >
                      <Link2 size={16} />
                    </button>
                    <button
                      onClick={() => handleOpenEdit(member)}
                      className="text-gray-500 hover:text-church-blue"
                      title="Edit Profile"
                    >
                      <Edit3 size={16} />
                    </button>
                    <button
                      onClick={() => handleDelete(member.id)}
                      className="text-gray-500 hover:text-red-600"
                      title="Delete Member"
                    >
                      <Trash2 size={16} />
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Create / Edit Modal Form */}
      {showForm && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40 p-4">
          <div className="bg-white rounded-lg shadow-lg border max-w-lg w-full max-h-[90vh] overflow-y-auto">
            <div className="bg-church-navy p-4 text-white flex justify-between items-center rounded-t-lg">
              <h3 className="font-bold text-sm">{editId ? "Edit Member Profile" : "Register New Member"}</h3>
              <button onClick={() => setShowForm(false)} className="text-white hover:text-blue-300">
                <X size={20} />
              </button>
            </div>
            
            <form onSubmit={handleSubmit} className="p-6 space-y-4 text-xs">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block font-bold text-church-navy mb-1">First Name *</label>
                  <input
                    type="text" required name="firstName"
                    value={formData.firstName} onChange={handleInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block font-bold text-church-navy mb-1">Last Name *</label>
                  <input
                    type="text" required name="lastName"
                    value={formData.lastName} onChange={handleInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block font-bold text-church-navy mb-1">Email Address</label>
                  <input
                    type="email" name="email"
                    value={formData.email} onChange={handleInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block font-bold text-church-navy mb-1">Phone Number</label>
                  <input
                    type="text" name="phone"
                    value={formData.phone} onChange={handleInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-3 gap-4">
                <div>
                  <label className="block font-bold text-church-navy mb-1">Gender</label>
                  <select
                    name="gender" value={formData.gender} onChange={handleInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  >
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                  </select>
                </div>
                <div>
                  <label className="block font-bold text-church-navy mb-1">Date of Birth</label>
                  <input
                    type="date" name="dob"
                    value={formData.dob} onChange={handleInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block font-bold text-church-navy mb-1">Category</label>
                  <select
                    name="category" value={formData.category} onChange={handleInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  >
                    <option value="ADULT">Adult</option>
                    <option value="YOUTH">Youth</option>
                    <option value="CHILD">Child</option>
                  </select>
                </div>
              </div>

              <div>
                <label className="block font-bold text-church-navy mb-1">Residential Address</label>
                <input
                  type="text" name="address"
                  value={formData.address} onChange={handleInputChange}
                  className="w-full h-8 px-2 border rounded focus:outline-none"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block font-bold text-church-navy mb-1">Status</label>
                  <select
                    name="status" value={formData.status} onChange={handleInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  >
                    <option value="ACTIVE">Active</option>
                    <option value="INACTIVE">Inactive</option>
                  </select>
                </div>
                <div>
                  <label className="block font-bold text-church-navy mb-1">Campus Code</label>
                  <input
                    type="text" name="campusCode"
                    value={formData.campusCode} onChange={handleInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  />
                </div>
              </div>

              <div className="pt-4 border-t flex justify-end space-x-2">
                <button
                  type="button" onClick={() => setShowForm(false)}
                  className="bg-gray-100 text-gray-700 px-4 h-8 rounded hover:bg-gray-200"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="bg-church-navy text-white px-4 h-8 rounded hover:bg-blue-900"
                >
                  Save Profile
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Link Family Modal */}
      {linkMemberId && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40 p-4">
          <div className="bg-white rounded-lg shadow-lg border max-w-sm w-full">
            <div className="bg-church-navy p-4 text-white flex justify-between items-center rounded-t-lg">
              <h3 className="font-bold text-sm">Link Family Group</h3>
              <button onClick={() => setLinkMemberId(null)} className="text-white hover:text-blue-300">
                <X size={20} />
              </button>
            </div>
            
            <form onSubmit={handleLinkFamily} className="p-6 space-y-4 text-xs">
              <div>
                <label className="block font-bold text-church-navy mb-1">Family ID (leave empty to unlink)</label>
                <input
                  type="number"
                  value={linkFamilyId}
                  onChange={(e) => setLinkFamilyId(e.target.value)}
                  placeholder="e.g. 1"
                  className="w-full h-8 px-2 border rounded focus:outline-none"
                />
              </div>

              <div>
                <label className="block font-bold text-church-navy mb-1">Family Role</label>
                <select
                  value={linkFamilyRole}
                  onChange={(e) => setLinkFamilyRole(e.target.value)}
                  className="w-full h-8 px-2 border rounded focus:outline-none"
                >
                  <option value="HEAD_OF_HOUSEHOLD">Head of Household</option>
                  <option value="SPOUSE">Spouse</option>
                  <option value="CHILD">Child</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>

              <div className="pt-4 border-t flex justify-end space-x-2">
                <button
                  type="button" onClick={() => setLinkMemberId(null)}
                  className="bg-gray-100 text-gray-700 px-4 h-8 rounded hover:bg-gray-200"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="bg-church-navy text-white px-4 h-8 rounded hover:bg-blue-900"
                >
                  Save Link
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default MemberManagement;
