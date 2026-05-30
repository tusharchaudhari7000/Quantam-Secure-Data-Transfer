import { AtomIcon } from "lucide-react"

function Footer() {
  return (
    <footer className="bg-black py-10 border-t border-gray-800">
      <div className="container mx-auto text-center text-gray-400">
        <div className="flex items-center justify-center gap-2 mb-4">
          <AtomIcon className="h-6 w-6 text-cyan-400" />
          <span className="text-lg font-bold text-white">QuantumChat</span>
        </div>
        <p>© {new Date().getFullYear()} QuantumChat. All rights reserved.</p>
      </div>
    </footer>
  )
}

export default Footer
