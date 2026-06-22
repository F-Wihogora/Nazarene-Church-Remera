import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { useLanguage } from '../context/LanguageContext';
import { DollarSign, ArrowUpRight, ArrowDownRight, Percent, Calendar, PlusCircle } from 'lucide-react';

const AdminFinances = () => {
  const { t } = useLanguage();
  const [finances, setFinances] = useState([]);
  const [members, setMembers] = useState([]);

  // Form states
  const [recordType, setRecordType] = useState('INCOME');
  const [category, setCategory] = useState('OFFERING');
  const [amount, setAmount] = useState('');
  const [recordDate, setRecordDate] = useState('');
  const [selectedMember, setSelectedMember] = useState('');
  const [description, setDescription] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('CASH');

  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState('');
  const [err, setErr] = useState('');

  const fetchFinancesAndMembers = async () => {
    try {
      const finRes = await api.get('/api/admin/finances');
      setFinances(finRes.data);

      const memRes = await api.get('/api/members');
      setMembers(memRes.data);
    } catch (e) {
      console.error("Failed to fetch finance lists", e);
    }
  };

  useEffect(() => {
    fetchFinancesAndMembers();
  }, []);

  const totalIncome = finances
    .filter(f => f.recordType === 'INCOME')
    .reduce((sum, f) => sum + f.amount, 0);

  const totalExpense = finances
    .filter(f => f.recordType === 'EXPENSE')
    .reduce((sum, f) => sum + f.amount, 0);

  const netBalance = totalIncome - totalExpense;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErr('');
    setMsg('');

    try {
      let url = `/api/admin/finances?recordType=${recordType}&category=${category}&amount=${amount}&date=${recordDate}&paymentMethod=${paymentMethod}`;
      if (selectedMember) {
        url += `&memberId=${selectedMember}`;
      }
      if (description) {
        url += `&description=${encodeURIComponent(description)}`;
      }

      await api.post(url);
      setMsg('Finance transaction logged successfully!');
      setAmount('');
      setDescription('');
      setSelectedMember('');
      fetchFinancesAndMembers();
    } catch (e) {
      setErr('Failed to log finance transaction. Check inputs.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-8">
      <div className="border-b pb-4">
        <h2 className="text-xl font-bold text-church-navy">Treasury & Finance Management</h2>
        <p className="text-xs text-gray-500">Track and log local offerings, tithing, operational costs, and utility billing.</p>
      </div>

      {/* Analytics Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm flex items-center justify-between">
          <div>
            <span className="text-[10px] font-bold uppercase text-gray-500 tracking-wider">Total Income</span>
            <h3 className="text-2xl font-black text-green-600 mt-1">{totalIncome.toLocaleString()} RWF</h3>
          </div>
          <div className="w-12 h-12 bg-green-50 rounded-full flex items-center justify-center text-green-600">
            <ArrowUpRight size={24} />
          </div>
        </div>

        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm flex items-center justify-between">
          <div>
            <span className="text-[10px] font-bold uppercase text-gray-500 tracking-wider">Total Expenses</span>
            <h3 className="text-2xl font-black text-red-600 mt-1">{totalExpense.toLocaleString()} RWF</h3>
          </div>
          <div className="w-12 h-12 bg-red-50 rounded-full flex items-center justify-center text-red-600">
            <ArrowDownRight size={24} />
          </div>
        </div>

        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm flex items-center justify-between">
          <div>
            <span className="text-[10px] font-bold uppercase text-gray-500 tracking-wider">Net Balance</span>
            <h3 className={`text-2xl font-black mt-1 ${netBalance >= 0 ? 'text-church-navy' : 'text-red-700'}`}>
              {netBalance.toLocaleString()} RWF
            </h3>
          </div>
          <div className="w-12 h-12 bg-blue-50 rounded-full flex items-center justify-center text-church-blue">
            <DollarSign size={24} />
          </div>
        </div>
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
        {/* Log Transaction */}
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm space-y-4">
          <h3 className="text-sm font-bold text-church-navy border-b pb-2 uppercase tracking-wider flex items-center gap-2">
            <PlusCircle size={16} />
            Log Income/Expense
          </h3>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Transaction Type
              </label>
              <div className="grid grid-cols-2 gap-2">
                <button
                  type="button"
                  onClick={() => { setRecordType('INCOME'); setCategory('OFFERING'); }}
                  className={`h-9 rounded font-bold text-xs uppercase tracking-wider border transition-all ${
                    recordType === 'INCOME'
                      ? 'bg-green-600 text-white border-green-600'
                      : 'bg-white text-gray-600 border-church-gray hover:bg-gray-50'
                  }`}
                >
                  Income
                </button>
                <button
                  type="button"
                  onClick={() => { setRecordType('EXPENSE'); setCategory('UTILITY'); }}
                  className={`h-9 rounded font-bold text-xs uppercase tracking-wider border transition-all ${
                    recordType === 'EXPENSE'
                      ? 'bg-red-600 text-white border-red-600'
                      : 'bg-white text-gray-600 border-church-gray hover:bg-gray-50'
                  }`}
                >
                  Expense
                </button>
              </div>
            </div>

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Category
              </label>
              <select
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs bg-white"
              >
                {recordType === 'INCOME' ? (
                  <>
                    <option value="OFFERING">General Offering</option>
                    <option value="TITHE">Tithe Contribution</option>
                    <option value="DONATION">Building / Missions Donation</option>
                    <option value="OTHER">Other Income</option>
                  </>
                ) : (
                  <>
                    <option value="UTILITY">Electricity / Water / Internet</option>
                    <option value="SALARY">Staff Salary & Allowances</option>
                    <option value="REPAIRS">Facility Repairs / Maintenance</option>
                    <option value="OTHER">Other Ministry Expenses</option>
                  </>
                )}
              </select>
            </div>

            {category === 'TITHE' && (
              <div>
                <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                  Associate Member (Tithing)
                </label>
                <select
                  value={selectedMember}
                  onChange={(e) => setSelectedMember(e.target.value)}
                  className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs bg-white"
                >
                  <option value="">-- Choose Member --</option>
                  {members.map(m => (
                    <option key={m.id} value={m.id}>
                      {m.firstName} {m.lastName}
                    </option>
                  ))}
                </select>
              </div>
            )}

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Amount (RWF)
              </label>
              <input
                type="number"
                required
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                placeholder="e.g. 50000"
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs"
              />
            </div>

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Payment Method
              </label>
              <select
                value={paymentMethod}
                onChange={(e) => setPaymentMethod(e.target.value)}
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs bg-white"
              >
                <option value="CASH">Cash</option>
                <option value="MOBILE_MONEY">Mobile Money (Momo)</option>
                <option value="BANK_TRANSFER">Bank Transfer</option>
                <option value="CHECK">Check</option>
              </select>
            </div>

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Transaction Date
              </label>
              <input
                type="date"
                required
                value={recordDate}
                onChange={(e) => setRecordDate(e.target.value)}
                className="w-full h-10 px-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs"
              />
            </div>

            <div>
              <label className="block text-[10px] font-bold uppercase tracking-wider text-church-navy mb-1">
                Notes / Details
              </label>
              <textarea
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Enter description details"
                className="w-full p-3 border border-church-gray rounded focus:outline-none focus:ring-1 focus:ring-church-blue text-xs h-20 resize-none"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full h-10 bg-church-navy text-white rounded font-bold text-xs uppercase tracking-wide hover:bg-blue-900 transition-colors flex items-center justify-center disabled:opacity-50"
            >
              {loading ? 'Logging...' : 'Log Transaction'}
            </button>
          </form>
        </div>

        {/* Transaction History */}
        <div className="bg-white p-6 rounded-xl border border-church-gray shadow-sm lg:col-span-2 space-y-4">
          <h3 className="text-sm font-bold text-church-navy border-b pb-2 uppercase tracking-wider flex items-center gap-2">
            <DollarSign size={16} />
            Ledger Book
          </h3>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="bg-slate-50 text-church-navy font-bold uppercase text-[10px] tracking-wider border-b border-church-gray">
                  <th className="p-3">Date</th>
                  <th className="p-3">Type</th>
                  <th className="p-3">Category</th>
                  <th className="p-3">Method</th>
                  <th className="p-3">Member (Tithing)</th>
                  <th className="p-3">Description</th>
                  <th className="p-3 text-right">Amount (RWF)</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {finances.length === 0 ? (
                  <tr>
                    <td colSpan="7" className="p-4 text-center text-gray-500 font-medium italic">
                      No finance logs exist. Log income or expense transactions in the panel.
                    </td>
                  </tr>
                ) : (
                  finances.map((fin) => (
                    <tr key={fin.id} className="hover:bg-slate-50">
                      <td className="p-3 text-gray-500 font-medium">{fin.recordDate}</td>
                      <td className="p-3">
                        <span className={`text-[9px] font-bold px-2 py-0.5 rounded border uppercase ${
                          fin.recordType === 'INCOME'
                            ? 'bg-green-50 border-green-150 text-green-700'
                            : 'bg-red-50 border-red-150 text-red-700'
                        }`}>
                          {fin.recordType}
                        </span>
                      </td>
                      <td className="p-3 font-semibold text-church-navy">{fin.category}</td>
                      <td className="p-3 text-gray-600 font-medium">{fin.paymentMethod}</td>
                      <td className="p-3 font-medium text-church-navy">
                        {fin.member ? `${fin.member.firstName} ${fin.member.lastName}` : '-'}
                      </td>
                      <td className="p-3 text-gray-500 truncate max-w-[150px]" title={fin.description}>
                        {fin.description || '-'}
                      </td>
                      <td className={`p-3 text-right font-bold ${
                        fin.recordType === 'INCOME' ? 'text-green-700' : 'text-red-700'
                      }`}>
                        {fin.recordType === 'INCOME' ? '+' : '-'}{fin.amount.toLocaleString()}
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

export default AdminFinances;
