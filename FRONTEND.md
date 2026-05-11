# Cómo correr el proyecto con frontend web

El proyecto ahora tiene dos partes nuevas además del código original:

- `backend/` — Spring Boot que envuelve los patrones existentes y los expone como REST.
- `frontend/` — SPA en React + TypeScript + Tailwind con tema oscuro neón.
- `tools/apache-maven-3.9.15/` — Maven autocontenido (no requiere instalación global).

El código original sigue intacto en `src/`.

## Patrones expuestos en la UI (8)

Singleton · Abstract Factory · Prototype · Facade · Adapter · Decorator · Bridge · Composite

Builder y Factory Method (Dashboard) se conservan en el código fuente pero no se exponen — el routing por rol del frontend reemplaza al Factory Method.

---

## Ejecución rápida (un solo JAR)

Requisitos: Java 17+ instalado.

```bash
java -jar backend/target/gameengine.jar
```

Abrir <http://localhost:8080/> y entrar con `oscar / 1234` o `anderson / 1234`.

El JAR ya viene compilado. Si quieres reconstruirlo:

```bash
# Build del frontend
cd frontend
npm install
npm run build
cp -r dist/* ../backend/src/main/resources/static/

# Build del backend
cd ../backend
../tools/apache-maven-3.9.15/bin/mvn -DskipTests package
# Resultado: backend/target/gameengine.jar
```

---

## Modo desarrollo (hot reload)

Útil mientras editas el frontend.

Terminal 1 — backend:
```bash
cd backend
../tools/apache-maven-3.9.15/bin/mvn spring-boot:run
# corre en http://localhost:8080
```

Terminal 2 — frontend:
```bash
cd frontend
npm run dev
# corre en http://localhost:5173 con proxy /api -> :8080
```

Abrir <http://localhost:5173>.

---

## Endpoints REST

| Patrón | Endpoint | Método |
|---|---|---|
| Singleton | `/api/auth/login`, `/api/auth/registro`, `/api/auth/me` | POST/POST/GET |
| Abstract Factory | `/api/tienda/{estandar\|premium}` | GET |
| Prototype | `/api/partida/modos`, `/api/partida/crear` | GET, POST |
| Facade | `/api/partida/iniciar` | POST |
| Adapter | `/api/notificaciones/enviar`, `/api/notificaciones/resultado` | POST |
| Decorator | `/api/mejoras/aplicar` | POST |
| Bridge | `/api/ataques/ejecutar`, `/api/ataques/combinaciones` | POST, GET |
| Composite | `/api/clan`, `/api/clan/mensaje`, `/api/clan/jugador` | GET, POST, POST |

Todos los endpoints que invocan código que escribe a `System.out` capturan ese output con `OutputCapture` y lo devuelven en el campo `log` del JSON, para que el frontend lo muestre en su panel terminal.
