# Croissant Exporter for Dataverse

[Croissant][] is a high-level format for machine learning datasets. See the [Croissant writeup] in the Dataverse guides for how this format is used in Dataverse and how to enable it.

[Croissant]: https://github.com/mlcommons/croissant
[Croissant writeup]: https://preview.guides.gdcc.io/en/develop/admin/discoverability.html#schema-org-json-ld-croissant-metadata

## Installation

Please note: the Croissant exporter works best with Dataverse 6.2 and higher (where it updates the content of `<head>` as described in the [guides](https://preview.guides.gdcc.io/en/develop/admin/discoverability.html#schema-org-json-ld-croissant-metadata)) but can be used with 6.0 and higher (to get the export functionality).

1. First, download the Croissant jar file from Maven Central. Most likely, you will want the latest released version, which you can find at <https://repo1.maven.org/maven2/io/gdcc/export/croissant/>. (Unreleased/snapshot versions are [available](https://s01.oss.sonatype.org/content/groups/staging/io/gdcc/export/croissant/) but not recommended for production use.)

1. Place the jar in the directory you specified for the [dataverse.spi.exporters.directory](https://guides.dataverse.org/en/latest/installation/config.html#dataverse-spi-exporters-directory) setting.

1. Restart Payara.

1. On published datasets, click "Metadata" and then "Export Metadata". You should see a "Croissant" option in the drop down.

## Changelog

See [CHANGELOG.md](CHANGELOG.md).

## Open questions

Croissant is a new format and there are a number of open questions about it. (Developers may find additional open question in the code flagged with "TODO" or similar.)

### Can ingested files have multiple URLs to download various formats?

We opened https://github.com/mlcommons/croissant/issues/641 about this. Perhaps there should be a FileObject for each format offered?

### What if the Croissant file is huge, due to many files (distribution) and columns (recordSet)?

Good question. We already have a similar problem with our Schema.org JSON-LD format. We opened https://github.com/mlcommons/croissant/issues/646 about it.

For a [test dataset][] with 10,000 files (images), the Schema.org file is 1.6 MB and the Croissant file is 2.5 MB.

For a [large dataset][] with 25,310 files (images), the Schema.org file is 4.4 MB and the Croissant file is 7.1 MB.

Datasets with many columns from ingested files are another potential problem.

Google's [guidelines on structured data][] don't indicate any size limit but we should take care not to make pages too heavy.

[test dataset]: https://github.com/IQSS/dataverse-sample-data/pull/42
[large dataset]: https://dataverse.harvard.edu/dataset.xhtml?persistentId=doi:10.7910/DVN/3CTMKP
[guidelines on structured data]: https://developers.google.com/search/docs/appearance/structured-data/sd-policies

### Can summary statistics go into Croissant?

No, but we opened https://github.com/mlcommons/croissant/issues/640 about this. See also "potential areas of work" in the [Croissant Task Force Minutes] for 2024-04-01.

## Developer documentation

The rest of this documentation is for developers of the Dataverse Croissant exporter, including how to build and test it.

### The Croissant spec and task force

You can find the Croissant [spec] on the [Croissant] website. Consider joining the Croissant Task Force to ask questions (see [Croissant Task Force Minutes] for previous meetings). Don't be shy about opening issues at https://github.com/mlcommons/croissant/issues

[spec]: https://mlcommons.github.io/croissant/docs/croissant-spec.html
[Croissant Task Force Minutes]: https://docs.google.com/document/d/1OINP9AmphhAa3J9sw87QtpY9Hb4_Ym9Zxf1_ycF1_04/edit?usp=sharing

### To run tests

```
mvn test
```

### To validate

```
./validate.sh
```

Note that [mlcroissant][] is changing! Sometimes in backward-incompatible ways! If you upgrade, you may see an error like this:

```
WARNING: The JSON-LD `@context` is not standard. Refer to the official @context (e.g., from the example datasets in https://github.com/mlcommons/croissant/tree/main/datasets/1.0).
```

As the error indicates, go to https://github.com/mlcommons/croissant/tree/main/datasets/1.0 and find a dataset such as "titanic" and copy the latest `@context` from there. Note that this may be different (but more up to date) than the version in the spec at https://mlcommons.github.io/croissant/docs/croissant-spec.html#appendix-1-json-ld-context

In short, we are trusting the output of `mlcroissant validate --jsonld` over the spec.

For additional validation you can use https://search.google.com/test/rich-results and https://validator.schema.org which are linked from https://developers.google.com/search/docs/appearance/structured-data

Finally, there is a tool at https://huggingface.co/spaces/JoaquinVanschoren/croissant-checker that is used for datasets submitted to the NeurIPS conference.

[mlcroissant]: https://pypi.org/project/mlcroissant/

Note that https://validator.schema.org cannot be used for validation because of the Croissant-specific extensions the spec uses. You should expect to see errors like "http://mlcommons.org/croissant/FileObject is not a known valid target type for the distribution property".

### To update expected JSON

As a convenience, you can run `mvn test` and then `update-expected.sh` after making code changes to update the expected Croissant ouput in the tests with new output.

### To format code

```
mvn spotless::apply
```

## To check the pom.xml file


```
mvn pomchecker:check-maven-central
```

### To build the Croissant jar

```
mvn package
```

### To use the Croissant jar in Docker

Under "environment" in the compose file, add the following.

```
DATAVERSE_SPI_EXPORTERS_DIRECTORY: "/dv/exporters"
```

Then create an `exporters` directory and copy the jar into it (dev example shown).

```
mkdir docker-dev-volumes/app/data/exporters
cp target/*jar docker-dev-volumes/app/data/exporters
```

Then stop and start the containers. On a dataset, click "Metadata" then "Export Metadata" and in the dropdown you should see "Croissant" listed.

If you update the jar but not the dataset and want to see the changes, you can reexport all datasets or a specific dataset per https://guides.dataverse.org/en/6.2/admin/metadataexport.html#batch-exports-through-the-api

```
curl http://localhost:8080/api/admin/metadata/reExportAll
curl http://localhost:8080/api/admin/metadata/:persistentId/reExportDataset?persistentId=doi:10.5072/FK2/DZRHUP
```

### To use the Croissant jar in non-Docker

Same as above but use a JVM option in domain.xml such as the example below.

```
<jvm-options>-Ddataverse.spi.exporters.directory=/home/dataverse/dataverse-exporters/croissant/target</jvm-options>
```
### Differences from Kaggle

- I see an `encodingFormat` of `text/comma-separated-values`. Kind of curious about that since I think `text/csv` is more the MIME type that's on https://www.iana.org/assignments/media-types/media-types.xhtml and https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types . See https://github.com/IQSS/dataverse/issues/4943#issuecomment-2145333830

### Differences from pyDataverse

A pyDataverse implementation is underway at https://github.com/Dans-labs/pyDataverse/tree/semantic-mappings and it has the following differences compared to the implementation in this dataverse-exporters repo:

- Under `@context`:
  - Missing: `"wd": "https://www.wikidata.org/wiki/"`
- Missing: `citation` (Related Publication).
- `citeAs` shows title rather than full BibTex.
- under `creator`:
  - under `@type`, `sc:Person` is used instead of `Person`.
  - Missing: `@id`. Should be an ORCID id when available (e.g. `"@id": "https://orcid.org/0000-0002-9528-9470"`).
  - Missing: `@identifier`. Should be an ORCID id when available.
  - Missing: `@sameAs`. Should be an ORCID id when available.
  - Missing: `affiliation`.
  - Missing: `familyName`.
  - Missing: `givenName`.
- Missing: `dateModified`. Should be YYYY-MM-DD.
- `datePublished` is YYYY-MM-DD HH:MM:SS. Should be YYYY-MM-DD.
- Under `distribution`:
    - For files, for `@id`, the database id (e.g. `f26148`) is used instead of the filename (e.g. `README.md`) for path plus filename (e.g. `doc/README.md`).
    - `field` is repeated over and over. See https://github.com/gdcc/exporter-croissant/issues/1 and https://github.com/gdcc/exporter-croissant/pull/2
- Missing: `funder`
- Missing: `includedInDataCatalog`
- `name` inserts underscores for spaces (e.g. "Max Schema.org" becomes "Max_Schema.org")
- Missing: `publisher`. Expected is something like `{"@type": "Organization","name": "Root"}`.
- Missing: `spatialCoverage`. Expected is an array with something like `"Cambridge, MA, United States, Harvard Square"`.
- Missing: `temporalCoverage`. Expected is an array with something like `"2023-01-01/2023-12-31"`.
- `version` shows the version of Dataverse (`6.2 build develop-28a9d44.0`) rather than the version of the dataset.
- `dateCreated` is present.

### Difference from Schema.org JSON-LD

Dataverse's Schema.org implementation does the following:

- `@context` is much more minimal, only showing "http://schema.org".
- Type is `sc:Dataset` instead of `cr:Dataset` (Croissant-specific).
- No `conformsTo` (`"conformsTo": "http://mlcommons.org/croissant/1.0"` is Croissant-specific).
- No `citeAs` (Croissant-specific).
- No `recordSet` (Croissant-specific).
- No `url`
- For `version`, "1" is used instead of "1.0".
- Under `distribution` the type is `DataDownload` instead of `cr:FileObject` (Croissant-specific).
- Under `distribution` there is no `@id`.
- Under `distribution` there is no `md5`.
- Under `distribution`, `contentSize` is a number rather than a string.
- Has the following (but our Croissant doesn't):
  - `@id`
  - `identifier`
  - `author` (duplicate of `creator`)
  - `provider` (duplicate of `publisher`)

### Changes under consideration

- Append " B" to `contentSize` strings to clarify that the implied unit is bytes. The examples in the spec and on GitHub do this.
