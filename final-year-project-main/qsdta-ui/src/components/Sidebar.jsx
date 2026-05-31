"use client"

import { useState, useEffect } from "react"
import { Link, useLocation, useNavigate } from "react-router-dom"
import axios from "axios"
import { AtomIcon, Users, UserCheck, LogOut, Trash2 } from "lucide-react"

function Sidebar() {
  const location = useLocation()
  const navigate = useNavigate()
  const [isCollapsed, setIsCollapsed] = useState(false)
  const [showLoader, setShowLoader] = useState(false)
  const [loggedInUserUUID, setLoggedInUserUUID] = useState(null)

  useEffect(() => {
    const userStr = localStorage.getItem("loggedInUser")
    if (userStr) {
      try {
        const user = JSON.parse(userStr)
        setLoggedInUserUUID(user.uuid)
      } catch (e) {
        console.error("Failed to parse user from localStorage")
      }
    }
  }, [])

  const navItems = [
    {
      name: "All Users",
      href: "/dashboard",
      icon: <Users className="h-5 w-5" />,
      exact: true,
    },
    {
      name: "Connections",
      href: "/connections",
      icon: <UserCheck className="h-5 w-5" />,
      exact: true,
    },
  ]

  const isActive = (href, exact) => {
    if (exact) {
      return location.pathname === href
    }
    return location.pathname.startsWith(href)
  }

  const handleLogout = () => {
    setShowLoader(true)
    setTimeout(() => {
      localStorage.removeItem("token")
      localStorage.removeItem("loggedInUser")
      setShowLoader(false)
      navigate("/")
    }, 3000) // 3 seconds loader
  }

  const handleDeleteAccount = async () => {
    if (!loggedInUserUUID) {
      alert("User not logged in")
      return
    }

    const confirmDelete = window.confirm(
      "Are you sure you want to delete your account? This action cannot be undone."
    )
    if (!confirmDelete) return

    setShowLoader(true)
    try {
      await axios.delete(`http://${window.location.hostname}:8080/qsdta/api/users/delete/${loggedInUserUUID}`)
      // Clear data & redirect
      localStorage.removeItem("token")
      localStorage.removeItem("loggedInUser")
      setShowLoader(false)
      alert("Account deleted successfully.")
      navigate("/")
    } catch (error) {
      setShowLoader(false)
      alert(error.response?.data || error.message || "Failed to delete account")
    }
  }

  return (
    <>
      {showLoader && (
        <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-70 flex items-center justify-center z-[9999]">
          <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-white border-opacity-50"></div>
        </div>
      )}

      <div
        className={`bg-gray-900 border-r border-gray-800 h-full flex flex-col transition-all duration-300 ${
          isCollapsed ? "w-16" : "w-64"
        }`}
      >
        {/* Logo */}
        <div className="p-4 border-b border-gray-800 flex items-center justify-between">
          <div className="flex items-center">
            <AtomIcon className="h-8 w-8 text-cyan-400 flex-shrink-0" />
            {!isCollapsed && (
              <span className="ml-2 text-xl font-bold bg-gradient-to-r from-cyan-400 to-purple-400 bg-clip-text text-transparent">
                QuantumChat
              </span>
            )}
          </div>
          {!isCollapsed && (
            <span className="text-[10px] bg-cyan-500/10 text-cyan-400 px-2 py-0.5 rounded-full border border-cyan-500/20 font-medium">
              Secure
            </span>
          )}
        </div>

        {/* Navigation */}
        <nav className="flex-1 py-4">
          <ul className="space-y-1">
            {navItems.map((item) => (
              <li key={item.name}>
                <Link to={item.href}>
                  <button
                    className={`w-full flex items-center px-4 py-2 rounded-md ${
                      isActive(item.href, item.exact)
                        ? "bg-gradient-to-r from-purple-900/50 to-cyan-900/50 text-white"
                        : "text-gray-400 hover:text-white hover:bg-gray-800"
                    }`}
                  >
                    {item.icon}
                    {!isCollapsed && <span className="ml-3">{item.name}</span>}
                  </button>
                </Link>
              </li>
            ))}

            {/* Delete Account Button */}
            <li>
              <button
                onClick={handleDeleteAccount}
                className="w-full flex items-center px-4 py-2 rounded-md text-red-500 hover:text-white hover:bg-red-700"
              >
                <Trash2 className="h-5 w-5" />
                {!isCollapsed && <span className="ml-3">Delete Account</span>}
              </button>
            </li>
          </ul>
        </nav>

        {/* Logout */}
        <div className="p-4 border-t border-gray-800">
          <button
            onClick={handleLogout}
            className="w-full flex items-center px-4 py-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-800"
          >
            <LogOut className="h-5 w-5" />
            {!isCollapsed && <span className="ml-3">Logout</span>}
          </button>
        </div>

        {/* Collapse button */}
        <div className="p-4 border-t border-gray-800">
          <button
            onClick={() => setIsCollapsed(!isCollapsed)}
            className="w-full px-2 py-1 border border-gray-700 rounded-md text-sm"
          >
            {isCollapsed ? ">>" : "<<"}
          </button>
        </div>
      </div>
    </>
  )
}

export default Sidebar
