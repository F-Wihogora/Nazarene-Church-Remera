import React from 'react';

const Logo = ({ className = "w-12 h-12" }) => {
  return (
    <svg 
      viewBox="0 0 100 100" 
      className={`${className} select-none`}
      fill="none" 
      xmlns="http://www.w3.org/2000/svg"
    >
      {/* Outer Ring */}
      <circle cx="50" cy="50" r="46" stroke="currentColor" strokeWidth="2.5" />
      <circle cx="50" cy="50" r="39" stroke="currentColor" strokeWidth="1.2" />

      {/* Decorative dots in the ring border */}
      <circle cx="16" cy="50" r="1" fill="currentColor" />
      <circle cx="84" cy="50" r="1" fill="currentColor" />

      {/* Stylized Holy Spirit Dove */}
      <path 
        d="M 46 22 
           C 51 28, 54 36, 52 44 
           C 49 43, 44 43, 38 45
           C 45 49, 52 50, 52 58
           C 52 50, 57 49, 61 47
           C 55 45, 51 44, 49 44
           C 52 35, 49 27, 46 22 Z" 
        fill="currentColor" 
      />

      {/* Open Bible at the Bottom */}
      <path 
        d="M 33 66 
           C 40 62, 47 64, 50 67 
           C 53 64, 60 62, 67 66
           L 67 76
           C 60 72, 53 74, 50 77
           C 47 74, 40 72, 33 76
           Z" 
        fill="none" 
        stroke="currentColor" 
        strokeWidth="1.5" 
        strokeLinejoin="round"
      />
      {/* Pages lines inside the bible */}
      <path d="M 50 67 L 50 77" stroke="currentColor" strokeWidth="1.5" />
      <path d="M 37 68 C 42 66, 46 67, 48 69" stroke="currentColor" strokeWidth="0.8" />
      <path d="M 63 68 C 58 66, 54 67, 52 69" stroke="currentColor" strokeWidth="0.8" />
      
      {/* Bookmark Ribbon */}
      <path d="M 48 70 L 48 81 L 46 79 Z" fill="currentColor" />

      {/* Texts (rendered as stylized paths or simplified text paths for SVG scaling) */}
      {/* Holiness unto the Lord inside the logo */}
      <text 
        x="50" 
        y="42" 
        fill="currentColor" 
        fontSize="3.2" 
        fontWeight="bold" 
        textAnchor="middle"
        letterSpacing="0.1"
      >
        HOLINESS
      </text>
      <text 
        x="50" 
        y="46" 
        fill="currentColor" 
        fontSize="2.8" 
        textAnchor="middle"
      >
        UNTO THE
      </text>
      <text 
        x="50" 
        y="50" 
        fill="currentColor" 
        fontSize="3.2" 
        fontWeight="bold" 
        textAnchor="middle"
      >
        LORD
      </text>

      {/* Church of the Nazarene text arched (simplified for cross-browser SVG rendering) */}
      <text 
        x="50" 
        y="12" 
        fill="currentColor" 
        fontSize="4.8" 
        fontWeight="bold" 
        textAnchor="middle"
        letterSpacing="0.2"
      >
        CHURCH OF THE
      </text>
      
      <text 
        x="50" 
        y="91" 
        fill="currentColor" 
        fontSize="6.2" 
        fontWeight="heavy" 
        textAnchor="middle"
        letterSpacing="0.4"
      >
        NAZARENE
      </text>
    </svg>
  );
};

export default Logo;
