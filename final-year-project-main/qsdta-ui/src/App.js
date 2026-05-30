import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Home from "./components/Home"
import Dashboard from "./components/Dashboard"
import Connections from "./components/Connections"
import Chat from "./components/Chat"
import "./App.css"

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/connections" element={<Connections />} />
        <Route path="/chat/:userId" element={<Chat />} />
      </Routes>
    </Router>
  )
}

export default App

