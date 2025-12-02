import { createFileRoute, useNavigate } from '@tanstack/react-router'
import { Link } from "@tanstack/react-router";
import Navbar from "../../components/Navbar";
import HeroMainImg from "../../assets/hero-main-img.svg";
import HeroExtensionImg from "../../assets/hero-tail-bg.svg";
import CloseButton from "../../assets/close-button.svg";
import "../../styles/register.css";
import { use, useEffect, useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import apis from '../../api/api';
import { OrbitProgress } from 'react-loading-indicators';
import { AuthContext } from '../../contexts/AuthContext.jsx';

export const Route = createFileRoute("/register/")({
    component: Register,
})

function Register() {
    const authContext = use(AuthContext);
    if (!authContext) {
        throw new Error('Auth context failed.');
    }
    const { handleSetAuth, handleRemoveAuth, auth } = authContext;
    const queryClient = useQueryClient();
    const [email, setEmail] = useState("");

    const navigate = useNavigate();

    const { mutate, isError, error, isPending } = useMutation({
        mutationKey: ["registerUser"],
        mutationFn: apis.registerUser,
        onSuccess: (data) => {
            localStorage.setItem("email", data.email);
            localStorage.setItem("codeResendAvailableAt", data.codeResendAvailableAt);
            localStorage.setItem("validUntil", data.validUntil);
            queryClient.setQueryData("registerUser", {
                "email": data.email,
                "codeResendAvailableAt": data.codeResendAvailableAt,
                "validUntil": data.validUntil
            });
            navigate({ to: "/register/confirm" });
        },
        onError: (error) => {
            setEmail("")
            throw error.email;
        }
    });


    const handleSubmit = (e) => {
        e.preventDefault();

        if (email.length === 0) {
            return;
        }

        mutate({
            "email": email,
            "timezone": Intl.DateTimeFormat().resolvedOptions().timeZone,
        });

    };

    const handleChange = (e) => {
        setEmail(e.target.value);
    };

    useEffect(() => {
        if (auth.accessToken != "") {
            navigate({ to: "/shop" });
        }

        document.body.style.position = 'fixed';
        document.body.style.width = '100%';

        const prevEmail = localStorage.getItem("email");
        const prevCodeResendAvailableAt = localStorage.getItem("codeResendAvailableAt");
        const prevValidUntil = localStorage.getItem("validUntil");

        if (prevEmail === null || prevCodeResendAvailableAt === null || prevValidUntil === null || new Date > new Date(prevValidUntil)) {
            localStorage.removeItem("email");
            localStorage.removeItem("codeResendAvailableAt");
            localStorage.removeItem("validUntil");

            return () => {
                document.body.style.position = '';
                document.body.style.width = '';
            }
        }

        queryClient.setQueryData("registerUser", {
            "email": prevEmail,
            "codeResendAvailableAt": prevCodeResendAvailableAt,
            "prevValidUntil": prevValidUntil
        });

        navigate({ to: "/register/confirm" });
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
            <div className="register-overlay">
                <div className="register-overlay-box-container">
                    <Link className="close-button" to="/home"><img alt="close-button" src={CloseButton} /></Link>
                    <h1>Enter your email.</h1>
                    <input value={email} onChange={handleChange} className={isError ? 'register-email-invalid-field' : 'register-email-field'} placeholder={isError ? error.email : 'your@email.com'} type="text" />
                    {isPending ? <div className="register-submit-loading"><OrbitProgress style={{ fontSize: "0.3em" }} variant="track-disc" dense color="#ff8000" size="small" text="" textColor="#454080" /></div> : <a className='register-submit-button' onClick={handleSubmit}>Submit</a>}
                    <span className='register-already-account-container'>
                        Already have an account?
                        <Link className='register-login-button' to="/login">Login</Link>
                    </span>
                    <p className='register-subtext'>By clicking "Submit”, you accept Trippers’ Gear<br></br> Rental PH Terms of Service and Privacy Policy.</p>
                </div>

            </div>
        </>)
}
