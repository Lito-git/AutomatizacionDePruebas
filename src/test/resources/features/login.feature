Feature: Inicio de sesión
  Como usuario registrado
  Quiero iniciar sesión con mi correo y contraseña
  Para acceder a mi cuenta

  Background:
    Given que existe el usuario "ana@iplacex.cl" con contraseña "Clave123" y nombre "Ana"

  Scenario: Inicio de sesión exitoso con credenciales válidas
    When ingreso el correo "ana@iplacex.cl" y la contraseña "Clave123"
    Then debería ver el mensaje "Bienvenido, Ana"

  Scenario Outline: Inicio de sesión rechazado
    When ingreso el correo "<correo>" y la contraseña "<contrasena>"
    Then debería ver el mensaje "<mensaje>"

    Examples:
      | correo          | contrasena | mensaje                           |
      | ana@iplacex.cl  | ClaveMala  | Credenciales inválidas            |
      | otro@iplacex.cl | Clave123   | Credenciales inválidas            |
      |                 | Clave123   | Debe ingresar correo y contraseña |
      | ana@iplacex.cl  |            | Debe ingresar correo y contraseña |