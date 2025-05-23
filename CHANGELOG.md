0.1.5
=====

- Avoid file mismatches by matching on storageIdentifier rather than relying on order in array. See https://github.com/gdcc/exporter-croissant/pull/17
- Include "extract column" object per field, suggest testing with https://huggingface.co/spaces/JoaquinVanschoren/croissant-checker . See https://github.com/gdcc/exporter-croissant/pull/16
- Replace "text/tsv" (invalid) with "text/tab-separated-values" (valid). See https://github.com/gdcc/exporter-croissant/pull/18
- Upgrade to mlcroissant 1.0.17 in validation script. See https://github.com/gdcc/exporter-croissant/pull/15

0.1.4
=====

- Handle drafts. See https://github.com/gdcc/exporter-croissant/pull/14
- Switch to parent pom. See https://github.com/gdcc/exporter-croissant/issues/5
- Tweaked spotless (code formatting) config. See https://github.com/gdcc/exporter-croissant/pull/12

0.1.3
=====

- Strings in the following fields are escaped using HTML entities: title, filename, file description, file format, variable name, variable description. See https://github.com/gdcc/exporter-croissant/pull/8

0.1.2
=====

- Fixed a bug where `field` was being repeated over and over. See https://github.com/gdcc/exporter-croissant/issues/1 and https://github.com/gdcc/exporter-croissant/pull/2
- Fixed a bug where `sc:Integer` was being used for all numeric types. `sc:Float` is now supported as well. See https://github.com/gdcc/exporter-croissant/issues/3 and https://github.com/gdcc/exporter-croissant/pull/4

0.1.1
=====

Initial release.
