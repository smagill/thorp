package net.kemitix.s3thorp

import cats.effect.IO
import net.kemitix.s3thorp.Sync.{Bucket, LastModified, LocalFile, MD5Hash, RemoteKey}
import net.kemitix.s3thorp.awssdk.S3Client

trait DummyS3Client extends S3Client {

  override def objectHead(bucket: Bucket, remoteKey: RemoteKey): IO[Option[(MD5Hash, LastModified)]] = ???

  override def upload(localFile: LocalFile, bucket: Bucket, remoteKey: RemoteKey): IO[Either[Throwable, MD5Hash]] = ???
}
