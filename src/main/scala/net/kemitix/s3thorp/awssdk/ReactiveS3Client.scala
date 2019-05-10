package net.kemitix.s3thorp.awssdk

import cats.effect.IO
import net.kemitix.s3thorp.Main.putStrLn
import net.kemitix.s3thorp.Sync.{Bucket, LocalFile, RemoteKey}
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.services.s3.model.{HeadObjectRequest, HeadObjectResponse, PutObjectRequest}

private class ReactiveS3Client
  extends S3Client
    with CatsIOS3Client {

  override def objectHead(bucket: Bucket, remoteKey: RemoteKey) = {
    val request = HeadObjectRequest.builder()
      .bucket(bucket)
      .key(remoteKey)
      .build()
    println(s"S3:HeadObject: $bucket : $remoteKey")
    s3Client.headObject(request).attempt.map {
      case Right(response) => {
        println(s"  -- ${response.eTag()} : ${response.lastModified()}")
        Some((response.eTag(), response.lastModified()))
      }
      case Left(_) => {
        println("  -- Not found in S3")
        None
      }
    }
  }

  override def upload(localFile: LocalFile, bucket: Bucket, remoteKey: RemoteKey): IO[Unit] = {
    val request = PutObjectRequest.builder()
      .bucket(bucket)
      .key(remoteKey)
      .build()
    val body = AsyncRequestBody.fromFile(localFile)
    for {
      _ <- putStrLn(s"S3:PutObject: $bucket : $remoteKey")
      _ <- s3Client.putObject(request, body)
      _ <- putStrLn("  -- Done")
    } yield ()
  }
}
