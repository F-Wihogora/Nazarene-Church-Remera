import React, { createContext, useState, useContext } from 'react';

const LanguageContext = createContext();

const translations = {
  en: {
    title: "Nazarene Church Remera",
    motto: "Holiness unto the Lord",
    home: "Home",
    about: "About",
    services: "Services",
    sermons: "Sermons",
    events: "Events",
    joinUs: "Join Us",
    login: "Login",
    logout: "Logout",
    dashboard: "Dashboard",
    members: "Members",
    finances: "Finances",
    schedule: "Schedule",
    adminPortal: "Admin Portal",
    welcomeBack: "Welcome Back",
    activeMembers: "Active Members",
    prayerRequests: "Prayer Requests",
    livestream: "Livestream",
    recentAnnouncements: "Recent Announcements",
    visitorRate: "Visitor Rate",
    worshipPlan: "Worship Planner",
    language: "Language",
    username: "Username",
    password: "Password",
    submit: "Submit",
    loading: "Loading..."
  },
  rw: {
    title: "Itorero rya Nazareti Remera",
    motto: "Kwezwa ku Uwiteka",
    home: "Ahabanza",
    about: "Ibitwerekeye",
    services: "Amateraniro",
    sermons: "Inyigisho",
    events: "Ibikorwa",
    joinUs: "Kwiyandikisha",
    login: "Kwinjira",
    logout: "Gusohoka",
    dashboard: "Incamake",
    members: "Abayoboke",
    finances: "Imari",
    schedule: "Gahunda",
    adminPortal: "Urubuga rw'Ubuyobozi",
    welcomeBack: "Murakaza Neza",
    activeMembers: "Abayoboke Bakora",
    prayerRequests: "Gusaba Amasengesho",
    livestream: "Akanya K'Amasengesho Live",
    recentAnnouncements: "Amatangazo Aheruka",
    visitorRate: "Abashyitsi",
    worshipPlan: "Ibyerekeye Guhimbaza",
    language: "Ururimi",
    username: "Izina ry'ukoresha",
    password: "Ijambo ry'ibanga",
    submit: "Kohereza",
    loading: "Tegereza..."
  },
  fr: {
    title: "Église du Nazaréen Remera",
    motto: "La Sainteté à l'Éternel",
    home: "Accueil",
    about: "À Propos",
    services: "Cultes",
    sermons: "Sermons",
    events: "Événements",
    joinUs: "Nous Rejoindre",
    login: "Connexion",
    logout: "Déconnexion",
    dashboard: "Tableau de Bord",
    members: "Membres",
    finances: "Finances",
    schedule: "Calendrier",
    adminPortal: "Portail Admin",
    welcomeBack: "Bon Retour",
    activeMembers: "Membres Actifs",
    prayerRequests: "Requêtes de Prière",
    livestream: "En Direct",
    recentAnnouncements: "Annonces Récentes",
    visitorRate: "Taux de Visiteurs",
    worshipPlan: "Planificateur de Culte",
    language: "Langue",
    username: "Nom d'utilisateur",
    password: "Mot de passe",
    submit: "Soumettre",
    loading: "Chargement..."
  }
};

export const LanguageProvider = ({ children }) => {
  const [lang, setLang] = useState('en');

  const t = (key) => {
    return translations[lang]?.[key] || translations['en']?.[key] || key;
  };

  return (
    <LanguageContext.Provider value={{ lang, setLang, t }}>
      {children}
    </LanguageContext.Provider>
  );
};

export const useLanguage = () => useContext(LanguageContext);
