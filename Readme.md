# Scalamo - Scala API for DynamoDB

The Java API for DynamoDB is verbose and cumbersome, and while it's fully
usable from Scala, there is a need for a cleaner and more elegant way
to interact with DynamoDB when one is working in Scala.

This library is in its early stages, with an initial focus on automatic
typesafe mapping between Scala case classes and DynamoDB `Item` objects.
It uses Shapeless to achieve this rather than runtime reflection, so
you know at compile time whether a case class is mappable or not. At
runtime any particuarly `Item` could fail to convert due to the 
unstructured nature of DynamoDB rows. To handle problems at runtime,
the `Validated` class from Cats is used to provide a result that is
either a `Valid` instance of the requested case class, or an `Invalid`
result with all errors collected in a `NonEmptyList`.

All the standard types supported by the `Item` class are supported in
Scalamo. Mappers for Java 8 `ZonedDateTime` and `Instant` values are
also provided.

# Example

```scala
import scalamo._
import java.time.Instant

case class User(userId: String, name: String, lastLogin: Instant)

val dynamoDB = ???
val table = dynamoDB.getTable("users")
val user = table.get[User]("userId", "12345")

table.put(user.copy(lastLogin = Instant.now()))
```
