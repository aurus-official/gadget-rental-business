import { Link } from "@tanstack/react-router";
import BrandLogo from "../assets/brand-logo.svg";
import ShoppingCartIcon from "../assets/shopping-cart-icon.svg";
import "../styles/navbar.css"
import { AuthContext } from "../contexts/AuthContext";
import { use } from "react";

// TODO: Let the local storage be expired as long it has matched access token
// TODO: When doing api calls and it fails; use the refresh token endpoint, then retry
// TODO: Create function for total retry logic

function Navbar() {
    const authContext = use(AuthContext);
    if (!authContext) {
        throw new Error('Auth context failed.');
    }
    const { auth } = authContext;

    return (
        <div className="navbar-container">
            <img alt="logo" src={BrandLogo} />
            <div className="menu-container">
                <ul className="menu-ul-container">
                    <li><Link to="/home">Home</Link></li>
                    <li><Link to="#">Shop</Link></li>
                    <li><Link to="#">How To Rent</Link></li>
                    <li><Link to="#">Account</Link></li>
                    {auth.accessToken === "" ?
                        <Link className="navbar-login-button" to="/login">Login</Link> :
                        <li><Link to="#"><img className="cart-icon" alt="cart-icon" src={ShoppingCartIcon} /></Link></li>
                    }
                </ul>
            </div>
        </div>
    )
}

export default Navbar;
