ENLACE AL REPOSITORIO DEL PROYECTO: https://github.com/JuanSalvadorFructuosoCampoy/proyectoSpringCloud

-----------------------------------------------------------------------------

ACCESO A LAS BASES DE DATOS H2:
Procedimientos: http://localhost:8081/h2-console
Intervinientes: http://localhost:8082/h2-console

Credenciales de acceso en ambas bases de datos:

Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:testdb
User Name: Juansa
Password:

-----------------------------------------------------------------------------

PROCEDIMIENTOS:
Consumo de la API Procedimientos en Postman:

GET: http://localhost:8001

GET de un procedimiento en específico: http://localhost:8001/{id}

POST: http://localhost:8001
Body:
{
"numeroProcedimiento": "PR0010",
"anno": 2021
}

PUT: http://localhost:8001/{id}
Body:
{
"numeroProcedimiento": "PR0006",
"anno": 2021
}

DELETE: http://localhost:8001/{id}

-----------------------------------------------------------------------------

INTERVINIENTES:
Consumo de la API Intervinientes en Postman:

GET: http://localhost:8002
GET de un interviniente en específico: http://localhost:8002/{id}

POST: http://localhost:8002
Body:
{
"nombre": "Juan Salvador",
"tipoIntervencion": "Tipo Nuevo"
}

PUT: http://localhost:8002/{id}
Body:
{
"nombre": "Antonio Campillo",
"tipoIntervencion": "Tipo Nuevo"
}

DELETE:  http://localhost:8002/{id}

-----------------------------------------------------------------------------

COMUNICACIÓN ENTRE MICROSERVICIOS:
Consumo de la API para gestionar intervinientes desde Procedimientos:

VER INTERVINIENTES DE UN PROCEDIMIENTO:
Solicitud GET
URL (ejemplo): http://localhost:8001/1

ASOCIAR UN INTERVINIENTE YA EXISTENTE A UN PROCEDIMIENTO:
Solicitud PUT
URL (ejemplo): http://localhost:8001/asignar-int/1?interId=5
El interviniente de ID 5 pasa a unirse al procedimiento de ID 1

CREAR NUEVO INTERVINIENTE Y ASOCIARLO A UN PROCEDIMIENTO:
Solicitud POST
URL (ejemplo): http://localhost:8001/crear-int/1
Body:
{
"nombre": "Andrés Antonio Martínez",
"tipoIntervencion": "Tipo 5"
}
El interviniente se crea y se asocia al procedimiento de ID 1

BORRAR UN INTERVINIENTE DE UN PROCEDIMIENTO PERO SIN BORRAR AL INTERVINIENTE:
Solicitud DELETE
URL (ejemplo): http://localhost:8001/eliminar-int/1?interId=2
El interviniente se elimina de los intervinientes del procedimiento 1 y 
pasa a tener procedimiento_id = null, pero sigue existiendo en la base de datos.
