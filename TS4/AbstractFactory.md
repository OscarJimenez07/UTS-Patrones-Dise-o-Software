# Patrón Abstract Factory — Tienda de Ítems por Nivel (Estándar / Premium)

## Diagrama UML
![AbstractFactory](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/OscarJimenez07/UTS-Patrones-Dise-o-Software/master/TS4/AbstractFactory.puml)

## ¿Qué es?

El patrón Abstract Factory proporciona una **interfaz para crear familias de objetos relacionados** sin especificar sus clases concretas. A diferencia del Factory Method (que crea un solo tipo de producto), Abstract Factory crea **múltiples productos que pertenecen a una misma familia**.

En este proyecto, la tienda virtual ofrece ítems en dos niveles: **Estándar** (obtenidos con moneda del juego) y **Premium** (comprados con moneda real). Cada nivel produce una familia completa de productos relacionados: Skins, Potenciadores y Recompensas. El Abstract Factory permite que la clase `Tienda` trabaje con cualquier nivel sin conocer las clases concretas.

## Fragmento de código del proyecto

### Abstract Factory y fábricas concretas — `src/TiendaItems.java`

```java
// ABSTRACT FACTORY: define la creación de una FAMILIA completa de ítems.
// Cada fábrica concreta crea Skin + Potenciador + Recompensa de su nivel.
interface TiendaItemFactory {
    Skin crearSkin(String nombre);
    Potenciador crearPotenciador(String nombre);
    Recompensa crearRecompensa(String descripcion);
}

// Fábrica concreta: familia de ítems Estándar (moneda del juego).
class TiendaEstandarFactory implements TiendaItemFactory {
    @Override public Skin crearSkin(String nombre) { return new SkinEstandar(nombre); }
    @Override public Potenciador crearPotenciador(String nombre) { return new PotenciadorEstandar(nombre); }
    @Override public Recompensa crearRecompensa(String descripcion) { return new RecompensaEstandar(descripcion); }
}

// Fábrica concreta: familia de ítems Premium (moneda real).
class TiendaPremiumFactory implements TiendaItemFactory {
    @Override public Skin crearSkin(String nombre) { return new SkinPremium(nombre); }
    @Override public Potenciador crearPotenciador(String nombre) { return new PotenciadorPremium(nombre); }
    @Override public Recompensa crearRecompensa(String descripcion) { return new RecompensaPremium(descripcion); }
}
```

### Ejemplo de productos — `src/TiendaItems.java`

```java
// Producto abstracto
interface Skin {
    String getNombre();
    String getRareza();
    void aplicar();
}

// Producto concreto Estándar
class SkinEstandar implements Skin {
    private String nombre;
    public SkinEstandar(String nombre) { this.nombre = nombre; }
    @Override public String getNombre() { return nombre; }
    @Override public String getRareza() { return "Común"; }
    @Override public void aplicar() {
        System.out.println("  [Skin Estándar] Aplicando '" + nombre + "' (rareza: Común)");
    }
}

// Producto concreto Premium
class SkinPremium implements Skin {
    private String nombre;
    public SkinPremium(String nombre) { this.nombre = nombre; }
    @Override public String getNombre() { return nombre; }
    @Override public String getRareza() { return "Legendaria"; }
    @Override public void aplicar() {
        System.out.println("  [Skin Premium] Aplicando '" + nombre + "' (rareza: Legendaria) con efectos especiales");
    }
}
```

### Cliente que usa el Abstract Factory — `src/Tienda.java`

```java
public class Tienda {

    private TiendaItemFactory factory;
    private String nivel;

    public Tienda(TiendaItemFactory factory, String nivel) {
        this.factory = factory;
        this.nivel = nivel;
    }

    public void mostrarOfertaDelDia() {
        Skin skin = factory.crearSkin("Dragon de Fuego");
        Potenciador pot = factory.crearPotenciador("Velocidad x2");
        Recompensa rec = factory.crearRecompensa("Mision diaria completada");

        System.out.println("  TIENDA - Oferta del Dia (" + nivel + ")");
        skin.aplicar();
        pot.activar();
        rec.reclamar();
    }
}
```

### ¿Cómo se usa en el proyecto?

En `src/Main.java` se demuestra el patrón:

```java
// Tienda Estándar: crea ítems obtenibles con moneda del juego
TiendaItemFactory factoryEstandar = new TiendaEstandarFactory();
Tienda tiendaEstandar = new Tienda(factoryEstandar, "Estándar");
tiendaEstandar.mostrarOfertaDelDia();

// Tienda Premium: crea ítems comprables con moneda real
TiendaItemFactory factoryPremium = new TiendaPremiumFactory();
Tienda tiendaPremium = new Tienda(factoryPremium, "Premium");
tiendaPremium.mostrarOfertaDelDia();

// Solo cambiando la fábrica, toda la familia de productos cambia.
```

## Archivos del proyecto involucrados

| Archivo | Rol en el patrón |
|---|---|
| `src/TiendaItems.java` | Interfaces de productos, productos concretos, Abstract Factory y fábricas concretas |
| `src/Tienda.java` | Cliente que usa el Abstract Factory |
| `src/Main.java` | Demostración del patrón |

## Diferencia con Factory Method (TS3)

| Aspecto | Factory Method (TS3) | Abstract Factory (TS4) |
|---|---|---|
| **Alcance** | Crea **un solo tipo** de producto (Dashboard) | Crea **familias** de productos relacionados (Skin + Potenciador + Recompensa) |
| **Mecanismo** | Herencia: las subclases sobrescriben el método fábrica | Composición: el cliente recibe un objeto fábrica |
| **Ejemplo en el proyecto** | `DashboardFactory` → crea un `Dashboard` | `TiendaItemFactory` → crea `Skin`, `Potenciador` y `Recompensa` |
