import Navbar from '../components/layout/Navbar.jsx'
import Footer from '../components/layout/Footer.jsx'
import Hero from '../components/landing/Hero.jsx'
import FeaturesSection from '../components/landing/FeaturesSection.jsx'
import HowItWorks from '../components/landing/HowItWorks.jsx'
import Statistics from '../components/landing/Statistics.jsx'
import Testimonials from '../components/landing/Testimonials.jsx'
import CTASection from '../components/landing/CTASection.jsx'

function LandingPage() {
  return (
    <div className="min-h-screen bg-ink">
      <Navbar />
      <main>
        <Hero />
        <FeaturesSection />
        <HowItWorks />
        <Statistics />
        <Testimonials />
        <CTASection />
      </main>
      <Footer />
    </div>
  )
}

export default LandingPage
