import { createFileRoute, redirect, useNavigate } from '@tanstack/react-router'
import { Link } from "@tanstack/react-router";
import Navbar from "../../components/Navbar";
import HeroMainImg from "../../assets/hero-main-img.svg";
import HeroExtensionImg from "../../assets/hero-tail-bg.svg";
import CloseButton from "../../assets/close-button.svg";
import "../../styles/confirm.css";
import { use, useEffect, useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import apis from '../../api/api';
import { OrbitProgress } from 'react-loading-indicators';
import { AuthContext } from '../../contexts/AuthContext.jsx';

export const Route = createFileRoute("/register/confirm")({
    beforeLoad: async ({ context }) => {
        const registerUserData = context.queryClient.getQueryData("registerUser");
        const confirmUserData = context.queryClient.getQueryData("confirmUser") || {};

        if (!registerUserData) {
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

    component: Confirm,
})

function Confirm() {
    const authContext = use(AuthContext);
    if (!authContext) {
        throw new Error('Auth context failed.');
    }
    const { handleSetAuth, handleRemoveAuth, auth } = authContext;
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const [exitOnError, setExitOnError] = useState(false)
    const [secondsRemaining, setSecondsRemaining] = useState(0);
    const { email, token, codeResendAvailableAt } = Route.useLoaderData();
    const [code, setCode] = useState("");
    const { mutate: confirmUserMutate, isPending: confirmUserIsPending, error: confirmUserError, isError: confirmUserIsError } = useMutation({
        mutationKey: ["confirmUser"],
        mutationFn: apis.confirmUser,
        onSuccess: (data) => {
            queryClient.setQueryData("confirmUser", {
                ...data
            })
            navigate({ to: "/register/create" });
        },
        onError: (error) => {
            setCode("");
            throw error
        }
    });

    const { mutate: resendEmailMutate, isPending: resendEmailIsPending } = useMutation({
        mutationKey: ["registerUser"],
        mutationFn: apis.resendEmail,
        onSuccess: (data) => {
            localStorage.setItem("email", data.email);
            localStorage.setItem("codeResendAvailableAt", data.codeResendAvailableAt);
            localStorage.setItem("validUntil", data.validUntil);
            queryClient.invalidateQueries({ queryKey: ["registerUser"] });
            queryClient.setQueryData("registerUser", {
                "email": data.email,
                "codeResendAvailableAt": data.codeResendAvailableAt,
                "validUntil": data.validUntil
            });
            navigate(0);
        },
        onError: (error) => {
            localStorage.removeItem("email");
            localStorage.removeItem("codeResendAvailableAt");
            localStorage.removeItem("validUntil");
            queryClient.invalidateQueries({ queryKey: ["registerUser"] });
            if (error.httpStatus === "UNAUTHORIZED" || error.httpStatus === "TOO_MANY_REQUESTS") {
                setExitOnError();
            }
            throw error;
        },
    });

    const handleConfirmUserSubmit = (e) => {
        e.preventDefault();

        if (code.length !== 6) {
            return;
        }

        confirmUserMutate({
            "email": email,
            "code": code,
        });
    };

    const handleResendEmailSubmit = (e) => {
        e.preventDefault();
        resendEmailMutate({
            "email": email,
            "timezone": Intl.DateTimeFormat().resolvedOptions().timeZone,
        });
    };

    const handleChange = (e) => {
        setCode(e.target.value);
    };

    useEffect(() => {
        if (exitOnError) {
            const timer = setTimeout(() => {
                navigate('/errorPage');
            }, 5000);

            return () => clearTimeout(timer);
        }
    }, [exitOnError, navigate]);



    useEffect(() => {
        if (token) {
            navigate({ to: '/register/create' });
        }

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

    useEffect(() => {
        const nextValidResendTime = new Date(codeResendAvailableAt).getTime();
        const interval = setInterval(() => {
            const now = Date.now();
            const secondsRemaining = Math.max(0, Math.floor((nextValidResendTime - now) / 1000));
            setSecondsRemaining(secondsRemaining);

            if (secondsRemaining <= 0) {
                clearInterval(interval);
            }
        }, 1000);

        return () => {
            setSecondsRemaining(0);
            clearInterval(interval)
        };
    }, [codeResendAvailableAt]);

    let button_content;

    if (resendEmailIsPending) {
        button_content = <div className="confirm-resend-loading"><OrbitProgress style={{ fontSize: "0.3em" }} variant="track-disc" dense color="#ff8000" size="small" text="" textColor="#454080" /></div>
    } else {
        if (secondsRemaining === 0) {
            button_content = <a className='confirm-resend-button' onClick={handleResendEmailSubmit}>Resend</a>
        } else {
            button_content = secondsRemaining
        }
    }

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
            <div className="confirm-overlay">
                <div className="confirm-overlay-box-container">
                    <Link className="close-button" to="/home"><img alt="close-button" src={CloseButton} /></Link>
                    <h1>Confirm your account with the email code.</h1>
                    <input value={code} onChange={handleChange} className={confirmUserIsError ? 'confirm-code-field-invalid' : 'confirm-code-field'} maxLength={6} placeholder={confirmUserIsError ? confirmUserError.message : '6-digit code'} type="text" />
                    {confirmUserIsPending ? <div className="confirm-submit-loading"><OrbitProgress style={{ fontSize: "0.3em" }} variant="track-disc" dense color="#ff8000" size="small" text="" textColor="#454080" /></div> : <a className='confirm-submit-button' onClick={handleConfirmUserSubmit}>Confirm</a>}
                    <span className='confirm-already-account-container'>
                        Haven’t received the code yet? {button_content}
                    </span>
                    <p className='confirm-subtext'>By clicking "Confirm”, you accept Trippers’ Gear<br></br> Rental PH Terms of Service and Privacy Policy.</p>
                </div>
            </div>
        </>)
}
