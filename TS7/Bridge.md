# Patron Bridge — Sistema de Ataques por Elemento

## Diagrama UML
![Bridge](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/OscarJimenez07/UTS-Patrones-Dise-o-Software/master/TS7/Bridge.puml)

## ¿Que es?

El patron Bridge **separa una abstraccion de su implementacion** para que ambas puedan variar de forma independiente. Funciona como un "puente" que conecta dos jerarquias de clases que cambian por razones distintas.

En este proyecto, los personajes tienen dos dimensiones independientes al atacar: el **tipo de ataque** (cuerpo a cuerpo, distancia, magico) y el **elemento** que lo potencia (fuego, hielo, electrico). Sin Bridge, habria que crear una clase por cada combinacion: `AtaqueCuerpoACuerpoFuego`, `AtaqueCuerpoACuerpoHielo`, `AtaqueDistanciaFuego`, etc. Con Bridge, ambas dimensiones se desarrollan por separado y se combinan en tiempo de ejecucion.

## Fragmento de codigo del proyecto

### Implementor — `src/SistemaAtaques.java`

```java
// IMPLEMENTOR: interfaz que define el contrato
// del elemento que potencia el ataque.
interface ElementoAtaque {
    String getNombre();
    int getDanoElemento();
    String getEfecto();
}
```

### Concrete Implementor — `src/SistemaAtaques.java`

```java
// CONCRETE IMPLEMENTOR: elemento de fuego.
class ElementoFuego implements ElementoAtaque {
    @Override
    public String getNombre() { return "Fuego"; }

    @Override
    public int getDanoElemento() { return 20; }

    @Override
    public String getEfecto() { return "Quemadura: dano continuo por 3 turnos"; }
}
```

### Abstraction — `src/SistemaAtaques.java`

```java
// ABSTRACTION: contiene una referencia al ElementoAtaque (el "puente").
abstract class AtaquePersonaje {
    protected ElementoAtaque elemento;  // <-- el puente

    public AtaquePersonaje(ElementoAtaque elemento) {
        this.elemento = elemento;
    }

    public abstract String getTipoAtaque();
    public abstract int getDanoBase();

    public void ejecutarAtaque() {
        int danoTotal = getDanoBase() + elemento.getDanoElemento();
        System.out.println("  Ejecutando: " + getTipoAtaque() + " de " + elemento.getNombre());
        System.out.println("    Dano total: " + danoTotal);
        System.out.println("    Efecto: " + elemento.getEfecto());
    }
}
```

### Refined Abstraction — `src/SistemaAtaques.java`

```java
// REFINED ABSTRACTION: ataque cuerpo a cuerpo.
class AtaqueCuerpoACuerpo extends AtaquePersonaje {

    public AtaqueCuerpoACuerpo(ElementoAtaque elemento) {
        super(elemento);
    }

    @Override
    public String getTipoAtaque() { return "Golpe Cuerpo a Cuerpo"; }

    @Override
    public int getDanoBase() { return 30; }

    @Override
    public void ejecutarAtaque() {
        super.ejecutarAtaque();
        System.out.println("    [Cuerpo a cuerpo: ignora 10% de armadura]");
    }
}
```

### ¿Como se usa en el proyecto?

En `src/Main.java` el jugador selecciona un tipo de ataque y un elemento por separado. El patron Bridge los combina sin necesidad de crear una clase para cada combinacion:

```java
// Elegir elemento (implementor)
ElementoAtaque elemento = new ElementoFuego();

// Elegir tipo de ataque (abstraction) y conectar via puente
AtaquePersonaje ataque = new AtaqueCuerpoACuerpo(elemento);

// Ejecutar — el ataque delega al elemento automaticamente
ataque.ejecutarAtaque();
// Resultado: Golpe Cuerpo a Cuerpo de Fuego
// Dano: 30 + 20 = 50
// Efecto: Quemadura

// Mismo tipo de ataque, diferente elemento:
AtaquePersonaje ataque2 = new AtaqueCuerpoACuerpo(new ElementoHielo());
ataque2.ejecutarAtaque();
// Resultado: Golpe Cuerpo a Cuerpo de Hielo
// Dano: 30 + 12 = 42
// Efecto: Congelacion
```

## Archivos del proyecto involucrados

| Archivo | Rol en el patron |
|---|---|
| `src/SistemaAtaques.java` | Implementor, Concrete Implementors, Abstraction y Refined Abstractions |
| `src/Main.java` | Cliente que combina tipos de ataque con elementos |

## Comparacion con patrones anteriores

| Aspecto | Builder (TS5) | Prototype (TS6) | Adapter (TS6) | Decorator (TS7) | **Bridge (TS7)** |
|---|---|---|---|---|---|
| **Proposito** | Construir objeto complejo **paso a paso** | Crear objetos **clonando** un prototipo | Hacer compatibles **interfaces incompatibles** | Agregar **responsabilidades dinamicas** | Separar **abstraccion de implementacion** |
| **Tipo** | Creacional | Creacional | Estructural | Estructural | **Estructural** |
| **Problema que resuelve** | Objeto con muchos parametros | Crear copias eficientes | Integrar servicios externos | Extender comportamiento sin modificar la clase | Evitar explosion de subclases con dos dimensiones |
| **Mecanismo** | Composicion (builder + director) | Clonacion | Composicion (adapter envuelve adaptee) | Composicion (decorator envuelve componente) | Composicion (abstraccion referencia implementor) |
| **Ejemplo en el proyecto** | `PersonajeBuilder` → construye `Personaje` | `RegistroConfiguraciones` → clona configs | `AdaptadorDiscord` → adapta Discord | `MejoraDanoFuego` → anade fuego a habilidad | `AtaqueCuerpoACuerpo` + `ElementoFuego` |

## Diferencia clave: Decorator vs Bridge

| Aspecto | Decorator | Bridge |
|---|---|---|
| **Objetivo** | Agregar comportamiento extra a un objeto existente | Separar dos dimensiones que varian independientemente |
| **Estructura** | Cadena de envolturas (A envuelve B envuelve C) | Dos jerarquias conectadas por una referencia |
| **Se apilan?** | Si, multiples capas | No, se elige una combinacion |
| **En el proyecto** | Habilidad + mejora + mejora + mejora | Tipo de ataque + elemento |
