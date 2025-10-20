import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { UserPlus } from 'lucide-react';
import '../../styles/auth.scss';

// This is a simple redirect component to the login page with registration mode active
export default function RegisterPage() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(true);
  
  useEffect(() => {
    // Redirect to the login page with mode=register parameter after a short delay
    const timer = setTimeout(() => {
      router.push('/auth/login?mode=register');
    }, 800);
    
    return () => clearTimeout(timer);
  }, [router]);
  
  return (
    <div className="auth-page flex justify-center items-center h-screen bg-gray-50">
      <div className="auth-container w-full max-w-md p-8 bg-white rounded-2xl shadow-lg border border-gray-200 text-center">
        <h1 className="auth-title text-3xl font-bold mb-4">Create Account</h1>
        <p className="text-gray-600 mb-6">Preparing your registration form...</p>
        <div className="flex justify-center items-center">
          <div className="w-12 h-12 border-4 border-amber-500 border-t-transparent rounded-full animate-spin mr-4"></div>
          <UserPlus size={24} className="text-amber-500" />
        </div>
      </div>
    </div>
  );
}