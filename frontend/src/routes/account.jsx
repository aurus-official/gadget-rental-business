import { createFileRoute } from '@tanstack/react-router'
import Navbar from '../components/Navbar'
import Footer from '../components/Footer'

export const Route = createFileRoute('/account')({
    component: Account,
})

function Account() {
    return (
        <>
            <Navbar />
            <Footer />
        </>
    )
}
