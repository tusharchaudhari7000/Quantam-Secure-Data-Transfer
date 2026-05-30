function AboutSection() {
  return (
    <section id="about" className="mt-8 py-20 bg-gradient-to-b from-black to-gray-900">
      <div className="container mx-auto">
        <h2 className="text-4xl font-bold mb-12 text-center bg-gradient-to-r from-purple-400 to-cyan-400 bg-clip-text text-transparent">
          About QuantumChat
        </h2>

        <div className="relative mb-16">
          {/* Quantum particle effect */}
          <div className="absolute inset-0 overflow-hidden">
            {[...Array(20)].map((_, i) => (
              <div
                key={i}
                className="absolute rounded-full bg-cyan-400"
                style={{
                  width: `${Math.random() * 6 + 2}px`,
                  height: `${Math.random() * 6 + 2}px`,
                  left: `${Math.random() * 100}%`,
                  top: `${Math.random() * 100}%`,
                  opacity: Math.random() * 0.5 + 0.2,
                  animation: `pulse ${Math.random() * 3 + 2}s infinite`,
                  animationDelay: `${Math.random() * 2}s`,
                }}
              />
            ))}
          </div>

          {/* Main content */}
          <div className="grid md:grid-cols-2 gap-12 relative z-10">
            {/* Left column - Quantum visualization */}
            <div className="relative">
              <div className="absolute -inset-4 rounded-xl blur-xl bg-gradient-to-r from-purple-600 to-cyan-400 opacity-30"></div>
              <div className="relative h-full flex items-center justify-center bg-black bg-opacity-70 p-8 rounded-xl border border-gray-800">
                <div className="relative w-64 h-64">
                  {/* Quantum sphere */}
                  <div className="absolute inset-0 rounded-full border-4 border-purple-500 opacity-20 animate-[spin_8s_linear_infinite]"></div>
                  <div className="absolute inset-2 rounded-full border-4 border-cyan-500 opacity-20 animate-[spin_12s_linear_infinite_reverse]"></div>
                  <div className="absolute inset-4 rounded-full border-4 border-purple-400 opacity-20 animate-[spin_10s_linear_infinite]"></div>

                  {/* Core */}
                  <div className="absolute inset-0 flex items-center justify-center">
                    <div className="w-16 h-16 rounded-full bg-gradient-to-br from-purple-600 to-cyan-400 animate-pulse shadow-lg shadow-purple-500/50"></div>
                  </div>

                  {/* Orbiting particles */}
                  {[...Array(3)].map((_, i) => (
                    <div
                      key={i}
                      className="absolute w-full h-full animate-[spin_5s_linear_infinite]"
                      style={{ animationDelay: `${i * -1.5}s` }}
                    >
                      <div
                        className="absolute w-4 h-4 rounded-full bg-cyan-400 shadow-lg shadow-cyan-400/50"
                        style={{
                          left: "50%",
                          top: i % 2 === 0 ? "0" : "100%",
                          transform: "translateX(-50%)",
                        }}
                      ></div>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            {/* Right column - Text content */}
            <div className="flex flex-col justify-center">
              <h3 className="text-2xl font-bold mb-6 text-white">Quantum-Secured Messaging</h3>
              <p className="text-lg text-gray-300 mb-6">
                QuantumChat harnesses the power of quantum mechanics to revolutionize secure communications. Unlike
                traditional encryption that relies on mathematical complexity, our technology leverages the fundamental
                laws of physics.
              </p>
              <div className="space-y-4">
                <div className="flex items-start">
                  <div className="w-10 h-10 rounded-full bg-purple-900 flex items-center justify-center flex-shrink-0 mr-4">
                    <div className="w-5 h-5 rounded-full bg-purple-400"></div>
                  </div>
                  <div>
                    <h4 className="font-bold text-white mb-1">Quantum Key Distribution</h4>
                    <p className="text-gray-300">
                      Encryption keys protected by the laws of quantum physics, making interception impossible without
                      detection.
                    </p>
                  </div>
                </div>
                <div className="flex items-start">
                  <div className="w-10 h-10 rounded-full bg-cyan-900 flex items-center justify-center flex-shrink-0 mr-4">
                    <div className="w-5 h-5 rounded-full bg-cyan-400"></div>
                  </div>
                  <div>
                    <h4 className="font-bold text-white mb-1">Entanglement-Based Security</h4>
                    <p className="text-gray-300">
                      Messages secured through quantum entanglement, creating an unbreakable bond between sender and
                      receiver.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Bottom stats */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-6 text-center">
          <div className="bg-black bg-opacity-50 p-6 rounded-xl border border-gray-800">
            <div className="text-3xl font-bold text-cyan-400 mb-2">256-bit</div>
            <div className="text-gray-400">Quantum Encryption</div>
          </div>
          <div className="bg-black bg-opacity-50 p-6 rounded-xl border border-gray-800">
            <div className="text-3xl font-bold text-purple-400 mb-2">0.01ms</div>
            <div className="text-gray-400">Latency</div>
          </div>
          <div className="bg-black bg-opacity-50 p-6 rounded-xl border border-gray-800">
            <div className="text-3xl font-bold text-cyan-400 mb-2">99.99%</div>
            <div className="text-gray-400">Uptime</div>
          </div>
          <div className="bg-black bg-opacity-50 p-6 rounded-xl border border-gray-800">
            <div className="text-3xl font-bold text-purple-400 mb-2">10M+</div>
            <div className="text-gray-400">Users Protected</div>
          </div>
        </div>
      </div>
    </section>
  )
}

export default AboutSection
