import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { Music, Plus, Calendar, Save, ListMusic, ChevronRight } from 'lucide-react';

const WorshipPlanner = () => {
  const [songs, setSongs] = useState([]);
  const [songSearch, setSongSearch] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Song Form State
  const [showSongForm, setShowSongForm] = useState(false);
  const [songData, setSongData] = useState({
    title: '',
    songKey: 'G',
    bpm: 80,
    lyrics: '',
    category: 'PRAISE'
  });

  // Service Plan State (Mocking Event ID = 1 for current Sunday service)
  const targetEventId = 1;
  const [planData, setPlanData] = useState({
    eventId: targetEventId,
    eventTitle: 'Sunday Worship Service',
    title: 'Sunday Celebration Plan',
    openingPrayer: 'Elder Alice Mutoni',
    bibleReading: 'Romans 12:1-8',
    sermonTitle: 'Living Sacrifices in holiness',
    offeringDetails: 'Tithes & General Offerings via MoMo',
    announcements: '1. NYI Youth fellowship this Saturday. 2. NDI Discipleship classes next Sunday.',
    closingPrayer: 'Pastor Jean Kabera',
    songIds: []
  });

  useEffect(() => {
    fetchSongs();
    fetchServicePlan();
  }, [songSearch]);

  const fetchSongs = async () => {
    try {
      const response = await api.get('/api/public/worship/songs', {
        params: { search: songSearch }
      });
      setSongs(response.data);
    } catch (err) {
      console.error("Failed to load songs", err);
    }
  };

  const fetchServicePlan = async () => {
    setLoading(true);
    try {
      const response = await api.get(`/api/worship/plans/${targetEventId}`);
      if (response.data && response.data.id) {
        setPlanData(response.data);
      }
    } catch (err) {
      console.warn("No existing service plan found for this event, loading defaults.");
    } finally {
      setLoading(false);
    }
  };

  const handleSongInputChange = (e) => {
    const { name, value } = e.target;
    setSongData(prev => ({ ...prev, [name]: value }));
  };

  const handlePlanInputChange = (e) => {
    const { name, value } = e.target;
    setPlanData(prev => ({ ...prev, [name]: value }));
  };

  const handleSongSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      await api.post('/api/worship/songs', songData);
      setSuccess("New song added to the directory.");
      setShowSongForm(false);
      fetchSongs();
    } catch (err) {
      setError("Failed to save song. Required: Media Team or Pastor permissions.");
    }
  };

  const handlePlanSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      await api.post('/api/worship/plans', planData);
      setSuccess("Service plan saved successfully.");
      fetchServicePlan();
    } catch (err) {
      setError("Failed to save service plan. Required: Pastor or Admin permissions.");
    }
  };

  const toggleSongSelection = (songId) => {
    setPlanData(prev => {
      const songIds = [...prev.songIds];
      const index = songIds.indexOf(songId);
      if (index > -1) {
        songIds.splice(index, 1);
      } else {
        songIds.push(songId);
      }
      return { ...prev, songIds };
    });
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
      {/* Service Plan Config (Left Col) */}
      <div className="lg:col-span-2 space-y-6">
        <div className="flex items-center justify-between border-b pb-4">
          <div>
            <h2 className="text-xl font-bold text-church-navy">Service Order Planner</h2>
            <p className="text-xs text-gray-500">Plan Sunday liturgies, scripts, preachings, and hymns.</p>
          </div>
          <button
            onClick={handlePlanSubmit}
            className="flex items-center space-x-1 bg-church-navy text-white px-4 py-2 rounded text-xs font-semibold hover:bg-blue-900 transition-colors shadow"
          >
            <Save size={14} />
            <span>Save Plan</span>
          </button>
        </div>

        {error && <div className="p-3 bg-red-50 border border-red-200 text-red-700 text-xs font-bold rounded">{error}</div>}
        {success && <div className="p-3 bg-green-50 border border-green-200 text-green-700 text-xs font-bold rounded">{success}</div>}

        <form onSubmit={handlePlanSubmit} className="bg-white p-6 rounded-lg border border-church-gray shadow-sm space-y-4 text-xs">
          <div>
            <label className="block font-bold text-church-navy mb-1.5">Service Plan Title</label>
            <input
              type="text" name="title"
              value={planData.title} onChange={handlePlanInputChange}
              className="w-full h-8 px-3 border rounded text-xs focus:ring-1 focus:ring-church-blue"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block font-bold text-church-navy mb-1.5">Opening Prayer Officiator</label>
              <input
                type="text" name="openingPrayer"
                value={planData.openingPrayer} onChange={handlePlanInputChange}
                className="w-full h-8 px-3 border rounded text-xs focus:ring-1"
              />
            </div>
            <div>
              <label className="block font-bold text-church-navy mb-1.5">Bible Reading Scripture</label>
              <input
                type="text" name="bibleReading"
                value={planData.bibleReading} onChange={handlePlanInputChange}
                className="w-full h-8 px-3 border rounded text-xs focus:ring-1"
                placeholder="e.g. Romans 8:28"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block font-bold text-church-navy mb-1.5">Sermon Subject / Preacher</label>
              <input
                type="text" name="sermonTitle"
                value={planData.sermonTitle} onChange={handlePlanInputChange}
                className="w-full h-8 px-3 border rounded text-xs focus:ring-1"
              />
            </div>
            <div>
              <label className="block font-bold text-church-navy mb-1.5">Offering & Tithes Guide</label>
              <input
                type="text" name="offeringDetails"
                value={planData.offeringDetails} onChange={handlePlanInputChange}
                className="w-full h-8 px-3 border rounded text-xs focus:ring-1"
              />
            </div>
          </div>

          <div>
            <label className="block font-bold text-church-navy mb-1.5">Announcements Bulletin</label>
            <textarea
              name="announcements" rows="3"
              value={planData.announcements} onChange={handlePlanInputChange}
              className="w-full p-3 border rounded text-xs focus:ring-1"
            />
          </div>

          <div>
            <label className="block font-bold text-church-navy mb-1.5">Closing Prayer / Benediction</label>
            <input
              type="text" name="closingPrayer"
              value={planData.closingPrayer} onChange={handlePlanInputChange}
              className="w-full h-8 px-3 border rounded text-xs focus:ring-1"
            />
          </div>

          <div>
            <span className="block font-bold text-church-navy mb-1.5">Selected Worship Songs ({planData.songIds.length})</span>
            <div className="border border-church-gray rounded p-3 space-y-2 bg-church-soft">
              {planData.songIds.length === 0 ? (
                <span className="text-gray-400">No songs selected. Use the directory sidebar to add.</span>
              ) : (
                planData.songIds.map(id => {
                  const song = songs.find(s => s.id === id);
                  return (
                    <div key={id} className="flex justify-between items-center bg-white p-2 rounded border border-church-gray">
                      <div className="flex items-center space-x-2">
                        <Music size={14} className="text-church-blue" />
                        <span className="font-semibold text-church-navy">{song?.title || `Song ID: ${id}`}</span>
                      </div>
                      <button
                        type="button" onClick={() => toggleSongSelection(id)}
                        className="text-red-500 hover:text-red-700 font-bold"
                      >
                        Remove
                      </button>
                    </div>
                  );
                })
              )}
            </div>
          </div>
        </form>
      </div>

      {/* Song Directory Sidebar (Right Col) */}
      <div className="space-y-6">
        <div className="flex items-center justify-between border-b pb-4">
          <div>
            <h3 className="font-bold text-sm text-church-navy">Song Directory</h3>
            <p className="text-[10px] text-gray-500">Praise and Worship library.</p>
          </div>
          <button
            onClick={() => setShowSongForm(true)}
            className="flex items-center space-x-1 border border-church-blue text-church-blue px-3 py-1.5 rounded text-[10px] font-bold hover:bg-church-blue hover:text-white"
          >
            <Plus size={12} />
            <span>Add Song</span>
          </button>
        </div>

        {/* Directory Search */}
        <input
          type="text"
          placeholder="Filter songs by title..."
          value={songSearch}
          onChange={(e) => setSongSearch(e.target.value)}
          className="w-full h-8 px-2 border rounded text-xs focus:ring-1 focus:ring-church-blue"
        />

        {/* Songs List */}
        <div className="space-y-3 max-h-[60vh] overflow-y-auto pr-1">
          {songs.map(song => {
            const isSelected = planData.songIds.includes(song.id);
            return (
              <div
                key={song.id}
                onClick={() => toggleSongSelection(song.id)}
                className={`p-3 rounded-lg border cursor-pointer transition-all-300 flex justify-between items-center ${
                  isSelected 
                    ? 'border-church-blue bg-blue-50 bg-opacity-40' 
                    : 'border-church-gray bg-white hover:bg-slate-50'
                }`}
              >
                <div>
                  <h4 className="font-bold text-xs text-church-navy">{song.title}</h4>
                  <div className="flex space-x-2 text-[10px] text-gray-500 mt-1">
                    <span>Key: {song.songKey}</span>
                    <span>BPM: {song.bpm}</span>
                    <span className="px-1.5 bg-church-soft border rounded text-[8px] font-bold text-church-navy uppercase">
                      {song.category}
                    </span>
                  </div>
                </div>
                <ChevronRight size={14} className={isSelected ? 'text-church-blue' : 'text-gray-400'} />
              </div>
            );
          })}
        </div>
      </div>

      {/* Add Song Modal */}
      {showSongForm && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40 p-4">
          <div className="bg-white rounded-lg shadow-lg border max-w-sm w-full">
            <div className="bg-church-navy p-4 text-white flex justify-between items-center rounded-t-lg">
              <h3 className="font-bold text-sm">Add Song to Library</h3>
              <button onClick={() => setShowSongForm(false)} className="text-white hover:text-blue-300">
                <X size={20} />
              </button>
            </div>
            
            <form onSubmit={handleSongSubmit} className="p-6 space-y-4 text-xs">
              <div>
                <label className="block font-bold text-church-navy mb-1">Song Title *</label>
                <input
                  type="text" required name="title"
                  value={songData.title} onChange={handleSongInputChange}
                  className="w-full h-8 px-2 border rounded focus:outline-none"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block font-bold text-church-navy mb-1">Key</label>
                  <input
                    type="text" name="songKey"
                    value={songData.songKey} onChange={handleSongInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block font-bold text-church-navy mb-1">Tempo (BPM)</label>
                  <input
                    type="number" name="bpm"
                    value={songData.bpm} onChange={handleSongInputChange}
                    className="w-full h-8 px-2 border rounded focus:outline-none"
                  />
                </div>
              </div>

              <div>
                <label className="block font-bold text-church-navy mb-1">Category</label>
                <select
                  name="category" value={songData.category} onChange={handleSongInputChange}
                  className="w-full h-8 px-2 border rounded focus:outline-none"
                >
                  <option value="PRAISE">Praise</option>
                  <option value="WORSHIP">Worship</option>
                  <option value="HYMN">Hymn</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>

              <div>
                <label className="block font-bold text-church-navy mb-1">Lyrics Summary</label>
                <textarea
                  name="lyrics" rows="3"
                  value={songData.lyrics} onChange={handleSongInputChange}
                  className="w-full p-2 border rounded focus:outline-none"
                />
              </div>

              <div className="pt-4 border-t flex justify-end space-x-2">
                <button
                  type="button" onClick={() => setShowSongForm(false)}
                  className="bg-gray-100 text-gray-700 px-4 h-8 rounded hover:bg-gray-200"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="bg-church-navy text-white px-4 h-8 rounded hover:bg-blue-900"
                >
                  Save Song
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default WorshipPlanner;
