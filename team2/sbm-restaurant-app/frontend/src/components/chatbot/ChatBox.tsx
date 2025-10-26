"use client";

import React, { useState, useEffect, useRef } from "react";
import "./chatbotstyle.scss";

type Msg = { id?: number; from: "user" | "assistant"; text: string; simulated?: boolean };

export default function ChatBox() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState<Msg[]>([]);
  const [loading, setLoading] = useState(false);
  const listRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    // scroll to bottom on new message
    listRef.current?.scrollTo({ top: listRef.current.scrollHeight, behavior: "smooth" });
  }, [messages]);

  async function sendMessage() {
    const text = input.trim();
    if (!text) return;
    const userMsg: Msg = { from: "user", text };
    setMessages((m) => [...m, userMsg]);
    setInput("");
    setLoading(true);

    try {
      const res = await fetch("/api/chat", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ message: text })
      });

      if (!res.ok) {
        const err = await res.json().catch(() => ({}));
        setMessages((m) => [...m, { from: "assistant", text: `Error: ${res.status} ${JSON.stringify(err)}` }]);
        return;
      }

      const data = await res.json();
      const reply = data.reply ?? "(no reply)";
      const simulated = Boolean(data.simulated);
      setMessages((m) => [...m, { from: "assistant", text: reply, simulated }]);
    } catch (e) {
      setMessages((m) => [...m, { from: "assistant", text: `Network error: ${String(e)}` }]);
    } finally {
      setLoading(false);
    }
  }

  function onKey(e: React.KeyboardEvent<HTMLInputElement>) {
    if (e.key === "Enter") sendMessage();
  }

  return (
    <div style={{ maxWidth: 720, margin: "24px auto", padding: 12, border: "1px solid #eee", borderRadius: 8 }}>
      <h2>Restaurant Chatbot</h2>
      <div ref={listRef} style={{ maxHeight: 360, overflow: "auto", padding: 8, background: "#fafafa", borderRadius: 6 }} className="chatbot-bubble-container">
        {messages.length === 0 && <div style={{ color: "#666" }}>Ask about your order status or what's in your order.</div>}
        {messages.map((m, i) => (
          <div key={i}>
            <div style={{ fontSize: 12, color: "#888" }}>{m.from === "user" ? "You" : m.simulated ? "Assistant (simulated)" : "Assistant"}</div>
            <div className={`chatbot-message ${m.from}`}>{m.text}</div>
          </div>
        ))}
      </div>

      <div style={{ display: "flex", gap: 8, marginTop: 12 }}>
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={onKey}
          placeholder="Ask about order status (e.g. 'status of order #42')"
          style={{ flex: 1, padding: 8, borderRadius: 6, border: "1px solid #ddd" }}
          disabled={loading}
        />
        <button onClick={sendMessage} disabled={loading} style={{ padding: "8px 12px", borderRadius: 6 }}>
          {loading ? "Sending..." : "Send"}
        </button>
      </div>
    </div>
  );
}
