import React from "react";
import { NavLink } from "react-router-dom";

const FooterSection = ({ title, links }) => (
    <>
        <h6 className="fw-bold">{title}</h6>
        <ul className="list-unstyled">
            {links.map(({ label, to }, index) => (
                <li key={index}>
                    <NavLink to={to} className="text-white text-decoration-none">
                        {label}
                    </NavLink>
                </li>
            ))}
        </ul>
    </>
);

export default FooterSection;
