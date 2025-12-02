import { createFileRoute, useNavigate } from '@tanstack/react-router'
import { Link } from "@tanstack/react-router";
import Navbar from "../components/Navbar";
import HeroMainImg from "../assets/hero-main-img.svg";
import HeroExtensionImg from "../assets/hero-tail-bg.svg";
import CloseButton from "../assets/close-button.svg";
import "../styles/login.css";
import { useEffect, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import apis from '../api/api';
import { OrbitProgress } from 'react-loading-indicators';
import { use } from 'react';
import { AuthContext } from '../contexts/AuthContext.jsx';

export const Route = createFileRoute("/login")({
    loader: async ({ context }) => {
        return { data: context.data }
    },
    component: Login,
})

function Login() {
    const authContext = use(AuthContext);
    if (!authContext) {
        throw new Error('Auth context failed.');
    }
    const { handleSetAuth, handleRemoveAuth, auth } = authContext;
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    const { mutate, isError, error, isPending } = useMutation({
        mutationKey: ["loginUser"],
        mutationFn: apis.loginUser,
        onSuccess: (data) => {
            localStorage.setItem("accessToken", data.accessToken);
            const date = new Date();
            date.setMinutes(date.getMinutes() + 10);
            const auth = {
                expiry: date,
                accessToken: data.accessToken
            };
            handleSetAuth(auth);

            navigate({ to: "/shop" });
        },
        onError: (error) => {
            setEmail("");
            setPassword("");
            throw error;
        },
    });

    const handleSubmit = (e) => {
        e.preventDefault();

        if (email.length === 0 || password.length === 0) {
            return;
        }

        mutate({
            "email": email,
            "password": password,
        });
    };

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    };

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    };

    useEffect(() => {
        if (auth.accessToken != "") {
            navigate({ to: "/shop" });
        }

        document.body.style.position = 'fixed';
        document.body.style.width = '100%';

        return () => {
            document.body.style.position = '';
            document.body.style.width = '';
        }
    }, []);
    return (
        <>
            <Navbar />
            <header className="header-container">
                <div className="left-data-container">
                    <h1 className="header-text">Rent a Gear, <br></br>and Capture <br></br>the Moments!</h1>
                    <h5 className="header-subtext">"Gamit ang tamang tools, bawat trip mo magiging <br></br>unforgettable. Let’s make those memories last!"</h5>
                    <div className="header-button-container"><Link className="rent-button" to="/login">RENT NOW</Link></div>
                </div>
                <img alt="hero-img" src={HeroMainImg} />
            </header>
            <header className="header-image-extension-container"><img className="header-extension" alt="hero-img-extension" src={HeroExtensionImg} /></header>
            <div className="login-overlay">
                <div className="login-overlay-box-container">
                    <Link className="close-button" to="/home"><img alt="close-button" src={CloseButton} /></Link>
                    <h1>Login to your account.</h1>
                    <input value={email} onChange={handleEmailChange} className={isError ? 'login-email-invalid-field' : 'login-email-field'} placeholder={isError ? error.message : 'your email'} type="text" />
                    <input value={password} onChange={handlePasswordChange} className={isError ? 'login-password-invalid-field' : 'login-password-field'} placeholder={isError ? error.message : 'your password'} type="password" />
                    {isPending ? <div className="login-submit-loading"><OrbitProgress style={{ fontSize: "0.3em" }} variant="track-disc" dense color="#ff8000" size="small" text="" textColor="#454080" /></div> : <a className='register-submit-button' onClick={handleSubmit}>Login</a>}
                    <span className='login-already-account-container'>
                        Haven't created an account yet?
                        <Link className='login-register-button' to="/register">Register</Link>
                    </span>
                    <p className='login-subtext'>By clicking "Login”, you accept Trippers’ Gear<br></br> Rental PH Terms of Service and Privacy Policy.</p>
                </div>

            </div>
        </>)
}
