import { createFileRoute } from '@tanstack/react-router';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import "../styles/shop.css";
import RentalCard from '../components/RentalCard';
import Arrow from "../assets/arrow.svg";

export const Route = createFileRoute('/shop')({
    component: Shop,
})

function Shop() {
    return (
        <>
            <Navbar />
            <section className='shop-section-1-container'>
                <h1 className='shop-section-1-text'>Browse Our Latest Gears</h1>
            </section>
            <section className='shop-outer-2-container'>
                <div className='shop-section-2-container'>
                    <aside className='shop-filter-container'>
                    </aside>
                    <div className='shop-right-container'>
                        <div className='shop-search-container'><input className='shop-search-field' type='text' placeholder='Search' /></div>
                        <div className='shop-latter-container'>
                            <div className='shop-gadgets-container'>
                                <RentalCard />
                                <RentalCard />
                                <RentalCard />
                                <RentalCard />
                                <RentalCard />
                                <RentalCard />
                                <RentalCard />
                                <RentalCard />
                                <RentalCard />
                            </div>
                            <div className='shop-pages-container'>
                                <div className='shop-arrow-container shop-left-arrow-container'><a><img alt='left-arrow' src={Arrow} /></a></div>
                                <button className='shop-page-button-enabled shop-page-button' type='button'><span>1</span></button>
                                <button className='shop-page-button' type='button'><span>2</span></button>
                                <button className='shop-page-button' type='button'><span>3</span></button>
                                <div className='shop-arrow-container shop-right-arrow-container'><a><img alt='right-arrow' src={Arrow} /></a></div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <Footer />
        </>
    )
}
