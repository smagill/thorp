package net.kemitix.s3thorp.awssdk

import cats.effect.IO
import com.github.j5ik2o.reactive.aws.s3.S3AsyncClient
import com.github.j5ik2o.reactive.aws.s3.cats.S3CatsIOClient
import net.kemitix.s3thorp.Main.putStrLn
import net.kemitix.s3thorp.Sync.{Bucket, LocalFile, RemoteKey}
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.services.s3.model.{HeadObjectRequest, NoSuchKeyException, PutObjectRequest}
import software.amazon.awssdk.services.s3.{S3AsyncClient => JavaS3AsyncClient}

private class ReactiveS3Client extends S3Client {

  private val s3Client = S3CatsIOClient(S3AsyncClient(JavaS3AsyncClient.create))

  override def objectHead(bucket: Bucket, remoteKey: RemoteKey) = {
    val request = HeadObjectRequest.builder()
      .bucket(bucket)
      .key(remoteKey)
      .build()
    try {
      for {
        _ <- putStrLn(s"S3:HeadObject: $bucket : $remoteKey")
        response <- s3Client.headObject(request)
        _ <- putStrLn(s"  -- ${response.eTag()} : ${response.lastModified()}")
      } yield Some((response.eTag(), response.lastModified()))
    } catch {
      case _: NoSuchKeyException => IO(None)
    }
  }

  override def upload(localFile: LocalFile, bucket: Bucket, remoteKey: RemoteKey): IO[Unit] = {
    val request = PutObjectRequest.builder()
      .bucket(bucket)
      .key(remoteKey)
      .build()
    val body = AsyncRequestBody.fromFile(localFile)
    try {
      for {
        _ <- putStrLn(s"S3:PutObject: $bucket : $remoteKey")
        _ <- s3Client.putObject(request, body)
        _ <- putStrLn("  -- Done")
      } yield ()
    }
  }
}
