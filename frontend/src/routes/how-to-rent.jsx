import { createFileRoute } from '@tanstack/react-router'
import Navbar from '../components/Navbar'
import Footer from '../components/Footer'

export const Route = createFileRoute('/how-to-rent')({
    component: HowToRent,

})

function HowToRent() {
    return (
        <>
            <Navbar />
            <Footer />
        </>
    )
}
