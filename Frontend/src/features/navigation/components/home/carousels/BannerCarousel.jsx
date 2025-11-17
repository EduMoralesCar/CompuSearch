import React from "react";
import { Carousel } from "react-bootstrap";
import banner1 from "../../../../../assets/banners/banner_home_1.webp";
import banner2 from "../../../../../assets/banners/banner_home_2.jpg";

//Import de gif
import banner3 from "../../../../../assets/banners/banner_builds.gif";
import banner4 from "../../../../../assets/banners/banner_laptop.gif";
import banner5 from "../../../../../assets/banners/banner_prox_app.gif";



const BannerCarousel = () => (
    <Carousel fade>
        <Carousel.Item>
            <img className="d-block w-100" src={banner3} alt="First slide" />
            <Carousel.Caption className="bg-dark bg-opacity-50 p-2 p-md-3 rounded">
                <h3 className="text-white fs-5 fs-md-3">Bienvenido a CompuSearch</h3>
                <p className="text-white d-none d-md-block">Encuentra los mejores componentes para tu PC</p>
            </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
            <img className="d-block w-100" src={banner4} alt="slide 2" />
        </Carousel.Item>
        <Carousel.Item>
            <img className="d-block w-100" src={banner5} alt="slide 3" />
        </Carousel.Item>

        <Carousel.Item>
            <img className="d-block w-100" src={banner1} alt="slide 4" />
        </Carousel.Item>
        
        <Carousel.Item>
            <img className="d-block w-100" src={banner2} alt="slide 5" />
        </Carousel.Item>
        
    </Carousel>
);

export default BannerCarousel;
