import { createFileRoute } from '@tanstack/react-router';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';

export const Route = createFileRoute('/shop')({
    component: RouteComponent,
})

function RouteComponent() {
    return (
        <>
            <Navbar />
            <Footer />
        </>
    )
}
