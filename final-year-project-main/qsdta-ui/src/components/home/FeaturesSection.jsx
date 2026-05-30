import { MessageSquare, Shield, Zap } from "lucide-react"

function FeaturesSection() {
  const features = [
    {
      icon: <Shield className="h-12 w-12 text-purple-500 mb-4" />,
      title: "Quantum Encryption",
      description:
        "Messages are secured using quantum key distribution, making them theoretically impossible to intercept without detection.",
    },
    {
      icon: <MessageSquare className="h-12 w-12 text-cyan-500 mb-4" />,
      title: "Seamless Communication",
      description: "Enjoy a smooth and intuitive chat experience with real-time messaging and connection management.",
    },
    {
      icon: <Zap className="h-12 w-12 text-yellow-500 mb-4" />,
      title: "Lightning Fast",
      description:
        "Our optimized infrastructure ensures your messages are delivered instantly, regardless of distance.",
    },
  ]

  return (
    <section id="features" className="py-20 bg-black">
      <div className="container mx-auto">
        <h2 className="text-4xl font-bold mb-12 text-center bg-gradient-to-r from-purple-400 to-cyan-400 bg-clip-text text-transparent">
          Quantum Features
        </h2>
        <div className="grid md:grid-cols-3 gap-8">
          {features.map((feature, index) => (
            <div key={index} className="bg-gray-900 p-6 rounded-xl border border-gray-800">
              {feature.icon}
              <h3 className="text-xl font-bold mb-3 text-white">{feature.title}</h3>
              <p className="text-gray-300">{feature.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

export default FeaturesSection
