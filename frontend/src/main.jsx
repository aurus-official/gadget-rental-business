import ReactDOM from 'react-dom/client'
import { RouterProvider, createRouter } from '@tanstack/react-router'
import "./styles/main.css"

import { routeTree } from './routeTree.gen'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { AuthProvider } from './contexts/AuthContext'

const queryClient = new QueryClient();
const router = createRouter({ routeTree, context: { queryClient }, scrollRestoration: true, defaultPreload: "intent" })

const rootElement = document.getElementById('root')
const root = ReactDOM.createRoot(rootElement)

root.render(
    <AuthProvider>
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
        </QueryClientProvider>
    </AuthProvider>
)

