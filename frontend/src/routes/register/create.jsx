import { createFileRoute, redirect, useNavigate } from '@tanstack/react-router'
import { Link } from "@tanstack/react-router";
import Navbar from "../../components/Navbar";
import HeroMainImg from "../../assets/hero-main-img.svg";
import HeroExtensionImg from "../../assets/hero-tail-bg.svg";
import CloseButton from "../../assets/close-button.svg";
import "../../styles/create.css";
import { use, useEffect, useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import apis from '../../api/api.js';
import { OrbitProgress } from 'react-loading-indicators';
import { AuthContext } from '../../contexts/AuthContext.jsx';

export const Route = createFileRoute("/register/create")({
    beforeLoad: async ({ context }) => {
        const registerUserData = context.queryClient.getQueryData("registerUser");
        const confirmUserData = context.queryClient.getQueryData("confirmUser");

        if (!confirmUserData || !registerUserData) {
            throw redirect({ to: "/register" });
        }

        const data = {
            ...confirmUserData, ...registerUserData
        }
        return { data };
    },
    loader: async ({ context }) => {
        return { ...context.data }
    },
    component: Create,
})

function Create() {
    const authContext = use(AuthContext);
    if (!authContext) {
        throw new Error('Auth context failed.');
    }
    const { handleSetAuth, handleRemoveAuth, auth } = authContext;
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const { token, email } = Route.useLoaderData();
    const navigate = useNavigate();
    const { mutate, isError, error, isPending } = useMutation({
        mutationFn: apis.createUser,
        onSuccess: (_) => {
            localStorage.clear();
            navigate({ to: "/login" });
        },
        onError: (error) => {
            setPassword("")
            setConfirmPassword("")
            throw error;
        },
    });

    const handleSubmit = (e) => {
        e.preventDefault();

        if (password.length === 0 || confirmPassword.length === 0) {
            return;
        }

        mutate({
            "password": password,
            "confirmPassword": confirmPassword,
            "email": email,
            "token": token
        });
    };

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    };

    const handleConfirmPasswordChange = (e) => {
        setConfirmPassword(e.target.value);
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
            <div className="create-overlay">
                <div className="create-overlay-box-container">
                    <Link className="close-button" to="/home"><img alt="close-button" src={CloseButton} /></Link>
                    <h1>Create your account.</h1>
                    <input value={password} onChange={handlePasswordChange} className={isError ? 'create-password-invalid-field' : 'create-password-field'} placeholder={isError ? error.message : 'your password'} type="password" />
                    <input value={confirmPassword} onChange={handleConfirmPasswordChange} className={isError ? 'create-confirm-password-invalid-field' : 'create-confirm-password-field'} placeholder={isError ? error.message : 'confirm your password'} type="password" />
                    {isPending ? <div className="create-submit-loading"><OrbitProgress style={{ fontSize: "0.3em" }} variant="track-disc" dense color="#ff8000" size="small" text="" textColor="#454080" /></div> : <a className='create-submit-button' onClick={handleSubmit}>Create</a>}
                    <p className='create-subtext'>By clicking "Create”, you accept Trippers’ Gear<br></br> Rental PH Terms of Service and Privacy Policy.</p>
                </div>

            </div>
        </>)
}
