#!/bin/sh
# We are aware of warnings for the "version" field. See the README.
mlcroissant validate --jsonld src/test/resources/minimal/out/croissant.json
mlcroissant validate --jsonld src/test/resources/max/out/croissant.json
mlcroissant validate --jsonld src/test/resources/cars/out/croissant.json
