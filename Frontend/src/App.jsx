import React, { useEffect, useState } from "react"
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Header from "./components/Header"
import Footer from "./components/Footer"

import Home from "./pages/Home"
import Componentes from "./pages/Componentes"
import Tiendas from "./pages/Tiendas"
import Categorias from "./pages/Categorias"
import Builds from "./pages/Builds"
import Login from "./pages/Login"
import Registro from "./pages/Registro"

const App = () => {
  const [headerHeight, setHeaderHeight] = useState(0)

  useEffect(() => {
    const header = document.querySelector("header")
    if (header) {
      setHeaderHeight(header.offsetHeight)
    }

    const handleResize = () => {
      if (header) setHeaderHeight(header.offsetHeight)
    }
    window.addEventListener("resize", handleResize)

    return () => window.removeEventListener("resize", handleResize)
  }, [])

  return (
    <Router>
      <div className="d-flex flex-column min-vh-100">
        <Header />

        <main className="flex-grow-1" style={{ paddingTop: headerHeight }}>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/componentes" element={<Componentes />} />
            <Route path="/tiendas" element={<Tiendas />} />
            <Route path="/categorias" element={<Categorias />} />
            <Route path="/builds" element={<Builds />} />
            <Route path="/login" element={<Login />} />
            <Route path="/registro" element={<Registro />} />
          </Routes>
        </main>

        <Footer />
      </div>
    </Router>
  )
}

export default App
