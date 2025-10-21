"use client";

import { Suspense } from "react";
import LoginComponent from "../../../page-views/auth/login";

function LoginContent() {
  return <LoginComponent />;
}

export default function LoginPage() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <LoginContent />
    </Suspense>
  );
}