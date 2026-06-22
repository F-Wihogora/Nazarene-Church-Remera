import React from 'react';
import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { Play, Calendar, Users, Heart, BookOpen, Compass } from 'lucide-react';

const Home = () => {
  const { t } = useLanguage();

  const serviceTimes = [
    { name: "Sunday Celebration Service", time: "09:00 AM - 11:30 AM", desc: "Main corporate worship, Scripture reading & preaching." },
    { name: "Youth Fellowship (NYI)", time: "Saturday 04:00 PM - 06:00 PM", desc: "Vibrant praise, discussion, and connection for youth." },
    { name: "Friday Overnight Prayer", time: "Friday 10:00 PM - 04:00 AM", desc: "Intercessory prayer and fasting vigils." }
  ];

  const ministries = [
    { title: "NDI (Discipleship)", desc: "Empowering believers with Bible reading plans and structured Sunday classes.", icon: BookOpen },
    { title: "NYI (Youth Ministry)", desc: "Enabling youth to grow spiritually, lead, and impact the church community.", icon: Compass },
    { title: "NCM (Compassion)", desc: "Caring for our community through food distributions, student support, and benevolence.", icon: Heart },
    { title: "NMI (Missions)", desc: "Spreading the gospel globally and locally through outreach and trips.", icon: Users }
  ];

  return (
    <div className="space-y-16 pb-16">
      {/* Hero Section */}
      <section className="relative bg-church-navy text-white py-24 px-4 overflow-hidden border-b border-church-navy border-opacity-30">
        {/* Subtle decorative overlays */}
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_right,_var(--tw-gradient-stops))] from-blue-900 via-church-navy to-church-dark opacity-90" />
        
        <div className="relative max-w-5xl mx-auto text-center space-y-6">
          <div className="inline-flex items-center space-x-2 bg-white bg-opacity-10 px-3 py-1.5 rounded-full text-xs font-semibold uppercase tracking-wider text-blue-200">
            <span>Nazarene Church Remera</span>
          </div>
          <h1 className="text-4xl md:text-6xl font-extrabold tracking-tight text-white leading-tight">
            Holiness Unto The Lord
          </h1>
          <p className="max-w-2xl mx-auto text-base md:text-lg text-gray-300">
            Welcome to the Nazarene Church Remera Digital Ecosystem (NCR-DE). We are a community dedicated to spiritual growth, biblical holiness, and caring for others.
          </p>
          <div className="flex flex-wrap justify-center gap-4 pt-4">
            <Link
              to="/join"
              className="bg-white text-church-navy font-bold text-sm px-6 py-3 rounded-md shadow hover:bg-gray-100 transition-all-300"
            >
              {t('joinUs')}
            </Link>
            <a
              href="#services"
              className="bg-transparent border border-white border-opacity-30 text-white font-bold text-sm px-6 py-3 rounded-md hover:bg-white hover:bg-opacity-10 transition-all-300"
            >
              View Service Times
            </a>
          </div>
        </div>
      </section>

      {/* Livestream Highlight Banner */}
      <section className="max-w-5xl mx-auto px-4">
        <div className="glass-panel p-6 md:p-8 rounded-2xl shadow-lg border border-church-gray flex flex-col md:flex-row items-center justify-between gap-6">
          <div className="space-y-3 text-center md:text-left">
            <div className="inline-flex items-center space-x-1 bg-red-100 text-red-800 px-2 py-0.5 rounded text-xs font-bold uppercase tracking-wider">
              <span className="h-2 w-2 rounded-full bg-red-600 animate-ping"></span>
              <span>Live Stream</span>
            </div>
            <h2 className="text-xl md:text-2xl font-extrabold text-church-navy">Join Our Services Online</h2>
            <p className="text-sm text-gray-600 max-w-md">
              We broadcast our Sunday Services live every week at 9:00 AM on YouTube and Facebook. Watch sermons and participate in worship remotely.
            </p>
          </div>
          <div className="w-full md:w-auto">
            {/* Embedded mockup video stream player */}
            <div className="relative aspect-video w-full md:w-96 rounded-lg overflow-hidden bg-church-navy border-2 border-church-navy flex items-center justify-center text-white">
              <div className="absolute inset-0 bg-black bg-opacity-40 flex flex-col items-center justify-center space-y-2">
                <Play size={48} className="text-white fill-white hover:scale-115 transition-transform cursor-pointer" />
                <span className="text-xs font-semibold tracking-wider">WATCH LIVESTREAM</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Service Schedules */}
      <section id="services" className="max-w-5xl mx-auto px-4 scroll-mt-20">
        <div className="text-center space-y-3 mb-10">
          <h2 className="text-2xl md:text-3xl font-extrabold text-church-navy">Our Service Times</h2>
          <p className="text-sm text-gray-600 max-w-lg mx-auto">
            Join us in-person or follow along with our weekly fellowship gatherings. All are welcome!
          </p>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {serviceTimes.map((svc) => (
            <div key={svc.name} className="bg-white p-6 rounded-xl border border-church-gray shadow-sm hover:shadow-md transition-shadow flex flex-col justify-between">
              <div>
                <div className="text-church-blue font-bold text-xs uppercase tracking-wider mb-1 flex items-center space-x-1">
                  <Calendar size={14} />
                  <span>Gathering</span>
                </div>
                <h3 className="text-lg font-bold text-church-navy mb-2">{svc.name}</h3>
                <p className="text-sm text-gray-600 mb-4">{svc.desc}</p>
              </div>
              <div className="bg-church-soft p-3 rounded text-center text-church-navy font-bold text-sm border border-church-gray border-opacity-50">
                {svc.time}
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Ministries / Departments */}
      <section className="bg-white py-16 border-t border-b border-church-gray">
        <div className="max-w-5xl mx-auto px-4">
          <div className="text-center space-y-3 mb-12">
            <h2 className="text-2xl md:text-3xl font-extrabold text-church-navy">Our Church Ministries</h2>
            <p className="text-sm text-gray-600 max-w-lg mx-auto">
              NCR-DE supports several ministry teams designed to serve families, youth, discipleship, and community benevolence.
            </p>
          </div>
          
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {ministries.map((m) => {
              const Icon = m.icon;
              return (
                <div key={m.title} className="p-6 bg-church-soft rounded-xl border border-church-gray border-opacity-50 hover:-translate-y-1 transition-transform">
                  <div className="w-10 h-10 bg-church-navy text-white rounded-lg flex items-center justify-center mb-4">
                    <Icon size={20} />
                  </div>
                  <h3 className="text-base font-bold text-church-navy mb-2">{m.title}</h3>
                  <p className="text-xs text-gray-600 leading-relaxed">{m.desc}</p>
                </div>
              );
            })}
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
