0.1.3
=====

- Changed media type from `application/json` to `application/ld+json; profile="http://mlcommons.org/croissant/1.0"` to be more specific. See https://github.com/gdcc/exporter-croissant/issues/6 and https://github.com/gdcc/exporter-croissant/pull/7

0.1.2
=====

- Fixed a bug where `field` was being repeated over and over. See https://github.com/gdcc/exporter-croissant/issues/1 and https://github.com/gdcc/exporter-croissant/pull/2
- Fixed a bug where `sc:Integer` was being used for all numeric types. `sc:Float` is now supported as well. See https://github.com/gdcc/exporter-croissant/issues/3 and https://github.com/gdcc/exporter-croissant/pull/4

0.1.1
=====

Initial release.
