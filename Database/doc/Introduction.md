# An encrypted database based on HSQL

We took the powerful **HSQL** and add encryption on its working files to add security.

At database creation, a login and a password are defined. 
They are used for create an encryption key.
This encryption key is used for encrypt **HSQL** files.

To open the database later, have to use the same login and password.

To simplify database manipulation we add over **HSQL** some objects and used **Kotlin DSL** to define a syntax.

This documentation will explains each query **DSL** syntax.

[Menu](Menu.md)
