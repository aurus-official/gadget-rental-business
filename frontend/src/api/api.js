const apis = {
    getReviews: async () => {
        try {
            const response = await fetch(
                "http://localhost:80/api/v1/reviews?page=1",
                {
                    method: "GET",
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw errorData || new Error("Error fetching reviews.");
            }

            return await response.json();
        } catch (exception) {
            throw exception;
        }
    },
    registerUser: async (data) => {
        try {
            const response = await fetch(
                "http://localhost:80/api/v1/client/email-verification-requests",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw errorData || new Error("Error fetching reviews.");
            }

            return await response.json();
        } catch (exception) {
            throw exception;
        }
    },
    confirmUser: async (data) => {
        try {
            const response = await fetch(
                "http://localhost:80/api/v1/client/email-verification",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw errorData || new Error("Error fetching reviews.");
            }

            const token = await response.text();
            return {
                token: token,
            };
        } catch (exception) {
            throw exception;
        }
    },
    resendEmail: async (data) => {
        try {
            const response = await fetch(
                "http://localhost:80/api/v1/client/email-verification-requests/resend",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw errorData;
            }

            return await response.json();
        } catch (exception) {
            throw exception;
        }
    },
    createUser: async (data) => {
        try {
            const response = await fetch("http://localhost:80/api/v1/clients", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${data.token}`,
                },
                body: JSON.stringify({
                    password: data.password,
                    confirmPassword: data.confirmPassword,
                    email: data.email,
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                console.log(errorData);
                const errorMessage = Object.values(errorData)[0];
                const message = {
                    message: errorMessage,
                };
                throw message;
            }

            return await response.text();
        } catch (exception) {
            throw exception;
        }
    },
    loginUser: async (data) => {
        try {
            const response = await fetch(
                "http://localhost:80/api/v1/auth/login",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw errorData;
            }

            return await response.json();
        } catch (exception) {
            throw exception;
        }
    },
    refreshUser: async (data) => {
        try {
            const response = await fetch(
                "http://localhost:80/api/v1/auth/login",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                }
            );

            if (!response.ok) {
                const errorData = await response.json();
                throw errorData;
            }

            return await response.json();
        } catch (exception) {
            throw exception;
        }
    },
};

export default apis;
