"use client"

import { AtomIcon } from "lucide-react"

function Navbar({ onLoginClick, onRegisterClick, isLoggedIn, onLogoutClick }) {
  return (
    <nav className="container mx-auto py-6 flex justify-between items-center">
      <div className="flex items-center gap-2">
        <AtomIcon className="h-8 w-8 text-cyan-400" />
        <span className="text-xl font-bold">QuantumChat</span>
      </div>
      <div className="flex items-center gap-6">
        <a href="#about" className="hover:text-cyan-400 transition-colors">
          About
        </a>
        <a href="#features" className="hover:text-cyan-400 transition-colors">
          Features
        </a>
        <a href="#contact" className="hover:text-cyan-400 transition-colors">
          Contact
        </a>
        <div className="flex items-center gap-3 ml-6">
          {!isLoggedIn ? (
            <>
              <button
                onClick={onLoginClick}
                className="px-4 py-2 text-white hover:text-cyan-400 transition-colors"
              >
                Login
              </button>
              <button
                onClick={onRegisterClick}
                className="px-4 py-2 bg-gradient-to-r from-purple-600 to-cyan-600 hover:from-purple-700 hover:to-cyan-700 text-white rounded-md font-medium transition-all"
              >
                Register
              </button>
            </>
          ) : (
            <button
              onClick={onLogoutClick}
              className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-md font-medium transition-all"
            >
              Logout
            </button>
          )}
        </div>
      </div>
    </nav>
  )
}

export default Navbar
