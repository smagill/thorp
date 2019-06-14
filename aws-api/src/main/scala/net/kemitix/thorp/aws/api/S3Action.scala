package net.kemitix.thorp.aws.api

import net.kemitix.thorp.domain.{MD5Hash, RemoteKey}

sealed trait S3Action {

  // the remote key that was uploaded, deleted or otherwise updated by the action
  def remoteKey: RemoteKey

  val order: Int

}

object S3Action {

  final case class DoNothingS3Action(remoteKey: RemoteKey) extends S3Action {
    override val order: Int = 0
  }

  final case class CopyS3Action(remoteKey: RemoteKey) extends S3Action {
    override val order: Int = 1
  }

  final case class UploadS3Action(
                                   remoteKey: RemoteKey,
                                   md5Hash: MD5Hash) extends S3Action {
    override val order: Int = 2
  }

  final case class DeleteS3Action(remoteKey: RemoteKey) extends S3Action {
    override val order: Int = 3
  }

  final case class ErroredS3Action(remoteKey: RemoteKey, e: Throwable) extends S3Action {
    override val order: Int = 10
  }

  implicit def ord[A <: S3Action]: Ordering[A] = Ordering.by(_.order)

}