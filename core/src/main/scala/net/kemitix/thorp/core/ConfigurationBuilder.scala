package net.kemitix.thorp.core

import java.io.File
import java.nio.file.Paths

import cats.data.NonEmptyChain
import cats.effect.IO
import net.kemitix.thorp.core.ConfigValidator.validateConfig
import net.kemitix.thorp.core.ParseConfigFile.parseFile
import net.kemitix.thorp.domain.Config

/**
  * Builds a configuration from settings in a file within the
  * `source` directory and from supplied configuration options.
  */
trait ConfigurationBuilder {

  private val pwdFile: File = Paths.get(System.getenv("PWD")).toFile

  private val defaultConfig: Config = Config(source = pwdFile)

  def buildConfig(priorityOptions: Seq[ConfigOption]): IO[Either[NonEmptyChain[ConfigValidation], Config]] = {
    val source = findSource(priorityOptions)
    for {
      sourceOptions <- sourceOptions(source)
      userOptions <- userOptions()
      globalOptions <- globalOptions()
      collected = priorityOptions ++ sourceOptions ++ userOptions ++ globalOptions
      config = collateOptions(collected)
    } yield validateConfig(config).toEither
  }

  private def findSource(priorityOptions: Seq[ConfigOption]): File =
    priorityOptions.foldRight(pwdFile)((co, f) => co match {
      case ConfigOption.Source(source) => source.toFile
      case _ => f
    })

  private def sourceOptions(source: File): IO[Seq[ConfigOption]] =
    readFile(source, ".thorp.conf")

  private def userOptions(): IO[Seq[ConfigOption]] =
    readFile(userHome, ".config/thorp.conf")

  private def globalOptions(): IO[Seq[ConfigOption]] =
    parseFile(Paths.get("/etc/thorp.conf"))

  private def userHome = new File(System.getProperty("user.home"))

  private def readFile(source: File, filename: String): IO[Seq[ConfigOption]] =
    parseFile(source.toPath.resolve(filename))

  private def collateOptions(configOptions: Seq[ConfigOption]): Config =
    configOptions.foldRight(defaultConfig)((co, c) => co.update(c))
}

object ConfigurationBuilder extends ConfigurationBuilder