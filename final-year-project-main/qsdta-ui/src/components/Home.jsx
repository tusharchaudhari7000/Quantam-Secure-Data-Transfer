import { useState, useEffect } from "react"
import axios from "axios"
import Navbar from "./home/Navbar"
import LoginModal from "./home/LoginModal"
import RegisterModal from "./home/RegisterModal"
import HeroSection from "./home/HeroSection"
import AboutSection from "./home/AboutSection"
import FeaturesSection from "./home/FeaturesSection"
import ContactSection from "./home/ContactSection"
import Footer from "./home/Footer"

function Loader() {
  return (
    <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-60 flex items-center justify-center z-[9999]">
      <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-white border-opacity-50"></div>
    </div>
  )
}

function Home() {
  const [showLoginModal, setShowLoginModal] = useState(false)
  const [showRegisterModal, setShowRegisterModal] = useState(false)
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [loggedInUser, setLoggedInUser] = useState(null)
  const [isLoading, setIsLoading] = useState(false)

  const [loginData, setLoginData] = useState({
    email: "",
    password: "",
  })

  const [registerData, setRegisterData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    picLink: "",
    password: "",
  })

  useEffect(() => {
    const token = localStorage.getItem("token")
    const savedUser = localStorage.getItem("loggedInUser")
    if (token && savedUser) {
      setLoggedInUser(JSON.parse(savedUser))
      setIsLoggedIn(true)
    }
  }, [])

  const handleLoginSubmit = async (e) => {
    e.preventDefault()
    setIsLoading(true)
    const delay = new Promise((resolve) => setTimeout(resolve, 5000)) // 5s loader
    try {
      const apiCall = axios.post(
        `http://${window.location.hostname}:8080/qsdta/api/users/login`,
        loginData,
        { headers: { "Content-Type": "application/json" } }
      )

      const [res] = await Promise.all([apiCall, delay])
      const data = res.data

      localStorage.setItem("token", data.token || "dummy_token")
      localStorage.setItem("loggedInUser", JSON.stringify(data))
      setLoggedInUser(data)
      setIsLoggedIn(true)
      setShowLoginModal(false)
    } catch (err) {
      alert(err.response?.data || err.message || "Login failed")
    } finally {
      setIsLoading(false)
    }
  }

  const handleRegisterSubmit = async (e) => {
    e.preventDefault()
    setIsLoading(true)
    const delay = new Promise((resolve) => setTimeout(resolve, 5000)) // 5s loader
    try {
      const registerCall = axios.post(
        `http://${window.location.hostname}:8080/qsdta/api/users/register`,
        registerData,
        { headers: { "Content-Type": "application/json" } }
      )

      await Promise.all([registerCall, delay]) // wait for both to finish

      const loginRes = await axios.post(
        `http://${window.location.hostname}:8080/qsdta/api/users/login`,
        {
          email: registerData.email,
          password: registerData.password,
        }
      )

      const userData = loginRes.data
      localStorage.setItem("token", userData.token || "dummy_token")
      localStorage.setItem("loggedInUser", JSON.stringify(userData))
      setLoggedInUser(userData)
      setIsLoggedIn(true)
      setShowRegisterModal(false)
    } catch (err) {
      alert(err.response?.data || err.message || "Register failed")
    } finally {
      setIsLoading(false)
    }
  }

  const handleLogout = () => {
    localStorage.removeItem("token")
    localStorage.removeItem("loggedInUser")
    setIsLoggedIn(false)
    setLoggedInUser(null)
  }

  const closeModals = () => {
    setShowLoginModal(false)
    setShowRegisterModal(false)
  }

  const switchToRegister = () => {
    setShowLoginModal(false)
    setShowRegisterModal(true)
  }

  const switchToLogin = () => {
    setShowRegisterModal(false)
    setShowLoginModal(true)
  }

  return (
    <div className="min-h-screen bg-black text-white">
      {isLoading && <Loader />}

      <Navbar
        onLoginClick={() => setShowLoginModal(true)}
        onRegisterClick={() => setShowRegisterModal(true)}
        isLoggedIn={isLoggedIn}
        onLogoutClick={handleLogout}
      />

      <LoginModal
        isOpen={showLoginModal}
        onClose={closeModals}
        onSubmit={handleLoginSubmit}
        loginData={loginData}
        setLoginData={setLoginData}
        onSwitchToRegister={switchToRegister}
      />

      <RegisterModal
        isOpen={showRegisterModal}
        onClose={closeModals}
        onSubmit={handleRegisterSubmit}
        registerData={registerData}
        setRegisterData={setRegisterData}
        onSwitchToLogin={switchToLogin}
      />

      <HeroSection isLoggedIn={isLoggedIn} user={loggedInUser} />
      <AboutSection />
      <FeaturesSection />
      <ContactSection />
      <Footer />
    </div>
  )
}

export default Home
