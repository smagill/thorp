package net.kemitix.s3thorp.awssdk

import com.github.j5ik2o.reactive.aws.s3.cats.S3CatsIOClient

trait CatsIOS3Client extends S3CatsIOClientProvider {

  def s3Client = S3CatsIOClient(underlying)

}
