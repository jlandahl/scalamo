# Scalamo - Scala API for DynamoDB

The Java API for DynamoDB is verbose and cumbersome, and while it's fully
usable from Scala, there is a need for a cleaner and more elegant way
to interact with DynamoDB when one is working in Scala.

This library is in its early stages, with an initial focus on automatic
typesafe mapping between Scala case classes and DynamoDB `Item` objects.
It uses [Shapeless](https://github.com/milessabin/shapeless) to achieve 
this rather than runtime reflection, so the compiler will catch any case
classes that are not mappable.

Due to the unstructured nature of DynamoDB rows, any particular mapping
could potentially fail at runtime, so the `Validated` class from 
[Cats](http://typelevel.org/cats/) is used to provide a result that is 
either a `Valid` instance of the requested case class or an `Invalid` 
result with all errors collected in a `NonEmptyList`.

All the standard types supported by the `Item` class are supported in
Scalamo. Mappers for Java 8 `ZonedDateTime` and `Instant` values are
also provided. `ZonedDateTime` values are stored as strings in ISO-8601
format and `Instant` values are stored as `Long`s.

# Example

```scala
import cats.data.Validated.{Invalid, Valid}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import java.time.Instant
import scalamo.{Table, Validated}

case class User(userId: String, name: String, lastLogin: Instant)

object Example extends App {
  val dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient())
  val table = Table[User](dynamoDB, "users")

  val users = Seq(
    User("mark-o", "Mark Corrigan", Instant.now()),
    User("jez", "Jeremy Usborne", Instant.now()),
    User("superhans", "Super Hans", Instant.now())
  )

  // batch write users
  table.put(users)

  // retrieve one user
  val maybeUser: Option[Validated[User]] = table.get("userId", "mark-o")
  maybeUser match {
    case Some(Valid(user)) =>
      table.put(user.copy(lastLogin = Instant.now()))
      println("Updated user")
    case Some(Invalid(errors)) =>
      println(s"Errors parsing user item: $errors")
    case None =>
      println("Not found")
  }

  // batch get users
  val maybeUsers: Validated[Seq[User]] = table.get("userId", Seq("jez", "superhans", "sophie"))
  for {
    users <- maybeUsers
    user <- users
  } println(user)
}
```

Note that `scalamo.Validated` is defined as:
```scala
type Validated[A] = cats.data.ValidatedNel[Throwable, A]
```
