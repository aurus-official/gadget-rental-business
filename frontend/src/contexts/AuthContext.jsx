import { createContext, useEffect, useState } from "react";

const AuthContext = createContext();

function AuthProvider({ children }) {
    const [auth, setAuth] = useState({
        accessToken: "",
        expiry: "",
    });

    useEffect(() => {
        const accessToken = localStorage.getItem("accessToken");
        const expiry = localStorage.getItem("expiry");

        if (!accessToken || !expiry || Date.now() > new Date(expiry)) {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("expiry");
            setAuth({
                accessToken: "",
                expiry: "",
            });
            return;
        }

        setAuth({
            accessToken: accessToken,
            expiry: expiry,
        });


    }, []);

    const handleSetAuth = (auth) => {
        localStorage.setItem("accessToken", auth.accessToken);
        localStorage.setItem("expiry", auth.expiry);
        setAuth({
            accessToken: auth.accessToken,
            expiry: auth.expiry,
        });
    };

    const handleRemoveAuth = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("expiry");
        setAuth({
            accessToken: "",
            expiry: "",
        });
    };

    return (
        <AuthContext value={{ handleSetAuth, handleRemoveAuth, auth }}>
            {children}
        </AuthContext>
    );
}

export { AuthContext, AuthProvider };
