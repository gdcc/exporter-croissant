# Croissant Exporter for Dataverse

[Croissant][] is a high-level format for machine learning datasets. See the [Croissant writeup] in the guides for how this format is used in Dataverse and how to enable it.

[Croissant]: https://github.com/mlcommons/croissant
[Croissant writeup]: https://dataverse-guide--10533.org.readthedocs.build/en/10533/installation/advanced.html#croissant-croissant

## Known issues

### The "version" field triggers a validation warning.

For the "version" field, we are providing normal, Dataverse-style versions such as "1.0" and "2.1". This triggers a warning from the Croissant validator, but we are working with the Croissant spec authors to correct this in the following issues:

- 1.0 as a string should be a valid version for a dataset: https://github.com/mlcommons/croissant/issues/609
- add flag to validator to ignore certain warnings: https://github.com/mlcommons/croissant/issues/643

The [versioning section][] of Croissant spec treats any string as valid but recommends MAJOR.MINOR.PATCH (Semantic Versioning). However, values like "1.0.0" are unusable when provided to both the Datavese API and UI:

- API: If you try to pass "1.0.0" to the API like https://dataverse.harvard.edu/api/datasets/:persistentId/versions/1.0.0?persistentId=doi:10.7910/DVN/TJCLKP you will get an error: "Illegal version identifier '1.0.0'".

- UI: If you try to pass "1.0.0" to a dataset like https://dataverse.harvard.edu/dataset.xhtml?persistentId=doi:10.7910/DVN/TJCLKP&version=1.0.0 and the dataset has multiple versions, you will be directed to the latest version (4.0 as of this writing), rather version 1.0 as you expect.

[versioning section]: https://mlcommons.github.io/croissant/docs/croissant-spec.html#dataset-versioningcheckpoints

For the reasons above, we are providing MAJOR.MINOR (e.g. "1.0") as the version.

We are using a string type for the "version" field in case we ever want to change Dataverse in the future to support MAJOR.MINOR.PATCH versions.

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

Note: We are aware of warnings for the "version" field. See above.

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

[mlcroissant]: https://pypi.org/project/mlcroissant/

Note that https://validator.schema.org cannot be used for validation because of the Croissant-specific extensions the spec uses. You should expect to see errors like "http://mlcommons.org/croissant/FileObject is not a known valid target type for the distribution property".

### To update expected JSON

As a convenience, you can run `mvn test` and then `update-expected.sh` after making code changes to update the expected Croissant ouput in the tests with new output.

### To format code

```
spotless::apply
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
- Missing: `funder`
- Missing: `includedInDataCatalog`
- `name` inserts underscores for spaces (e.g. "Max Schema.org" becomes "Max_Schema.org")
- Missing: `publisher`. Expected is something like `{"@type": "Organization","name": "Root"}`.
- Missing: `spatialCoverage`. Expected is an array with something like `"Cambridge, MA, United States, Harvard Square"`.
- Missing: `temportalCoverage`. Expected is an array with something like `"2023-01-01/2023-12-31"`.
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
