"use client";

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import '../../styles/auth.scss';

// This is a simple redirect component to the login page
export default function SignInPage() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(true);
  
  useEffect(() => {
    // Redirect to the login page after a short delay for better UX
    const timer = setTimeout(() => {
      router.push('/auth/login');
    }, 800);
    
    return () => clearTimeout(timer);
  }, [router]);
  
  return (
    <div className="auth-page flex justify-center items-center h-screen bg-gray-50">
      <div className="auth-container w-full max-w-md p-8 bg-white rounded-2xl shadow-lg border border-gray-200 text-center">
        <h1 className="auth-title text-3xl font-bold mb-4">Welcome Back</h1>
        <p className="text-gray-600 mb-6">Redirecting you to sign in...</p>
        <div className="flex justify-center">
          <div className="w-12 h-12 border-4 border-amber-500 border-t-transparent rounded-full animate-spin"></div>
        </div>
      </div>
    </div>
  );
}