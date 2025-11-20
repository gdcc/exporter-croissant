#!/bin/sh
# We are aware of warnings for the "version" field. See the README.
# Check datasets with this tool as well: https://huggingface.co/spaces/JoaquinVanschoren/croissant-checker
for i in src/test/resources/*/out/croissant.json; do
  echo testing $i
  mlcroissant validate --jsonld $i
done
