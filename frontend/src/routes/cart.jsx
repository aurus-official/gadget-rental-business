import { createFileRoute } from '@tanstack/react-router'
import Navbar from '../components/Navbar'
import Footer from '../components/Footer'

export const Route = createFileRoute('/cart')({
    component: Cart,
})

function Cart() {
    return (
        <>
            <Navbar />
            <Footer />
        </>
    )
}
