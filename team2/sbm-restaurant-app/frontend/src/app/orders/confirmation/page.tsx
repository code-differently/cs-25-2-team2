"use client";

import { Suspense } from "react";
import OrderConfirmationComponent from "../../../page-views/order/confirmation";

function OrderConfirmationContent() {
  return <OrderConfirmationComponent />;
}

export default function OrderConfirmationPage() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <OrderConfirmationContent />
    </Suspense>
  );
}