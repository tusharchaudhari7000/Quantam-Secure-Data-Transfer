"use client"

import { useState, useRef, useEffect } from "react"
import { useParams, Link } from "react-router-dom"
import axios from "axios"
import Sidebar from "./Sidebar"
import { SendIcon, Paperclip } from "lucide-react"

function Chat() {
  const params = useParams()
  const userId = params.userId
  const [connections, setConnections] = useState([])
  const [user, setUser] = useState(null)
  const [messages, setMessages] = useState([])
  const [newMessage, setNewMessage] = useState("")
  const [loggedInUserUUID, setLoggedInUserUUID] = useState("")
  const [selectedFile, setSelectedFile] = useState(null)
  const fileInputRef = useRef(null)
  const messagesEndRef = useRef(null)

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

  useEffect(() => {
    if (!loggedInUserUUID) return
    const fetchConnections = async () => {
      try {
        const response = await axios.get(
          `http://${window.location.hostname}:8080/qsdta/api/user/connection/list?uuid=${loggedInUserUUID}`
        )
        setConnections(response.data.content || response.data)
      } catch (error) {
        console.error("Error fetching connections:", error)
      }
    }
    fetchConnections()
  }, [loggedInUserUUID])

  useEffect(() => {
    if (connections.length > 0) {
      const foundUser = connections.find(
        (conn) => conn.authUser1.uuid === userId || conn.authUser2.uuid === userId
      )
      if (foundUser) {
        const chatUser = foundUser.authUser1.uuid === userId ? foundUser.authUser1 : foundUser.authUser2
        setUser(chatUser)
        fetchMessages(foundUser.id)
      }
    }
  }, [connections, userId])

  const fetchMessages = async (connectionId) => {
    try {
      const response = await axios.post(`http://${window.location.hostname}:8080/qsdta/api/connection-data/all`, {
        id: connectionId
      })

      const decodedMessages = response.data.map((msg) => {
        const isFile = msg.dataType !== "String"
        return {
          id: msg.id,
          content: isFile ? msg.byteData : atob(msg.byteData),
          sender: msg.owner === loggedInUserUUID ? "user" : "contact",
          timestamp: new Date(msg.timestamp || Date.now()),
          isFile: isFile,
          fileName: msg.fileName || null,
          fileType: msg.dataType
        }
      })

      setMessages(decodedMessages)
    } catch (error) {
      console.error("Error fetching messages:", error)
    }
  }

  const handleSendMessage = async (e) => {
    e.preventDefault()

    if (!newMessage.trim() && !selectedFile) return

    const foundUser = connections.find(
      (conn) => conn.authUser1.uuid === userId || conn.authUser2.uuid === userId
    )
    if (!foundUser) return

    const connectionId = foundUser.id

    let newMsg
    if (selectedFile) {
      const reader = new FileReader()
      reader.onloadend = async () => {
        const base64File = reader.result.split(",")[1]

        newMsg = {
          userConnection: { id: connectionId },
          dataType: selectedFile.type || "application/octet-stream",
          byteData: base64File,
          owner: loggedInUserUUID,
          fileName: selectedFile.name
        }

        await sendMessage(newMsg, true)
      }
      reader.readAsDataURL(selectedFile)
      setSelectedFile(null)
    } else {
      newMsg = {
        userConnection: { id: connectionId },
        dataType: "String",
        byteData: btoa(newMessage),
        owner: loggedInUserUUID
      }

      await sendMessage(newMsg, false)
    }

    setNewMessage("")
  }

  const sendMessage = async (msg, isFile) => {
    try {
      const response = await axios.post(`http://${window.location.hostname}:8080/qsdta/api/connection-data/add`, msg, {
        headers: { "Content-Type": "application/json" }
      })

      const savedMessage = {
        id: response.data.id || Date.now().toString(),
        content: isFile ? msg.byteData : newMessage,
        sender: "user",
        timestamp: new Date(),
        isFile: isFile,
        fileName: msg.fileName || null,
        fileType: msg.dataType
      }

      setMessages((prev) => [...prev, savedMessage])
    } catch (error) {
      console.error("Error sending message:", error.response?.data || error.message)
    }
  }

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
  }, [messages])

  const handleFileClick = () => {
    fileInputRef.current.click()
  }

  const handleFileChange = (e) => {
    const file = e.target.files[0]
    if (file) setSelectedFile(file)
  }

  if (!user) {
    return (
      <div className="flex h-screen bg-black text-white">
        <Sidebar />
        <main className="flex-1 p-6">
          <p>User not found</p>
          <Link to="/connections">
            <button className="mt-4 px-4 py-2 bg-gradient-to-r from-purple-600 to-cyan-600 rounded-md text-white">
              Back to Connections
            </button>
          </Link>
        </main>
      </div>
    )
  }

  return (
    <div className="flex h-screen bg-black text-white">
      <Sidebar />
      <div className="flex-1 flex flex-col bg-gray-900">
        <div className="bg-black p-4 border-b border-gray-800 flex items-center">
          <div className="relative">
            <div className="w-10 h-10 rounded-full bg-gradient-to-r from-purple-500 to-cyan-500 flex items-center justify-center text-white font-bold">
              {user.firstName.charAt(0)}
            </div>
            <div className="absolute bottom-0 right-0 w-3 h-3 bg-green-500 rounded-full border-2 border-black"></div>
          </div>
          <div className="ml-3">
            <h2 className="font-bold text-white">{user.firstName} {user.lastName}</h2>
            <p className="text-xs text-gray-400">Online</p>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto p-4 space-y-4">
          {messages.map((message) => (
            <div key={message.id} className={`flex ${message.sender === "user" ? "justify-end" : "justify-start"}`}>
              <div
                className={`max-w-[80%] rounded-2xl px-4 py-2 ${
                  message.sender === "user"
                    ? "bg-gradient-to-r from-purple-600 to-cyan-600 text-white"
                    : "bg-gray-800 text-white"
                }`}
              >
                {message.isFile ? (
                  message.fileType.startsWith("image/") ? (
                    <img
                      src={`data:${message.fileType};base64,${message.content}`}
                      alt="attachment"
                      className="max-w-xs rounded"
                    />
                  ) : (
                    <a
                      href={`data:${message.fileType};base64,${message.content}`}
                      download={message.fileName}
                      className="underline text-sm"
                    >
                      📎 {message.fileName}
                    </a>
                  )
                ) : (
                  <p>{message.content}</p>
                )}
                <p className="text-xs opacity-70 mt-1">
                  {message.timestamp.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}
                </p>
              </div>
            </div>
          ))}
          <div ref={messagesEndRef} />
        </div>

        <form onSubmit={handleSendMessage} className="bg-black p-4 border-t border-gray-800">
          <div className="flex gap-2 items-center">
            <input
              ref={fileInputRef}
              type="file"
              className="hidden"
              onChange={handleFileChange}
            />
            <button
              type="button"
              onClick={handleFileClick}
              className="text-gray-400 hover:text-white"
              title="Attach file"
            >
              <Paperclip className="h-5 w-5" />
            </button>
            <input
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Type your message..."
              className="flex-1 bg-gray-800 border border-gray-700 rounded-md px-4 py-2 text-white focus:outline-none focus:ring-2 focus:ring-cyan-500"
            />
            <button
              type="submit"
              className="bg-gradient-to-r from-purple-600 to-cyan-600 hover:from-purple-700 hover:to-cyan-700 rounded-md px-4 py-2"
            >
              <SendIcon className="h-5 w-5" />
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default Chat
