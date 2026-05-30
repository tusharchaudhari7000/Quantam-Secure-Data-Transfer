"use client"

import { AtomIcon, X } from "lucide-react"

function RegisterModal({ isOpen, onClose, onSubmit, registerData, setRegisterData, onSwitchToLogin }) {
  if (!isOpen) return null

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-gray-900 rounded-xl border border-gray-800 p-8 w-full max-w-md mx-4 relative max-h-[90vh] overflow-y-auto">
        <button onClick={onClose} className="absolute top-4 right-4 text-gray-400 hover:text-white transition-colors">
          <X className="h-6 w-6" />
        </button>

        <div className="text-center mb-6">
          <AtomIcon className="h-12 w-12 text-cyan-400 mx-auto mb-4" />
          <h2 className="text-2xl font-bold bg-gradient-to-r from-purple-400 to-cyan-400 bg-clip-text text-transparent">
            Join QuantumChat
          </h2>
          <p className="text-gray-400 mt-2">Create your quantum-secured account</p>
        </div>

        <form onSubmit={onSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label htmlFor="register-firstName" className="block text-sm font-medium text-gray-300 mb-1">
                First Name
              </label>
              <input
                type="text"
                id="register-firstName"
                value={registerData.firstName}
                onChange={(e) => setRegisterData({ ...registerData, firstName: e.target.value })}
                className="w-full bg-gray-800 border border-gray-700 rounded-md p-3 text-white focus:ring-2 focus:ring-cyan-500 focus:border-transparent"
                required
              />
            </div>

            <div>
              <label htmlFor="register-lastName" className="block text-sm font-medium text-gray-300 mb-1">
                Last Name
              </label>
              <input
                type="text"
                id="register-lastName"
                value={registerData.lastName}
                onChange={(e) => setRegisterData({ ...registerData, lastName: e.target.value })}
                className="w-full bg-gray-800 border border-gray-700 rounded-md p-3 text-white focus:ring-2 focus:ring-cyan-500 focus:border-transparent"
                required
              />
            </div>
          </div>

          <div>
            <label htmlFor="register-email" className="block text-sm font-medium text-gray-300 mb-1">
              Email
            </label>
            <input
              type="email"
              id="register-email"
              value={registerData.email}
              onChange={(e) => setRegisterData({ ...registerData, email: e.target.value })}
              className="w-full bg-gray-800 border border-gray-700 rounded-md p-3 text-white focus:ring-2 focus:ring-cyan-500 focus:border-transparent"
              required
            />
          </div>

          <div>
            <label htmlFor="register-picLink" className="block text-sm font-medium text-gray-300 mb-1">
              Profile Picture URL
            </label>
            <input
              type="url"
              id="register-picLink"
              value={registerData.picLink}
              onChange={(e) => setRegisterData({ ...registerData, picLink: e.target.value })}
              className="w-full bg-gray-800 border border-gray-700 rounded-md p-3 text-white focus:ring-2 focus:ring-cyan-500 focus:border-transparent"
              placeholder="https://example.com/your-photo.jpg"
            />
          </div>

          <div>
            <label htmlFor="register-password" className="block text-sm font-medium text-gray-300 mb-1">
              Password
            </label>
            <input
              type="password"
              id="register-password"
              value={registerData.password}
              onChange={(e) => setRegisterData({ ...registerData, password: e.target.value })}
              className="w-full bg-gray-800 border border-gray-700 rounded-md p-3 text-white focus:ring-2 focus:ring-cyan-500 focus:border-transparent"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full px-4 py-3 bg-gradient-to-r from-purple-600 to-cyan-600 hover:from-purple-700 hover:to-cyan-700 text-white rounded-md font-medium transition-all"
          >
            Create Account
          </button>
        </form>

        <div className="text-center mt-4">
          <p className="text-gray-400">
            Already have an account?{" "}
            <button onClick={onSwitchToLogin} className="text-cyan-400 hover:text-cyan-300 transition-colors">
              Sign in here
            </button>
          </p>
        </div>
      </div>
    </div>
  )
}

export default RegisterModal
