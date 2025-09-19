# 🖥️ Frontend - CompuSearch

Este directorio contendrá el desarrollo del frontend para **CompuSearch**.

## 🚀 Tecnologías principales

| Tecnología      | Descripción                                         |
|-----------------|-----------------------------------------------------|
| ⚡ **Vite**     | Herramienta de desarrollo y bundler ultrarrápido    |
| ⚛️ **React**    | Librería para interfaces de usuario interactivas     |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/javascript/javascript-original.svg" alt="JS" width="20"/> **JavaScript** | Lenguaje de programación principal                  |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/nodejs/nodejs-original.svg" alt="Node.js" width="21"/> **Node.js** | Entorno de ejecución para scripts y herramientas    |

> **Nota:** El stack puede ampliarse conforme avance el proyecto (por ejemplo: librerías de estilos, gestión de estado, testing, etc.).

## 🧑‍💻 IDE Recomendado

- **[Visual Studio Code](https://code.visualstudio.com/)** (recomendado)
  > con extensiones de React y JavaScript
- **[WebStorm](https://www.jetbrains.com/webstorm/promo/?msclkid=4429641795d71dcbebea0cdb09c466c7&utm_source=bing&utm_medium=cpc&utm_campaign=AMER_en_AMER_WebStorm_Branded&utm_term=webstorm&utm_content=webstorm)** (opcional)

## 📦 Estructura propuesta

```
Frontend/
│
├── public/                 # Archivos estáticos y favicon
├── src/                    # Código fuente principal
│   ├── assets/             # Imágenes, estilos, etc.
│   ├── components/         # Componentes reutilizables de React
│   ├── pages/              # Vistas principales (páginas)
│   ├── services/           # Lógica de conexión con el backend (APIs)
│   ├── App.jsx             # Componente raíz
│   └── main.jsx            # Punto de entrada
├── .gitignore
├── index.html
├── package.json
├── README.md
└── vite.config.js
```

### 📁 Descripción rápida de carpetas

| Carpeta       | Descripción                              |
|---------------|------------------------------------------|
| `public/`     | Archivos públicos y estáticos            |
| `src/`        | Código fuente principal                  |
| `assets/`     | Imágenes y recursos estáticos            |
| `components/` | Componentes reutilizables de React       |
| `pages/`      | Vistas principales del frontend          |
| `services/`   | Lógica para consumir APIs                |

## ⚙️ Instalación y ejecución inicial

Sigue estos pasos para crear y levantar el entorno del frontend.  
Puedes guiarte por las imágenes de referencia:

1. **Crear un nuevo proyecto con Vite:**  
   ```bash
   npm create vite@latest nombre-proyecto
   # Selecciona: React
   # Selecciona: JavaScript
   ```
2. **Entrar al directorio del proyecto e instalar dependencias:**  
   ```bash
   cd nombre-proyecto
   npm install
   ```
3. **Instalar Bootstrap, Bootstrap Icons y Popper.js (opcional pero recomendado para estilos):**  
   ```bash
   npm install bootstrap bootstrap-icons @popperjs/core
   ```
4. **Levantar el servidor de desarrollo:**
   ```bash
   npm run dev
   ```
5. Una vez ejecutado se lanza un servidor local:
   
🌐 Acceso local: [http://localhost:5173/](http://localhost:5173/)   
   
---
