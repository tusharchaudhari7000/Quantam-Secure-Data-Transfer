import Sidebar from "./Sidebar"
import UserList from "./UserList"


function Dashboard() {
  return (
    <div className="flex h-screen bg-black text-white">
      <Sidebar />
      <main className="flex-1 overflow-auto p-6">
        <h1 className="text-3xl font-bold mb-6 bg-gradient-to-r from-purple-400 to-cyan-400 bg-clip-text text-transparent">
          All Users
        </h1>
        <UserList  showConnectButton={true} />
      </main>
    </div>
  )
}

export default Dashboard

