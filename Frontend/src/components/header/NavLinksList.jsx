import React from 'react';
import { NavLink } from 'react-router-dom';

const NavLinksList = ({ itemClassName = "nav-item", linkClassName = "nav-link" }) => (
    <>
        <li className={itemClassName}>
            <NavLink to="/" className={linkClassName}>Inicio</NavLink>
        </li>
        <li className={itemClassName}>
            <NavLink to="/componentes" className={linkClassName}>Componentes</NavLink>
        </li>
        <li className={itemClassName}>
            <NavLink to="/categorias" className={linkClassName}>Categorías</NavLink>
        </li>
        <li className={itemClassName}>
            <NavLink to="/builds" className={linkClassName}>Armado</NavLink>
        </li>
    </>
);

export default NavLinksList;