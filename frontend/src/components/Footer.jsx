import BrandLogoWhite from "../assets/brand-logo-white.svg"
import Tiktok from "../assets/tiktok.svg"
import Facebook from "../assets/facebook.svg"
import Instagram from "../assets/instagram.svg"
import { Link } from "@tanstack/react-router";
import "../styles/footer.css";

function Footer() {
    return (
        <div className="under-footer-container">
            <footer className="footer-container">
                <img className="logo-white" alt="logo-white" src={BrandLogoWhite} />
                <div className="footer-right-container">
                    <div className="footer-quicklinks-container">
                        <h1 className="footer-quicklinks-text">Quick Links</h1>
                        <ul className="footer-ul-container">
                            <li><Link to="#">Home</Link></li>
                            <li><Link to="#">Shop</Link></li>
                            <li><Link to="#">How To Rent</Link></li>
                            <li><Link to="#">Account</Link></li>
                        </ul>
                    </div>
                    <div className="footer-bhours-container">
                        <h1 className="footer-bhours-text">Business Hours</h1>
                        <h3>Monday to Friday:<br />9:00 am to 6:00 pm</h3>
                        <h3>Saturday and Sunday:<br />Closed</h3>
                    </div>
                    <div className="footer-contact-container">
                        <h1 className="footer-contact-text">Contact Us</h1>
                        <h3 className="footer-email">book@vantrippers<br />travelandtour.com</h3>
                        <h3 className="footer-phone-number">+63 (976) 476-4187</h3>
                    </div>
                    <div className="footer-social-container">
                        <h1 className="footer-social-text">Our Social Media</h1>
                        <div className="footer-social-links-container">
                            <a href="https://www.tiktok.com/@vantripperstravelsph"><img alt="tiktok" src={Tiktok} /></a>
                            <a href="https://www.facebook.com/Vantrippers.ph"><img alt="facebook" src={Facebook} /></a>
                            <a href="https://www.instagram.com/vantripperstravels"><img alt="tiktok" src={Instagram} /></a>
                        </div>
                    </div>
                </div>
            </footer>
            <hr className="footer-horizontal-line" />
            <p className="under-footer-text">© 2025 Trippers’ Gear Rental PH. All Rights Reserved</p>
        </div>)
}

export default Footer
