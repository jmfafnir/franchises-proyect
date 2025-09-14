# 📌 Franchises Microservice

Este microservicio gestiona **franquicias, sucursales y productos**.  
Expone endpoints REST para administrar inventarios distribuidos en diferentes sucursales.

---

## ⚙️ Requisitos previos

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)
- [PostgreSQL](https://www.postgresql.org/) (opcional, el proyecto puede correr con H2 en local).
- Java 21.
- gradle 8.*

---

## 🛠️ Variables de entorno

| Variable            | Descripción                              | Ejemplo                  |
|---------------------|------------------------------------------|--------------------------|
| `SERVER_PORT`       | Puerto de ejecución del microservicio    | `8080`                   |
| `BD_HOST`           | Host de la base de datos                 | `localhost`              |
| `BD_PORT`           | Puerto de la base de datos               | `5432`                   |
| `BD_DATABASE`       | Nombre de la base de datos               | `franchises_db`          |
| `BD_SHEMA`          | Esquema usado en la base de datos        | `public`                 |
| `BD_USERNAME`       | Usuario de la base de datos              | `postgres`               |
| `BD_PASSWORD`       | Contraseña del usuario                   | `postgres`               |
| `REST_BASE_PATH`    | Path base para los endpoints REST        | `/api/v1`                |

---

Se requiere contar un una BD postgrets contenerizada para almacenar lo informacion
con el siguiente esquema de BD 

CREATE TABLE franchise (
franchise_id SERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL
);

CREATE TABLE branch (
branch_id SERIAL PRIMARY KEY,
franchise_id INT NOT NULL REFERENCES franchise(franchise_id) ON DELETE CASCADE ON UPDATE CASCADE,
name VARCHAR(255) NOT NULL,
city VARCHAR(255)
);

CREATE TABLE product (
product_id SERIAL PRIMARY KEY,
branch_id INT NOT NULL REFERENCES branch(branch_id) ON DELETE CASCADE ON UPDATE CASCADE,
name VARCHAR(255) NOT NULL,
stock INT NOT NULL DEFAULT 0,
price NUMERIC(10,2)
);
