import React from "react";
import { Carousel } from "react-bootstrap";
import banner1 from "../../../../../assets/banners/banner_home_1.webp";
import banner2 from "../../../../../assets/banners/banner_home_2.jpg";

const BannerCarousel = () => (
    <Carousel fade>
        <Carousel.Item>
            <img className="d-block w-100" src={banner1} alt="First slide" />
            <Carousel.Caption className="bg-dark bg-opacity-50 p-2 p-md-3 rounded">
                <h3 className="text-white fs-5 fs-md-3">Bienvenido a CompuSearch</h3>
                <p className="text-white d-none d-md-block">Encuentra los mejores componentes para tu PC</p>
            </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
            <img className="d-block w-100" src={banner2} alt="Second slide" />
        </Carousel.Item>
    </Carousel>
);

export default BannerCarousel;
