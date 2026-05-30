import { Link } from "react-router-dom"
import { AtomIcon } from "lucide-react"

function HeroSection({ isLoggedIn }) {
  return (
    <section className="container mx-auto py-20 flex flex-col items-center text-center">
      <div className="relative">
        <div className="absolute -inset-1 rounded-full blur-xl bg-gradient-to-r from-purple-600 to-cyan-400 opacity-70"></div>
        <AtomIcon className="relative h-24 w-24 text-cyan-400 mb-6" />
      </div>
      <h1 className="text-5xl font-bold mb-6 bg-gradient-to-r from-purple-400 to-cyan-400 bg-clip-text text-transparent">
        Quantum-Secured Communication
      </h1>
      <p className="text-xl text-gray-300 max-w-2xl mb-10">
        Experience the future of secure messaging with quantum cryptography. Your conversations, protected by the
        fundamental laws of physics.
      </p>
      {isLoggedIn && (
        <Link to="/dashboard">
          <button className="px-6 py-3 bg-gradient-to-r from-purple-600 to-cyan-600 hover:from-purple-700 hover:to-cyan-700 text-white rounded-md text-lg font-medium">
            Get Started
          </button>
        </Link>
      )}
    </section>
  )
}

export default HeroSection
