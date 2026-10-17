# ShopMaster Backend — Arquitectura con Patrones Estructurales

Proyecto académico para el curso de **Programación II (Parcial 2)** del Programa de Ingeniería de Sistemas y Computación. Este motor backend en Java refactoriza la arquitectura de comercio electrónico de **ShopMaster** aplicando los cinco patrones de diseño estructurales vistos en clase.

## Estado del Proyecto y Clases por Patrón

| # | Requerimiento | Patrón Estructural | Clases del Proyecto | Estado |
| :--- | :--- | :--- | :--- | :--- |
| **1** | Gestión flexible de productos y paquetes promocionales | **Composite** | `Componente`, `Producto`, `Combo` | Completado por compañera |
| **2** | Servicios opcionales y acumulables sobre el pedido | **Decorator** | `Pedido`, `PedidoBase`, `PedidoDecorator`, `GarantiaExtendida`, `EmpaqueRegalo`, `ManejoFragil`, `Extra` | Completado por compañera |
| **3** | Integración con pasarela externa (`StripeUSA`) | **Adapter** | `ProcesadorPago`, `StripeUSA`, `AdaptadorStripeUSA` | Completado |
| **4** | Barrera de seguridad, control de cupos y auditoría | **Proxy** | `ProxySeguridadPago` (implementa `ProcesadorPago`) | Completado |
| **5** | Simplificación de la compra rápida (*One-Click Checkout*) | **Facade** | `ShopMasterFacade`, `Main`, `Formato` | Completado |

## Detalle de Diseño por Subsistema

### 1. Catálogo de Productos y Combos (Patrón Composite)

Permite construir estructuras en árbol donde tanto un artículo suelto como un paquete promocional implementan el mismo contrato base sin condicionales ni `instanceof`.

* **`Componente` (Interfaz):** Declara `getNombre()`, `getPrecio()` y `mostrar(String sangria)`.
* **`Producto` (Hoja):** Representa un artículo individual con su nombre y precio directo.
* **`Combo` (Compuesto):** Mantiene una lista `List<Componente> hijos`, permite encadenar llamadas con `.agregar(Componente c)`, suma recursivamente `c.getPrecio()` y muestra la jerarquía indentada.

### 2. Beneficios Post-Venta Acumulables (Patrón Decorator)

Envuelve un pedido base con capas opcionales y apilables en cualquier orden (incluso repetibles, como doble empaque de regalo) sin banderas booleanas ni subclases por combinación.

* **`Pedido` (Interfaz):** Define `getDescripcion()` y `getCosto()`.
* **`PedidoBase` (Componente Concreto):** Envuelve el `Componente` del catálogo y parte de su precio base.
* **`PedidoDecorator` (Decorador Abstracto):** Mantiene una referencia `protected final Pedido pedido` y delega las operaciones.
* **Decoradores Concretos:**
  * `GarantiaExtendida`: Incrementa el valor actual acumulado en un **+10%** (`pedido.getCosto() * 1.10`).
  * `EmpaqueRegalo`: Suma **$15.000 COP** fijos (`pedido.getCosto() + 15000`).
  * `ManejoFragil`: Suma **$25.000 COP** fijos (`pedido.getCosto() + 25000`).
* **`Extra` (Enum):** Facilita indicar qué decoradores aplicar y en qué orden desde la fachada (`GARANTIA`, `EMPAQUE_REGALO`, `MANEJO_FRAGIL`).

### 3. Pasarela Internacional de Pagos (Patrón Adapter)

Conecta el contrato en español de la tienda con la librería externa norteamericana sin modificar el código fuente de `StripeUSA`.

* **`ProcesadorPago` (Target):** Interfaz local con el método `boolean cobrarEnPesos(String usuario, double montoCOP)`.
* **`StripeUSA` (Adaptee):** Clase externa con el método `boolean chargeInDollars(double amountUSD, String customerEmail, String merchantKey)`.
* **`AdaptadorStripeUSA` (Adapter):** Implementa `ProcesadorPago`, convierte los pesos colombianos a dólares (`montoUSD = montoCOP / 4000.0`), prepara el correo del cliente y la `merchantKey`, y delega el cobro a `StripeUSA.chargeInDollars(...)`.

### 4. Control de Seguridad y Auditoría (Patrón Proxy)

Actúa como sustituto de `ProcesadorPago` frente al sistema para aplicar reglas financieras antes de tocar la pasarela real.

* **`ProxySeguridadPago` (Proxy de Protección, Virtual y Auditoría):**
  * Si `usuario.equalsIgnoreCase("BLOQUEADO")`, rechaza inmediatamente la transacción y muestra la alerta en consola.
  * Si `montoCOP > 5_000_000`, rechaza inmediatamente la transacción por superar el tope máximo permitido.
  * Emplea **inicialización diferida (*Lazy Initialization*)**: el atributo `procesadorReal` permanece en `null` hasta que una transacción supera todas las validaciones, garantizando que en los rechazos **ni siquiera se instancie ni se toque** el procesador bancario real.
  * Si la transacción es aprobada, delega el cobro al `AdaptadorStripeUSA` e imprime `[AUDITORÍA: Transacción aprobada para <usuario> por un valor de $<montoCOP> COP]`.

### 5. Checkout Simplificado (Patrón Facade)

Oculta el ensamblaje de subsistemas detrás de un único punto de entrada para el cliente.

* **`ShopMasterFacade` (Fachada):** Expone `ejecutarCompraRapida(String usuario, Componente itemCatalogo, Extra... extras)`.
* **Flujo Interno:** Imprime el árbol del catálogo, crea el `PedidoBase`, envuelve dinámicamente el pedido con los decoradores según los `Extra` recibidos, muestra el subtotal y total con recargos, y ejecuta el pago a través de `ProxySeguridadPago`.

## Escenarios de Prueba en `Main`

| Caso de Prueba | Entrada Configurada | Resultado en Consola |
| :--- | :--- | :--- |
| **1. Compra Exitosa Completa** | Usuario `"cliente_vip"`, `"Combo Setup Gamer"` (`$800.000` + `$200.000` + `"Kit de Limpieza"` de `$25.000` y `$15.000` = `$1.040.000 COP`) + `GARANTIA` (+10%) + `EMPAQUE_REGALO` (+`$15.000`) | Muestra árbol de productos, total de `$1.159.000 COP`, conversión a `$289,75 USD` en `StripeUSA` y registro `[AUDITORÍA: Transacción aprobada para cliente_vip...]`. |
| **2. Rechazo por Tope de Dinero** | Usuario `"usuario_corporativo"`, `"Servidor Empresarial"` (`$6.000.000 COP`) + `MANEJO_FRAGIL` (+`$25.000` = `$6.025.000 COP`) | `ProxySeguridadPago` detecta que `$6.025.000 > $5.000.000 COP` y rechaza el cobro sin instanciar ni llamar a `StripeUSA`. |
| **3. Rechazo por Usuario Reportado** | Usuario `"BLOQUEADO"`, `"Mouse Óptico"` (`$150.000 COP`), sin extras | `ProxySeguridadPago` detecta el usuario `"BLOQUEADO"`, rechaza de inmediato sin tocar `StripeUSA` y muestra la alerta de seguridad. |
