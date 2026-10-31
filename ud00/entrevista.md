# Entrevista inicial — AD

Esta actividad es formativa y no lleva nota: sirve para conocer tu punto de partida. Edita este archivo y escribe tus respuestas debajo de cada pregunta; no crees otro documento.

**Responde sin consultar documentación, Internet ni IA.** Basta una respuesta corta. Si no lo sabes, escribe «No lo sé»; si tienes una idea pero dudas, explícala.

## Programación

### 1. ¿Qué lenguajes de programación has utilizado?

**Respuesta:**

### 2. Identifica el lenguaje de cada fragmento e indica qué valor muestra. Por ejemplo: «1. Java; valor: 9».

**1**

```java
int total = 0;
for (int n : new int[]{2, 3, 4}) total += n;
System.out.println(total);
```

Lenguaje:  
Valor mostrado:

**2**

```python
total = 0
for n in [2, 3, 4]:
    total += n
print(total)
```

Lenguaje:  
Valor mostrado:

**3**

```javascript
let total = 0;
for (const n of [2, 3, 4]) total += n;
console.log(total);
```

Lenguaje:  
Valor mostrado:

### 3. ¿Qué muestra este programa? Explica brevemente qué calcula la función.

```python
def resumir(numeros):
    total = 0
    for numero in numeros:
        if numero > 0:
            total += numero
    return total

print(resumir([3, -2, 5]))
```

**Respuesta:**

## Bases de datos

### 4. ¿Qué condiciones debe cumplir un cliente para aparecer en el resultado? ¿Cómo se ordenan los resultados?

```sql
SELECT c.nombre, COUNT(p.id) AS total_pedidos
FROM clientes AS c
JOIN pedidos AS p ON p.cliente_id = c.id
GROUP BY c.id, c.nombre
HAVING COUNT(p.id) > 1
ORDER BY total_pedidos DESC;
```

**Respuesta:**

### 5. ¿Para qué sirve una clave primaria en una tabla? Explícalo con tus propias palabras.

**Respuesta:**

### 6. En una base de datos, ¿qué relación suele existir entre un cliente y sus pedidos?

**Respuesta:**

### 7. ¿Qué diferencia básica hay entre guardar datos en tablas relacionadas y guardarlos como documentos JSON?

**Respuesta:**

### 8. ¿Has creado o consultado alguna base de datos? ¿Cuál?

**Respuesta:**

### 9. ¿Has trabajado (o en clase) conectando una aplicación a un almacén de datos? ¿Cuál y cómo? Descríbelo brevemente.

**Respuesta:**

## Git

### 10. ¿Habías utilizado Git antes de estas prácticas? ¿Para qué? Valora tu manejo del 1 al 10.

**1:** primer contacto; **10:** uso habitual y con soltura.

**Respuesta:**

## Presentación

Cuando hayas respondido, revisa los cambios, crea tus commits y publícalos en tu repositorio privado. Cuando quieras presentar esta preparación, crea y publica la etiqueta `practica-ad-00-v1`. No necesitas adjuntar este archivo ni capturas en Moodle.
