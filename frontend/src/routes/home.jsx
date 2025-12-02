import { Link } from "@tanstack/react-router";
import { createFileRoute } from "@tanstack/react-router";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import HeroMainImg from "../assets/hero-main-img.svg";
import HeroExtensionImg from "../assets/hero-tail-bg.svg";
import Item1 from "../assets/hero-item-1.svg";
import Item2 from "../assets/hero-item-2.svg";
import Item3 from "../assets/hero-item-3.svg";
import ReviewCard from "../components/ReviewCard";
import "../styles/home.css";
import { useSuspenseQuery } from "@tanstack/react-query";
import apis from "../api/api";

export const Route = createFileRoute('/home')({
    component: Home,
    loader: async ({ context }) => {
        await context.queryClient.prefetchQuery({
            queryKey: ["reviews"],
            queryFn: apis.getReviews,
        })
    }
})

function Home() {
    // const reviewCardData = useSuspenseQuery({
    //     queryKey: ["reviews"],
    //     queryFn: apis.getReviews,
    // });
    // console.log(reviewCardData.data);

    const cardElements = []
    for (let i = 0; i < 4; ++i) {
        const cardDatum = {
            "profile_name": "Joseph Reggie Arpon",
            "initials": function(profile_name) {
                const names = profile_name.split(" ");
                const namesInitial = names.reduce((accum, item) => {
                    accum = accum.concat(item.charAt(0).toUpperCase())
                    return accum;
                }, "")
                return namesInitial
            },
            "review": "”Excellent experience from start to finish. Transparent pricing, flexible rental terms, and well-maintained units.\
            Their customer service team responded quickly to all my questions. I’ll be recommending them to friends and family.",
            "location": "Amadeo Cavite",
            "rating": 4.5
        }

        cardElements.push(<ReviewCard key={i} cardDatum={cardDatum} />);
    }
    // const cardElements = reviewCardData.data?.map(datum => {
    //     const cardDatum = {
    //         "profile_name": datum.profile_name,
    //         "initials": function() {
    //             const names = this.profile_name.split(" ");
    //             const namesInitial = names.reduce((accum, item) => { accum.concat(item.charAt(0).toUpperCase()) })
    //             return namesInitial
    //         },
    //         "review": datum.review,
    //         "location": datum.location,
    //         "rate": datum.rate
    //     }
    //
    //     return <ReviewCard cardDatum={cardDatum} />
    // });
    // TODO: Make the rent-button dynamic.

    return (
        <>
            <Navbar />
            <header className="header-container">
                <div className="left-data-container">
                    <h1 className="header-text">Rent a Gear, <br></br>and Capture <br></br>the Moments!</h1>
                    <h5 className="header-subtext">"Gamit ang tamang tools, bawat trip mo magiging <br></br>unforgettable. Let’s make those memories last!"</h5>
                    <div className="header-button-container"><Link className="rent-button" to="/register">RENT NOW</Link></div>
                </div>
                <img alt="hero-img" src={HeroMainImg} />
            </header>
            <header className="header-image-extension-container"><img className="header-extension" alt="hero-img-extension" src={HeroExtensionImg} /></header>
            <section className="section-1-container">
                <h1 className="section-1-text">Why <span className="section-1-text-highlight">CHOOSE</span><br></br>Us?</h1>
                <div className="section-item-container">
                    <div>
                        <img src={Item1} alt="item-1" />
                        <h3>Affordable and<br></br>Flexible Rental Plans</h3>
                        <h6>We offer budget-friendly pricing with customizable rental durations to match every customer need.</h6>
                    </div>
                    <div>
                        <img src={Item2} alt="item-2" />
                        <h3>Well-Maintained,<br></br>Reliable Equipment</h3>
                        <h6>We provide clean, regularly inspected rental units ensuring consistent quality and dependable performance.</h6>
                    </div>
                    <div>
                        <img src={Item3} alt="item-3" />
                        <h3>Fast, Friendly, and<br></br>Hassle-Free Service</h3>
                        <h6>We deliver smooth transactions, quick assistance, and customer-focused support for a stress-free experience.</h6>
                    </div>
                </div>
            </section>
            <section className="section-2-container">
                <div className="section-2-text-container">
                    <h1 className="section-2-text">Here's What Our Customers Say</h1>
                </div>
                <div className="review-cards-container">
                    {cardElements}
                </div>
            </section>
            <section className="section-3-container">
                <div className="section-3-bg">
                    <iframe className="section-3-map" src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3866.175239714716!2d120.91736567518178!3d14.301248684335423!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3397d55ac4deca35%3A0xc500ae48392d9e0e!2sVantrippers%20travel%20and%20tours!5e0!3m2!1sen!2sph!4v1764035674380!5m2!1sen!2sph" allowFullScreen={false} loading="lazy" referrerPolicy="no-referrer-when-downgrade"></iframe>
                    <div className="section-3-data">
                        <div className="section-3-name-container">
                            <div className="section-3-firstname-field">
                                <label htmlFor="">First Name</label>
                                <input type="text" />
                            </div>
                            <div className="section-3-lastname-field">
                                <label htmlFor="">Last Name</label>
                                <input type="text" />
                            </div>
                        </div>
                        <div className="section-3-email-field">
                            <label htmlFor="">Email</label>
                            <input type="text" />
                        </div>
                        <div className="section-3-message-field">
                            <label htmlFor="">Message</label>
                            <textarea cols={5} />
                        </div>
                        <button className="section-3-inquire-button">INQUIRE</button>
                    </div>
                </div>
            </section>
            <Footer />
        </>
    )
}
