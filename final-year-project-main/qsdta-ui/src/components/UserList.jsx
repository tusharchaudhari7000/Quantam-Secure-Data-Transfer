"use client";

import { useEffect, useState, useCallback } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import { MultiStepLoader } from "./loader/multi-step-loader";
import { IconSquareRoundedX } from "@tabler/icons-react";

function UserList({ showConnectButton, onUserConnected }) {
  const [users, setUsers] = useState([]);
  const [connectedIds, setConnectedIds] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [multiLoading, setMultiLoading] = useState(false);
  const [loggedInUserUUID, setLoggedInUserUUID] = useState("");

  const loadingStates = [
    { text: "Authenticating users" },
    { text: "Implementing BB84 protocol" },
    { text: "Establishing Connection" },
    { text: "Distributing Quantum Key" },
    { text: "Connection Established" }
  ];

  // Get logged-in user's UUID from localStorage
  useEffect(() => {
    const userStr = localStorage.getItem("loggedInUser");
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        setLoggedInUserUUID(user.uuid);
      } catch (e) {
        console.error("Failed to parse loggedInUser from localStorage");
      }
    }
  }, []);

  // Fetch Users API call
  const fetchUsers = async () => {
    try {
      const userRes = await axios.get(`http://${window.location.hostname}:8080/qsdta/api/users/page?page=0&size=20`);
      return userRes.data.content || userRes.data;
    } catch (err) {
      console.error("Error fetching users:", err);
      return [];
    }
  };

  // Fetch Connections API call
  const fetchConnections = async () => {
    try {
      const connRes = await axios.get(`http://${window.location.hostname}:8080/qsdta/api/user/connection/list?uuid=${loggedInUserUUID}`);
      return connRes.data.content || connRes.data;
    } catch (err) {
      console.error("Error fetching connections:", err);
      return [];
    }
  };

  // Combine both API calls to fetch users and connections
  const fetchUsersAndConnections = useCallback(async () => {
    if (!loggedInUserUUID) return;

    setLoading(true);

    const fetchedUsers = await fetchUsers();
    const connections = await fetchConnections();

    const connectedUUIDs = new Set();
    connections.forEach((conn) => {
      if (conn.authUser1.uuid !== loggedInUserUUID) {
        connectedUUIDs.add(conn.authUser1.uuid);
      }
      if (conn.authUser2.uuid !== loggedInUserUUID) {
        connectedUUIDs.add(conn.authUser2.uuid);
      }
    });

    setUsers(fetchedUsers);
    setConnectedIds(connectedUUIDs);
    setLoading(false);
  }, [loggedInUserUUID]);

  // Connect User API call
  const handleConnect = async (user) => {
    try {
      setMultiLoading(true);

      const response = await axios.post(`http://${window.location.hostname}:8080/qsdta/api/user/connection/insert?photons=1000`, {
        authUser1: { uuid: loggedInUserUUID },
        authUser2: { uuid: user.uuid },
        createdTimestamp: new Date().toISOString(),
      });

      if (response.status === 201 || response.status === 200) {
        setConnectedIds((prev) => new Set([...prev, user.uuid]));
        onUserConnected?.(user);
      }
    } catch (error) {
      console.error("Failed to establish connection:", error);
    } finally {
      setTimeout(() => {
        setMultiLoading(false);
      }, 5000);
    }
  };

  useEffect(() => {
    fetchUsersAndConnections();
  }, [fetchUsersAndConnections]);

  return (
    <div className="relative">
      <MultiStepLoader loadingStates={loadingStates} loading={multiLoading} duration={1000} />

      {multiLoading && (
        <button
          className="fixed top-4 right-4 text-black dark:text-white z-[120]"
          onClick={() => setMultiLoading(false)}
        >
          <IconSquareRoundedX className="h-10 w-10" />
        </button>
      )}

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {loading && <p>Loading users...</p>}
        {error && <p className="text-red-500">{error}</p>}
        {!loading && !error && users.length === 0 && <p>No users found.</p>}

        {users.map((user) => (
          <div key={user.uuid} className="bg-gray-900 rounded-xl border border-gray-800 p-4 flex flex-col">
            <div className="flex items-center mb-4">
              <div className="w-12 h-12 rounded-full bg-gradient-to-r from-purple-500 to-cyan-500 flex items-center justify-center text-white font-bold text-lg">
                {user.firstName.charAt(0)}
              </div>
              <div className="ml-3">
                <h3 className="font-bold text-white">{user.firstName} {user.lastName}</h3>
                <p className="text-sm text-gray-400">{user.status || "Offline"}</p>
              </div>
            </div>

            <div className="mt-auto flex gap-2">
              {showConnectButton ? (
                <button
                  className={`w-full px-4 py-2 rounded-md ${connectedIds.has(user.uuid)
                    ? "bg-gradient-to-r from-purple-600 to-cyan-600 text-white"
                    : "border border-gray-700 hover:bg-gray-800 text-white"}`}
                  onClick={() => handleConnect(user)}
                  disabled={connectedIds.has(user.uuid) || multiLoading}
                >
                  {connectedIds.has(user.uuid) ? "Connected" : "Connect"}
                </button>
              ) : (
                <Link to={`/chat/${user.uuid}`} className="w-full">
                  <button className="w-full px-4 py-2 rounded-md bg-gradient-to-r from-purple-600 to-cyan-600 hover:from-purple-700 hover:to-cyan-700 text-white">
                    Chat
                  </button>
                </Link>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default UserList;
