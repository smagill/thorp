package net.kemitix.s3thorp

import scopt.OParser
import scopt.OParser.{builder,sequence, parse}
import cats.effect.IO

object ParseArgs {

  val defaultConfig = Config("def-bucket", "def-prefix", "def-source")

  val configParser: OParser[Unit, Config] = {
    val parserBuilder = builder[Config]
    import parserBuilder._
    sequence(
      programName("S3Thorp"),
      head("s3thorp", "0.1.0"),
      opt[String]('s', "source")
        .action((str, c) => c.copy(source = str))
        .text("Source directory to sync to S3"),
      opt[String]('b', "bucket")
        .action((str, c) => c.copy(bucket = str))
        .text("S3 bucket name"),
      opt[String]('p', "prefix")
        .action((str, c) => c.copy(prefix = str))
        .text("Prefix within the S3 Bucket")
    )
  }

  def apply(args: List[String]): IO[Config] =
    parse(configParser, args, defaultConfig) match {
      case Some(config) => IO.pure(config)
      case _ => IO.raiseError(new IllegalArgumentException)
    }

}
