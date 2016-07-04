package scalamo

package object jcache {
  import com.amazonaws.services.dynamodbv2.document.PrimaryKey

  type KeyMapper[K] = K => PrimaryKey
}
