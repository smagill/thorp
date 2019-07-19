package net.kemitix.thorp.core

import java.nio.file.Path

import net.kemitix.thorp.domain
import net.kemitix.thorp.domain.{Config, RemoteKey}
import net.kemitix.thorp.domain.Config._

sealed trait ConfigOption {
  def update(config: Config): Config
}

object ConfigOption {

  case class Source(path: Path) extends ConfigOption {
    override def update(config: Config): Config =
      sources.modify(_ ++ path)(config)
  }

  case class Bucket(name: String) extends ConfigOption {
    override def update(config: Config): Config =
      if (config.bucket.name.isEmpty)
        bucket.set(domain.Bucket(name))(config)
      else
        config
  }

  case class Prefix(path: String) extends ConfigOption {
    override def update(config: Config): Config =
      if (config.prefix.key.isEmpty)
        prefix.set(RemoteKey(path))(config)
      else
        config
  }

  case class Include(pattern: String) extends ConfigOption {
    override def update(config: Config): Config =
      filters.modify(domain.Filter.Include(pattern) :: _)(config)
  }

  case class Exclude(pattern: String) extends ConfigOption {
    override def update(config: Config): Config =
      filters.modify(domain.Filter.Exclude(pattern) :: _)(config)
  }

  case class Debug() extends ConfigOption {
    override def update(config: Config): Config =
      debug.set(true)(config)
  }

  case object Version extends ConfigOption {
    override def update(config: Config): Config = config
  }

  case object BatchMode extends ConfigOption {
    override def update(config: Config): Config =
      batchMode.set(true)(config)
  }

  case object IgnoreUserOptions extends ConfigOption {
    override def update(config: Config): Config = config
  }

  case object IgnoreGlobalOptions extends ConfigOption {
    override def update(config: Config): Config = config
  }

}
