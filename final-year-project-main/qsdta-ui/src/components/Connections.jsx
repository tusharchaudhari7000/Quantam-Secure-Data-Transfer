"use client";

import { useState, useEffect } from "react";
import axios from "axios";
import Sidebar from "./Sidebar";
import { Link } from "react-router-dom";

function Connections() {
  const [connections, setConnections] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [loggedInUserUUID, setLoggedInUserUUID] = useState("");

  // Load UUID from localStorage on mount
  useEffect(() => {
    const userStr = localStorage.getItem("loggedInUser");
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        setLoggedInUserUUID(user.uuid);
      } catch (e) {
        console.error("Failed to parse user from localStorage");
      }
    }
  }, []);

  useEffect(() => {
    const fetchConnections = async () => {
      if (!loggedInUserUUID) return;

      setLoading(true);
      setError(null);

      try {
        const response = await axios.get(
          `http://${window.location.hostname}:8080/qsdta/api/user/connection/list?uuid=${loggedInUserUUID}`,
          { headers: { "Content-Type": "application/json" } }
        );
        setConnections(response.data.content || response.data);
      } catch (error) {
        console.error("Error fetching connections:", error.response?.data || error.message);
        setError("Failed to load connections. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchConnections();
  }, [loggedInUserUUID]);

  const filteredConnections = connections.filter((conn) => {
    const otherUser = conn.authUser1.uuid === loggedInUserUUID ? conn.authUser2 : conn.authUser1;
    return `${otherUser.firstName} ${otherUser.lastName}`.toLowerCase().includes(searchTerm.toLowerCase());
  });

  const handleDeleteConnection = async (connId) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this connection?");
    if (!confirmDelete) return;

    try {
      await axios.delete(`http://${window.location.hostname}:8080/qsdta/api/user/connection/delete`, {
        data: { id: connId },
        headers: { "Content-Type": "application/json" },
      });
      setConnections((prev) => prev.filter((conn) => conn.id !== connId));
    } catch (err) {
      console.error("Failed to delete connection:", err.response?.data || err.message);
      alert("Error deleting connection. Please try again.");
    }
  };

  return (
    <div className="flex h-screen bg-gray-900 text-white">
      <Sidebar />
      <main className="flex-1 overflow-auto p-6">
        <h1 className="text-3xl font-bold mb-6 bg-gradient-to-r from-purple-400 to-cyan-400 bg-clip-text text-transparent">
          Your Connections
        </h1>
        <input
          type="text"
          placeholder="Search..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full p-2 mb-4 text-black rounded-md"
        />
        {loading && <p>Loading...</p>}
        {error && <p className="text-red-500">{error}</p>}
        <h2 className="text-2xl font-bold mt-6">Connected Users</h2>
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3 mt-4">
          {filteredConnections.map((conn) => {
            const otherUser =
              conn.authUser1.uuid === loggedInUserUUID ? conn.authUser2 : conn.authUser1;
            return (
              <div key={otherUser.uuid} className="bg-gray-800 rounded-xl border border-gray-700 p-4 flex flex-col items-center text-center">
                {otherUser.profileImage ? (
                  <img
                    src={otherUser.profileImage}
                    alt="Profile"
                    className="w-16 h-16 rounded-full object-cover"
                    loading="lazy"
                  />
                ) : (
                  <div className="w-16 h-16 rounded-full bg-gradient-to-r from-purple-500 to-cyan-500 flex items-center justify-center text-white font-bold text-2xl">
                    {otherUser.firstName.charAt(0)}
                  </div>
                )}
                <h3 className="mt-2 text-lg font-semibold">{otherUser.firstName} {otherUser.lastName}</h3>
                <p className="text-sm text-gray-400">{otherUser.email}</p>
                <Link to={`/chat/${otherUser.uuid}`} className="w-full mt-4">
                  <button className="w-full px-4 py-2 rounded-md bg-gradient-to-r from-purple-600 to-cyan-600 hover:from-purple-700 hover:to-cyan-700 text-white">
                    Chat
                  </button>
                </Link>
                <button
                  className="w-full mt-2 px-4 py-2 rounded-md bg-gradient-to-r from-purple-600 to-cyan-600 hover:from-purple-700 hover:to-cyan-700 text-white"
                  onClick={() => handleDeleteConnection(conn.id)}
                >
                  Delete Connection
                </button>
              </div>
            );
          })}
        </div>
      </main>
    </div>
  );
}

export default Connections;
